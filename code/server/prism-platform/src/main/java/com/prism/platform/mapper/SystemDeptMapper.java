package com.prism.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prism.platform.entity.SystemDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门 Mapper
 */
@Mapper
public interface SystemDeptMapper extends BaseMapper<SystemDept> {

    SystemDept selectByCode(@Param("code") String code);

    List<SystemDept> selectByParentId(@Param("parentId") Long parentId);

    List<SystemDept> selectChildrenByPath(@Param("pathPrefix") String pathPrefix);
}
