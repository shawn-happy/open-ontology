# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

Open Ontology（本体语义平台）——以本体（Ontology）为核心的企业数据语义化中台产品。当前处于产品早期阶段，仓库内容为**产品设计资产**（PRD + 高保真 HTML 原型），尚无后端/前端工程代码。设计参考 Palantir Foundry（Object Types / Link Types / Shared Property Types / Action Types / Functions）。

产品为中文 UI。核心概念：工作空间（顶层隔离单元）→ 本体资产（类/接口/抽象类/枚举类、属性、关联关系、值约束、动作、函数）、数据资产（数据源/数据集/Pipeline）、安全策略（标记/数据权限/加密/脱敏）、应用资产（API Key/Workflow/Skills）、审计。

## 目录结构

- `docs/prd/产品功能设计.md` — PRD，章节与原型页面一一映射（每节标注对应原型 HTML 路径）。
- `docs/prototype/` — HTML/JS 高保真原型：
  - `index.html` 为入口导航页；目录与侧边栏父菜单一一对应：`platform/`、`ontology/`、`data/`、`security/`、`application/`、`audit/`、`workspace/`。
  - 共享布局与交互在 `assets/app.js`（`GLOBAL_NAV` / `SPACE_NAV` 菜单配置、`App.mount()` 页面挂载）+ `assets/style.css`。
- `docs/palantir/` — 已爬取的 Palantir Foundry 官方文档快照（静态 HTML + `_resources/` 图片），按板块分目录（`ontology/`、`security/`、`api/` 等），`index.html` 为目录索引。仅作设计参考，勿修改其内容。爬虫脚本已移除，如需重新爬取需恢复工具链（uv + requests + markdown，Python ≥3.12）。

## 运行命令

无构建/测试步骤。原型直接用浏览器打开 HTML（或 `python3 -m http.server` 后访问）。

## 关键约定（硬约束）

新增/调整功能页面时，必须**三处同步**：

1. `docs/prototype/assets/app.js` 中对应的 NAV 配置（`GLOBAL_NAV` 平台级 / `SPACE_NAV` 空间级）。
2. 对应父菜单目录下新建 HTML 页面（使用 `assets/app.js` 的 `App.mount()` 挂载，参考现有页面）。
3. `docs/prd/产品功能设计.md` 的章节及「功能结构与原型页面映射」表。

- 父菜单 = 文件目录：每个一级功能模块一个独立目录，每个子功能是独立 HTML 页面（用户明确拒绝把子功能合并为单页 Tabs）。
- 子目录页面通过 `app.js` 中的 `ROOT` 前缀逻辑处理相对路径，新增一级目录时需同步更新该正则。
