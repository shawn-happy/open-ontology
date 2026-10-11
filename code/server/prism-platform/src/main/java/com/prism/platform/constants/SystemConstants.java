package com.prism.platform.constants;

/**
 * 系统管理模块常量
 */
public final class SystemConstants {

    private SystemConstants() {}

    // ===== 内置用户 =====
    /** 内置管理员用户名 */
    public static final String ADMIN_USERNAME = "admin";

    /** 内置管理员用户 ID */
    public static final Long ADMIN_USER_ID = 1L;

    // ===== 内置角色编码 =====
    /** 超级管理员角色编码 */
    public static final String ROLE_SUPER_ADMIN = "super_admin";

    /** 系统运维角色编码 */
    public static final String ROLE_SYSTEM_OPS = "system_ops";

    /** 空间管理员角色编码 */
    public static final String ROLE_WORKSPACE_ADMIN = "workspace_admin";

    // ===== 内置部门 =====
    /** 根部门 ID */
    public static final Long ROOT_DEPT_ID = 1L;

    /** 根部门编码 */
    public static final String ROOT_DEPT_CODE = "ROOT";

    // ===== 初始密码 =====
    /** 初始密码（新增用户、重置密码、忘记密码统一使用） */
    public static final String INITIAL_PASSWORD = "onto12345678!";

    // ===== Redis Key 前缀 =====
    public static final String REDIS_PREFIX = "prism:";

    /** 验证码 Key 前缀：prism:captcha:{key} */
    public static final String REDIS_CAPTCHA_PREFIX = REDIS_PREFIX + "captcha:";

    /** 登录失败计数 Key 前缀：prism:login:fail:{username} */
    public static final String REDIS_LOGIN_FAIL_PREFIX = REDIS_PREFIX + "login:fail:";

    /** Access Token Key 前缀：prism:token:access:{userId}:{tokenHash} */
    public static final String REDIS_TOKEN_ACCESS_PREFIX = REDIS_PREFIX + "token:access:";

    /** Refresh Token Key 前缀：prism:token:refresh:{userId}:{tokenHash} */
    public static final String REDIS_TOKEN_REFRESH_PREFIX = REDIS_PREFIX + "token:refresh:";

    /** 用户权限缓存 Key 前缀：prism:user:perms:{userId} */
    public static final String REDIS_USER_PERMS_PREFIX = REDIS_PREFIX + "user:perms:";

    /** 用户信息缓存 Key 前缀：prism:user:info:{userId} */
    public static final String REDIS_USER_INFO_PREFIX = REDIS_PREFIX + "user:info:";

    /** 单点登录当前 Token Hash：prism:token:session:{userId} */
    public static final String REDIS_TOKEN_SESSION_PREFIX = REDIS_PREFIX + "token:session:";

    // ===== 登录安全 =====
    /** 默认最大登录失败次数 */
    public static final int DEFAULT_MAX_FAIL_COUNT = 5;

    /** 默认锁定时长（分钟） */
    public static final int DEFAULT_LOCK_MINUTES = 30;

    /** 验证码有效期（秒） */
    public static final long CAPTCHA_TTL_SECONDS = 300L;

    // ===== 登录类型 =====
    public static final String LOGIN_TYPE_USERNAME = "USERNAME";
    public static final String LOGIN_TYPE_PHONE = "PHONE";
    public static final String LOGIN_TYPE_EMAIL = "EMAIL";
}
