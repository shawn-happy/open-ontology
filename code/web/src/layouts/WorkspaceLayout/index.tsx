import { useEffect } from 'react';
import { Outlet, history, useLocation, useParams, useModel } from '@umijs/max';
import {
  Avatar,
  Dropdown,
  Layout,
  Menu,
  Select,
  Spin,
  Tag,
  message,
} from 'antd';
import type { MenuProps } from 'antd';
import {
  ArrowLeftOutlined,
  HomeOutlined,
  KeyOutlined,
  LogoutOutlined,
  TeamOutlined,
  UserOutlined,
} from '@ant-design/icons';
import { logout as logoutApi } from '@/services/auth';
import styles from './index.less';

const { Header, Sider, Content } = Layout;

/** 内置角色徽标色（与分配记录页 RoleBadge 一致） */
const ROLE_COLORS: Record<string, string> = {
  OWNER: 'purple',
  MAINTAINER: 'blue',
  DEVELOPER: 'green',
  REPORTER: 'cyan',
};

/**
 * 空间级布局（精简版）：顶栏（返回平台 + 空间切换下拉 + 用户下拉）+ 侧边栏两项（空间首页 / 成员管理）
 *
 * 挂载时拉取 accessible 解析当前空间 {name, workspaceRole} 写入 useModel('workspace')；
 * 空间不在可访问列表 → toast「无权访问该空间」并跳转平台首页（PRD FR-6）。
 */
export default function WorkspaceLayout() {
  const { workspaceCode } = useParams<{ workspaceCode: string }>();
  const location = useLocation();
  const {
    workspaceCode: currentCode,
    accessibleWorkspaces,
    myRole,
    initialized,
    initWorkspace,
  } = useModel('workspace');

  useEffect(() => {
    if (!workspaceCode) {
      return;
    }
    let cancelled = false;
    (async () => {
      const ok = await initWorkspace(workspaceCode);
      if (!ok && !cancelled) {
        message.error('无权访问该空间');
        history.push('/');
      }
    })();
    return () => {
      cancelled = true;
    };
  }, [workspaceCode, initWorkspace]);

  let userInfo: Record<string, any> = {};
  try {
    userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
  } catch {
    // ignore
  }

  const handleLogout = async () => {
    try {
      await logoutApi();
    } catch {
      // 即使接口失败也清除本地状态
    }
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('userInfo');
    localStorage.removeItem('permissions');
    localStorage.removeItem('workspaceCode');
    history.push('/login');
  };

  const userMenuItems: MenuProps['items'] = [
    { key: 'profile', icon: <UserOutlined />, label: '用户信息' },
    { key: 'password', icon: <KeyOutlined />, label: '修改密码' },
    { type: 'divider' },
    { key: 'logout', icon: <LogoutOutlined />, label: '退出登录' },
  ];

  const handleUserMenuClick: MenuProps['onClick'] = ({ key }) => {
    if (key === 'profile') {
      history.push('/system/profile');
    } else if (key === 'password') {
      history.push('/system/profile?action=changePassword');
    } else if (key === 'logout') {
      handleLogout();
    }
  };

  const menuItems: MenuProps['items'] = [
    {
      key: `/workspace/${workspaceCode}/home`,
      icon: <HomeOutlined />,
      label: '空间首页',
    },
    {
      key: `/workspace/${workspaceCode}/members`,
      icon: <TeamOutlined />,
      label: '成员管理',
    },
  ];

  // 当前选中菜单：/members 前缀匹配成员管理，其余归空间首页
  const selectedKey = location.pathname.includes('/members')
    ? `/workspace/${workspaceCode}/members`
    : `/workspace/${workspaceCode}/home`;

  return (
    <Layout className={styles.workspaceLayout}>
      <Header className={styles.header}>
        <div className={styles.headerLeft}>
          <a className={styles.backLink} onClick={() => history.push('/')}>
            <ArrowLeftOutlined /> 返回平台
          </a>
          <span className={styles.divider} />
          <Select
            className={styles.workspaceSwitch}
            value={currentCode || workspaceCode}
            options={accessibleWorkspaces.map((w) => ({
              label: w.name,
              value: w.code,
            }))}
            onChange={(code) => history.push(`/workspace/${code}/home`)}
            popupMatchSelectWidth={false}
          />
          {myRole && (
            <Tag color={ROLE_COLORS[myRole] || 'green'} className={styles.roleTag}>
              {myRole.charAt(0) + myRole.slice(1).toLowerCase()}
            </Tag>
          )}
        </div>
        <Dropdown
          menu={{ items: userMenuItems, onClick: handleUserMenuClick }}
          trigger={['click']}
        >
          <div className={styles.userArea}>
            <Avatar size={28} src={userInfo.avatar || undefined} icon={<UserOutlined />} />
            <span className={styles.userName}>
              {userInfo.name || userInfo.username || '用户'}
            </span>
          </div>
        </Dropdown>
      </Header>
      <Layout>
        <Sider width={200} theme="light" className={styles.sider}>
          <Menu
            mode="inline"
            selectedKeys={[selectedKey]}
            items={menuItems}
            onClick={({ key }) => history.push(key)}
            className={styles.menu}
          />
        </Sider>
        <Content className={styles.content}>
          {initialized ? (
            <Outlet />
          ) : (
            <div className={styles.loadingWrap}>
              <Spin size="large" />
              <div className={styles.loadingText}>正在加载空间信息…</div>
            </div>
          )}
        </Content>
      </Layout>
    </Layout>
  );
}
