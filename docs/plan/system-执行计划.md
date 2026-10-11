# 系统管理模块 - 执行计划

**版本**: v1.1  
**日期**: 2026-09-29  
**关联技术文档**: `docs/design/system-技术实现文档.md` (v1.1)

---

## 📋 执行概览

本计划覆盖系统管理模块的后端和前端实现，按依赖关系分为 4 个阶段：

1. **数据库层**：Flyway DDL 脚本
2. **后端基础层**：Entity、Mapper、DTO、配置类
3. **后端业务层**：Service、Controller、安全框架、AOP 切面
4. **前端层**：页面组件、路由、服务调用

**重要说明**：
- ✅ 本计划**不包含单元测试**（单测在归档阶段统一生成）
- ✅ 并行策略：后端基础层完成后，后端业务层与前端层可并行开发
- ✅ 所有代码必须遵循 `spec/backend/` 规范
- ✅ Flyway 脚本**仅编写 SQL 文件**，不配置 Maven 插件执行（手动导入或通过 CI 执行）
- ✅ 共享基础设施代码（Security、AOP、注解、枚举、配置）放入 `prism-common` 模块，便于其他业务模块复用

### 模块职责划分

| 模块 | 职责 | 包路径 |
|---|---|---|
| `prism-common` | 共享基础设施：Spring Security + JWT 框架、AOP 审计切面、注解、枚举、全局配置 | `com.prism.common.*` |
| `prism-platform` | 系统管理业务代码：Entity、Mapper、DTO/VO、Service、Controller、Flyway DDL | `com.prism.platform.*` |

---

## 阶段 1：数据库层

### 任务 1.1：创建 Flyway 数据库脚本

**目标**：创建系统管理模块的全部数据库表结构（仅编写 SQL 文件，不执行）

**文件**：
- 📝 新建：`code/server/prism-platform/src/main/resources/db/migration/V1__init_system.sql`

**内容**：
1. 创建 10 张表（使用 `CREATE TABLE IF NOT EXISTS` 保证幂等）：
   - `system_user` - 用户表
   - `system_role` - 角色表
   - `system_menu` - 菜单权限表
   - `system_dept` - 部门表
   - `system_user_role` - 用户角色关联表
   - `system_role_menu` - 角色菜单关联表
   - `system_user_dept` - 用户部门关联表
   - `system_audit_log` - 审计日志表
   - `system_login_log` - 登录日志表
   - `system_exec_log` - 执行日志表

2. 插入初始化数据（使用 `INSERT ... WHERE NOT EXISTS` 保证幂等）：
   - 根部门（集团）
   - 内置角色（超级管理员、系统运维、空间管理员）
   - 初始管理员用户（admin / onto12345678!）
   - 系统管理菜单树（1 个目录 + 5 个菜单 + 25 个按钮）
   - 超级管理员角色关联全部菜单

> **注意**：此脚本仅编写，不配置 Flyway Maven 插件自动执行。部署时由运维手动执行或通过 CI/CD 流水线执行。

**依赖**：无

---

## 阶段 2：后端基础层

### 任务 2.1：创建 Entity 实体类

**目标**：定义 10 个数据库表对应的 Entity 类

**文件**：
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/entity/SystemUser.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/entity/SystemRole.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/entity/SystemMenu.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/entity/SystemDept.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/entity/SystemUserRole.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/entity/SystemRoleMenu.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/entity/SystemUserDept.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/entity/SystemAuditLog.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/entity/SystemLoginLog.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/entity/SystemExecLog.java`

**实现要点**：
- 继承 `BaseEntity`（id、createdAt、updatedAt、createdBy、updatedBy、deleted）
- 使用 `@TableId(type = IdType.AUTO)` 自增主键
- 使用 `@Data`、`@EqualsAndHashCode(callSuper = true)`（继承 BaseEntity）
- 使用 `@TableName("system_user")` 指定表名
- 字段使用 `@TableField`（如逻辑删除字段 `deleted`）
- 枚举字段使用 String 类型（如 status: ENABLED/DISABLED）

**依赖**：任务 1.1

**验收标准**：
- [ ] `mvn clean compile` 通过
- [ ] Entity 类与数据库表字段一一对应
- [ ] 遵循 `spec/backend/mybatis-plus-spec.md` 和 `spec/backend/lombok-spec.md`

---

### 任务 2.2：创建 Mapper 接口

**目标**：定义 10 个 Mapper 接口

**文件**：
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/mapper/SystemUserMapper.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/mapper/SystemRoleMapper.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/mapper/SystemMenuMapper.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/mapper/SystemDeptMapper.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/mapper/SystemUserRoleMapper.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/mapper/SystemRoleMenuMapper.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/mapper/SystemUserDeptMapper.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/mapper/SystemAuditLogMapper.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/mapper/SystemLoginLogMapper.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/mapper/SystemExecLogMapper.java`

