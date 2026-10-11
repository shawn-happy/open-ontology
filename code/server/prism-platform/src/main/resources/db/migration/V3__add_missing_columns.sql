-- V3: 补充日志表缺失的 created_by / updated_by 字段

ALTER TABLE system_audit_log
    ADD COLUMN created_by BIGINT NULL COMMENT '创建人ID' AFTER updated_at,
    ADD COLUMN updated_by BIGINT NULL COMMENT '更新人ID' AFTER created_by;

ALTER TABLE system_login_log
    ADD COLUMN created_by BIGINT NULL COMMENT '创建人ID' AFTER updated_at,
    ADD COLUMN updated_by BIGINT NULL COMMENT '更新人ID' AFTER created_by;

ALTER TABLE system_exec_log
    ADD COLUMN created_by BIGINT NULL COMMENT '创建人ID' AFTER updated_at,
    ADD COLUMN updated_by BIGINT NULL COMMENT '更新人ID' AFTER created_by;
