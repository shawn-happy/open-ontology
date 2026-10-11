import { useState, useEffect, useCallback } from 'react';
import {
  Card,
  Tree,
  Button,
  Space,
  Tag,
  Switch,
  message,
  Modal,
  Table,
  Typography,
} from 'antd';
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  UserSwitchOutlined,
  SwapOutlined,
  ExclamationCircleOutlined,
} from '@ant-design/icons';
import { getDeptTree, enableDisableDept, deleteDept } from '@/services/dept';
import type { DeptTreeVO } from '@/services/dept';
import DeptForm from './components/DeptForm';
import AssignUsersModal from './components/AssignUsers';
import styles from './index.less';

const { Title, Text } = Typography;

export default function DepartmentsPage() {
  const [deptTree, setDeptTree] = useState<any[]>([]);
  const [rawDeptData, setRawDeptData] = useState<DeptTreeVO[]>([]);
  const [selectedNode, setSelectedNode] = useState<DeptTreeVO | null>(null);
  const [formVisible, setFormVisible] = useState(false);
  const [editingNode, setEditingNode] = useState<DeptTreeVO | null>(null);
  const [defaultParentId, setDefaultParentId] = useState<string | undefined>();
  const [assignVisible, setAssignVisible] = useState(false);
  const [loading, setLoading] = useState(false);

  const loadDeptTree = useCallback(async () => {
    setLoading(true);
    try {
      const res = await getDeptTree();
      if (res.code === '000000') {
        const data = res.data || [];
        setRawDeptData(data);
        setDeptTree(convertDeptTree(data));
      }
    } catch (error) {
      console.error('Failed to load dept tree:', error);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadDeptTree();
  }, [loadDeptTree]);

  const convertDeptTree = (data: DeptTreeVO[]): any[] => {
    return data.map((item) => ({
      key: item.id,
      title: `${item.name} (${item.memberCount || 0}人)`,
      raw: item,
      children: item.children ? convertDeptTree(item.children) : [],
    }));
  };

  const handleSelect = (keys: any[], info: any) => {
    if (keys.length > 0) {
      setSelectedNode(info.node.raw as DeptTreeVO);
    } else {
      setSelectedNode(null);
    }
  };

  // 启用/停用部门
  const handleToggleStatus = (record: DeptTreeVO, checked: boolean) => {
    if (record.depth === 0 && !checked) {
      message.warning('根节点不可停用');
      return;
    }
    const status = checked ? 'ENABLED' : 'DISABLED';
    if (!checked && record.memberCount && record.memberCount > 0) {
      Modal.confirm({
        title: `确认停用该部门？`,
        content: `该部门下有 ${record.memberCount} 名用户，停用后将自动迁移至上级部门。`,
        icon: <ExclamationCircleOutlined />,
        onOk: async () => {
          await doToggleStatus(record.id, status);
        },
      });
    } else {
      Modal.confirm({
        title: `确认${checked ? '启用' : '停用'}该部门？`,
        icon: <ExclamationCircleOutlined />,
        onOk: async () => {
          await doToggleStatus(record.id, status);
        },
      });
    }
  };

  const doToggleStatus = async (id: string, status: string) => {
    try {
      const res = await enableDisableDept(id, status);
      if (res.code === '000000') {
        message.success(`${status === 'ENABLED' ? '启用' : '停用'}成功`);
        loadDeptTree();
        if (selectedNode?.id === id) {
          setSelectedNode({ ...selectedNode, status });
        }
      } else {
        message.error(res.msg);
      }
    } catch (error: any) {
      message.error(error?.message || '操作失败');
    }
  };

  // 删除部门
  const handleDelete = (record: DeptTreeVO) => {
    if (record.depth === 0) {
      message.warning('根节点不可删除');
      return;
    }
    Modal.confirm({
      title: '确认删除该部门？',
      content: '删除后不可恢复。',
      icon: <ExclamationCircleOutlined />,
      onOk: async () => {
        try {
          const res = await deleteDept(record.id);
          if (res.code === '000000') {
            message.success('删除成功');
            loadDeptTree();
            if (selectedNode?.id === record.id) {
              setSelectedNode(null);
            }
          } else {
            message.error(res.msg);
          }
        } catch (error: any) {
          message.error(error?.message || '操作失败');
        }
      },
    });
  };

  // 新增子部门
  const handleAddChild = () => {
    if (!selectedNode) {
      message.warning('请先选择一个部门');
      return;
    }
    setEditingNode(null);
    setDefaultParentId(selectedNode.id);
    setFormVisible(true);
  };

  // 新增一级部门
  const handleAddRoot = () => {
    setEditingNode(null);
    setDefaultParentId(undefined);
    setFormVisible(true);
  };

  // 编辑部门
  const handleEdit = () => {
    if (!selectedNode) return;
    setEditingNode(selectedNode);
    setFormVisible(true);
  };

  // 直属成员表格列
  const memberColumns = [
    { title: '姓名', dataIndex: 'name', width: 120 },
    { title: '用户名', dataIndex: 'username', width: 120 },
    {
      title: '角色',
      dataIndex: 'roleNames',
      render: (roles: string[]) => (
        <span>
          {(roles || []).map((r) => (
            <Tag key={r} color="blue" style={{ margin: 2 }}>
              {r}
            </Tag>
          ))}
        </span>
      ),
    },
  ];

  return (
    <div className={styles.deptPage}>
      {/* 左侧部门树 */}
      <Card className={styles.deptTree} size="small" loading={loading}>
        <div className={styles.deptTreeHeader}>
          <span className={styles.deptTreeTitle}>部门组织</span>
          <Button type="primary" size="small" onClick={handleAddRoot}>
            新增一级部门
          </Button>
        </div>
        <Tree
          treeData={deptTree}
          onSelect={handleSelect}
          defaultExpandAll
          showLine
          draggable
        />
      </Card>

      {/* 右侧详情区 */}
      <div className={styles.detailPanel}>
        {selectedNode ? (
          <>
            <div className={styles.detailHeader}>
              <Space>
                <span className={styles.detailTitle}>{selectedNode.name}</span>
                <Tag color={selectedNode.status === 'ENABLED' ? 'green' : 'red'}>
                  {selectedNode.status === 'ENABLED' ? '启用' : '停用'}
                </Tag>
              </Space>
              <Space>
                <Button icon={<PlusOutlined />} onClick={handleAddChild}>
                  新增子部门
                </Button>
                <Button icon={<EditOutlined />} onClick={handleEdit} disabled={selectedNode.depth === 0}>
                  编辑
                </Button>
                <Button
                  icon={<UserSwitchOutlined />}
                  onClick={() => setAssignVisible(true)}
                >
                  分配用户
                </Button>
                <Button
                  icon={<DeleteOutlined />}
                  danger
                  onClick={() => handleDelete(selectedNode)}
                  disabled={selectedNode.depth === 0}
                >
                  删除
                </Button>
              </Space>
            </div>

            <div className={styles.detailInfo}>
              <div>
                <div className={styles.detailLabel}>部门编码</div>
                <div className={styles.detailValue}>{selectedNode.code || '-'}</div>
              </div>
              <div>
                <div className={styles.detailLabel}>负责人</div>
                <div className={styles.detailValue}>{selectedNode.leaderName || '-'}</div>
              </div>
              <div>
                <div className={styles.detailLabel}>成员数</div>
                <div className={styles.detailValue}>{selectedNode.memberCount || 0}</div>
              </div>
              <div>
                <div className={styles.detailLabel}>排序</div>
                <div className={styles.detailValue}>{selectedNode.sortOrder ?? '-'}</div>
              </div>
              <div>
                <div className={styles.detailLabel}>层级深度</div>
                <div className={styles.detailValue}>{selectedNode.depth}</div>
              </div>
              <div>
                <div className={styles.detailLabel}>状态</div>
                <div className={styles.detailValue}>
                  <Switch
                    checked={selectedNode.status === 'ENABLED'}
                    onChange={(checked) => handleToggleStatus(selectedNode, checked)}
                    disabled={selectedNode.depth === 0}
                    size="small"
                  />
                </div>
              </div>
            </div>

            <Title level={5}>直属成员</Title>
            <Table
              dataSource={selectedNode.members || []}
              columns={memberColumns}
              rowKey="id"
              size="small"
              pagination={false}
              locale={{ emptyText: '暂无直属成员' }}
            />
          </>
        ) : (
          <div className={styles.emptyDetail}>
            <Text type="secondary">请在左侧选择一个部门查看详情</Text>
          </div>
        )}
      </div>

      {/* 新增/编辑弹窗 */}
      <DeptForm
        visible={formVisible}
        initialValues={editingNode}
        parentId={defaultParentId}
        allDeptData={rawDeptData}
        onCancel={() => {
          setFormVisible(false);
          setEditingNode(null);
        }}
        onSuccess={() => {
          setFormVisible(false);
          setEditingNode(null);
          loadDeptTree();
        }}
      />

      {/* 分配用户弹窗 */}
      <AssignUsersModal
        visible={assignVisible}
        dept={selectedNode}
        onCancel={() => {
          setAssignVisible(false);
        }}
        onSuccess={() => {
          setAssignVisible(false);
          loadDeptTree();
        }}
      />
    </div>
  );
}
