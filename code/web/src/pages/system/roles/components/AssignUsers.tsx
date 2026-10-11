import { useEffect, useState } from 'react';
import { Modal, Checkbox, message, Spin, Tag } from 'antd';
import { getUserPage } from '@/services/user';
import type { UserListVO } from '@/services/user';
import { assignUsersToRole } from '@/services/role';
import type { RoleListVO } from '@/services/role';

interface AssignUsersProps {
  visible: boolean;
  role: RoleListVO | null;
  onCancel: () => void;
  onSuccess: () => void;
}

export default function AssignUsers({ visible, role, onCancel, onSuccess }: AssignUsersProps) {
  const [allUsers, setAllUsers] = useState<UserListVO[]>([]);
  const [selectedUserIds, setSelectedUserIds] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (visible && role) {
      loadUsers();
    }
  }, [visible, role]);

  const loadUsers = async () => {
    setLoading(true);
    try {
      const res = await getUserPage({ currentPage: 1, pageSize: 1000 });
      if (res.code === '000000') {
        const users = res.data.data || [];
        setAllUsers(users);
        // 找出已有该角色的用户
        const assigned = users
          .filter((u) => u.roleIds?.includes(role!.id))
          .map((u) => u.id);
        setSelectedUserIds(assigned);
      }
    } catch (error) {
      console.error('Failed to load users:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleOk = async () => {
    if (!role) return;
    setSubmitting(true);
    try {
      const res = await assignUsersToRole(role.id, selectedUserIds);
      if (res.code === '000000') {
        message.success('用户分配成功');
        onSuccess();
      } else {
        message.error(res.msg);
      }
    } catch (error: any) {
      message.error(error?.message || '操作失败');
    } finally {
      setSubmitting(false);
    }
  };

  const handleUserChange = (userId: string, checked: boolean) => {
    if (checked) {
      setSelectedUserIds([...selectedUserIds, userId]);
    } else {
      setSelectedUserIds(selectedUserIds.filter((id) => id !== userId));
    }
  };

  const isSuperAdmin = role?.code === 'super_admin';

  return (
    <Modal
      title={`分配用户 - ${role?.name || ''}`}
      open={visible}
      onOk={handleOk}
      onCancel={onCancel}
      confirmLoading={submitting}
      destroyOnHidden
      width={480}
    >
      {isSuperAdmin && (
        <div style={{ marginBottom: 12, color: '#faad14' }}>
          超级管理员角色仅可分配给 admin 用户
        </div>
      )}
      <Spin spinning={loading}>
        <div style={{ maxHeight: 400, overflowY: 'auto' }}>
          {allUsers.map((user) => {
            const disabled = isSuperAdmin && user.username !== 'admin';
            return (
              <div
                key={user.id}
                style={{
                  padding: '8px 0',
                  borderBottom: '1px solid #f0f0f0',
                  display: 'flex',
                  alignItems: 'center',
                  gap: 8,
                }}
              >
                <Checkbox
                  checked={selectedUserIds.includes(user.id)}
                  onChange={(e) => handleUserChange(user.id, e.target.checked)}
                  disabled={disabled}
                />
                <span>{user.name}</span>
                <span style={{ color: '#999', fontSize: 12 }}>({user.username})</span>
                {user.deptName && <Tag>{user.deptName}</Tag>}
              </div>
            );
          })}
          {allUsers.length === 0 && !loading && (
            <div style={{ textAlign: 'center', padding: 40, color: '#999' }}>暂无用户</div>
          )}
        </div>
      </Spin>
    </Modal>
  );
}
