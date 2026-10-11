/**
 * 请求工具函数
 */

/**
 * 生成请求 ID
 */
export function generateRequestId(): string {
  return `req_${Date.now()}_${Math.random().toString(36).substring(2, 9)}`;
}

/**
 * 获取当前工作空间编码（从 URL 或 localStorage）
 */
export function getWorkspaceCode(): string | null {
  // 从 URL 路径中提取 /web/{workspaceCode}/...
  const match = window.location.pathname.match(/^\/web\/([^\/]+)/);
  if (match) {
    return match[1];
  }
  // 从 localStorage 获取
  return localStorage.getItem('workspaceCode');
}

/**
 * 设置当前工作空间编码
 */
export function setWorkspaceCode(code: string): void {
  localStorage.setItem('workspaceCode', code);
}
