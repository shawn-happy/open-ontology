import { request } from '@umijs/max';
import type { Result, PageResult, PageQuery } from './api';

/** 审计日志VO */
export interface AuditLogVO {
  id: string;
  operateAt: string;
  operatorName: string;
  operatorUsername: string;
  module: string;
  content: string;
  type: string;
  result: string;
  ipAddress: string;
  requestId: string;
  userAgent: string;
}

/** 登录日志VO */
export interface LoginLogVO {
  id: string;
  loginAt: string;
  username: string;
  name: string;
  ipAddress: string;
  location: string;
  browser: string;
  os: string;
  result: string;
  failReason: string;
  userAgent: string;
  requestId: string;
}

/** 执行日志VO */
export interface ExecLogVO {
  id: string;
  execType: string;
  taskName: string;
  triggerUsername: string;
  triggerName: string;
  status: string;
  durationMs: number;
  detail: string;
  startedAt: string;
  finishedAt: string;
  requestId: string;
}

/** 审计日志查询 */
export interface AuditLogQuery extends PageQuery {
  module?: string;
  type?: string;
  startTime?: string;
  endTime?: string;
}

/** 登录日志查询 */
export interface LoginLogQuery extends PageQuery {
  username?: string;
  result?: string;
  startTime?: string;
  endTime?: string;
}

/** 执行日志查询 */
export interface ExecLogQuery extends PageQuery {
  execType?: string;
  status?: string;
  startTime?: string;
  endTime?: string;
}

/** 审计日志分页查询 */
export async function getAuditLogPage(params: AuditLogQuery) {
  return request<Result<PageResult<AuditLogVO>>>('/web/platform/system/logs/audit', {
    method: 'GET',
    params,
  });
}

/** 登录日志分页查询 */
export async function getLoginLogPage(params: LoginLogQuery) {
  return request<Result<PageResult<LoginLogVO>>>('/web/platform/system/logs/login', {
    method: 'GET',
    params,
  });
}

/** 执行日志分页查询 */
export async function getExecLogPage(params: ExecLogQuery) {
  return request<Result<PageResult<ExecLogVO>>>('/web/platform/system/logs/exec', {
    method: 'GET',
    params,
  });
}

/** 导出日志 */
export async function exportLogs(type: 'audit' | 'login' | 'exec', params?: any) {
  return request('/web/platform/system/logs/export', {
    method: 'GET',
    params: { type, ...params },
    responseType: 'blob',
  });
}
