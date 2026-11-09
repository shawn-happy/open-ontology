import { Tag } from 'antd';

/** 内置四级角色徽标色（与原型一致：Owner 紫 / Maintainer 蓝 / Developer 绿 / Reporter 青） */
const ROLE_COLORS: Record<string, string> = {
  OWNER: 'purple',
  MAINTAINER: 'blue',
  DEVELOPER: 'green',
  REPORTER: 'cyan',
};

/** 自定义空间角色兜底色 */
const CUSTOM_ROLE_COLOR = 'gold';

interface RoleBadgeProps {
  /** 角色码（如 OWNER / MAINTAINER / 自定义角色码） */
  role: string;
  /** 展示名（后端 roleDisplayName / WorkspaceRoleVO.displayName），缺省按角色码美化 */
  displayName?: string;
  style?: React.CSSProperties;
}

/**
 * 空间角色徽标
 */
export default function RoleBadge({ role, displayName, style }: RoleBadgeProps) {
  const text =
    displayName || (role ? role.charAt(0) + role.slice(1).toLowerCase() : '-');
  return (
    <Tag color={ROLE_COLORS[role] || CUSTOM_ROLE_COLOR} style={{ marginInlineEnd: 0, ...style }}>
      {text}
    </Tag>
  );
}
