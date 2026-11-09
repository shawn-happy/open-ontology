package com.prism.platform.aspect;

import com.prism.common.context.RequestContextHolder;
import com.prism.common.enums.WorkspaceRole;
import com.prism.common.exception.BusinessException;
import com.prism.common.security.SecurityUtils;
import com.prism.platform.entity.SystemRole;
import com.prism.platform.mapper.SystemRoleMapper;
import com.prism.platform.security.annotation.RequireWorkspaceRole;
import com.prism.platform.service.WorkspaceMemberService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
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
 * 空间角色鉴权切面测试（四步判定：内置超管 / 上下文缺失 / super_admin / 等级校验）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("空间角色鉴权切面")
class WorkspaceRoleAspectTest {

    private static final String CODE = "SMART-MFG";
    private static final Long USER_ID = 100L;
    private static final Object PROCEED_RESULT = new Object();

    @Mock
    private WorkspaceMemberService workspaceMemberService;

    @Mock
    private SystemRoleMapper systemRoleMapper;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @InjectMocks
    private WorkspaceRoleAspect aspect;

    private MockedStatic<SecurityUtils> securityUtils;

    private RequireWorkspaceRole annotation;

    @BeforeEach
    void setUp() throws Throwable {
        securityUtils = Mockito.mockStatic(SecurityUtils.class);
        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(USER_ID);
        RequestContextHolder.setWorkspaceCode(CODE);
        when(joinPoint.proceed()).thenReturn(PROCEED_RESULT);
        annotation = requireRole(WorkspaceRole.DEVELOPER);
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.clear();
        securityUtils.close();
    }

    private RequireWorkspaceRole requireRole(WorkspaceRole role) {
        RequireWorkspaceRole ann = Mockito.mock(RequireWorkspaceRole.class);
        when(ann.value()).thenReturn(role);
        return ann;
    }

    private SystemRole role(String code, String status) {
        SystemRole r = new SystemRole();
        r.setCode(code);
        r.setStatus(status);
        return r;
    }

    private String codeOf(Throwable e) {
        return ((BusinessException) e).getCode();
    }

    @Test
    @DisplayName("内置超管（userId=1）直接放行，不查上下文与角色")
    void check_builtinAdmin_proceeds() throws Throwable {
        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
        RequestContextHolder.clear();

        Object result = aspect.checkWorkspaceRole(joinPoint, annotation);

        assertThat(result).isSameAs(PROCEED_RESULT);
        verify(systemRoleMapper, never()).selectByUserId(anyLong());
        verify(workspaceMemberService, never()).getEffectiveRole(anyString(), anyLong());
    }

    @Test
    @DisplayName("空间上下文缺失 → 020401")
    void check_nullWorkspaceCode_throws020401() {
        RequestContextHolder.clear();

        assertThatThrownBy(() -> aspect.checkWorkspaceRole(joinPoint, annotation))
                .isInstanceOf(BusinessException.class)
                .extracting(this::codeOf)
                .isEqualTo("020401");
    }

    @Test
    @DisplayName("启用 super_admin 角色放行，不查空间角色")
    void check_superAdminRole_proceeds() throws Throwable {
        when(systemRoleMapper.selectByUserId(USER_ID))
                .thenReturn(List.of(role("super_admin", "ENABLED")));

        Object result = aspect.checkWorkspaceRole(joinPoint, annotation);

        assertThat(result).isSameAs(PROCEED_RESULT);
        verify(workspaceMemberService, never()).getEffectiveRole(anyString(), anyLong());
    }

    @Test
    @DisplayName("停用 super_admin 角色不放行，进入等级校验")
    void check_disabledSuperAdmin_fallsThroughToLevelCheck() {
        when(systemRoleMapper.selectByUserId(USER_ID))
                .thenReturn(List.of(role("super_admin", "DISABLED")));
        when(workspaceMemberService.getEffectiveRole(CODE, USER_ID)).thenReturn(null);

        assertThatThrownBy(() -> aspect.checkWorkspaceRole(joinPoint, annotation))
                .isInstanceOf(BusinessException.class)
                .extracting(this::codeOf)
                .isEqualTo("020403");
    }

