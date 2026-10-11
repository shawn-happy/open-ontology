import { useEffect, useState } from 'react';
import {
  ModalForm,
  ProFormText,
  ProFormDigit,
  ProFormSelect,
  ProFormRadio,
} from '@ant-design/pro-components';
import { Form, message } from 'antd';
import { createMenu, updateMenu } from '@/services/menu';
import type { MenuTreeVO } from '@/services/menu';

interface MenuFormProps {
  visible: boolean;
  initialValues: MenuTreeVO | null;
  parentId?: string;
  defaultType: string;
  allMenuData: MenuTreeVO[];
  onCancel: () => void;
  onSuccess: () => void;
}

export default function MenuForm({
  visible,
  initialValues,
  parentId,
  defaultType,
  allMenuData,
  onCancel,
  onSuccess,
}: MenuFormProps) {
  const [form] = Form.useForm();
  const isEdit = !!initialValues;
  const [nodeType, setNodeType] = useState<string>(defaultType);

  useEffect(() => {
    if (visible) {
      if (initialValues) {
        form.setFieldsValue({
          name: initialValues.name,
          type: initialValues.type,
          parentId: initialValues.parentId,
          permission: initialValues.permission,
          path: initialValues.path,
          icon: initialValues.icon,
          sortOrder: initialValues.sortOrder,
        });
        setNodeType(initialValues.type);
      } else {
        form.resetFields();
        form.setFieldsValue({
          type: defaultType,
          parentId: parentId,
          sortOrder: 0,
        });
        setNodeType(defaultType);
      }
    }
  }, [visible, initialValues, parentId, defaultType, form]);

  // 生成上级节点选项
  const getParentOptions = () => {
    const options: { label: string; value: string }[] = [];
    const flatten = (data: MenuTreeVO[], prefix = '') => {
      data.forEach((item) => {
        // 按钮不能作为上级
        if (item.type !== 'BUTTON') {
          const label = prefix ? `${prefix} / ${item.name}` : item.name;
          options.push({ label, value: item.id });
          if (item.children) {
            flatten(item.children, label);
          }
        }
      });
    };
    flatten(allMenuData);
    return options;
  };

  const handleSubmit = async (values: any) => {
    try {
      let res;
      if (isEdit && initialValues) {
        res = await updateMenu({
          id: initialValues.id,
          ...values,
        });
      } else {
        res = await createMenu(values);
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
      title={isEdit ? '编辑权限节点' : '新增权限节点'}
      open={visible}
      form={form}
      modalProps={{
        onCancel,
        destroyOnHidden: true,
      }}
      onFinish={handleSubmit}
      width={520}
      onValuesChange={(changed) => {
        if (changed.type) {
          setNodeType(changed.type);
        }
      }}
    >
      <ProFormRadio.Group
        name="type"
        label="节点类型"
        rules={[{ required: true }]}
        options={[
          { label: '目录', value: 'DIRECTORY' },
          { label: '菜单', value: 'MENU' },
          { label: '按钮', value: 'BUTTON' },
        ]}
        disabled={isEdit}
      />

      <ProFormText
        name="name"
        label="名称"
        placeholder="请输入节点名称"
        rules={[
          { required: true, message: '请输入节点名称' },
          { max: 50, message: '名称长度不能超过50位' },
        ]}
        fieldProps={{ maxLength: 50 }}
      />

      <ProFormSelect
        name="parentId"
        label="上级节点"
        placeholder="请选择上级节点"
        options={getParentOptions()}
        rules={[
          {
            required: nodeType === 'BUTTON' || nodeType === 'MENU',
            message: '请选择上级节点',
          },
        ]}
        fieldProps={{
          showSearch: true,
          allowClear: true,
        }}
      />

      <ProFormText
        name="permission"
        label="权限标识"
        placeholder="如 system:user:create"
        rules={[
          {
            required: nodeType === 'MENU' || nodeType === 'BUTTON',
            message: '请输入权限标识',
          },
          { max: 100, message: '权限标识长度不能超过100位' },
          {
            pattern: /^[a-z]+:[a-z]+:[a-z]+$/,
            message: '格式为 模块:资源:动作，如 system:user:create',
          },
        ]}
        fieldProps={{ maxLength: 100 }}
      />

      <ProFormText
        name="path"
        label="路由路径"
        placeholder="如 /system/users"
        fieldProps={{ maxLength: 200 }}
      />

      <ProFormText
        name="icon"
        label="图标"
        placeholder="菜单图标（可选）"
        fieldProps={{ maxLength: 20 }}
      />

      <ProFormDigit
        name="sortOrder"
        label="排序"
        placeholder="正整数"
        min={0}
        fieldProps={{ precision: 0 }}
      />
    </ModalForm>
  );
}
