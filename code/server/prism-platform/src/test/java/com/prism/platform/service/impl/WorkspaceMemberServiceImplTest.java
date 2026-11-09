package com.prism.platform.service.impl;

import com.prism.common.exception.BusinessException;
import com.prism.common.security.SecurityUtils;
import com.prism.platform.constants.WorkspaceConstants;
import com.prism.platform.dto.MemberRemoveDTO;
import com.prism.platform.dto.MemberRoleDTO;
import com.prism.platform.dto.WorkspaceAssignDTO;
import com.prism.platform.dto.WorkspaceRemoveDeptDTO;
import com.prism.platform.entity.SystemDept;
import com.prism.platform.entity.SystemRole;
import com.prism.platform.entity.Workspace;
import com.prism.platform.entity.WorkspaceMember;
import com.prism.platform.mapper.SystemDeptMapper;
import com.prism.platform.mapper.SystemRoleMapper;
import com.prism.platform.mapper.WorkspaceDeptMapper;
import com.prism.platform.mapper.WorkspaceMapper;
import com.prism.platform.mapper.WorkspaceMemberMapper;
import com.prism.platform.service.support.WorkspaceCacheSupport;
import com.prism.platform.service.support.WorkspaceRoleChecker;
import com.prism.platform.vo.AssignResultVO;
import com.prism.platform.vo.MemberStatsVO;
import com.prism.platform.vo.WorkspaceRoleVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 空间成员分配服务测试（分配幂等 / 角色调整全分支 / 移除三分支 / 统计）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("空间成员分配服务")
class WorkspaceMemberServiceImplTest {

    private static final String CODE = "SMART-MFG";
    private static final Long WS_ID = 1L;
    private static final Long OPERATOR_ID = 20L;
    private static final Long TARGET_ID = 100L;

    @Mock
    private WorkspaceMapper workspaceMapper;

    @Mock
    private WorkspaceMemberMapper workspaceMemberMapper;

    @Mock
    private WorkspaceDeptMapper workspaceDeptMapper;

    @Mock
    private SystemRoleMapper systemRoleMapper;

    @Mock
    private SystemDeptMapper systemDeptMapper;

    @Spy
    private WorkspaceRoleChecker roleChecker = new WorkspaceRoleChecker();

    @Mock
    private WorkspaceCacheSupport cacheSupport;

    @InjectMocks
    private WorkspaceMemberServiceImpl memberService;

    private MockedStatic<SecurityUtils> securityUtils;

    @BeforeEach
    void setUp() {
        securityUtils = Mockito.mockStatic(SecurityUtils.class);
        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(OPERATOR_ID);
        // 默认：操作人非超管（无系统角色）
        when(systemRoleMapper.selectByUserId(OPERATOR_ID)).thenReturn(Collections.emptyList());
        when(systemRoleMapper.selectCustomWorkspaceRoles(anyString())).thenReturn(Collections.emptyList());
    }

    @AfterEach
    void tearDown() {
        securityUtils.close();
    }

    private Workspace workspace(String status) {
        Workspace ws = new Workspace();
        ws.setId(WS_ID);
        ws.setCode(CODE);
        ws.setStatus(status);
        ws.setOwnerUserId(999L);
        return ws;
    }

    private void mockEnabledWorkspace() {
        when(workspaceMapper.selectById(WS_ID)).thenReturn(workspace(WorkspaceConstants.STATUS_ENABLED));
        when(workspaceMapper.selectByCode(CODE)).thenReturn(workspace(WorkspaceConstants.STATUS_ENABLED));
    }

    private WorkspaceMember member(Long id, Long userId, String role, String source) {
        WorkspaceMember m = new WorkspaceMember();
        m.setId(id);
        m.setUserId(userId);
        m.setWorkspaceCode(CODE);
        m.setWorkspaceRole(role);
        m.setSource(source);
        return m;
    }

    private WorkspaceAssignDTO assignUsersDTO(Long... userIds) {
        WorkspaceAssignDTO dto = new WorkspaceAssignDTO();
        dto.setUserIds(List.of(userIds));
        return dto;
    }

    private String codeOf(Throwable t) {
        return ((BusinessException) t).getCode();
    }

