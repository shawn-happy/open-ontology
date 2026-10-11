import { request } from '@umijs/max';
import type { Result, PageResult, PageQuery } from './api';

/** 用户列表VO */
export interface UserListVO {
  id: string;
  username: string;
  name: string;
  phone: string;
  email: string;
  avatar: string;
  status: string;
  deptName: string;
  deptId: string;
  roleNames: string[];
  roleIds: string[];
  lastLoginAt: string;
}

/** 用户详情VO */
export interface UserDetailVO extends UserListVO {
  createdAt: string;
  updatedAt: string;
  createdBy: string;
  updatedBy: string;
}

/** 创建用户请求 */
export interface CreateUserDTO {
  username: string;
  name: string;
  phone?: string;
  email?: string;
  deptId?: string;
  status: string;
}

/** 更新用户请求 */
export interface UpdateUserDTO {
  id: string;
  username: string;
  name: string;
  phone?: string;
  email?: string;
  deptId?: string;
  status: string;
}

/** 用户分页查询 */
export interface UserPageQuery extends PageQuery {
  deptId?: string;
  status?: string;
}

/** 导入结果 */
export interface ImportResultVO {
  totalCount: number;
  successCount: number;
  failCount: number;
  failDetails: { rowNum: number; username: string; reason: string }[];
}

/** 用户列表分页查询 */
export async function getUserPage(params: UserPageQuery) {
  return request<Result<PageResult<UserListVO>>>('/web/platform/system/users', {
    method: 'GET',
    params,
  });
}

/** 获取用户详情 */
export async function getUserDetail(id: string) {
  return request<Result<UserDetailVO>>(`/web/platform/system/users/${id}`, {
    method: 'GET',
  });
}

/** 创建用户 */
export async function createUser(data: CreateUserDTO) {
  return request<Result<void>>('/web/platform/system/users/create', {
    method: 'POST',
    data,
  });
}

/** 更新用户 */
export async function updateUser(data: UpdateUserDTO) {
  return request<Result<void>>('/web/platform/system/users/update', {
    method: 'POST',
    data,
  });
}

/** 删除用户 */
export async function deleteUser(id: string) {
  return request<Result<void>>(`/web/platform/system/users/${id}/delete`, {
    method: 'POST',
  });
}

/** 启用/停用用户 */
export async function enableDisableUser(id: string, status: string) {
  return request<Result<void>>('/web/platform/system/users/enable-disable', {
    method: 'POST',
    data: { id, status },
  });
}

/** 重置密码 */
export async function resetPassword(id: string) {
  return request<Result<void>>(`/web/platform/system/users/${id}/reset-password`, {
    method: 'POST',
  });
}

/** 分配角色 */
export async function assignRoles(userId: string, roleIds: string[]) {
  return request<Result<void>>('/web/platform/system/users/assign-roles', {
    method: 'POST',
    data: { userId, roleIds },
  });
}

/** 分配部门 */
export async function assignDept(userId: string, deptId: string) {
  return request<Result<void>>('/web/platform/system/users/assign-dept', {
    method: 'POST',
    data: { userId, deptId },
  });
}

/** 批量导入用户 */
export async function importUsers(file: File) {
  const formData = new FormData();
  formData.append('file', file);
  return request<Result<ImportResultVO>>('/web/platform/system/users/import', {
    method: 'POST',
    data: formData,
    requestType: 'form',
  });
}

/** 批量导出用户 */
export async function exportUsers(params: UserPageQuery) {
  return request('/web/platform/system/users/export', {
    method: 'GET',
    params,
    responseType: 'blob',
  });
}

/** 下载导入模板 */
export async function downloadTemplate() {
  return request('/web/platform/system/users/import-template', {
    method: 'GET',
    responseType: 'blob',
  });
}
