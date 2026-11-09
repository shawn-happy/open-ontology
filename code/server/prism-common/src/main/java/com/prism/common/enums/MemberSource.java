package com.prism.common.enums;

import lombok.Getter;

/**
 * 空间成员记录来源枚举（platform_workspace_member.source）
 */
@Getter
public enum MemberSource {

    /** 直接分配：把某个用户单独拉入空间并指定角色，逐条记录 */
    DIRECT("DIRECT", "直接分配"),

    /** 个人角色覆盖：部门来源成员被单独分配角色后写入，覆盖部门默认角色 */
    PERSONAL("PERSONAL", "个人角色覆盖"),

    /** 排除：部门来源成员被平台级部门维度操作排除出空间 */
    EXCLUDED("EXCLUDED", "部门来源已排除");

    private final String value;
    private final String label;

    MemberSource(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
