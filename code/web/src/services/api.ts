import { request } from '@umijs/max';

/**
 * 统一响应结构
 */
export interface Result<T = any> {
  code: string;
  msg: string;
  data: T;
  requestId: string;
}

/**
 * 分页响应
 */
export interface PageResult<T = any> {
  data: T[];
  pageInfo: {
    currentPage: number;
    pageSize: number;
    total: number;
    totalPages: number;
    hasNext: boolean;
    hasPrevious: boolean;
  };
}

/**
 * 分页请求
 */
export interface PageQuery {
  currentPage?: number;
  pageSize?: number;
  keyword?: string;
}

/**
 * 健康检查
 */
export async function healthCheck() {
  return request<Result<string>>('/web/platform/health', {
    method: 'GET',
  });
}