**实现要点**：
- 继承 `BaseMapper<Entity>`
- 使用 `@Mapper` 注解
- 定义必要的自定义查询方法（如 `selectByUsername`、`selectByPhone`、`selectByEmail` 等）

**依赖**：任务 2.1

**验收标准**：
- [ ] `mvn clean compile` 通过
- [ ] 遵循 `spec/backend/mybatis-plus-spec.md`（继承 BaseMapper、禁止继承 IService/ServiceImpl）

---

### 任务 2.3：创建 DTO 和 VO 类

**目标**：定义请求参数 DTO 和响应 VO

**文件**：

**DTO（请求参数）**：
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/LoginDTO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/ChangePasswordDTO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/CreateUserDTO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/UpdateUserDTO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/UserPageQuery.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/CreateRoleDTO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/UpdateRoleDTO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/RolePermissionDTO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/RolePageQuery.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/CreateMenuDTO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/UpdateMenuDTO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/CreateDeptDTO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/UpdateDeptDTO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/DeptTreeQuery.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/AuditLogQuery.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/LoginLogQuery.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/ExecLogQuery.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/UpdateProfileDTO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/ForgotPasswordDTO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/dto/RefreshTokenDTO.java`

**VO（响应结果）**：
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/vo/LoginVO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/vo/CaptchaVO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/vo/UserInfoVO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/vo/UserListVO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/vo/UserDetailVO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/vo/RoleListVO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/vo/RoleDetailVO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/vo/MenuTreeVO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/vo/DeptTreeVO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/vo/AuditLogVO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/vo/LoginLogVO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/vo/ExecLogVO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/vo/UserProfileVO.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/vo/ImportResultVO.java`

**实现要点**：
- DTO 使用 `@Data`，Query 类继承 `PageQuery`
- 使用 `@NotBlank`、`@Size`、`@Pattern` 等注解进行参数校验
- VO 使用 `@Data`，Long 类型字段添加 `@JsonSerialize(using = ToStringSerializer.class)` 防止精度丢失
- 遵循 `spec/backend/jackson-spec.md`（camelCase、NON_NULL）

**依赖**：无

**验收标准**：
- [ ] `mvn clean compile` 通过
- [ ] DTO 字段与 API 设计文档一致
- [ ] 参数校验注解符合 PRD 表单校验规则

---

### 任务 2.4：创建配置类、枚举和常量

**目标**：定义全局共享配置类（放 `prism-common`）和模块专属常量（放 `prism-platform`）

**文件 — prism-common（全局共享）**：
- 📝 新建：`code/server/prism-common/src/main/java/com/prism/common/config/SecurityConfig.java`
- 📝 新建：`code/server/prism-common/src/main/java/com/prism/common/config/AsyncConfig.java`
- 📝 新建：`code/server/prism-common/src/main/java/com/prism/common/enums/UserStatus.java`
- 📝 新建：`code/server/prism-common/src/main/java/com/prism/common/enums/RoleType.java`
- 📝 新建：`code/server/prism-common/src/main/java/com/prism/common/enums/DataScope.java`
- 📝 新建：`code/server/prism-common/src/main/java/com/prism/common/enums/MenuType.java`
- 📝 新建：`code/server/prism-common/src/main/java/com/prism/common/enums/LogType.java`

