package com.prism.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.prism.common.enums.WorkspaceRole;
import com.prism.common.exception.BusinessException;
import com.prism.common.security.SecurityUtils;
import com.prism.platform.dto.WorkspaceSaveDTO;
import com.prism.platform.entity.SystemRole;
import com.prism.platform.entity.SystemUser;
import com.prism.platform.entity.Workspace;
import com.prism.platform.entity.WorkspaceMember;
import com.prism.platform.mapper.SystemDeptMapper;
import com.prism.platform.mapper.SystemRoleMapper;
import com.prism.platform.mapper.SystemUserDeptMapper;
import com.prism.platform.mapper.SystemUserMapper;
import com.prism.platform.mapper.WorkspaceDeptMapper;
import com.prism.platform.mapper.WorkspaceMapper;
import com.prism.platform.mapper.WorkspaceMemberMapper;
import com.prism.platform.service.support.WorkspaceCacheSupport;
import com.prism.platform.service.support.WorkspaceDefaultRoleResolver;
import com.prism.platform.vo.WorkspaceOverviewVO;
import com.prism.platform.vo.WorkspaceSimpleVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 工作空间管理服务测试（创建/编辑/删除/可访问列表/概览）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("工作空间管理服务")
class WorkspaceServiceImplTest {

    private static final String CODE = "SMART-MFG";
    private static final Long OWNER_ID = 10L;
    private static final Long CREATOR_ID = 20L;

    @Mock
    private WorkspaceMapper workspaceMapper;

    @Mock
    private WorkspaceMemberMapper workspaceMemberMapper;

    @Mock
    private WorkspaceDeptMapper workspaceDeptMapper;

    @Mock
    private SystemUserMapper systemUserMapper;

    @Mock
    private SystemUserDeptMapper systemUserDeptMapper;

    @Mock
    private SystemDeptMapper systemDeptMapper;

    @Mock
    private SystemRoleMapper systemRoleMapper;

    @Mock
    private WorkspaceDefaultRoleResolver defaultRoleResolver;

    @Mock
    private WorkspaceCacheSupport cacheSupport;

    @InjectMocks
    private WorkspaceServiceImpl workspaceService;

    private MockedStatic<SecurityUtils> securityUtils;

    @BeforeEach
    void setUp() {
        securityUtils = Mockito.mockStatic(SecurityUtils.class);
        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(CREATOR_ID);
    }

    @AfterEach
    void tearDown() {
        securityUtils.close();
    }

    private SystemUser enabledUser(Long id) {
        SystemUser user = new SystemUser();
        user.setId(id);
        user.setName("用户" + id);
        user.setStatus("ENABLED");
        return user;
    }

    private Workspace workspace(Long id, String code, String status, Long ownerUserId) {
        Workspace ws = new Workspace();
        ws.setId(id);
        ws.setCode(code);
        ws.setName("智能制造空间");
        ws.setStatus(status);
        ws.setOwnerUserId(ownerUserId);
        return ws;
    }

    private WorkspaceSaveDTO createDTO(String code, String name, Long ownerUserId) {
        WorkspaceSaveDTO dto = new WorkspaceSaveDTO();
        dto.setCode(code);
        dto.setName(name);
        dto.setOwnerUserId(ownerUserId);
        return dto;
    }

    // ===== create =====

