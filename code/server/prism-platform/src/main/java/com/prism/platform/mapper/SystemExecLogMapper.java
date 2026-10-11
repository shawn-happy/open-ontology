package com.prism.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prism.platform.entity.SystemExecLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 执行日志 Mapper
 */
@Mapper
public interface SystemExecLogMapper extends BaseMapper<SystemExecLog> {
}
