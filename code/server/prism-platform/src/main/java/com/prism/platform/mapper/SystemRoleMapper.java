package com.prism.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prism.platform.entity.SystemRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色 Mapper
 */
@Mapper
public interface SystemRoleMapper extends BaseMapper<SystemRole> {

    SystemRole selectByCode(@Param("code") String code);

    List<SystemRole> selectByUserId(@Param("userId") Long userId);
}
