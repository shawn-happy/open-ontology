import { request } from '@umijs/max';
import type { Result, PageResult, PageQuery } from './api';

/** 角色列表VO */
export interface RoleListVO {
  id: string;
  name: string;
  code: string;
  type: string;
  dataScope: string;
  memberCount: number;
  status: string;
  remark: string;
  builtIn: boolean;
  updatedAt: string;
}

/** 角色详情VO */
export interface RoleDetailVO extends RoleListVO {
  menuIds: string[];
  customWorkspaceCodes: string[];
  createdAt: string;
  createdBy: string;
  updatedBy: string;
}

/** 创建角色请求 */
export interface CreateRoleDTO {
  name: string;
  code: string;
  type: string;
  remark?: string;
}

/** 更新角色请求 */
export interface UpdateRoleDTO {
  id: string;
  name: string;
  code: string;
  type: string;
  remark?: string;
}

/** 角色权限配置 */
export interface RolePermissionDTO {
  roleId: string;
  menuIds: string[];
  dataScope: string;
  customWorkspaceCodes?: string[];
}

/** 角色分页查询 */
export interface RolePageQuery extends PageQuery {}

/** 角色列表分页查询 */
export async function getRolePage(params: RolePageQuery) {
  return request<Result<PageResult<RoleListVO>>>('/web/platform/system/roles', {
    method: 'GET',
    params,
  });
}

/** 获取全部角色（不分页，用于下拉选择） */
export async function getAllRoles(keyword?: string) {
  return request<Result<RoleListVO[]>>('/web/platform/system/roles/all', {
    method: 'GET',
    params: keyword ? { keyword } : {},
  });
}

/** 获取角色详情 */
export async function getRoleDetail(id: string) {
  return request<Result<RoleDetailVO>>(`/web/platform/system/roles/${id}`, {
    method: 'GET',
  });
}

/** 创建角色 */
export async function createRole(data: CreateRoleDTO) {
  return request<Result<void>>('/web/platform/system/roles/create', {
    method: 'POST',
    data,
  });
}

/** 更新角色 */
export async function updateRole(data: UpdateRoleDTO) {
  return request<Result<void>>('/web/platform/system/roles/update', {
    method: 'POST',
    data,
  });
}

/** 删除角色 */
export async function deleteRole(id: string) {
  return request<Result<void>>(`/web/platform/system/roles/${id}/delete`, {
    method: 'POST',
  });
}

/** 启用/停用角色 */
export async function enableDisableRole(id: string, status: string) {
  return request<Result<void>>('/web/platform/system/roles/enable-disable', {
    method: 'POST',
    data: { id, status },
  });
}

/** 配置权限 */
export async function configurePermissions(data: RolePermissionDTO) {
  return request<Result<void>>('/web/platform/system/roles/configure-permissions', {
    method: 'POST',
    data,
  });
}

/** 分配用户 */
export async function assignUsersToRole(roleId: string, userIds: string[]) {
  return request<Result<void>>('/web/platform/system/roles/assign-users', {
    method: 'POST',
    data: { targetId: roleId, userIds },
  });
}
