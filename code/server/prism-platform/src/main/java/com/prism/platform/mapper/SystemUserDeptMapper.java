package com.prism.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prism.platform.entity.SystemUserDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户部门关联 Mapper
 */
@Mapper
public interface SystemUserDeptMapper extends BaseMapper<SystemUserDept> {

    SystemUserDept selectByUserId(@Param("userId") Long userId);

    List<SystemUserDept> selectByDeptId(@Param("deptId") Long deptId);

    int deleteByUserId(@Param("userId") Long userId);
}
