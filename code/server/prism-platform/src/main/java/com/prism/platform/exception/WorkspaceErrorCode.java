package com.prism.platform.exception;

import lombok.Getter;

/**
 * 工作空间模块错误码
 * 编码规则：02（模块）+ 功能编码（2位）+ 错误编码（2位）
 * 功能域：01=空间管理，02=成员分配，03=空间角色，04=空间上下文
 */
@Getter
public enum WorkspaceErrorCode {

    // ===== 空间管理 (0201xx) =====
    WORKSPACE_CODE_EXISTS("020101", "空间编码已存在"),
    WORKSPACE_CODE_READONLY("020102", "空间编码创建后不可修改"),
    WORKSPACE_NOT_FOUND("020103", "工作空间不存在"),
    WORKSPACE_DISABLED("020104", "工作空间已停用"),
    WORKSPACE_HAS_PUBLISHED_ASSETS("020105", "空间内存在已发布资产，不可删除"),
    WORKSPACE_CODE_INVALID("020106", "空间编码格式不合法"),
    WORKSPACE_CODE_RESERVED("020107", "空间编码为系统保留字"),
    WORKSPACE_NAME_EXISTS("020108", "空间名称已存在"),

    // ===== 成员分配 (0202xx) =====
    MEMBER_ALREADY_EXISTS("020201", "用户已是空间成员"),
    MEMBER_NOT_FOUND("020202", "成员不在该空间中"),
    LAST_OWNER_CANNOT_REMOVE("020203", "空间至少保留一名 Owner"),
    DEPT_ALREADY_ASSIGNED("020204", "部门已分配至该空间"),
    DEPT_NOT_FOUND("020205", "部门不存在"),
    OWNER_ROLE_CANNOT_OVERRIDE("020206", "空间创建人的 Owner 角色不可覆盖或降级"),
    MEMBER_ROLE_LEVEL_DENIED("020207", "无权操作角色等级不低于自己的成员"),
    OWNER_GRANT_DENIED("020208", "Owner 角色仅能由 Owner 授予"),
    DEPT_SOURCE_MEMBER_NOT_REMOVABLE("020209", "该成员由部门分配引入，请在部门维度移除所属部门"),

    // ===== 空间角色 (0203xx) =====
    WORKSPACE_ROLE_NOT_FOUND("020301", "空间角色不存在"),
    BUILTIN_WORKSPACE_ROLE_READONLY("020302", "内置空间角色不可修改"),

    // ===== 空间上下文 (0204xx) =====
    WORKSPACE_CONTEXT_INVALID("020401", "请求路径中的工作空间无效或已停用"),
    WORKSPACE_ACCESS_DENIED("020402", "无权访问该工作空间"),
    WORKSPACE_ROLE_INSUFFICIENT("020403", "空间角色权限不足");

    private final String code;
    private final String message;

    WorkspaceErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
