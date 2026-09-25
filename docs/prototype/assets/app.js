/* ============ Open Ontology 原型 · 共享布局与交互 ============ */
(function () {
  // 子目录页面（platform/ ontology/ data/ …）相对根目录前缀，保证侧边栏链接在任何页面都正确
  const ROOT = /\/(platform|ontology|data|security|application|audit|workspace|developer|approval)\//.test(location.pathname) ? '../' : '';
  // 公共菜单：平台层左上角图标栏（icon rail），仅展示图标，悬停显示名称
  const GLOBAL_NAV = [
    { id: 'home', href: ROOT + 'index.html', icon: '⌂', label: '首页' },
    { id: 'workspace', href: ROOT + 'platform/workspace.html', icon: '▦', label: '工作空间' },
    { id: 'system', href: ROOT + 'platform/system.html', icon: '⚙', label: '系统管理' },
    { id: 'approval', href: ROOT + 'approval/approval-config.html', icon: '✓', label: '审批中心' },
  ];

  // 空间内菜单：统一放到左侧边栏，仅在空间内页面展示
  const SPACE_NAV = [
    {
      group: '本体资产管理',
      items: [
        { id: 'ontology-graph', href: ROOT + 'ontology/ontology-graph.html', icon: '◈', label: '本体图谱' },
        { id: 'ontology-relations', href: ROOT + 'ontology/ontology-relations.html', icon: '⇄', label: '关联关系' },
        { id: 'ontology-shared-props', href: ROOT + 'ontology/ontology-shared-props.html', icon: '⧉', label: '共享属性' },
        { id: 'ontology-enums', href: ROOT + 'ontology/ontology-enums.html', icon: '☰', label: '枚举类' },
        { id: 'ontology-constraints', href: ROOT + 'ontology/ontology-constraints.html', icon: '⌗', label: '值约束规则' },
        { id: 'ontology-actions', href: ROOT + 'ontology/ontology-actions.html', icon: '▶', label: '动作' },
        { id: 'ontology-functions', href: ROOT + 'ontology/ontology-functions.html', icon: 'ƒ', label: '函数' },
        { id: 'ontology-migrations', href: ROOT + 'ontology/ontology-migrations.html', icon: '⇅', label: 'Schema 迁移' },
        { id: 'ontology-branching', href: ROOT + 'ontology/ontology-branching.html', icon: '⑂', label: '本体分支' },
      ],
    },
    {
      group: '数据管理',
      items: [
        { id: 'data-sources', href: ROOT + 'data/data-sources.html', icon: '⛁', label: '数据源管理' },
        { id: 'data-datasets', href: ROOT + 'data/data-datasets.html', icon: '⛃', label: '数据集管理' },
        { id: 'data-pipeline', href: ROOT + 'data/data-pipeline.html', icon: '⇶', label: 'Pipeline 管理' },
        { id: 'data-explore', href: ROOT + 'data/data-explore.html', icon: '⌨', label: '数据探查' },
      ],
    },
    {
      group: '安全管理',
      items: [
        { id: 'sec-marks', href: ROOT + 'security/sec-marks.html', icon: '⚑', label: '标记管理' },
        { id: 'sec-permissions', href: ROOT + 'security/sec-permissions.html', icon: '⛨', label: '数据权限' },
        { id: 'sec-encryption', href: ROOT + 'security/sec-encryption.html', icon: '⚿', label: '加密' },
        { id: 'sec-masking', href: ROOT + 'security/sec-masking.html', icon: '◌', label: '脱敏' },
      ],
    },
    {
      group: '应用管理',
      items: [
        { id: 'app-apikeys', href: ROOT + 'application/app-apikeys.html', icon: '⚿', label: 'API Key' },
        { id: 'app-workflow', href: ROOT + 'application/app-workflow.html', icon: '❖', label: 'Workflow 编排' },
        { id: 'app-skills', href: ROOT + 'application/app-skills.html', icon: '✦', label: 'Skills' },
      ],
    },
    {
      group: '开发者平台',
      items: [
        { id: 'dev-console', href: ROOT + 'developer/dev-console.html', icon: '⌗', label: 'Developer Console' },
        { id: 'dev-sdk', href: ROOT + 'developer/dev-sdk.html', icon: '⇉', label: 'SDK 与开放 API' },
        { id: 'dev-code', href: ROOT + 'developer/dev-code.html', icon: '⌨', label: '代码化本体' },
      ],
    },
    {
      group: '成员管理',
      items: [
        { id: 'member-assign', href: ROOT + 'workspace/member-assign.html', icon: '⊕', label: '分配记录' },
      ],
    },
    {
      group: '审计管理',
      items: [
        { id: 'audit-logs', href: ROOT + 'audit/audit-logs.html', icon: '☰', label: '审计日志' },
        { id: 'audit-assets', href: ROOT + 'audit/audit-assets.html', icon: '◔', label: '本体资产统计' },
        { id: 'audit-pipelines', href: ROOT + 'audit/audit-pipelines.html', icon: '⇶', label: 'Pipeline 运行' },
        { id: 'audit-apps', href: ROOT + 'audit/audit-apps.html', icon: '❖', label: '应用调用' },
      ],
    },
  ];

  const WORKSPACES = ['智能制造空间', '供应链分析空间', '设备运维空间'];

  let WS = localStorage.getItem('oo-ws') || WORKSPACES[0];

  // 左上角图标栏：公共菜单（首页 / 工作空间管理 / 系统管理）
  function iconRail(active) {
    return `
      <a class="rail-logo" href="${ROOT}index.html" title="Open Ontology"><div class="mark">◈</div></a>
      ${GLOBAL_NAV.map(
        (it) => `
      <a class="rail-item ${it.id === active ? 'active' : ''}" href="${it.href}" title="${it.label}">${it.icon}</a>`
      ).join('')}
      <div class="rail-foot">v0.1</div>
    `;
  }

  // 左侧边栏：空间内菜单（空间首页 + 本体资产 / 数据 / 安全 / 应用 / 审计）
  function sidebar(active) {
    return `
      <a class="nav-item ${active === 'ws-home' ? 'active' : ''}" href="${ROOT}workspace/home.html">
        <span class="ico">⌂</span>空间首页
        ${active === 'ws-home' ? '<span class="badge-mini">当前</span>' : ''}
      </a>
    ` + SPACE_NAV.map(
      (g) => `
      <div class="nav-group-title">${g.group}</div>
      ${g.items
        .map(
          (it) => `
        <a class="nav-item ${it.id === active ? 'active' : ''}" href="${it.href}">
          <span class="ico">${it.icon}</span>${it.label}
          ${it.id === active ? '<span class="badge-mini">当前</span>' : ''}
        </a>`
        )
        .join('')}`
    ).join('');
  }
  // 顶栏左侧：空间下拉切换
  function wsSwitcher() {
    // 从列表进入的自定义空间（如新建的空间）也纳入下拉首项，保证可切回
    const list = WORKSPACES.includes(WS) ? WORKSPACES : [WS, ...WORKSPACES];
    return `
      <div class="ws-switch" onclick="App.toggleWsMenu(event)">
        <span class="ico">⛃</span><b>${WS}</b><span class="caret">⌄</span>
        <div class="ws-menu" id="ws-menu">
          <div class="ws-menu-title">切换工作空间</div>
          ${list.map(
            (w) => `
          <div class="ws-item ${w === WS ? 'cur' : ''}" onclick="App.selectWs('${w}', event)">${w}${w === WS ? '<span class="ws-check">✓</span>' : ''}</div>`
          ).join('')}
        </div>
      </div>
    `;
  }

  // 顶栏右侧：审批通知铃铛 + 个人用户信息
  // 审批通知演示数据（持久化到 localStorage，审批记录页的操作会追加通知）
  const DEFAULT_NOTIFS = [
    { t: 'submit', txt: '张伟 提交了动作「batchArchiveFaults」的发布审批', time: '09-24 10:12' },
    { t: 'ok', txt: '您发起的函数「calcEquipmentYield」发布审批已通过', time: '09-23 18:40' },
    { t: 'reject', txt: '李静 驳回了共享属性「asset_no」的发布申请', time: '09-23 14:05' },
  ];
  function getNotifs() {
    try {
      const saved = JSON.parse(localStorage.getItem('oo-notifs'));
      return Array.isArray(saved) ? saved : DEFAULT_NOTIFS.slice();
    } catch (e) { return DEFAULT_NOTIFS.slice(); }
  }
  function saveNotifs(list) { localStorage.setItem('oo-notifs', JSON.stringify(list)); }

  function notifyBell() {
    const list = getNotifs();
    const unread = list.filter((n) => !n.read).length;
    return `
      <div class="notify-bell" onclick="App.toggleNotify(event)" title="审批通知">
        <span class="ico">✉</span>${unread ? `<span class="bell-dot">${unread}</span>` : ''}
        <div class="notify-menu" id="notify-menu">
          <div class="notify-h">审批通知</div>
          ${list.length ? list.slice(0, 6).map((n) => `
          <a class="notify-item" href="${ROOT}approval/approval-records.html" onclick="App.readAllNotifs()">
            <span class="n-dot ${n.t}"></span>
            <span class="n-txt">${n.txt}<em>${n.time}</em></span>
          </a>`).join('') : '<div class="notify-empty">暂无通知</div>'}
          <a class="notify-all" href="${ROOT}approval/approval-records.html" onclick="App.readAllNotifs()">查看全部 →</a>
        </div>
      </div>
    `;
  }

  function userInfo() {
    return `
      ${notifyBell()}
      <div class="user-info" title="shawn（超级管理员）">
        <div class="avatar">S</div>
        <div class="user-meta"><b>shawn</b><span>超级管理员</span></div>
      </div>
    `;
  }

  window.App = {
    get workspace() { return WS; },

    /**
     * 挂载页面
     * @param cfg { page, crumbs: [..], title, desc, actions, content (HTML), onMount }
     */
    mount(cfg) {
      const inSpace = !['home', 'workspace', 'system', 'approval'].includes(cfg.page);
      this._lastCfg = cfg; 
      // 空间切换后整页重挂载使用
      // 空间内页面：面包屑首位动态替换为当前空间名
      const crumbs = (cfg.crumbs || []).slice();
      if (inSpace && WORKSPACES.includes(crumbs[0])) crumbs[0] = WS;
      // 平台层页面：展示公共图标栏；空间内页面：隐藏公共菜单，顶栏提供返回平台入口
      const layoutTop = inSpace
        ? `
          <div class="main">
            <div class="topbar">
              <a class="btn btn-sm back-btn" href="${ROOT}index.html" title="返回平台首页">← 返回平台</a>
              ${wsSwitcher()}
              <div class="crumbs">${crumbs.map((c, i, a) =>
                i === a.length - 1 ? `<b>${c}</b>` : `${c} / `).join('')}</div>
              <div class="spacer"></div>
              ${userInfo()}
            </div>
            <div class="content-row">
              <aside class="sidebar">${sidebar(cfg.page)}</aside>
              <div class="page-body">
                <div class="page-header">
                  <div>
                    <h1>${cfg.title}</h1>
                    ${cfg.desc ? `<div class="desc">${cfg.desc}</div>` : ''}
                  </div>
                  <div class="actions">${cfg.actions || ''}</div>
                </div>
                <div id="page-content">${cfg.content || ''}</div>
              </div>
            </div>
          </div>`
        : `
          <aside class="icon-rail">${iconRail(cfg.page)}</aside>
          <div class="main">
            <div class="topbar">
              <div class="crumbs">${crumbs.map((c, i, a) =>
                i === a.length - 1 ? `<b>${c}</b>` : `${c} / `).join('')}</div>
              <div class="spacer"></div>
              ${userInfo()}
            </div>
            <div class="page-body">
              <div class="page-header">
                <div>
                  <h1>${cfg.title}</h1>
                  ${cfg.desc ? `<div class="desc">${cfg.desc}</div>` : ''}
                </div>
                <div class="actions">${cfg.actions || ''}</div>
              </div>
              <div id="page-content">${cfg.content || ''}</div>
            </div>
          </div>`;
      document.getElementById('root').innerHTML = `
        <div class="layout">${layoutTop}</div>
        <div class="toast" id="toast"></div>
      `;
      // 绑定通用交互
      document.querySelectorAll('[data-close-mask]').forEach((el) => {
        el.addEventListener('click', (e) => { if (e.target === el) App.closeAll(); });
      });
      // 点击空白处关闭空间下拉菜单与通知下拉
      document.addEventListener('click', () => {
        const menu = document.getElementById('ws-menu');
        if (menu) menu.classList.remove('open');
        const nm = document.getElementById('notify-menu');
        if (nm) nm.classList.remove('open');
      });
      bindTabs();
      if (cfg.onMount) cfg.onMount();
    },

    toggleWsMenu(e) {
      e && e.stopPropagation();
      document.getElementById('ws-menu').classList.toggle('open');
    },

    /** 顶栏审批通知铃铛 */
    toggleNotify(e) {
      e && e.stopPropagation();
      const nm = document.getElementById('notify-menu');
      if (nm) nm.classList.toggle('open');
    },

    /** 点击通知项后全部标记已读（红点清零） */
    readAllNotifs() {
      const list = getNotifs();
      list.forEach((n) => (n.read = true));
      saveNotifs(list);
      // 铃铛红点由页面跳转后重新渲染，无需手动移除
    },

    /** 追加一条审批通知（供审批记录页的操作调用） */
    pushNotif(t, txt) {
      const list = getNotifs();
      const now = new Date();
      const pad = (v) => String(v).padStart(2, '0');
      list.unshift({ t, txt, time: `${pad(now.getMonth() + 1)}-${pad(now.getDate())} ${pad(now.getHours())}:${pad(now.getMinutes())}` });
      saveNotifs(list);
      // 重新渲染铃铛红点
      const bell = document.querySelector('.notify-bell');
      if (bell) { bell.outerHTML = notifyBell(); }
    },

    selectWs(name, e) {
      e && e.stopPropagation();
      if (name === WS) { this.toggleWsMenu(); return; }
      WS = name;
      localStorage.setItem('oo-ws', WS);
      toast('已切换到空间：' + WS);
      this.mount(this._lastCfg); 
      // 重挂载以刷新下拉选中态
    },

    /** 从工作空间列表进入某个空间 */
    enterWs(name) {
      WS = name;
      localStorage.setItem('oo-ws', WS);
      location.href = ROOT + 'workspace/home.html';
    },

    toast(msg) { toast(msg); },
    openModal(id) { document.getElementById(id).classList.add('open'); },
    closeModal(id) { document.getElementById(id).classList.remove('open'); },
    openDrawer(id) {
      document.getElementById(id + '-mask').classList.add('open');
      document.getElementById(id).classList.add('open');
    },
    closeDrawer(id) {
      document.getElementById(id + '-mask').classList.remove('open');
      document.getElementById(id).classList.remove('open');
    },
    closeAll() {
      document.querySelectorAll('.mask.open').forEach((m) => m.classList.remove('open'));
      document.querySelectorAll('.drawer.open').forEach((d) => {
        App.closeDrawer(d.id);
      });
    },
  };

  function toast(msg) {
    const t = document.getElementById('toast');
    t.textContent = msg;
    t.classList.add('show');
    clearTimeout(t._timer);
    t._timer = setTimeout(() => t.classList.remove('show'), 1800);
  }
  window.toast = toast;

  function bindTabs() {
    document.querySelectorAll('.tabs').forEach((tabsEl) => {
      const group = tabsEl.dataset.group;
      tabsEl.querySelectorAll('.tab').forEach((tab) => {
        tab.addEventListener('click', () => {
          tabsEl.querySelectorAll('.tab').forEach((t) => t.classList.remove('active'));
          tab.classList.add('active');
          document
            .querySelectorAll(`.tab-pane[data-group="${group}"]`)
            .forEach((p) => p.classList.toggle('active', p.dataset.pane === tab.dataset.pane));
        });
      });
    });
  }
})();
