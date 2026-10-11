---
name: backend-api-spec
description: Open Ontology 后端 API 设计规范，定义 URL 路径、HTTP Method、Response 结构、错误编码、分页、DTO 校验与 Swagger 接入等强制约束
version: "1.0"
status: enforced
tech_stack: Java 21 + Spring Boot 3.3 + Spring Cloud Alibaba
created: 2026-09-29
tags: [api, spec, backend, rest, swagger]
---

# Open Ontology 后端 API 规范

## 1. URL 路径规范

### 1.1 路径前缀

| 类型 | 前缀 | 说明 | 示例 |
|---|---|---|---|
| **平台级管理端** | `/web` | 平台全局资源，不归属任何工作空间 | `/web/platform/users` |
| **空间级管理端** | `/web/{workspaceCode}` | 工作空间内资源，路径变量携带空间编码 | `/web/SMART-MFG/ontology/classes` |
| **对外 Open API** | `/api/v1` | 对外开放接口，供第三方系统调用 | `/api/v1/objects/query` |

### 1.2 路径命名规则

- **全部小写**，单词间以 **短横线（kebab-case）** 分隔：`/object-types` ✅ `/objectTypes` ❌
- **资源名使用复数**：`/classes` ✅ `/class` ❌
- **路径层级不超过 4 层**：`/web/{workspaceCode}/data/pipelines` ✅
- **空间编码通过路径变量传递**，禁止放在 query 参数中

### 1.3 路径示例

```
# 平台级
GET  /web/platform/users                    # 用户列表
GET  /web/platform/users/{userId}            # 用户详情
POST /web/platform/users/create              # 创建用户
POST /web/platform/users/update              # 更新用户
POST /web/platform/users/delete              # 删除用户

# 空间级
GET  /web/{workspaceCode}/ontology/classes                # 类列表
GET  /web/{workspaceCode}/ontology/classes/{classId}       # 类详情
POST /web/{workspaceCode}/ontology/classes/create          # 创建类
POST /web/{workspaceCode}/ontology/classes/update          # 更新类
POST /web/{workspaceCode}/ontology/classes/{classId}/props # 属性列表

# 对外 Open API
GET  /api/v1/objects/{objectType}             # 对象查询
GET  /api/v1/objects/{objectType}/{objectId}   # 单个对象详情
POST /api/v1/actions/invoke                    # 执行动作
POST /api/v1/workflows/{workflowId}/trigger    # 触发 Workflow
```

---

## 2. HTTP Method 规范

**仅允许 GET 和 POST**，禁止使用 PUT / PATCH / DELETE。

| Method | 用途 | 示例 |
|---|---|---|
| **GET** | 查询（列表、详情、搜索、统计） | `GET /web/{ws}/ontology/classes` |
| **POST** | 写操作（创建、更新、删除）+ 复杂查询 | `POST /web/{ws}/ontology/classes/create` |

### GET vs POST 选择

| 场景 | Method | URL 后缀 |
|---|---|---|
| 简单查询（ID / 列表分页） | GET | — |
| 复杂查询（多条件、嵌套过滤） | POST | `/search` 或 `/query` |
| 创建 | POST | `/create` |
| 更新 | POST | `/update` |
| 删除 | POST | `/delete` |
| 批量操作 | POST | `/batch-create` / `/batch-delete` |
| 执行动作 | POST | `/invoke` / `/trigger` / `/execute` |

---

## 3. Response Body 规范

### 3.1 统一响应结构

所有 API 返回统一的 JSON 结构：

