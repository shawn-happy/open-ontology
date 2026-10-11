package com.prism.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prism.platform.entity.SystemMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单权限 Mapper
 */
@Mapper
public interface SystemMenuMapper extends BaseMapper<SystemMenu> {

    List<SystemMenu> selectAll();

    SystemMenu selectByPermission(@Param("permission") String permission);

    List<SystemMenu> selectByParentId(@Param("parentId") Long parentId);
}
