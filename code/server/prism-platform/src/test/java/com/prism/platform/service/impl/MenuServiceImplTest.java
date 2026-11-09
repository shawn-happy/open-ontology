package com.prism.platform.service.impl;

import com.prism.common.exception.BusinessException;
import com.prism.platform.dto.CreateMenuDTO;
import com.prism.platform.dto.UpdateMenuDTO;
import com.prism.platform.entity.SystemMenu;
import com.prism.platform.entity.SystemRole;
import com.prism.platform.entity.SystemRoleMenu;
import com.prism.platform.mapper.SystemMenuMapper;
import com.prism.platform.mapper.SystemRoleMapper;
import com.prism.platform.mapper.SystemRoleMenuMapper;
import com.prism.platform.vo.MenuTreeVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 菜单权限服务测试（树构建/增删改/启停用/权限串解析/权限缓存清理）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("菜单权限服务")
class MenuServiceImplTest {

    private static final String PERMS_PATTERN = "prism:user:perms:*";

    @Mock
    private SystemMenuMapper systemMenuMapper;

    @Mock
    private SystemRoleMapper systemRoleMapper;

    @Mock
    private SystemRoleMenuMapper systemRoleMenuMapper;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @InjectMocks
    private MenuServiceImpl menuService;

    private SystemMenu menu(Long id, Long parentId, String name, String type, String permission,
            String status) {
        SystemMenu menu = new SystemMenu();
        menu.setId(id);
        menu.setParentId(parentId);
        menu.setName(name);
        menu.setType(type);
        menu.setPermission(permission);
        menu.setStatus(status);
        menu.setSortOrder(0);
        return menu;
    }

    private SystemRole role(Long id) {
        SystemRole role = new SystemRole();
        role.setId(id);
        role.setName("角色" + id);
        return role;
    }

    private SystemRoleMenu roleMenu(Long roleId, Long menuId) {
        SystemRoleMenu rm = new SystemRoleMenu();
        rm.setRoleId(roleId);
        rm.setMenuId(menuId);
        return rm;
    }

    private String codeOf(BusinessException e) {
        return e.getCode();
    }

    // ===== create =====

    @Test
    @DisplayName("新增菜单：权限标识已存在 → 010401")
    void create_permissionExists_throws010401() {
        when(systemMenuMapper.selectByPermission("system:user:list"))
                .thenReturn(menu(1L, 0L, "用户列表", "MENU", "system:user:list", "ENABLED"));
        CreateMenuDTO dto = CreateMenuDTO.builder()
                .parentId(0L).name("用户列表").type("MENU").permission("system:user:list")
                .build();

        assertThatThrownBy(() -> menuService.create(dto))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("010401");
    }

    @Test
    @DisplayName("新增菜单：权限标识为空跳过唯一性校验，排序默认 0、状态 ENABLED")
    void create_blankPermission_skipsUniqueCheckAndInserts() {
        CreateMenuDTO dto = CreateMenuDTO.builder()
                .parentId(0L).name("系统管理").type("DIRECTORY")
                .build();

        menuService.create(dto);

        verify(systemMenuMapper, never()).selectByPermission(anyString());
        ArgumentCaptor<SystemMenu> captor = ArgumentCaptor.forClass(SystemMenu.class);
        verify(systemMenuMapper).insert(captor.capture());
        SystemMenu inserted = captor.getValue();
        assertThat(inserted.getParentId()).isZero();
        assertThat(inserted.getName()).isEqualTo("系统管理");
        assertThat(inserted.getType()).isEqualTo("DIRECTORY");
        assertThat(inserted.getSortOrder()).isZero();
        assertThat(inserted.getStatus()).isEqualTo("ENABLED");
    }

    @Test
    @DisplayName("新增菜单：权限标识未占用时插入成功并保留字段")
    void create_permissionAvailable_insertsMenu() {
        when(systemMenuMapper.selectByPermission("system:user:create")).thenReturn(null);
        CreateMenuDTO dto = CreateMenuDTO.builder()
                .parentId(2L).name("新增用户").type("BUTTON")
                .permission("system:user:create").path("/system/user")
                .component("system/user/index").icon("plus").sortOrder(7)
                .build();

        menuService.create(dto);

        ArgumentCaptor<SystemMenu> captor = ArgumentCaptor.forClass(SystemMenu.class);
        verify(systemMenuMapper).insert(captor.capture());
        SystemMenu inserted = captor.getValue();
        assertThat(inserted.getPermission()).isEqualTo("system:user:create");
        assertThat(inserted.getPath()).isEqualTo("/system/user");
        assertThat(inserted.getComponent()).isEqualTo("system/user/index");
        assertThat(inserted.getIcon()).isEqualTo("plus");
        assertThat(inserted.getSortOrder()).isEqualTo(7);
    }

