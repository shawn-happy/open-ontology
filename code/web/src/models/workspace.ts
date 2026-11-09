import { useCallback, useState } from 'react';
import { getAccessibleWorkspaces } from '@/services/workspace';
import type { WorkspaceSimpleVO } from '@/services/workspace';
import { setWorkspaceCode } from '@/utils/request';

/**
 * 空间角色等级（与后端 WorkspaceRole 枚举一致；自定义角色视为 Developer 等级）
 */
export const ROLE_LEVELS: Record<string, number> = {
  OWNER: 5,
  MAINTAINER: 4,
  DEVELOPER: 3,
  REPORTER: 2,
};

export const CUSTOM_ROLE_LEVEL = 3;

/** 角色码 → 等级；未知（自定义）角色按 Developer 等级处理；无角色返回 0 */
export function roleLevelOf(role?: string | null): number {
  if (!role) {
    return 0;
  }
  return ROLE_LEVELS[role] ?? CUSTOM_ROLE_LEVEL;
}

/**
 * 当前工作空间上下文（WorkspaceLayout 挂载时通过 initWorkspace 拉取 accessible 解析）
 *
 * 消费方式：useModel('workspace')
 */
export default function useWorkspaceModel() {
  const [workspaceCode, setCode] = useState<string>('');
  const [workspaceName, setName] = useState<string>('');
  const [myRole, setMyRole] = useState<string | null>(null);
  /** 当前用户可访问的全部空间（顶栏切换下拉用） */
  const [accessibleWorkspaces, setAccessibleWorkspaces] = useState<
    WorkspaceSimpleVO[]
  >([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [initialized, setInitialized] = useState<boolean>(false);

  const myRoleLevel = roleLevelOf(myRole);

  /**
   * 按空间编码初始化上下文：拉取可访问空间列表并定位当前空间。
   * @returns true=有权访问且已初始化；false=不在可访问列表（无权访问）或接口失败
   */
  const initWorkspace = useCallback(async (code: string): Promise<boolean> => {
    setLoading(true);
    try {
      const res = await getAccessibleWorkspaces();
      if (res.code !== '000000') {
        return false;
      }
      const list = res.data || [];
      setAccessibleWorkspaces(list);
      const hit = list.find((w) => w.code === code);
      if (!hit) {
        return false;
      }
      setCode(hit.code);
      setName(hit.name);
      setMyRole(hit.workspaceRole ?? null);
      setWorkspaceCode(hit.code);
      setInitialized(true);
      return true;
    } catch {
      // 异常已由全局请求层统一提示，此处按无权访问处理
      return false;
    } finally {
      setLoading(false);
    }
  }, []);

  return {
    workspaceCode,
    workspaceName,
    myRole,
    myRoleLevel,
    accessibleWorkspaces,
    loading,
    initialized,
    initWorkspace,
  };
}
