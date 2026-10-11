# 系统管理功能交付清单

**交付日期**：2026-09-29
**交付范围**：系统管理模块（平台级共享功能）

---

## 一、PRD 文档

| 文档 | 路径 | 说明 |
|---|---|---|
| 需求设计文档 | `docs/prd/system-需求设计.md` | 完整的系统管理需求设计，含背景、用户角色、功能需求清单（FR-01 ~ FR-49）、页面交互说明、非功能需求、开放问题 |
| 产品功能设计（已更新） | `docs/prd/产品功能设计.md` | 更新了 2.3 节功能结构映射表、3.2 节系统管理描述 |

---

## 二、高保真原型页面

| 页面 | 路径 | 说明 |
|---|---|---|
| 登录页面 | `docs/prototype/system/login.html` | 独立登录页，支持用户名/手机号/邮箱三种登录方式，admin/admin123 初始账号 |
| 系统管理总览 | `docs/prototype/system/system.html` | 功能入口，六宫格卡片导航至各子功能 |
| 用户管理 | `docs/prototype/system/sys-users.html` | 用户 CRUD、分配角色/部门、重置密码、模糊搜索、分页、部门树筛选 |
| 角色管理 | `docs/prototype/system/sys-roles.html` | 角色 CRUD、配置权限（菜单+按钮+空间数据权限）、分配用户、超级管理员保护 |
| 权限管理 | `docs/prototype/system/sys-perms.html` | 菜单资源树（目录/菜单/按钮三级）、CRUD、启停用、权限标识管理 |
| 部门管理 | `docs/prototype/system/sys-depts.html` | 部门树、CRUD、分配用户/负责人、启停用与用户迁移 |
| 日志管理 | `docs/prototype/system/sys-logs.html` | 审计日志+登录日志+执行日志三 Tab，多维筛选与详情查看 |
| 旧版重定向 | `docs/prototype/platform/system.html` | 自动跳转至新版 `system/system.html` |

---

## 三、三处同步（硬约束）

### 同步点 1：app.js NAV 配置

**文件**：`docs/prototype/assets/app.js`

**改动**：
1. ROOT 正则新增 `system` 前缀（第 4 行）
2. GLOBAL_NAV 新增系统管理菜单组（第 9-14 行）：
   - system（系统管理总览）
   - sys-users（用户管理）
   - sys-roles（角色管理）
   - sys-perms（权限管理）
   - sys-depts（部门管理）
   - sys-logs（日志管理）
3. inSpace 判断新增 6 个页面 ID（第 192 行），确保系统管理页面使用平台级布局（图标栏）而非空间级布局（侧边栏）

### 同步点 2：HTML 页面 App.mount()

所有新增页面均使用 `App.mount()` 挂载，page 参数与 NAV 配置一一对应：
- `page: 'system'` → 系统管理总览
- `page: 'sys-users'` → 用户管理
- `page: 'sys-roles'` → 角色管理
- `page: 'sys-perms'` → 权限管理
- `page: 'sys-depts'` → 部门管理
- `page: 'sys-logs'` → 日志管理
- 登录页面为独立页面，不使用 App.mount()

### 同步点 3：PRD 映射表

**文件**：`docs/prd/产品功能设计.md`

**改动**：
- 2.3 节功能结构映射表已更新，系统管理展开为 6 个子功能页面
- 3.2 节系统管理描述已重写，包含完整的子功能表格

---

## 四、核心功能特性

### 4.1 登录与身份认证
- 三种登录方式：用户名 / 手机号 / 邮箱 + 密码
- 初始管理员账号 admin / admin123
- 首次登录提示修改密码

### 4.2 超级管理员保护机制
- admin 用户：不可停用、用户名不可修改、密码需安全渠道重置
- 超级管理员角色：仅可分配给 admin，其余用户不可分配
- 超级管理员角色：不可修改、不可停用

### 4.3 权限体系
- 三级结构：目录 → 菜单 → 按钮
- 菜单权限控制导航可见性
- 按钮权限标识用于接口鉴权（如 `system:user:create`）
- 空间数据权限：全部空间 / 本部门及子部门 / 本部门 / 仅自己创建 / 自定义

