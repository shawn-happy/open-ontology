import { request } from '@umijs/max';
import type { Result } from './api';

/** 菜单树VO */
export interface MenuTreeVO {
  id: string;
  parentId: string;
  name: string;
  type: string;
  permission: string;
  path: string;
  icon: string;
  sortOrder: number;
  status: string;
  children?: MenuTreeVO[];
}

/** 创建菜单请求 */
export interface CreateMenuDTO {
  parentId?: string;
  name: string;
  type: string;
  permission?: string;
  path?: string;
  component?: string;
  icon?: string;
  sortOrder?: number;
}

/** 更新菜单请求 */
export interface UpdateMenuDTO {
  id: string;
  name: string;
  type: string;
  permission?: string;
  path?: string;
  component?: string;
  icon?: string;
  sortOrder?: number;
}

/** 获取菜单树 */
export async function getMenuTree() {
  return request<Result<MenuTreeVO[]>>('/web/platform/system/menus/tree', {
    method: 'GET',
  });
}

/** 获取菜单详情 */
export async function getMenuDetail(id: string) {
  return request<Result<MenuTreeVO>>(`/web/platform/system/menus/${id}`, {
    method: 'GET',
  });
}

/** 创建菜单 */
export async function createMenu(data: CreateMenuDTO) {
  return request<Result<void>>('/web/platform/system/menus/create', {
    method: 'POST',
    data,
  });
}

/** 更新菜单 */
export async function updateMenu(data: UpdateMenuDTO) {
  return request<Result<void>>('/web/platform/system/menus/update', {
    method: 'POST',
    data,
  });
}

/** 删除菜单 */
export async function deleteMenu(id: string) {
  return request<Result<void>>(`/web/platform/system/menus/${id}/delete`, {
    method: 'POST',
  });
}

/** 启用/停用菜单 */
export async function enableDisableMenu(id: string, status: string) {
  return request<Result<void>>('/web/platform/system/menus/enable-disable', {
    method: 'POST',
    data: { id, status },
  });
}