    @Test
    @DisplayName("创建空间：编码为保留字 WORKSPACE → 020107")
    void create_reservedCode_throws020107() {
        assertThatThrownBy(() -> workspaceService.create(createDTO("WORKSPACE", "空间", OWNER_ID)))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getCode())
                .isEqualTo("020107");
    }

    @Test
    @DisplayName("创建空间：编码已存在 → 020101")
    void create_codeExists_throws020101() {
        when(workspaceMapper.selectByCode(CODE)).thenReturn(workspace(1L, CODE, "ENABLED", OWNER_ID));

        assertThatThrownBy(() -> workspaceService.create(createDTO(CODE, "空间", OWNER_ID)))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getCode())
                .isEqualTo("020101");
    }

    @Test
    @DisplayName("创建空间：名称已存在 → 020108")
    void create_nameExists_throws020108() {
        when(workspaceMapper.selectByCode(CODE)).thenReturn(null);
        when(workspaceMapper.selectCount(any())).thenReturn(1L);

        assertThatThrownBy(() -> workspaceService.create(createDTO(CODE, "智能制造空间", OWNER_ID)))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getCode())
                .isEqualTo("020108");
    }

    @Test
    @DisplayName("创建空间：空间管理员已停用 → 999904")
    void create_ownerDisabled_throws999904() {
        when(workspaceMapper.selectByCode(CODE)).thenReturn(null);
        when(workspaceMapper.selectCount(any())).thenReturn(0L);
        SystemUser disabled = enabledUser(OWNER_ID);
        disabled.setStatus("DISABLED");
        when(systemUserMapper.selectById(OWNER_ID)).thenReturn(disabled);

        assertThatThrownBy(() -> workspaceService.create(createDTO(CODE, "空间", OWNER_ID)))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getCode())
                .isEqualTo("999904");
    }

    @Test
    @DisplayName("创建空间成功：Owner 写 DIRECT+OWNER，创建人按默认角色判定另写 DIRECT，并失效缓存")
    void create_success_insertsOwnerAndCreatorMembers() {
        when(workspaceMapper.selectByCode(CODE)).thenReturn(null);
        when(workspaceMapper.selectCount(any())).thenReturn(0L);
        when(systemUserMapper.selectById(OWNER_ID)).thenReturn(enabledUser(OWNER_ID));
        when(defaultRoleResolver.resolve(CREATOR_ID)).thenReturn(WorkspaceRole.MAINTAINER);

        workspaceService.create(createDTO(CODE, "新空间", OWNER_ID));

        ArgumentCaptor<WorkspaceMember> captor = ArgumentCaptor.forClass(WorkspaceMember.class);
        verify(workspaceMemberMapper, Mockito.times(2)).insert(captor.capture());
        List<WorkspaceMember> inserted = captor.getAllValues();

        assertThat(inserted.get(0).getUserId()).isEqualTo(OWNER_ID);
        assertThat(inserted.get(0).getWorkspaceRole()).isEqualTo("OWNER");
        assertThat(inserted.get(0).getSource()).isEqualTo("DIRECT");
        assertThat(inserted.get(1).getUserId()).isEqualTo(CREATOR_ID);
        assertThat(inserted.get(1).getWorkspaceRole()).isEqualTo("MAINTAINER");
        assertThat(inserted.get(1).getSource()).isEqualTo("DIRECT");

        verify(cacheSupport).evictOverview();
        verify(cacheSupport).evictAccessible(OWNER_ID);
        verify(cacheSupport).evictAccessible(CREATOR_ID);
    }

    @Test
    @DisplayName("创建空间：创建人即空间管理员时只写一条成员记录")
    void create_creatorIsOwner_insertsSingleMember() {
        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(OWNER_ID);
        when(workspaceMapper.selectByCode(CODE)).thenReturn(null);
        when(workspaceMapper.selectCount(any())).thenReturn(0L);
        when(systemUserMapper.selectById(OWNER_ID)).thenReturn(enabledUser(OWNER_ID));

        workspaceService.create(createDTO(CODE, "新空间", OWNER_ID));

        verify(workspaceMemberMapper, Mockito.times(1)).insert(any(WorkspaceMember.class));
        verify(defaultRoleResolver, never()).resolve(anyLong());
    }

    // ===== update =====

    private WorkspaceSaveDTO updateDTO(Long id, Long newOwnerId) {
        WorkspaceSaveDTO dto = new WorkspaceSaveDTO();
        dto.setId(id);
        dto.setOwnerUserId(newOwnerId);
        return dto;
    }

    @Test
    @DisplayName("编辑空间：名称与其他空间重复 → 020108")
    void update_nameConflict_throws020108() {
        when(workspaceMapper.selectById(1L)).thenReturn(workspace(1L, CODE, "ENABLED", OWNER_ID));
        when(workspaceMapper.selectCount(any())).thenReturn(1L);
        WorkspaceSaveDTO dto = updateDTO(1L, null);
        dto.setName("智能制造空间");

        assertThatThrownBy(() -> workspaceService.update(dto))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getCode())
                .isEqualTo("020108");
    }

    @Test
    @DisplayName("编辑空间：更换 Owner → 旧 Owner DIRECT 降 MAINTAINER，新 Owner 升 OWNER")
    void update_ownerChanged_demotesOldAndPromotesNew() {
        Workspace ws = workspace(1L, CODE, "ENABLED", OWNER_ID);
        when(workspaceMapper.selectById(1L)).thenReturn(ws);
        when(workspaceMapper.selectCount(any())).thenReturn(0L);
        Long newOwnerId = 30L;
        when(systemUserMapper.selectById(newOwnerId)).thenReturn(enabledUser(newOwnerId));

        WorkspaceMember oldDirect = new WorkspaceMember();
        oldDirect.setId(11L);
        oldDirect.setUserId(OWNER_ID);
        oldDirect.setWorkspaceRole("OWNER");
        oldDirect.setSource("DIRECT");
        // update 流程仅查旧 Owner 的 DIRECT 记录（新 Owner upsert 时第二次 selectOne 返回 null → insert）
        when(workspaceMemberMapper.selectOne(any())).thenReturn(oldDirect, (WorkspaceMember) null);

        workspaceService.update(updateDTO(1L, newOwnerId));

        ArgumentCaptor<WorkspaceMember> updateCaptor = ArgumentCaptor.forClass(WorkspaceMember.class);
        verify(workspaceMemberMapper, Mockito.atLeastOnce()).updateById(updateCaptor.capture());
        WorkspaceMember demote = updateCaptor.getAllValues().get(0);
        assertThat(demote.getId()).isEqualTo(11L);
        assertThat(demote.getWorkspaceRole()).isEqualTo("MAINTAINER");

        ArgumentCaptor<WorkspaceMember> insertCaptor = ArgumentCaptor.forClass(WorkspaceMember.class);
        verify(workspaceMemberMapper).insert(insertCaptor.capture());
        assertThat(insertCaptor.getValue().getUserId()).isEqualTo(newOwnerId);
        assertThat(insertCaptor.getValue().getWorkspaceRole()).isEqualTo("OWNER");

        verify(cacheSupport).evictMember(CODE, OWNER_ID);
        verify(cacheSupport).evictMember(CODE, newOwnerId);
    }

    // ===== updateStatus / delete =====

    @Test
    @DisplayName("停用空间：失效空间信息 / 概览 / 全量可访问缓存")
    void updateStatus_evictsCaches() {
        when(workspaceMapper.selectById(1L)).thenReturn(workspace(1L, CODE, "ENABLED", OWNER_ID));

        workspaceService.updateStatus(1L, "DISABLED");

        verify(workspaceMapper).updateById(any(Workspace.class));
        verify(cacheSupport).evictWorkspaceInfo(CODE);
        verify(cacheSupport).evictOverview();
        verify(cacheSupport).evictAllAccessible();
    }

    @Test
    @DisplayName("删除空间：级联逻辑删除成员与部门分配记录并失效全部缓存")
    void delete_cascadesMembersAndDepts() {
        when(workspaceMapper.selectById(1L)).thenReturn(workspace(1L, CODE, "ENABLED", OWNER_ID));

        workspaceService.delete(1L);

        verify(workspaceMapper).deleteById(1L);
        verify(workspaceMemberMapper).delete(any(Wrapper.class));
        verify(workspaceDeptMapper).delete(any(Wrapper.class));
        verify(cacheSupport).evictWorkspaceInfo(CODE);
        verify(cacheSupport).evictWorkspaceMembers(CODE);
        verify(cacheSupport).evictOverview();
        verify(cacheSupport).evictAllAccessible();
    }

    @Test
    @DisplayName("删除空间：空间不存在 → 020103")
    void delete_notFound_throws020103() {
        when(workspaceMapper.selectById(99L)).thenReturn(null);

        assertThatThrownBy(() -> workspaceService.delete(99L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getCode())
                .isEqualTo("020103");
    }

    // ===== overview / accessible =====

    @Test
    @DisplayName("概览：缓存命中直接返回")
    void overview_cacheHit_returnsCached() {
        WorkspaceOverviewVO cached = WorkspaceOverviewVO.builder().totalSpaces(3).build();
        when(cacheSupport.getOverview()).thenReturn(cached);

        assertThat(workspaceService.overview()).isSameAs(cached);
        verify(workspaceMapper, never()).selectCount(any());
    }

    @Test
    @DisplayName("可访问空间：userId 为空返回空列表")
    void accessible_nullUserId_returnsEmpty() {
        assertThat(workspaceService.accessible(null)).isEmpty();
    }

    @Test
    @DisplayName("可访问空间：缓存命中直接返回")
    void accessible_cacheHit_returnsCached() {
        List<WorkspaceSimpleVO> cached = List.of(WorkspaceSimpleVO.builder().code(CODE).build());
        when(cacheSupport.getAccessible(CREATOR_ID)).thenReturn(cached);

        assertThat(workspaceService.accessible(CREATOR_ID)).isSameAs(cached);
    }

    @Test
    @DisplayName("可访问空间：super_admin 返回全部启用空间")
    void accessible_superAdmin_returnsAllEnabled() {
        when(cacheSupport.getAccessible(CREATOR_ID)).thenReturn(null);
        SystemRole superAdmin = new SystemRole();
        superAdmin.setCode("super_admin");
        superAdmin.setStatus("ENABLED");
        when(systemRoleMapper.selectByUserId(CREATOR_ID)).thenReturn(List.of(superAdmin));
        when(workspaceMapper.selectList(any()))
                .thenReturn(List.of(workspace(1L, CODE, "ENABLED", OWNER_ID)));
        when(workspaceMemberMapper.selectEffectiveRole(CODE, CREATOR_ID)).thenReturn("OWNER");

        List<WorkspaceSimpleVO> result = workspaceService.accessible(CREATOR_ID);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCode()).isEqualTo(CODE);
        assertThat(result.get(0).getWorkspaceRole()).isEqualTo("OWNER");
        verify(cacheSupport).putAccessible(CREATOR_ID, result);
    }

    @Test
    @DisplayName("可访问空间：普通用户按成员空间 ∪ 自定义空间过滤，无命中返回空")
    void accessible_normalUser_unionOfMemberAndCustomCodes() {
        when(cacheSupport.getAccessible(CREATOR_ID)).thenReturn(null);
        SystemRole customRole = new SystemRole();
        customRole.setCode("custom");
        customRole.setStatus("ENABLED");
        customRole.setDataScope("CUSTOM");
        customRole.setCustomWorkspaceCodes(List.of("CUSTOM-WS"));
        when(systemRoleMapper.selectByUserId(CREATOR_ID)).thenReturn(List.of(customRole));
        when(workspaceMemberMapper.selectMemberWorkspaceCodes(CREATOR_ID)).thenReturn(List.of(CODE));
        when(workspaceMapper.selectList(any())).thenReturn(Collections.emptyList());

        assertThat(workspaceService.accessible(CREATOR_ID)).isEmpty();
        verify(workspaceMapper).selectList(any());
    }

    @Test
    @DisplayName("可访问空间：普通用户无任何空间 → 空列表且不查空间表")
    void accessible_normalUserNoCodes_returnsEmpty() {
        when(cacheSupport.getAccessible(CREATOR_ID)).thenReturn(null);
        when(systemRoleMapper.selectByUserId(CREATOR_ID)).thenReturn(Collections.emptyList());
        when(workspaceMemberMapper.selectMemberWorkspaceCodes(CREATOR_ID))
                .thenReturn(Collections.emptyList());

        assertThat(workspaceService.accessible(CREATOR_ID)).isEmpty();
        verify(workspaceMapper, never()).selectList(any());
    }

    // ===== isCodeAvailable =====

    @Test
    @DisplayName("编码可用性：格式非法 / 保留字 / 已存在 → false，未占用 → true")
    void isCodeAvailable_variousInputs() {
        assertThat(workspaceService.isCodeAvailable("bad_code")).isFalse();
        assertThat(workspaceService.isCodeAvailable("WORKSPACE")).isFalse();
        assertThat(workspaceService.isCodeAvailable("")).isFalse();

        when(workspaceMapper.selectByCode(CODE)).thenReturn(workspace(1L, CODE, "ENABLED", OWNER_ID));
        assertThat(workspaceService.isCodeAvailable(CODE)).isFalse();

        when(workspaceMapper.selectByCode("NEW-CODE")).thenReturn(null);
        assertThat(workspaceService.isCodeAvailable("NEW-CODE")).isTrue();
    }

    @Test
    @DisplayName("getByCode：不存在 → 020103")
    void getByCode_notFound_throws020103() {
        when(workspaceMapper.selectByCode(anyString())).thenReturn(null);

        assertThatThrownBy(() -> workspaceService.getByCode(CODE))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getCode())
                .isEqualTo("020103");
    }
}
