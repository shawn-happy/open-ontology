import { request } from '@umijs/max';
import type { Result } from './api';

/** 登录请求 */
export interface LoginDTO {
  loginType: 'username' | 'phone' | 'email';
  username?: string;
  phone?: string;
  email?: string;
  password: string;
  captchaKey: string;
  captchaCode: string;
  rememberMe?: boolean;
}

/** 登录响应 */
export interface LoginVO {
  accessToken: string;
  refreshToken: string;
  firstLogin: boolean;
  passwordExpired: boolean;
  userInfo: UserInfoVO;
  permissions: string[];
}

export interface UserInfoVO {
  id: string;
  username: string;
  name: string;
  phone: string;
  email: string;
  avatar: string;
  roleNames: string[];
  deptName: string;
}

/** 验证码响应 */
export interface CaptchaVO {
  captchaKey: string;
  captchaImage: string;
}

/** 修改密码请求 */
export interface ChangePasswordDTO {
  oldPassword: string;
  newPassword: string;
  confirmPassword: string;
}

/** 忘记密码请求 */
export interface ForgotPasswordDTO {
  accountType: 'phone' | 'email';
  account: string;
  verifyCode: string;
}

/** 刷新Token请求 */
export interface RefreshTokenDTO {
  refreshToken: string;
}

/** 发送验证码 */
export interface SendVerifyCodeDTO {
  accountType: 'phone' | 'email';
  account: string;
}

/** 登录 */
export async function login(data: LoginDTO) {
  return request<Result<LoginVO>>('/web/platform/auth/login', {
    method: 'POST',
    data,
  });
}

/** 登出 */
export async function logout() {
  return request<Result<void>>('/web/platform/auth/logout', {
    method: 'POST',
  });
}

/** 获取图形验证码 */
export async function getCaptcha() {
  return request<Result<CaptchaVO>>('/web/platform/auth/captcha', {
    method: 'GET',
  });
}

/** 刷新Token */
export async function refreshToken(data: RefreshTokenDTO) {
  return request<Result<string>>('/web/platform/auth/refresh-token', {
    method: 'POST',
    data,
  });
}

/** 首次登录强制修改密码 */
export async function changePasswordFirstTime(data: ChangePasswordDTO) {
  return request<Result<void>>('/web/platform/auth/change-password-first-time', {
    method: 'POST',
    data,
  });
}

/** 忘记密码 */
export async function forgotPassword(data: ForgotPasswordDTO) {
  return request<Result<void>>('/web/platform/auth/forgot-password', {
    method: 'POST',
    data,
  });
}

/** 发送验证码（忘记密码用） */
export async function sendVerifyCode(data: SendVerifyCodeDTO) {
  return request<Result<void>>('/web/platform/auth/send-verify-code', {
    method: 'POST',
    data,
  });
}