**文件 — prism-platform（模块专属）**：
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/constants/SystemConstants.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/exception/SystemErrorCode.java`

**实现要点**：
- `SecurityConfig`：配置 `SecurityFilterChain`，放行登录/验证码/公开接口，启用 JWT 过滤器（全局共享，其他模块也需认证）
- `AsyncConfig`：配置 `@Async` 线程池（使用虚拟线程 Executor）（全局共享）
- 枚举类（`prism-common`）：`UserStatus`（ENABLED/DISABLED）、`RoleType`（GLOBAL/WORKSPACE）、`DataScope`（ALL/DEPT_AND_CHILDREN/DEPT/SELF/CUSTOM）、`MenuType`（DIRECTORY/MENU/BUTTON）、`LogType`（CREATE/UPDATE/DELETE/ENABLE/DISABLE/LOGIN 等）
- `SystemConstants`（`prism-platform`）：定义模块专属常量（如 admin 用户编码、超管角色编码、初始密码、Redis Key 前缀等）
- 枚举类（`prism-common`）：使用 `String` 值（如 ENABLED/DISABLED、GLOBAL/WORKSPACE、DIRECTORY/MENU/BUTTON 等）
- `SystemErrorCode`（`prism-platform`）：扩展 `ErrorCode`，定义系统管理模块错误码（010101 ~ 010601）

**依赖**：无

**验收标准**：
- [ ] `mvn clean compile` 通过（prism-common 和 prism-platform 均编译通过）
- [ ] 错误码与技术实现文档第 1.4 节一致
- [ ] 枚举类放在 prism-common，模块常量和错误码放在 prism-platform

---

## 阶段 3：后端业务层

### 任务 3.1：实现 Spring Security + JWT 认证框架

**目标**：实现 JWT Token 生成/验证、Spring Security 过滤器链（全局共享，放 `prism-common`）

**文件 — prism-common（全局共享）**：
- 📝 新建：`code/server/prism-common/src/main/java/com/prism/common/security/JwtTokenProvider.java`
- 📝 新建：`code/server/prism-common/src/main/java/com/prism/common/security/JwtAuthenticationFilter.java`
- 📝 新建：`code/server/prism-common/src/main/java/com/prism/common/security/CustomUserDetailsService.java`（接口定义，由 prism-platform 提供实现）
- 📝 新建：`code/server/prism-common/src/main/java/com/prism/common/security/SecurityUtils.java`
- 📝 新建：`code/server/prism-common/src/main/java/com/prism/common/security/annotation/RequirePermission.java`
- 📝 修改：`code/server/prism-common/src/main/java/com/prism/common/config/SecurityConfig.java`（完善过滤器链配置）

**文件 — prism-platform（模块专属实现）**：
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/security/CustomUserDetailsServiceImpl.java`（实现 CustomUserDetailsService 接口）

**实现要点**：
- `JwtTokenProvider`：生成 Access Token（2h）和 Refresh Token（7d/30d），使用 `io.jsonwebtoken:jjwt` 库，Token 存入 Redis
- `JwtAuthenticationFilter`：继承 `OncePerRequestFilter`，解析 Token、从 Redis 验证、加载权限、设置 `SecurityContext`
- `CustomUserDetailsService`：实现 `UserDetailsService`，从数据库加载用户权限
- `SecurityUtils`：提供 `getCurrentUserId()`、`getCurrentUsername()` 等静态方法
- `@RequirePermission`：自定义注解，配合 `@PreAuthorize` 实现权限校验
- `@LoginLog`、`@AuditLog`：自定义注解，配合 AOP 切面实现日志记录

**依赖**：任务 2.1、2.2、2.3、2.4

**验收标准**：
- [ ] `mvn clean compile` 通过
- [ ] JWT Token 可正常生成和验证
- [ ] Spring Security 过滤器链正确配置
- [ ] 公开接口（登录/验证码）可匿名访问

---

### 任务 3.2：实现 AOP 审计日志切面

**目标**：使用 Spring AOP + @Async 实现审计日志自动记录（全局共享，放 `prism-common`）

**文件 — prism-common（全局共享）**：
- 📝 新建：`code/server/prism-common/src/main/java/com/prism/common/aspect/AuditLogAspect.java`
- 📝 新建：`code/server/prism-common/src/main/java/com/prism/common/aspect/LoginLogAspect.java`
- 📝 新建：`code/server/prism-common/src/main/java/com/prism/common/aspect/AuditLog.java`（注解）
- 📝 新建：`code/server/prism-common/src/main/java/com/prism/common/aspect/LoginLog.java`（注解）

**实现要点**：
- `AuditLogAspect`：拦截 `@AuditLog` 注解的方法，记录操作者、模块、操作内容、结果、IP、User-Agent、请求 ID
- `LoginLogAspect`：拦截 `@LoginLog` 注解的方法，记录登录信息
- 使用 `@Async` 异步写入数据库（不阻塞主业务）
- 使用 `RequestContextHolder` 获取当前用户信息
- 支持记录方法参数和返回值（JSON 格式存入 detail 字段）

**依赖**：任务 2.1（AuditLog/LoginLog Entity）、2.2（Mapper）、3.1（SecurityUtils）

**验收标准**：
- [ ] `mvn clean compile` 通过
- [ ] AOP 切面可正确拦截注解方法
- [ ] 日志异步写入数据库

---

### 任务 3.3：实现认证服务（AuthService）

**目标**：实现登录、登出、Token 刷新、验证码、忘记密码等功能

