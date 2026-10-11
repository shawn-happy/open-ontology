import { useEffect, useState } from 'react';
import { ModalForm, ProFormText, ProFormSelect, ProFormRadio } from '@ant-design/pro-components';
import { Form, message, TreeSelect } from 'antd';
import { createUser, updateUser } from '@/services/user';
import type { UserListVO } from '@/services/user';
import { getDeptTree } from '@/services/dept';
import type { DeptTreeVO } from '@/services/dept';

interface UserFormProps {
  visible: boolean;
  initialValues: UserListVO | null;
  onCancel: () => void;
  onSuccess: () => void;
}

export default function UserForm({ visible, initialValues, onCancel, onSuccess }: UserFormProps) {
  const [form] = Form.useForm();
  const [deptTreeData, setDeptTreeData] = useState<any[]>([]);
  const isEdit = !!initialValues;

  const convertToTreeData = (data: DeptTreeVO[]): any[] => {
    return data.map((item) => ({
      title: item.name,
      value: String(item.id),
      children: item.children && item.children.length > 0
        ? convertToTreeData(item.children)
        : undefined,
    }));
  };

  useEffect(() => {
    if (!visible) return;

    const init = async () => {
      // 加载部门树
      try {
        const res = await getDeptTree();
        if (res.code === '000000') {
          setDeptTreeData(convertToTreeData(res.data || []));
        }
      } catch (error) {
        console.error('Failed to load dept tree:', error);
      }

      // 回填表单
      if (initialValues) {
        form.setFieldsValue({
          username: initialValues.username,
          name: initialValues.name,
          phone: initialValues.phone,
          email: initialValues.email,
          deptId: initialValues.deptId != null ? String(initialValues.deptId) : undefined,
          status: initialValues.status || 'ENABLED',
        });
      } else {
        form.resetFields();
        form.setFieldsValue({ status: 'ENABLED' });
      }
    };

    init();
  }, [visible, initialValues, form]);

  const handleSubmit = async (values: any) => {
    try {
      let res;
      if (isEdit && initialValues) {
        res = await updateUser({
          id: initialValues.id,
          ...values,
        });
      } else {
        res = await createUser(values);
      }

      if (res.code === '000000') {
        message.success(isEdit ? '编辑成功' : '新增成功');
        onSuccess();
        return true;
      } else {
        message.error(res.msg);
        return false;
      }
    } catch (error: any) {
      message.error(error?.message || '操作失败');
      return false;
    }
  };

  return (
    <ModalForm
      title={isEdit ? '编辑用户' : '新增用户'}
      open={visible}
      form={form}
      modalProps={{
        onCancel,
        destroyOnHidden: true,
      }}
      onFinish={handleSubmit}
      width={520}
    >
      <ProFormText
        name="username"
        label="用户名"
        placeholder="请输入用户名"
        rules={[
          { required: true, message: '请输入用户名' },
          { max: 32, message: '用户名长度不能超过32位' },
          { pattern: /^[a-zA-Z0-9_]+$/, message: '仅允许字母、数字、下划线' },
        ]}
        disabled={isEdit && initialValues?.username === 'admin'}
        fieldProps={{
          maxLength: 32,
        }}
      />

      <ProFormText
        name="name"
        label="姓名"
        placeholder="请输入姓名"
        rules={[
          { required: true, message: '请输入姓名' },
          { max: 50, message: '姓名长度不能超过50位' },
        ]}
        fieldProps={{
          maxLength: 50,
        }}
      />

      <ProFormText
        name="phone"
        label="手机号"
        placeholder="请输入手机号"
        rules={[
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号' },
        ]}
        fieldProps={{
          maxLength: 11,
        }}
      />

      <ProFormText
        name="email"
        label="邮箱"
        placeholder="请输入请输入邮箱地址"
        rules={[
          { type: 'email', message: '请输入正确的邮箱地址' },
        ]}
        fieldProps={{
          maxLength: 100,
        }}
      />

      <Form.Item name="deptId" label="部门">
        <TreeSelect
          treeData={deptTreeData}
          placeholder="请选择部门"
          allowClear
          showSearch
          treeDefaultExpandAll
          treeNodeFilterProp="title"
          style={{ width: '100%' }}
        />
      </Form.Item>

      <ProFormRadio.Group
        name="status"
        label="状态"
        options={[
          { label: '启用', value: 'ENABLED' },
          { label: '停用', value: 'DISABLED' },
        ]}
        rules={[{ required: true }]}
      />
    </ModalForm>
  );
}
