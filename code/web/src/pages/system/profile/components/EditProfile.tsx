import { useEffect } from 'react';
import { ModalForm, ProFormText } from '@ant-design/pro-components';
import { Form, message } from 'antd';
import { updateProfile } from '@/services/profile';
import type { UserProfileVO } from '@/services/profile';

interface EditProfileModalProps {
  visible: boolean;
  profile: UserProfileVO | null;
  onCancel: () => void;
  onSuccess: () => void;
}

export default function EditProfileModal({ visible, profile, onCancel, onSuccess }: EditProfileModalProps) {
  const [form] = Form.useForm();

  useEffect(() => {
    if (visible && profile) {
      form.setFieldsValue({
        phone: profile.phone,
        email: profile.email,
      });
    }
  }, [visible, profile, form]);

  const handleSubmit = async (values: any) => {
    try {
      const res = await updateProfile(values);
      if (res.code === '000000') {
        message.success('个人信息修改成功');
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
      title="修改个人信息"
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
        name="phone"
        label="手机号"
        placeholder="请输入手机号"
        rules={[
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号' },
        ]}
        fieldProps={{ maxLength: 11 }}
      />

      <ProFormText
        name="email"
        label="邮箱"
        placeholder="请输入邮箱地址"
        rules={[
          { type: 'email', message: '请输入正确的邮箱地址' },
        ]}
        fieldProps={{ maxLength: 100 }}
      />
    </ModalForm>
  );
}
