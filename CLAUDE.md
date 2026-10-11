# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

**Prism**（Prism Ontology Platform，本体语义平台）——以本体（Ontology）为核心的企业数据语义化中台产品。名称寓意：将散乱白光（原始数据）折射为有序光谱（语义对象）。设计参考 Palantir Foundry（Object Types / Link Types / Shared Property Types / Action Types / Functions）。

产品为中文 UI。核心概念：工作空间（顶层隔离单元）→ 本体资产（类/接口/抽象类/枚举类、属性、关联关系、值约束、动作、函数）、数据资产（数据源/数据集/Pipeline）、安全策略（标记/数据权限/加密/脱敏）、应用资产（API Key/Workflow/Skills）、审计。

## 目录结构

- `code/server/` — 后端 Maven 多模块项目（Java 21 + Spring Boot 3.5 + Spring Cloud Alibaba）：
  - `pom.xml` — Parent POM，统一管理依赖版本。
  - `prism-common/` — 公共模块（BaseEntity、Result/PageResult/PageQuery、ErrorCode/BusinessException/GlobalExceptionHandler、MybatisPlusConfig、JacksonConfig、RequestContextHolder）。
  - `prism-platform/` — Web 管理端服务（用户、角色、部门、工作空间、审批），端口 8100，URL 前缀 `/web/platform`。
- `code/web/` — 前端项目（React 18 + Ant Design 5 + UmiJS 4 / @umijs/max），代理 `/web` 和 `/api` 到后端 8100 端口。
- `docs/prd/产品功能设计.md` — PRD，章节与原型页面一一映射（每节标注对应原型 HTML 路径）。
- `docs/prototype/` — HTML/JS 高保真原型：
  - `index.html` 为入口导航页；目录与侧边栏父菜单一一对应：`platform/`、`ontology/`、`data/`、`security/`、`application/`、`audit/`、`workspace/`。
  - 共享布局与交互在 `assets/app.js`（`GLOBAL_NAV` / `SPACE_NAV` 菜单配置、`App.mount()` 页面挂载）+ `assets/style.css`。
- `docs/palantir/` — 已爬取的 Palantir Foundry 官方文档快照（静态 HTML + `_resources/` 图片），按板块分目录（`ontology/`、`security/`、`api/` 等），`index.html` 为目录索引。仅作设计参考，勿修改其内容。爬虫脚本已移除，如需重新爬取需恢复工具链（uv + requests + markdown，Python ≥3.12）。
- `docs/arch/技术选型报告.md` — 技术选型报告（React + Ant Design 前端 / Java 21 + Spring Boot 3 + Spring Cloud Alibaba 后端 / MySQL + Doris + NebulaGraph + Redis + MinIO + Kafka 存储矩阵 / DolphinScheduler 调度 / Spark + Flink 数据开发）。
- `spec/backend/` — 后端开发强制规范（所有后端代码必须遵循）：
  - `api-spec.md` — API 设计规范：URL 路径（`/web` 平台级 / `/web/{workspaceCode}` 空间级 / `/api/v1` Open API）、仅 GET/POST、统一响应结构（code/msg/data/requestId）、6 位错误编码、分页、DTO 校验、Swagger 接入。
  - `database-spec.md` — 数据库设计规范：单库、表名 `{模块}_{实体}`（无 `oo_` 前缀）、自增主键、`workspace_code` 空间隔离、仅普通索引（禁止 UNIQUE/FOREIGN KEY/CHECK，业务规则由应用层保证）、Flyway DDL 管理。
  - `mybatis-plus-spec.md` — MyBatis-Plus 规范：Entity 继承 BaseEntity + `@TableId(IdType.AUTO)`、Mapper 继承 BaseMapper、**Service 禁止继承 IService/ServiceImpl**（纯业务接口，构造注入 Mapper）、LambdaQueryWrapper、多租户拦截。
  - `jackson-spec.md` — Jackson 规范：全局 camelCase + NON_NULL、Long ID 序列化 String 防精度丢失、`java.time` 统一、多态序列化（`@JsonTypeInfo` + `@JsonSubTypes`，type 字段大写 snake_case）、脱敏序列化器。
  - `lombok-spec.md` — Lombok 规范：构造注入用 `@RequiredArgsConstructor` + `private final`（禁止 `@Autowired`）、子类 `@Data` 必须 `@EqualsAndHashCode(callSuper=true)`、继承场景用 `@SuperBuilder`、禁止 `@SneakyThrows` / `@Cleanup`。
  - `java21-spec.md` — Java 21 最佳实践：虚拟线程（I/O 场景，禁止 synchronized 改用 ReentrantLock，禁止 ThreadLocal 存大对象）、Record、Pattern Matching、Sealed Classes、Text Blocks、var 规则。
  - `unit-test-spec.md` — 单元测试规范：行覆盖率 ≥ 80%（JaCoCo）、JUnit 5 + Mockito + AssertJ（禁止 JUnit 原生断言）、AAA 模式、Testcontainers 集成测试、MockMvc API 测试、VO/DTO/Entity/Exception 排除覆盖统计、Spring Format 格式检查。

## 运行命令

### 后端

```bash
# 编译
cd code/server && mvn clean compile

# 启动 prism-platform 服务
cd code/server && mvn spring-boot:run -pl prism-platform

# 运行测试 + 覆盖率
cd code/server && mvn test

# 查看覆盖率报告
# target/site/jacoco/index.html

# 健康检查
curl http://localhost:8100/web/platform/health
```

### 前端

```bash
cd code/web && npm install && npm start
```

### 原型（高保真 HTML）

直接用浏览器打开 HTML（或 `python3 -m http.server` 后访问）。

## 关键约定（硬约束）

新增/调整功能页面时，必须**三处同步**：

1. `docs/prototype/assets/app.js` 中对应的 NAV 配置（`GLOBAL_NAV` 平台级 / `SPACE_NAV` 空间级）。
2. 对应父菜单目录下新建 HTML 页面（使用 `assets/app.js` 的 `App.mount()` 挂载，参考现有页面）。
3. `docs/prd/产品功能设计.md` 的章节及「功能结构与原型页面映射」表。

- 父菜单 = 文件目录：每个一级功能模块一个独立目录，每个子功能是独立 HTML 页面（用户明确拒绝把子功能合并为单页 Tabs）。
- 子目录页面通过 `app.js` 中的 `ROOT` 前缀逻辑处理相对路径，新增一级目录时需同步更新该正则。
- 后端代码必须遵循 `spec/backend/` 下全部规范文件，编写或审查代码时主动对照检查清单。
