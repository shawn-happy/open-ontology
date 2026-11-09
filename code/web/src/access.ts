/**
 * UmiJS Access 插件权限定义
 *
 * 权限数据来源：登录接口返回的 permissions（权限码列表，如 system:user:create），
 * 与后端 system_menu 表 BUTTON 类型节点的 permission 字段一一对应。
 *
 * - 菜单级：拥有某模块任一按钮权限码（前缀匹配）即可看到对应菜单；
 *   超级管理员关联全部菜单，天然拥有所有权限码。
 * - 按钮级：页面内通过 useAccess().hasPerm('xxx') 精确判断。
 */
export interface InitialState {
  userInfo?: Record<string, any>;
  permissions?: string[];
}

export default function access(initialState: InitialState | undefined) {
  const permissions = initialState?.permissions ?? [];

  /** 精确匹配权限码（按钮级） */
  const hasPerm = (code: string) => permissions.includes(code);

  /** 前缀匹配：拥有该模块任一权限（菜单级） */
  const hasModulePerm = (prefix: string) =>
    permissions.some((p) => p.startsWith(prefix));

  return {
    hasPerm,
    canSystemUsers: hasModulePerm('system:user:'),
    canSystemRoles: hasModulePerm('system:role:'),
    canSystemPermissions: hasModulePerm('system:menu:'),
    canSystemDepts: hasModulePerm('system:dept:'),
    canSystemWorkspaces: hasModulePerm('platform:workspace:'),
    canSystemLogs: hasModulePerm('system:log:'),
  };
}
