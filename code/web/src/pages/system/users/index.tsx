import { useState, useRef, useEffect, useCallback } from 'react';
import {
  Card,
  Tree,
  Button,
  Space,
  Tag,
  Switch,
  message,
  Modal,
  Avatar,
} from 'antd';
import {
  PlusOutlined,
  DownloadOutlined,
  UploadOutlined,
  ExclamationCircleOutlined,
} from '@ant-design/icons';
import { ProTable } from '@ant-design/pro-components';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { getDeptTree } from '@/services/dept';
import type { DeptTreeVO } from '@/services/dept';
import {
  getUserPage,
  enableDisableUser,
  resetPassword,
  exportUsers,
} from '@/services/user';
import type { UserListVO } from '@/services/user';
import UserForm from './components/UserForm';
import AssignRoleModal from './components/AssignRole';
import AssignDeptModal from './components/AssignDept';
import ImportUsersModal from './components/ImportUsers';
import styles from './index.less';

export default function UsersPage() {
  const actionRef = useRef<ActionType>();
  const [deptTree, setDeptTree] = useState<any[]>([]);
  const [selectedDeptId, setSelectedDeptId] = useState<string | undefined>();
  const [selectedDeptName, setSelectedDeptName] = useState<string>('');
  const [userFormVisible, setUserFormVisible] = useState(false);
  const [editingUser, setEditingUser] = useState<UserListVO | null>(null);
  const [assignRoleVisible, setAssignRoleVisible] = useState(false);
  const [assignDeptVisible, setAssignDeptVisible] = useState(false);
  const [importVisible, setImportVisible] = useState(false);
  const [currentUser, setCurrentUser] = useState<UserListVO | null>(null);

  // 加载部门树
  const loadDeptTree = useCallback(async () => {
    try {
      const res = await getDeptTree();
      if (res.code === '000000') {
        const tree = convertDeptTree(res.data || []);
        setDeptTree(tree);
      }
    } catch (error) {
      console.error('Failed to load dept tree:', error);
    }
  }, []);

  // 转换部门树数据为Tree组件格式
  const convertDeptTree = (data: DeptTreeVO[]): any[] => {
    return data.map((item) => ({
      key: item.id,
      title: `${item.name} (${item.memberCount || 0}人)`,
      children: item.children ? convertDeptTree(item.children) : [],
    }));
  };

  useEffect(() => {
    loadDeptTree();
  }, [loadDeptTree]);

  // 导出用户
  const handleExport = async () => {
    try {
      const res = await exportUsers({
        deptId: selectedDeptId,
        keyword: '',
        status: '',
      });
      const blob = new Blob([res.data], {
        type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `用户列表_${new Date().toLocaleDateString()}.xlsx`;
      a.click();
      window.URL.revokeObjectURL(url);
      message.success('导出成功');
    } catch {
      message.error('导出失败');
    }
  };

  // 启用/停用用户
  const handleToggleStatus = (record: UserListVO, checked: boolean) => {
    if (record.username === 'admin') {
      message.warning('admin 账号不可停用');
      return;
    }
    const status = checked ? 'ENABLED' : 'DISABLED';
    Modal.confirm({
      title: `确认${checked ? '启用' : '停用'}该用户？`,
      icon: <ExclamationCircleOutlined />,
      onOk: async () => {
        try {
          const res = await enableDisableUser(record.id, status);
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

  // 重置密码
  const handleResetPassword = (record: UserListVO) => {
    if (record.username === 'admin') {
      message.warning('admin 账号请通过安全渠道重置密码');
      return;
    }
    Modal.confirm({
      title: '确认重置密码？',
      content: '重置后密码将恢复为初始密码 onto12345678!，用户下次登录时须强制修改密码。',
      icon: <ExclamationCircleOutlined />,
      onOk: async () => {
        try {
          const res = await resetPassword(record.id);
          if (res.code === '000000') {
            message.success('密码已重置');
          } else {
            message.error(res.msg);
          }
        } catch (error: any) {
          message.error(error?.message || '操作失败');
        }
      },
    });
  };

  // 表格列定义
  const columns: ProColumns<UserListVO>[] = [
    {
      title: '头像',
      dataIndex: 'avatar',
      width: 60,
      search: false,
      render: (_, record) => {
        if (record.avatar) {
          return <Avatar src={record.avatar} size={32} />;
        }
        return (
          <span className={styles.avatarText}>
            {record.name?.charAt(0) || '?'}
          </span>
        );
      },
    },
    {
      title: '姓名',
      dataIndex: 'name',
      width: 100,
      search: false,
    },
    {
      title: '用户名',
      dataIndex: 'username',
      width: 120,
      search: false,
    },
    {
      title: '手机号',
      dataIndex: 'phone',
      width: 130,
      search: false,
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      width: 180,
      search: false,
      ellipsis: true,
    },
    {
      title: '部门',
      dataIndex: 'deptName',
      width: 120,
      search: false,
    },
    {
      title: '角色',
      dataIndex: 'roleNames',
      width: 160,
      search: false,
      render: (_, record) => (
        <span>
          {(record.roleNames || []).map((name) => (
            <Tag key={name} className={styles.roleTag} color="blue">
              {name}
            </Tag>
          ))}
        </span>
      ),
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
          disabled={record.username === 'admin'}
          size="small"
        />
      ),
    },
    {
      title: '最近登录',
      dataIndex: 'lastLoginAt',
      width: 170,
      search: false,
      valueType: 'dateTime',
    },
    {
      title: '操作',
      valueType: 'option',
      width: 280,
      fixed: 'right',
      render: (_, record) => (
        <Space size="small">
          <a
            onClick={() => {
              setEditingUser(record);
              setUserFormVisible(true);
            }}
          >
            编辑
          </a>
          <a
            onClick={() => {
              setCurrentUser(record);
              setAssignRoleVisible(true);
            }}
          >
            分配角色
          </a>
          <a
            onClick={() => {
              setCurrentUser(record);
              setAssignDeptVisible(true);
            }}
          >
            分配部门
          </a>
          <a onClick={() => handleResetPassword(record)}>重置密码</a>
        </Space>
      ),
    },
  ];

  return (
    <div className={styles.userPage}>
      {/* 左侧部门树 */}
      <Card className={styles.deptTree} size="small">
        <div className={styles.deptTreeTitle}>部门筛选</div>
        <Tree
          treeData={deptTree}
          onSelect={(keys, info) => {
            if (keys.length > 0) {
              setSelectedDeptId(keys[0] as string);
              setSelectedDeptName(info.node.title as string);
            } else {
              setSelectedDeptId(undefined);
              setSelectedDeptName('');
            }
            actionRef.current?.reload();
          }}
          defaultExpandAll
          showLine
        />
        {selectedDeptId && (
          <Button
            type="link"
            size="small"
            onClick={() => {
              setSelectedDeptId(undefined);
              setSelectedDeptName('');
              actionRef.current?.reload();
            }}
            style={{ marginTop: 8 }}
          >
            清除筛选
          </Button>
        )}
      </Card>

      {/* 右侧用户列表 */}
      <div className={styles.userContent}>
        <ProTable<UserListVO>
          headerTitle={
            <Space>
              <span>用户管理</span>
              {selectedDeptName && (
                <Tag color="blue">
                  部门: {selectedDeptName}
                </Tag>
              )}
            </Space>
          }
          actionRef={actionRef}
          rowKey="id"
          columns={columns}
          scroll={{ x: 'max-content' }}
          search={{
            labelWidth: 'auto',
            defaultCollapsed: false,
          }}
          params={{ deptId: selectedDeptId }}
          request={async (params) => {
            const res = await getUserPage({
              currentPage: params.current,
              pageSize: params.pageSize,
              keyword: params.keyword,
              deptId: params.deptId || selectedDeptId,
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
            <Button
              key="create"
              type="primary"
              icon={<PlusOutlined />}
              onClick={() => {
                setEditingUser(null);
                setUserFormVisible(true);
              }}
            >
              新增用户
            </Button>,
            <Button
              key="import"
              icon={<UploadOutlined />}
              onClick={() => setImportVisible(true)}
            >
              批量导入
            </Button>,
            <Button key="export" icon={<DownloadOutlined />} onClick={handleExport}>
              批量导出
            </Button>,
          ]}
          pagination={{
            defaultPageSize: 10,
            showSizeChanger: true,
          }}
        />
      </div>

      {/* 新增/编辑用户弹窗 */}
      <UserForm
        visible={userFormVisible}
        initialValues={editingUser}
        onCancel={() => {
          setUserFormVisible(false);
          setEditingUser(null);
        }}
        onSuccess={() => {
          setUserFormVisible(false);
          setEditingUser(null);
          actionRef.current?.reload();
        }}
      />

      {/* 分配角色弹窗 */}
      <AssignRoleModal
        visible={assignRoleVisible}
        user={currentUser}
        onCancel={() => {
          setAssignRoleVisible(false);
          setCurrentUser(null);
        }}
        onSuccess={() => {
          setAssignRoleVisible(false);
          setCurrentUser(null);
          actionRef.current?.reload();
        }}
      />

      {/* 分配部门弹窗 */}
      <AssignDeptModal
        visible={assignDeptVisible}
        user={currentUser}
        onCancel={() => {
          setAssignDeptVisible(false);
          setCurrentUser(null);
        }}
        onSuccess={() => {
          setAssignDeptVisible(false);
          setCurrentUser(null);
          actionRef.current?.reload();
        }}
      />

      {/* 批量导入弹窗 */}
      <ImportUsersModal
        visible={importVisible}
        onCancel={() => setImportVisible(false)}
        onSuccess={() => {
          setImportVisible(false);
          actionRef.current?.reload();
        }}
      />
    </div>
  );
}
