-- ===== 工作空间管理模块初始化脚本 =====
-- 文件路径：prism-platform/src/main/resources/db/migration/V5__init_workspace.sql
-- 说明：创建工作空间主表 / 空间成员记录表 / 空间部门分配表

-- -----------------------------------------------------------
-- 1. 工作空间表（全局资源，不含 workspace_code）
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS platform_workspace (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    name            VARCHAR(128)    NOT NULL    COMMENT '空间名称',
    code            VARCHAR(64)     NOT NULL    COMMENT '空间编码（大写字母/数字/中划线，创建后不可修改，全平台唯一）',
    description     VARCHAR(512)    NULL        COMMENT '空间描述',
    owner_user_id   BIGINT          NOT NULL    COMMENT '空间管理员用户ID（system_user.id）',
    status          VARCHAR(16)     NOT NULL    DEFAULT 'ENABLED' COMMENT '状态：ENABLED/DISABLED',
    last_active_at  DATETIME        NULL        COMMENT '最近活跃时间（空间级请求异步回写，用于活跃空间统计）',
    is_deleted      TINYINT(1)      NOT NULL    DEFAULT 0 COMMENT '逻辑删除（0=未删除，1=已删除）',
    created_by      BIGINT          NULL        COMMENT '创建人ID',
    updated_by      BIGINT          NULL        COMMENT '更新人ID',
    created_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_platform_workspace_code (code, is_deleted),
    INDEX idx_platform_workspace_status (status, is_deleted),
    INDEX idx_platform_workspace_owner (owner_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工作空间表';

-- -----------------------------------------------------------
-- 2. 空间成员记录表
--    source=DIRECT   直接分配的成员
--    source=PERSONAL 部门来源成员的个人角色覆盖记录
--    source=EXCLUDED 部门来源成员的排除记录
--    应用层保证 (workspace_code, user_id, source) 至多一条有效记录
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS platform_workspace_member (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    workspace_code  VARCHAR(64)     NOT NULL    COMMENT '空间编码',
    user_id         BIGINT          NOT NULL    COMMENT '用户ID（system_user.id）',
    workspace_role  VARCHAR(32)     NOT NULL    COMMENT '空间角色编码（内置：OWNER/MAINTAINER/DEVELOPER/REPORTER/GUEST；或自定义空间角色 system_role.code）',
    source          VARCHAR(16)     NOT NULL    COMMENT '来源：DIRECT=直接分配 / PERSONAL=个人角色覆盖 / EXCLUDED=部门来源已排除',
    dept_id         BIGINT          NULL        COMMENT '部门来源ID（source=PERSONAL/EXCLUDED 时记录来自哪个分配部门）',
    is_deleted      TINYINT(1)      NOT NULL    DEFAULT 0 COMMENT '逻辑删除（0=未删除，1=已删除）',
    created_by      BIGINT          NULL        COMMENT '分配人ID',
    updated_by      BIGINT          NULL        COMMENT '更新人ID',
    created_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '分配时间',
    updated_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_pw_member_ws_user (workspace_code, user_id),
    INDEX idx_pw_member_user (user_id, is_deleted),
    INDEX idx_pw_member_ws_role (workspace_code, workspace_role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='空间成员记录表';

-- -----------------------------------------------------------
-- 3. 空间部门分配表（成员查询时按 system_dept.path 动态展开含子部门）
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS platform_workspace_dept (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    workspace_code  VARCHAR(64)     NOT NULL    COMMENT '空间编码',
    dept_id         BIGINT          NOT NULL    COMMENT '部门ID（system_dept.id）',
    default_role    VARCHAR(32)     NOT NULL    DEFAULT 'REPORTER' COMMENT '部门成员默认空间角色编码',
    is_deleted      TINYINT(1)      NOT NULL    DEFAULT 0 COMMENT '逻辑删除（0=未删除，1=已删除）',
    created_by      BIGINT          NULL        COMMENT '分配人ID',
    updated_by      BIGINT          NULL        COMMENT '更新人ID',
    created_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '分配时间',
    updated_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_pw_dept_ws (workspace_code, dept_id),
    INDEX idx_pw_dept_dept (dept_id, is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='空间部门分配表';
