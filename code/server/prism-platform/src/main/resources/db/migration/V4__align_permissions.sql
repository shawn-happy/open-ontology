-- V4: 权限码对齐（粗粒度模型）
-- 1. 补种后端 @RequirePermission 实际使用但未初始化的 list/detail 权限码
-- 2. 移除不再使用的细粒度权限码节点（后端统一使用 create/update/delete/export 粗粒度码）

-- ===== 补种 list/detail 按钮权限节点 =====

-- 用户管理
INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 60, 2, '用户列表', 'BUTTON', 'system:user:list', NULL, NULL, NULL, 10, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 60);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 61, 2, '用户详情', 'BUTTON', 'system:user:detail', NULL, NULL, NULL, 11, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 61);

-- 角色管理
INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 62, 3, '角色列表', 'BUTTON', 'system:role:list', NULL, NULL, NULL, 10, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 62);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 63, 3, '角色详情', 'BUTTON', 'system:role:detail', NULL, NULL, NULL, 11, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 63);

-- 权限管理
INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 64, 4, '权限树', 'BUTTON', 'system:menu:list', NULL, NULL, NULL, 10, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 64);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 65, 4, '权限节点详情', 'BUTTON', 'system:menu:detail', NULL, NULL, NULL, 11, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 65);

-- 部门管理
INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 66, 5, '部门树', 'BUTTON', 'system:dept:list', NULL, NULL, NULL, 10, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 66);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 67, 5, '部门详情', 'BUTTON', 'system:dept:detail', NULL, NULL, NULL, 11, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 67);

-- ===== 软删除不再使用的细粒度权限码节点 =====
-- 用户：status/reset-pwd/assign-role/assign-dept/import（后端统一使用 system:user:update / create）
-- 角色：status/config-perm/assign-user（后端统一使用 system:role:update）
-- 权限：status（后端统一使用 system:menu:update）
-- 部门：status/assign-user/set-leader/transfer（后端统一使用 system:dept:update）
UPDATE system_menu
SET is_deleted = 1, updated_at = NOW()
WHERE id IN (13, 14, 15, 16, 17, 23, 24, 25, 33, 43, 44, 45, 46)
  AND is_deleted = 0;

-- 同步软删除对应的角色-菜单关联
UPDATE system_role_menu
SET is_deleted = 1, updated_at = NOW()
WHERE menu_id IN (13, 14, 15, 16, 17, 23, 24, 25, 33, 43, 44, 45, 46)
  AND is_deleted = 0;

-- ===== 超级管理员角色关联新增菜单节点 =====
INSERT INTO system_role_menu (role_id, menu_id, created_by, created_at, updated_at)
SELECT 1, id, 1, NOW(), NOW() FROM system_menu
WHERE is_deleted = 0
  AND NOT EXISTS (SELECT 1 FROM system_role_menu rm WHERE rm.role_id = 1 AND rm.menu_id = system_menu.id AND rm.is_deleted = 0);