**文件**：
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/service/AuthService.java`（接口）
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/service/impl/AuthServiceImpl.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/service/CaptchaService.java`（接口）
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/service/impl/CaptchaServiceImpl.java`

**实现要点**：
- `login()`：验证码校验 → 查询用户 → 检查状态 → 检查锁定 → 验证密码 → 生成双 Token → 记录登录日志（@LoginLog）
- `logout()`：清除 Redis 中的 Token
- `refreshToken()`：验证 Refresh Token，生成新 Access Token
- `getCaptcha()`：生成 4 位图形验证码（字母+数字），存入 Redis（TTL 5 分钟）
- `forgotPassword()`：验证手机号/邮箱，重置密码为 `onto12345678!`
- `changePasswordFirstTime()`：首次登录强制修改密码，更新 `pwd_update_time`

**登录流程顺序**（重要）：
1. 验证码校验（失败不消耗登录失败次数）
2. 查询用户（不存在则记录失败次数，返回"用户名或密码错误"）
3. 检查账号状态（停用则返回"账号已停用"）
4. 检查锁定状态（已锁定则返回"账号已锁定"）
5. 验证密码（失败则记录失败次数，>=5 次则锁定 30 分钟）
6. 检查首次登录（`pwd_update_time` 为空则返回 `firstLogin=true`）
7. 检查密码过期（可选，根据配置）
8. 生成双 Token 并存入 Redis
9. 单点互踢（如开启，清除旧设备 Token）
10. 更新最后登录时间
11. 记录登录日志（@LoginLog）

**依赖**：任务 2.1、2.2、3.1、3.2

**验收标准**：
- [ ] `mvn clean compile` 通过
- [ ] 登录流程符合技术实现文档 3.1 节流程图
- [ ] 登录失败 5 次后账号锁定 30 分钟
- [ ] 首次登录返回 `firstLogin=true` 标记
- [ ] 验证码校验失败不消耗登录失败次数

---

### 任务 3.4：实现用户管理服务（UserService）

**目标**：实现用户 CRUD、分配角色/部门、重置密码、批量导入导出

**文件**：
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/service/UserService.java`（接口）
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/service/impl/UserServiceImpl.java`

**实现要点**：
- `create()`：校验用户名/手机号/邮箱唯一性，BCrypt 加密初始密码，保存用户、关联角色、关联部门（@AuditLog）
- `update()`：校验 admin 用户名不可修改，更新用户信息（@AuditLog）
- `delete()`：逻辑删除（@AuditLog）
- `getById()`：查询用户详情（含角色名称、部门名称）
- `page()`：分页查询，支持关键词、部门、状态筛选
- `enableDisable()`：启停用（admin 不可停用），清除用户权限缓存（@AuditLog）
- `resetPassword()`：重置密码为 `onto12345678!`，清除 `pwd_update_time`（@AuditLog）
- `assignRoles()`：分配角色（超管角色仅 admin 可分配），清除用户权限缓存（@AuditLog）
- `assignDept()`：分配部门（@AuditLog）
- `importUsers()`：解析 Excel，校验数据，批量插入，返回导入结果（@AuditLog）
- `exportUsers()`：按筛选条件导出 Excel

**依赖**：任务 2.1、2.2、3.1、3.2

**验收标准**：
- [ ] `mvn clean compile` 通过
- [ ] admin 用户不可停用、用户名不可修改
- [ ] 超管角色仅可分配给 admin
- [ ] 重置密码后用户下次登录触发强制修改密码流程
- [ ] 分配角色/部门后清除 Redis 权限缓存

---

### 任务 3.5：实现角色管理服务（RoleService）

**目标**：实现角色 CRUD、权限配置、用户分配

**文件**：
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/service/RoleService.java`（接口）
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/service/impl/RoleServiceImpl.java`

**实现要点**：
- `create()`：校验角色名称/编码唯一性，保存角色（@AuditLog）
- `update()`：内置角色不可修改，更新角色信息（@AuditLog）
- `delete()`：内置角色不可删除（@AuditLog）
- `getById()`：查询角色详情（含关联菜单 ID 列表）
- `page()`：分页查询，支持角色名称/编码筛选
- `enableDisable()`：内置角色不可停用（@AuditLog）
- `configurePermissions()`：配置菜单权限（删除旧关联、插入新关联），配置数据权限，清除关联用户的权限缓存（@AuditLog）
- `assignUsers()`：分配用户（超管角色仅 admin 可分配），清除用户权限缓存（@AuditLog）

**依赖**：任务 2.1、2.2、3.1、3.2

**验收标准**：
- [ ] `mvn clean compile` 通过
- [ ] 内置角色（super_admin、system_ops、workspace_admin）不可修改、不可停用、不可删除
- [ ] 超管角色仅可分配给 admin
- [ ] 配置权限后清除关联用户的 Redis 权限缓存

---

### 任务 3.6：实现菜单权限服务（MenuService）

**目标**：实现菜单树 CRUD、启停用

**文件**：
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/service/MenuService.java`（接口）
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/service/impl/MenuServiceImpl.java`

**实现要点**：
- `create()`：校验权限标识唯一性（如提供），校验层级合法性（目录→菜单→按钮），保存菜单（@AuditLog）
- `update()`：更新菜单信息（@AuditLog）
- `delete()`：校验无下级节点，逻辑删除（@AuditLog）
- `tree()`：查询全部菜单，构建树形结构
- `enableDisable()`：启停用菜单（@AuditLog）
- `getByPermission()`：根据权限标识查询菜单

**层级规则**：
- 目录（DIRECTORY）可包含目录、菜单、按钮
- 菜单（MENU）可包含按钮
- 按钮（BUTTON）不可包含任何子节点

**依赖**：任务 2.1、2.2、3.2

**验收标准**：
- [ ] `mvn clean compile` 通过
- [ ] 权限标识全局唯一
- [ ] 菜单层级符合规则（按钮不可作为菜单的父节点）
- [ ] 树形结构正确构建

---

### 任务 3.7：实现部门管理服务（DeptService）

**目标**：实现部门树 CRUD、分配用户、设置负责人、转移、排序

**文件**：
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/service/DeptService.java`（接口）
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/service/impl/DeptServiceImpl.java`

**实现要点**：
- `create()`：校验部门名称同级唯一性、编码全局唯一性，计算 path 和 depth，保存部门（@AuditLog）
- `update()`：更新部门信息（@AuditLog）
- `delete()`：根节点不可删除（@AuditLog）
- `tree()`：查询全部部门，构建树形结构
- `enableDisable()`：停用时查询下属用户数量，批量迁移用户至上级部门，递归停用子部门（@AuditLog）
- `assignUsers()`：分配用户到部门（@AuditLog）
- `setLeader()`：设置部门负责人（@AuditLog）
- `transfer()`：部门转移（校验不可转移到自身或下级），更新 path（@AuditLog）
- `updateSort()`：批量更新排序（@AuditLog）

**依赖**：任务 2.1、2.2、3.2

**验收标准**：
- [ ] `mvn clean compile` 通过
- [ ] 部门名称同级唯一、编码全局唯一
- [ ] 停用部门时用户自动迁移至上级部门
- [ ] 部门转移时更新子节点的 path 字段

---

### 任务 3.8：实现日志查询服务（LogService）

**目标**：实现审计日志、登录日志、执行日志的查询和导出

**文件**：
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/service/LogService.java`（接口）
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/service/impl/LogServiceImpl.java`

**实现要点**：
- `pageAuditLogs()`：分页查询审计日志，支持模块、类型、时间范围、关键词筛选
- `pageLoginLogs()`：分页查询登录日志，支持用户名、时间范围、结果筛选
- `pageExecLogs()`：分页查询执行日志，支持类型、状态、时间范围筛选
- `exportLogs()`：按筛选条件导出 CSV

**依赖**：任务 2.1、2.2

**验收标准**：
- [ ] `mvn clean compile` 通过
- [ ] 分页查询支持多条件筛选
- [ ] 导出 CSV 格式正确

---

### 任务 3.9：实现用户自服务（ProfileService）

**目标**：实现个人信息查看/修改、修改密码、上传头像

**文件**：
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/service/ProfileService.java`（接口）
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/service/impl/ProfileServiceImpl.java`

**实现要点**：
- `getProfile()`：获取当前用户信息（含角色、部门）
- `updateProfile()`：修改手机号、邮箱（@AuditLog）
- `changePassword()`：验证旧密码，校验新密码复杂度，更新密码，更新 `pwd_update_time`（@AuditLog）
- `uploadAvatar()`：上传头像到 MinIO/OSS，返回 URL，更新用户头像字段

**依赖**：任务 2.1、2.2、3.1、3.2

**验收标准**：
- [ ] `mvn clean compile` 通过
- [ ] 修改密码需验证旧密码
- [ ] 新密码符合复杂度要求（8-20 位，含大小写字母、数字、特殊字符）
- [ ] 头像上传成功返回可访问 URL

---

### 任务 3.10：实现 Controller 层

**目标**：实现 7 个 Controller，暴露 54 个 API 接口

**文件**：
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/controller/AuthController.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/controller/UserController.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/controller/RoleController.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/controller/MenuController.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/controller/DeptController.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/controller/LogController.java`
- 📝 新建：`code/server/prism-platform/src/main/java/com/prism/platform/controller/ProfileController.java`

