package com.prism.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.prism.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 角色菜单关联实体
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("system_role_menu")
public class SystemRoleMenu extends BaseEntity {

    @TableField("role_id")
    private Long roleId;

    @TableField("menu_id")
    private Long menuId;
}
