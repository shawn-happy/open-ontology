-- ===== 空间管理菜单归属调整 =====
-- 文件路径：prism-platform/src/main/resources/db/migration/V7__workspace_menu_path.sql
-- 说明：空间管理属于系统管理的一部分。将 V6 中顶层的「工作空间」菜单（id=100）
--       移入「系统管理」目录（id=1），置于部门管理（sort_order=4）之后，
--       并对齐前端路由 /system/workspaces；日志管理（id=6）排序后移。

UPDATE system_menu
SET parent_id  = 1,
    name       = '空间管理',
    path       = '/system/workspaces',
    component  = 'system/workspaces',
    icon       = 'AppstoreOutlined',
    sort_order = 5,
    updated_at = NOW()
WHERE id = 100;

UPDATE system_menu
SET sort_order = 6,
    updated_at = NOW()
WHERE id = 6;
