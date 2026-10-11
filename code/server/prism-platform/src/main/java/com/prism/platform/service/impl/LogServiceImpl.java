package com.prism.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prism.common.result.PageResult;
import com.prism.platform.dto.AuditLogQuery;
import com.prism.platform.dto.ExecLogQuery;
import com.prism.platform.dto.LoginLogQuery;
import com.prism.platform.entity.SystemAuditLog;
import com.prism.platform.entity.SystemExecLog;
import com.prism.platform.entity.SystemLoginLog;
import com.prism.platform.mapper.SystemAuditLogMapper;
import com.prism.platform.mapper.SystemExecLogMapper;
import com.prism.platform.mapper.SystemLoginLogMapper;
import com.prism.platform.service.LogService;
import com.prism.platform.vo.AuditLogVO;
import com.prism.platform.vo.ExecLogVO;
import com.prism.platform.vo.LoginLogVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 日志管理服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final SystemAuditLogMapper systemAuditLogMapper;
    private final SystemLoginLogMapper systemLoginLogMapper;
    private final SystemExecLogMapper systemExecLogMapper;

    @Override
    public PageResult<AuditLogVO> pageAuditLogs(AuditLogQuery query) {
        Page<SystemAuditLog> page = new Page<>(query.getCurrentPage(), query.getPageSize());
        LambdaQueryWrapper<SystemAuditLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(query.getKeyword())) {
            wrapper.like(SystemAuditLog::getUsername, query.getKeyword());
        }
        if (StringUtils.isNotBlank(query.getModule())) {
            wrapper.eq(SystemAuditLog::getModule, query.getModule());
        }
        if (StringUtils.isNotBlank(query.getType())) {
            wrapper.eq(SystemAuditLog::getType, query.getType());
        }
        if (query.getStartTime() != null) {
            wrapper.ge(SystemAuditLog::getOperatedAt, query.getStartTime());
        }
        if (query.getEndTime() != null) {
            wrapper.le(SystemAuditLog::getOperatedAt, query.getEndTime());
        }
        wrapper.orderByDesc(SystemAuditLog::getOperatedAt);

        IPage<SystemAuditLog> result = systemAuditLogMapper.selectPage(page, wrapper);

        List<AuditLogVO> list = result.getRecords().stream()
                .map(this::convertToAuditLogVO)
                .collect(Collectors.toList());

        return PageResult.from(result, list);
    }

    @Override
    public PageResult<LoginLogVO> pageLoginLogs(LoginLogQuery query) {
        Page<SystemLoginLog> page = new Page<>(query.getCurrentPage(), query.getPageSize());
        LambdaQueryWrapper<SystemLoginLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(query.getUsername())) {
            wrapper.like(SystemLoginLog::getUsername, query.getUsername());
        }
        if (StringUtils.isNotBlank(query.getResult())) {
            wrapper.eq(SystemLoginLog::getResult, query.getResult());
        }
        if (query.getStartTime() != null) {
            wrapper.ge(SystemLoginLog::getLoginAt, query.getStartTime());
        }
        if (query.getEndTime() != null) {
            wrapper.le(SystemLoginLog::getLoginAt, query.getEndTime());
        }
        wrapper.orderByDesc(SystemLoginLog::getLoginAt);

        IPage<SystemLoginLog> result = systemLoginLogMapper.selectPage(page, wrapper);

        List<LoginLogVO> list = result.getRecords().stream()
                .map(this::convertToLoginLogVO)
                .collect(Collectors.toList());

        return PageResult.from(result, list);
    }

    @Override
    public PageResult<ExecLogVO> pageExecLogs(ExecLogQuery query) {
        Page<SystemExecLog> page = new Page<>(query.getCurrentPage(), query.getPageSize());
        LambdaQueryWrapper<SystemExecLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(query.getKeyword())) {
            wrapper.like(SystemExecLog::getTaskName, query.getKeyword());
        }
        if (StringUtils.isNotBlank(query.getExecType())) {
            wrapper.eq(SystemExecLog::getExecType, query.getExecType());
        }
        if (StringUtils.isNotBlank(query.getStatus())) {
            wrapper.eq(SystemExecLog::getStatus, query.getStatus());
        }
        if (query.getStartTime() != null) {
            wrapper.ge(SystemExecLog::getStartedAt, query.getStartTime());
        }
        if (query.getEndTime() != null) {
            wrapper.le(SystemExecLog::getStartedAt, query.getEndTime());
        }
        wrapper.orderByDesc(SystemExecLog::getStartedAt);

        IPage<SystemExecLog> result = systemExecLogMapper.selectPage(page, wrapper);

        List<ExecLogVO> list = result.getRecords().stream()
                .map(this::convertToExecLogVO)
                .collect(Collectors.toList());

        return PageResult.from(result, list);
    }

    @Override
    public List<AuditLogVO> exportLogs(AuditLogQuery query) {
        LambdaQueryWrapper<SystemAuditLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(query.getKeyword())) {
            wrapper.like(SystemAuditLog::getUsername, query.getKeyword());
        }
        if (StringUtils.isNotBlank(query.getModule())) {
            wrapper.eq(SystemAuditLog::getModule, query.getModule());
        }
        if (StringUtils.isNotBlank(query.getType())) {
            wrapper.eq(SystemAuditLog::getType, query.getType());
        }
        if (query.getStartTime() != null) {
            wrapper.ge(SystemAuditLog::getOperatedAt, query.getStartTime());
        }
        if (query.getEndTime() != null) {
            wrapper.le(SystemAuditLog::getOperatedAt, query.getEndTime());
        }
        wrapper.orderByDesc(SystemAuditLog::getOperatedAt);

        // 限制导出数量
        wrapper.last("LIMIT 10000");

        List<SystemAuditLog> logs = systemAuditLogMapper.selectList(wrapper);
        return logs.stream().map(this::convertToAuditLogVO).collect(Collectors.toList());
    }

    private AuditLogVO convertToAuditLogVO(SystemAuditLog log) {
        return AuditLogVO.builder()
                .id(log.getId())
                .userId(log.getUserId())
                .username(log.getUsername())
                .module(log.getModule())
                .operation(log.getOperation())
                .type(log.getType())
                .result(log.getResult())
                .ip(log.getIp())
                .requestId(log.getRequestId())
                .userAgent(log.getUserAgent())
                .detail(log.getDetail())
                .operatedAt(log.getOperatedAt())
                .build();
    }

    private LoginLogVO convertToLoginLogVO(SystemLoginLog log) {
        return LoginLogVO.builder()
                .id(log.getId())
                .userId(log.getUserId())
                .username(log.getUsername())
                .ip(log.getIp())
                .location(log.getLocation())
                .browser(log.getBrowser())
                .os(log.getOs())
                .result(log.getResult())
                .failReason(log.getFailReason())
                .requestId(log.getRequestId())
                .loginAt(log.getLoginAt())
                .build();
    }

    private ExecLogVO convertToExecLogVO(SystemExecLog log) {
        return ExecLogVO.builder()
                .id(log.getId())
                .execType(log.getExecType())
                .taskName(log.getTaskName())
                .triggerUserId(log.getTriggerUserId())
                .triggerUsername(log.getTriggerUsername())
                .status(log.getStatus())
                .durationMs(log.getDurationMs())
                .detail(log.getDetail())
                .requestId(log.getRequestId())
                .startedAt(log.getStartedAt())
                .finishedAt(log.getFinishedAt())
                .build();
    }
}