    // ===== assignUsers =====

    @Nested
    @DisplayName("批量分配用户")
    class AssignUsers {

        @Test
        @DisplayName("空间已停用 → 020104")
        void assignUsers_workspaceDisabled_throws020104() {
            when(workspaceMapper.selectById(WS_ID)).thenReturn(workspace("DISABLED"));

            assertThatThrownBy(() -> memberService.assignUsers(WS_ID, assignUsersDTO(TARGET_ID)))
                    .isInstanceOf(BusinessException.class)
                    .extracting(WorkspaceMemberServiceImplTest.this::codeOf)
                    .isEqualTo("020104");
        }

        @Test
        @DisplayName("角色非法 → 020301")
        void assignUsers_invalidRole_throws020301() {
            mockEnabledWorkspace();
            WorkspaceAssignDTO dto = assignUsersDTO(TARGET_ID);
            dto.setWorkspaceRole("NOT_EXIST");

            assertThatThrownBy(() -> memberService.assignUsers(WS_ID, dto))
                    .isInstanceOf(BusinessException.class)
                    .extracting(WorkspaceMemberServiceImplTest.this::codeOf)
                    .isEqualTo("020301");
        }

        @Test
        @DisplayName("新用户 → 插入 DIRECT 记录（默认角色 REPORTER），清单内重复只处理一次")
        void assignUsers_newUser_insertsDirectWithDefaultRole() {
            mockEnabledWorkspace();
            when(workspaceMemberMapper.selectOne(any())).thenReturn(null);
            when(workspaceMemberMapper.selectEffectiveRole(CODE, TARGET_ID)).thenReturn(null);

            AssignResultVO result = memberService.assignUsers(WS_ID,
                    assignUsersDTO(TARGET_ID, TARGET_ID));

            assertThat(result.getSuccessCount()).isEqualTo(1);
            ArgumentCaptor<WorkspaceMember> captor = ArgumentCaptor.forClass(WorkspaceMember.class);
            verify(workspaceMemberMapper).insert(captor.capture());
            assertThat(captor.getValue().getSource()).isEqualTo("DIRECT");
            assertThat(captor.getValue().getWorkspaceRole()).isEqualTo("REPORTER");
            assertThat(captor.getValue().getUserId()).isEqualTo(TARGET_ID);
            verify(cacheSupport).evictMember(CODE, TARGET_ID);
            verify(cacheSupport).evictOverview();
        }

        @Test
        @DisplayName("已是直接分配成员 → 幂等跳过")
        void assignUsers_existingDirect_skips() {
            mockEnabledWorkspace();
            when(workspaceMemberMapper.selectOne(any()))
                    .thenReturn(member(11L, TARGET_ID, "DEVELOPER", "DIRECT"));

            AssignResultVO result = memberService.assignUsers(WS_ID, assignUsersDTO(TARGET_ID));

            assertThat(result.getSkipCount()).isEqualTo(1);
            assertThat(result.getSuccessCount()).isZero();
            verify(workspaceMemberMapper, never()).insert(any(WorkspaceMember.class));
        }

        @Test
        @DisplayName("部门来源有效成员 → 幂等跳过")
        void assignUsers_deptMember_skips() {
            mockEnabledWorkspace();
            when(workspaceMemberMapper.selectOne(any())).thenReturn(null);
            when(workspaceMemberMapper.selectEffectiveRole(CODE, TARGET_ID)).thenReturn("REPORTER");

            AssignResultVO result = memberService.assignUsers(WS_ID, assignUsersDTO(TARGET_ID));

            assertThat(result.getSkipCount()).isEqualTo(1);
            verify(workspaceMemberMapper, never()).insert(any(WorkspaceMember.class));
        }

