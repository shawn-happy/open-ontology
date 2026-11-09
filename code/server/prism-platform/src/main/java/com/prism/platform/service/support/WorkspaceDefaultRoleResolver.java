package com.prism.platform.service.support;

import com.prism.common.enums.WorkspaceRole;
import com.prism.platform.constants.SystemConstants;
import com.prism.platform.entity.SystemDept;
import com.prism.platform.entity.SystemRole;
import com.prism.platform.entity.SystemUserDept;
import com.prism.platform.mapper.SystemDeptMapper;
import com.prism.platform.mapper.SystemRoleMapper;
import com.prism.platform.mapper.SystemUserDeptMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 空间创建人默认角色判定器（PRD FR-19 / FR-23）
 * <p>
 * 判定规则：创建人若为其所在部门的 leader，或持有系统角色 ∈ {super_admin, workspace_admin}（启用）→ OWNER；
 * 否则 → MAINTAINER。
 */
@Component
@RequiredArgsConstructor
public class WorkspaceDefaultRoleResolver {

    private final SystemUserDeptMapper systemUserDeptMapper;
    private final SystemDeptMapper systemDeptMapper;
    private final SystemRoleMapper systemRoleMapper;

    /**
     * 判定创建人的默认空间角色
     *
     * @param userId 创建人用户 ID
     * @return OWNER 或 MAINTAINER
     */
    public WorkspaceRole resolve(Long userId) {
        if (userId == null) {
            return WorkspaceRole.MAINTAINER;
        }

        // 1. 是否为所在部门负责人
        SystemUserDept userDept = systemUserDeptMapper.selectByUserId(userId);
        if (userDept != null && userDept.getDeptId() != null) {
            SystemDept dept = systemDeptMapper.selectById(userDept.getDeptId());
            if (dept != null && userId.equals(dept.getLeaderId())) {
                return WorkspaceRole.OWNER;
            }
        }

        // 2. 是否持有超管 / 空间管理员系统角色（启用）
        List<SystemRole> roles = systemRoleMapper.selectByUserId(userId);
        if (roles != null) {
            for (SystemRole role : roles) {
                if (!"ENABLED".equals(role.getStatus())) {
                    continue;
                }
                if (SystemConstants.ROLE_SUPER_ADMIN.equals(role.getCode())
                        || SystemConstants.ROLE_WORKSPACE_ADMIN.equals(role.getCode())) {
                    return WorkspaceRole.OWNER;
                }
            }
        }

        return WorkspaceRole.MAINTAINER;
    }
}
