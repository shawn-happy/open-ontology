package com.prism.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prism.platform.entity.SystemLoginLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志 Mapper
 */
@Mapper
public interface SystemLoginLogMapper extends BaseMapper<SystemLoginLog> {
}
