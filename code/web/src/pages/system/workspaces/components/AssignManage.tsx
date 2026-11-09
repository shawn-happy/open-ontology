import { useCallback, useEffect, useMemo, useState } from 'react';
import {
  Button,
  Modal,
  Select,
  Space,
  Spin,
  Tabs,
  Tag,
  Transfer,
  message,
} from 'antd';
import type { TransferItem } from 'antd/es/transfer';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import {
  assignWorkspaceDepts,
  assignWorkspaceUsers,
  getMemberPage,
  getWorkspaceDetail,
  getWorkspaceRoles,
  removeWorkspaceDept,
  updateMemberRole,
} from '@/services/workspace';
import type {
  AssignResultVO,
  WorkspaceDeptVO,
  WorkspaceListVO,
  WorkspaceRoleVO,
} from '@/services/workspace';
import { getDeptTree } from '@/services/dept';
import type { DeptTreeVO } from '@/services/dept';
import { getUserPage } from '@/services/user';
import type { UserListVO } from '@/services/user';
import styles from '../index.less';

interface AssignManageModalProps {
  visible: boolean;
  workspace: WorkspaceListVO | null;
  onCancel: () => void;
  /** 分配成功后回调（刷新外层列表统计） */
  onSuccess: () => void;
}

/** 用户穿梭框数据项 */
interface UserTransferItem extends Omit<TransferItem, 'key'> {
  key: string;
  name: string;
  username: string;
  deptName: string;
  /** 是否已是空间成员（固定展示在右侧，不可移回） */
  inSpace: boolean;
}

/** 部门穿梭框数据项 */
interface DeptTransferItem extends Omit<TransferItem, 'key'> {
  key: string;
  title: string;
  depth: number;
  linked: boolean;
}

/** 分配结果提示 */
const showAssignResult = (action: string, results: AssignResultVO[]) => {
  const success = results.reduce((s, r) => s + r.successCount, 0);
  const skip = results.reduce((s, r) => s + r.skipCount, 0);
  message.success(
    `${action}成功 ${success} 个${skip ? `，跳过 ${skip} 个（已存在）` : ''}`,
  );
};

/**
 * 分配管理弹窗（用户 / 部门 两个维度，穿梭框交互）：
 * - 用户：左侧为待分配用户池（已分配用户不出现在左侧），右侧展示已分配成员
 *   （不可移回，可下拉调整其空间角色）；新选入成员可逐人指定空间角色；
 *   角色调整与新成员分配均暂存，点击「保存」后批量提交；
 * - 部门：左侧部门树平铺（已关联置灰），右侧已选部门可逐个指定默认角色；
 *   已关联部门列表支持移除。
 * 保存时按角色分组调用批量分配接口。
 */
