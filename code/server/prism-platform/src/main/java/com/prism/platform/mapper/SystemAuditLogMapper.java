package com.prism.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prism.platform.entity.SystemAuditLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 审计日志 Mapper
 */
@Mapper
public interface SystemAuditLogMapper extends BaseMapper<SystemAuditLog> {
}
