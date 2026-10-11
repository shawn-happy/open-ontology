package com.prism.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prism.platform.entity.SystemUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关联 Mapper
 */
@Mapper
public interface SystemUserRoleMapper extends BaseMapper<SystemUserRole> {

    List<SystemUserRole> selectByUserId(@Param("userId") Long userId);

    int deleteByUserId(@Param("userId") Long userId);

    int deleteByRoleId(@Param("roleId") Long roleId);
}
