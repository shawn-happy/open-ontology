import { useState, useRef } from 'react';
import { Button, Space, Tag, Switch, message, Modal } from 'antd';
import {
  PlusOutlined,
  ExclamationCircleOutlined,
} from '@ant-design/icons';
import { ProTable } from '@ant-design/pro-components';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import {
  getRolePage,
  enableDisableRole,
} from '@/services/role';
import type { RoleListVO } from '@/services/role';
import RoleForm from './components/RoleForm';
import ConfigurePermissions from './components/ConfigurePermissions';
import AssignUsers from './components/AssignUsers';
import styles from './index.less';

export default function RolesPage() {
  const actionRef = useRef<ActionType>();
  const [roleFormVisible, setRoleFormVisible] = useState(false);
  const [editingRole, setEditingRole] = useState<RoleListVO | null>(null);
  const [permVisible, setPermVisible] = useState(false);
  const [assignVisible, setAssignVisible] = useState(false);
  const [currentRole, setCurrentRole] = useState<RoleListVO | null>(null);

  // 启用/停用角色
  const handleToggleStatus = (record: RoleListVO, checked: boolean) => {
    if (record.builtIn) {
      message.warning('内置角色不可停用');
      return;
    }
    const status = checked ? 'ENABLED' : 'DISABLED';
    Modal.confirm({
      title: `确认${checked ? '启用' : '停用'}该角色？`,
      icon: <ExclamationCircleOutlined />,
      onOk: async () => {
        try {
          const res = await enableDisableRole(record.id, status);
          if (res.code === '000000') {
            message.success(`${checked ? '启用' : '停用'}成功`);
            actionRef.current?.reload();
          } else {
            message.error(res.msg);
          }
        } catch (error: any) {
          message.error(error?.message || '操作失败');
        }
      },
    });
  };

  const columns: ProColumns<RoleListVO>[] = [
    {
      title: '角色名称',
      dataIndex: 'name',
      width: 160,
      render: (_, record) => (
        <Space>
          <span>{record.name}</span>
          {record.builtIn && <Tag color="orange" className={styles.builtInTag}>内置</Tag>}
        </Space>
      ),
    },
    {
      title: '角色编码',
      dataIndex: 'code',
      width: 140,
      search: false,
    },
    {
      title: '类型',
      dataIndex: 'type',
      width: 80,
      search: false,
      render: (_, record) => (
        <Tag color={record.type === 'GLOBAL' ? 'blue' : 'green'}>
          {record.type === 'GLOBAL' ? '全局' : '空间'}
        </Tag>
      ),
    },
    {
      title: '空间权限',
      dataIndex: 'dataScope',
      width: 140,
      search: false,
      render: (_, record) => {
        const scopeMap: Record<string, string> = {
          ALL: '全部空间',
          DEPT_AND_CHILDREN: '本部门及子部门',
          DEPT: '本部门',
          SELF: '仅自己创建',
          CUSTOM: '自定义',
        };
        return scopeMap[record.dataScope] || '-';
      },
    },
    {
      title: '成员数',
      dataIndex: 'memberCount',
      width: 80,
      search: false,
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 80,
      search: false,
      render: (_, record) => (
        <Switch
          checked={record.status === 'ENABLED'}
          onChange={(checked) => handleToggleStatus(record, checked)}
          disabled={record.builtIn}
          size="small"
        />
      ),
    },
    {
      title: '备注',
      dataIndex: 'remark',
      width: 160,
      search: false,
      ellipsis: true,
    },
    {
      title: '更新时间',
      dataIndex: 'updatedAt',
      width: 170,
      search: false,
      valueType: 'dateTime',
    },
    {
      title: '操作',
      valueType: 'option',
      width: 240,
      render: (_, record) => {
        if (record.code === 'super_admin') {
          return <span style={{ color: '#999' }}>不可操作</span>;
        }
        return (
          <Space size="small">
            <a
              onClick={() => {
                setEditingRole(record);
                setRoleFormVisible(true);
              }}
            >
              编辑
            </a>
            <a
              onClick={() => {
                setCurrentRole(record);
                setPermVisible(true);
              }}
            >
              配置权限
            </a>
            <a
              onClick={() => {
                setCurrentRole(record);
                setAssignVisible(true);
              }}
            >
              分配用户
            </a>
          </Space>
        );
      },
    },
  ];

  return (
    <div className={styles.rolesPage}>
      <ProTable<RoleListVO>
        headerTitle="角色管理"
        actionRef={actionRef}
        rowKey="id"
        columns={columns}
        search={{
          labelWidth: 'auto',
          defaultCollapsed: false,
        }}
        request={async (params) => {
          const res = await getRolePage({
            currentPage: params.current,
            pageSize: params.pageSize,
            keyword: params.keyword,
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
          <Button
            key="create"
            type="primary"
            icon={<PlusOutlined />}
            onClick={() => {
              setEditingRole(null);
              setRoleFormVisible(true);
            }}
          >
            新增角色
          </Button>,
        ]}
        pagination={{
          defaultPageSize: 10,
          showSizeChanger: true,
        }}
      />

      <RoleForm
        visible={roleFormVisible}
        initialValues={editingRole}
        onCancel={() => {
          setRoleFormVisible(false);
          setEditingRole(null);
        }}
        onSuccess={() => {
          setRoleFormVisible(false);
          setEditingRole(null);
          actionRef.current?.reload();
        }}
      />

      <ConfigurePermissions
        visible={permVisible}
        role={currentRole}
        onCancel={() => {
          setPermVisible(false);
          setCurrentRole(null);
        }}
        onSuccess={() => {
          setPermVisible(false);
          setCurrentRole(null);
          actionRef.current?.reload();
        }}
      />

      <AssignUsers
        visible={assignVisible}
        role={currentRole}
        onCancel={() => {
          setAssignVisible(false);
          setCurrentRole(null);
        }}
        onSuccess={() => {
          setAssignVisible(false);
          setCurrentRole(null);
          actionRef.current?.reload();
        }}
      />
    </div>
  );
}
