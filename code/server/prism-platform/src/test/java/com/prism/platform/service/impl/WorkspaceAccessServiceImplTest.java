package com.prism.platform.service.impl;

import com.prism.common.exception.BusinessException;
import com.prism.platform.constants.WorkspaceConstants;
import com.prism.platform.entity.SystemRole;
import com.prism.platform.entity.Workspace;
import com.prism.platform.mapper.SystemRoleMapper;
import com.prism.platform.mapper.WorkspaceMapper;
import com.prism.platform.mapper.WorkspaceMemberMapper;
import com.prism.platform.service.support.WorkspaceCacheSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 空间访问校验服务测试（020401 / 020402 判定顺序）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("空间访问校验服务")
class WorkspaceAccessServiceImplTest {

    private static final String CODE = "SMART-MFG";
    private static final Long USER_ID = 100L;

    @Mock
    private WorkspaceMapper workspaceMapper;

    @Mock
    private WorkspaceMemberMapper workspaceMemberMapper;

    @Mock
    private SystemRoleMapper systemRoleMapper;

    @Mock
    private WorkspaceCacheSupport cacheSupport;

    @InjectMocks
    private WorkspaceAccessServiceImpl accessService;

    private Workspace workspace(String status) {
        Workspace ws = new Workspace();
        ws.setId(1L);
        ws.setCode(CODE);
        ws.setStatus(status);
        return ws;
    }

    private SystemRole role(String code, String status, String dataScope, List<String> customCodes) {
        SystemRole role = new SystemRole();
        role.setCode(code);
        role.setStatus(status);
        role.setDataScope(dataScope);
        role.setCustomWorkspaceCodes(customCodes);
        return role;
    }

    private void mockDbWorkspace(Workspace ws) {
        when(cacheSupport.getWorkspaceInfo(CODE)).thenReturn(null);
        when(workspaceMapper.selectByCode(CODE)).thenReturn(ws);
    }

    // ===== getValidWorkspace =====

