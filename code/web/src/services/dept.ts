import { request } from '@umijs/max';
import type { Result } from './api';

/** 部门树VO */
export interface DeptTreeVO {
  id: string;
  parentId: string;
  name: string;
  code: string;
  depth: number;
  leaderName: string;
  leaderId: string;
  memberCount: number;
  sortOrder: number;
  status: string;
  children?: DeptTreeVO[];
}

/** 创建部门请求 */
export interface CreateDeptDTO {
  parentId: string;
  name: string;
  code: string;
  leaderId?: string;
  sortOrder?: number;
}

/** 更新部门请求 */
export interface UpdateDeptDTO {
  id: string;
  name: string;
  code: string;
  leaderId?: string;
  sortOrder?: number;
}

/** 排序项 */
export interface SortItem {
  id: string;
  sortOrder: number;
}

/** 获取部门树 */
export async function getDeptTree() {
  return request<Result<DeptTreeVO[]>>('/web/platform/system/depts/tree', {
    method: 'GET',
  });
}

/** 获取部门详情 */
export async function getDeptDetail(id: string) {
  return request<Result<DeptTreeVO>>(`/web/platform/system/depts/${id}`, {
    method: 'GET',
  });
}

/** 创建部门 */
export async function createDept(data: CreateDeptDTO) {
  return request<Result<void>>('/web/platform/system/depts/create', {
    method: 'POST',
    data,
  });
}

/** 更新部门 */
export async function updateDept(data: UpdateDeptDTO) {
  return request<Result<void>>('/web/platform/system/depts/update', {
    method: 'POST',
    data,
  });
}

/** 删除部门 */
export async function deleteDept(id: string) {
  return request<Result<void>>(`/web/platform/system/depts/${id}/delete`, {
    method: 'POST',
  });
}

/** 启用/停用部门 */
export async function enableDisableDept(id: string, status: string) {
  return request<Result<void>>('/web/platform/system/depts/enable-disable', {
    method: 'POST',
    data: { id, status },
  });
}

/** 分配用户 */
export async function assignUsersToDept(deptId: string, userIds: string[]) {
  return request<Result<void>>('/web/platform/system/depts/assign-users', {
    method: 'POST',
    data: { targetId: deptId, userIds },
  });
}

/** 设置负责人 */
export async function setDeptLeader(deptId: string, leaderId: string) {
  return request<Result<void>>('/web/platform/system/depts/set-leader', {
    method: 'POST',
    data: { deptId, leaderId },
  });
}

/** 部门转移 */
export async function transferDept(id: string, targetParentId: string) {
  return request<Result<void>>('/web/platform/system/depts/transfer', {
    method: 'POST',
    data: { sourceDeptId: id, targetDeptId: targetParentId },
  });
}

/** 更新排序 */
export async function updateDeptSort(sortList: SortItem[]) {
  return request<Result<void>>('/web/platform/system/depts/update-sort', {
    method: 'POST',
    data: { sortList },
  });
}
