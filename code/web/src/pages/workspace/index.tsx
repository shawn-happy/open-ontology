import { Navigate, useParams } from '@umijs/max';

/**
 * 空间根路径重定向：/workspace/{code} → /workspace/{code}/home
 * （动态路径参数无法用静态 redirect 插值，故用组件重定向）
 */
export default function WorkspaceIndex() {
  const { workspaceCode } = useParams<{ workspaceCode: string }>();
  return <Navigate to={`/workspace/${workspaceCode}/home`} replace />;
}
