package com.prism.common.enums;

import lombok.Getter;

/**
 * 用户状态枚举
 */
@Getter
public enum UserStatus {

    ENABLED("ENABLED", "启用"),
    DISABLED("DISABLED", "停用");

    private final String value;
    private final String label;

    UserStatus(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
