package com.prism.platform.service;

import com.prism.platform.entity.Workspace;

/**
 * 工作空间访问校验服务（空间上下文）
 */
public interface WorkspaceAccessService {

    /**
     * 获取有效工作空间（存在且启用）
     *
     * @param code 空间编码
     * @return 工作空间实体
     * @throws com.prism.common.exception.BusinessException 空间不存在或已停用（020401）
     */
    Workspace getValidWorkspace(String code);

    /**
     * 校验用户是否有权访问指定空间
     * 判定顺序：空间有效 → 超管（userId=1 或 super_admin 角色）→ 有效成员 → dataScope=ALL 放行 → 拒绝
     *
     * @param code   空间编码
     * @param userId 用户 ID
     * @throws com.prism.common.exception.BusinessException 空间无效（020401）或无权访问（020402）
     */
    void checkAccess(String code, Long userId);

    /**
     * 判断用户是否为空间有效成员（DIRECT / 部门展开 / PERSONAL 覆盖，排除 EXCLUDED）
     *
     * @param code   空间编码
     * @param userId 用户 ID
     * @return true=有效成员
     */
    boolean isEffectiveMember(String code, Long userId);
}