**实现要点**：
- 使用 `@RestController`、`@RequestMapping("/web/platform")`
- 方法使用 `@GetMapping`（仅 GET 请求）或 `@PostMapping`（其他请求）
- 使用 `@Valid` 校验请求参数
- 使用 `@RequirePermission` 注解控制权限
- 使用 `@LoginLog`、`@AuditLog` 注解记录日志
- 返回类型统一使用 `Result<T>` 或 `Result<PageResult<T>>`

**接口清单**（参考技术实现文档第 8 节）：
- `AuthController`：7 个接口（登录/登出/刷新 Token/验证码/忘记密码/强制修改密码）
- `UserController`：12 个接口（CRUD/分配/批量导入导出）
- `RoleController`：9 个接口（CRUD/权限配置/用户分配）
- `MenuController`：7 个接口（CRUD/树形查询）
- `DeptController`：10 个接口（CRUD/分配/转移/排序）
- `LogController`：5 个接口（三类日志查询/导出）
- `ProfileController`：4 个接口（个人信息/修改密码/上传头像）

**依赖**：任务 3.1 ~ 3.9

**验收标准**：
- [ ] `mvn clean compile` 通过
- [ ] API 路径符合 `spec/backend/api-spec.md`（仅 GET/POST、`/web/platform` 前缀）
- [ ] 权限注解正确配置
- [ ] 日志注解正确配置

