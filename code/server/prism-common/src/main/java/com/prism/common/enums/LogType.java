package com.prism.common.enums;

import lombok.Getter;

/**
 * 操作日志类型枚举
 */
@Getter
public enum LogType {

    CREATE("CREATE", "创建"),
    UPDATE("UPDATE", "更新"),
    DELETE("DELETE", "删除"),
    ENABLE("ENABLE", "启用"),
    DISABLE("DISABLE", "停用"),
    LOGIN("LOGIN", "登录"),
    LOGOUT("LOGOUT", "登出"),
    ASSIGN("ASSIGN", "分配"),
    RESET_PWD("RESET_PWD", "重置密码"),
    IMPORT("IMPORT", "导入"),
    EXPORT("EXPORT", "导出");

    private final String value;
    private final String label;

    LogType(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
