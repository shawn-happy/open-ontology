package com.prism.platform.aspect;

import com.prism.common.context.RequestContextHolder;
import com.prism.common.enums.WorkspaceRole;
import com.prism.common.exception.BusinessException;
import com.prism.common.security.SecurityUtils;
import com.prism.platform.constants.SystemConstants;
import com.prism.platform.entity.SystemRole;
import com.prism.platform.exception.WorkspaceErrorCode;
import com.prism.platform.mapper.SystemRoleMapper;
import com.prism.platform.security.annotation.RequireWorkspaceRole;
import com.prism.platform.service.WorkspaceMemberService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 空间角色鉴权切面
 * <p>
 * 拦截 @RequireWorkspaceRole：
 * ① 内置超管（userId=1）放行；② 无空间上下文抛 020401；③ super_admin 角色放行；
 * ④ 取用户有效空间角色（含缓存），等级不足抛 020403。
 * <p>
 * 前置条件：WorkspaceContextFilter（@Order(2)）已完成空间有效性与访问权校验并注入 workspaceCode。
 */
@Aspect
@Component
@RequiredArgsConstructor
public class WorkspaceRoleAspect {

    private final WorkspaceMemberService workspaceMemberService;
    private final SystemRoleMapper systemRoleMapper;

    @Around("@annotation(requireWorkspaceRole)")
    public Object checkWorkspaceRole(ProceedingJoinPoint joinPoint, RequireWorkspaceRole requireWorkspaceRole)
            throws Throwable {
        Long userId = SecurityUtils.getCurrentUserId();

        // ① 内置超管放行
        if (userId != null && SystemConstants.ADMIN_USER_ID.equals(userId)) {
            return joinPoint.proceed();
        }

        // ② 空间上下文缺失（未经 Filter 注入，属服务端路由配置错误）
        String workspaceCode = RequestContextHolder.getWorkspaceCode();
        if (workspaceCode == null) {
            throw new BusinessException(WorkspaceErrorCode.WORKSPACE_CONTEXT_INVALID.getCode(),
                    WorkspaceErrorCode.WORKSPACE_CONTEXT_INVALID.getMessage());
        }

        // ③ super_admin 角色绕过空间角色矩阵
        if (hasSuperAdminRole(userId)) {
            return joinPoint.proceed();
        }

        // ④ 有效空间角色等级校验
        String effectiveRole = workspaceMemberService.getEffectiveRole(workspaceCode, userId);
        int level = effectiveRole != null ? WorkspaceRole.levelOf(effectiveRole) : 0;
        WorkspaceRole required = requireWorkspaceRole.value();
        if (level < required.getLevel()) {
            throw new BusinessException(WorkspaceErrorCode.WORKSPACE_ROLE_INSUFFICIENT.getCode(),
                    WorkspaceErrorCode.WORKSPACE_ROLE_INSUFFICIENT.getMessage());
        }
        return joinPoint.proceed();
    }

    private boolean hasSuperAdminRole(Long userId) {
        if (userId == null) {
            return false;
        }
        List<SystemRole> roles = systemRoleMapper.selectByUserId(userId);
        return roles != null && roles.stream()
                .anyMatch(r -> "ENABLED".equals(r.getStatus())
                        && SystemConstants.ROLE_SUPER_ADMIN.equals(r.getCode()));
    }
}
