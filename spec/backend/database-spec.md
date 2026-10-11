---
name: backend-database-spec
description: Open Ontology 后端数据库设计规范，定义命名、索引、多租户隔离、字段类型选择与 DDL 管理约束
version: "1.0"
status: enforced
tech_stack: MySQL 8.0+ + MyBatis-Plus 3.5.x
created: 2026-09-29
tags: [database, mysql, ddl, spec, backend]
---

# 数据库设计规范

## 1. 命名规范

**表名**：`{模块}_{实体}`，snake_case，单数（`ontology_class` ✅ `ontology_classes` ❌）。模块前缀：`platform_` / `ontology_` / `data_` / `security_` / `application_` / `audit_`。

**列名**：snake_case。布尔前缀 `is_`、外键后缀 `_id`、时间后缀 `_at`。工作空间字段用 `workspace_code`（VARCHAR，编码而非 ID）。

**索引命名**：普通索引 `idx_{表名}_{字段}`。**禁止 `UNIQUE KEY` / `FOREIGN KEY` / `CHECK` 约束**，所有业务规则由应用层保证。

---

## 2. 公共字段

每张表必须包含：`id BIGINT AUTO_INCREMENT`（主键）、`created_at`、`updated_at`、`created_by`、`updated_by`、`is_deleted TINYINT(1) DEFAULT 0`。

**空间级资源**（本体类、Pipeline、数据集等）额外包含 `workspace_code VARCHAR(64) NOT NULL`；**全局资源**（用户、角色、枚举类等）不含此字段。

---

## 3. 字段类型

| 场景 | MySQL 类型 | Java 类型 |
|---|---|---|
| 主键 | `BIGINT AUTO_INCREMENT` | `Long` |
| 工作空间编码 | `VARCHAR(64)` | `String` |
| 短文本 | `VARCHAR(n)` | `String` |
| 长文本 | `TEXT` | `String` |
| JSON | `JSON` | `String`（配合 TypeHandler） |
| 整数 | `INT` / `BIGINT` | `Integer` / `Long` |
| 小数/金额 | `DECIMAL(p,s)` | `BigDecimal` |
| 布尔 | `TINYINT(1)` | `Boolean` |
| 日期时间 | `DATETIME` | `LocalDateTime` |
| 枚举 | `VARCHAR(32)` | `Enum`（配合 TypeHandler） |

**禁止类型**：`FLOAT`/`DOUBLE`（精度丢失）、`TIMESTAMP`（时区问题）、MySQL `ENUM`（变更需 ALTER）、`BLOB`/`LONGBLOB`（大文件走 MinIO）、`BIT`（可读性差）。

---

## 4. 索引规范

- 仅允许普通索引，**禁止 `UNIQUE INDEX` / `FOREIGN KEY` / `CHECK`**
- 单表索引 ≤ 5 个（含主键），联合索引遵循最左前缀原则
- 推荐索引字段：`workspace_code`、业务唯一键（唯一性由应用层校验）、高频查询条件
- 禁止冗余索引、禁止全表扫描、LIKE 仅支持前缀匹配

---

## 5. DDL 管理

DDL 变更通过 **Flyway** 管理（`resources/db/migration/V{version}__{description}.sql`），禁止手动执行或修改已执行脚本。

**DDL 示例**（空间级资源）：

```sql
CREATE TABLE ontology_class (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
    workspace_code  VARCHAR(64)     NOT NULL    COMMENT '工作空间编码',
    name            VARCHAR(64)     NOT NULL    COMMENT '显示名称',
    api_name        VARCHAR(64)     NOT NULL    COMMENT 'API 名称（PascalCase）',
    kind            VARCHAR(16)     NOT NULL    COMMENT '类型：CLASS/INTERFACE/ENUM',
    category_id     BIGINT          NULL        COMMENT '所属类目 ID',
    status          VARCHAR(16)     NOT NULL    DEFAULT 'DRAFT' COMMENT '状态',
    is_deleted      TINYINT(1)      NOT NULL    DEFAULT 0 COMMENT '逻辑删除',
    created_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME        NOT NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by      BIGINT          NULL        COMMENT '创建人 ID',
    updated_by      BIGINT          NULL        COMMENT '更新人 ID',
    PRIMARY KEY (id),
    INDEX idx_workspace_code (workspace_code, is_deleted),
    INDEX idx_api_name (workspace_code, api_name, is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='本体类定义';
```

每张表和每个字段必须有 `COMMENT`，字符集统一 `utf8mb4_unicode_ci`。

---

## 6. 禁止事项

- ❌ 物理删除（统一 `is_deleted` 逻辑删除）
- ❌ `UNIQUE KEY` / `FOREIGN KEY` / `CHECK` 约束（应用层保证）
- ❌ 存储过程、触发器、视图（应用层实现）
- ❌ `SELECT *`（指定字段）
- ❌ 事务中调用外部 HTTP / RPC

> **核心原则**：数据库仅负责存储与索引，所有业务约束由应用层实现。

---

## 7. 检查清单

- [ ] 表名 `{模块}_{实体}`，snake_case，单数
- [ ] 主键 `BIGINT AUTO_INCREMENT`
- [ ] 包含公共字段（id、created_at、updated_at、is_deleted 等）
- [ ] 空间级资源含 `workspace_code VARCHAR(64)`；全局资源不含
- [ ] 金额用 `DECIMAL`，时间用 `DATETIME`，禁止 FLOAT/TIMESTAMP/ENUM/BLOB
- [ ] 仅普通索引，禁止 UNIQUE KEY / FOREIGN KEY / CHECK
- [ ] DDL 通过 Flyway 管理，表和字段均有 COMMENT，字符集 `utf8mb4_unicode_ci`
