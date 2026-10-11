-- ===== 修复 system_menu 表结构 =====
-- 问题：V1 迁移脚本可能未完全执行，导致部分字段缺失
-- 解决方案：使用 ALTER TABLE ADD COLUMN IF NOT EXISTS 补全缺失字段

-- 添加 parent_id（如果不存在）
SET @dbname = DATABASE();
SET @tablename = 'system_menu';

-- 检查并添加 parent_id
SELECT COUNT(*) INTO @exists FROM information_schema.columns
WHERE table_schema = @dbname AND table_name = @tablename AND column_name = 'parent_id';

SET @sql = IF(@exists = 0,
    'ALTER TABLE system_menu ADD COLUMN parent_id BIGINT NOT NULL DEFAULT 0 COMMENT ''上级菜单ID（0表示顶级目录）'' AFTER id',
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加 permission
SELECT COUNT(*) INTO @exists FROM information_schema.columns
WHERE table_schema = @dbname AND table_name = @tablename AND column_name = 'permission';

SET @sql = IF(@exists = 0,
    'ALTER TABLE system_menu ADD COLUMN permission VARCHAR(100) NULL COMMENT ''权限标识（如system:user:create）'' AFTER type',
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加 path
SELECT COUNT(*) INTO @exists FROM information_schema.columns
WHERE table_schema = @dbname AND table_name = @tablename AND column_name = 'path';

SET @sql = IF(@exists = 0,
    'ALTER TABLE system_menu ADD COLUMN path VARCHAR(200) NULL COMMENT ''前端路由路径'' AFTER permission',
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加 component
SELECT COUNT(*) INTO @exists FROM information_schema.columns
WHERE table_schema = @dbname AND table_name = @tablename AND column_name = 'component';

SET @sql = IF(@exists = 0,
    'ALTER TABLE system_menu ADD COLUMN component VARCHAR(200) NULL COMMENT ''前端组件路径'' AFTER path',
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加 icon
SELECT COUNT(*) INTO @exists FROM information_schema.columns
WHERE table_schema = @dbname AND table_name = @tablename AND column_name = 'icon';

SET @sql = IF(@exists = 0,
    'ALTER TABLE system_menu ADD COLUMN icon VARCHAR(50) NULL COMMENT ''菜单图标'' AFTER component',
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加 sort_order
SELECT COUNT(*) INTO @exists FROM information_schema.columns
WHERE table_schema = @dbname AND table_name = @tablename AND column_name = 'sort_order';

SET @sql = IF(@exists = 0,
    'ALTER TABLE system_menu ADD COLUMN sort_order INT NOT NULL DEFAULT 0 COMMENT ''排序（数值越小越靠前）'' AFTER icon',
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加 is_deleted
SELECT COUNT(*) INTO @exists FROM information_schema.columns
WHERE table_schema = @dbname AND table_name = @tablename AND column_name = 'is_deleted';

SET @sql = IF(@exists = 0,
    'ALTER TABLE system_menu ADD COLUMN is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT ''逻辑删除（0=未删除，1=已删除）'' AFTER status',
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
