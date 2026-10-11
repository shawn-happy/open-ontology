-- ===== 系统管理模块初始化脚本 =====
-- 文件路径：prism-platform/src/main/resources/db/migration/V1__init_system.sql
-- 说明：创建系统管理模块全部 10 张表 + 初始化内置数据

-- -----------------------------------------------------------
-- 1. 用户表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS system_user (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    username        VARCHAR(32)     NOT NULL    COMMENT '用户名',
    password        VARCHAR(128)    NOT NULL    COMMENT '密码（BCrypt哈希）',
    name            VARCHAR(50)     NOT NULL    COMMENT '姓名',
    phone           VARCHAR(11)     NULL        COMMENT '手机号',
    email           VARCHAR(100)    NULL        COMMENT '邮箱',
    avatar          VARCHAR(500)    NULL        COMMENT '头像URL',
    status          VARCHAR(16)     NOT NULL    DEFAULT 'ENABLED' COMMENT '状态：ENABLED/DISABLED',
    pwd_update_time DATETIME        NULL        COMMENT '密码最后修改时间（空表示初始密码未修改）',
    login_fail_count INT            NOT NULL    DEFAULT 0 COMMENT '连续登录失败次数',
    lock_until      DATETIME        NULL        COMMENT '账号锁定截止时间',
    last_login_at   DATETIME        NULL        COMMENT '最近登录时间',
    last_login_ip   VARCHAR(64)     NULL        COMMENT '最近登录IP',
    is_deleted      TINYINT(1)      NOT NULL    DEFAULT 0 COMMENT '逻辑删除（0=未删除，1=已删除）',
    created_by      BIGINT          NULL        COMMENT '创建人ID',
    updated_by      BIGINT          NULL        COMMENT '更新人ID',
    created_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_system_user_username (username, is_deleted),
    INDEX idx_system_user_phone (phone, is_deleted),
    INDEX idx_system_user_email (email, is_deleted),
    INDEX idx_system_user_status (status, is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- -----------------------------------------------------------
-- 2. 角色表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS system_role (
    id                      BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    name                    VARCHAR(50)     NOT NULL    COMMENT '角色名称',
    code                    VARCHAR(50)     NOT NULL    COMMENT '角色编码',
    type                    VARCHAR(16)     NOT NULL    DEFAULT 'GLOBAL' COMMENT '角色类型：GLOBAL（全局）/WORKSPACE（空间级）',
    data_scope              VARCHAR(32)     NOT NULL    DEFAULT 'ALL' COMMENT '数据权限范围：ALL/DEPT_AND_CHILDREN/DEPT/SELF/CUSTOM',
    custom_workspace_codes  JSON            NULL        COMMENT '自定义可见空间编码列表（data_scope=CUSTOM时使用）',
    status                  VARCHAR(16)     NOT NULL    DEFAULT 'ENABLED' COMMENT '状态：ENABLED/DISABLED',
    is_builtin              TINYINT(1)      NOT NULL    DEFAULT 0 COMMENT '是否内置角色（0=否，1=是）',
    remark                  VARCHAR(200)    NULL        COMMENT '备注',
    is_deleted              TINYINT(1)      NOT NULL    DEFAULT 0 COMMENT '逻辑删除（0=未删除，1=已删除）',
    created_by              BIGINT          NULL        COMMENT '创建人ID',
    updated_by              BIGINT          NULL        COMMENT '更新人ID',
    created_at              DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at              DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_system_role_code (code, is_deleted),
    INDEX idx_system_role_status (status, is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- -----------------------------------------------------------
-- 3. 菜单权限表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS system_menu (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    parent_id       BIGINT          NOT NULL    DEFAULT 0 COMMENT '上级菜单ID（0表示顶级目录）',
    name            VARCHAR(50)     NOT NULL    COMMENT '菜单名称',
    type            VARCHAR(16)     NOT NULL    COMMENT '类型：DIRECTORY（目录）/MENU（菜单）/BUTTON（按钮）',
    permission      VARCHAR(100)    NULL        COMMENT '权限标识（如system:user:create）',
    path            VARCHAR(200)    NULL        COMMENT '前端路由路径',
    component       VARCHAR(200)    NULL        COMMENT '前端组件路径',
    icon            VARCHAR(50)     NULL        COMMENT '菜单图标',
    sort_order      INT             NOT NULL    DEFAULT 0 COMMENT '排序（数值越小越靠前）',
    status          VARCHAR(16)     NOT NULL    DEFAULT 'ENABLED' COMMENT '状态：ENABLED/DISABLED',
    is_deleted      TINYINT(1)      NOT NULL    DEFAULT 0 COMMENT '逻辑删除（0=未删除，1=已删除）',
    created_by      BIGINT          NULL        COMMENT '创建人ID',
    updated_by      BIGINT          NULL        COMMENT '更新人ID',
    created_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_system_menu_parent_id (parent_id, is_deleted),
    INDEX idx_system_menu_permission (permission, is_deleted),
    INDEX idx_system_menu_type (type, is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表（目录-菜单-按钮三级）';

-- -----------------------------------------------------------
-- 4. 部门表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS system_dept (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    parent_id       BIGINT          NOT NULL    DEFAULT 0 COMMENT '上级部门ID（0表示根节点）',
    name            VARCHAR(50)     NOT NULL    COMMENT '部门名称',
    code            VARCHAR(50)     NOT NULL    COMMENT '部门编码',
    path            VARCHAR(500)    NULL        COMMENT '层级路径（如/1/5/12/，用于快速查询子节点）',
    depth           INT             NOT NULL    DEFAULT 0 COMMENT '层级深度（根节点为0）',
    leader_id       BIGINT          NULL        COMMENT '负责人用户ID',
    sort_order      INT             NOT NULL    DEFAULT 0 COMMENT '排序（数值越小越靠前）',
    status          VARCHAR(16)     NOT NULL    DEFAULT 'ENABLED' COMMENT '状态：ENABLED/DISABLED',
    is_deleted      TINYINT(1)      NOT NULL    DEFAULT 0 COMMENT '逻辑删除（0=未删除，1=已删除）',
    created_by      BIGINT          NULL        COMMENT '创建人ID',
    updated_by      BIGINT          NULL        COMMENT '更新人ID',
    created_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_system_dept_parent_id (parent_id, is_deleted),
    INDEX idx_system_dept_code (code, is_deleted),
    INDEX idx_system_dept_path (path)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表（无限层级树）';

-- -----------------------------------------------------------
-- 5. 用户角色关联表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS system_user_role (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    user_id         BIGINT          NOT NULL    COMMENT '用户ID',
    role_id         BIGINT          NOT NULL    COMMENT '角色ID',
    is_deleted      TINYINT(1)      NOT NULL    DEFAULT 0 COMMENT '逻辑删除（0=未删除，1=已删除）',
    created_by      BIGINT          NULL        COMMENT '创建人ID',
    updated_by      BIGINT          NULL        COMMENT '更新人ID',
    created_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_system_user_role_user_id (user_id, is_deleted),
    INDEX idx_system_user_role_role_id (role_id, is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- -----------------------------------------------------------
-- 6. 角色菜单关联表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS system_role_menu (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    role_id         BIGINT          NOT NULL    COMMENT '角色ID',
    menu_id         BIGINT          NOT NULL    COMMENT '菜单ID',
    is_deleted      TINYINT(1)      NOT NULL    DEFAULT 0 COMMENT '逻辑删除（0=未删除，1=已删除）',
    created_by      BIGINT          NULL        COMMENT '创建人ID',
    updated_by      BIGINT          NULL        COMMENT '更新人ID',
    created_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_system_role_menu_role_id (role_id, is_deleted),
    INDEX idx_system_role_menu_menu_id (menu_id, is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- -----------------------------------------------------------
-- 7. 用户部门关联表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS system_user_dept (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    user_id         BIGINT          NOT NULL    COMMENT '用户ID',
    dept_id         BIGINT          NOT NULL    COMMENT '部门ID',
    is_deleted      TINYINT(1)      NOT NULL    DEFAULT 0 COMMENT '逻辑删除（0=未删除，1=已删除）',
    created_by      BIGINT          NULL        COMMENT '创建人ID',
    updated_by      BIGINT          NULL        COMMENT '更新人ID',
    created_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_system_user_dept_user_id (user_id, is_deleted),
    INDEX idx_system_user_dept_dept_id (dept_id, is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户部门关联表';

-- -----------------------------------------------------------
-- 8. 审计日志表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS system_audit_log (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    user_id         BIGINT          NULL        COMMENT '操作者ID',
    username        VARCHAR(32)     NULL        COMMENT '操作者用户名',
    module          VARCHAR(32)     NOT NULL    COMMENT '模块（如system/ontology/data等）',
    operation       VARCHAR(200)    NOT NULL    COMMENT '操作内容描述',
    type            VARCHAR(32)     NOT NULL    COMMENT '操作类型：CREATE/UPDATE/DELETE/ENABLE/DISABLE/LOGIN等',
    result          VARCHAR(16)     NOT NULL    DEFAULT 'SUCCESS' COMMENT '结果：SUCCESS/FAIL',
    ip              VARCHAR(64)     NULL        COMMENT '操作者IP地址',
    request_id      VARCHAR(64)     NULL        COMMENT '请求追踪ID',
    user_agent      VARCHAR(500)    NULL        COMMENT 'User-Agent',
    detail          TEXT            NULL        COMMENT '操作详情（JSON格式）',
    operated_at     DATETIME        NOT NULL    COMMENT '操作时间',
    is_deleted      TINYINT(1)      NOT NULL    DEFAULT 0 COMMENT '逻辑删除（0=未删除，1=已删除）',
    created_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_system_audit_log_user_id (user_id),
    INDEX idx_system_audit_log_module (module),
    INDEX idx_system_audit_log_operated_at (operated_at),
    INDEX idx_system_audit_log_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审计日志表';

-- -----------------------------------------------------------
-- 9. 登录日志表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS system_login_log (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    user_id         BIGINT          NULL        COMMENT '用户ID（登录失败且用户不存在时可为空）',
    username        VARCHAR(32)     NOT NULL    COMMENT '登录使用的用户名',
    ip              VARCHAR(64)     NULL        COMMENT '登录IP地址',
    location        VARCHAR(128)    NULL        COMMENT '登录位置（通过IP解析）',
    browser         VARCHAR(64)     NULL        COMMENT '浏览器名称',
    os              VARCHAR(64)     NULL        COMMENT '操作系统名称',
    result          VARCHAR(16)     NOT NULL    DEFAULT 'SUCCESS' COMMENT '登录结果：SUCCESS/FAIL',
    fail_reason     VARCHAR(200)    NULL        COMMENT '失败原因',
    user_agent      VARCHAR(500)    NULL        COMMENT 'User-Agent',
    request_id      VARCHAR(64)     NULL        COMMENT '请求追踪ID',
    login_at        DATETIME        NOT NULL    COMMENT '登录时间',
    is_deleted      TINYINT(1)      NOT NULL    DEFAULT 0 COMMENT '逻辑删除（0=未删除，1=已删除）',
    created_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_system_login_log_user_id (user_id),
    INDEX idx_system_login_log_username (username),
    INDEX idx_system_login_log_login_at (login_at),
    INDEX idx_system_login_log_result (result)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志表';

-- -----------------------------------------------------------
-- 10. 执行日志表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS system_exec_log (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    exec_type       VARCHAR(32)     NOT NULL    COMMENT '执行类型：ACTION/FUNCTION/PIPELINE',
    task_name       VARCHAR(200)    NOT NULL    COMMENT '任务名称',
    trigger_user_id BIGINT          NULL        COMMENT '触发者ID',
    trigger_username VARCHAR(32)    NULL        COMMENT '触发者用户名',
    status          VARCHAR(16)     NOT NULL    DEFAULT 'RUNNING' COMMENT '状态：RUNNING/SUCCESS/FAIL',
    duration_ms     BIGINT          NULL        COMMENT '执行耗时（毫秒）',
    detail          TEXT            NULL        COMMENT '执行详情（JSON格式）',
    request_id      VARCHAR(64)     NULL        COMMENT '请求追踪ID',
    started_at      DATETIME        NOT NULL    COMMENT '开始时间',
    finished_at     DATETIME        NULL        COMMENT '结束时间',
    is_deleted      TINYINT(1)      NOT NULL    DEFAULT 0 COMMENT '逻辑删除（0=未删除，1=已删除）',
    created_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_system_exec_log_exec_type (exec_type),
    INDEX idx_system_exec_log_status (status),
    INDEX idx_system_exec_log_started_at (started_at),
    INDEX idx_system_exec_log_trigger_user_id (trigger_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='执行日志表（Action/Function/Pipeline）';

-- ===== 初始化内置数据 =====

-- 根部门
INSERT INTO system_dept (id, parent_id, name, code, path, depth, sort_order, status, created_by, created_at, updated_at)
SELECT 1, 0, '集团', 'ROOT', '/1/', 0, 0, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_dept WHERE id = 1);

-- 内置角色
INSERT INTO system_role (id, name, code, type, data_scope, status, is_builtin, remark, created_by, created_at, updated_at)
SELECT 1, '超级管理员', 'super_admin', 'GLOBAL', 'ALL', 'ENABLED', 1, '系统内置超级管理员角色，拥有全部权限', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_role WHERE id = 1);

INSERT INTO system_role (id, name, code, type, data_scope, status, is_builtin, remark, created_by, created_at, updated_at)
SELECT 2, '系统运维', 'system_ops', 'GLOBAL', 'ALL', 'ENABLED', 1, '系统运维角色，管理平台用户与日志', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_role WHERE id = 2);

INSERT INTO system_role (id, name, code, type, data_scope, status, is_builtin, remark, created_by, created_at, updated_at)
SELECT 3, '空间管理员', 'workspace_admin', 'WORKSPACE', 'ALL', 'ENABLED', 1, '空间管理员角色，管理空间内资源', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_role WHERE id = 3);

-- 初始管理员用户（密码为 BCrypt("onto12345678!")）
INSERT INTO system_user (id, username, password, name, phone, email, status, created_by, created_at, updated_at)
SELECT 1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '超级管理员', '13800000000', 'admin@prism.local', 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_user WHERE id = 1);

-- admin 关联超级管理员角色
INSERT INTO system_user_role (user_id, role_id, created_by, created_at, updated_at)
SELECT 1, 1, 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_user_role WHERE user_id = 1 AND role_id = 1);

-- admin 关联根部门
INSERT INTO system_user_dept (user_id, dept_id, created_by, created_at, updated_at)
SELECT 1, 1, 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_user_dept WHERE user_id = 1 AND dept_id = 1);

-- ===== 初始化系统管理菜单 =====

-- 一级目录：系统管理
INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 1, 0, '系统管理', 'DIRECTORY', NULL, '/system', NULL, 'SettingOutlined', 100, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 1);

-- 二级菜单
INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 2, 1, '用户管理', 'MENU', NULL, '/system/users', 'system/sys-users', 'UserOutlined', 1, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 2);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 3, 1, '角色管理', 'MENU', NULL, '/system/roles', 'system/sys-roles', 'TeamOutlined', 2, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 3);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 4, 1, '权限管理', 'MENU', NULL, '/system/permissions', 'system/sys-perms', 'SafetyCertificateOutlined', 3, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 4);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 5, 1, '部门管理', 'MENU', NULL, '/system/departments', 'system/sys-depts', 'ApartmentOutlined', 4, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 6, 1, '日志管理', 'MENU', NULL, '/system/logs', 'system/sys-logs', 'FileTextOutlined', 5, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 6);

-- 三级按钮权限：用户管理
INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 10, 2, '新增用户', 'BUTTON', 'system:user:create', NULL, NULL, NULL, 1, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 10);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 11, 2, '编辑用户', 'BUTTON', 'system:user:update', NULL, NULL, NULL, 2, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 11);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 12, 2, '删除用户', 'BUTTON', 'system:user:delete', NULL, NULL, NULL, 3, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 12);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 13, 2, '启停用用户', 'BUTTON', 'system:user:status', NULL, NULL, NULL, 4, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 13);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 14, 2, '重置密码', 'BUTTON', 'system:user:reset-pwd', NULL, NULL, NULL, 5, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 14);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 15, 2, '分配角色', 'BUTTON', 'system:user:assign-role', NULL, NULL, NULL, 6, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 15);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 16, 2, '分配部门', 'BUTTON', 'system:user:assign-dept', NULL, NULL, NULL, 7, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 16);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 17, 2, '导入用户', 'BUTTON', 'system:user:import', NULL, NULL, NULL, 8, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 17);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 18, 2, '导出用户', 'BUTTON', 'system:user:export', NULL, NULL, NULL, 9, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 18);

