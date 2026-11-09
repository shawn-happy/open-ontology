import { useEffect, useState } from 'react';
import { Modal, Radio, Space, message } from 'antd';
import type { MemberListVO, WorkspaceRoleVO } from '@/services/workspace';
import { updateMemberRole } from '@/services/workspace';
import RoleBadge from './RoleBadge';
import styles from '../index.less';

interface AssignRoleModalProps {
  visible: boolean;
  workspaceCode: string;
  member: MemberListVO | null;
  /** 空间角色全集（内置 + 自定义），由列表页统一加载 */
  roles: WorkspaceRoleVO[];
  /** 当前登录人在本空间的角色码 */
  myRole: string | null;
  /** 当前登录人角色等级 */
  myRoleLevel: number;
  onCancel: () => void;
  onSuccess: () => void;
}

/**
 * 分配空间角色弹窗（Modal + Radio 纵排，每行徽标 + 权限说明）
 *
 * 可选项按 FR-22 过滤：仅可授予等级低于自己的角色；Owner 选项仅 Owner 可见。
 * 部门来源成员保存后写入个人角色（覆盖部门默认 Reporter）；直接分配成员立即改写角色。
 */
export default function AssignRoleModal({
  visible,
  workspaceCode,
  member,
  roles,
  myRole,
  myRoleLevel,
  onCancel,
  onSuccess,
}: AssignRoleModalProps) {
  const [selected, setSelected] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  // 打开时默认选中当前角色
  useEffect(() => {
    if (visible && member) {
      setSelected(member.workspaceRole || null);
    }
  }, [visible, member]);

  if (!member) {
    return null;
  }

  // FR-22：可授角色等级必须低于操作人；OWNER（等级 5）仅 Owner 本人可授
  const assignableRoles = roles.filter(
    (r) => r.level < myRoleLevel || (r.code === 'OWNER' && myRole === 'OWNER'),
  );

  const handleOk = async () => {
    if (!selected) {
      message.warning('请选择一个空间角色');
      return;
    }
    setSubmitting(true);
    try {
      const res = await updateMemberRole(workspaceCode, member.userId, selected);
      if (res.code === '000000') {
        const roleName =
          roles.find((r) => r.code === selected)?.displayName || selected;
        if (member.source === 'DEPT') {
          message.success(`已分配个人角色 ${roleName}（覆盖部门默认）`);
        } else {
          message.success(`已调整角色为 ${roleName}`);
        }
        onSuccess();
      }
    } catch {
      // 异常已由全局请求层统一提示（020203/020206/020207/020208 等），弹窗保留
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Modal
      title={
        <span>
          分配空间角色 · <span className={styles.modalUser}>{member.name}（{member.username}）</span>
        </span>
      }
      open={visible}
      onOk={handleOk}
      onCancel={onCancel}
      confirmLoading={submitting}
      okText="确定"
      cancelText="取消"
      maskClosable={false}
      destroyOnClose
    >
      <div className={styles.modalTip}>
        {member.source === 'DEPT'
          ? '该成员由部门分配引入，默认 Reporter；分配角色后即为个人角色，覆盖部门默认。'
          : '为该成员选择空间角色，保存后立即生效。'}
      </div>
      <Radio.Group
        value={selected}
        onChange={(e) => setSelected(e.target.value)}
        className={styles.roleRadioGroup}
      >
        <Space direction="vertical" style={{ width: '100%' }}>
          {assignableRoles.map((r) => (
            <Radio key={r.code} value={r.code} className={styles.roleRadioItem}>
              <RoleBadge role={r.code} displayName={r.displayName} style={{ marginInline: '8px 8px' }} />
              <span className={styles.roleDesc}>{r.description}</span>
            </Radio>
          ))}
        </Space>
      </Radio.Group>
    </Modal>
  );
}
