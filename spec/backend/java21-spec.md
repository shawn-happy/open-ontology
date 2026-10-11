---
name: backend-java21-spec
description: Open Ontology 后端 Java 21 使用最佳实践，定义虚拟线程、Record、Pattern Matching、Sealed Classes 等新特性使用规范
version: "1.0"
status: enforced
tech_stack: Java 21 LTS + Spring Boot 3.3
created: 2026-09-29
tags: [java21, best-practices, spec, backend]
---

# Java 21 使用最佳实践

## 1. 虚拟线程（Virtual Threads）

### 1.1 适用场景

虚拟线程是 Java 21 最重要的特性，适用于 **I/O 密集型**任务：

| 场景 | 是否使用 | 说明 |
|---|---|---|
| HTTP 请求处理 | ✅ | Spring Boot 3.2+ 默认启用 |
| 数据库查询 | ✅ | JDBC / MyBatis 操作 |
| 外部 API 调用 | ✅ | HTTP 客户端请求 |
| 消息队列收发 | ✅ | Kafka 生产/消费 |
| CPU 密集型计算 | ❌ | 使用平台线程（线程池） |
| synchronized 代码块 | ⚠️ | 可能 pin 住载体线程，改用 ReentrantLock |

### 1.2 Spring Boot 启用

```yaml
spring:
  threads:
    virtual:
      enabled: true   # Spring Boot 3.2+ 一键启用虚拟线程
```

### 1.3 使用规则

- **禁止**手动创建虚拟线程池（虚拟线程不需要池化，按需创建）
- **禁止**在虚拟线程中使用 `synchronized`（会 pin 住载体线程）
- 需要 `synchronized` 时改用 `ReentrantLock`
- **禁止**在虚拟线程中使用 `ThreadLocal` 存储大量数据（改用 `ScopedValue`）

```java
// ✅ 正确：I/O 操作使用虚拟线程
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
executor.submit(() -> {
    var result = httpClient.send(request, BodyHandlers.ofString());
    return result.body();
});

// ❌ 禁止：虚拟线程中 synchronized（会 pin）
synchronized (lock) {
    dbConnection.execute(query);  // JDBC 调用
}

// ✅ 正确：改用 ReentrantLock
private final ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    dbConnection.execute(query);
} finally {
    lock.unlock();
}
```

---

## 2. Record 类型

### 2.1 适用场景

Record 适用于**不可变数据传输对象**：

| 场景 | 是否使用 | 说明 |
|---|---|---|
| 配置类 | ✅ | 不可变配置 |
| 方法返回值（多值） | ✅ | 替代 Object[] / Map |
| 简单 DTO（无校验） | ✅ | 只读传输 |
| 需要校验的 DTO | ❌ | Record 不支持 Java Validation 注解绑定 |
| 可变 VO | ❌ | Record 字段不可变 |
| Entity | ❌ | Entity 需要可变 |

### 2.2 使用示例

```java
// ✅ 配置类
public record DorisConfig(
    String host,
    int port,
    String database,
    String username
) {}

// ✅ 方法返回多值
public record QueryResult<T>(List<T> data, long total, int costMs) {}

public QueryResult<ClassListVO> listClasses(Query query) {
    var data = classMapper.selectList(wrapper);
    var total = classMapper.selectCount(wrapper);
    return new QueryResult<>(data, total, 38);
}

// ❌ 禁止：需要校验的 DTO 用 Record
public record CreateUserDTO(
    @NotBlank String name,   // ⚠️ Record 参数校验支持有限
    @Email String email
) {}

// ✅ 正确：DTO 继续使用普通类
public class CreateUserDTO {
    @NotBlank private String name;
    @Email private String email;
}
```

---

## 3. Pattern Matching（模式匹配）

### 3.1 instanceof 模式匹配

