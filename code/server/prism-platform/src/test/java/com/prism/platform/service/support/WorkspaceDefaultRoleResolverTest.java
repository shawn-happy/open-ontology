package com.prism.platform.service.support;

import com.prism.common.enums.WorkspaceRole;
import com.prism.platform.entity.SystemDept;
import com.prism.platform.entity.SystemRole;
import com.prism.platform.entity.SystemUserDept;
import com.prism.platform.mapper.SystemDeptMapper;
import com.prism.platform.mapper.SystemRoleMapper;
import com.prism.platform.mapper.SystemUserDeptMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * 创建人默认角色判定器测试（PRD FR-19 / FR-23，US-008）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("创建人默认角色判定器")
class WorkspaceDefaultRoleResolverTest {

    private static final Long USER_ID = 100L;
    private static final Long DEPT_ID = 10L;

    @Mock
    private SystemUserDeptMapper systemUserDeptMapper;

    @Mock
    private SystemDeptMapper systemDeptMapper;

    @Mock
    private SystemRoleMapper systemRoleMapper;

    @InjectMocks
    private WorkspaceDefaultRoleResolver resolver;

    private SystemUserDept userDept;
    private SystemDept dept;

    @BeforeEach
    void setUp() {
        userDept = new SystemUserDept();
        userDept.setUserId(USER_ID);
        userDept.setDeptId(DEPT_ID);

        dept = new SystemDept();
        dept.setId(DEPT_ID);
    }

    private SystemRole role(String code, String status) {
        SystemRole role = new SystemRole();
        role.setCode(code);
        role.setStatus(status);
        return role;
    }

    @ParameterizedTest(name = "leader={0}, 系统角色={1} → {2}")
    @CsvSource({
            "true,  super_admin,     OWNER",
            "true,  workspace_admin, OWNER",
            "true,  normal,          OWNER",
            "false, super_admin,     OWNER",
            "false, workspace_admin, OWNER",
            "false, normal,          MAINTAINER",
    })
    @DisplayName("部门 leader 或超管/空间管理员 → OWNER，否则 MAINTAINER")
    void resolve_leaderAndRoleCombination_expectedDefaultRole(boolean leader, String roleCode,
            WorkspaceRole expected) {
        dept.setLeaderId(leader ? USER_ID : 999L);
        when(systemUserDeptMapper.selectByUserId(USER_ID)).thenReturn(userDept);
        when(systemDeptMapper.selectById(DEPT_ID)).thenReturn(dept);
        when(systemRoleMapper.selectByUserId(USER_ID))
                .thenReturn(List.of(role(roleCode, "ENABLED")));

        assertThat(resolver.resolve(USER_ID)).isEqualTo(expected);
    }

    @Test
    @DisplayName("userId 为空 → MAINTAINER 兜底")
    void resolve_nullUserId_returnsMaintainer() {
        assertThat(resolver.resolve(null)).isEqualTo(WorkspaceRole.MAINTAINER);
    }

    @Test
    @DisplayName("无部门记录时按系统角色判定")
    void resolve_noDept_fallbackToSystemRole() {
        when(systemUserDeptMapper.selectByUserId(USER_ID)).thenReturn(null);
        when(systemRoleMapper.selectByUserId(USER_ID))
                .thenReturn(List.of(role("workspace_admin", "ENABLED")));

        assertThat(resolver.resolve(USER_ID)).isEqualTo(WorkspaceRole.OWNER);
    }

    @Test
    @DisplayName("系统角色已停用 → 不参与判定，降级 MAINTAINER")
    void resolve_disabledRole_ignored() {
        when(systemUserDeptMapper.selectByUserId(USER_ID)).thenReturn(null);
        when(systemRoleMapper.selectByUserId(USER_ID))
                .thenReturn(List.of(role("super_admin", "DISABLED")));

        assertThat(resolver.resolve(USER_ID)).isEqualTo(WorkspaceRole.MAINTAINER);
    }

    @Test
    @DisplayName("部门不存在时不抛异常，按系统角色判定")
    void resolve_deptMissing_fallbackToSystemRole() {
        when(systemUserDeptMapper.selectByUserId(USER_ID)).thenReturn(userDept);
        when(systemDeptMapper.selectById(anyLong())).thenReturn(null);
        when(systemRoleMapper.selectByUserId(USER_ID)).thenReturn(List.of(role("normal", "ENABLED")));

        assertThat(resolver.resolve(USER_ID)).isEqualTo(WorkspaceRole.MAINTAINER);
    }
}
