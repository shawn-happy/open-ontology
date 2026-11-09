import { Tabs } from 'antd';
import { useAccess } from '@umijs/max';
import AuditLogTab from './components/AuditLogTab';
import LoginLogTab from './components/LoginLogTab';
import ExecLogTab from './components/ExecLogTab';
import styles from './index.less';

export default function LogsPage() {
  const access = useAccess();

  // Tab 可见性与后端 BUTTON 权限码对齐
  const allTabs = [
    {
      key: 'audit',
      label: '审计日志',
      children: <AuditLogTab />,
      perm: 'system:log:audit',
    },
    {
      key: 'login',
      label: '登录日志',
      children: <LoginLogTab />,
      perm: 'system:log:login',
    },
    {
      key: 'exec',
      label: '执行日志',
      children: <ExecLogTab />,
      perm: 'system:log:exec',
    },
  ];
  const tabItems = allTabs
    .filter((tab) => access.hasPerm(tab.perm))
    .map(({ perm, ...tab }) => tab);

  return (
    <div className={styles.logsPage}>
      <Tabs
        items={tabItems}
        defaultActiveKey={tabItems[0]?.key}
        className={styles.logTab}
      />
    </div>
  );
}
