---
name: backend-lombok-spec
description: Open Ontology 后端 Lombok 使用规范，定义允许/禁止的注解、与 Spring 注入的配合及常见陷阱规避
version: "1.0"
status: enforced
tech_stack: Java 21 + Spring Boot 3.3 + Lombok 1.18.x
created: 2026-09-29
tags: [lombok, spec, backend]
---

# Lombok 使用规范

## 1. 注解矩阵

**推荐**：

| 注解 | 场景 |
|---|---|
| `@Data` | Entity、VO、DTO |
| `@Getter` / `@Setter` | 精细控制（替代 `@Data`） |
| `@NoArgsConstructor` | Entity、VO、DTO（框架反射需要） |
| `@Builder` + `@AllArgsConstructor` | VO/DTO 测试构建 |
| `@Slf4j` | Service、Controller、工具类 |
| `@RequiredArgsConstructor` | Service、Controller 构造注入 |

**禁止**：`@SneakyThrows`（隐藏异常）、`@Cleanup`（用 try-with-resources）、`@Synchronized`（用 ReentrantLock）、`@val`/`@var`（Java 已有）、`@Wither`/`@With`/`@Delegate`（可读性差）。`@AllArgsConstructor` 禁止用于依赖注入。

---

## 2. 分层规则

**Entity**：`@Data` + `@NoArgsConstructor`。

**VO / DTO**：`@Data` + `@NoArgsConstructor` + `@AllArgsConstructor` + `@Builder`（`@Builder` 依赖全参构造）。

**Service / Controller**：`@RequiredArgsConstructor` + `private final` 字段 = **构造注入**。禁止 `@Autowired` 字段注入。

---

## 3. 注意事项

- **继承**：子类 `@Data` 必须加 `@EqualsAndHashCode(callSuper = true)`，否则 equals/hashCode 不考虑父类字段
- **toString 递归**：双向引用字段加 `@ToString.Exclude`，避免 StackOverflow
- **Builder 继承**：子类 Builder 会丢失父类字段，使用 `@SuperBuilder` 替代 `@Builder`

---

## 4. 检查清单

- [ ] Service / Controller 用 `@RequiredArgsConstructor` + `private final`，禁止 `@Autowired`
- [ ] 子类 `@Data` 加 `@EqualsAndHashCode(callSuper = true)`
- [ ] 双向引用加 `@ToString.Exclude`
- [ ] 继承场景用 `@SuperBuilder`
- [ ] 禁止 `@SneakyThrows` / `@Cleanup` / `@Synchronized`
- [ ] Entity / VO / DTO 标注 `@NoArgsConstructor`
