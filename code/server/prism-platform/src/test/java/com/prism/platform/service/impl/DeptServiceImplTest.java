package com.prism.platform.service.impl;

import com.prism.common.exception.BusinessException;
import com.prism.platform.dto.CreateDeptDTO;
import com.prism.platform.dto.DeptTreeQuery;
import com.prism.platform.dto.UpdateDeptDTO;
import com.prism.platform.entity.SystemDept;
import com.prism.platform.entity.SystemUserDept;
import com.prism.platform.mapper.SystemDeptMapper;
import com.prism.platform.mapper.SystemUserDeptMapper;
import com.prism.platform.vo.DeptTreeVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

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
 * 部门管理服务测试（树构建/增删改/启停用/分配用户/负责人/转移/排序）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("部门管理服务")
class DeptServiceImplTest {

    private static final Long ROOT_ID = 1L;

    @Mock
    private SystemDeptMapper systemDeptMapper;

    @Mock
    private SystemUserDeptMapper systemUserDeptMapper;

    @InjectMocks
    private DeptServiceImpl deptService;

    private SystemDept dept(Long id, Long parentId, String name, String path, Integer depth) {
        SystemDept dept = new SystemDept();
        dept.setId(id);
        dept.setParentId(parentId);
        dept.setName(name);
        dept.setCode("CODE_" + id);
        dept.setPath(path);
        dept.setDepth(depth);
        dept.setSortOrder(0);
        dept.setStatus("ENABLED");
        return dept;
    }

    private SystemUserDept userDept(Long id, Long userId, Long deptId) {
        SystemUserDept ud = new SystemUserDept();
        ud.setId(id);
        ud.setUserId(userId);
        ud.setDeptId(deptId);
        return ud;
    }

    private String codeOf(BusinessException e) {
        return e.getCode();
    }

    // ===== create =====

    @Test
    @DisplayName("新增部门：编码已存在 → 010502")
    void create_codeExists_throws010502() {
        when(systemDeptMapper.selectByCode("TECH")).thenReturn(dept(9L, 0L, "已有部门", "/", 0));
        CreateDeptDTO dto = CreateDeptDTO.builder().parentId(0L).name("技术部").code("TECH").build();

        assertThatThrownBy(() -> deptService.create(dto))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("010502");
    }

    @Test
    @DisplayName("新增部门：同级名称重复 → 010501")
    void create_nameSameLevelExists_throws010501() {
        when(systemDeptMapper.selectByCode("TECH")).thenReturn(null);
        when(systemDeptMapper.selectCount(any())).thenReturn(1L);
        CreateDeptDTO dto = CreateDeptDTO.builder().parentId(0L).name("技术部").code("TECH").build();

        assertThatThrownBy(() -> deptService.create(dto))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("010501");
    }

    @Test
    @DisplayName("新增部门：上级部门不存在 → 999904")
    void create_parentNotExists_throws999904() {
        when(systemDeptMapper.selectByCode("TECH")).thenReturn(null);
        when(systemDeptMapper.selectCount(any())).thenReturn(0L);
        when(systemDeptMapper.selectById(99L)).thenReturn(null);
        CreateDeptDTO dto = CreateDeptDTO.builder().parentId(99L).name("技术部").code("TECH").build();

        assertThatThrownBy(() -> deptService.create(dto))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999904");
    }

    @Test
    @DisplayName("新增部门：parentId 为空按根层级处理，path=/、depth=0、排序默认 0")
    void create_nullParentId_insertsAsRootLevel() {
        when(systemDeptMapper.selectByCode("TECH")).thenReturn(null);
        when(systemDeptMapper.selectCount(any())).thenReturn(0L);
        CreateDeptDTO dto = CreateDeptDTO.builder().name("技术部").code("TECH").build();

        deptService.create(dto);

        ArgumentCaptor<SystemDept> captor = ArgumentCaptor.forClass(SystemDept.class);
        verify(systemDeptMapper).insert(captor.capture());
        SystemDept inserted = captor.getValue();
        assertThat(inserted.getParentId()).isEqualTo(0L);
        assertThat(inserted.getPath()).isEqualTo("/");
        assertThat(inserted.getDepth()).isZero();
        assertThat(inserted.getSortOrder()).isZero();
        assertThat(inserted.getStatus()).isEqualTo("ENABLED");
    }