### 4.4 部门管理
- 无限层级部门树
- 停用部门时下属用户自动迁移至上级部门
- 根节点（集团）不可编辑、不可停用

### 4.5 日志审计
- 审计日志：本体资源操作、数据 Pipeline 变更
- 登录日志：用户登录记录与安全审计（失败 5 次锁定 30 分钟）
- 执行日志：Action / Function / Pipeline 运行记录

---

## 五、测试要点

### 5.1 功能测试
- [ ] 三种登录方式均可正常登录
- [ ] admin 账号不可停用、用户名不可修改
- [ ] 超级管理员角色仅可分配给 admin
- [ ] 内置角色不可编辑、不可停用
- [ ] 权限树勾选后对应菜单/按钮可见
- [ ] 停用部门时用户正确迁移
- [ ] 日志三类 Tab 切换正常

### 5.2 安全测试
- [ ] 密码强度校验（8 位+大小写+数字+特殊字符）
- [ ] 登录失败 5 次锁定 30 分钟
- [ ] 无权限用户不可访问受限菜单
- [ ] 按钮权限标识与后端接口鉴权一致

---

## 六、后续迭代建议

| 优先级 | 功能 | 说明 |
|---|---|---|
| v1.1 | LDAP/SSO 集成 | 支持企业现有身份认证系统 |
| v1.1 | 操作日志实时告警 | 越权访问、异常登录自动告警 |
| v1.2 | 部门树拖拽排序 | 简化部门层级调整操作 |
| v1.2 | 权限变更实时生效 | 关键权限变更强制在线用户刷新 |
| v2.0 | 多因素认证（MFA） | 敏感操作二次验证 |
| v2.0 | 操作审计视频回放 | 关键操作全程录屏追溯 |

---

## 七、技术实现要点（供开发参考）

### 7.1 后端 API 路径

根据 `spec/backend/api-spec.md`，系统管理相关 API 统一使用 `/web/platform` 前缀：

- 用户管理：`/web/platform/user/{action}`
- 角色管理：`/web/platform/role/{action}`
- 权限管理：`/web/platform/menu/{action}`
- 部门管理：`/web/platform/dept/{action}`
- 日志查询：`/web/platform/log/{type}/list`
- 登录认证：`/web/platform/auth/login`

### 7.2 数据库表设计

根据 `spec/backend/database-spec.md`，表名使用 `platform_{entity}` 格式：

- `platform_user` - 用户表
- `platform_role` - 角色表
- `platform_user_role` - 用户角色关联表
- `platform_menu` - 菜单权限表
- `platform_role_menu` - 角色菜单关联表
- `platform_dept` - 部门表
- `platform_user_dept` - 用户部门关联表
- `platform_log_audit` - 审计日志表
- `platform_log_login` - 登录日志表
- `platform_log_exec` - 执行日志表

### 7.3 权限标识示例

```
system:user:list        - 查看用户列表
system:user:create      - 新增用户
system:user:update      - 编辑用户
system:user:delete      - 删除用户
system:user:toggle      - 启停用用户
system:user:resetPwd    - 重置密码
system:user:assignRole  - 分配角色
system:user:assignDept  - 分配部门

system:role:list        - 查看角色列表
system:role:create      - 新增角色
system:role:update      - 编辑角色
system:role:delete      - 删除角色
system:role:toggle      - 启停用角色
system:role:perm        - 配置权限
system:role:assignUser  - 分配用户

system:menu:list        - 查看菜单列表
system:menu:create      - 新增菜单
system:menu:update      - 编辑菜单
system:menu:delete      - 删除菜单
system:menu:toggle      - 启停用菜单

system:dept:list        - 查看部门列表
system:dept:create      - 新增部门
system:dept:update      - 编辑部门
system:dept:delete      - 删除部门
system:dept:toggle      - 启停用部门
system:dept:assignUser  - 分配用户

system:log:audit        - 查看审计日志
system:log:login        - 查看登录日志
system:log:exec         - 查看执行日志
system:log:export       - 导出日志
```

---

**交付完成**：所有原型页面、PRD 文档、NAV 配置已同步，可直接浏览器打开 HTML 文件预览。
