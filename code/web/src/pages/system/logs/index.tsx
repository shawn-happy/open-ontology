import { Tabs } from 'antd';
import AuditLogTab from './components/AuditLogTab';
import LoginLogTab from './components/LoginLogTab';
import ExecLogTab from './components/ExecLogTab';
import styles from './index.less';

export default function LogsPage() {
  const tabItems = [
    {
      key: 'audit',
      label: '审计日志',
      children: <AuditLogTab />,
    },
    {
      key: 'login',
      label: '登录日志',
      children: <LoginLogTab />,
    },
    {
      key: 'exec',
      label: '执行日志',
      children: <ExecLogTab />,
    },
  ];

  return (
    <div className={styles.logsPage}>
      <Tabs items={tabItems} defaultActiveKey="audit" className={styles.logTab} />
    </div>
  );
}
