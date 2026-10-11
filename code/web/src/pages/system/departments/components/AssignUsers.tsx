import { useEffect, useState } from 'react';
import { Modal, Checkbox, message, Spin, Tag } from 'antd';
import { getUserPage } from '@/services/user';
import type { UserListVO } from '@/services/user';
import { assignUsersToDept } from '@/services/dept';
import type { DeptTreeVO } from '@/services/dept';

interface AssignUsersModalProps {
  visible: boolean;
  dept: DeptTreeVO | null;
  onCancel: () => void;
  onSuccess: () => void;
}

export default function AssignUsersModal({ visible, dept, onCancel, onSuccess }: AssignUsersModalProps) {
  const [allUsers, setAllUsers] = useState<UserListVO[]>([]);
  const [selectedUserIds, setSelectedUserIds] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (visible && dept) {
      loadUsers();
    }
  }, [visible, dept]);

  const loadUsers = async () => {
    setLoading(true);
    try {
      const res = await getUserPage({ currentPage: 1, pageSize: 1000 });
      if (res.code === '000000') {
        const users = res.data.data || [];
        setAllUsers(users);
        // 找出该部门下的用户
        const assigned = users
          .filter((u) => u.deptId === dept!.id)
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
    if (!dept) return;
    setSubmitting(true);
    try {
      const res = await assignUsersToDept(dept.id, selectedUserIds);
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

  return (
    <Modal
      title={`分配用户 - ${dept?.name || ''}`}
      open={visible}
      onOk={handleOk}
      onCancel={onCancel}
      confirmLoading={submitting}
      destroyOnHidden
      width={480}
    >
      <Spin spinning={loading}>
        <div style={{ maxHeight: 400, overflowY: 'auto' }}>
          {allUsers.map((user) => (
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
              />
              <span>{user.name}</span>
              <span style={{ color: '#999', fontSize: 12 }}>({user.username})</span>
              {user.deptName && <Tag>{user.deptName}</Tag>}
            </div>
          ))}
          {allUsers.length === 0 && !loading && (
            <div style={{ textAlign: 'center', padding: 40, color: '#999' }}>暂无用户</div>
          )}
        </div>
      </Spin>
    </Modal>
  );
}
