---
name: backend-unit-test-spec
description: Open Ontology 后端单元测试规范，定义覆盖率目标、测试分层与测试编写约束
version: "1.0"
status: enforced
tech_stack: Java 21 + JUnit 5 + Mockito 5 + AssertJ 3 + JaCoCo + Spring Format
created: 2026-09-29
tags: [test, junit, mockito, jacoco, coverage, spec, backend]
---

# 单元测试规范

## 1. 覆盖率目标

行覆盖率 ≥ 80%（JaCoCo 统计，CI 强制门禁）。分层目标：Domain ≥ 90%、Application/Service ≥ 85%、Web（Controller）≥ 75%。

**排除项**（不计入覆盖率）：`**/entity/**`、`**/vo/**`、`**/dto/**`、`**/bo/**`、`**/exception/**`、`**/config/**`、`**/enums/**`、`**/constant/**`、`**/*Application.*`、`**/filter/**`。

---

## 2. 测试分层

| 层级 | 工具 | 占比 | 速度 |
|---|---|---|---|
| **Unit** | JUnit5 + Mockito | 80% | <100ms |
| **API** | MockMvc | 20% | <2s |

---

## 3. @DisplayName 规范

**每个测试类、嵌套分组、测试方法都必须标注 `@DisplayName`**，描述测试意图的中文说明。

```java
@DisplayName("订单服务")
class OrderServiceTest {

    @Nested
    @DisplayName("创建订单")
    class CreateOrder {

        @Test
        @DisplayName("输入合法 - 返回订单ID")
        void create_whenValidInput_thenReturnOrderId() { }

        @Test
        @DisplayName("名称重复 - 抛出业务异常")
        void create_whenDuplicateName_throwBusinessException() { }
    }

    @Nested
    @DisplayName("查询订单")
    class GetOrder {

        @Test
        @DisplayName("订单存在 - 返回订单详情")
        void getById_whenExists_thenReturnOrder() { }

        @Test
        @DisplayName("订单不存在 - 返回null")
        void getById_whenNotFound_returnNull() { }
    }
}
```

---

## 4. 编写规则

### 4.1 AAA 模式

每个测试方法遵循 **Arrange → Act → Assert** 三段式。

### 4.2 方法命名

格式：`methodName_scenario_expectedBehavior`

### 4.3 Mock 规则

- **必须 Mock**：Repository、HTTP 客户端、消息队列、外部服务、缓存
- **禁止 Mock**：Domain Entity、VO、DTO、BO、被测类本身

### 4.4 断言规则

统一使用 **AssertJ** 流式断言，禁止 JUnit 原生断言（`assertEquals` / `assertTrue` 等）。

```java
assertThat(result).isNotNull();
assertThat(result.getName()).isEqualTo("张三");
assertThatThrownBy(() -> service.create(null))
    .isInstanceOf(BusinessException.class)
    .hasMessageContaining("不能为空");
```

### 4.5 测试独立性

- 每个测试方法独立运行，不依赖执行顺序
- 禁止测试间共享可变状态
- 禁止使用 `Thread.sleep()`

---

## 5. 测试数据

### 5.1 CSV 文件（推荐，适合表格型数据）

将测试数据放在 `src/test/resources/testdata/` 目录：

```csv
# src/test/resources/testdata/users.csv
id, name, email
1, 张三, zhangsan@example.com
2, 李四, lisi@example.com
3, 王五, wangwu@example.com
```

```java
@ParameterizedTest
@CsvFileSource(resources = "/testdata/users.csv", numLinesToSkip = 1)
@DisplayName("CSV文件 - 批量创建用户")
void create_whenValidCsvData_thenSuccess(Long id, String name, String email) {
    // ...
}
```

### 5.2 JSON 文件（适合复杂对象）

```json
// src/test/resources/testdata/orders.json
[
  { "orderId": 1, "userId": 100, "amount": 99.9, "status": "PENDING" },
  { "orderId": 2, "userId": 200, "amount": 199.0, "status": "PAID" }
]
```

```java
@ParameterizedTest
@MethodSource("loadOrdersFromJson")
@DisplayName("JSON文件 - 批量处理订单")
void process_whenValidOrderData_thenSuccess(OrderTestData order) {
    // ...
}

static Stream<OrderTestData> loadOrdersFromJson() throws Exception {
    String json = new String(
        OrderServiceTest.class.getResourceAsStream("/testdata/orders.json").readAllBytes());
    return Arrays.stream(new ObjectMapper().readValue(json, OrderTestData[].class));
}
```

### 5.3 内联参数（适合少量简单数据）

```java
@ParameterizedTest
@ValueSource(strings = {"", " ", "   "})
@DisplayName("空白名称 - 抛出异常")
void create_whenBlankName_throwException(String name) { }

@ParameterizedTest
@CsvSource({"1, 张三", "2, 李四"})
@DisplayName("内联CSV - 批量查询用户")
void getById_whenValidId_thenReturnUser(Long id, String name) { }
```

**选择规则**：数据 ≤ 3 条且字段少 → 内联；数据多或字段复杂 → CSV/JSON 文件。

---

## 6. API 测试（MockMvc）

```java
@WebMvcTest(OrderController.class)
@DisplayName("订单控制器")
class OrderControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private OrderService orderService;

    @Test
    @DisplayName("GET /orders/{id} - 返回订单详情")
    void getById_whenExists_thenReturnOrder() throws Exception {
        when(orderService.getById(1L)).thenReturn(new OrderDetailVO(1L, "订单A"));

        mockMvc.perform(get("/web/platform/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.name").value("订单A"));
    }
}
```

---

## 7. 覆盖率报告

```bash
mvn test jacoco:report          # 生成覆盖率报告（target/site/jacoco/index.html）
mvn verify                       # CI 门禁检查（覆盖率不达标构建失败）
mvn spring-javaformat:validate   # 代码格式检查
mvn spring-javaformat:apply      # 自动格式化
```

---

## 8. 禁止事项

- ❌ 测试私有方法 | ❌ Mock 领域对象/DTO/VO | ❌ `@Disabled` 不修复
- ❌ 无断言的测试 | ❌ 测试间共享可变状态 | ❌ `Thread.sleep()`
- ❌ JUnit 原生断言 | ❌ 缺少 `@DisplayName`

---

## 9. 检查清单

- [ ] 行覆盖率 ≥ 80%
- [ ] 测试类、`@Nested` 分组、测试方法均有 `@DisplayName`
- [ ] 方法命名 `methodName_scenario_expectedBehavior`
- [ ] AssertJ 断言，禁止 JUnit 原生断言
- [ ] 每个测试独立，无顺序依赖
- [ ] 仅 Mock 外部依赖
- [ ] API 测试使用 MockMvc + `@WebMvcTest`
- [ ] 参数化场景使用 `@ParameterizedTest`，数据多时用 CSV/JSON 文件
- [ ] 无 `@Disabled` 测试（或关联 issue）
- [ ] JaCoCo 排除 VO/DTO/Entity/Exception/Config 等
- [ ] Spring Format 格式检查通过