---

## 阶段 4：前端层

### 任务 4.1：配置路由和菜单

**目标**：在 UmiJS 中配置系统管理模块的路由和菜单

**文件**：
- 📝 修改：`code/web/config/routes.ts`（添加系统管理路由）
- 📝 修改：`code/web/src/layouts/BasicLayout.tsx`（添加系统管理菜单项）

**实现要点**：
- 路由配置：`/system/login`、`/system/users`、`/system/roles`、`/system/permissions`、`/system/departments`、`/system/logs`
- 菜单配置：系统管理（一级菜单）→ 用户管理/角色管理/权限管理/部门管理/日志管理（二级菜单）
- 登录页面不使用 `BasicLayout`，其他页面使用 `BasicLayout`

**依赖**：无（可与后端并行开发）

**验收标准**：
- [ ] `npm run start` 启动成功
- [ ] 路由可正常访问
- [ ] 菜单正确显示

---

### 任务 4.2：实现登录页面

**目标**：实现登录页面（用户名/手机号/邮箱 + 密码 + 验证码）

**文件**：
- 📝 新建：`code/web/src/pages/system/login/index.tsx`
- 📝 新建：`code/web/src/pages/system/login/index.less`
- 📝 新建：`code/web/src/services/auth.ts`

**实现要点**：
- 三种登录方式 Tab 切换（用户名/手机号/邮箱）
- 图形验证码（Canvas 绘制，点击刷新）
- 首次登录强制修改密码弹窗
- 忘记密码流程（手机号/邮箱验证码）
- 记住我勾选框
- 调用 `POST /web/platform/auth/login`、`GET /web/platform/auth/captcha` 等接口

**依赖**：任务 4.1、后端任务 3.1、3.3

**验收标准**：
- [ ] `npm run start` 启动成功
- [ ] 登录页面可正常访问
- [ ] 三种登录方式均可登录（代理后端接口）
- [ ] 验证码可正常生成和校验
- [ ] 首次登录弹出强制修改密码弹窗

---

### 任务 4.3：实现用户管理页面

**目标**：实现用户管理页面（CRUD、分配角色/部门、重置密码、批量导入导出）

**文件**：
- 📝 新建：`code/web/src/pages/system/users/index.tsx`
- 📝 新建：`code/web/src/pages/system/users/index.less`
- 📝 新建：`code/web/src/pages/system/users/components/UserForm.tsx`（新增/编辑弹窗）
- 📝 新建：`code/web/src/pages/system/users/components/UserDetail.tsx`（详情弹窗）
- 📝 新建：`code/web/src/pages/system/users/components/AssignRole.tsx`（分配角色弹窗）
- 📝 新建：`code/web/src/pages/system/users/components/AssignDept.tsx`（分配部门弹窗）
- 📝 新建：`code/web/src/pages/system/users/components/ImportUsers.tsx`（批量导入弹窗）
- 📝 新建：`code/web/src/services/user.ts`

**实现要点**：
- 左侧部门树筛选
- 右侧用户列表（ProTable）：分页、搜索、状态筛选
- 操作列：编辑、启停用、重置密码、分配角色、分配部门
- 顶部按钮：新增用户、批量导入、批量导出
- 调用 `GET /web/platform/system/users`、`POST /web/platform/system/users/create` 等接口

**依赖**：任务 4.1、后端任务 3.4、3.5、3.7

**验收标准**：
- [ ] `npm run start` 启动成功
- [ ] 用户列表可正常展示（代理后端接口）
- [ ] 新增/编辑/删除用户功能正常
- [ ] 分配角色/部门功能正常
- [ ] 批量导入/导出功能正常