    @Test
    @DisplayName("空间编码为空 → 020401")
    void getValidWorkspace_blankCode_throws020401() {
        assertThatThrownBy(() -> accessService.getValidWorkspace(null))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getCode())
                .isEqualTo("020401");
    }

    @Test
    @DisplayName("空间不存在 → 020401")
    void getValidWorkspace_notFound_throws020401() {
        mockDbWorkspace(null);
        assertThatThrownBy(() -> accessService.getValidWorkspace(CODE))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getCode())
                .isEqualTo("020401");
    }

    @Test
    @DisplayName("空间已停用 → 020401")
    void getValidWorkspace_disabled_throws020401() {
        mockDbWorkspace(workspace(WorkspaceConstants.STATUS_DISABLED));
        assertThatThrownBy(() -> accessService.getValidWorkspace(CODE))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getCode())
                .isEqualTo("020401");
    }

    @Test
    @DisplayName("缓存命中直接返回，不回源 DB")
    void getValidWorkspace_cacheHit_skipsDb() {
        Workspace cached = workspace(WorkspaceConstants.STATUS_ENABLED);
        when(cacheSupport.getWorkspaceInfo(CODE)).thenReturn(cached);

        assertThat(accessService.getValidWorkspace(CODE)).isSameAs(cached);
        verify(workspaceMapper, never()).selectByCode(anyString());
    }

    @Test
    @DisplayName("缓存未命中回源 DB 并写缓存")
    void getValidWorkspace_cacheMiss_loadsDbAndPutsCache() {
        Workspace ws = workspace(WorkspaceConstants.STATUS_ENABLED);
        mockDbWorkspace(ws);

        assertThat(accessService.getValidWorkspace(CODE)).isSameAs(ws);
        verify(cacheSupport).putWorkspaceInfo(ws);
    }

    // ===== checkAccess =====

    @Test
    @DisplayName("userId 为空 → 020402")
    void checkAccess_nullUserId_throws020402() {
        mockDbWorkspace(workspace(WorkspaceConstants.STATUS_ENABLED));
        assertThatThrownBy(() -> accessService.checkAccess(CODE, null))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getCode())
                .isEqualTo("020402");
    }

    @Test
    @DisplayName("空间无效 → 020401 优先于成员判定")
    void checkAccess_invalidWorkspace_throws020401() {
        mockDbWorkspace(null);
        assertThatThrownBy(() -> accessService.checkAccess(CODE, USER_ID))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getCode())
                .isEqualTo("020401");
    }

    @Test
    @DisplayName("内置超管（userId=1）直接放行，不查角色")
    void checkAccess_builtinAdmin_passes() {
        mockDbWorkspace(workspace(WorkspaceConstants.STATUS_ENABLED));

        accessService.checkAccess(CODE, 1L);

        verify(systemRoleMapper, never()).selectByUserId(anyLong());
    }

    @Test
    @DisplayName("启用 super_admin 角色放行")
    void checkAccess_superAdminRole_passes() {
        mockDbWorkspace(workspace(WorkspaceConstants.STATUS_ENABLED));
        when(systemRoleMapper.selectByUserId(USER_ID))
                .thenReturn(List.of(role("super_admin", "ENABLED", null, null)));

        accessService.checkAccess(CODE, USER_ID);

        verify(workspaceMemberMapper, never()).selectEffectiveRole(anyString(), anyLong());
    }

    @Test
    @DisplayName("有效成员放行（无系统角色）")
    void checkAccess_effectiveMember_passes() {
        mockDbWorkspace(workspace(WorkspaceConstants.STATUS_ENABLED));
        when(systemRoleMapper.selectByUserId(USER_ID)).thenReturn(Collections.emptyList());
        when(workspaceMemberMapper.selectEffectiveRole(CODE, USER_ID)).thenReturn("DEVELOPER");

        accessService.checkAccess(CODE, USER_ID);
    }

    @Test
    @DisplayName("dataScope=ALL 角色放行（非成员）")
    void checkAccess_dataScopeAll_passes() {
        mockDbWorkspace(workspace(WorkspaceConstants.STATUS_ENABLED));
        when(systemRoleMapper.selectByUserId(USER_ID))
                .thenReturn(List.of(role("ops", "ENABLED", "ALL", null)));
        when(workspaceMemberMapper.selectEffectiveRole(CODE, USER_ID)).thenReturn(null);

        accessService.checkAccess(CODE, USER_ID);
    }

    @Test
    @DisplayName("dataScope=CUSTOM 且自定义空间列表含该编码 → 放行")
    void checkAccess_dataScopeCustomHit_passes() {
        mockDbWorkspace(workspace(WorkspaceConstants.STATUS_ENABLED));
        when(systemRoleMapper.selectByUserId(USER_ID))
                .thenReturn(List.of(role("custom", "ENABLED", "CUSTOM", List.of(CODE))));
        when(workspaceMemberMapper.selectEffectiveRole(CODE, USER_ID)).thenReturn(null);

        accessService.checkAccess(CODE, USER_ID);
    }

    @Test
    @DisplayName("dataScope=CUSTOM 不含该编码且非成员 → 020402")
    void checkAccess_dataScopeCustomMiss_throws020402() {
        mockDbWorkspace(workspace(WorkspaceConstants.STATUS_ENABLED));
        when(systemRoleMapper.selectByUserId(USER_ID))
                .thenReturn(List.of(role("custom", "ENABLED", "CUSTOM", List.of("OTHER-WS"))));
        when(workspaceMemberMapper.selectEffectiveRole(CODE, USER_ID)).thenReturn(null);

        assertThatThrownBy(() -> accessService.checkAccess(CODE, USER_ID))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getCode())
                .isEqualTo("020402");
    }

    @Test
    @DisplayName("停用角色不参与判定 → 020402")
    void checkAccess_disabledRoleIgnored_throws020402() {
        mockDbWorkspace(workspace(WorkspaceConstants.STATUS_ENABLED));
        when(systemRoleMapper.selectByUserId(USER_ID))
                .thenReturn(List.of(role("super_admin", "DISABLED", "ALL", null)));
        when(workspaceMemberMapper.selectEffectiveRole(CODE, USER_ID)).thenReturn(null);

        assertThatThrownBy(() -> accessService.checkAccess(CODE, USER_ID))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getCode())
                .isEqualTo("020402");
    }

    // ===== isEffectiveMember =====

    @Test
    @DisplayName("有效成员判定：角色非空为 true，参数为空为 false")
    void isEffectiveMember_variousInputs() {
        when(workspaceMemberMapper.selectEffectiveRole(CODE, USER_ID)).thenReturn("REPORTER");

        assertThat(accessService.isEffectiveMember(CODE, USER_ID)).isTrue();
        assertThat(accessService.isEffectiveMember(null, USER_ID)).isFalse();
        assertThat(accessService.isEffectiveMember(CODE, null)).isFalse();
    }
}
