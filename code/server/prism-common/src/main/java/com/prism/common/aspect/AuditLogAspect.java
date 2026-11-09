package com.prism.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 审计日志切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {

    private final AuditLogService auditLogService;
    private final ObjectMapper objectMapper;

    /**
     * 拦截 @AuditLog 注解方法
     */
    @Around("@annotation(auditLog)")
    public Object around(ProceedingJoinPoint joinPoint, AuditLog auditLog) throws Throwable {
        Object result = null;
        String operationResult = "SUCCESS";
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            operationResult = "FAIL";
            throw e;
        } finally {
            try {
                Long userId = com.prism.common.security.SecurityUtils.getCurrentUserId();
                String username = com.prism.common.security.SecurityUtils.getCurrentUsername();
                String ip = getClientIp();
                String userAgent = getUserAgent();
                String requestId = com.prism.common.context.RequestContextHolder.getRequestId();
                String detail = serializeArgs(joinPoint.getArgs());

                asyncSaveAuditLog(userId, username, auditLog.module(), auditLog.operation(),
                        auditLog.type(), operationResult, ip, requestId, userAgent, detail);
            } catch (Exception e) {
                log.error("审计日志记录失败", e);
            }
        }
    }

    /**
     * 拦截 @LoginLog 注解方法（登录/登出）
     * <p>
     * 用户身份解析：登出走安全上下文；登录成功时上下文尚未建立，从返回结果反射提取
     * （LoginVO.userInfo.id/username）；登录失败从入参反射提取账号标识（username/phone/email）。
     */
    @Around("@annotation(loginLog)")
    public Object aroundLogin(ProceedingJoinPoint joinPoint, LoginLog loginLog) throws Throwable {
        Object result = null;
        Throwable failure = null;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            failure = e;
            throw e;
        } finally {
            try {
                Long userId = com.prism.common.security.SecurityUtils.getCurrentUserId();
                String username = com.prism.common.security.SecurityUtils.getCurrentUsername();
                if (failure == null) {
                    if (userId == null) {
                        userId = extractUserId(result);
                    }
                    if (username == null) {
                        username = extractUsername(result);
                    }
                } else if (username == null) {
                    username = extractUsernameFromArgs(joinPoint.getArgs());
                }
                String failReason = failure == null ? null : failure.getMessage();
                asyncSaveLoginLog(userId, username, getClientIp(), getUserAgent(),
                        failure == null ? "SUCCESS" : "FAIL", failReason,
                        com.prism.common.context.RequestContextHolder.getRequestId());
            } catch (Exception e) {
                log.error("登录日志记录失败", e);
            }
        }
    }

    /**
     * 异步保存审计日志
     */
    @Async("asyncExecutor")
    public void asyncSaveAuditLog(Long userId, String username, String module, String operation,
                                   String type, String result, String ip, String requestId,
                                   String userAgent, String detail) {
        try {
            auditLogService.saveAuditLog(userId, username, module, operation, type, result,
                    ip, requestId, userAgent, detail);
        } catch (Exception e) {
            log.error("异步保存审计日志失败", e);
        }
    }

    /**
     * 异步保存登录日志
     */
    @Async("asyncExecutor")
    public void asyncSaveLoginLog(Long userId, String username, String ip, String userAgent,
                                   String result, String failReason, String requestId) {
        try {
            auditLogService.saveLoginLog(userId, username, ip, userAgent, result, failReason, requestId);
        } catch (Exception e) {
            log.error("异步保存登录日志失败", e);
        }
    }

    /** 从登录返回结果提取用户 ID（LoginVO.userInfo.id，反射避免 common 模块依赖业务 VO） */
    private Long extractUserId(Object result) {
        Object userInfo = invokeGetter(result, "getUserInfo");
        Object id = invokeGetter(userInfo != null ? userInfo : result, "getId");
        return id instanceof Number number ? number.longValue() : null;
    }

    /** 从登录返回结果提取用户名（LoginVO.userInfo.username） */
    private String extractUsername(Object result) {
        Object userInfo = invokeGetter(result, "getUserInfo");
        Object username = invokeGetter(userInfo != null ? userInfo : result, "getUsername");
        return username != null ? username.toString() : null;
    }

    /** 从入参提取账号标识（用户名/手机号/邮箱，登录失败时安全上下文无用户信息） */
    private String extractUsernameFromArgs(Object[] args) {
        if (args == null) {
            return null;
        }
        for (Object arg : args) {
            for (String getter : List.of("getUsername", "getPhone", "getEmail")) {
                Object value = invokeGetter(arg, getter);
                if (value instanceof String str && !str.isBlank()) {
                    return str;
                }
            }
        }
        return null;
    }

    private Object invokeGetter(Object target, String getterName) {
        if (target == null) {
            return null;
        }
        try {
            Method method = target.getClass().getMethod(getterName);
            return method.invoke(target);
        } catch (Exception e) {
            return null;
        }
    }

    private String getClientIp() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return "";
        }
        HttpServletRequest request = attrs.getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private String getUserAgent() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return "";
        }
        return attrs.getRequest().getHeader("User-Agent");
    }

    private String serializeArgs(Object[] args) {
        try {
            return objectMapper.writeValueAsString(args);
        } catch (Exception e) {
            log.warn("序列化参数失败: {}", e.getMessage());
            return "";
        }
    }
}