-- 三级按钮权限：角色管理
INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 20, 3, '新增角色', 'BUTTON', 'system:role:create', NULL, NULL, NULL, 1, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 20);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 21, 3, '编辑角色', 'BUTTON', 'system:role:update', NULL, NULL, NULL, 2, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 21);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 22, 3, '删除角色', 'BUTTON', 'system:role:delete', NULL, NULL, NULL, 3, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 22);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 23, 3, '启停用角色', 'BUTTON', 'system:role:status', NULL, NULL, NULL, 4, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 23);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 24, 3, '配置权限', 'BUTTON', 'system:role:config-perm', NULL, NULL, NULL, 5, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 24);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 25, 3, '分配用户', 'BUTTON', 'system:role:assign-user', NULL, NULL, NULL, 6, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 25);

-- 三级按钮权限：权限管理
INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 30, 4, '新增节点', 'BUTTON', 'system:menu:create', NULL, NULL, NULL, 1, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 30);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 31, 4, '编辑节点', 'BUTTON', 'system:menu:update', NULL, NULL, NULL, 2, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 31);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 32, 4, '删除节点', 'BUTTON', 'system:menu:delete', NULL, NULL, NULL, 3, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 32);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 33, 4, '启停用节点', 'BUTTON', 'system:menu:status', NULL, NULL, NULL, 4, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 33);