        @Test
        @DisplayName("存在 EXCLUDED 排除记录 → 改写为 DIRECT 并级联清理 PERSONAL")
        void assignUsers_excludedExists_rewritesToDirect() {
            mockEnabledWorkspace();
            WorkspaceMember excluded = member(12L, TARGET_ID, "REPORTER", "EXCLUDED");
            // 第一次 selectOne 查 DIRECT → null；第二次查 EXCLUDED → 命中
            when(workspaceMemberMapper.selectOne(any())).thenReturn(null, excluded);
            when(workspaceMemberMapper.selectEffectiveRole(CODE, TARGET_ID)).thenReturn(null);

            AssignResultVO result = memberService.assignUsers(WS_ID, assignUsersDTO(TARGET_ID));

            assertThat(result.getSuccessCount()).isEqualTo(1);
            verify(workspaceMemberMapper).update(isNull(), any());
            verify(workspaceMemberMapper).delete(any());
            verify(workspaceMemberMapper, never()).insert(any(WorkspaceMember.class));
        }
    }

    // ===== assignDepts / removeDept =====

    @Nested
    @DisplayName("部门分配与移除")
    class AssignDepts {

        private WorkspaceAssignDTO deptsDTO(Long... deptIds) {
            WorkspaceAssignDTO dto = new WorkspaceAssignDTO();
            dto.setDeptIds(List.of(deptIds));
            return dto;
        }

        @Test
        @DisplayName("部门不存在 → 020205")
        void assignDepts_deptNotFound_throws020205() {
            mockEnabledWorkspace();
            when(systemDeptMapper.selectById(anyLong())).thenReturn(null);

            assertThatThrownBy(() -> memberService.assignDepts(WS_ID, deptsDTO(5L)))
                    .isInstanceOf(BusinessException.class)
                    .extracting(WorkspaceMemberServiceImplTest.this::codeOf)
                    .isEqualTo("020205");
        }

