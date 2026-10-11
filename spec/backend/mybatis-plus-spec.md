---
name: backend-mybatis-plus-spec
description: Open Ontology 后端 MyBatis-Plus 使用规范，定义实体映射、Mapper/Service 层约定、分页、多租户与性能约束
version: "1.0"
status: enforced
tech_stack: Java 21 + Spring Boot 3.3 + MyBatis-Plus 3.5.x
created: 2026-09-29
tags: [mybatis-plus, orm, spec, backend]
---

# MyBatis-Plus 使用规范

## 1. Entity 规范

- 继承 `BaseEntity`（含 id、createdAt、updatedAt、createdBy、updatedBy、deleted）
- 使用 `@TableName` 显式指定表名
- 使用 `@TableId(type = IdType.AUTO)` 自增主键
- 逻辑删除字段使用 `@TableLogic`
- **每个字段必须显式声明 `@TableField` 注解，指定数据库列名**（禁止依赖 `mapUnderscoreToCamelCase` 隐式映射），确保 Java 字段与 SQL 列名一一对应、消除歧义
- JSON 字段使用 `@TableField(value = "列名", typeHandler = JacksonTypeHandler.class)`
- BaseEntity 基类字段同样使用 `@TableField` 显式映射：`created_at`、`updated_at`、`created_by`、`updated_by`、`is_deleted`

**示例**：
```java
@TableName("system_user")
public class SystemUser extends BaseEntity {
    @TableField("username")
    private String username;

    @TableField("login_fail_count")
    private Integer loginFailCount;

    @TableField("pwd_update_time")
    private LocalDateTime pwdUpdateTime;
}
```

**资源隔离**：空间级资源 Entity 额外声明 `private String workspaceCode`；全局资源（用户、角色、枚举类等）不含此字段。

> BaseEntity、MetaObjectHandler、租户拦截器配置：待代码实现后以源码引用方式补充。

---

## 2. Mapper 规范

- 继承 `BaseMapper<T>`，标注 `@Mapper`
- **禁止使用 `@Select`、`@Update`、`@Insert`、`@Delete` 注解**，所有自定义 SQL 必须写在 XML Mapper 文件中
- XML Mapper 统一放在 `src/main/resources/mapper/` 目录，文件命名与 Mapper 接口一致（如 `SystemUserMapper.xml`）
- 配置 `mybatis-plus.mapper-locations: classpath*:mapper/**/*.xml`
- 自定义方法命名：查询用 `select` / `selectList`、计数用 `count`、批量用 `batchInsert` / `batchUpdate`

**禁止使用 BaseMapper 批量插入方法**：
- ❌ `BaseMapper#insert(Collection<T>)` — 内部逐条 INSERT，性能差且无法利用批量语法优势
- ❌ `BaseMapper#insertOrUpdate(Collection<T>, int)` — 同上，且语义不透明

**批量插入/更新必须使用 XML SQL**：

```xml
<!-- 批量插入：INSERT INTO ... VALUES (), () -->
<insert id="batchInsert">
    INSERT INTO system_role_menu (role_id, menu_id, created_by, created_at, updated_at)
    VALUES
    <foreach collection="list" item="item" separator=",">
        (#{item.roleId}, #{item.menuId}, #{item.createdBy}, NOW(), NOW())
    </foreach>
</insert>

<!-- 批量插入或更新：ON DUPLICATE KEY UPDATE -->
<insert id="batchInsertOrUpdate">
    INSERT INTO system_role_menu (role_id, menu_id, created_by, created_at, updated_at)
    VALUES
    <foreach collection="list" item="item" separator=",">
        (#{item.roleId}, #{item.menuId}, #{item.createdBy}, NOW(), NOW())
    </foreach>
    ON DUPLICATE KEY UPDATE
        updated_at = VALUES(updated_at)
</insert>
```

**禁止注解 SQL 的原因**：
- 注解 SQL 无法被 Git 有效 diff 和 review
- 复杂 SQL（多表 join、动态 `<if>`/`<foreach>`）在注解中难以维护
- 统一 XML 便于 DBA 审查和 SQL 优化
- 保持 Mapper 接口干净整洁，职责分离

---

## 3. Service 规范

**禁止继承 `IService<T>` / `ServiceImpl<M, T>`**。Service 为纯业务接口，实现类通过构造注入 Mapper。

```java
public interface OntologyClassService {
    OntologyClass getByApiName(String workspaceCode, String apiName);
    PageResult<ClassListVO> page(String workspaceCode, ClassListQuery query);
    OntologyClass create(String workspaceCode, CreateClassDTO dto);
}

@Service
@RequiredArgsConstructor
public class OntologyClassServiceImpl implements OntologyClassService {
    private final OntologyClassMapper classMapper;

    @Override
    public OntologyClass getByApiName(String workspaceCode, String apiName) {
        return classMapper.selectOne(new LambdaQueryWrapper<OntologyClass>()
            .eq(OntologyClass::getWorkspaceCode, workspaceCode)
            .eq(OntologyClass::getApiName, apiName)
            .eq(OntologyClass::getDeleted, 0));
    }
}
```

**方法命名**：查询 `get`/`list`/`page`，写操作 `create`/`update`/`delete`/`batchCreate`。

**禁止事项**：
- ❌ 继承 `IService` / `ServiceImpl`（API 膨胀、语义不清、绕过业务校验）
- ❌ 使用 `QueryWrapper` 字符串字段名，统一使用 `LambdaQueryWrapper`
- ❌ Controller 直接调用 Mapper
- ❌ 调用 `mapper.deleteById()` 物理删除

---

## 4. 分页查询

```java
Page<Entity> page = new Page<>(currentPage, pageSize);
mapper.selectPage(page, wrapper);
```

- 默认 pageSize = 20，最大 100
- 禁止内存分页（`list()` 后 `subList`）

---

## 5. 多租户隔离

- 空间级资源表含 `workspace_code`，通过 `TenantLineInnerInterceptor` 自动注入条件
- 全局资源表排除在租户拦截之外

---

## 6. 性能约束

| 规则 | 说明 |
|---|---|
| 禁止 `SELECT *` | 大表查询指定字段 |
| 禁止内存分页 | 必须使用 `Page<T>` 数据库分页 |
| 索引感知 | 查询条件字段必须有索引 |
| 慢 SQL 监控 | 超过 200ms 自动告警 |

---

## 7. 检查清单

- [ ] Entity：`@TableName` + `@TableId(IdType.AUTO)` + `@TableLogic`
- [ ] 每个字段显式声明 `@TableField("列名")`，禁止依赖隐式驼峰映射
- [ ] Service 不继承 `IService` / `ServiceImpl`，构造注入 Mapper
- [ ] 使用 `LambdaQueryWrapper`，禁止字符串字段名
- [ ] 分页使用 `mapper.selectPage()`，禁止内存分页
- [ ] 空间级资源含 `workspaceCode`；全局资源不含
- [ ] 禁止物理删除
- [ ] 禁止使用 `@Select`/`@Update`/`@Insert`/`@Delete` 注解，所有 SQL 写在 XML
- [ ] 禁止使用 `BaseMapper#insert(Collection)` / `insertOrUpdate(Collection, int)` 批量方法，改用 XML `INSERT INTO VALUES (),()` 或 `ON DUPLICATE KEY UPDATE`
