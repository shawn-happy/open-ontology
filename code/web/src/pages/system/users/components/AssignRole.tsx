import { useEffect, useState } from 'react';
import { Modal, Checkbox, Tag, Spin, Button, Space, message } from 'antd';
import { getAllRoles } from '@/services/role';
import type { RoleListVO } from '@/services/role';
import { assignRoles } from '@/services/user';
import type { UserListVO } from '@/services/user';

interface AssignRoleModalProps {
  visible: boolean;
  user: UserListVO | null;
  onCancel: () => void;
  onSuccess: () => void;
}

export default function AssignRoleModal({ visible, user, onCancel, onSuccess }: AssignRoleModalProps) {
  const [allRoles, setAllRoles] = useState<RoleListVO[]>([]);
  const [selectedRoleIds, setSelectedRoleIds] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (visible && user) {
      loadRoles();
      setSelectedRoleIds(user.roleIds || []);
    }
  }, [visible, user]);

  const loadRoles = async () => {
    setLoading(true);
    try {
      const res = await getAllRoles();
      if (res.code === '000000') {
        setAllRoles(res.data || []);
      }
    } catch (error) {
      console.error('Failed to load roles:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleOk = async () => {
    if (!user) return;
    setSubmitting(true);
    try {
      const res = await assignRoles(user.id, selectedRoleIds);
      if (res.code === '000000') {
        message.success('角色分配成功');
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

  const handleToggle = (roleId: string, checked: boolean) => {
    if (checked) {
      setSelectedRoleIds([...selectedRoleIds, roleId]);
    } else {
      setSelectedRoleIds(selectedRoleIds.filter((id) => id !== roleId));
    }
  };

  return (
    <Modal
      title={`分配角色 - ${user?.name || ''}`}
      open={visible}
      onOk={handleOk}
      onCancel={onCancel}
      confirmLoading={submitting}
      destroyOnHidden
      width={420}
    >
      <Spin spinning={loading}>
        <div style={{ maxHeight: 400, overflowY: 'auto' }}>
          {allRoles.map((role) => {
            const isSuperAdmin = role.code === 'super_admin';
            const isAdminUser = user?.username === 'admin';
            const disabled = isSuperAdmin && !isAdminUser;
            const checked = selectedRoleIds.includes(role.id);

            return (
              <div
                key={role.id}
                style={{
                  padding: '8px 4px',
                  borderBottom: '1px solid #f0f0f0',
                  cursor: disabled ? 'not-allowed' : 'pointer',
                  display: 'flex',
                  alignItems: 'center',
                  gap: 8,
                }}
                onClick={() => {
                  if (!disabled) handleToggle(role.id, !checked);
                }}
              >
                <Checkbox
                  checked={checked}
                  disabled={disabled}
                  onClick={(e) => e.stopPropagation()}
                  onChange={(e) => handleToggle(role.id, e.target.checked)}
                />
                <span style={{ flex: 1 }}>{role.name}</span>
                {role.builtIn && <Tag color="orange">内置</Tag>}
                {role.type === 'WORKSPACE' && <Tag color="green">空间</Tag>}
                {isSuperAdmin && !isAdminUser && (
                  <span style={{ color: '#999', fontSize: 12 }}>仅 admin 可分配</span>
                )}
              </div>
            );
          })}
          {allRoles.length === 0 && !loading && (
            <div style={{ textAlign: 'center', padding: 40, color: '#999' }}>暂无角色</div>
          )}
        </div>
      </Spin>
    </Modal>
  );
}
