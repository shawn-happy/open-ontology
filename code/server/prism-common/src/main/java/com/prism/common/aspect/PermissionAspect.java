package com.prism.common.aspect;

import com.prism.common.security.SecurityUtils;
import com.prism.common.security.annotation.RequirePermission;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/**
 * 权限校验切面
 * 拦截 @RequirePermission 注解，检查当前用户是否拥有指定权限
 */
@Aspect
@Component
public class PermissionAspect {

    /**
     * 拦截所有标注了 @RequirePermission 的方法
     */
    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) throws Throwable {
        String requiredPerm = requirePermission.value();

        // admin 用户（userId=1）拥有所有权限，直接放行
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId != null && userId == 1L) {
            return joinPoint.proceed();
        }

        // 检查是否拥有指定权限
        if (!SecurityUtils.hasPermission(requiredPerm)) {
            throw new AccessDeniedException("无权限访问：" + requiredPerm);
        }

        return joinPoint.proceed();
    }
}
