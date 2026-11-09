import type { RunTimeLayoutConfig, RequestConfig } from '@umijs/max';
import { history, getRequestInstance } from '@umijs/max';
import axios from 'axios';
import { Dropdown, message } from 'antd';
import { UserOutlined, KeyOutlined, LogoutOutlined } from '@ant-design/icons';
import type { MenuProps } from 'antd';
import { logout as logoutApi } from './services/auth';
import ErrorBoundary from './components/ErrorBoundary';

// 不需要登录即可访问的路径
const PUBLIC_PATHS = ['/login'];

/**
 * 全局初始状态：为 Access 插件提供权限数据源
 * 登录成功后由登录页通过 setInitialState 刷新（见 pages/system/login）
 */
export async function getInitialState(): Promise<{
  userInfo: Record<string, any>;
  permissions: string[];
}> {
  let userInfo: Record<string, any> = {};
  let permissions: string[] = [];
  try {
    userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
    permissions = JSON.parse(localStorage.getItem('permissions') || '[]');
  } catch {
    // ignore
  }
  return { userInfo, permissions };
}

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

// ==================== 统一异常处理 ====================

/** Token 刷新单例 Promise：并发多个 401 时共享同一次刷新请求 */
let refreshingPromise: Promise<string | null> | null = null;

/** 用 refreshToken 换取新 accessToken，失败返回 null */
async function refreshAccessToken(): Promise<string | null> {
  const refreshToken = localStorage.getItem('refreshToken');
  if (!refreshToken) {
    return null;
  }
  try {
    // 使用裸 axios，绕过全局拦截器，避免刷新接口自身 401 造成递归
    const res = await axios.post('/web/auth/refresh-token', { refreshToken });
    const body = res.data;
    if (body?.code === '000000' && body.data) {
      localStorage.setItem('accessToken', body.data);
      return body.data as string;
    }
    return null;
  } catch {
    return null;
  }
}

/** 清除本地登录状态并跳转登录页 */
function redirectToLogin() {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
  localStorage.removeItem('userInfo');
  localStorage.removeItem('permissions');
  if (window.location.pathname !== '/login') {
    history.push('/login');
  }
}

/**
 * 401 静默刷新拦截器：
 * 收到 401 后先尝试刷新 token 并重放原请求（并发请求共享一次刷新），
 * 刷新失败才清除状态跳转登录页。
 */
const unauthorizedInterceptor = async (error: any) => {
  const { response, config } = error || {};
  if (
    response?.status === 401 &&
    config &&
    !config.__isRetryRequest &&
    !config.url?.includes('/auth/login') &&
    !config.url?.includes('/auth/refresh-token')
  ) {
    if (!refreshingPromise) {
      refreshingPromise = refreshAccessToken().finally(() => {
        refreshingPromise = null;
      });
    }
    const newToken = await refreshingPromise;
    if (newToken) {
      config.__isRetryRequest = true;
      config.headers = { ...config.headers, Authorization: `Bearer ${newToken}` };
      // 通过全局实例重放，响应继续走统一拦截器链（含业务错误检查）
      return getRequestInstance().request(config);
    }
    redirectToLogin();
  }
  return Promise.reject(error);
};

/** HTTP 状态码 → 用户友好提示 */
const HTTP_ERROR_MESSAGES: Record<number, string> = {
  400: '请求参数错误',
  403: '没有权限执行该操作',
  404: '请求的资源不存在',
  408: '请求超时，请稍后重试',
  500: '服务器内部错误',
  502: '网关异常',
  503: '服务暂不可用',
  504: '网关超时',
};

// 请求配置
export const request: RequestConfig = {
  timeout: 30000,
  errorConfig: {
    // 兜底：umi 内置 errorThrower 仅在响应含 success===false 时触发，
    // 后端 { code, msg } 结构的业务错误由下方响应拦截器统一抛出
    errorThrower: (res: any) => {
      const { code, msg } = res || {};
      if (code && code !== '000000') {
        const error: any = new Error(msg);
        error.name = 'BizError';
        error.info = res;
        throw error;
      }
    },
    // 统一异常提示入口：业务错误 / HTTP 错误 / 网络错误在此 toast 一次，
    // 页面 catch 中只处理流程（刷新验证码、关弹窗等），不要再重复提示
    errorHandler: (error: any, opts: any) => {
      if (opts?.skipErrorHandler) {
        throw error;
      }
      if (error?.name === 'BizError') {
        message.error(error.info?.msg || '操作失败');
        return;
      }
      if (error?.response) {
        const status = error.response.status;
        if (status === 401) {
          message.warning('登录已过期，请重新登录');
        } else {
          message.error(HTTP_ERROR_MESSAGES[status] || `请求失败（${status}）`);
        }
      } else if (error?.request) {
        message.error('网络连接失败，请检查网络后重试');
      } else if (error?.message) {
        message.error(error.message);
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
    [
      (response: any) => {
        // 业务错误统一抛出：后端响应 { code, msg }，无 success 字段，
        // umi 内置 errorThrower 不会触发，需在此主动检查并转为 BizError
        const data = response.data;
        if (
          data &&
          typeof data === 'object' &&
          !(data instanceof Blob) &&
          'code' in data &&
          data.code !== '000000'
        ) {
          const error: any = new Error(data.msg || '请求失败');
          error.name = 'BizError';
          error.info = data;
          throw error;
        }
        return response;
      },
      // 说明：umi 的 IErrorInterceptor 类型要求错误拦截器返回 Promise<Error>，
      // 而 401 重放场景需返回 Promise<AxiosResponse>，故此处断言放宽类型
      unauthorizedInterceptor as any,
    ],
  ],
};

// 根部挂载 ErrorBoundary，捕获 React 组件树渲染异常，避免白屏
export function rootContainer(container: React.ReactNode) {
  return <ErrorBoundary>{container}</ErrorBoundary>;
}