```java
// ✅ Java 21 模式匹配
if (obj instanceof String s && !s.isBlank()) {
    processString(s);
}

// ❌ 旧写法
if (obj instanceof String) {
    String s = (String) obj;
    if (!s.isBlank()) {
        processString(s);
    }
}
```

### 3.2 switch 模式匹配

```java
// ✅ 类型模式匹配
String result = switch (node) {
    case StringNode s  -> "字符串: " + s.value();
    case IntNode i     -> "整数: " + i.value();
    case NullNode n    -> "空值";
    default            -> "未知类型";
};

// ✅ 守护模式（Guarded Pattern）
String classify = switch (value) {
    case Integer i when i < 0   -> "负数";
    case Integer i when i == 0  -> "零";
    case Integer i              -> "正数: " + i;
    case String s when s.isEmpty() -> "空字符串";
    case String s               -> "字符串: " + s;
    default                     -> "其他";
};
```

### 3.3 使用规则

- 优先使用 switch 模式匹配替代 `if-else instanceof` 链
- 复杂类型分支使用 sealed class + switch（见下节）

---

## 4. Sealed Classes（密封类）

适用于**固定子类型集合**的场景：

```java
// ✅ 密封类：限定子类
public sealed interface PipelineNode
    permits SourceNode, TransformNode, SinkNode {}

public record SourceNode(String dataSourceId) implements PipelineNode {}
public record TransformNode(String functionId) implements PipelineNode {}
public record SinkNode(String targetDatasetId) implements PipelineNode {}

// switch 可以省略 default（编译器检查完备性）
String describe(PipelineNode node) {
    return switch (node) {
        case SourceNode s    -> "数据源: " + s.dataSourceId();
        case TransformNode t -> "转换: " + t.functionId();
        case SinkNode s      -> "目标: " + s.targetDatasetId();
        // 无需 default，编译器已知所有子类型
    };
}
```

### 适用场景

| 场景 | 说明 |
|---|---|
| Pipeline 节点类型 | 固定的 Source / Transform / Sink |
| 值约束规则类型 | 正则 / 范围 / 枚举 / 外键 / 函数 |
| 动作类型 | 对象写入 / 函数调用 / 通知 / HTTP |

---

## 5. Sequenced Collections（有序集合）

Java 21 新增 `SequencedCollection` / `SequencedSet` / `SequencedMap` 接口：

```java
// ✅ 统一的首尾元素访问
SequencedCollection<String> list = new ArrayList<>(List.of("a", "b", "c"));
list.getFirst();   // "a"
list.getLast();    // "c"
list.addFirst("z");
list.reversed();   // ["c", "b", "a"]

// ✅ SequencedMap（保持插入顺序）
SequencedMap<String, Integer> map = new LinkedHashMap<>();
map.put("a", 1);
map.put("b", 2);
map.firstEntry();  // a=1
map.lastEntry();   // b=2
```

### 使用规则

- 需要访问首尾元素时，使用 `getFirst()` / `getLast()` 替代 `get(0)` / `get(size()-1)`
- 需要反转集合时，使用 `reversed()` 替代 `Collections.reverse()`

---

## 6. Text Blocks（文本块）

```java
// ✅ SQL 查询
String sql = """
    SELECT c.id, c.name, c.api_name
    FROM oo_ontology_class c
    WHERE c.workspace_id = ?
      AND c.is_deleted = 0
    ORDER BY c.created_at DESC
    """;

// ✅ JSON 模板
String json = """
    {
        "name": "%s",
        "apiName": "%s",
        "kind": "%s"
    }
    """.formatted(name, apiName, kind);

// ✅ 多行日志
log.info("""
    Pipeline 执行完成:
      pipeline: {}
      duration: {}ms
      records:  {}
    """, pipelineName, duration, recordCount);
```

### 使用规则

- SQL、JSON、HTML 模板等多行文本使用 Text Block
- 字符串拼接优先使用 `.formatted()` 或 `STR.` 模板（Preview）

---

## 7. var 局部变量类型推断