---

### 任务 4.4：实现角色管理页面

**目标**：实现角色管理页面（CRUD、权限配置、用户分配）

**文件**：
- 📝 新建：`code/web/src/pages/system/roles/index.tsx`
- 📝 新建：`code/web/src/pages/system/roles/index.less`
- 📝 新建：`code/web/src/pages/system/roles/components/RoleForm.tsx`（新增/编辑弹窗）
- 📝 新建：`code/web/src/pages/system/roles/components/ConfigurePermissions.tsx`（权限配置弹窗）
- 📝 新建：`code/web/src/pages/system/roles/components/AssignUsers.tsx`（分配用户弹窗）
- 📝 新建：`code/web/src/services/role.ts`

**实现要点**：
- 角色列表（ProTable）：分页、搜索
- 操作列：编辑、启停用、配置权限、分配用户
- 顶部按钮：新增角色
- 权限配置弹窗：左侧菜单树（Tree 组件），右侧数据权限单选
- 超管角色行：显示"不可操作"，禁用编辑/权限/分配按钮
- 调用 `GET /web/platform/system/roles`、`POST /web/platform/system/roles/create` 等接口

**依赖**：任务 4.1、后端任务 3.5、3.6

**验收标准**：
- [ ] `npm run start` 启动成功
- [ ] 角色列表可正常展示
- [ ] 新增/编辑/删除角色功能正常
- [ ] 权限配置功能正常（菜单树勾选）
- [ ] 超管角色行正确显示"不可操作"

---

### 任务 4.5：实现权限管理页面

**目标**：实现权限管理页面（菜单树 CRUD、启停用）

**文件**：
- 📝 新建：`code/web/src/pages/system/permissions/index.tsx`
- 📝 新建：`code/web/src/pages/system/permissions/index.less`
- 📝 新建：`code/web/src/pages/system/permissions/components/MenuForm.tsx`（新增/编辑弹窗）
- 📝 新建：`code/web/src/services/menu.ts`

**实现要点**：
- 左侧菜单树（Tree 组件，可展开/折叠）
- 右侧详情区：节点类型徽标、名称、权限标识、排序、状态、编辑/删除按钮、下级节点表格
- 顶部按钮：新增目录/菜单/按钮
- 调用 `GET /web/platform/system/menus/tree`、`POST /web/platform/system/menus/create` 等接口

**依赖**：任务 4.1、后端任务 3.6

**验收标准**：
- [ ] `npm run start` 启动成功
- [ ] 菜单树可正常展示
- [ ] 新增/编辑/删除菜单功能正常
- [ ] 启停用功能正常

---

### 任务 4.6：实现部门管理页面

**目标**：实现部门管理页面（部门树 CRUD、分配用户、设置负责人、转移、排序）

**文件**：
- 📝 新建：`code/web/src/pages/system/departments/index.tsx`
- 📝 新建：`code/web/src/pages/system/departments/index.less`
- 📝 新建：`code/web/src/pages/system/departments/components/DeptForm.tsx`（新增/编辑弹窗）
- 📝 新建：`code/web/src/pages/system/departments/components/AssignUsers.tsx`（分配用户弹窗）
- 📝 新建：`code/web/src/services/dept.ts`

**实现要点**：
- 左侧部门树（Tree 组件，支持拖拽排序）
- 右侧详情区：编码、负责人、成员数、排序、状态、操作按钮（新增子部门/编辑/分配用户/设置负责人/部门转移）、直属成员表格
- 停用时弹出确认框，提示将迁移用户至上级部门
- 调用 `GET /web/platform/system/depts/tree`、`POST /web/platform/system/depts/create` 等接口

**依赖**：任务 4.1、后端任务 3.4、3.7

**验收标准**：
- [ ] `npm run start` 启动成功
- [ ] 部门树可正常展示
- [ ] 新增/编辑/删除部门功能正常
- [ ] 停用部门时正确提示用户迁移
- [ ] 拖拽排序功能正常

---

### 任务 4.7：实现日志管理页面

**目标**：实现日志管理页面（审计日志/登录日志/执行日志三 Tab）

**文件**：
- 📝 新建：`code/web/src/pages/system/logs/index.tsx`
- 📝 新建：`code/web/src/pages/system/logs/index.less`
- 📝 新建：`code/web/src/pages/system/logs/components/AuditLogTab.tsx`
- 📝 新建：`code/web/src/pages/system/logs/components/LoginLogTab.tsx`
- 📝 新建：`code/web/src/pages/system/logs/components/ExecLogTab.tsx`
- 📝 新建：`code/web/src/services/log.ts`

