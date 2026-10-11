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
  Descriptions,
  Table,
  Typography,
} from 'antd';
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  ExclamationCircleOutlined,
  FolderOutlined,
  FileOutlined,
  ControlOutlined,
} from '@ant-design/icons';
import { getMenuTree, deleteMenu, enableDisableMenu } from '@/services/menu';
import type { MenuTreeVO } from '@/services/menu';
import MenuForm from './components/MenuForm';
import styles from './index.less';

const { Title, Text } = Typography;

export default function PermissionsPage() {
  const [menuTree, setMenuTree] = useState<any[]>([]);
  const [rawMenuData, setRawMenuData] = useState<MenuTreeVO[]>([]);
  const [selectedNode, setSelectedNode] = useState<MenuTreeVO | null>(null);
  const [formVisible, setFormVisible] = useState(false);
  const [editingNode, setEditingNode] = useState<MenuTreeVO | null>(null);
  const [defaultParentId, setDefaultParentId] = useState<string | undefined>();
  const [defaultType, setDefaultType] = useState<string>('DIRECTORY');
  const [loading, setLoading] = useState(false);

  // 加载菜单树
  const loadMenuTree = useCallback(async () => {
    setLoading(true);
    try {
      const res = await getMenuTree();
      if (res.code === '000000') {
        const data = res.data || [];
        setRawMenuData(data);
        setMenuTree(convertMenuTree(data));
      }
    } catch (error) {
      console.error('Failed to load menu tree:', error);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadMenuTree();
  }, [loadMenuTree]);

  // 转换菜单树数据
  const convertMenuTree = (data: MenuTreeVO[]): any[] => {
    return data.map((item) => ({
      key: item.id,
      title: (
        <Space>
          {getTypeIcon(item.type)}
          <span>{item.name}</span>
          <Tag color={getTypeColor(item.type)} style={{ fontSize: 10 }}>
            {getTypeLabel(item.type)}
          </Tag>
        </Space>
      ),
      raw: item,
      children: item.children ? convertMenuTree(item.children) : [],
    }));
  };

  const getTypeIcon = (type: string) => {
    switch (type) {
      case 'DIRECTORY':
        return <FolderOutlined style={{ color: '#1890ff' }} />;
      case 'MENU':
        return <FileOutlined style={{ color: '#52c41a' }} />;
      case 'BUTTON':
        return <ControlOutlined style={{ color: '#fa8c16' }} />;
      default:
        return null;
    }
  };

  const getTypeColor = (type: string) => {
    switch (type) {
      case 'DIRECTORY':
        return 'blue';
      case 'MENU':
        return 'green';
      case 'BUTTON':
        return 'orange';
      default:
        return 'default';
    }
  };

  const getTypeLabel = (type: string) => {
    switch (type) {
      case 'DIRECTORY':
        return '目录';
      case 'MENU':
        return '菜单';
      case 'BUTTON':
        return '按钮';
      default:
        return type;
    }
  };

  // 处理树节点选择
  const handleSelect = (keys: any[], info: any) => {
    if (keys.length > 0) {
      const node = info.node.raw as MenuTreeVO;
      setSelectedNode(node);
    } else {
      setSelectedNode(null);
    }
  };

  // 启用/停用
  const handleToggleStatus = (record: MenuTreeVO, checked: boolean) => {
    const status = checked ? 'ENABLED' : 'DISABLED';
    Modal.confirm({
      title: `确认${checked ? '启用' : '停用'}该节点？`,
      icon: <ExclamationCircleOutlined />,
      onOk: async () => {
        try {
          const res = await enableDisableMenu(record.id, status);
          if (res.code === '000000') {
            message.success(`${checked ? '启用' : '停用'}成功`);
            loadMenuTree();
            if (selectedNode?.id === record.id) {
              setSelectedNode({ ...record, status });
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

  // 删除节点
  const handleDelete = (record: MenuTreeVO) => {
    Modal.confirm({
      title: '确认删除该节点？',
      content: '删除后不可恢复，需确保无下级节点。',
      icon: <ExclamationCircleOutlined />,
      onOk: async () => {
        try {
          const res = await deleteMenu(record.id);
          if (res.code === '000000') {
            message.success('删除成功');
            loadMenuTree();
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

  // 新增节点
  const handleAdd = (type: string) => {
    setEditingNode(null);
    setDefaultType(type);
    setDefaultParentId(selectedNode?.id);
    setFormVisible(true);
  };

  // 编辑节点
  const handleEdit = () => {
    if (!selectedNode) return;
    setEditingNode(selectedNode);
    setFormVisible(true);
  };

  // 下级节点表格列
  const childrenColumns = [
    {
      title: '名称',
      dataIndex: 'name',
      width: 160,
    },
    {
      title: '类型',
      dataIndex: 'type',
      width: 80,
      render: (type: string) => (
        <Tag color={getTypeColor(type)}>{getTypeLabel(type)}</Tag>
      ),
    },
    {
      title: '权限标识',
      dataIndex: 'permission',
      width: 200,
    },
    {
      title: '排序',
      dataIndex: 'sortOrder',
      width: 60,
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 80,
      render: (status: string, record: MenuTreeVO) => (
        <Switch
          checked={status === 'ENABLED'}
          onChange={(checked) => handleToggleStatus(record, checked)}
          size="small"
        />
      ),
    },
    {
      title: '操作',
      width: 120,
      render: (_: any, record: MenuTreeVO) => (
        <Space size="small">
          <a
            onClick={() => {
              setEditingNode(record);
              setFormVisible(true);
            }}
          >
            编辑
          </a>
          <a onClick={() => handleDelete(record)}>删除</a>
        </Space>
      ),
    },
  ];

  return (
    <div className={styles.permissionsPage}>
      {/* 左侧菜单树 */}
      <Card className={styles.menuTree} size="small" loading={loading}>
        <div className={styles.menuTreeHeader}>
          <span className={styles.menuTreeTitle}>菜单资源树</span>
          <div className={styles.menuTreeActions}>
            <Button size="small" onClick={() => handleAdd('DIRECTORY')}>
              目录
            </Button>
            <Button size="small" onClick={() => handleAdd('MENU')}>
              菜单
            </Button>
            <Button size="small" onClick={() => handleAdd('BUTTON')}>
              按钮
            </Button>
          </div>
        </div>
        <Tree
          treeData={menuTree}
          onSelect={handleSelect}
          defaultExpandAll
          showLine
        />
      </Card>

      {/* 右侧详情区 */}
      <div className={styles.detailPanel}>
        {selectedNode ? (
          <>
            <div className={styles.detailHeader}>
              <Space>
                {getTypeIcon(selectedNode.type)}
                <span className={styles.detailTitle}>{selectedNode.name}</span>
                <Tag color={getTypeColor(selectedNode.type)}>
                  {getTypeLabel(selectedNode.type)}
                </Tag>
              </Space>
              <Space>
                <Button icon={<EditOutlined />} onClick={handleEdit}>
                  编辑
                </Button>
                <Button
                  icon={<DeleteOutlined />}
                  danger
                  onClick={() => handleDelete(selectedNode)}
                >
                  删除
                </Button>
              </Space>
            </div>

            <div className={styles.detailInfo}>
              <div>
                <div className={styles.detailLabel}>权限标识</div>
                <div className={styles.detailValue}>
                  {selectedNode.permission || '-'}
                </div>
              </div>
              <div>
                <div className={styles.detailLabel}>路由路径</div>
                <div className={styles.detailValue}>
                  {selectedNode.path || '-'}
                </div>
              </div>
              <div>
                <div className={styles.detailLabel}>排序</div>
                <div className={styles.detailValue}>
                  {selectedNode.sortOrder ?? '-'}
                </div>
              </div>
              <div>
                <div className={styles.detailLabel}>状态</div>
                <div className={styles.detailValue}>
                  <Switch
                    checked={selectedNode.status === 'ENABLED'}
                    onChange={(checked) =>
                      handleToggleStatus(selectedNode, checked)
                    }
                    size="small"
                  />
                </div>
              </div>
              <div>
                <div className={styles.detailLabel}>图标</div>
                <div className={styles.detailValue}>
                  {selectedNode.icon || '-'}
                </div>
              </div>
            </div>

            {/* 下级节点 */}
            <Title level={5}>下级节点</Title>
            <Table
              dataSource={selectedNode.children || []}
              columns={childrenColumns}
              rowKey="id"
              size="small"
              pagination={false}
            />
          </>
        ) : (
          <div className={styles.emptyDetail}>
            <Text type="secondary">请在左侧选择一个节点查看详情</Text>
          </div>
        )}
      </div>

      {/* 新增/编辑弹窗 */}
      <MenuForm
        visible={formVisible}
        initialValues={editingNode}
        parentId={defaultParentId}
        defaultType={defaultType}
        allMenuData={rawMenuData}
        onCancel={() => {
          setFormVisible(false);
          setEditingNode(null);
        }}
        onSuccess={() => {
          setFormVisible(false);
          setEditingNode(null);
          loadMenuTree();
        }}
      />
    </div>
  );
}
