package com.prism.common.enums;

import lombok.Getter;

/**
 * 角色类型枚举
 */
@Getter
public enum RoleType {

    GLOBAL("GLOBAL", "全局角色"),
    WORKSPACE("WORKSPACE", "空间级角色");

    private final String value;
    private final String label;

    RoleType(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
