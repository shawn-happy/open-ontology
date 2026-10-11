import { useEffect } from 'react';
import { ModalForm, ProFormText, ProFormSelect, ProFormTextArea } from '@ant-design/pro-components';
import { Form, message } from 'antd';
import { createRole, updateRole } from '@/services/role';
import type { RoleListVO } from '@/services/role';

interface RoleFormProps {
  visible: boolean;
  initialValues: RoleListVO | null;
  onCancel: () => void;
  onSuccess: () => void;
}

export default function RoleForm({ visible, initialValues, onCancel, onSuccess }: RoleFormProps) {
  const [form] = Form.useForm();
  const isEdit = !!initialValues;

  useEffect(() => {
    if (visible && initialValues) {
      form.setFieldsValue({
        name: initialValues.name,
        code: initialValues.code,
        type: initialValues.type,
        remark: initialValues.remark,
      });
    } else if (visible) {
      form.resetFields();
      form.setFieldsValue({ type: 'GLOBAL' });
    }
  }, [visible, initialValues, form]);

  const handleSubmit = async (values: any) => {
    try {
      let res;
      if (isEdit && initialValues) {
        res = await updateRole({
          id: initialValues.id,
          ...values,
        });
      } else {
        res = await createRole(values);
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
      title={isEdit ? '编辑角色' : '新增角色'}
      open={visible}
      form={form}
      modalProps={{
        onCancel,
        destroyOnHidden: true,
      }}
      onFinish={handleSubmit}
      width={480}
    >
      <ProFormText
        name="name"
        label="角色名称"
        placeholder="请输入角色名称"
        rules={[
          { required: true, message: '请输入角色名称' },
          { max: 50, message: '角色名称长度不能超过50位' },
        ]}
        fieldProps={{ maxLength: 50 }}
      />

      <ProFormText
        name="code"
        label="角色编码"
        placeholder="请输入角色编码（字母、数字、下划线）"
        rules={[
          { required: true, message: '请输入角色编码' },
          { max: 50, message: '角色编码长度不能超过50位' },
          { pattern: /^[a-zA-Z0-9_]+$/, message: '仅允许字母、数字、下划线' },
        ]}
        disabled={isEdit}
        fieldProps={{ maxLength: 50 }}
      />

      <ProFormSelect
        name="type"
        label="角色类型"
        placeholder="请选择角色类型"
        rules={[{ required: true, message: '请选择角色类型' }]}
        options={[
          { label: '全局', value: 'GLOBAL' },
          { label: '空间', value: 'WORKSPACE' },
        ]}
      />

      <ProFormTextArea
        name="remark"
        label="备注"
        placeholder="请输入备注"
        fieldProps={{ maxLength: 200, rows: 3 }}
      />
    </ModalForm>
  );
}
