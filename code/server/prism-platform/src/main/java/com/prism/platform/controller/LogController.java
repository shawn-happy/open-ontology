package com.prism.platform.controller;

import com.prism.common.result.PageResult;
import com.prism.common.result.Result;
import com.prism.common.security.annotation.RequirePermission;
import com.prism.platform.dto.AuditLogQuery;
import com.prism.platform.dto.ExecLogQuery;
import com.prism.platform.dto.LoginLogQuery;
import com.prism.platform.service.LogService;
import com.prism.platform.vo.AuditLogVO;
import com.prism.platform.vo.ExecLogVO;
import com.prism.platform.vo.LoginLogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 日志管理控制器
 */
@Tag(name = "日志管理")
@RestController
@RequestMapping("/web/platform")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @Operation(summary = "审计日志分页")
    @GetMapping("/system/logs/audit")
    @RequirePermission("system:log:audit")
    public Result<PageResult<AuditLogVO>> pageAuditLogs(AuditLogQuery query) {
        PageResult<AuditLogVO> result = logService.pageAuditLogs(query);
        return Result.ok(result);
    }

    @Operation(summary = "登录日志分页")
    @GetMapping("/system/logs/login")
    @RequirePermission("system:log:login")
    public Result<PageResult<LoginLogVO>> pageLoginLogs(LoginLogQuery query) {
        PageResult<LoginLogVO> result = logService.pageLoginLogs(query);
        return Result.ok(result);
    }

    @Operation(summary = "执行日志分页")
    @GetMapping("/system/logs/exec")
    @RequirePermission("system:log:exec")
    public Result<PageResult<ExecLogVO>> pageExecLogs(ExecLogQuery query) {
        PageResult<ExecLogVO> result = logService.pageExecLogs(query);
        return Result.ok(result);
    }

    @Operation(summary = "导出审计日志")
    @GetMapping("/system/logs/export")
    @RequirePermission("system:log:export")
    public Result<List<AuditLogVO>> exportLogs(AuditLogQuery query) {
        List<AuditLogVO> list = logService.exportLogs(query);
        return Result.ok(list);
    }
}
