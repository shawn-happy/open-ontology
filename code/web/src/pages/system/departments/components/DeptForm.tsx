import { useEffect } from 'react';
import {
  ModalForm,
  ProFormText,
  ProFormDigit,
  ProFormSelect,
} from '@ant-design/pro-components';
import { Form, message } from 'antd';
import { createDept, updateDept } from '@/services/dept';
import type { DeptTreeVO } from '@/services/dept';
import { getUserPage } from '@/services/user';

interface DeptFormProps {
  visible: boolean;
  initialValues: DeptTreeVO | null;
  parentId?: string;
  allDeptData: DeptTreeVO[];
  onCancel: () => void;
  onSuccess: () => void;
}

export default function DeptForm({
  visible,
  initialValues,
  parentId,
  allDeptData,
  onCancel,
  onSuccess,
}: DeptFormProps) {
  const [form] = Form.useForm();
  const isEdit = !!initialValues;

  useEffect(() => {
    if (visible && initialValues) {
      form.setFieldsValue({
        name: initialValues.name,
        code: initialValues.code,
        parentId: initialValues.parentId,
        leaderId: initialValues.leaderId,
        sortOrder: initialValues.sortOrder,
      });
    } else if (visible) {
      form.resetFields();
      form.setFieldsValue({
        parentId: parentId,
        sortOrder: 0,
      });
    }
  }, [visible, initialValues, parentId, form]);

  // 生成上级部门选项
  const getParentOptions = () => {
    const options: { label: string; value: string }[] = [];
    const flatten = (data: DeptTreeVO[], prefix = '') => {
      data.forEach((item) => {
        // 编辑时排除自身及其子节点
        if (isEdit && initialValues && (item.id === initialValues.id)) {
          return;
        }
        const label = prefix ? `${prefix} / ${item.name}` : item.name;
        options.push({ label, value: item.id });
        if (item.children) {
          flatten(item.children, label);
        }
      });
    };
    flatten(allDeptData);
    return options;
  };

  const handleSubmit = async (values: any) => {
    try {
      let res;
      if (isEdit && initialValues) {
        res = await updateDept({
          id: initialValues.id,
          ...values,
        });
      } else {
        res = await createDept({
          ...values,
          parentId: values.parentId || parentId,
        });
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
      title={isEdit ? '编辑部门' : '新增部门'}
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
        label="部门名称"
        placeholder="请输入部门名称"
        rules={[
          { required: true, message: '请输入部门名称' },
          { max: 50, message: '部门名称长度不能超过50位' },
        ]}
        fieldProps={{ maxLength: 50 }}
      />

      <ProFormText
        name="code"
        label="部门编码"
        placeholder="请输入部门编码（字母、数字、下划线）"
        rules={[
          { required: true, message: '请输入部门编码' },
          { max: 50, message: '部门编码长度不能超过50位' },
          { pattern: /^[a-zA-Z0-9_]+$/, message: '仅允许字母、数字、下划线' },
        ]}
        disabled={isEdit}
        fieldProps={{ maxLength: 50 }}
      />

      <ProFormSelect
        name="parentId"
        label="上级部门"
        placeholder="请选择上级部门"
        options={getParentOptions()}
        fieldProps={{
          showSearch: true,
          allowClear: true,
        }}
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