    // ===== update =====

    @Test
    @DisplayName("编辑菜单：菜单不存在 → 999904")
    void update_notFound_throws999904() {
        when(systemMenuMapper.selectById(2L)).thenReturn(null);

        assertThatThrownBy(() -> menuService.update(UpdateMenuDTO.builder().id(2L).name("x").build()))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999904");
    }

    @Test
    @DisplayName("编辑菜单：名称为空 → 999900")
    void update_blankName_throws999900() {
        when(systemMenuMapper.selectById(2L))
                .thenReturn(menu(2L, 0L, "用户管理", "MENU", "system:user:list", "ENABLED"));

        assertThatThrownBy(() -> menuService.update(UpdateMenuDTO.builder().id(2L).name(" ").build()))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999900");
    }

    @Test
    @DisplayName("编辑菜单：权限标识被其他菜单占用 → 010401")
    void update_permissionOwnedByOther_throws010401() {
        when(systemMenuMapper.selectById(2L))
                .thenReturn(menu(2L, 0L, "用户管理", "MENU", "system:user:list", "ENABLED"));
        when(systemMenuMapper.selectByPermission("system:role:list"))
                .thenReturn(menu(3L, 0L, "角色管理", "MENU", "system:role:list", "ENABLED"));

        assertThatThrownBy(
                () -> menuService.update(UpdateMenuDTO.builder()
                        .id(2L).name("用户管理").permission("system:role:list").build()))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("010401");
    }

