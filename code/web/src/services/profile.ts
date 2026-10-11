import { request } from '@umijs/max';
import type { Result } from './api';

/** 个人信息VO */
export interface UserProfileVO {
  id: string;
  username: string;
  name: string;
  phone: string;
  email: string;
  avatar: string;
  roleNames: string[];
  deptName: string;
  lastLoginAt: string;
  createdAt: string;
}

/** 更新个人信息请求 */
export interface UpdateProfileDTO {
  phone?: string;
  email?: string;
}

/** 修改密码请求 */
export interface ChangePasswordDTO {
  oldPassword: string;
  newPassword: string;
  confirmPassword: string;
}

/** 获取个人信息 */
export async function getProfile() {
  return request<Result<UserProfileVO>>('/web/platform/system/profile', {
    method: 'GET',
  });
}

/** 更新个人信息 */
export async function updateProfile(data: UpdateProfileDTO) {
  return request<Result<void>>('/web/platform/system/profile/update', {
    method: 'POST',
    data,
  });
}

/** 修改密码 */
export async function changePassword(data: ChangePasswordDTO) {
  return request<Result<void>>('/web/platform/system/profile/change-password', {
    method: 'POST',
    data,
  });
}

/** 上传头像 */
export async function uploadAvatar(file: File) {
  const formData = new FormData();
  formData.append('file', file);
  return request<Result<string>>('/web/platform/system/profile/upload-avatar', {
    method: 'POST',
    data: formData,
    requestType: 'form',
  });
}
