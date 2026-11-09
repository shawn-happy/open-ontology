package com.prism.platform.service.support;

import com.prism.common.enums.WorkspaceRole;
import com.prism.common.exception.BusinessException;
import com.prism.platform.exception.WorkspaceErrorCode;
import org.springframework.stereotype.Component;

/**
 * 空间角色等级约束校验器（PRD FR-22，纯逻辑组件）
 */
@Component
public class WorkspaceRoleChecker {

    /**
     * 角色操作（分配/调整角色、移除成员、清除个人角色）仅能作用于
     * 角色等级严格低于操作人的成员；违规抛 020207。
     *
     * @param operatorLevel 操作人空间角色等级
     * @param targetLevel   目标成员空间角色等级
     */
    public void assertCanOperate(int operatorLevel, int targetLevel) {
        if (targetLevel >= operatorLevel) {
            throw new BusinessException(WorkspaceErrorCode.MEMBER_ROLE_LEVEL_DENIED.getCode(),
                    WorkspaceErrorCode.MEMBER_ROLE_LEVEL_DENIED.getMessage());
        }
    }

    /**
     * Owner 角色仅能由 Owner 授予；违规抛 020208。
     *
     * @param operatorRole 操作人空间角色编码
     * @param newRole      拟授予的角色编码
     */
    public void assertCanGrant(String operatorRole, String newRole) {
        if (WorkspaceRole.OWNER.getCode().equals(newRole)
                && !WorkspaceRole.OWNER.getCode().equals(operatorRole)) {
            throw new BusinessException(WorkspaceErrorCode.OWNER_GRANT_DENIED.getCode(),
                    WorkspaceErrorCode.OWNER_GRANT_DENIED.getMessage());
        }
    }
}
