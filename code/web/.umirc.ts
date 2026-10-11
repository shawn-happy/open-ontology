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
        },
        {
          name: '角色管理',
          path: '/system/roles',
          component: './system/roles',
        },
        {
          name: '权限管理',
          path: '/system/permissions',
          component: './system/permissions',
        },
        {
          name: '部门管理',
          path: '/system/departments',
          component: './system/departments',
        },
        {
          name: '日志管理',
          path: '/system/logs',
          component: './system/logs',
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
      target: 'http://localhost:8100',
      changeOrigin: true,
    },
    '/api': {
      target: 'http://localhost:8100',
      changeOrigin: true,
    },
  },
  npmClient: 'npm',
});
