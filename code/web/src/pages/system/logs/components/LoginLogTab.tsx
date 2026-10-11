import { useRef } from 'react';
import { Button, Tag, message, Alert } from 'antd';
import { DownloadOutlined } from '@ant-design/icons';
import { ProTable } from '@ant-design/pro-components';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { getLoginLogPage, exportLogs } from '@/services/log';
import type { LoginLogVO } from '@/services/log';

export default function LoginLogTab() {
  const actionRef = useRef<ActionType>();

  const handleExport = async () => {
    try {
      const res = await exportLogs('login');
      const blob = new Blob([res.data], { type: 'text/csv;charset=utf-8' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `登录日志_${new Date().toLocaleDateString()}.csv`;
      a.click();
      window.URL.revokeObjectURL(url);
      message.success('导出成功');
    } catch {
      message.error('导出失败');
    }
  };

  const columns: ProColumns<LoginLogVO>[] = [
    {
      title: '时间',
      dataIndex: 'loginAt',
      width: 170,
      search: false,
      valueType: 'dateTime',
    },
    {
      title: '用户名',
      dataIndex: 'username',
      width: 120,
    },
    {
      title: '姓名',
      dataIndex: 'name',
      width: 100,
      search: false,
    },
    {
      title: 'IP',
      dataIndex: 'ipAddress',
      width: 140,
      search: false,
    },
    {
      title: '位置',
      dataIndex: 'location',
      width: 120,
      search: false,
    },
    {
      title: '浏览器/系统',
      width: 200,
      search: false,
      render: (_, record) => (
        <span>
          {record.browser} / {record.os}
        </span>
      ),
    },
    {
      title: '结果',
      dataIndex: 'result',
      width: 80,
      valueType: 'select',
      valueEnum: {
        SUCCESS: '成功',
        FAIL: '失败',
      },
      render: (_, record) => (
        <Tag color={record.result === 'SUCCESS' ? 'success' : 'error'}>
          {record.result === 'SUCCESS' ? '成功' : '失败'}
        </Tag>
      ),
    },
  ];

  return (
    <>
      <Alert
        message="登录失败超过 5 次自动锁定 30 分钟"
        type="info"
        showIcon
        style={{ marginBottom: 16 }}
      />
      <ProTable<LoginLogVO>
        actionRef={actionRef}
        rowKey="id"
        columns={columns}
        search={{
          labelWidth: 'auto',
          defaultCollapsed: false,
        }}
        request={async (params) => {
          const res = await getLoginLogPage({
            currentPage: params.current,
            pageSize: params.pageSize,
            username: params.username,
            result: params.result,
          });
          if (res.code === '000000') {
            const pageData = res.data;
            return {
              data: pageData.data || [],
              total: pageData.pageInfo?.total || 0,
              success: true,
            };
          }
          return { data: [], total: 0, success: true };
        }}
        toolBarRender={() => [
          <Button key="export" icon={<DownloadOutlined />} onClick={handleExport}>
            导出
          </Button>,
        ]}
        pagination={{
          defaultPageSize: 10,
          showSizeChanger: true,
        }}
      />
    </>
  );
}
