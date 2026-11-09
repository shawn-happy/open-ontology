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

    /**
     * 查询绑定到指定空间的自定义空间角色（type=WORKSPACE 且 custom_workspace_codes 含该空间编码）
     */
    List<SystemRole> selectCustomWorkspaceRoles(@Param("workspaceCode") String workspaceCode);
}
