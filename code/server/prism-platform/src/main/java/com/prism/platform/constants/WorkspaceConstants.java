package com.prism.platform.constants;

/**
 * 工作空间模块常量
 */
public final class WorkspaceConstants {

    private WorkspaceConstants() {}

    // ===== 空间编码 =====
    /** 空间编码格式（大写字母开头，大写字母/数字/中划线/下划线，3-64 位） */
    public static final String CODE_PATTERN = "^[A-Z][A-Z0-9_-]{2,63}$";

    /** 系统保留空间编码（避免 /web/workspace/workspace/** 歧义） */
    public static final String RESERVED_CODE = "WORKSPACE";

    // ===== 空间状态 =====
    public static final String STATUS_ENABLED = "ENABLED";
    public static final String STATUS_DISABLED = "DISABLED";

    // ===== 成员列表来源（对外 VO 口径，区别于 member 表 source 字段） =====
    /** 直接分配 */
    public static final String SOURCE_DIRECT = "DIRECT";
    /** 部门分配（动态展开，含个人角色覆盖） */
    public static final String SOURCE_DEPT = "DEPT";

    // ===== Redis Key =====
    /** 空间信息缓存：prism:workspace:info:{code}，TTL 30min */
    public static final String REDIS_WS_INFO_PREFIX = SystemConstants.REDIS_PREFIX + "workspace:info:";

    /** 用户可访问空间缓存：prism:workspace:accessible:{userId}，TTL 10min */
    public static final String REDIS_WS_ACCESSIBLE_PREFIX = SystemConstants.REDIS_PREFIX + "workspace:accessible:";

    /** 成员判定缓存：prism:workspace:member:{code}:{userId}，TTL 10min（值为角色编码或 DEPT/NONE） */
    public static final String REDIS_WS_MEMBER_PREFIX = SystemConstants.REDIS_PREFIX + "workspace:member:";

    /** 平台概览统计缓存：prism:workspace:overview，TTL 60s */
    public static final String REDIS_WS_OVERVIEW_KEY = SystemConstants.REDIS_PREFIX + "workspace:overview";

    /** 当日活跃标记：prism:workspace:active:{code}，TTL 24h（SETNX） */
    public static final String REDIS_WS_ACTIVE_PREFIX = SystemConstants.REDIS_PREFIX + "workspace:active:";

    // ===== Redis TTL（秒） =====
    public static final long TTL_WS_INFO_SECONDS = 30 * 60L;
    public static final long TTL_WS_ACCESSIBLE_SECONDS = 10 * 60L;
    public static final long TTL_WS_MEMBER_SECONDS = 10 * 60L;
    public static final long TTL_WS_OVERVIEW_SECONDS = 60L;
    public static final long TTL_WS_ACTIVE_SECONDS = 24 * 3600L;

    // ===== 成员判定缓存特殊值 =====
    /** 用户为部门来源成员（无 DIRECT/PERSONAL 记录） */
    public static final String MEMBER_CACHE_DEPT = "DEPT";
    /** 用户非空间成员 */
    public static final String MEMBER_CACHE_NONE = "NONE";

    // ===== 其他 =====
    /** 批量分配单次上限 */
    public static final int ASSIGN_BATCH_LIMIT = 200;
}
