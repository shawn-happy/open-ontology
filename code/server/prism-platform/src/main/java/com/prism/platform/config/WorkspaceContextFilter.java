package com.prism.platform.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prism.common.context.RequestContextHolder;
import com.prism.common.exception.BusinessException;
import com.prism.common.result.Result;
import com.prism.common.security.SecurityUtils;
import com.prism.platform.service.WorkspaceAccessService;
import com.prism.platform.service.support.WorkspaceActivityTracker;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 空间上下文过滤器
 * <p>
 * 匹配空间级 URL（/web/workspace/{code}/...）：解析空间编码 → 校验空间有效性与访问权限
 * → 注入 RequestContextHolder.workspaceCode（供 MyBatis-Plus 租户拦截器使用）→ finally 清理。
 * <p>
 * 执行顺序：Security 过滤器链（order=-100，认证完成）→ RequestIdFilter（@Order(1)）→ 本过滤器（@Order(2)）。
 * 校验失败直接写 JSON 响应（HTTP 200 + 业务码 020401/020402），不进入后续链路。
 */
@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class WorkspaceContextFilter extends OncePerRequestFilter {

    /** 空间级 URL 匹配（编码为大写字母开头 + 大写字母/数字/中划线/下划线，保留字 WORKSPACE 不会出现于业务路由） */
    private static final Pattern WORKSPACE_PATH_PATTERN =
            Pattern.compile("^/web/workspace/([A-Z][A-Z0-9_-]{2,63})(/.*)?$");

    private final WorkspaceAccessService workspaceAccessService;
    private final WorkspaceActivityTracker workspaceActivityTracker;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Matcher matcher = WORKSPACE_PATH_PATTERN.matcher(request.getRequestURI());
        if (!matcher.matches()) {
            filterChain.doFilter(request, response);
            return;
        }
        String workspaceCode = matcher.group(1);
        try {
            workspaceAccessService.checkAccess(workspaceCode, SecurityUtils.getCurrentUserId());
            RequestContextHolder.setWorkspaceCode(workspaceCode);
            // 当日活跃标记（SETNX 24h，容错降级；每日定时回写 last_active_at）
            workspaceActivityTracker.markActive(workspaceCode);
            filterChain.doFilter(request, response);
        }
        catch (BusinessException e) {
            log.warn("空间上下文校验失败：uri={}, code={}, bizCode={}", request.getRequestURI(), workspaceCode,
                    e.getCode());
            writeError(response, e.getCode(), e.getMessage());
        }
        finally {
            // 仅清理 workspaceCode（requestId/userId 由 RequestIdFilter finally 统一 clear）
            RequestContextHolder.clearWorkspace();
        }
    }

    private void writeError(HttpServletResponse response, String code, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(Result.fail(code, message)));
    }
}