    @Test
    @DisplayName("新增部门：有上级时按上级 path/id 拼接层级路径并加深 depth")
    void create_withParent_computesPathAndDepth() {
        when(systemDeptMapper.selectByCode("BACKEND")).thenReturn(null);
        when(systemDeptMapper.selectCount(any())).thenReturn(0L);
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, 0L, "技术部", "/", 0));
        CreateDeptDTO dto = CreateDeptDTO.builder()
                .parentId(2L).name("后端组").code("BACKEND").leaderId(8L).sortOrder(5)
                .build();

        deptService.create(dto);

        ArgumentCaptor<SystemDept> captor = ArgumentCaptor.forClass(SystemDept.class);
        verify(systemDeptMapper).insert(captor.capture());
        SystemDept inserted = captor.getValue();
        assertThat(inserted.getPath()).isEqualTo("/2/");
        assertThat(inserted.getDepth()).isEqualTo(1);
        assertThat(inserted.getLeaderId()).isEqualTo(8L);
        assertThat(inserted.getSortOrder()).isEqualTo(5);
    }

    // ===== update =====

    private UpdateDeptDTO updateDTO(Long id, String name) {
        return UpdateDeptDTO.builder().id(id).name(name).build();
    }

    @Test
    @DisplayName("编辑部门：部门不存在 → 999904")
    void update_notFound_throws999904() {
        when(systemDeptMapper.selectById(2L)).thenReturn(null);

        assertThatThrownBy(() -> deptService.update(updateDTO(2L, "技术部")))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999904");
    }

    @Test
    @DisplayName("编辑部门：名称为空 → 999900")
    void update_blankName_throws999900() {
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, 0L, "技术部", "/", 0));

        assertThatThrownBy(() -> deptService.update(updateDTO(2L, " ")))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999900");
    }

    @Test
    @DisplayName("编辑部门：根节点只读 → 010503")
    void update_rootNode_throws010503() {
        when(systemDeptMapper.selectById(ROOT_ID)).thenReturn(dept(ROOT_ID, 0L, "根节点", "/", 0));

        assertThatThrownBy(() -> deptService.update(updateDTO(ROOT_ID, "新名字")))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("010503");
    }

    @Test
    @DisplayName("编辑部门：同级名称重复 → 010501")
    void update_nameConflict_throws010501() {
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, 0L, "技术部", "/", 0));
        when(systemDeptMapper.selectCount(any())).thenReturn(1L);

        assertThatThrownBy(() -> deptService.update(updateDTO(2L, "产品部")))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("010501");
    }

    @Test
    @DisplayName("编辑部门：上级设为自身 → 010504")
    void update_parentIsSelf_throws010504() {
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, ROOT_ID, "技术部", "/", 0));
        when(systemDeptMapper.selectCount(any())).thenReturn(0L);
        UpdateDeptDTO dto = updateDTO(2L, "技术部");
        dto.setParentId(2L);

        assertThatThrownBy(() -> deptService.update(dto))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("010504");
    }

    @Test
    @DisplayName("编辑部门：上级设为下级 → 010504")
    void update_parentIsChild_throws010504() {
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, ROOT_ID, "技术部", "/", 0));
        when(systemDeptMapper.selectCount(any())).thenReturn(0L);
        when(systemDeptMapper.selectChildrenByPath("/2/"))
                .thenReturn(List.of(dept(7L, 2L, "后端组", "/2/", 1)));
        UpdateDeptDTO dto = updateDTO(2L, "技术部");
        dto.setParentId(7L);

        assertThatThrownBy(() -> deptService.update(dto))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("010504");
    }

    @Test
    @DisplayName("编辑部门：上级不变时仅更新基础字段，不重算 path")
    void update_parentUnchanged_updatesBasicFieldsOnly() {
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, ROOT_ID, "技术部", "/", 0));
        when(systemDeptMapper.selectCount(any())).thenReturn(0L);
        UpdateDeptDTO dto = updateDTO(2L, "研发中心");
        dto.setSortOrder(3);
        dto.setLeaderId(8L);

        deptService.update(dto);

        ArgumentCaptor<SystemDept> captor = ArgumentCaptor.forClass(SystemDept.class);
        verify(systemDeptMapper).updateById(captor.capture());
        SystemDept updated = captor.getValue();
        assertThat(updated.getName()).isEqualTo("研发中心");
        assertThat(updated.getLeaderId()).isEqualTo(8L);
        assertThat(updated.getSortOrder()).isEqualTo(3);
        assertThat(updated.getPath()).isNull();
        verify(systemDeptMapper, never()).selectChildrenByPath(anyString());
    }

    @Test
    @DisplayName("编辑部门：移动到根层级时 path 重置为 /、depth=0")
    void update_moveToRoot_resetsPathAndDepth() {
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, ROOT_ID, "技术部", "/1/", 1));
        when(systemDeptMapper.selectCount(any())).thenReturn(0L);
        when(systemDeptMapper.selectChildrenByPath("/1/2/")).thenReturn(List.of());
        UpdateDeptDTO dto = updateDTO(2L, "技术部");
        dto.setParentId(0L);

        deptService.update(dto);

        ArgumentCaptor<SystemDept> captor = ArgumentCaptor.forClass(SystemDept.class);
        verify(systemDeptMapper).updateById(captor.capture());
        assertThat(captor.getValue().getPath()).isEqualTo("/");
        assertThat(captor.getValue().getDepth()).isZero();
    }

    @Test
    @DisplayName("编辑部门：移动到新上级时自身与全部子孙的 path/depth 同步重算")
    void update_moveToNewParent_updatesSelfAndDescendantPaths() {
        // 原：dept2 挂根下（path=/，depth=0），子孙 dept7（path=/2/，depth=1）
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, ROOT_ID, "技术部", "/", 0));
        when(systemDeptMapper.selectCount(any())).thenReturn(0L);
        when(systemDeptMapper.selectChildrenByPath("/2/"))
                .thenReturn(List.of(dept(7L, 2L, "后端组", "/2/", 1)));
        // 新上级 dept5：path=/1/，depth=1 → dept2 新 path=/1/5/，depth=2
        when(systemDeptMapper.selectById(5L)).thenReturn(dept(5L, ROOT_ID, "研发中心", "/1/", 1));
        UpdateDeptDTO dto = updateDTO(2L, "技术部");
        dto.setParentId(5L);

        deptService.update(dto);

        ArgumentCaptor<SystemDept> captor = ArgumentCaptor.forClass(SystemDept.class);
        verify(systemDeptMapper, Mockito.times(2)).updateById(captor.capture());
        SystemDept child = captor.getAllValues().get(0);
        assertThat(child.getId()).isEqualTo(7L);
        assertThat(child.getPath()).isEqualTo("/1/5/2/");
        assertThat(child.getDepth()).isEqualTo(3);
        SystemDept self = captor.getAllValues().get(1);
        assertThat(self.getId()).isEqualTo(2L);
        assertThat(self.getPath()).isEqualTo("/1/5/");
        assertThat(self.getDepth()).isEqualTo(2);
    }

    @Test
    @DisplayName("编辑部门：移动时新上级不存在 → 999904")
    void update_moveToParent_parentNotExists_throws999904() {
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, ROOT_ID, "技术部", "/", 0));
        when(systemDeptMapper.selectCount(any())).thenReturn(0L);
        when(systemDeptMapper.selectChildrenByPath("/2/")).thenReturn(List.of());
        when(systemDeptMapper.selectById(9L)).thenReturn(null);
        UpdateDeptDTO dto = updateDTO(2L, "技术部");
        dto.setParentId(9L);

        assertThatThrownBy(() -> deptService.update(dto))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999904");
    }

    // ===== delete =====

    @Test
    @DisplayName("删除部门：部门不存在 → 999904")
    void delete_notFound_throws999904() {
        when(systemDeptMapper.selectById(2L)).thenReturn(null);

        assertThatThrownBy(() -> deptService.delete(2L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999904");
    }

    @Test
    @DisplayName("删除部门：根节点只读 → 010503")
    void delete_rootNode_throws010503() {
        when(systemDeptMapper.selectById(ROOT_ID)).thenReturn(dept(ROOT_ID, 0L, "根节点", "/", 0));

        assertThatThrownBy(() -> deptService.delete(ROOT_ID))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("010503");
    }

    @Test
    @DisplayName("删除部门：存在下级部门不可删除 → 999904")
    void delete_hasChildren_throws999904() {
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, ROOT_ID, "技术部", "/", 0));
        when(systemDeptMapper.selectByParentId(2L))
                .thenReturn(List.of(dept(7L, 2L, "后端组", "/2/", 1)));

        assertThatThrownBy(() -> deptService.delete(2L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999904");
    }

    @Test
    @DisplayName("删除部门：无下级时删除成功")
    void delete_noChildren_deletesDept() {
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, ROOT_ID, "技术部", "/", 0));
        when(systemDeptMapper.selectByParentId(2L)).thenReturn(List.of());

        deptService.delete(2L);

        verify(systemDeptMapper).deleteById(2L);
    }

    // ===== tree =====

    @Test
    @DisplayName("部门树：按状态过滤并构建层级，附带成员数，负责人名待前端补充为 null")
    void tree_withStatusFilter_buildsHierarchyWithMemberCount() {
        SystemDept tech = dept(2L, 0L, "技术部", "/", 0);
        tech.setLeaderId(8L);
        SystemDept backend = dept(3L, 2L, "后端组", "/2/", 1);
        when(systemDeptMapper.selectList(any())).thenReturn(List.of(tech, backend));
        when(systemUserDeptMapper.selectByDeptId(2L)).thenReturn(List.of(
                userDept(100L, 5L, 2L), userDept(101L, 6L, 2L)));
        when(systemUserDeptMapper.selectByDeptId(3L)).thenReturn(List.of());
        DeptTreeQuery query = new DeptTreeQuery();
        query.setStatus("ENABLED");

        List<DeptTreeVO> tree = deptService.tree(query);

        assertThat(tree).hasSize(1);
        DeptTreeVO root = tree.get(0);
        assertThat(root.getName()).isEqualTo("技术部");
        assertThat(root.getMemberCount()).isEqualTo(2);
        assertThat(root.getLeaderName()).isNull();
        assertThat(root.getChildren()).hasSize(1);
        assertThat(root.getChildren().get(0).getName()).isEqualTo("后端组");
        assertThat(root.getChildren().get(0).getMemberCount()).isZero();
        assertThat(root.getChildren().get(0).getChildren()).isEmpty();
    }

    @Test
    @DisplayName("部门树：无状态过滤时返回全部根层级节点")
    void tree_noFilter_returnsFullTree() {
        when(systemDeptMapper.selectList(any()))
                .thenReturn(List.of(dept(2L, 0L, "技术部", "/", 0)));
        when(systemUserDeptMapper.selectByDeptId(2L)).thenReturn(List.of());
        DeptTreeQuery query = new DeptTreeQuery();

        List<DeptTreeVO> tree = deptService.tree(query);

        assertThat(tree).hasSize(1);
        verify(systemDeptMapper).selectList(any());
    }

    // ===== enableDisable =====

    @Test
    @DisplayName("启停用部门：部门不存在 → 999904")
    void enableDisable_notFound_throws999904() {
        when(systemDeptMapper.selectById(2L)).thenReturn(null);

        assertThatThrownBy(() -> deptService.enableDisable(2L, "DISABLED"))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999904");
    }

    @Test
    @DisplayName("启停用部门：根节点只读 → 010503")
    void enableDisable_rootNode_throws010503() {
        when(systemDeptMapper.selectById(ROOT_ID)).thenReturn(dept(ROOT_ID, 0L, "根节点", "/", 0));

        assertThatThrownBy(() -> deptService.enableDisable(ROOT_ID, "DISABLED"))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("010503");
    }

    @Test
    @DisplayName("停用部门：用户迁移至上级部门并更新状态")
    void enableDisable_disableWithParent_migratesUsersToParent() {
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, ROOT_ID, "技术部", "/", 0));
        when(systemUserDeptMapper.selectByDeptId(2L))
                .thenReturn(List.of(userDept(100L, 5L, 2L)));

        deptService.enableDisable(2L, "DISABLED");

        ArgumentCaptor<SystemUserDept> udCaptor = ArgumentCaptor.forClass(SystemUserDept.class);
        verify(systemUserDeptMapper).updateById(udCaptor.capture());
        assertThat(udCaptor.getValue().getId()).isEqualTo(100L);
        assertThat(udCaptor.getValue().getDeptId()).isEqualTo(ROOT_ID);

        ArgumentCaptor<SystemDept> deptCaptor = ArgumentCaptor.forClass(SystemDept.class);
        verify(systemDeptMapper).updateById(deptCaptor.capture());
        assertThat(deptCaptor.getValue().getStatus()).isEqualTo("DISABLED");
    }

    @Test
    @DisplayName("停用顶级部门（parentId=0）：直接解除用户部门关联")
    void enableDisable_disableTopLevel_removesUserLinks() {
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, 0L, "技术部", "/", 0));
        when(systemUserDeptMapper.selectByDeptId(2L))
                .thenReturn(List.of(userDept(100L, 5L, 2L)));

        deptService.enableDisable(2L, "DISABLED");

        verify(systemUserDeptMapper).deleteByUserId(5L);
        verify(systemUserDeptMapper, never()).updateById(any(SystemUserDept.class));
        verify(systemDeptMapper).updateById(any(SystemDept.class));
    }

    @Test
    @DisplayName("启用部门：仅更新状态，不迁移用户")
    void enableDisable_enable_onlyUpdatesStatus() {
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, ROOT_ID, "技术部", "/", 0));

        deptService.enableDisable(2L, "ENABLED");

        verify(systemUserDeptMapper, never()).selectByDeptId(anyLong());
        ArgumentCaptor<SystemDept> captor = ArgumentCaptor.forClass(SystemDept.class);
        verify(systemDeptMapper).updateById(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo("ENABLED");
    }

    // ===== assignUsers =====

    @Test
    @DisplayName("分配用户：部门不存在 → 999904")
    void assignUsers_deptNotFound_throws999904() {
        when(systemDeptMapper.selectById(2L)).thenReturn(null);

        assertThatThrownBy(() -> deptService.assignUsers(2L, List.of(5L)))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999904");
    }

    @Test
    @DisplayName("分配用户：先删旧关联再逐个新增")
    void assignUsers_replacesEachUserDeptLink() {
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, 0L, "技术部", "/", 0));

        deptService.assignUsers(2L, List.of(5L, 6L));

        verify(systemUserDeptMapper).deleteByUserId(5L);
        verify(systemUserDeptMapper).deleteByUserId(6L);
        ArgumentCaptor<SystemUserDept> captor = ArgumentCaptor.forClass(SystemUserDept.class);
        verify(systemUserDeptMapper, Mockito.times(2)).insert(captor.capture());
        assertThat(captor.getAllValues())
                .extracting(SystemUserDept::getUserId)
                .containsExactly(5L, 6L);
        assertThat(captor.getAllValues())
                .allSatisfy(ud -> assertThat(ud.getDeptId()).isEqualTo(2L));
    }

    @Test
    @DisplayName("分配用户：userIds 为 null 时不产生任何关联变更")
    void assignUsers_nullUserIds_noLinkChanges() {
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, 0L, "技术部", "/", 0));

        deptService.assignUsers(2L, null);

        verify(systemUserDeptMapper, never()).deleteByUserId(anyLong());
        verify(systemUserDeptMapper, never()).insert(any(SystemUserDept.class));
    }

    // ===== setLeader =====

    @Test
    @DisplayName("设置负责人：部门不存在 → 999904")
    void setLeader_notFound_throws999904() {
        when(systemDeptMapper.selectById(2L)).thenReturn(null);

        assertThatThrownBy(() -> deptService.setLeader(2L, 8L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999904");
    }

    @Test
    @DisplayName("设置负责人：仅更新 leaderId")
    void setLeader_updatesLeaderId() {
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, 0L, "技术部", "/", 0));

        deptService.setLeader(2L, 8L);

        ArgumentCaptor<SystemDept> captor = ArgumentCaptor.forClass(SystemDept.class);
        verify(systemDeptMapper).updateById(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(2L);
        assertThat(captor.getValue().getLeaderId()).isEqualTo(8L);
    }

    // ===== transfer =====

    @Test
    @DisplayName("部门转移：源部门不存在 → 999904")
    void transfer_sourceNotFound_throws999904() {
        when(systemDeptMapper.selectById(2L)).thenReturn(null);

        assertThatThrownBy(() -> deptService.transfer(2L, 3L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999904");
    }

    @Test
    @DisplayName("部门转移：目标部门不存在 → 999904")
    void transfer_targetNotFound_throws999904() {
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, ROOT_ID, "技术部", "/", 0));
        when(systemDeptMapper.selectById(3L)).thenReturn(null);

        assertThatThrownBy(() -> deptService.transfer(2L, 3L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999904");
    }

    @Test
    @DisplayName("部门转移：用户迁至目标部门，子部门及其子孙挂到目标下并重算 path/depth")
    void transfer_migratesUsersAndReparentsChildren() {
        // 源 dept2（path=/，depth=0）→ 目标 dept3（path=/1/，depth=1）
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, ROOT_ID, "技术部", "/", 0));
        when(systemDeptMapper.selectById(3L)).thenReturn(dept(3L, ROOT_ID, "产品中心", "/1/", 1));
        when(systemUserDeptMapper.selectByDeptId(2L))
                .thenReturn(List.of(userDept(100L, 5L, 2L)));
        when(systemDeptMapper.selectByParentId(2L))
                .thenReturn(List.of(dept(7L, 2L, "后端组", "/2/", 1)));
        when(systemDeptMapper.selectChildrenByPath("/2/7/"))
                .thenReturn(List.of(dept(9L, 7L, "Java 小组", "/2/7/", 2)));

        deptService.transfer(2L, 3L);

        ArgumentCaptor<SystemUserDept> udCaptor = ArgumentCaptor.forClass(SystemUserDept.class);
        verify(systemUserDeptMapper).updateById(udCaptor.capture());
        assertThat(udCaptor.getValue().getId()).isEqualTo(100L);
        assertThat(udCaptor.getValue().getDeptId()).isEqualTo(3L);

        ArgumentCaptor<SystemDept> deptCaptor = ArgumentCaptor.forClass(SystemDept.class);
        verify(systemDeptMapper, Mockito.times(2)).updateById(deptCaptor.capture());
        SystemDept child = deptCaptor.getAllValues().get(0);
        assertThat(child.getId()).isEqualTo(7L);
        assertThat(child.getParentId()).isEqualTo(3L);
        assertThat(child.getPath()).isEqualTo("/1/3/");
        assertThat(child.getDepth()).isEqualTo(2);
        SystemDept grandChild = deptCaptor.getAllValues().get(1);
        assertThat(grandChild.getId()).isEqualTo(9L);
        assertThat(grandChild.getPath()).isEqualTo("/1/3/7/");
        assertThat(grandChild.getDepth()).isEqualTo(3);
    }

    // ===== updateSort =====

    @Test
    @DisplayName("更新排序：部门不存在 → 999904")
    void updateSort_notFound_throws999904() {
        when(systemDeptMapper.selectById(2L)).thenReturn(null);

        assertThatThrownBy(() -> deptService.updateSort(2L, 3))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999904");
    }

    @Test
    @DisplayName("更新排序：仅更新 sortOrder")
    void updateSort_updatesSortOrder() {
        when(systemDeptMapper.selectById(2L)).thenReturn(dept(2L, 0L, "技术部", "/", 0));

        deptService.updateSort(2L, 9);

        ArgumentCaptor<SystemDept> captor = ArgumentCaptor.forClass(SystemDept.class);
        verify(systemDeptMapper).updateById(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(2L);
        assertThat(captor.getValue().getSortOrder()).isEqualTo(9);
    }
}
