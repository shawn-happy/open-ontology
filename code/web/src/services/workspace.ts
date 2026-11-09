import { request } from '@umijs/max';
import type { Result, PageResult, PageQuery } from './api';

// ==================== 类型定义 ====================

/** 空间列表 VO */
export interface WorkspaceListVO {
  id: string;
  name: string;
  code: string;
  description: string;
  ownerUserId: string;
  ownerUserName: string;
  status: string;
  memberCount: number;
  assetCount: number;
  lastActiveAt: string;
  createdAt: string;
}

/** 空间部门分配 VO */
export interface WorkspaceDeptVO {
  deptId: string;
  deptName: string;
  defaultRole: string;
  assignedByName: string;
  assignedAt: string;
}

/** 空间详情 VO */
export interface WorkspaceDetailVO extends WorkspaceListVO {
  ownerUserDeptName: string;
  depts: WorkspaceDeptVO[];
}

/** 平台概览 VO */
export interface WorkspaceOverviewVO {
  totalSpaces: number;
  totalMembers: number;
  totalAssets: number;
  activeSpaces30d: number;
}

/** 可访问空间 VO（空间切换器） */
export interface WorkspaceSimpleVO {
  code: string;
  name: string;
  workspaceRole: string | null;
}

/** 分配结果 VO */
export interface AssignResultVO {
  successCount: number;
  skipCount: number;
}

/** 成员分配记录 VO */
export interface MemberListVO {
  userId: string;
  username: string;
  name: string;
  deptName: string;
  workspaceRole: string;
  roleDisplayName: string;
  roleLevel: number;
  isPersonalRole: boolean;
  /** DIRECT=直接分配 / DEPT=部门分配 */
  source: 'DIRECT' | 'DEPT';
  sourceDeptName: string;
  assignedById: string;
  assignedByName: string;
  assignedAt: string;
}

/** 成员统计 VO */
export interface MemberStatsVO {
  totalMembers: number;
  directCount: number;
  deptCount: number;
  ownerCount: number;
}

/** 空间角色 VO */
export interface WorkspaceRoleVO {
  code: string;
  displayName: string;
  description: string;
  level: number;
  builtin: boolean;
}

/** 空间首页统计 VO */
export interface WorkspaceHomeStatsVO {
  memberCount: number;
  memberCountTrend: number | null;
  ontologyAssetCount: number;
  ontologyAssetTrend: number | null;
  datasetCount: number;
  datasetTrend: number | null;
  pipelineCount: number;
  pipelineTrend: number | null;
  apiCallToday: number;
  apiCallTrend: number | null;
}

/** 首页 Pipeline VO（本期空数组） */
export interface HomePipelineVO {
  id: string;
  name: string;
  status: string;
  progress: number;
  startedAt: string;
}

/** 首页动态 VO（本期空数组） */
export interface HomeActivityVO {
  id: string;
  type: string;
  content: string;
  operatorName: string;
  occurredAt: string;
}

/** 空间分页查询 */
export interface WorkspacePageQuery extends PageQuery {
  status?: string;
}

/** 成员分页查询 */
export interface MemberPageQuery extends PageQuery {
  workspaceRole?: string;
  source?: string;
}

/** 创建空间请求 */
export interface WorkspaceCreateDTO {
  name: string;
  code: string;
  description?: string;
  ownerUserId: string;
  status?: string;
}

/** 更新空间请求 */
export interface WorkspaceUpdateDTO {
  id: string;
  name?: string;
  description?: string;
  ownerUserId?: string;
}

// ==================== 平台级接口 ====================

/** 空间列表分页 */
export async function getWorkspacePage(params: WorkspacePageQuery) {
  return request<Result<PageResult<WorkspaceListVO>>>('/web/platform/workspaces', {
    method: 'GET',
    params,
  });
}

/** 平台概览统计 */
export async function getWorkspaceOverview() {
  return request<Result<WorkspaceOverviewVO>>('/web/platform/workspaces/overview', {
    method: 'GET',
  });
}

/** 可访问空间列表（登录即可，空间切换器数据源） */
export async function getAccessibleWorkspaces() {
  return request<Result<WorkspaceSimpleVO[]>>('/web/platform/workspaces/accessible', {
    method: 'GET',
  });
}

/** 空间编码唯一性校验（true=可用） */
export async function checkWorkspaceCode(code: string) {
  return request<Result<boolean>>('/web/platform/workspaces/code/check', {
    method: 'GET',
    params: { code },
  });
}

