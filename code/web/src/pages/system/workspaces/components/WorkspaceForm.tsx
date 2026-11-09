import { useEffect, useState } from 'react';
import {
  ModalForm,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { Form, message } from 'antd';
import {
  checkWorkspaceCode,
  createWorkspace,
  updateWorkspace,
} from '@/services/workspace';
import type { WorkspaceListVO } from '@/services/workspace';
import { getUserPage } from '@/services/user';
import type { UserListVO } from '@/services/user';

interface WorkspaceFormProps {
  visible: boolean;
  initialValues: WorkspaceListVO | null;
  onCancel: () => void;
  onSuccess: () => void;
}

/**
 * 创建/编辑空间弹窗：
 * 编码仅创建时可填（异步唯一性校验），编辑时禁用；
 * 状态仅创建时可选，编辑时通过列表 Switch 启停用。
 */
export default function WorkspaceForm({
  visible,
  initialValues,
  onCancel,
  onSuccess,
}: WorkspaceFormProps) {
  const [form] = Form.useForm();
  const [users, setUsers] = useState<UserListVO[]>([]);
  const isEdit = !!initialValues;

  useEffect(() => {
    if (!visible) {
      return;
    }
    if (isEdit && initialValues) {
      form.setFieldsValue({
        name: initialValues.name,
        code: initialValues.code,
        description: initialValues.description,
        ownerUserId: initialValues.ownerUserId,
      });
    } else {
      form.resetFields();
      form.setFieldsValue({ status: 'ENABLED' });
    }
    // 空间管理员候选（仅启用用户）
    getUserPage({ currentPage: 1, pageSize: 1000, status: 'ENABLED' })
      .then((res) => {
        if (res.code === '000000') {
          setUsers(res.data.data || []);
        }
      })
      .catch(() => {
        // 异常已由全局请求层统一提示
      });
  }, [visible, initialValues, isEdit, form]);

  const handleSubmit = async (values: any) => {
    try {
      const res = isEdit
        ? await updateWorkspace({
            id: initialValues!.id,
            name: values.name,
            description: values.description,
            ownerUserId: values.ownerUserId,
          })
        : await createWorkspace({
            name: values.name,
            code: values.code,
            description: values.description,
            ownerUserId: values.ownerUserId,
            status: values.status,
          });
      if (res.code === '000000') {
        message.success(isEdit ? '空间已更新' : '空间已创建');
        onSuccess();
        return true;
      }
      return false;
    } catch {
      // 异常已由全局请求层统一提示
      return false;
    }
  };

  return (
    <ModalForm
      title={isEdit ? '编辑空间' : '创建空间'}
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
        name="name"
        label="空间名称"
        placeholder="如：智能制造空间"
        rules={[
          { required: true, message: '请输入空间名称' },
          { max: 50, message: '空间名称长度不能超过50位' },
        ]}
        fieldProps={{ maxLength: 50 }}
      />

      <ProFormText
        name="code"
        label="空间编码"
        placeholder="如：SMART-MFG（唯一标识）"
        tooltip={isEdit ? '创建后不可修改' : undefined}
        disabled={isEdit}
        rules={
          isEdit
            ? []
            : [
                { required: true, message: '请输入空间编码' },
                {
                  pattern: /^[A-Z][A-Z0-9_-]{2,63}$/,
                  message: '须以大写字母开头，仅含大写字母/数字/中划线/下划线，长度 3-64',
                },
                {
                  validator: async (_, value) => {
                    if (!value) {
                      return;
                    }
                    const res = await checkWorkspaceCode(value);
                    if (res.code === '000000' && !res.data) {
                      throw new Error('空间编码已存在');
                    }
                  },
                },
              ]
        }
        fieldProps={{ maxLength: 64 }}
      />

      <ProFormTextArea
        name="description"
        label="描述"
        placeholder="空间用途说明"
        fieldProps={{ rows: 3, maxLength: 200, showCount: true }}
      />

      <ProFormSelect
        name="ownerUserId"
        label="空间管理员"
        placeholder="请选择"
        rules={[{ required: true, message: '请选择空间管理员' }]}
        options={users.map((u) => ({
          label: `${u.name}（${u.username}）${u.deptName ? ` · ${u.deptName}` : ''}`,
          value: u.id,
        }))}
        fieldProps={{ showSearch: true, optionFilterProp: 'label' }}
        tooltip="仅超级管理员 / 空间管理员可创建工作空间"
      />

      {!isEdit && (
        <ProFormSelect
          name="status"
          label="状态"
          options={[
            { label: '启用', value: 'ENABLED' },
            { label: '停用', value: 'DISABLED' },
          ]}
          allowClear={false}
        />
      )}
    </ModalForm>
  );
}