    @Test
    @DisplayName("编辑菜单：权限标识属于自身时正常更新")
    void update_permissionOwnedBySelf_updates() {
        when(systemMenuMapper.selectById(2L))
                .thenReturn(menu(2L, 0L, "用户管理", "MENU", "system:user:list", "ENABLED"));
        when(systemMenuMapper.selectByPermission("system:user:list"))
                .thenReturn(menu(2L, 0L, "用户管理", "MENU", "system:user:list", "ENABLED"));
        when(stringRedisTemplate.keys(PERMS_PATTERN)).thenReturn(Set.of());

        menuService.update(UpdateMenuDTO.builder()
                .id(2L).name("用户管理V2").permission("system:user:list").sortOrder(3)
                .build());

        ArgumentCaptor<SystemMenu> captor = ArgumentCaptor.forClass(SystemMenu.class);
        verify(systemMenuMapper).updateById(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("用户管理V2");
        assertThat(captor.getValue().getSortOrder()).isEqualTo(3);
    }

    @Test
    @DisplayName("编辑菜单成功后清理全部用户权限缓存")
    void update_success_clearsUserPermissionCache() {
        when(systemMenuMapper.selectById(2L))
                .thenReturn(menu(2L, 0L, "用户管理", "MENU", null, "ENABLED"));
        when(stringRedisTemplate.keys(PERMS_PATTERN))
                .thenReturn(Set.of("prism:user:perms:1", "prism:user:perms:2"));

        menuService.update(UpdateMenuDTO.builder().id(2L).name("用户管理V2").build());

        verify(stringRedisTemplate)
                .delete(Set.of("prism:user:perms:1", "prism:user:perms:2"));
    }

    // ===== delete =====

    @Test
    @DisplayName("删除菜单：菜单不存在 → 999904")
    void delete_notFound_throws999904() {
        when(systemMenuMapper.selectById(2L)).thenReturn(null);

        assertThatThrownBy(() -> menuService.delete(2L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999904");
    }

    @Test
    @DisplayName("删除菜单：存在下级节点 → 010402")
    void delete_hasChildren_throws010402() {
        when(systemMenuMapper.selectById(2L))
                .thenReturn(menu(2L, 0L, "系统管理", "DIRECTORY", null, "ENABLED"));
        when(systemMenuMapper.selectByParentId(2L))
                .thenReturn(List.of(menu(3L, 2L, "用户管理", "MENU", null, "ENABLED")));

        assertThatThrownBy(() -> menuService.delete(2L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("010402");
    }

    @Test
    @DisplayName("删除菜单：无下级时删除成功；缓存 key 为空不触发删除")
    void delete_noChildren_deletesAndSkipsEmptyCacheDelete() {
        when(systemMenuMapper.selectById(3L))
                .thenReturn(menu(3L, 2L, "用户管理", "MENU", null, "ENABLED"));
        when(systemMenuMapper.selectByParentId(3L)).thenReturn(List.of());
        when(stringRedisTemplate.keys(PERMS_PATTERN)).thenReturn(Set.of());

        menuService.delete(3L);

        verify(systemMenuMapper).deleteById(3L);
        verify(stringRedisTemplate, never()).delete(anyCollection());
    }

    // ===== tree =====

    @Test
    @DisplayName("菜单树：目录-菜单-按钮三级层级构建")
    void tree_buildsThreeLevelHierarchy() {
        when(systemMenuMapper.selectAll()).thenReturn(List.of(
                menu(1L, 0L, "系统管理", "DIRECTORY", null, "ENABLED"),
                menu(2L, 1L, "用户管理", "MENU", "system:user:list", "ENABLED"),
                menu(3L, 2L, "新增用户", "BUTTON", "system:user:create", "ENABLED")));

        List<MenuTreeVO> tree = menuService.tree();

        assertThat(tree).hasSize(1);
        MenuTreeVO directory = tree.get(0);
        assertThat(directory.getName()).isEqualTo("系统管理");
        assertThat(directory.getChildren()).hasSize(1);
        MenuTreeVO menuVo = directory.getChildren().get(0);
        assertThat(menuVo.getPermission()).isEqualTo("system:user:list");
        assertThat(menuVo.getChildren()).hasSize(1);
        assertThat(menuVo.getChildren().get(0).getType()).isEqualTo("BUTTON");
        assertThat(menuVo.getChildren().get(0).getChildren()).isEmpty();
    }

    // ===== enableDisable =====

    @Test
    @DisplayName("启停用菜单：菜单不存在 → 999904")
    void enableDisable_notFound_throws999904() {
        when(systemMenuMapper.selectById(2L)).thenReturn(null);

        assertThatThrownBy(() -> menuService.enableDisable(2L, "DISABLED"))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999904");
    }

    @Test
    @DisplayName("停用菜单：更新状态；缓存 key 返回 null 时不清理也不报错")
    void enableDisable_nullCacheKeys_updatesStatusOnly() {
        when(systemMenuMapper.selectById(2L))
                .thenReturn(menu(2L, 0L, "用户管理", "MENU", null, "ENABLED"));
        when(stringRedisTemplate.keys(PERMS_PATTERN)).thenReturn(null);

        menuService.enableDisable(2L, "DISABLED");

        ArgumentCaptor<SystemMenu> captor = ArgumentCaptor.forClass(SystemMenu.class);
        verify(systemMenuMapper).updateById(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo("DISABLED");
        verify(stringRedisTemplate, never()).delete(anyCollection());
    }

    // ===== getPermissionsByUserId =====

    @Test
    @DisplayName("权限串：用户无角色（null 或空列表）→ 返回空列表")
    void getPermissionsByUserId_noRoles_returnsEmpty() {
        when(systemRoleMapper.selectByUserId(5L)).thenReturn(null);
        assertThat(menuService.getPermissionsByUserId(5L)).isEmpty();

        when(systemRoleMapper.selectByUserId(5L)).thenReturn(List.of());
        assertThat(menuService.getPermissionsByUserId(5L)).isEmpty();
    }

    @Test
    @DisplayName("权限串：过滤停用/空标识/已删菜单并跨角色去重")
    void getPermissionsByUserId_filtersInvalidAndDedups() {
        when(systemRoleMapper.selectByUserId(5L)).thenReturn(List.of(role(1L), role(2L)));
        // 角色1 与角色2 共享菜单1；菜单2 停用；菜单3 权限标识为空；菜单4 已删除
        when(systemRoleMenuMapper.selectByRoleId(1L)).thenReturn(List.of(roleMenu(1L, 1L)));
        when(systemRoleMenuMapper.selectByRoleId(2L)).thenReturn(List.of(
                roleMenu(2L, 1L), roleMenu(2L, 2L), roleMenu(2L, 3L), roleMenu(2L, 4L)));
        when(systemMenuMapper.selectById(1L))
                .thenReturn(menu(1L, 0L, "用户列表", "MENU", "system:user:list", "ENABLED"));
        when(systemMenuMapper.selectById(2L))
                .thenReturn(menu(2L, 0L, "角色列表", "MENU", "system:role:list", "DISABLED"));
        when(systemMenuMapper.selectById(3L))
                .thenReturn(menu(3L, 0L, "目录", "DIRECTORY", " ", "ENABLED"));
        when(systemMenuMapper.selectById(4L)).thenReturn(null);

        List<String> permissions = menuService.getPermissionsByUserId(5L);

        assertThat(permissions).containsExactly("system:user:list");
    }
}
