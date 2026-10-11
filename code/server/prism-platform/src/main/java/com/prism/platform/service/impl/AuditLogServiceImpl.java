package com.prism.platform.service.impl;

import com.prism.common.aspect.AuditLogService;
import com.prism.platform.entity.SystemAuditLog;
import com.prism.platform.entity.SystemLoginLog;
import com.prism.platform.mapper.SystemAuditLogMapper;
import com.prism.platform.mapper.SystemLoginLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 审计日志写入实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final SystemAuditLogMapper systemAuditLogMapper;
    private final SystemLoginLogMapper systemLoginLogMapper;

    @Override
    @Async("asyncExecutor")
    public void saveAuditLog(Long userId, String username, String module, String operation,
                             String type, String result, String ip, String requestId,
                             String userAgent, String detail) {
        try {
            SystemAuditLog auditLog = new SystemAuditLog();
            auditLog.setUserId(userId);
            auditLog.setUsername(username);
            auditLog.setModule(module);
            auditLog.setOperation(operation);
            auditLog.setType(type);
            auditLog.setResult(result);
            auditLog.setIp(ip);
            auditLog.setRequestId(requestId);
            auditLog.setUserAgent(userAgent);
            auditLog.setDetail(detail);
            auditLog.setOperatedAt(LocalDateTime.now());
            systemAuditLogMapper.insert(auditLog);
        } catch (Exception e) {
            log.error("保存审计日志失败: module={}, operation={}", module, operation, e);
        }
    }

    @Override
    @Async("asyncExecutor")
    public void saveLoginLog(Long userId, String username, String ip, String userAgent,
                             String result, String failReason, String requestId) {
        try {
            SystemLoginLog loginLog = new SystemLoginLog();
            loginLog.setUserId(userId);
            loginLog.setUsername(username);
            loginLog.setIp(ip);
            loginLog.setUserAgent(userAgent);
            loginLog.setResult(result);
            loginLog.setFailReason(failReason);
            loginLog.setRequestId(requestId);
            loginLog.setLoginAt(LocalDateTime.now());
            systemLoginLogMapper.insert(loginLog);
        } catch (Exception e) {
            log.error("保存登录日志失败: username={}", username, e);
        }
    }
}
