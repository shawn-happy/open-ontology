import { useState, useEffect, useCallback } from 'react';
import { Form, Input, Button, Checkbox, message, Modal, Tabs } from 'antd';
import { UserOutlined, LockOutlined, SafetyOutlined } from '@ant-design/icons';
import { history } from '@umijs/max';
import { login, getCaptcha, changePasswordFirstTime, forgotPassword, sendVerifyCode } from '@/services/auth';
import type { LoginDTO, CaptchaVO } from '@/services/auth';
import styles from './index.less';

export default function LoginPage() {
  const [captcha, setCaptcha] = useState<CaptchaVO | null>(null);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  const [firstLoginModalVisible, setFirstLoginModalVisible] = useState(false);
  const [firstLoginToken, setFirstLoginToken] = useState('');
  const [forgotModalVisible, setForgotModalVisible] = useState(false);
  const [forgotForm] = Form.useForm();
  const [countdown, setCountdown] = useState(0);

  // 加载验证码
  const loadCaptcha = useCallback(async () => {
    try {
      const res = await getCaptcha();
      if (res.code === '000000') {
        setCaptcha(res.data);
      }
    } catch (error) {
      console.error('Failed to load captcha:', error);
    }
  }, []);

  useEffect(() => {
    loadCaptcha();
  }, [loadCaptcha]);

  // 验证码倒计时
  useEffect(() => {
    if (countdown > 0) {
      const timer = setTimeout(() => setCountdown(countdown - 1), 1000);
      return () => clearTimeout(timer);
    }
  }, [countdown]);

  // 处理登录
  // 根据输入内容自动识别登录类型
  const detectLoginType = (account: string): { loginType: 'username' | 'phone' | 'email'; value: string } => {
    const trimmed = account.trim();
    if (/^1[3-9]\d{9}$/.test(trimmed)) {
      return { loginType: 'phone', value: trimmed };
    }
    if (/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(trimmed)) {
      return { loginType: 'email', value: trimmed };
    }
    return { loginType: 'username', value: trimmed };
  };

  const handleLogin = async (values: any) => {
    // 前端验证码校验
    const userInput = values.captchaCode?.trim();
    if (!userInput) {
      message.error('请输入验证码');
      return;
    }

    const account = values.account?.trim();
    if (!account) {
      message.error('请输入账号');
      return;
    }

    const { loginType, value } = detectLoginType(account);

    setLoading(true);
    try {
      const loginData: LoginDTO = {
        loginType,
        password: values.password,
        captchaKey: captcha?.captchaKey || '',
        captchaCode: userInput,
        rememberMe: values.rememberMe || false,
      };

      if (loginType === 'username') {
        loginData.username = value;
      } else if (loginType === 'phone') {
        loginData.phone = value;
      } else {
        loginData.email = value;
      }

      const res = await login(loginData);

      if (res.code === '000000') {
        const { accessToken, refreshToken, firstLogin, passwordExpired, userInfo, permissions } = res.data;

        // 存储Token
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
        localStorage.setItem('userInfo', JSON.stringify(userInfo));
        localStorage.setItem('permissions', JSON.stringify(permissions));

        if (firstLogin || passwordExpired) {
          // 首次登录或密码过期，弹出强制修改密码弹窗
          setFirstLoginToken(accessToken);
          setFirstLoginModalVisible(true);
        } else {
          message.success('登录成功');
          history.push('/welcome');
        }
      } else {
        message.error(res.msg || '登录失败');
        loadCaptcha();
        form.setFieldsValue({ captchaCode: '' });
      }
    } catch (error: any) {
      message.error(error?.message || '登录失败，请重试');
      loadCaptcha();
      form.setFieldsValue({ captchaCode: '' });
    } finally {
      setLoading(false);
    }
  };

  // 处理强制修改密码
  const handleChangePasswordFirst = async (values: any) => {
    try {
      const res = await changePasswordFirstTime({
        oldPassword: values.oldPassword,
        newPassword: values.newPassword,
        confirmPassword: values.confirmPassword,
      });

      if (res.code === '000000') {
        message.success('密码修改成功，请重新登录');
        setFirstLoginModalVisible(false);
        form.resetFields();
        loadCaptcha();
      } else {
        message.error(res.msg || '密码修改失败');
      }
    } catch (error: any) {
      message.error(error?.message || '密码修改失败');
    }
  };

  // 发送验证码（忘记密码）
  const handleSendVerifyCode = async () => {
    const account = forgotForm.getFieldValue('account');
    const accountType = forgotForm.getFieldValue('accountType') || 'phone';

    if (!account) {
      message.warning('请输入手机号或邮箱');
      return;
    }

    try {
      const res = await sendVerifyCode({ accountType, account });
      if (res.code === '000000') {
        message.success('验证码已发送');
        setCountdown(60);
      } else {
        message.error(res.msg || '发送失败');
      }
    } catch (error: any) {
      message.error(error?.message || '发送失败');
    }
  };

  // 处理忘记密码
  const handleForgotPassword = async (values: any) => {
    try {
      const res = await forgotPassword({
        accountType: values.accountType,
        account: values.account,
        verifyCode: values.verifyCode,
      });

      if (res.code === '000000') {
        message.success('密码已重置为初始密码，请重新登录');
        setForgotModalVisible(false);
        forgotForm.resetFields();
        loadCaptcha();
      } else {
        message.error(res.msg || '重置失败');
      }
    } catch (error: any) {
      message.error(error?.message || '重置失败');
    }
  };


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

  return (
    <div className={styles.loginPage}>
      <div className={styles.loginCard}>
        <div className={styles.loginHeader}>
          <h1 className={styles.loginTitle}>Prism 本体语义平台</h1>
          <p className={styles.loginSubtitle}>企业数据语义化中台</p>
        </div>

        <Form
          form={form}
          onFinish={handleLogin}
          size="large"
          initialValues={{ rememberMe: false }}
        >
          <Form.Item
            name="account"
            rules={[
              { required: true, message: '请输入用户名/手机号/邮箱' },
            ]}
          >
            <Input prefix={<UserOutlined />} placeholder="用户名 / 手机号 / 邮箱" size="large" />
          </Form.Item>

          <Form.Item
            name="password"
            rules={[{ required: true, message: '请输入密码' }]}
          >
            <Input.Password
              prefix={<LockOutlined />}
              placeholder="密码"
              size="large"
            />
          </Form.Item>

          <Form.Item>
            <div className={styles.captchaRow}>
              <Form.Item
                name="captchaCode"
                noStyle
                rules={[{ required: true, message: '请输入验证码' }]}
              >
                <Input
                  prefix={<SafetyOutlined />}
                  placeholder="验证码"
                  className={styles.captchaInput}
                />
              </Form.Item>
              {captcha?.captchaImage && (
                <img
                  src={captcha.captchaImage}
                  alt="验证码"
                  className={styles.captchaImage}
                  onClick={loadCaptcha}
                  title="点击刷新验证码"
                  style={{ cursor: 'pointer', height: 40 }}
                />
              )}
            </div>
          </Form.Item>

          <Form.Item>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <Form.Item name="rememberMe" valuePropName="checked" noStyle>
                <Checkbox>记住我</Checkbox>
              </Form.Item>
              <a onClick={() => setForgotModalVisible(true)}>忘记密码？</a>
            </div>
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              loading={loading}
              block
              size="large"
            >
              登录
            </Button>
          </Form.Item>
        </Form>

        <div className={styles.loginFooter}>
          初始管理员账号：admin / onto12345678!
        </div>
      </div>

      {/* 首次登录强制修改密码弹窗 */}
      <Modal
        title="首次登录，请修改密码"
        open={firstLoginModalVisible}
        closable={false}
        maskClosable={false}
        footer={null}
        destroyOnHidden
      >
        <Form onFinish={handleChangePasswordFirst} layout="vertical">
          <Form.Item
            name="oldPassword"
            label="当前密码"
            rules={[{ required: true, message: '请输入当前密码' }]}
          >
            <Input.Password placeholder="请输入当前密码" />
          </Form.Item>

          <Form.Item
            name="newPassword"
            label="新密码"
            rules={[
              { required: true, message: '请输入新密码' },
              { validator: validatePassword },
            ]}
          >
            <Input.Password placeholder="8-20位，含大小写字母、数字和特殊字符" />
          </Form.Item>

          <Form.Item
            name="confirmPassword"
            label="确认新密码"
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
          >
            <Input.Password placeholder="请再次输入新密码" />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" block>
              确认修改
            </Button>
          </Form.Item>
        </Form>
      </Modal>

      {/* 忘记密码弹窗 */}
      <Modal
        title="忘记密码"
        open={forgotModalVisible}
        onCancel={() => setForgotModalVisible(false)}
        footer={null}
        destroyOnHidden
      >
        <Form form={forgotForm} onFinish={handleForgotPassword} layout="vertical">
          <Form.Item
            name="accountType"
            label="验证方式"
            initialValue="phone"
          >
            <Tabs
              items={[
                { key: 'phone', label: '手机号验证' },
                { key: 'email', label: '邮箱验证' },
              ]}
              onChange={(key) => {
                forgotForm.setFieldsValue({ accountType: key });
              }}
            />
          </Form.Item>

          <Form.Item
            name="account"
            label="手机号/邮箱"
            rules={[{ required: true, message: '请输入已绑定的手机号或邮箱' }]}
          >
            <Input placeholder="请输入已绑定的手机号或邮箱" />
          </Form.Item>

          <Form.Item
            name="verifyCode"
            label="验证码"
            rules={[{ required: true, message: '请输入验证码' }]}
          >
            <div className={styles.captchaRow}>
              <Input placeholder="请输入验证码" className={styles.captchaInput} />
              <Button
                type="primary"
                ghost
                onClick={handleSendVerifyCode}
                disabled={countdown > 0}
              >
                {countdown > 0 ? `${countdown}秒后重试` : '发送验证码'}
              </Button>
            </div>
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" block>
              重置密码
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
