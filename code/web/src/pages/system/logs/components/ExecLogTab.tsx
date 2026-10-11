import { useRef, useState } from 'react';
import { Button, Tag, message, Modal, Descriptions } from 'antd';
import { DownloadOutlined } from '@ant-design/icons';
import { ProTable } from '@ant-design/pro-components';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { getExecLogPage, exportLogs } from '@/services/log';
import type { ExecLogVO } from '@/services/log';

export default function ExecLogTab() {
  const actionRef = useRef<ActionType>();
  const [detailVisible, setDetailVisible] = useState(false);
  const [currentLog, setCurrentLog] = useState<ExecLogVO | null>(null);

  const handleExport = async () => {
    try {
      const res = await exportLogs('exec');
      const blob = new Blob([res.data], { type: 'text/csv;charset=utf-8' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `执行日志_${new Date().toLocaleDateString()}.csv`;
      a.click();
      window.URL.revokeObjectURL(url);
      message.success('导出成功');
    } catch {
      message.error('导出失败');
    }
  };

  const columns: ProColumns<ExecLogVO>[] = [
    {
      title: '时间',
      dataIndex: 'startedAt',
      width: 170,
      search: false,
      valueType: 'dateTime',
    },
    {
      title: '类型',
      dataIndex: 'execType',
      width: 100,
      valueType: 'select',
      valueEnum: {
        Action: 'Action',
        Function: 'Function',
        Pipeline: 'Pipeline',
      },
      render: (_, record) => {
        const colorMap: Record<string, string> = {
          Action: 'blue',
          Function: 'green',
          Pipeline: 'purple',
        };
        return (
          <Tag color={colorMap[record.execType] || 'default'}>
            {record.execType}
          </Tag>
        );
      },
    },
    {
      title: '任务名称',
      dataIndex: 'taskName',
      width: 200,
      search: false,
      ellipsis: true,
    },
    {
      title: '触发者',
      dataIndex: 'triggerName',
      width: 100,
      search: false,
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 80,
      valueType: 'select',
      valueEnum: {
        SUCCESS: '成功',
        FAIL: '失败',
        RUNNING: '运行中',
      },
      render: (_, record) => {
        const colorMap: Record<string, string> = {
          SUCCESS: 'success',
          FAIL: 'error',
          RUNNING: 'processing',
        };
        const textMap: Record<string, string> = {
          SUCCESS: '成功',
          FAIL: '失败',
          RUNNING: '运行中',
        };
        return (
          <Tag color={colorMap[record.status] || 'default'}>
            {textMap[record.status] || record.status}
          </Tag>
        );
      },
    },
    {
      title: '耗时',
      dataIndex: 'durationMs',
      width: 100,
      search: false,
      render: (_, record) => {
        if (record.durationMs >= 1000) {
          return `${(record.durationMs / 1000).toFixed(1)}s`;
        }
        return `${record.durationMs}ms`;
      },
    },
    {
      title: '操作',
      valueType: 'option',
      width: 80,
      render: (_, record) => (
        <a
          onClick={() => {
            setCurrentLog(record);
            setDetailVisible(true);
          }}
        >
          详情
        </a>
      ),
    },
  ];

  return (
    <>
      <ProTable<ExecLogVO>
        actionRef={actionRef}
        rowKey="id"
        columns={columns}
        search={{
          labelWidth: 'auto',
          defaultCollapsed: false,
        }}
        request={async (params) => {
          const res = await getExecLogPage({
            currentPage: params.current,
            pageSize: params.pageSize,
            execType: params.execType,
            status: params.status,
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

      <Modal
        title="执行日志详情"
        open={detailVisible}
        onCancel={() => setDetailVisible(false)}
        footer={null}
        width={600}
      >
        {currentLog && (
          <Descriptions column={2} bordered size="small">
            <Descriptions.Item label="类型">
              <Tag>{currentLog.execType}</Tag>
            </Descriptions.Item>
            <Descriptions.Item label="任务名称">{currentLog.taskName}</Descriptions.Item>
            <Descriptions.Item label="触发者">{currentLog.triggerName}</Descriptions.Item>
            <Descriptions.Item label="状态">
              <Tag color={currentLog.status === 'SUCCESS' ? 'success' : 'error'}>
                {currentLog.status === 'SUCCESS' ? '成功' : '失败'}
              </Tag>
            </Descriptions.Item>
            <Descriptions.Item label="开始时间">{currentLog.startedAt}</Descriptions.Item>
            <Descriptions.Item label="结束时间">{currentLog.finishedAt}</Descriptions.Item>
            <Descriptions.Item label="耗时">{currentLog.durationMs}ms</Descriptions.Item>
            <Descriptions.Item label="请求 ID">{currentLog.requestId}</Descriptions.Item>
            <Descriptions.Item label="详情" span={2}>
              <pre style={{ maxHeight: 200, overflowY: 'auto', margin: 0, fontSize: 12 }}>
                {currentLog.detail || '无'}
              </pre>
            </Descriptions.Item>
          </Descriptions>
        )}
      </Modal>
    </>
  );
}
