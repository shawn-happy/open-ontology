package com.prism.platform.exception;

import com.prism.common.exception.ErrorCode;
import lombok.Getter;

/**
 * 系统管理模块错误码
 * 编码规则：01（模块）+ 功能编码（2位）+ 错误编码（2位）
 */
@Getter
public enum SystemErrorCode {

    // ===== 认证登录 (0101xx) =====
    USERNAME_OR_PASSWORD_ERROR("010101", "用户名或密码错误"),
    CAPTCHA_ERROR("010102", "验证码错误或已过期"),
    ACCOUNT_LOCKED("010103", "账号已被锁定"),
    ACCOUNT_DISABLED("010104", "账号已被停用"),
    ACCESS_TOKEN_EXPIRED("010105", "Access Token 已过期"),
    REFRESH_TOKEN_EXPIRED("010106", "Refresh Token 已过期"),
    FIRST_LOGIN_CHANGE_PWD("010107", "首次登录须修改密码"),
    PASSWORD_EXPIRED("010108", "密码已过期须修改"),

    // ===== 用户管理 (0102xx) =====
    USERNAME_EXISTS("010201", "用户名已存在"),
    PHONE_EXISTS("010202", "手机号已被使用"),
    EMAIL_EXISTS("010203", "邮箱已被使用"),
    ADMIN_CANNOT_DISABLE("010204", "admin 账号不可停用"),
    ADMIN_USERNAME_READONLY("010205", "admin 用户名不可修改"),
    SUPER_ADMIN_ONLY_FOR_ADMIN("010206", "超级管理员角色仅可分配给 admin"),

    // ===== 角色管理 (0103xx) =====
    ROLE_NAME_EXISTS("010301", "角色名称已存在"),
    ROLE_CODE_EXISTS("010302", "角色编码已存在"),
    BUILTIN_ROLE_READONLY("010303", "内置角色不可修改"),
    BUILTIN_ROLE_CANNOT_DISABLE("010304", "内置角色不可停用"),
    SUPER_ADMIN_ROLE_READONLY("010305", "超级管理员角色不可修改"),

    // ===== 权限管理 (0104xx) =====
    PERMISSION_EXISTS("010401", "权限标识已存在"),
    MENU_HAS_CHILDREN("010402", "存在下级节点不可删除"),
    MENU_TYPE_INVALID("010403", "菜单类型层级不合法"),

    // ===== 部门管理 (0105xx) =====
    DEPT_NAME_SAME_LEVEL_EXISTS("010501", "部门名称在同级已存在"),
    DEPT_CODE_EXISTS("010502", "部门编码已存在"),
    ROOT_NODE_READONLY("010503", "根节点不可操作"),
    PARENT_CANNOT_BE_SELF_OR_CHILD("010504", "上级部门不可为自身或下级"),

    // ===== 日志管理 (0106xx) =====
    LOG_EXPORT_FAILED("010601", "日志导出失败");

    private final String code;
    private final String message;

    SystemErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 转换为通用 ErrorCode 接口兼容的 BusinessException 参数
     */
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