**实现要点**：
- Tabs 组件切换三个日志类型
- 每个 Tab：筛选条件 + ProTable 列表 + 导出按钮
- 审计日志：模块、类型、时间范围筛选
- 登录日志：用户名、时间范围、结果筛选
- 执行日志：类型、状态、时间范围筛选
- 调用 `GET /web/platform/system/logs/audit`、`GET /web/platform/system/logs/login`、`GET /web/platform/system/logs/exec` 等接口

**依赖**：任务 4.1、后端任务 3.8

**验收标准**：
- [ ] `npm run start` 启动成功
- [ ] 三个 Tab 可正常切换
- [ ] 日志列表可正常展示
- [ ] 筛选功能正常
- [ ] 导出 CSV 功能正常

---

### 任务 4.8：实现用户自服务页面

**目标**：实现个人信息查看/修改、修改密码页面

**文件**：
- 📝 新建：`code/web/src/pages/system/profile/index.tsx`
- 📝 新建：`code/web/src/pages/system/profile/index.less`
- 📝 新建：`code/web/src/pages/system/profile/components/EditProfile.tsx`（修改个人信息弹窗）
- 📝 新建：`code/web/src/pages/system/profile/components/ChangePassword.tsx`（修改密码弹窗）

**实现要点**：
- 个人信息展示：头像、用户名、姓名、手机号、邮箱、角色、部门
- 修改个人信息弹窗：手机号、邮箱
- 修改密码弹窗：旧密码、新密码、确认新密码（含密码复杂度实时校验）
- 头像上传：点击头像触发文件选择，上传后刷新页面
- 调用 `GET /web/platform/system/profile`、`POST /web/platform/system/profile/update`、`POST /web/platform/system/profile/change-password` 等接口

**依赖**：任务 4.1、后端任务 3.9

**验收标准**：
- [ ] `npm run start` 启动成功
- [ ] 个人信息可正常展示
- [ ] 修改个人信息功能正常
- [ ] 修改密码功能正常（含复杂度校验）
- [ ] 头像上传功能正常

---

## 📊 任务依赖关系

```
任务 1.1（Flyway DDL）
    ↓
任务 2.1（Entity）→ 任务 2.2（Mapper）
    ↓
任务 2.3（DTO/VO）→ 任务 2.4（配置/常量）
    ↓
任务 3.1（Security + JWT）→ 任务 3.2（AOP 切面）
    ↓
任务 3.3 ~ 3.9（Service 层，可并行）
    ↓
任务 3.10（Controller 层）
    ↓
任务 4.1 ~ 4.8（前端页面，可并行）
```

**并行策略**：
- 任务 2.3（DTO/VO）和 2.4（配置/常量）可并行
- 任务 3.3 ~ 3.9（7 个 Service）可并行开发
- 任务 4.1 ~ 4.8（8 个前端页面）可并行开发（需先完成任务 4.1 路由配置）
- 前端开发可在后端 API 未完全就绪时使用 Mock 数据

---

## 🎯 验收标准总览

### 后端验收
- [ ] `mvn clean compile` 通过
- [ ] `mvn spring-boot:run -pl prism-platform` 启动成功
- [ ] 所有 API 接口可通过 Postman/Swagger 测试
- [ ] 数据库表结构正确（`mvn flyway:info` 查看迁移状态）
- [ ] Redis 缓存正常工作（Token、权限、验证码）
- [ ] 审计日志自动记录（AOP 切面生效）
- [ ] 登录流程符合设计要求（验证码→用户查询→状态→锁定→首次登录→密码）

### 前端验收
- [ ] `npm run start` 启动成功
- [ ] 所有页面可正常访问（路由正确）
- [ ] 菜单正确显示（BasicLayout）
- [ ] 代理配置正确（`/web` → 后端 8100 端口）
- [ ] 表单校验符合 PRD 规则
- [ ] 表格分页、搜索、筛选功能正常
- [ ] 弹窗交互流畅（新增/编辑/详情/分配）

---

## 📝 备注

1. **单元测试**：本计划不包含单元测试，单测将在归档阶段（阶段 7）统一生成
2. **Mock 数据**：前端开发时可使用 Mock 数据，后端 API 就绪后切换为真实接口
3. **代码审查**：每个任务完成后建议进行代码审查，确保符合 `spec/backend/` 规范
4. **增量提交**：建议每个任务完成后提交一次 Git commit，便于追踪变更
5. **环境依赖**：确保 MySQL 8.0+、Redis 7+ 已启动并配置正确

---

**计划编制**: AI Assistant  
**审核状态**: 待评审  
**最后更新**: 2026-09-29
