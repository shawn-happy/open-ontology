import { useEffect, useState } from 'react';
import { Modal, Tree, message, Spin } from 'antd';
import { getDeptTree } from '@/services/dept';
import type { DeptTreeVO } from '@/services/dept';
import { assignDept } from '@/services/user';
import type { UserListVO } from '@/services/user';

interface AssignDeptModalProps {
  visible: boolean;
  user: UserListVO | null;
  onCancel: () => void;
  onSuccess: () => void;
}

export default function AssignDeptModal({ visible, user, onCancel, onSuccess }: AssignDeptModalProps) {
  const [deptTree, setDeptTree] = useState<any[]>([]);
  const [selectedDeptId, setSelectedDeptId] = useState<string | undefined>();
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (visible && user) {
      loadDeptTree();
      setSelectedDeptId(user.deptId);
    }
  }, [visible, user]);

  const loadDeptTree = async () => {
    setLoading(true);
    try {
      const res = await getDeptTree();
      if (res.code === '000000') {
        setDeptTree(convertDeptTree(res.data || []));
      }
    } catch (error) {
      console.error('Failed to load dept tree:', error);
    } finally {
      setLoading(false);
    }
  };

  const convertDeptTree = (data: DeptTreeVO[]): any[] => {
    return data.map((item) => ({
      key: item.id,
      title: item.name,
      children: item.children ? convertDeptTree(item.children) : [],
    }));
  };

  const handleOk = async () => {
    if (!user) return;
    if (!selectedDeptId) {
      message.warning('请选择部门');
      return;
    }
    setSubmitting(true);
    try {
      const res = await assignDept(user.id, selectedDeptId);
      if (res.code === '000000') {
        message.success('部门分配成功');
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

  return (
    <Modal
      title={`分配部门 - ${user?.name || ''}`}
      open={visible}
      onOk={handleOk}
      onCancel={onCancel}
      confirmLoading={submitting}
      destroyOnHidden
      width={400}
    >
      <Spin spinning={loading}>
        <div style={{ maxHeight: 400, overflowY: 'auto' }}>
          <Tree
            treeData={deptTree}
            selectedKeys={selectedDeptId ? [selectedDeptId] : []}
            onSelect={(keys) => {
              if (keys.length > 0) {
                setSelectedDeptId(keys[0] as string);
              }
            }}
            defaultExpandAll
            showLine
            blockNode
          />
          {deptTree.length === 0 && !loading && (
            <div style={{ textAlign: 'center', padding: 40, color: '#999' }}>暂无部门</div>
          )}
        </div>
      </Spin>
    </Modal>
  );
}
