package com.prism.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prism.platform.entity.SystemRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色菜单关联 Mapper
 */
@Mapper
public interface SystemRoleMenuMapper extends BaseMapper<SystemRoleMenu> {

    List<SystemRoleMenu> selectByRoleId(@Param("roleId") Long roleId);

    int deleteByRoleId(@Param("roleId") Long roleId);
}
