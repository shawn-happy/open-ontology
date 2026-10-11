package com.prism.common.enums;

import lombok.Getter;

/**
 * 数据权限范围枚举
 */
@Getter
public enum DataScope {

    ALL("ALL", "全部空间"),
    DEPT_AND_CHILDREN("DEPT_AND_CHILDREN", "本部门及子部门"),
    DEPT("DEPT", "本部门"),
    SELF("SELF", "仅自己创建"),
    CUSTOM("CUSTOM", "自定义");

    private final String value;
    private final String label;

    DataScope(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
