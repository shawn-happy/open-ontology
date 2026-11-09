-- ===== 工作空间管理菜单与按钮权限种子 =====
-- 文件路径：prism-platform/src/main/resources/db/migration/V6__workspace_menu.sql
-- 说明：id 段 100-129（避开既有 1-67），平台级 + 空间级权限码

-- -----------------------------------------------------------
-- 1. 平台级菜单：工作空间
-- -----------------------------------------------------------
INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 100, 0, '工作空间', 'MENU', 'platform:workspace:list', '/platform/workspaces', 'platform/workspaces', 'AppstoreOutlined', 2, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 100);

-- 平台级按钮权限
INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 101, 100, '空间详情', 'BUTTON', 'platform:workspace:detail', NULL, NULL, NULL, 1, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 101);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 102, 100, '创建空间', 'BUTTON', 'platform:workspace:create', NULL, NULL, NULL, 2, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 102);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 103, 100, '编辑空间', 'BUTTON', 'platform:workspace:update', NULL, NULL, NULL, 3, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 103);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 104, 100, '删除空间', 'BUTTON', 'platform:workspace:delete', NULL, NULL, NULL, 4, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 104);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 105, 100, '启停用空间', 'BUTTON', 'platform:workspace:status', NULL, NULL, NULL, 5, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 105);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 106, 100, '成员分配', 'BUTTON', 'platform:workspace:assign', NULL, NULL, NULL, 6, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 106);

-- -----------------------------------------------------------
-- 2. 空间级菜单：成员管理 → 分配记录
-- -----------------------------------------------------------
INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 110, 100, '成员管理', 'DIRECTORY', NULL, NULL, NULL, 'TeamOutlined', 90, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 110);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 111, 110, '分配记录', 'MENU', 'workspace:member:list', '/workspace/members', 'workspace/members', NULL, 1, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 111);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 112, 111, '分配角色', 'BUTTON', 'workspace:member:role', NULL, NULL, NULL, 1, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 112);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 113, 111, '移除成员', 'BUTTON', 'workspace:member:remove', NULL, NULL, NULL, 2, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 113);

INSERT INTO system_menu (id, parent_id, name, type, permission, path, component, icon, sort_order, status, created_by, created_at, updated_at)
SELECT 114, 111, '分配成员', 'BUTTON', 'workspace:member:assign', NULL, NULL, NULL, 3, 'ENABLED', 1, NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 114);

-- -----------------------------------------------------------
-- 3. 角色授权
-- -----------------------------------------------------------
-- 超级管理员（role_id=1）关联全部新增菜单节点
INSERT INTO system_role_menu (role_id, menu_id, created_by, created_at, updated_at)
SELECT 1, id, 1, NOW(), NOW() FROM system_menu
WHERE id BETWEEN 100 AND 129
  AND is_deleted = 0
  AND NOT EXISTS (SELECT 1 FROM system_role_menu rm WHERE rm.role_id = 1 AND rm.menu_id = system_menu.id AND rm.is_deleted = 0);

-- 空间管理员（role_id=3）：空间列表/详情/创建/编辑/成员分配（不含删除与启停用，对齐权限码汇总表）
INSERT INTO system_role_menu (role_id, menu_id, created_by, created_at, updated_at)
SELECT 3, id, 1, NOW(), NOW() FROM system_menu
WHERE id IN (100, 101, 102, 103, 106)
  AND is_deleted = 0
  AND NOT EXISTS (SELECT 1 FROM system_role_menu rm WHERE rm.role_id = 3 AND rm.menu_id = system_menu.id AND rm.is_deleted = 0);