```json
{
    "code": "000000",
    "msg": "操作成功",
    "data": {},
    "requestId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| `code` | String | ✅ | 业务编码，6 位字符串 |
| `msg` | String | ✅ | 提示信息 |
| `data` | Object / Array / null | ✅ | 业务数据，**必须强类型 VO**，禁止 Map / JsonObject / JSONArray |
| `requestId` | String | ✅ | 请求追踪 ID，与 X-Request-ID 请求头一致 |

### 3.2 code 编码规则

| 编码 | 含义 |
|---|---|
| `000000` | 操作成功（全局唯一成功编码） |
| `XXXXXX`（6 位） | 失败编码 |

**失败编码格式**（6 位）：

```
┌─────────────────────────────────────────┐
│  XX       XX       XX                   │
│  │        │        │                     │
│  模块/服务  功能编码   错误编码             │
│  (2位)    (2位)    (2位)                 │
└─────────────────────────────────────────┘
```

| 段位 | 位数 | 说明 | 分配方 |
|---|---|---|---|
| 模块/服务编码 | 2 位 | 标识所属服务模块 | 架构组统一分配 |
| 功能编码 | 2 位 | 标识具体功能/操作 | 各模块负责人分配 |
| 错误编码 | 2 位 | 标识具体错误类型 | 各模块负责人分配 |

> 模块编码分配表在开发阶段由架构组统一维护，本文档不预分配。

**示例**：

| 场景 | 编码 | 解析 |
|---|---|---|
| Pipeline 创建时名称重复 | `080204` | 模块 `08` + 功能 `02`（创建）+ 错误 `04`（名称重复） |

---

## 4. 分页 Response 规范

### 4.1 分页响应结构

```json
{
    "code": "000000",
    "msg": "操作成功",
    "data": [],
    "pageInfo": {
        "currentPage": 1,
        "pageSize": 10,
        "total": 100,
        "totalPages": 10,
        "hasNext": true,
        "hasPrevious": false
    },
    "requestId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

### 4.2 pageInfo 字段定义

| 字段 | 类型 | 说明 |
|---|---|---|
| `currentPage` | int | 当前页码（从 1 开始） |
| `pageSize` | int | 每页条数 |
| `total` | long | 总记录数 |
| `totalPages` | int | 总页数（= ceil(total / pageSize)） |
| `hasNext` | boolean | 是否有下一页 |
| `hasPrevious` | boolean | 是否有上一页 |

### 4.3 分页请求参数

通过 GET query string 传递：

```
GET /web/{ws}/ontology/classes?currentPage=1&pageSize=20&keyword=设备
```

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|---|---|---|---|---|
| `currentPage` | int | 否 | 1 | 页码，从 1 开始 |
| `pageSize` | int | 否 | 20 | 每页条数，最大 100 |

> 4.4 Java 实现（PageQuery / PageResult）：待代码实现后以源码引用方式补充。

---

## 5. 请求体（DTO）规范

### 5.1 强制强类型

- **请求体**必须定义 DTO 类，禁止使用 `Map<>` / `JsonObject` / `JSONArray`
- **响应体**必须定义 VO 类，禁止使用 `Map<>` / `JsonObject` / `JSONArray`

### 5.2 DTO 校验注解（Java Validation）

所有 DTO 字段必须绑定校验注解：

| 注解 | 用途 | 示例 |
|---|---|---|
| `@NotBlank` | 非空且非空白字符串 | `@NotBlank(message = "名称不能为空")` |
| `@NotNull` | 非 null | `@NotNull(message = "类型不能为空")` |
| `@NotEmpty` | 集合非空 | `@NotEmpty(message = "列表不能为空")` |
| `@Size` | 字符串长度 / 集合大小 | `@Size(min = 1, max = 64)` |
| `@Min` / `@Max` | 数值范围 | `@Min(value = 1, message = "页码最小为 1")` |
| `@Pattern` | 正则匹配 | `@Pattern(regexp = "^[A-Z][a-zA-Z0-9]*$")` |
| `@Email` | 邮箱格式 | `@Email(message = "邮箱格式不正确")` |
| `@Valid` | 嵌套对象校验 | `@Valid private AddressDTO address;` |

---

## 6. Swagger 接入规范

### 强制接入范围

| 类型 | 必须接入 | 注解 |
|---|---|---|
| **Controller** | ✅ | `@Tag`（类级）+ `@Operation`（方法级） |
| **VO**（响应对象） | ✅ | `@Schema`（类级 + 字段级） |
| **DTO**（请求对象） | ✅ | `@Schema`（类级 + 字段级） |

---

## 7. 检查清单

### 代码审查时必须验证

- [ ] URL 前缀正确（`/web` / `/web/{workspaceCode}` / `/api/v1`）
- [ ] HTTP Method 仅使用 GET / POST
- [ ] Response 使用 `Result<T>` 包装，code/msg/data/requestId 四字段齐全
- [ ] data 字段使用强类型 VO，禁止 Map / JsonObject / JSONArray
- [ ] 请求体使用强类型 DTO，禁止 Map / JsonObject / JSONArray
- [ ] DTO 字段均绑定 Java Validation 校验注解
- [ ] Controller 类标注 `@Tag`，方法标注 `@Operation`
- [ ] VO / DTO 字段标注 `@Schema`
- [ ] 错误码符合 6 位编码规则（成功 `000000`）
- [ ] requestId 自动注入（Filter 层 + MDC）
- [ ] 分页接口返回 `PageResult<T>`，包含完整 pageInfo