export default function AssignManageModal({
  visible,
  workspace,
  onCancel,
  onSuccess,
}: AssignManageModalProps) {
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  const [roles, setRoles] = useState<WorkspaceRoleVO[]>([]);
  const [userItems, setUserItems] = useState<UserTransferItem[]>([]);
  const [deptItems, setDeptItems] = useState<DeptTransferItem[]>([]);
  /** 已关联部门 */
  const [linkedDepts, setLinkedDepts] = useState<WorkspaceDeptVO[]>([]);

  /** 穿梭框右侧已选用户 */
  const [targetUserIds, setTargetUserIds] = useState<string[]>([]);
  /** 已选用户的空间角色（逐人） */
  const [userRoles, setUserRoles] = useState<Record<string, string>>({});
  /** 已分配成员的当前空间角色（userId -> roleCode，含未保存的修改） */
  const [memberRoles, setMemberRoles] = useState<Record<string, string>>({});
  /** 已分配成员角色初始值（用于比对出待保存的变更） */
  const [initialMemberRoles, setInitialMemberRoles] = useState<
    Record<string, string>
  >({});
  /** 穿梭框右侧已选部门 */
  const [targetDeptIds, setTargetDeptIds] = useState<string[]>([]);
  /** 已选部门的默认角色（逐个） */
  const [deptRoles, setDeptRoles] = useState<Record<string, string>>({});

  const loadAll = useCallback(async () => {
    if (!workspace) {
      return;
    }
    setLoading(true);
    try {
      const [deptRes, userRes, memberRes, detailRes, roleRes] = await Promise.all([
        getDeptTree(),
        getUserPage({ currentPage: 1, pageSize: 1000, status: 'ENABLED' }),
        getMemberPage(workspace.code, {
          currentPage: 1,
          pageSize: 1000,
        }),
        getWorkspaceDetail(workspace.id),
        getWorkspaceRoles(workspace.code),
      ]);
      const members =
        memberRes.code === '000000' ? memberRes.data.data || [] : [];
      const inSpaceIds = new Set(members.map((m) => m.userId));
      // 已分配成员：固定在穿梭框右侧（disabled 不可移回）；左侧用户池仅含未分配用户
      const memberItems: UserTransferItem[] = members.map((m) => ({
        key: m.userId,
        name: m.name,
        username: m.username,
        deptName: m.deptName || '',
        inSpace: true,
        disabled: true,
      }));
      const roleMap = Object.fromEntries(
        members
          .filter((m) => m.workspaceRole)
          .map((m) => [m.userId, m.workspaceRole]),
      );
      setMemberRoles(roleMap);
      setInitialMemberRoles(roleMap);
      if (userRes.code === '000000') {
        const poolItems: UserTransferItem[] = (userRes.data.data || [])
          .filter((u: UserListVO) => !inSpaceIds.has(u.id))
          .map((u: UserListVO) => ({
            key: u.id,
            name: u.name,
            username: u.username,
            deptName: u.deptName || '',
            inSpace: false,
          }));
        setUserItems([...poolItems, ...memberItems]);
        setTargetUserIds(memberItems.map((m) => m.key));
      }
      if (deptRes.code === '000000') {
        const linked =
          detailRes.code === '000000' ? detailRes.data.depts || [] : [];
        const linkedIds = new Set(linked.map((d) => d.deptId));
        const items: DeptTransferItem[] = [];
        const walk = (nodes: DeptTreeVO[], depth: number) => {
          nodes.forEach((n) => {
            items.push({
              key: n.id,
              title: `${'　'.repeat(depth)}${n.name}`,
              depth,
              linked: linkedIds.has(n.id),
              disabled: linkedIds.has(n.id),
            });
            if (n.children?.length) {
              walk(n.children, depth + 1);
            }
          });
        };
        walk(deptRes.data || [], 0);
        setDeptItems(items);
        setLinkedDepts(linked);
      }
      if (roleRes.code === '000000') {
        setRoles(roleRes.data || []);
      }
    } catch {
      // 异常已由全局请求层统一提示
    } finally {
      setLoading(false);
    }
  }, [workspace]);

  useEffect(() => {
    if (visible && workspace) {
      setTargetUserIds([]);
      setUserRoles({});
      setMemberRoles({});
      setInitialMemberRoles({});
      setTargetDeptIds([]);
      setDeptRoles({});
      loadAll();
    }
  }, [visible, workspace, loadAll]);

  /** 角色下拉选项（OWNER 不可作为分配角色） */
  const roleOptions = useMemo(
    () =>
      roles
        .filter((r) => r.code !== 'OWNER')
        .map((r) => ({ label: r.displayName, value: r.code })),
    [roles],
  );

  const defaultRole = 'REPORTER';

  // 调整已分配成员的空间角色（仅暂存，点击「保存」后批量提交）
  const handleChangeMemberRole = (userId: string, role: string) => {
    setMemberRoles((prev) => ({ ...prev, [userId]: role }));
  };

  // 穿梭框移动时为新进入右侧的成员初始化默认角色
  const handleUserTransferChange = (nextTargetKeys: React.Key[]) => {
    const keys = nextTargetKeys as string[];
    setTargetUserIds(keys);
    setUserRoles((prev) => {
      const next = { ...prev };
      keys.forEach((k) => {
        if (!next[k]) {
          next[k] = defaultRole;
        }
      });
      return next;
    });
  };

  const handleDeptTransferChange = (nextTargetKeys: React.Key[]) => {
    const keys = nextTargetKeys as string[];
    setTargetDeptIds(keys);
    setDeptRoles((prev) => {
      const next = { ...prev };
      keys.forEach((k) => {
        if (!next[k]) {
          next[k] = defaultRole;
        }
      });
      return next;
    });
  };

  // 待保存内容：新加入右侧的用户 + 已分配成员中被修改过角色的变更项
  const newUserIds = useMemo(() => {
    const inSpaceIds = new Set(
      userItems.filter((u) => u.inSpace).map((u) => u.key),
    );
    return targetUserIds.filter((id) => !inSpaceIds.has(id));
  }, [targetUserIds, userItems]);

  const changedMemberRoles = useMemo(
    () =>
      Object.entries(memberRoles).filter(
        ([userId, role]) => initialMemberRoles[userId] !== role,
      ),
    [memberRoles, initialMemberRoles],
  );

  const userDirty = newUserIds.length > 0 || changedMemberRoles.length > 0;

  // 保存：新成员按角色分组批量分配 + 已分配成员的角色变更逐个提交
  const handleSaveUsers = async () => {
    if (!workspace || !userDirty) {
      message.warning('请先选择要分配的用户或调整成员角色');
      return;
    }
    setSubmitting(true);
    try {
      const results: AssignResultVO[] = [];
      if (newUserIds.length) {
        const groups = new Map<string, string[]>();
        newUserIds.forEach((id) => {
          const role = userRoles[id] || defaultRole;
          groups.set(role, [...(groups.get(role) || []), id]);
        });
        for (const [role, ids] of groups) {
          const res = await assignWorkspaceUsers(workspace.id, ids, role);
          if (res.code === '000000') {
            results.push(res.data);
          }
        }
      }
      let roleUpdated = 0;
      for (const [userId, role] of changedMemberRoles) {
        const res = await updateMemberRole(workspace.code, userId, role);
        if (res.code === '000000') {
          roleUpdated += 1;
        }
      }
      const success = results.reduce((s, r) => s + r.successCount, 0);
      const skip = results.reduce((s, r) => s + r.skipCount, 0);
      message.success(
        `保存成功：新分配 ${success} 人${skip ? `，跳过 ${skip} 人（已存在）` : ''}${
          roleUpdated ? `，更新角色 ${roleUpdated} 人` : ''
        }`,
      );
      setUserRoles({});
      await loadAll();
      onSuccess();
    } catch {
      // 异常已由全局请求层统一提示
    } finally {
      setSubmitting(false);
    }
  };

  // 保存：部门按默认角色分组批量关联
  const handleAssignDepts = async () => {
    if (!workspace || !targetDeptIds.length) {
      message.warning('请先在穿梭框右侧选择要关联的部门');
      return;
    }
    setSubmitting(true);
    try {
      const groups = new Map<string, string[]>();
      targetDeptIds.forEach((id) => {
        const role = deptRoles[id] || defaultRole;
        groups.set(role, [...(groups.get(role) || []), id]);
      });
      const results: AssignResultVO[] = [];
      for (const [role, ids] of groups) {
        const res = await assignWorkspaceDepts(workspace.id, ids, role);
        if (res.code === '000000') {
          results.push(res.data);
        }
      }
      showAssignResult('关联', results);
      setTargetDeptIds([]);
      setDeptRoles({});
      await loadAll();
      onSuccess();
    } catch {
      // 异常已由全局请求层统一提示
    } finally {
      setSubmitting(false);
    }
  };

  // 移除关联部门
  const handleRemoveDept = (dept: WorkspaceDeptVO) => {
    if (!workspace) {
      return;
    }
    Modal.confirm({
      title: `确认移除部门「${dept.deptName}」？`,
      content: '移除后该部门成员将失去通过部门分配获得的空间访问权限。',
      icon: <ExclamationCircleOutlined />,
      okButtonProps: { danger: true },
      onOk: async () => {
        try {
          const res = await removeWorkspaceDept(workspace.id, dept.deptId);
          if (res.code === '000000') {
            message.success(`已移除部门：${dept.deptName}`);
            await loadAll();
            onSuccess();
          }
        } catch {
          // 异常已由全局请求层统一提示
        }
      },
    });
  };

  const memberCount = userItems.filter((u) => u.inSpace).length;

  const userTab = (
    <Spin spinning={loading}>
      <Transfer<UserTransferItem>
        dataSource={userItems}
        targetKeys={targetUserIds}
        onChange={handleUserTransferChange}
        showSearch
        filterOption={(input, item) =>
          item.name.includes(input) ||
          item.username.includes(input) ||
          item.deptName.includes(input)
        }
        titles={['待分配用户', `已分配用户（${targetUserIds.length}）`]}
        listStyle={{ width: 340, height: 400 }}
        render={(item) =>
          item.inSpace ? (
            <Space size={4} onClick={(e) => e.stopPropagation()}>
              <span>
                {item.name}（{item.username}）
              </span>
              {roleOptions.some(
                (o) => o.value === (memberRoles[item.key] || defaultRole),
              ) ? (
                <Select
                  size="small"
                  style={{ width: 120 }}
                  value={memberRoles[item.key] || defaultRole}
                  options={roleOptions}
                  onChange={(v) => handleChangeMemberRole(item.key, v)}
                />
              ) : (
                <Tag color="gold">
                  {roles.find((r) => r.code === memberRoles[item.key])
                    ?.displayName || '空间管理员'}
                </Tag>
              )}
            </Space>
          ) : targetUserIds.includes(item.key) ? (
            <Space size={4} onClick={(e) => e.stopPropagation()}>
              <span>
                {item.name}（{item.username}）
              </span>
              <Select
                size="small"
                style={{ width: 120 }}
                value={userRoles[item.key] || defaultRole}
                options={roleOptions}
                onChange={(v) =>
                  setUserRoles((prev) => ({ ...prev, [item.key]: v }))
                }
              />
            </Space>
          ) : (
            <Space size={4}>
              <span>
                {item.name}（{item.username}）
              </span>
              {item.deptName && <Tag>{item.deptName}</Tag>}
            </Space>
          )
        }
      />
      <div
        style={{
          marginTop: 12,
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
        }}
      >
        <span style={{ color: 'rgba(0,0,0,0.45)', fontSize: 12 }}>
          从左侧选入新成员并逐人指定角色，或直接下拉调整已分配成员的角色；点击「保存」后批量生效
        </span>
        <Button
          type="primary"
          loading={submitting}
          disabled={!userDirty}
          onClick={handleSaveUsers}
        >
          保存
        </Button>
      </div>
    </Spin>
  );

  const deptTab = (
    <Spin spinning={loading}>
      <div className={styles.assignGroup}>已关联部门（{linkedDepts.length}）</div>
      {linkedDepts.length ? (
        linkedDepts.map((d) => (
          <div key={d.deptId} className={styles.deptLinkedItem}>
            <Space>
              <span>{d.deptName}</span>
              {d.defaultRole && <Tag color="blue">默认角色：{d.defaultRole}</Tag>}
              {d.assignedByName && (
                <span style={{ color: 'rgba(0,0,0,0.45)', fontSize: 12 }}>
                  由 {d.assignedByName} 分配
                </span>
              )}
            </Space>
            <a style={{ color: '#ff4d4f' }} onClick={() => handleRemoveDept(d)}>
              移除
            </a>
          </div>
        ))
      ) : (
        <div className={styles.emptyTip}>暂无关联部门</div>
      )}

      <div className={styles.assignGroup} style={{ marginTop: 12 }}>
        关联新部门（部门及其子部门成员将以指定默认角色加入空间）
      </div>
      <Transfer<DeptTransferItem>
        dataSource={deptItems}
        targetKeys={targetDeptIds}
        onChange={handleDeptTransferChange}
        showSearch
        filterOption={(input, item) => item.title.includes(input)}
        titles={['部门树', `已选部门（${targetDeptIds.length}）`]}
        listStyle={{ width: 340, height: 360 }}
        render={(item) =>
          targetDeptIds.includes(item.key) ? (
            <Space size={4} onClick={(e) => e.stopPropagation()}>
              <span>{item.title}</span>
              <Select
                size="small"
                style={{ width: 120 }}
                value={deptRoles[item.key] || defaultRole}
                options={roleOptions}
                onChange={(v) =>
                  setDeptRoles((prev) => ({ ...prev, [item.key]: v }))
                }
              />
            </Space>
          ) : (
            <span>{item.title}</span>
          )
        }
      />
      <div
        style={{
          marginTop: 12,
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
        }}
      >
        <span style={{ color: 'rgba(0,0,0,0.45)', fontSize: 12 }}>
          右侧可为每个部门指定成员默认角色，部门成员将以该角色加入空间
        </span>
        <Button
          type="primary"
          loading={submitting}
          disabled={!targetDeptIds.length}
          onClick={handleAssignDepts}
        >
          关联所选部门
        </Button>
      </div>
    </Spin>
  );

  return (
    <Modal
      title={`分配管理 · ${workspace?.name || ''}`}
      open={visible}
      onCancel={onCancel}
      footer={null}
      width={820}
      destroyOnHidden
    >
      <Tabs
        items={[
          {
            key: 'user',
            label: `用户（${memberCount}）`,
            children: userTab,
          },
          {
            key: 'dept',
            label: `部门（${linkedDepts.length}）`,
            children: deptTab,
          },
        ]}
      />
    </Modal>
  );
}
