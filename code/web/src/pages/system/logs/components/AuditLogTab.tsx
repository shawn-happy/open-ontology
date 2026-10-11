import { useRef, useState } from 'react';
import { Button, Tag, message, Modal, Descriptions } from 'antd';
import { DownloadOutlined, ExclamationCircleOutlined } from '@ant-design/icons';
import { ProTable } from '@ant-design/pro-components';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { getAuditLogPage, exportLogs } from '@/services/log';
import type { AuditLogVO } from '@/services/log';

export default function AuditLogTab() {
  const actionRef = useRef<ActionType>();
  const [detailVisible, setDetailVisible] = useState(false);
  const [currentLog, setCurrentLog] = useState<AuditLogVO | null>(null);

  const handleExport = async () => {
    try {
      const res = await exportLogs('audit');
      const blob = new Blob([res.data], { type: 'text/csv;charset=utf-8' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `审计日志_${new Date().toLocaleDateString()}.csv`;
      a.click();
      window.URL.revokeObjectURL(url);
      message.success('导出成功');
    } catch {
      message.error('导出失败');
    }
  };

  const columns: ProColumns<AuditLogVO>[] = [
    {
      title: '时间',
      dataIndex: 'operateAt',
      width: 170,
      search: false,
      valueType: 'dateTime',
    },
    {
      title: '操作者',
      dataIndex: 'operatorName',
      width: 100,
      search: false,
    },
    {
      title: '模块',
      dataIndex: 'module',
      width: 120,
      valueType: 'select',
      valueEnum: {
        system: '系统管理',
        ontology: '本体管理',
        data: '数据资产',
        security: '安全策略',
        application: '应用资产',
      },
      render: (_, record) => {
        const moduleMap: Record<string, string> = {
          system: '系统管理',
          ontology: '本体管理',
          data: '数据资产',
          security: '安全策略',
          application: '应用资产',
        };
        return <Tag>{moduleMap[record.module] || record.module}</Tag>;
      },
    },
    {
      title: '操作内容',
      dataIndex: 'content',
      ellipsis: true,
      search: false,
    },
    {
      title: '类型',
      dataIndex: 'type',
      width: 80,
      valueType: 'select',
      valueEnum: {
        CREATE: '新增',
        UPDATE: '修改',
        DELETE: '删除',
        ENABLE: '启用',
        DISABLE: '停用',
      },
      render: (_, record) => {
        const typeMap: Record<string, { text: string; color: string }> = {
          CREATE: { text: '新增', color: 'green' },
          UPDATE: { text: '修改', color: 'blue' },
          DELETE: { text: '删除', color: 'red' },
          ENABLE: { text: '启用', color: 'cyan' },
          DISABLE: { text: '停用', color: 'orange' },
        };
        const info = typeMap[record.type] || { text: record.type, color: 'default' };
        return <Tag color={info.color}>{info.text}</Tag>;
      },
    },
    {
      title: '结果',
      dataIndex: 'result',
      width: 80,
      search: false,
      render: (_, record) => (
        <Tag color={record.result === 'SUCCESS' ? 'success' : 'error'}>
          {record.result === 'SUCCESS' ? '成功' : '失败'}
        </Tag>
      ),
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
      <ProTable<AuditLogVO>
        actionRef={actionRef}
        rowKey="id"
        columns={columns}
        search={{
          labelWidth: 'auto',
          defaultCollapsed: false,
        }}
        request={async (params) => {
          const res = await getAuditLogPage({
            currentPage: params.current,
            pageSize: params.pageSize,
            keyword: params.keyword,
            module: params.module,
            type: params.type,
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
        title="审计日志详情"
        open={detailVisible}
        onCancel={() => setDetailVisible(false)}
        footer={null}
        width={600}
      >
        {currentLog && (
          <Descriptions column={2} bordered size="small">
            <Descriptions.Item label="操作时间">{currentLog.operateAt}</Descriptions.Item>
            <Descriptions.Item label="操作者">{currentLog.operatorName}</Descriptions.Item>
            <Descriptions.Item label="模块">{currentLog.module}</Descriptions.Item>
            <Descriptions.Item label="操作类型">{currentLog.type}</Descriptions.Item>
            <Descriptions.Item label="操作内容" span={2}>{currentLog.content}</Descriptions.Item>
            <Descriptions.Item label="结果">
              <Tag color={currentLog.result === 'SUCCESS' ? 'success' : 'error'}>
                {currentLog.result === 'SUCCESS' ? '成功' : '失败'}
              </Tag>
            </Descriptions.Item>
            <Descriptions.Item label="IP 地址">{currentLog.ipAddress}</Descriptions.Item>
            <Descriptions.Item label="请求 ID" span={2}>{currentLog.requestId}</Descriptions.Item>
            <Descriptions.Item label="User-Agent" span={2}>{currentLog.userAgent}</Descriptions.Item>
          </Descriptions>
        )}
      </Modal>
    </>
  );
}
