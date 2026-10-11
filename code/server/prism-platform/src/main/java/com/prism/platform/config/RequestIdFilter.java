package com.prism.platform.config;

import com.prism.common.context.RequestContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * 请求 ID 过滤器
 * 从请求头 X-Request-ID 获取或生成新的 requestId，注入到 Context
 */
@Component
@Order(1)
public class RequestIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String requestId = request.getHeader("X-Request-ID");
            if (requestId == null || requestId.isEmpty()) {
                requestId = UUID.randomUUID().toString();
            }
            RequestContextHolder.setRequestId(requestId);
            response.setHeader("X-Request-ID", requestId);

            filterChain.doFilter(request, response);
        } finally {
            RequestContextHolder.clear();
        }
    }
}