        @Test
        @DisplayName("部门已分配 → 幂等跳过；新部门插入（默认角色 REPORTER）")
        void assignDepts_mixedExistingAndNew() {
            mockEnabledWorkspace();
            SystemDept dept = new SystemDept();
            dept.setId(5L);
            when(systemDeptMapper.selectById(5L)).thenReturn(dept);
            when(workspaceDeptMapper.selectCount(any())).thenReturn(1L, 0L);

            AssignResultVO result = memberService.assignDepts(WS_ID, deptsDTO(5L, 5L, 6L));

            // 5L 首次已分配跳过，5L 重复项被去重，6L 部门不存在 → 020205
            assertThat(result.getSkipCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("移除部门：无分配记录幂等返回")
        void removeDept_noRecord_idempotent() {
            when(workspaceMapper.selectById(WS_ID)).thenReturn(workspace("ENABLED"));
            when(workspaceDeptMapper.selectOne(any())).thenReturn(null);
            WorkspaceRemoveDeptDTO dto = new WorkspaceRemoveDeptDTO();
            dto.setDeptId(5L);

            memberService.removeDept(WS_ID, dto);

            verify(workspaceDeptMapper, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("移除部门：PERSONAL 记录无其他部门覆盖 → 删除；有覆盖 → 改挂新部门")
        void removeDept_cleansDerivedRecords() {
            when(workspaceMapper.selectById(WS_ID)).thenReturn(workspace("ENABLED"));
            com.prism.platform.entity.WorkspaceDept record =
                    new com.prism.platform.entity.WorkspaceDept();
            record.setId(7L);
            record.setDeptId(5L);
            when(workspaceDeptMapper.selectOne(any())).thenReturn(record);

            WorkspaceMember noCover = member(21L, 101L, "DEVELOPER", "PERSONAL");
            WorkspaceMember reHang = member(22L, 102L, "MAINTAINER", "EXCLUDED");
            when(workspaceMemberMapper.selectList(any())).thenReturn(List.of(noCover, reHang));
            when(workspaceDeptMapper.selectCoveringDeptIds(eq(CODE), eq(101L), eq(5L)))
                    .thenReturn(Collections.emptyList());
            when(workspaceDeptMapper.selectCoveringDeptIds(eq(CODE), eq(102L), eq(5L)))
                    .thenReturn(List.of(8L));

            WorkspaceRemoveDeptDTO dto = new WorkspaceRemoveDeptDTO();
            dto.setDeptId(5L);
            memberService.removeDept(WS_ID, dto);

            verify(workspaceDeptMapper).deleteById(7L);
            verify(workspaceMemberMapper).deleteById(21L);
            ArgumentCaptor<WorkspaceMember> captor = ArgumentCaptor.forClass(WorkspaceMember.class);
            verify(workspaceMemberMapper).updateById(captor.capture());
            assertThat(captor.getValue().getId()).isEqualTo(22L);
            assertThat(captor.getValue().getDeptId()).isEqualTo(8L);
            verify(cacheSupport).evictMember(CODE, 101L);
            verify(cacheSupport).evictMember(CODE, 102L);
        }
    }

    // ===== 查询与缓存 =====

    @Nested
    @DisplayName("查询与角色缓存")
    class Queries {

        @Test
        @DisplayName("成员统计：deptCount = total - direct")
        void getMemberStats_computesDeptCount() {
            when(workspaceMemberMapper.countEffectiveMembers(CODE, null, null)).thenReturn(12);
            when(workspaceMemberMapper.countEffectiveMembers(CODE, "DIRECT", null)).thenReturn(5);
            when(workspaceMemberMapper.countEffectiveMembers(CODE, null, "OWNER")).thenReturn(2);

            MemberStatsVO stats = memberService.getMemberStats(CODE);

            assertThat(stats.getTotalMembers()).isEqualTo(12);
            assertThat(stats.getDirectCount()).isEqualTo(5);
            assertThat(stats.getDeptCount()).isEqualTo(7);
            assertThat(stats.getOwnerCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("角色列表：内置五级 + 自定义空间角色（level=3）")
        void listRoles_builtinPlusCustom() {
            SystemRole custom = new SystemRole();
            custom.setCode("DATA_STEWARD");
            custom.setName("数据管家");
            custom.setRemark("自定义");
            when(systemRoleMapper.selectCustomWorkspaceRoles(CODE)).thenReturn(List.of(custom));

            List<WorkspaceRoleVO> roles = memberService.listRoles(CODE);

            assertThat(roles).hasSize(6);
            assertThat(roles).filteredOn(WorkspaceRoleVO::getBuiltin).hasSize(5);
            WorkspaceRoleVO customVo = roles.get(5);
            assertThat(customVo.getCode()).isEqualTo("DATA_STEWARD");
            assertThat(customVo.getLevel()).isEqualTo(3);
            assertThat(customVo.getBuiltin()).isFalse();
        }

        @Test
        @DisplayName("有效角色：缓存 NONE → null；缓存角色码直接返回；未命中回源并写缓存")
        void getEffectiveRole_cacheSemantics() {
            when(cacheSupport.getMemberRole(CODE, 1L)).thenReturn(WorkspaceConstants.MEMBER_CACHE_NONE);
            assertThat(memberService.getEffectiveRole(CODE, 1L)).isNull();

            when(cacheSupport.getMemberRole(CODE, 2L)).thenReturn("DEVELOPER");
            assertThat(memberService.getEffectiveRole(CODE, 2L)).isEqualTo("DEVELOPER");

            when(cacheSupport.getMemberRole(CODE, 3L)).thenReturn(null);
            when(workspaceMemberMapper.selectEffectiveRole(CODE, 3L)).thenReturn("OWNER");
            assertThat(memberService.getEffectiveRole(CODE, 3L)).isEqualTo("OWNER");
            verify(cacheSupport).putMemberRole(CODE, 3L, "OWNER");

            when(workspaceMemberMapper.selectEffectiveRole(CODE, 4L)).thenReturn(null);
            assertThat(memberService.getEffectiveRole(CODE, 4L)).isNull();
            verify(cacheSupport).putMemberRole(CODE, 4L, WorkspaceConstants.MEMBER_CACHE_NONE);

            assertThat(memberService.getEffectiveRole(null, 5L)).isNull();
            assertThat(memberService.getEffectiveRole(CODE, null)).isNull();
        }
    }

    // ===== updateMemberRole =====

    @Nested
    @DisplayName("分配/覆盖成员角色")
    class UpdateMemberRole {

        private MemberRoleDTO dto(String newRole) {
            MemberRoleDTO dto = new MemberRoleDTO();
            dto.setUserId(TARGET_ID);
            dto.setWorkspaceRole(newRole);
            return dto;
        }

        @Test
        @DisplayName("空间不存在 → 020103")
        void updateRole_workspaceNotFound_throws020103() {
            when(workspaceMapper.selectByCode(CODE)).thenReturn(null);

            assertThatThrownBy(() -> memberService.updateMemberRole(CODE, dto("DEVELOPER")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(WorkspaceMemberServiceImplTest.this::codeOf)
                    .isEqualTo("020103");
        }

        @Test
        @DisplayName("目标非有效成员 → 020202")
        void updateRole_targetNotMember_throws020202() {
            mockEnabledWorkspace();
            when(workspaceMemberMapper.selectEffectiveRole(CODE, TARGET_ID)).thenReturn(null);

            assertThatThrownBy(() -> memberService.updateMemberRole(CODE, dto("DEVELOPER")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(WorkspaceMemberServiceImplTest.this::codeOf)
                    .isEqualTo("020202");
        }

        @Test
        @DisplayName("空间创建人的 OWNER 角色被降级 → 020206")
        void updateRole_creatorOwnerDowngrade_throws020206() {
            Workspace ws = workspace("ENABLED");
            ws.setOwnerUserId(TARGET_ID);
            when(workspaceMapper.selectByCode(CODE)).thenReturn(ws);
            when(workspaceMemberMapper.selectEffectiveRole(CODE, TARGET_ID)).thenReturn("OWNER");

            assertThatThrownBy(() -> memberService.updateMemberRole(CODE, dto("MAINTAINER")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(WorkspaceMemberServiceImplTest.this::codeOf)
                    .isEqualTo("020206");
        }

        @Test
        @DisplayName("目标等级不低于操作人 → 020207")
        void updateRole_targetLevelNotLower_throws020207() {
            mockEnabledWorkspace();
            // 目标 MAINTAINER(4)，操作人 DEVELOPER(3)
            when(workspaceMemberMapper.selectEffectiveRole(CODE, TARGET_ID)).thenReturn("MAINTAINER");
            when(workspaceMemberMapper.selectEffectiveRole(CODE, OPERATOR_ID)).thenReturn("DEVELOPER");

            assertThatThrownBy(() -> memberService.updateMemberRole(CODE, dto("REPORTER")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(WorkspaceMemberServiceImplTest.this::codeOf)
                    .isEqualTo("020207");
        }

        @Test
        @DisplayName("非 Owner 授予 OWNER → 020208")
        void updateRole_nonOwnerGrantsOwner_throws020208() {
            mockEnabledWorkspace();
            when(workspaceMemberMapper.selectEffectiveRole(CODE, TARGET_ID)).thenReturn("REPORTER");
            when(workspaceMemberMapper.selectEffectiveRole(CODE, OPERATOR_ID)).thenReturn("OWNER");
            doThrow(new BusinessException("020208", "Owner 角色仅能由 Owner 授予"))
                    .when(roleChecker).assertCanGrant("MAINTAINER", "OWNER");
            // 操作人实际为 MAINTAINER 时触发
            when(workspaceMemberMapper.selectEffectiveRole(CODE, OPERATOR_ID)).thenReturn("MAINTAINER");

            assertThatThrownBy(() -> memberService.updateMemberRole(CODE, dto("OWNER")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(WorkspaceMemberServiceImplTest.this::codeOf)
                    .isEqualTo("020208");
        }

        @Test
        @DisplayName("降级最后一名 Owner → 020203")
        void updateRole_lastOwnerDowngrade_throws020203() {
            mockEnabledWorkspace();
            when(workspaceMemberMapper.selectEffectiveRole(CODE, TARGET_ID)).thenReturn("OWNER");
            when(workspaceMemberMapper.selectEffectiveRole(CODE, OPERATOR_ID)).thenReturn("OWNER");
            // 操作人即目标场景会被 020207 拦截，此处操作人为另一 Owner 不成立（等级相同）→ 用超管绕过等级校验
            securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(workspaceMemberMapper.selectOwnersForUpdate(CODE))
                    .thenReturn(List.of(member(31L, TARGET_ID, "OWNER", "DIRECT")));

            assertThatThrownBy(() -> memberService.updateMemberRole(CODE, dto("MAINTAINER")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(WorkspaceMemberServiceImplTest.this::codeOf)
                    .isEqualTo("020203");
        }

        @Test
        @DisplayName("DIRECT 成员调整角色 → UPDATE 记录")
        void updateRole_directMember_updatesRow() {
            mockEnabledWorkspace();
            when(workspaceMemberMapper.selectEffectiveRole(CODE, TARGET_ID)).thenReturn("REPORTER");
            when(workspaceMemberMapper.selectEffectiveRole(CODE, OPERATOR_ID)).thenReturn("OWNER");
            when(workspaceMemberMapper.selectOne(any()))
                    .thenReturn(member(41L, TARGET_ID, "REPORTER", "DIRECT"));

            memberService.updateMemberRole(CODE, dto("DEVELOPER"));

            ArgumentCaptor<WorkspaceMember> captor = ArgumentCaptor.forClass(WorkspaceMember.class);
            verify(workspaceMemberMapper).updateById(captor.capture());
            assertThat(captor.getValue().getId()).isEqualTo(41L);
            assertThat(captor.getValue().getWorkspaceRole()).isEqualTo("DEVELOPER");
            verify(cacheSupport).evictMember(CODE, TARGET_ID);
        }

        @Test
        @DisplayName("部门来源成员分配角色 → 插入 PERSONAL 记录并挂覆盖部门")
        void updateRole_deptMember_insertsPersonal() {
            mockEnabledWorkspace();
            when(workspaceMemberMapper.selectEffectiveRole(CODE, TARGET_ID)).thenReturn("REPORTER");
            when(workspaceMemberMapper.selectEffectiveRole(CODE, OPERATOR_ID)).thenReturn("OWNER");
            // 无 DIRECT、无 PERSONAL
            when(workspaceMemberMapper.selectOne(any())).thenReturn(null);
            when(workspaceDeptMapper.selectCoveringDeptIds(CODE, TARGET_ID, null)).thenReturn(List.of(6L));

            memberService.updateMemberRole(CODE, dto("DEVELOPER"));

            ArgumentCaptor<WorkspaceMember> captor = ArgumentCaptor.forClass(WorkspaceMember.class);
            verify(workspaceMemberMapper).insert(captor.capture());
            assertThat(captor.getValue().getSource()).isEqualTo("PERSONAL");
            assertThat(captor.getValue().getDeptId()).isEqualTo(6L);
            assertThat(captor.getValue().getWorkspaceRole()).isEqualTo("DEVELOPER");
        }

        @Test
        @DisplayName("部门来源成员清除个人角色（newRole=null）→ 删除 PERSONAL 记录")
        void updateRole_clearPersonal_deletesRow() {
            mockEnabledWorkspace();
            when(workspaceMemberMapper.selectEffectiveRole(CODE, TARGET_ID)).thenReturn("DEVELOPER");
            when(workspaceMemberMapper.selectEffectiveRole(CODE, OPERATOR_ID)).thenReturn("OWNER");
            when(workspaceMemberMapper.selectOne(any()))
                    .thenReturn(null, member(42L, TARGET_ID, "DEVELOPER", "PERSONAL"));

            memberService.updateMemberRole(CODE, dto(null));

            verify(workspaceMemberMapper).deleteById(42L);
            verify(workspaceMemberMapper, never()).insert(any(WorkspaceMember.class));
            verify(workspaceMemberMapper, never()).updateById(any(WorkspaceMember.class));
        }
    }

    // ===== removeMember =====

    @Nested
    @DisplayName("移除成员三分支")
    class RemoveMember {

        private MemberRemoveDTO dto() {
            MemberRemoveDTO dto = new MemberRemoveDTO();
            dto.setUserId(TARGET_ID);
            return dto;
        }

        @Test
        @DisplayName("目标非有效成员 → 020202")
        void removeMember_notMember_throws020202() {
            when(workspaceMemberMapper.selectEffectiveRole(CODE, TARGET_ID)).thenReturn(null);

            assertThatThrownBy(() -> memberService.removeMember(CODE, dto()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(WorkspaceMemberServiceImplTest.this::codeOf)
                    .isEqualTo("020202");
        }

        @Test
        @DisplayName("目标等级不低于操作人（含 Owner 自移除）→ 020207")
        void removeMember_levelDenied_throws020207() {
            when(workspaceMemberMapper.selectEffectiveRole(CODE, TARGET_ID)).thenReturn("OWNER");
            when(workspaceMemberMapper.selectEffectiveRole(CODE, OPERATOR_ID)).thenReturn("OWNER");

            assertThatThrownBy(() -> memberService.removeMember(CODE, dto()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(WorkspaceMemberServiceImplTest.this::codeOf)
                    .isEqualTo("020207");
        }

        @Test
        @DisplayName("分支一：DIRECT 成员移除 → 级联清理三类来源记录")
        void removeMember_direct_cascadesAllSources() {
            when(workspaceMemberMapper.selectEffectiveRole(CODE, TARGET_ID)).thenReturn("DEVELOPER");
            when(workspaceMemberMapper.selectEffectiveRole(CODE, OPERATOR_ID)).thenReturn("OWNER");
            when(workspaceMemberMapper.selectOne(any()))
                    .thenReturn(member(51L, TARGET_ID, "DEVELOPER", "DIRECT"));

            memberService.removeMember(CODE, dto());

            // DIRECT / PERSONAL / EXCLUDED 三次条件删除
            verify(workspaceMemberMapper, Mockito.times(3)).delete(any());
            verify(cacheSupport).evictMember(CODE, TARGET_ID);
        }

        @Test
        @DisplayName("分支一：移除 DIRECT Owner 时仅剩一名 Owner → 020203")
        void removeMember_directLastOwner_throws020203() {
            when(workspaceMemberMapper.selectEffectiveRole(CODE, TARGET_ID)).thenReturn("OWNER");
            // 超管操作绕过等级校验
            securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
            when(workspaceMemberMapper.selectOne(any()))
                    .thenReturn(member(52L, TARGET_ID, "OWNER", "DIRECT"));
            when(workspaceMemberMapper.selectOwnersForUpdate(CODE))
                    .thenReturn(List.of(member(52L, TARGET_ID, "OWNER", "DIRECT")));

            assertThatThrownBy(() -> memberService.removeMember(CODE, dto()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(WorkspaceMemberServiceImplTest.this::codeOf)
                    .isEqualTo("020203");
            verify(workspaceMemberMapper, never()).delete(any());
        }

        @Test
        @DisplayName("分支二：部门来源已覆盖个人角色且仍被部门覆盖 → 仅删 PERSONAL")
        void removeMember_personalWithCoveringDept_deletesPersonalOnly() {
            when(workspaceMemberMapper.selectEffectiveRole(CODE, TARGET_ID)).thenReturn("DEVELOPER");
            when(workspaceMemberMapper.selectEffectiveRole(CODE, OPERATOR_ID)).thenReturn("OWNER");
            when(workspaceMemberMapper.selectOne(any()))
                    .thenReturn(null, member(53L, TARGET_ID, "DEVELOPER", "PERSONAL"));
            when(workspaceDeptMapper.selectCoveringDeptIds(CODE, TARGET_ID, null)).thenReturn(List.of(6L));

            memberService.removeMember(CODE, dto());

            verify(workspaceMemberMapper).deleteById(53L);
            verify(workspaceMemberMapper, never()).delete(any());
        }

        @Test
        @DisplayName("分支三：部门来源未覆盖个人角色 → 020209 兜底拒绝")
        void removeMember_deptSourceNotOverridden_throws020209() {
            when(workspaceMemberMapper.selectEffectiveRole(CODE, TARGET_ID)).thenReturn("REPORTER");
            when(workspaceMemberMapper.selectEffectiveRole(CODE, OPERATOR_ID)).thenReturn("OWNER");
            when(workspaceMemberMapper.selectOne(any())).thenReturn(null);
            when(workspaceDeptMapper.selectCoveringDeptIds(CODE, TARGET_ID, null)).thenReturn(List.of(6L));

            assertThatThrownBy(() -> memberService.removeMember(CODE, dto()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(WorkspaceMemberServiceImplTest.this::codeOf)
                    .isEqualTo("020209");
        }
    }
}
