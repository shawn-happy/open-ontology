package com.prism.platform.service;

import com.prism.common.result.PageResult;
import com.prism.platform.dto.AuditLogQuery;
import com.prism.platform.dto.ExecLogQuery;
import com.prism.platform.dto.LoginLogQuery;
import com.prism.platform.vo.AuditLogVO;
import com.prism.platform.vo.ExecLogVO;
import com.prism.platform.vo.LoginLogVO;

import java.util.List;

/**
 * 日志管理服务
 */
public interface LogService {

    PageResult<AuditLogVO> pageAuditLogs(AuditLogQuery query);

    PageResult<LoginLogVO> pageLoginLogs(LoginLogQuery query);

    PageResult<ExecLogVO> pageExecLogs(ExecLogQuery query);

    List<AuditLogVO> exportLogs(AuditLogQuery query);
}
