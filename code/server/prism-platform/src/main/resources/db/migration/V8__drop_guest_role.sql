-- 移除内置空间角色 GUEST（四级角色：OWNER/MAINTAINER/DEVELOPER/REPORTER）
-- 存量 GUEST 成员 / 部门默认角色迁移为最低级 REPORTER
UPDATE platform_workspace_member
SET workspace_role = 'REPORTER', updated_at = NOW()
WHERE workspace_role = 'GUEST';

UPDATE platform_workspace_dept
SET default_role = 'REPORTER', updated_at = NOW()
WHERE default_role = 'GUEST';
