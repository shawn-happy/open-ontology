package com.prism.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.prism.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 空间成员记录实体
 * source 语义（MemberSource）：DIRECT=直接分配 / PERSONAL=个人角色覆盖 / EXCLUDED=部门来源已排除
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("platform_workspace_member")
public class WorkspaceMember extends BaseEntity {

    @TableField("workspace_code")
    private String workspaceCode;

    @TableField("user_id")
    private Long userId;

    @TableField("workspace_role")
    private String workspaceRole;

    @TableField("source")
    private String source;

    @TableField("dept_id")
    private Long deptId;
}
