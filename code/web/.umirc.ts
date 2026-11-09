import { defineConfig } from '@umijs/max';

export default defineConfig({
  antd: {},
  access: {},
  model: {},
  initialState: {},
  request: {},
  layout: {
    title: 'Prism 本体语义平台',
  },
  routes: [
    {
      path: '/',
      redirect: '/welcome',
    },
    {
      name: '欢迎',
      path: '/welcome',
      component: './Welcome',
    },
    {
      name: '登录',
      path: '/login',
      component: './system/login',
      layout: false,
    },
    {
      path: '/workspace/:workspaceCode',
      component: '@/layouts/WorkspaceLayout',
      layout: false,
      routes: [
        {
          path: '/workspace/:workspaceCode',
          component: './workspace/index',
        },
        {
          name: '空间首页',
          path: '/workspace/:workspaceCode/home',
          component: './workspace/home',
        },
        {
          name: '成员管理',
          path: '/workspace/:workspaceCode/members',
          component: './workspace/members',
        },
      ],
    },
    {
      name: '系统管理',
      path: '/system',
      routes: [
        {
          path: '/system',
          redirect: '/system/users',
        },
        {
          name: '用户管理',
          path: '/system/users',
          component: './system/users',
          access: 'canSystemUsers',
        },
        {
          name: '角色管理',
          path: '/system/roles',
          component: './system/roles',
          access: 'canSystemRoles',
        },
        {
          name: '权限管理',
          path: '/system/permissions',
          component: './system/permissions',
          access: 'canSystemPermissions',
        },
        {
          name: '部门管理',
          path: '/system/departments',
          component: './system/departments',
          access: 'canSystemDepts',
        },
        {
          name: '空间管理',
          path: '/system/workspaces',
          component: './system/workspaces',
          access: 'canSystemWorkspaces',
        },
        {
          name: '日志管理',
          path: '/system/logs',
          component: './system/logs',
          access: 'canSystemLogs',
        },
        {
          name: '个人中心',
          path: '/system/profile',
          component: './system/profile',
          hideInMenu: true,
        },
      ],
    },
  ],
  proxy: {
    '/web': {
      target: 'http://localhost:8080',
      changeOrigin: true,
    },
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
    },
  },
  npmClient: 'npm',
});
