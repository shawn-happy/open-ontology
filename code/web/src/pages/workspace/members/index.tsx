import { useCallback, useEffect, useRef, useState } from 'react';
import { history, useModel } from '@umijs/max';
import {
  Button,
  Card,
  Col,
  Empty,
  Input,
  Modal,
  Result,
  Row,
  Select,
  Space,
  Statistic,
  Tag,
  message,
} from 'antd';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import { ProTable } from '@ant-design/pro-components';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import type {
  MemberListVO,
  MemberStatsVO,
  WorkspaceRoleVO,
} from '@/services/workspace';
import {
  getMemberPage,
  getMemberStats,
  getWorkspaceRoles,
  removeMember,
  updateMemberRole,
} from '@/services/workspace';
import RoleBadge from './components/RoleBadge';
import AssignRoleModal from './components/AssignRoleModal';
import styles from './index.less';

/** 角色筛选 Select 宽度 */
const ROLE_SELECT_WIDTH = 160;

/**
 * 空间成员分配记录页（成员维度）：
 * 直接分配逐条记录 + 部门分配动态展开去重（直接分配优先），支持搜索/角色筛选、
 * 分配角色（个人角色覆盖）与移除三分支（PRD 流程 C）。
 */
export default function WorkspaceMembersPage() {
  const { workspaceCode, myRole, myRoleLevel, initialized } = useModel('workspace');
  const actionRef = useRef<ActionType>();

  const [stats, setStats] = useState<MemberStatsVO | null>(null);
  const [roles, setRoles] = useState<WorkspaceRoleVO[]>([]);
  const [keyword, setKeyword] = useState('');
  const [debouncedKeyword, setDebouncedKeyword] = useState('');
  const [roleFilter, setRoleFilter] = useState<string | undefined>();
  const [forbidden, setForbidden] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [currentMember, setCurrentMember] = useState<MemberListVO | null>(null);

  // keyword 300ms 防抖即时过滤（与角色筛选 AND 叠加）
  useEffect(() => {
    const timer = setTimeout(() => setDebouncedKeyword(keyword.trim()), 300);
    return () => clearTimeout(timer);
  }, [keyword]);

  const loadStats = useCallback(async () => {
    if (!workspaceCode) {
      return;
    }
    try {
      const res = await getMemberStats(workspaceCode);
      if (res.code === '000000') {
        setStats(res.data);
      }
    } catch {
      // 403 由页面级 forbidden 判定统一处理；其余异常已由全局请求层提示
    }
  }, [workspaceCode]);

  const loadRoles = useCallback(async () => {
    if (!workspaceCode) {
      return;
    }
    try {
      const res = await getWorkspaceRoles(workspaceCode);
      if (res.code === '000000') {
        setRoles(res.data || []);
      }
    } catch {
      // 同上
    }
  }, [workspaceCode]);

  useEffect(() => {
    if (!initialized || !workspaceCode) {
      return;
    }
    // 已知角色不足 Developer → 直接页面级 403，不发请求；
    // myRole 为空（可能是超管/dataScope 放行）→ 交给后端判定，020403 时再置 forbidden
    if (myRoleLevel > 0 && myRoleLevel < 3) {
      setForbidden(true);
      return;
    }
    loadStats();
    loadRoles();
  }, [initialized, workspaceCode, myRoleLevel, loadStats, loadRoles]);

  // 移除成员三分支（PRD 流程 C / §4.5 文案）
  const handleRemove = (record: MemberListVO) => {
    if (record.source === 'DIRECT') {
      Modal.confirm({
        title: `确认将成员「${record.name}」移出空间？`,
        icon: <ExclamationCircleOutlined />,
        okText: '确认移除',
        cancelText: '取消',
        maskClosable: false,
        okButtonProps: { danger: true },
        onOk: async () => {
          try {
            const res = await removeMember(workspaceCode, record.userId);
            if (res.code === '000000') {
              message.success(`已移除成员：${record.name}`);
              actionRef.current?.reload();
              loadStats();
            }
          } catch {
            // 异常已由全局请求层统一提示（020203 唯一 Owner 等）
          }
        },
      });
      return;
    }
    if (record.source === 'DEPT' && record.isPersonalRole) {
      Modal.confirm({
        title: `清除「${record.name}」的个人角色？清除后恢复部门默认 Reporter。`,
        icon: <ExclamationCircleOutlined />,
        okText: '确认清除',
        cancelText: '取消',
        maskClosable: false,
        onOk: async () => {
          try {
            const res = await updateMemberRole(workspaceCode, record.userId, null);
            if (res.code === '000000') {
              message.success('已清除个人角色，恢复部门默认 Reporter');
              actionRef.current?.reload();
              loadStats();
            }
          } catch {
            // 异常已由全局请求层统一提示
          }
        },
      });
      return;
    }
    // 部门分配未覆盖角色：不调接口，仅引导（后端 020209 为兜底）
    message.info(
      `该成员由部门分配引入，请在「工作空间 → 分配成员 → 部门」中移除部门 ${record.sourceDeptName || ''}`,
    );
  };

  const columns: ProColumns<MemberListVO>[] = [
    { title: '用户账号', dataIndex: 'username', width: 130 },
    { title: '用户名字', dataIndex: 'name', width: 100 },
    { title: '部门', dataIndex: 'deptName', width: 140 },
    {
      title: '空间角色',
      dataIndex: 'workspaceRole',
      width: 180,
      render: (_, record) => (
        <Space size={4}>
          <RoleBadge role={record.workspaceRole} displayName={record.roleDisplayName} />
          {record.isPersonalRole && <span className={styles.roleExtra}>个人角色</span>}
          {!record.isPersonalRole && record.source === 'DEPT' && (
            <span className={styles.roleExtra}>部门默认</span>
          )}
        </Space>
      ),
    },
    { title: '分配人', dataIndex: 'assignedByName', width: 110 },
    { title: '分配时间', dataIndex: 'assignedAt', width: 170, valueType: 'dateTime' },
    {
      title: '来源',
      dataIndex: 'source',
      width: 180,
      render: (_, record) =>
        record.source === 'DIRECT' ? (
          <Tag>直接分配</Tag>
        ) : (
          <Tag>部门分配{record.sourceDeptName ? ` · ${record.sourceDeptName}` : ''}</Tag>
        ),
    },
  ];

  // 操作列：仅 Maintainer 及以上可见；行级按 FR-22（目标等级低于自己才可操作）
  if (myRoleLevel >= 4) {
    columns.push({
      title: '操作',
      valueType: 'option',
      width: 150,
      fixed: 'right',
      render: (_, record) => {
        if (record.roleLevel >= myRoleLevel) {
          return null;
        }
        return (
          <Space size="small">
            <a
              onClick={() => {
                setCurrentMember(record);
                setModalVisible(true);
              }}
            >
              分配角色
            </a>
            <a style={{ color: '#ff4d4f' }} onClick={() => handleRemove(record)}>
              移除
            </a>
          </Space>
        );
      },
    });
  }

  // 页面级 403（PRD §4.5：需要 Developer 及以上空间角色）
  if (forbidden) {
    return (
      <Result
        status="403"
        title="无权访问"
        subTitle="无权访问：需要 Developer 及以上空间角色"
        extra={
          <Button type="primary" onClick={() => history.push(`/workspace/${workspaceCode}/home`)}>
            返回空间首页
          </Button>
        }
      />
    );
  }

  return (
    <div className={styles.membersPage}>
      {/* 默认角色规则提示（PRD FR-19/23） */}
      <div className={styles.tip}>
        默认角色规则：空间<b>创建人 / 负责人</b>若为部门 leader，或系统角色为
        <b>超级管理员 / 空间管理员</b>，默认授予 <RoleBadge role="OWNER" displayName="Owner" />
        ；否则默认 <RoleBadge role="MAINTAINER" displayName="Maintainer" />
        。其余被拉入的成员默认 <RoleBadge role="REPORTER" displayName="Reporter" />
        ，部门分配引入的成员同样默认 Reporter，可在此调整个人角色。
      </div>

      {/* 统计卡片 ×4 */}
      <Row gutter={16} className={styles.statRow}>
        <Col span={6}>
          <Card size="small">
            <Statistic title="空间成员总数" value={stats?.totalMembers ?? '—'} />
          </Card>
        </Col>
        <Col span={6}>
          <Card size="small">
            <Statistic title="直接分配成员" value={stats?.directCount ?? '—'} />
          </Card>
        </Col>
        <Col span={6}>
          <Card size="small">
            <Statistic title="部门分配" value={stats?.deptCount ?? '—'} />
          </Card>
        </Col>
        <Col span={6}>
          <Card size="small">
            <Statistic title="Owner 数量" value={stats?.ownerCount ?? '—'} />
          </Card>
        </Col>
      </Row>

      <ProTable<MemberListVO>
        headerTitle="成员维度分配记录"
        actionRef={actionRef}
        rowKey="userId"
        columns={columns}
        scroll={{ x: 'max-content' }}
        search={false}
        params={{ debouncedKeyword, roleFilter }}
        request={async (params) => {
          if (!workspaceCode) {
            return { data: [], total: 0, success: true };
          }
          try {
            const res = await getMemberPage(workspaceCode, {
              currentPage: params.current,
              pageSize: params.pageSize,
              keyword: params.debouncedKeyword || undefined,
              workspaceRole: params.roleFilter || undefined,
            });
            if (res.code === '000000') {
              setForbidden(false);
              return {
                data: res.data.data || [],
                total: res.data.pageInfo?.total || 0,
                success: true,
              };
            }
          } catch (error: any) {
            // 空间角色不足（020403）→ 页面级 403
            if (error?.info?.code === '020403') {
              setForbidden(true);
            }
          }
          return { data: [], total: 0, success: false };
        }}
        toolBarRender={() => [
          <Input.Search
            key="keyword"
            placeholder="搜索：账号 / 姓名 / 部门…"
            allowClear
            style={{ width: 240 }}
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            onSearch={(v) => setDebouncedKeyword(v.trim())}
          />,
          <Select
            key="role"
            placeholder="全部角色"
            allowClear
            style={{ width: ROLE_SELECT_WIDTH }}
            value={roleFilter}
            onChange={(v) => setRoleFilter(v)}
            options={roles.map((r) => ({ value: r.code, label: r.displayName }))}
          />,
        ]}
        locale={{
          emptyText: <Empty description="未找到匹配的分配记录" />,
        }}
        pagination={{ defaultPageSize: 20, showSizeChanger: true }}
        footer={() => (
          <span className={styles.tableFooterTip}>
            部门分配来源的成员为动态展开：部门及其子部门下的成员变化时，记录自动同步；「个人角色」标记表示已单独分配角色并覆盖部门默认。
          </span>
        )}
      />

      {/* 分配角色弹窗 */}
      <AssignRoleModal
        visible={modalVisible}
        workspaceCode={workspaceCode}
        member={currentMember}
        roles={roles}
        myRole={myRole}
        myRoleLevel={myRoleLevel}
        onCancel={() => {
          setModalVisible(false);
          setCurrentMember(null);
        }}
        onSuccess={() => {
          setModalVisible(false);
          setCurrentMember(null);
          actionRef.current?.reload();
          loadStats();
        }}
      />
    </div>
  );
}
