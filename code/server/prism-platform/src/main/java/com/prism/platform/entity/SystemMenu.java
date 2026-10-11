package com.prism.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.prism.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 菜单权限实体（目录-菜单-按钮三级）
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("system_menu")
public class SystemMenu extends BaseEntity {

    @TableField("parent_id")
    private Long parentId;

    @TableField("name")
    private String name;

    @TableField("type")
    private String type;

    @TableField("permission")
    private String permission;

    @TableField("path")
    private String path;

    @TableField("component")
    private String component;

    @TableField("icon")
    private String icon;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField("status")
    private String status;
}