```java
// ✅ 右侧类型明显时使用 var
var list = new ArrayList<String>();           // 类型明显
var map = new HashMap<String, List<Integer>>(); // 泛型嵌套，var 更清晰
var stream = users.stream().filter(User::isActive);

// ❌ 右侧类型不明显时禁止 var
var result = service.process(data);    // 看不出返回类型
var x = getData();                     // 含义不明

// ❌ 禁止 var 用于 null
var obj = null;  // 编译错误

// ✅ Lambda 参数可以使用 var（需要注解时）
list.stream().map((@NotNull var s) -> s.trim());
```

### 使用规则

- 构造函数右侧类型明显时，使用 `var`
- 方法返回值不明显时，显式声明类型
- 禁止 `var` 用于 `null` 初始化

---

## 8. 不可变集合

```java
// ✅ 使用工厂方法创建不可变集合
List<String> list = List.of("a", "b", "c");
Set<Integer> set = Set.of(1, 2, 3);
Map<String, Integer> map = Map.of("a", 1, "b", 2);

// ✅ 从可变集合转不可变
List<String> immutable = List.copyOf(mutableList);

// ❌ 禁止 Collections.unmodifiableXxx（运行时包装，非真正不可变）
```

---

## 9. 异常处理

### 9.1 多异常捕获

```java
// ✅ 多异常合并
try {
    process();
} catch (IOException | SQLException e) {
    log.error("处理失败", e);
    throw new BusinessException("030101", "数据处理异常");
}
```

### 9.2 try-with-resources

```java
// ✅ 自动关闭资源
try (var reader = new BufferedReader(new FileReader(path));
     var writer = new BufferedWriter(new FileWriter(outPath))) {
    writer.write(reader.readLine());
}
```

- 所有 `AutoCloseable` 资源必须使用 try-with-resources
- 禁止手动 `finally { close() }`

---

## 10. 并发工具选择

| 场景 | Java 21 推荐 | 禁止 |
|---|---|---|
| 异步执行 | 虚拟线程 `newVirtualThreadPerTaskExecutor` | `newFixedThreadPool`（I/O 场景） |
| 定时任务 | `ScheduledExecutorService` | `Timer` |
| 并发容器 | `ConcurrentHashMap` | `Collections.synchronizedMap` |
| 原子操作 | `AtomicReference` / `LongAdder` | `synchronized` 计数器 |
| 锁 | `ReentrantLock` | `synchronized`（虚拟线程场景） |
| 信号量 | `Semaphore` | `wait/notify` |

---

## 11. 与 Spring Cloud Alibaba 兼容性

| 特性 | 兼容性 | 说明 |
|---|---|---|
| 虚拟线程 | ✅ 可用 | Spring Boot 3.2+ 支持，但部分 SDK（如旧版 Dubbo）可能 pin |
| Record | ✅ 可用 | 用于配置类、方法返回值，不用于 DTO |
| Pattern Matching | ✅ 可用 | 纯语言特性，无框架依赖 |
| Sealed Classes | ✅ 可用 | 纯语言特性 |
| Text Blocks | ✅ 可用 | 纯语言特性 |

> 灰度策略：虚拟线程先在 `oo-audit`（低优先级服务）启用，验证稳定后推广全服务。

---

## 12. 检查清单

- [ ] I/O 密集型任务使用虚拟线程，CPU 密集型使用平台线程
- [ ] 虚拟线程中禁止 `synchronized`，改用 `ReentrantLock`
- [ ] 虚拟线程中禁止 `ThreadLocal` 存储大数据，改用 `ScopedValue`
- [ ] 不可变数据传输使用 Record
- [ ] 类型分支使用 switch 模式匹配替代 instanceof 链
- [ ] 固定子类型集合使用 sealed class
- [ ] 多行文本使用 Text Block
- [ ] `var` 仅用于类型明显的场景
- [ ] 资源关闭使用 try-with-resources
