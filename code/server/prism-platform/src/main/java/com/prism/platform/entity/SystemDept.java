package com.prism.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.prism.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 部门实体（无限层级树）
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("system_dept")
public class SystemDept extends BaseEntity {

    @TableField("parent_id")
    private Long parentId;

    @TableField("name")
    private String name;

    @TableField("code")
    private String code;

    @TableField("path")
    private String path;

    @TableField("depth")
    private Integer depth;

    @TableField("leader_id")
    private Long leaderId;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField("status")
    private String status;
}
