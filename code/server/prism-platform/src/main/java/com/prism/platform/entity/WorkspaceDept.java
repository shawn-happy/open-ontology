package com.prism.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.prism.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 空间部门分配实体（成员查询时按 system_dept.path 动态展开含子部门）
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("platform_workspace_dept")
public class WorkspaceDept extends BaseEntity {

    @TableField("workspace_code")
    private String workspaceCode;

    @TableField("dept_id")
    private Long deptId;

    @TableField("default_role")
    private String defaultRole;
}