    @Test
    @DisplayName("有效角色等级不足（REPORTER < DEVELOPER）→ 020403")
    void check_insufficientLevel_throws020403() {
        when(systemRoleMapper.selectByUserId(USER_ID)).thenReturn(Collections.emptyList());
        when(workspaceMemberService.getEffectiveRole(CODE, USER_ID)).thenReturn("REPORTER");

        assertThatThrownBy(() -> aspect.checkWorkspaceRole(joinPoint, annotation))
                .isInstanceOf(BusinessException.class)
                .extracting(this::codeOf)
                .isEqualTo("020403");
    }

    @Test
    @DisplayName("无有效角色（null）→ 020403")
    void check_noEffectiveRole_throws020403() {
        when(systemRoleMapper.selectByUserId(USER_ID)).thenReturn(Collections.emptyList());
        when(workspaceMemberService.getEffectiveRole(CODE, USER_ID)).thenReturn(null);

        assertThatThrownBy(() -> aspect.checkWorkspaceRole(joinPoint, annotation))
                .isInstanceOf(BusinessException.class)
                .extracting(this::codeOf)
                .isEqualTo("020403");
    }

    @Test
    @DisplayName("有效角色等级达标（MAINTAINER ≥ DEVELOPER）→ 放行")
    void check_sufficientLevel_proceeds() throws Throwable {
        when(systemRoleMapper.selectByUserId(USER_ID)).thenReturn(Collections.emptyList());
        when(workspaceMemberService.getEffectiveRole(CODE, USER_ID)).thenReturn("MAINTAINER");

        Object result = aspect.checkWorkspaceRole(joinPoint, annotation);

        assertThat(result).isSameAs(PROCEED_RESULT);
    }

    @Test
    @DisplayName("自定义角色视为 DEVELOPER 等级，满足 DEVELOPER 要求")
    void check_customRole_treatedAsDeveloperLevel_proceeds() throws Throwable {
        when(systemRoleMapper.selectByUserId(USER_ID)).thenReturn(Collections.emptyList());
        when(workspaceMemberService.getEffectiveRole(CODE, USER_ID)).thenReturn("MY_CUSTOM_ROLE");

        Object result = aspect.checkWorkspaceRole(joinPoint, requireRole(WorkspaceRole.DEVELOPER));

        assertThat(result).isSameAs(PROCEED_RESULT);
    }

    @Test
    @DisplayName("自定义角色（level=3）不满足 MAINTAINER（level=4）要求 → 020403")
    void check_customRoleBelowMaintainer_throws020403() {
        when(systemRoleMapper.selectByUserId(USER_ID)).thenReturn(Collections.emptyList());
        when(workspaceMemberService.getEffectiveRole(CODE, USER_ID)).thenReturn("MY_CUSTOM_ROLE");

        assertThatThrownBy(
                () -> aspect.checkWorkspaceRole(joinPoint, requireRole(WorkspaceRole.MAINTAINER)))
                .isInstanceOf(BusinessException.class)
                .extracting(this::codeOf)
                .isEqualTo("020403");
    }

    @Test
    @DisplayName("userId 为空时 super_admin 判定跳过，无角色 → 020403")
    void check_nullUserId_throws020403() {
        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(null);
        when(workspaceMemberService.getEffectiveRole(CODE, null)).thenReturn(null);

        assertThatThrownBy(() -> aspect.checkWorkspaceRole(joinPoint, annotation))
                .isInstanceOf(BusinessException.class)
                .extracting(this::codeOf)
                .isEqualTo("020403");
        verify(systemRoleMapper, never()).selectByUserId(anyLong());
    }
}