-- 三级按钮权限：部门管理
INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 40, 5, '新增部门', 'BUTTON', 'system:dept:create', NULL, NULL, NULL, 1, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 40);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 41, 5, '编辑部门', 'BUTTON', 'system:dept:update', NULL, NULL, NULL, 2, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 41);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 42, 5, '删除部门', 'BUTTON', 'system:dept:delete', NULL, NULL, NULL, 3, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 42);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 43, 5, '启停用部门', 'BUTTON', 'system:dept:status', NULL, NULL, NULL, 4, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 43);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 44, 5, '分配用户', 'BUTTON', 'system:dept:assign-user', NULL, NULL, NULL, 5, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 44);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 45, 5, '设置负责人', 'BUTTON', 'system:dept:set-leader', NULL, NULL, NULL, 6, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 45);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 46, 5, '部门转移', 'BUTTON', 'system:dept:transfer', NULL, NULL, NULL, 7, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 46);

-- 三级按钮权限：日志管理
INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 50, 6, '查看审计日志', 'BUTTON', 'system:log:audit', NULL, NULL, NULL, 1, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 50);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 51, 6, '查看登录日志', 'BUTTON', 'system:log:login', NULL, NULL, NULL, 2, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 51);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 52, 6, '查看执行日志', 'BUTTON', 'system:log:exec', NULL, NULL, NULL, 3, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 52);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 53, 6, '导出日志', 'BUTTON', 'system:log:export', NULL, NULL, NULL, 4, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 53);

-- 超级管理员角色关联全部菜单
INSERT INTO system_role_menu (role_id, menu_id, created_by, created_at, updated_at)
SELECT 1, id, 1, NOW(), NOW() FROM system_menu
WHERE NOT EXISTS (SELECT 1 FROM system_role_menu rm WHERE rm.role_id = 1 AND rm.menu_id = system_menu.id);