/** 空间详情 */
export async function getWorkspaceDetail(id: string) {
  return request<Result<WorkspaceDetailVO>>(`/web/platform/workspaces/${id}`, {
    method: 'GET',
  });
}

/** 创建空间 */
export async function createWorkspace(data: WorkspaceCreateDTO) {
  return request<Result<string>>('/web/platform/workspaces/create', {
    method: 'POST',
    data,
  });
}

/** 编辑空间 */
export async function updateWorkspace(data: WorkspaceUpdateDTO) {
  return request<Result<void>>('/web/platform/workspaces/update', {
    method: 'POST',
    data,
  });
}

/** 启用/停用空间 */
export async function updateWorkspaceStatus(id: string, status: string) {
  return request<Result<void>>('/web/platform/workspaces/status', {
    method: 'POST',
    data: { id, status },
  });
}

/** 删除空间 */
export async function deleteWorkspace(id: string) {
  return request<Result<void>>('/web/platform/workspaces/delete', {
    method: 'POST',
    data: { id },
  });
}

/** 批量分配用户 */
export async function assignWorkspaceUsers(id: string, userIds: string[], workspaceRole?: string) {
  return request<Result<AssignResultVO>>(`/web/platform/workspaces/${id}/members/assign`, {
    method: 'POST',
    data: { userIds, workspaceRole },
  });
}

/** 批量分配部门（workspaceRole 为部门成员默认空间角色） */
export async function assignWorkspaceDepts(id: string, deptIds: string[], workspaceRole?: string) {
  return request<Result<AssignResultVO>>(`/web/platform/workspaces/${id}/depts/assign`, {
    method: 'POST',
    data: { deptIds, workspaceRole },
  });
}

/** 移除分配部门 */
export async function removeWorkspaceDept(id: string, deptId: string) {
  return request<Result<void>>(`/web/platform/workspaces/${id}/depts/remove`, {
    method: 'POST',
    data: { deptId },
  });
}

// ==================== 空间级接口（成员分配） ====================

/** 分配记录分页（动态展开去重，直接分配优先） */
export async function getMemberPage(workspaceCode: string, params: MemberPageQuery) {
  return request<Result<PageResult<MemberListVO>>>(`/web/workspace/${workspaceCode}/members`, {
    method: 'GET',
    params,
  });
}

/** 成员统计 */
export async function getMemberStats(workspaceCode: string) {
  return request<Result<MemberStatsVO>>(`/web/workspace/${workspaceCode}/members/stats`, {
    method: 'GET',
  });
}

/** 空间角色列表（内置五级 + 自定义角色） */
export async function getWorkspaceRoles(workspaceCode: string) {
  return request<Result<WorkspaceRoleVO[]>>(`/web/workspace/${workspaceCode}/roles`, {
    method: 'GET',
  });
}

/** 分配/覆盖成员角色（workspaceRole 传 null = 清除个人覆盖，恢复部门默认） */
export async function updateMemberRole(
  workspaceCode: string,
  userId: string,
  workspaceRole: string | null,
) {
  return request<Result<void>>(`/web/workspace/${workspaceCode}/members/role`, {
    method: 'POST',
    data: { userId, workspaceRole },
  });
}

/** 移除成员（后端三分支语义：DIRECT 移除 / PERSONAL 清除 / 部门未覆盖拒绝） */
export async function removeMember(workspaceCode: string, userId: string) {
  return request<Result<void>>(`/web/workspace/${workspaceCode}/members/remove`, {
    method: 'POST',
    data: { userId },
  });
}

// ==================== 空间级接口（空间首页） ====================

/** 首页统计卡片 */
export async function getHomeStats(workspaceCode: string) {
  return request<Result<WorkspaceHomeStatsVO>>(`/web/workspace/${workspaceCode}/home/stats`, {
    method: 'GET',
  });
}

/** 进行中 Pipeline（本期空数组） */
export async function getHomePipelines(workspaceCode: string) {
  return request<Result<HomePipelineVO[]>>(`/web/workspace/${workspaceCode}/home/pipelines`, {
    method: 'GET',
  });
}

/** 近期动态（本期空数组） */
export async function getHomeActivities(workspaceCode: string) {
  return request<Result<HomeActivityVO[]>>(`/web/workspace/${workspaceCode}/home/activities`, {
    method: 'GET',
  });
}
