package com.prism.common.enums;

import lombok.Getter;

/**
 * 内置空间角色枚举（四级，参考 GitLab 权限模型）
 * level 用于「多来源取最高角色」决议与角色操作等级约束：
 * OWNER(5) > MAINTAINER(4) > DEVELOPER(3) > REPORTER(2)
 */
@Getter
public enum WorkspaceRole {

    OWNER("OWNER", "Owner", 5, "空间所有者：空间内全部权限，含成员分配、角色赋予与删除空间"),
    MAINTAINER("MAINTAINER", "Maintainer", 4, "维护者：本体建模与发布、数据与应用管理、成员分配（不含删除空间）"),
    DEVELOPER("DEVELOPER", "Developer", 3, "开发者：本体建模、数据源 / 数据集 / Pipeline 等开发操作"),
    REPORTER("REPORTER", "Reporter", 2, "报告者：只读访问 + 数据探查、导出与分析报告");

    /** 自定义空间角色（system_role type=WORKSPACE）在等级决议时视为 DEVELOPER */
    public static final int CUSTOM_ROLE_LEVEL = DEVELOPER.level;

    private final String code;
    private final String displayName;
    private final int level;
    private final String description;

    WorkspaceRole(String code, String displayName, int level, String description) {
        this.code = code;
        this.displayName = displayName;
        this.level = level;
        this.description = description;
    }

    /**
     * 按编码解析内置角色
     *
     * @param code 角色编码
     * @return 匹配的枚举，未命中返回 null（可能是自定义空间角色 code）
     */
    public static WorkspaceRole fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (WorkspaceRole role : values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        return null;
    }

    /**
     * 取角色编码的等级；未命中内置枚举（自定义空间角色）时按 DEVELOPER 等级处理
     */
    public static int levelOf(String code) {
        WorkspaceRole role = fromCode(code);
        return role != null ? role.level : CUSTOM_ROLE_LEVEL;
    }
}
