package com.prism.platform.service.impl;

import com.prism.common.enums.DataScope;
import com.prism.common.exception.BusinessException;
import com.prism.platform.constants.SystemConstants;
import com.prism.platform.constants.WorkspaceConstants;
import com.prism.platform.entity.SystemRole;
import com.prism.platform.entity.Workspace;
import com.prism.platform.exception.WorkspaceErrorCode;
import com.prism.platform.mapper.SystemRoleMapper;
import com.prism.platform.mapper.WorkspaceMapper;
import com.prism.platform.mapper.WorkspaceMemberMapper;
import com.prism.platform.service.WorkspaceAccessService;
import com.prism.platform.service.support.WorkspaceCacheSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工作空间访问校验服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceAccessServiceImpl implements WorkspaceAccessService {

    private final WorkspaceMapper workspaceMapper;
    private final WorkspaceMemberMapper workspaceMemberMapper;
    private final SystemRoleMapper systemRoleMapper;
    private final WorkspaceCacheSupport cacheSupport;

    @Override
    public Workspace getValidWorkspace(String code) {
        if (code == null || code.isEmpty()) {
            throw invalidContext();
        }
        // 缓存命中直接使用（容错降级：异常按未命中处理）
        Workspace workspace = cacheSupport.getWorkspaceInfo(code);
        if (workspace == null) {
            workspace = workspaceMapper.selectByCode(code);
            if (workspace != null) {
                cacheSupport.putWorkspaceInfo(workspace);
            }
        }
        if (workspace == null || !WorkspaceConstants.STATUS_ENABLED.equals(workspace.getStatus())) {
            throw invalidContext();
        }
        return workspace;
    }

    private BusinessException invalidContext() {
        return new BusinessException(WorkspaceErrorCode.WORKSPACE_CONTEXT_INVALID.getCode(),
                WorkspaceErrorCode.WORKSPACE_CONTEXT_INVALID.getMessage());
    }

    @Override
    public void checkAccess(String code, Long userId) {
        // 1. 空间有效性校验（无效或停用 → 020401）
        getValidWorkspace(code);

        if (userId == null) {
            throw new BusinessException(WorkspaceErrorCode.WORKSPACE_ACCESS_DENIED.getCode(),
                    WorkspaceErrorCode.WORKSPACE_ACCESS_DENIED.getMessage());
        }

        // 2. 内置超管直接放行
        if (SystemConstants.ADMIN_USER_ID.equals(userId)) {
            return;
        }

        List<SystemRole> roles = systemRoleMapper.selectByUserId(userId);
        boolean superAdmin = false;
        boolean dataScopeHit = false;
        if (roles != null) {
            for (SystemRole role : roles) {
                if (!"ENABLED".equals(role.getStatus())) {
                    continue;
                }
                // 2.1 super_admin 角色放行
                if (SystemConstants.ROLE_SUPER_ADMIN.equals(role.getCode())) {
                    superAdmin = true;
                    break;
                }
                // 3. dataScope=ALL 放行；dataScope=CUSTOM 且自定义空间列表含该 code 放行
                if (DataScope.ALL.getValue().equals(role.getDataScope())) {
                    dataScopeHit = true;
                }
                else if (DataScope.CUSTOM.getValue().equals(role.getDataScope())
                        && role.getCustomWorkspaceCodes() != null
                        && role.getCustomWorkspaceCodes().contains(code)) {
                    dataScopeHit = true;
                }
            }
        }
        if (superAdmin) {
            return;
        }

        // 4. 有效成员放行
        if (isEffectiveMember(code, userId)) {
            return;
        }

        // 5. 角色空间数据权限覆盖该空间放行
        if (dataScopeHit) {
            return;
        }

        // 6. 其余拒绝（020402）
        throw new BusinessException(WorkspaceErrorCode.WORKSPACE_ACCESS_DENIED.getCode(),
                WorkspaceErrorCode.WORKSPACE_ACCESS_DENIED.getMessage());
    }

    @Override
    public boolean isEffectiveMember(String code, Long userId) {
        if (code == null || userId == null) {
            return false;
        }
        return workspaceMemberMapper.selectEffectiveRole(code, userId) != null;
    }
}
