import { useCallback, useEffect, useState } from 'react';
import { history, useAccess } from '@umijs/max';
import {
  Button,
  Card,
  Empty,
  Input,
  Modal,
  Pagination,
  Segmented,
  Select,
  Space,
  Spin,
  Statistic,
  Switch,
  Table,
  Tooltip,
  message,
} from 'antd';
import type { ColumnsType } from 'antd/es/table';
import {
  AppstoreOutlined,
  BarsOutlined,
  ExclamationCircleOutlined,
  PlusOutlined,
} from '@ant-design/icons';
import {
  getWorkspaceOverview,
  getWorkspacePage,
  updateWorkspaceStatus,
} from '@/services/workspace';
import type {
  WorkspaceListVO,
  WorkspaceOverviewVO,
} from '@/services/workspace';
import WorkspaceForm from './components/WorkspaceForm';
import AssignManageModal from './components/AssignManage';
import styles from './index.less';

/** 视图模式：卡片 / 列表 */
type ViewMode = 'card' | 'list';

/**
 * 空间管理页（系统管理）：
 * 卡片 / 列表双视图，支持关键字与状态筛选、服务端分页；
 * 行操作：进入空间、分配管理、编辑（对齐原型 system/sys-workspaces.html）。
 */
export default function WorkspacesPage() {
  const access = useAccess();

  const [viewMode, setViewMode] = useState<ViewMode>('card');
  const [overview, setOverview] = useState<WorkspaceOverviewVO | null>(null);
  const [list, setList] = useState<WorkspaceListVO[]>([]);
  const [loading, setLoading] = useState(false);
  const [keyword, setKeyword] = useState('');
  const [debouncedKeyword, setDebouncedKeyword] = useState('');
  const [statusFilter, setStatusFilter] = useState<string | undefined>();
  const [current, setCurrent] = useState(1);
  const [pageSize, setPageSize] = useState(12);
  const [total, setTotal] = useState(0);

  const [formVisible, setFormVisible] = useState(false);
  const [editingWs, setEditingWs] = useState<WorkspaceListVO | null>(null);
  const [assignVisible, setAssignVisible] = useState(false);
  const [currentWs, setCurrentWs] = useState<WorkspaceListVO | null>(null);

  // keyword 300ms 防抖（与状态筛选 AND 叠加）
  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedKeyword(keyword.trim());
      setCurrent(1);
    }, 300);
    return () => clearTimeout(timer);
  }, [keyword]);

  const loadOverview = useCallback(async () => {
    try {
      const res = await getWorkspaceOverview();
      if (res.code === '000000') {
        setOverview(res.data);
      }
    } catch {
      // 异常已由全局请求层统一提示
    }
  }, []);

  const loadList = useCallback(async () => {
    setLoading(true);
    try {
      const res = await getWorkspacePage({
        currentPage: current,
        pageSize,
        keyword: debouncedKeyword || undefined,
        status: statusFilter,
      });
      if (res.code === '000000') {
        setList(res.data.data || []);
        setTotal(res.data.pageInfo?.total || 0);
      }
    } catch {
      // 异常已由全局请求层统一提示
    } finally {
      setLoading(false);
    }
  }, [current, pageSize, debouncedKeyword, statusFilter]);

  useEffect(() => {
    loadOverview();
  }, [loadOverview]);

  useEffect(() => {
    loadList();
  }, [loadList]);

  // 进入空间
  const handleEnter = (record: WorkspaceListVO) => {
    history.push(`/workspace/${record.code}/home`);
  };

  // 启用/停用空间
  const handleToggleStatus = (record: WorkspaceListVO, checked: boolean) => {
    const status = checked ? 'ENABLED' : 'DISABLED';
    Modal.confirm({
      title: `确认${checked ? '启用' : '停用'}空间「${record.name}」？`,
      content: checked
        ? '启用后空间成员可正常访问空间内资源。'
        : '停用后空间成员将无法访问空间内资源。',
      icon: <ExclamationCircleOutlined />,
      onOk: async () => {
        try {
          const res = await updateWorkspaceStatus(record.id, status);
          if (res.code === '000000') {
            message.success(`${checked ? '启用' : '停用'}成功`);
            loadList();
            loadOverview();
          }
        } catch {
          // 异常已由全局请求层统一提示
        }
      },
    });
  };

  const statusSwitch = (record: WorkspaceListVO) => (
    <Switch
      checked={record.status === 'ENABLED'}
      checkedChildren="启用"
      unCheckedChildren="停用"
      size="small"
      disabled={!access.hasPerm('platform:workspace:status')}
      onChange={(checked) => handleToggleStatus(record, checked)}
    />
  );

  const actionLinks = (record: WorkspaceListVO) => (
    <Space size="small">
      <a onClick={(e) => { e.stopPropagation(); handleEnter(record); }}>进入空间</a>
      {access.hasPerm('platform:workspace:assign') && (
        <a
          onClick={(e) => {
            e.stopPropagation();
            setCurrentWs(record);
            setAssignVisible(true);
          }}
        >
          分配管理
        </a>
      )}
      {access.hasPerm('platform:workspace:update') && (
        <a
          onClick={(e) => {
            e.stopPropagation();
            setEditingWs(record);
            setFormVisible(true);
          }}
        >
          编辑
        </a>
      )}
    </Space>
  );

  // 列表视图列定义
  const columns: ColumnsType<WorkspaceListVO> = [
    {
      title: '空间名称',
      dataIndex: 'name',
      width: 180,
      render: (text: string, record) => (
        <Space>
          <span className={styles.wsIcon} style={{ width: 28, height: 28, borderRadius: 6, fontSize: 12 }}>
            {text.charAt(0)}
          </span>
          <a onClick={() => handleEnter(record)}>{text}</a>
        </Space>
      ),
    },
    { title: '空间编码', dataIndex: 'code', width: 140 },
    { title: '空间管理员', dataIndex: 'ownerUserName', width: 110 },
    {
      title: '描述',
      dataIndex: 'description',
      ellipsis: true,
      render: (text: string) => text || '-',
    },
    { title: '成员数', dataIndex: 'memberCount', width: 80, align: 'center' },
    { title: '资产数', dataIndex: 'assetCount', width: 80, align: 'center' },
    {
      title: '状态',
      dataIndex: 'status',
      width: 100,
      render: (_, record) => statusSwitch(record),
    },
    {
      title: '创建时间',
      dataIndex: 'createdAt',
      width: 170,
    },
    {
      title: '操作',
      key: 'option',
      width: 220,
      fixed: 'right',
      render: (_, record) => actionLinks(record),
    },
  ];

  const toolbar = (
    <div className={styles.toolbar}>
      <Input.Search
        allowClear
        placeholder="搜索：名称 / 编码 / 描述…"
        style={{ width: 260 }}
        value={keyword}
        onChange={(e) => setKeyword(e.target.value)}
        onSearch={(v) => {
          setDebouncedKeyword(v.trim());
          setCurrent(1);
        }}
      />
      <Select
        allowClear
        placeholder="全部状态"
        style={{ width: 120 }}
        value={statusFilter}
        onChange={(v) => {
          setStatusFilter(v);
          setCurrent(1);
        }}
        options={[
          { label: '启用', value: 'ENABLED' },
          { label: '停用', value: 'DISABLED' },
        ]}
      />
      <div className={styles.toolbarRight}>
        <Segmented
          value={viewMode}
          onChange={(v) => setViewMode(v as ViewMode)}
          options={[
            { label: '卡片', value: 'card', icon: <AppstoreOutlined /> },
            { label: '列表', value: 'list', icon: <BarsOutlined /> },
          ]}
        />
        {access.hasPerm('platform:workspace:create') && (
          <Button
            type="primary"
            icon={<PlusOutlined />}
            onClick={() => {
              setEditingWs(null);
              setFormVisible(true);
            }}
          >
            创建空间
          </Button>
        )}
      </div>
    </div>
  );

  return (
    <div className={styles.page}>
      {/* 平台概览统计 */}
      <div className={styles.statGrid}>
        <Card size="small">
          <Statistic title="工作空间总数" value={overview?.totalSpaces ?? '-'} />
        </Card>
        <Card size="small">
          <Statistic title="空间成员总数" value={overview?.totalMembers ?? '-'} />
        </Card>
        <Card size="small">
          <Statistic title="本体资产总数" value={overview?.totalAssets ?? '-'} />
        </Card>
        <Card size="small">
          <Statistic
            title="活跃空间（30天）"
            value={
              overview ? `${overview.activeSpaces30d} / ${overview.totalSpaces}` : '-'
            }
          />
        </Card>
      </div>

      <Card size="small" title="空间列表">
        {toolbar}
        <Spin spinning={loading} style={{ marginTop: 16 }}>
          {viewMode === 'card' ? (
            <>
              {list.length ? (
                <div className={styles.cardGrid}>
                  {list.map((record) => (
                    <Tooltip key={record.id} title="点击进入空间">
                      <div
                        className={`${styles.wsCard} ${
                          record.status !== 'ENABLED' ? styles.off : ''
                        }`}
                        onClick={() => handleEnter(record)}
                      >
                        <div className={styles.wsCardTop}>
                          <span className={styles.wsIcon}>{record.name.charAt(0)}</span>
                          {statusSwitch(record)}
                        </div>
                        <div className={styles.wsName}>{record.name}</div>
                        <div className={styles.wsCode}>{record.code}</div>
                        <div className={styles.wsDesc}>{record.description || '暂无描述'}</div>
                        <div className={styles.wsMeta}>
                          <Space size={12}>
                            <span>管理员：{record.ownerUserName || '-'}</span>
                            <span>成员 {record.memberCount ?? 0}</span>
                            <span>资产 {record.assetCount ?? 0}</span>
                          </Space>
                        </div>
                        <div className={styles.wsFoot} onClick={(e) => e.stopPropagation()}>
                          {actionLinks(record)}
                        </div>
                      </div>
                    </Tooltip>
                  ))}
                </div>
              ) : (
                <Empty description="未找到匹配的工作空间" />
              )}
              <div className={styles.pagerRow}>
                <Pagination
                  current={current}
                  pageSize={pageSize}
                  total={total}
                  showSizeChanger
                  pageSizeOptions={[12, 24, 48]}
                  showTotal={(t) => `共 ${t} 个空间`}
                  onChange={(p, ps) => {
                    setCurrent(p);
                    setPageSize(ps);
                  }}
                />
              </div>
            </>
          ) : (
            <Table<WorkspaceListVO>
              rowKey="id"
              columns={columns}
              dataSource={list}
              scroll={{ x: 'max-content' }}
              pagination={{
                current,
                pageSize,
                total,
                showSizeChanger: true,
                pageSizeOptions: [10, 20, 50],
                showTotal: (t) => `共 ${t} 个空间`,
                onChange: (p, ps) => {
                  setCurrent(p);
                  setPageSize(ps);
                },
              }}
            />
          )}
        </Spin>
      </Card>

      {/* 创建/编辑空间弹窗 */}
      <WorkspaceForm
        visible={formVisible}
        initialValues={editingWs}
        onCancel={() => {
          setFormVisible(false);
          setEditingWs(null);
        }}
        onSuccess={() => {
          setFormVisible(false);
          setEditingWs(null);
          loadList();
          loadOverview();
        }}
      />

      {/* 分配管理弹窗 */}
      <AssignManageModal
        visible={assignVisible}
        workspace={currentWs}
        onCancel={() => {
          setAssignVisible(false);
          setCurrentWs(null);
        }}
        onSuccess={() => {
          loadList();
          loadOverview();
        }}
      />
    </div>
  );
}
