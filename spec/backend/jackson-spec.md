---
name: backend-jackson-spec
description: Open Ontology 后端 Jackson 序列化/反序列化使用规范，定义命名策略、时间格式、空值处理、多态与类型安全约束
version: "1.0"
status: enforced
tech_stack: Java 21 + Spring Boot 3.3 + Jackson 2.x
created: 2026-09-29
tags: [jackson, json, serialization, spec, backend]
---

# Jackson 使用规范

## 1. 全局配置

通过 `application.yml` 统一配置，禁止在类上覆盖：camelCase 字段名、`NON_NULL`（null 不输出）、时间格式 `yyyy-MM-dd HH:mm:ss`、时区 `Asia/Shanghai`、`write-dates-as-timestamps: false`、`fail-on-unknown-properties: false`。

> ObjectMapper 自定义配置（如 Long → String 防精度丢失）：待代码实现后以源码引用方式补充。

---

## 2. 基础规则

**命名**：JSON 字段名 camelCase，与 Java 字段一致。禁止 `@JsonProperty` 改名（除对接外部系统）。枚举用 `@JsonValue` 输出业务值而非 name。

**时间**：统一 `java.time`（`LocalDateTime` / `LocalDate` / `LocalTime` / `Instant`），禁止 `java.util.Date`。禁止字段级 `@JsonFormat`（除非格式与全局不同）。

**空值**：null 不输出（`NON_NULL`），空集合输出 `[]`，空字符串输出 `""`，数值 0 输出 `0`。需始终输出的 null 字段标 `@JsonInclude(JsonInclude.Include.ALWAYS)`。

**Long 精度**：ID 字段（主键/外键）标 `@JsonSerialize(using = ToStringSerializer.class)` 防 JS 精度丢失；业务数值保持 Long。

**嵌套**：禁止循环引用（用扁平化 VO 打断），嵌套 ≤ 3 层，列表返回轻量 VO、详情返回完整 VO。

**脱敏**：手机号/身份证等敏感字段使用自定义序列化器（如 `@JsonSerialize(using = PhoneMaskSerializer.class)`）。

> 全局 Long → String 配置、脱敏序列化器实现：待代码实现后以源码引用方式补充。

---

## 3. 多态序列化

VO/DTO 中存在抽象类型或接口字段时，使用 `@JsonTypeInfo` + `@JsonSubTypes`：

```java
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SourceNodeDTO.class, name = "SOURCE"),
    @JsonSubTypes.Type(value = TransformNodeDTO.class, name = "TRANSFORM"),
    @JsonSubTypes.Type(value = SinkNodeDTO.class, name = "SINK"),
})
public abstract class PipelineNodeDTO {
    private String type;
    private String name;
}
```

**规则**：
- 类型字段名统一 `type`，类型值大写 snake_case（`SOURCE` / `TRANSFORM`）
- 禁止 `Id.CLASS` / `Id.MINIMAL_CLASS`（暴露内部实现，包路径变更导致不兼容）
- `visible = true` 确保子类可读取 type 字段
- 新增子类型必须同步更新 `@JsonSubTypes`
- 推荐与 Java 21 sealed class 配合，限制子类型范围
- Swagger 文档中多态字段标 `@Schema(oneOf = ...)` 列出所有子类型

---

## 4. 禁止事项

- ❌ `@JsonIgnore` 隐藏字段控制响应（应定义不同 VO 类）
- ❌ VO 中使用 `Map<String, Object>`（多态场景除外）
- ❌ 手动 `new ObjectMapper()`（使用 Spring 注入实例）
- ❌ Entity 上标注 Jackson 注解（Entity 与 VO 分离）

---

## 5. 检查清单

- [ ] 全局配置在 `application.yml`，不在类上覆盖
- [ ] 时间用 `java.time`，禁止 `java.util.Date`
- [ ] ID 字段（Long）标 `@JsonSerialize(using = ToStringSerializer.class)`
- [ ] 枚举用 `@JsonValue` 输出业务值
- [ ] 无循环引用，VO 嵌套 ≤ 3 层
- [ ] 敏感字段用自定义脱敏序列化器
- [ ] 多态基类标 `@JsonTypeInfo(use = Id.NAME, property = "type", visible = true)` + `@JsonSubTypes`
- [ ] 多态类型值大写 snake_case，新增子类同步更新
- [ ] Swagger 多态字段标 `@Schema(oneOf = ...)`
