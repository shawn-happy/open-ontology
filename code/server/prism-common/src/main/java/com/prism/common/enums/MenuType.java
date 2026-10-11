package com.prism.common.enums;

import lombok.Getter;

/**
 * 菜单类型枚举
 */
@Getter
public enum MenuType {

    DIRECTORY("DIRECTORY", "目录"),
    MENU("MENU", "菜单"),
    BUTTON("BUTTON", "按钮");

    private final String value;
    private final String label;

    MenuType(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
