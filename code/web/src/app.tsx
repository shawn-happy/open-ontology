import type { RunTimeLayoutConfig, RequestConfig } from '@umijs/max';
import { history } from '@umijs/max';
import { Dropdown } from 'antd';
import { UserOutlined, KeyOutlined, LogoutOutlined } from '@ant-design/icons';
import type { MenuProps } from 'antd';
import { logout as logoutApi } from './services/auth';

// 不需要登录即可访问的路径
const PUBLIC_PATHS = ['/login'];

// 运行时配置
export const layout: RunTimeLayoutConfig = () => {
  let userInfo: any = {};
  try {
    userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
  } catch {
    // ignore
  }

  return {
    title: 'Prism 本体语义平台',
    logo: '/logo.svg',
    menu: {
      locale: false,
    },
    layout: 'mix',
    splitMenus: false,
    avatarProps: {
      src: userInfo.avatar || undefined,
      title: userInfo.name || userInfo.username || '用户',
      render: (_: any, dom: any) => {
        const menuItems: MenuProps['items'] = [
          {
            key: 'profile',
            icon: <UserOutlined />,
            label: '用户信息',
          },
          {
            key: 'password',
            icon: <KeyOutlined />,
            label: '修改密码',
          },
          { type: 'divider' },
          {
            key: 'logout',
            icon: <LogoutOutlined />,
            label: '退出登录',
          },
        ];

        const handleMenuClick: MenuProps['onClick'] = async ({ key }) => {
          if (key === 'profile') {
            history.push('/system/profile');
          } else if (key === 'password') {
            history.push('/system/profile?action=changePassword');
          } else if (key === 'logout') {
            try {
              await logoutApi();
            } catch {
              // 即使接口失败也清除本地状态
            }
            localStorage.removeItem('accessToken');
            localStorage.removeItem('refreshToken');
            localStorage.removeItem('userInfo');
            localStorage.removeItem('permissions');
            history.push('/login');
          }
        };

        return (
          <Dropdown menu={{ items: menuItems, onClick: handleMenuClick }} trigger={['click']}>
            {dom}
          </Dropdown>
        );
      },
    },
    onPageChange: () => {
      const { pathname } = window.location;
      const token = localStorage.getItem('accessToken');

      // 公开路径直接放行
      if (PUBLIC_PATHS.includes(pathname)) {
        return;
      }

      // 未登录则跳转登录页
      if (!token) {
        history.push('/login');
      }
    },
    logout: async () => {
      try {
        await logoutApi();
      } catch {
        // 即使接口失败也清除本地状态
      }
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('userInfo');
      localStorage.removeItem('permissions');
      history.push('/login');
    },
  };
};

// 请求配置
export const request: RequestConfig = {
  timeout: 30000,
  errorConfig: {
    errorThrower: (res: any) => {
      const { code, msg } = res;
      if (code !== '000000') {
        const error: any = new Error(msg);
        error.name = 'BizError';
        error.info = res;
        throw error;
      }
    },
    errorHandler: (error: any) => {
      if (error.response) {
        const status = error.response.status;
        if (status === 401) {
          // Token 过期或无效，尝试刷新或跳转登录
          localStorage.removeItem('accessToken');
          localStorage.removeItem('userInfo');
          localStorage.removeItem('permissions');
          history.push('/login');
        }
        console.error(`Response status: ${status}`);
      } else if (error.request) {
        console.error('No response received');
      } else {
        console.error('Error:', error.message);
      }
    },
  },
  requestInterceptors: [
    (config: any) => {
      // 注入 X-Request-ID
      const requestId = `req_${Date.now()}_${Math.random().toString(36).substring(2, 9)}`;
      config.headers['X-Request-ID'] = requestId;

      // 注入 Authorization Token
      const token = localStorage.getItem('accessToken');
      if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
      }

      return config;
    },
  ],
  responseInterceptors: [
    (response: any) => {
      return response;
    },
  ],
};
