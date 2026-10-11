import { useEffect, useState } from 'react';
import { Modal, Tree, Radio, message, Spin, Divider, Typography } from 'antd';
import { getMenuTree } from '@/services/menu';
import type { MenuTreeVO } from '@/services/menu';
import { getRoleDetail, configurePermissions } from '@/services/role';
import type { RoleListVO } from '@/services/role';

const { Text } = Typography;

interface ConfigurePermissionsProps {
  visible: boolean;
  role: RoleListVO | null;
  onCancel: () => void;
  onSuccess: () => void;
}

export default function ConfigurePermissions({ visible, role, onCancel, onSuccess }: ConfigurePermissionsProps) {
  const [menuTree, setMenuTree] = useState<any[]>([]);
  const [checkedKeys, setCheckedKeys] = useState<string[]>([]);
  const [dataScope, setDataScope] = useState('ALL');
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (visible && role) {
      loadMenuTree();
      loadRolePermissions();
    }
  }, [visible, role]);

  const loadMenuTree = async () => {
    setLoading(true);
    try {
      const res = await getMenuTree();
      if (res.code === '000000') {
        setMenuTree(convertMenuTree(res.data || []));
      }
    } catch (error) {
      console.error('Failed to load menu tree:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadRolePermissions = async () => {
    if (!role) return;
    try {
      const res = await getRoleDetail(role.id);
      if (res.code === '000000') {
        setCheckedKeys(res.data.menuIds || []);
        setDataScope(res.data.dataScope || 'ALL');
      }
    } catch (error) {
      console.error('Failed to load role permissions:', error);
    }
  };

  const convertMenuTree = (data: MenuTreeVO[]): any[] => {
    return data.map((item) => ({
      key: item.id,
      title: item.name,
      children: item.children ? convertMenuTree(item.children) : [],
    }));
  };

  const handleOk = async () => {
    if (!role) return;
    setSubmitting(true);
    try {
      const res = await configurePermissions({
        roleId: role.id,
        menuIds: checkedKeys,
        dataScope,
      });
      if (res.code === '000000') {
        message.success('权限配置成功');
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
      title={`配置权限 - ${role?.name || ''}`}
      open={visible}
      onOk={handleOk}
      onCancel={onCancel}
      confirmLoading={submitting}
      destroyOnHidden
      width={640}
    >
      <Spin spinning={loading}>
        <div style={{ marginBottom: 16 }}>
          <Text strong>菜单权限</Text>
          <Divider style={{ margin: '8px 0' }} />
          <div style={{ maxHeight: 300, overflowY: 'auto', border: '1px solid #f0f0f0', padding: 8, borderRadius: 4 }}>
            <Tree
              checkable
              treeData={menuTree}
              checkedKeys={checkedKeys}
              onCheck={(keys: any) => {
                if (Array.isArray(keys)) {
                  setCheckedKeys(keys);
                } else {
                  setCheckedKeys(keys.checked);
                }
              }}
              defaultExpandAll
            />
          </div>
        </div>

        <div>
          <Text strong>空间数据权限</Text>
          <Divider style={{ margin: '8px 0' }} />
          <Radio.Group
            value={dataScope}
            onChange={(e) => setDataScope(e.target.value)}
            style={{ display: 'flex', flexDirection: 'column', gap: 8 }}
          >
            <Radio value="ALL">全部空间</Radio>
            <Radio value="DEPT_AND_CHILDREN">本部门及子部门</Radio>
            <Radio value="DEPT">本部门</Radio>
            <Radio value="SELF">仅自己创建</Radio>
            <Radio value="CUSTOM">自定义</Radio>
          </Radio.Group>
        </div>
      </Spin>
    </Modal>
  );
}
