import { ModalForm, ProFormText } from '@ant-design/pro-components';
import { Form, message } from 'antd';
import { changePassword } from '@/services/profile';

interface ChangePasswordModalProps {
  visible: boolean;
  onCancel: () => void;
  onSuccess: () => void;
}

export default function ChangePasswordModal({ visible, onCancel, onSuccess }: ChangePasswordModalProps) {
  const [form] = Form.useForm();

  // 密码复杂度校验
  const validatePassword = (_: any, value: string) => {
    if (!value) {
      return Promise.reject(new Error('请输入新密码'));
    }
    if (value.length < 8 || value.length > 20) {
      return Promise.reject(new Error('密码长度须为 8-20 位'));
    }
    const hasUpper = /[A-Z]/.test(value);
    const hasLower = /[a-z]/.test(value);
    const hasDigit = /\d/.test(value);
    const hasSpecial = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(value);
    if (!hasUpper || !hasLower || !hasDigit || !hasSpecial) {
      return Promise.reject(new Error('密码须包含大写字母、小写字母、数字和特殊字符'));
    }
    return Promise.resolve();
  };

  const handleSubmit = async (values: any) => {
    try {
      const res = await changePassword({
        oldPassword: values.oldPassword,
        newPassword: values.newPassword,
        confirmPassword: values.confirmPassword,
      });

      if (res.code === '000000') {
        message.success('密码修改成功');
        onSuccess();
        return true;
      } else {
        message.error(res.msg);
        return false;
      }
    } catch (error: any) {
      message.error(error?.message || '密码修改失败');
      return false;
    }
  };

  return (
    <ModalForm
      title="修改密码"
      open={visible}
      form={form}
      modalProps={{
        onCancel,
        destroyOnHidden: true,
      }}
      onFinish={handleSubmit}
      width={480}
    >
      <ProFormText.Password
        name="oldPassword"
        label="旧密码"
        placeholder="请输入旧密码"
        rules={[{ required: true, message: '请输入旧密码' }]}
      />

      <ProFormText.Password
        name="newPassword"
        label="新密码"
        placeholder="8-20位，含大小写字母、数字和特殊字符"
        rules={[
          { required: true, message: '请输入新密码' },
          { validator: validatePassword },
        ]}
      />

      <ProFormText.Password
        name="confirmPassword"
        label="确认新密码"
        placeholder="请再次输入新密码"
        dependencies={['newPassword']}
        rules={[
          { required: true, message: '请确认新密码' },
          ({ getFieldValue }) => ({
            validator(_, value) {
              if (!value || getFieldValue('newPassword') === value) {
                return Promise.resolve();
              }
              return Promise.reject(new Error('两次输入的密码不一致'));
            },
          }),
        ]}
      />
    </ModalForm>
  );
}
