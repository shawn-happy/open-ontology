package com.prism.platform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prism.common.exception.BusinessException;
import com.prism.common.result.PageResult;
import com.prism.platform.constants.SystemConstants;
import com.prism.platform.dto.CreateRoleDTO;
import com.prism.platform.dto.RolePageQuery;
import com.prism.platform.dto.RolePermissionDTO;
import com.prism.platform.dto.UpdateRoleDTO;
import com.prism.platform.entity.SystemRole;
import com.prism.platform.entity.SystemRoleMenu;
import com.prism.platform.entity.SystemUserRole;
import com.prism.platform.mapper.SystemRoleMapper;
import com.prism.platform.mapper.SystemRoleMenuMapper;
import com.prism.platform.mapper.SystemUserRoleMapper;
import com.prism.platform.vo.RoleDetailVO;
import com.prism.platform.vo.RoleListVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 角色管理服务测试（增删改查/权限配置/分配用户/内置角色保护）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("角色管理服务")
class RoleServiceImplTest {

    private static final Long ROLE_ID = 5L;
    private static final String ROLE_NAME = "审计员";
    private static final String ROLE_CODE = "auditor";

    @Mock
    private SystemRoleMapper systemRoleMapper;

    @Mock
    private SystemRoleMenuMapper systemRoleMenuMapper;

    @Mock
    private SystemUserRoleMapper systemUserRoleMapper;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @InjectMocks
    private RoleServiceImpl roleService;

    private SystemRole role(Long id, String code, boolean builtin) {
        SystemRole role = new SystemRole();
        role.setId(id);
        role.setName(ROLE_NAME);
        role.setCode(code);
        role.setType("GLOBAL");
        role.setDataScope("SELF");
        role.setStatus("ENABLED");
        role.setBuiltin(builtin);
        return role;
    }

    private SystemUserRole userRole(Long userId, Long roleId) {
        SystemUserRole ur = new SystemUserRole();
        ur.setUserId(userId);
        ur.setRoleId(roleId);
        return ur;
    }

    @Nested
    @DisplayName("新增角色")
    class Create {

        private CreateRoleDTO dto() {
            return CreateRoleDTO.builder()
                    .name(ROLE_NAME).code(ROLE_CODE).type("GLOBAL").remark("备注").build();
        }

        @Test
        @DisplayName("角色名称已存在 - 抛出 010301")
        void create_nameExists_throws010301() {
            when(systemRoleMapper.selectCount(any())).thenReturn(1L);

            assertThatThrownBy(() -> roleService.create(dto()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010301");
        }

        @Test
        @DisplayName("角色编码已存在 - 抛出 010302")
        void create_codeExists_throws010302() {
            when(systemRoleMapper.selectCount(any())).thenReturn(0L);
            when(systemRoleMapper.selectByCode(ROLE_CODE)).thenReturn(role(99L, ROLE_CODE, false));

            assertThatThrownBy(() -> roleService.create(dto()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010302");
        }

        @Test
        @DisplayName("创建成功 - 默认 SELF 数据范围/启用/非内置")
        void create_success_insertsWithDefaults() {
            when(systemRoleMapper.selectCount(any())).thenReturn(0L);
            when(systemRoleMapper.selectByCode(ROLE_CODE)).thenReturn(null);

            roleService.create(dto());

            ArgumentCaptor<SystemRole> captor = ArgumentCaptor.forClass(SystemRole.class);
            verify(systemRoleMapper).insert(captor.capture());
            SystemRole saved = captor.getValue();
            assertThat(saved.getName()).isEqualTo(ROLE_NAME);
            assertThat(saved.getCode()).isEqualTo(ROLE_CODE);
            assertThat(saved.getType()).isEqualTo("GLOBAL");
            assertThat(saved.getDataScope()).isEqualTo("SELF");
            assertThat(saved.getStatus()).isEqualTo("ENABLED");
            assertThat(saved.getBuiltin()).isFalse();
            assertThat(saved.getRemark()).isEqualTo("备注");
        }
    }

    @Nested
    @DisplayName("编辑角色")
    class Update {

        private UpdateRoleDTO dto(String name, String type) {
            return UpdateRoleDTO.builder().id(ROLE_ID).name(name).type(type).remark("新备注").build();
        }

        @Test
        @DisplayName("角色不存在 - 抛出 999904")
        void update_roleNotFound_throws999904() {
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(null);

            assertThatThrownBy(() -> roleService.update(dto(ROLE_NAME, "GLOBAL")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999904");
        }

        @Test
        @DisplayName("名称或类型为空 - 抛出 999900")
        void update_blankNameOrType_throws999900() {
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(role(ROLE_ID, ROLE_CODE, false));

            assertThatThrownBy(() -> roleService.update(dto(" ", "GLOBAL")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999900");
        }

        @Test
        @DisplayName("内置角色不可修改 - 抛出 010303")
        void update_builtinRole_throws010303() {
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(role(ROLE_ID, ROLE_CODE, true));

            assertThatThrownBy(() -> roleService.update(dto(ROLE_NAME, "GLOBAL")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010303");
        }

        @Test
        @DisplayName("超管角色不可修改 - 抛出 010305")
        void update_superAdminRole_throws010305() {
            when(systemRoleMapper.selectById(ROLE_ID))
                    .thenReturn(role(ROLE_ID, SystemConstants.ROLE_SUPER_ADMIN, false));

            assertThatThrownBy(() -> roleService.update(dto(ROLE_NAME, "GLOBAL")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010305");
        }

        @Test
        @DisplayName("名称与其他角色重复 - 抛出 010301")
        void update_nameConflict_throws010301() {
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(role(ROLE_ID, ROLE_CODE, false));
            when(systemRoleMapper.selectCount(any())).thenReturn(1L);

            assertThatThrownBy(() -> roleService.update(dto(ROLE_NAME, "GLOBAL")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010301");
        }

        @Test
        @DisplayName("编辑成功 - 仅更新名称类型备注")
        void update_success_updatesNameTypeRemark() {
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(role(ROLE_ID, ROLE_CODE, false));
            when(systemRoleMapper.selectCount(any())).thenReturn(0L);

            roleService.update(dto("新名称", "WORKSPACE"));

            ArgumentCaptor<SystemRole> captor = ArgumentCaptor.forClass(SystemRole.class);
            verify(systemRoleMapper).updateById(captor.capture());
            SystemRole updated = captor.getValue();
            assertThat(updated.getId()).isEqualTo(ROLE_ID);
            assertThat(updated.getName()).isEqualTo("新名称");
            assertThat(updated.getType()).isEqualTo("WORKSPACE");
            assertThat(updated.getRemark()).isEqualTo("新备注");
            assertThat(updated.getCode()).isNull();
        }
    }

    @Nested
    @DisplayName("删除角色")
    class Delete {

        @Test
        @DisplayName("角色不存在 - 抛出 999904")
        void delete_roleNotFound_throws999904() {
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(null);

            assertThatThrownBy(() -> roleService.delete(ROLE_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999904");
        }

        @Test
        @DisplayName("内置角色不可删除 - 抛出 010303")
        void delete_builtinRole_throws010303() {
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(role(ROLE_ID, ROLE_CODE, true));

            assertThatThrownBy(() -> roleService.delete(ROLE_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010303");
        }

        @Test
        @DisplayName("删除成功 - 级联删除菜单用户关联并清关联用户缓存")
        void delete_success_cascadesAndClearsCache() {
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(role(ROLE_ID, ROLE_CODE, false));
            when(systemUserRoleMapper.selectList(any()))
                    .thenReturn(List.of(userRole(7L, ROLE_ID), userRole(8L, ROLE_ID)));

            roleService.delete(ROLE_ID);

            verify(systemRoleMapper).deleteById(ROLE_ID);
            verify(systemRoleMenuMapper).deleteByRoleId(ROLE_ID);
            verify(systemUserRoleMapper).deleteByRoleId(ROLE_ID);
            verify(stringRedisTemplate).delete(SystemConstants.REDIS_USER_PERMS_PREFIX + 7L);
            verify(stringRedisTemplate).delete(SystemConstants.REDIS_USER_PERMS_PREFIX + 8L);
        }
    }

    @Nested
    @DisplayName("查询角色详情")
    class GetById {

        @Test
        @DisplayName("角色不存在 - 抛出 999904")
        void getById_roleNotFound_throws999904() {
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(null);

            assertThatThrownBy(() -> roleService.getById(ROLE_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999904");
        }

        @Test
        @DisplayName("查询成功 - 组装菜单 ID 与成员数")
        void getById_success_assemblesMenusAndMemberCount() {
            SystemRole role = role(ROLE_ID, ROLE_CODE, false);
            role.setCustomWorkspaceCodes(List.of("WS1"));
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(role);
            SystemRoleMenu rm1 = new SystemRoleMenu();
            rm1.setRoleId(ROLE_ID);
            rm1.setMenuId(100L);
            SystemRoleMenu rm2 = new SystemRoleMenu();
            rm2.setRoleId(ROLE_ID);
            rm2.setMenuId(101L);
            when(systemRoleMenuMapper.selectByRoleId(ROLE_ID)).thenReturn(List.of(rm1, rm2));
            when(systemUserRoleMapper.selectCount(any())).thenReturn(3L);

            RoleDetailVO vo = roleService.getById(ROLE_ID);

            assertThat(vo.getId()).isEqualTo(ROLE_ID);
            assertThat(vo.getCode()).isEqualTo(ROLE_CODE);
            assertThat(vo.getMenuIds()).containsExactly(100L, 101L);
            assertThat(vo.getMemberCount()).isEqualTo(3);
            assertThat(vo.getCustomWorkspaceCodes()).containsExactly("WS1");
        }
    }

    @Nested
    @DisplayName("分页查询角色")
    class PageQuery {

        @Test
        @DisplayName("带关键词查询 - 返回列表并统计成员数")
        void page_withKeyword_returnsVOsWithMemberCount() {
            RolePageQuery query = new RolePageQuery();
            query.setKeyword("审计");
            when(systemRoleMapper.selectPage(any(), any())).thenAnswer(invocation -> {
                Page<SystemRole> page = invocation.getArgument(0);
                page.setRecords(List.of(role(ROLE_ID, ROLE_CODE, false)));
                page.setTotal(1);
                return page;
            });
            when(systemUserRoleMapper.selectCount(any())).thenReturn(2L);

            PageResult<RoleListVO> result = roleService.page(query);

            assertThat(result.getData()).hasSize(1);
            assertThat(result.getData().get(0).getName()).isEqualTo(ROLE_NAME);
            assertThat(result.getData().get(0).getMemberCount()).isEqualTo(2);
            assertThat(result.getPageInfo().getTotal()).isEqualTo(1);
        }

        @Test
        @DisplayName("查询结果为空 - 返回空列表")
        void page_emptyRecords_returnsEmpty() {
            when(systemRoleMapper.selectPage(any(), any())).thenAnswer(invocation -> {
                Page<SystemRole> page = invocation.getArgument(0);
                page.setRecords(Collections.emptyList());
                page.setTotal(0);
                return page;
            });

            PageResult<RoleListVO> result = roleService.page(new RolePageQuery());

            assertThat(result.getData()).isEmpty();
            assertThat(result.getPageInfo().getTotal()).isZero();
        }
    }

    @Nested
    @DisplayName("启用停用角色")
    class EnableDisable {

        @Test
        @DisplayName("角色不存在 - 抛出 999904")
        void enableDisable_roleNotFound_throws999904() {
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(null);

            assertThatThrownBy(() -> roleService.enableDisable(ROLE_ID, "DISABLED"))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999904");
        }

        @Test
        @DisplayName("内置角色不可停用 - 抛出 010304")
        void enableDisable_builtinRole_throws010304() {
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(role(ROLE_ID, ROLE_CODE, true));

            assertThatThrownBy(() -> roleService.enableDisable(ROLE_ID, "DISABLED"))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010304");
        }

        @Test
        @DisplayName("停用成功 - 更新状态并清关联用户权限缓存")
        void enableDisable_success_updatesStatusAndClearsCache() {
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(role(ROLE_ID, ROLE_CODE, false));
            when(systemUserRoleMapper.selectList(any())).thenReturn(List.of(userRole(9L, ROLE_ID)));

            roleService.enableDisable(ROLE_ID, "DISABLED");

            ArgumentCaptor<SystemRole> captor = ArgumentCaptor.forClass(SystemRole.class);
            verify(systemRoleMapper).updateById(captor.capture());
            assertThat(captor.getValue().getStatus()).isEqualTo("DISABLED");
            verify(stringRedisTemplate).delete(SystemConstants.REDIS_USER_PERMS_PREFIX + 9L);
        }
    }

    @Nested
    @DisplayName("配置角色权限")
    class ConfigurePermissions {

        private RolePermissionDTO dto(List<Long> menuIds) {
            return RolePermissionDTO.builder()
                    .roleId(ROLE_ID).menuIds(menuIds).dataScope("CUSTOM")
                    .customWorkspaceCodes(List.of("WS1")).build();
        }

        @Test
        @DisplayName("角色不存在 - 抛出 999904")
        void configurePermissions_roleNotFound_throws999904() {
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(null);

            assertThatThrownBy(() -> roleService.configurePermissions(dto(List.of(1L))))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999904");
        }

        @Test
        @DisplayName("超管角色不可配置 - 抛出 010305")
        void configurePermissions_superAdminRole_throws010305() {
            when(systemRoleMapper.selectById(ROLE_ID))
                    .thenReturn(role(ROLE_ID, SystemConstants.ROLE_SUPER_ADMIN, false));

            assertThatThrownBy(() -> roleService.configurePermissions(dto(List.of(1L))))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010305");
        }

        @Test
        @DisplayName("配置成功 - 更新数据范围并重建菜单关联清缓存")
        void configurePermissions_success_replacesMenusAndClearsCache() {
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(role(ROLE_ID, ROLE_CODE, false));
            when(systemUserRoleMapper.selectList(any())).thenReturn(List.of(userRole(9L, ROLE_ID)));

            roleService.configurePermissions(dto(List.of(1L, 2L)));

            ArgumentCaptor<SystemRole> captor = ArgumentCaptor.forClass(SystemRole.class);
            verify(systemRoleMapper).updateById(captor.capture());
            assertThat(captor.getValue().getDataScope()).isEqualTo("CUSTOM");
            assertThat(captor.getValue().getCustomWorkspaceCodes()).containsExactly("WS1");

            verify(systemRoleMenuMapper).deleteByRoleId(ROLE_ID);
            ArgumentCaptor<SystemRoleMenu> menuCaptor = ArgumentCaptor.forClass(SystemRoleMenu.class);
            verify(systemRoleMenuMapper, Mockito.times(2)).insert(menuCaptor.capture());
            assertThat(menuCaptor.getAllValues()).extracting(SystemRoleMenu::getMenuId)
                    .containsExactly(1L, 2L);
            verify(stringRedisTemplate).delete(SystemConstants.REDIS_USER_PERMS_PREFIX + 9L);
        }

        @Test
        @DisplayName("菜单列表为 null - 仅删除旧关联不插入")
        void configurePermissions_nullMenuIds_onlyDeletes() {
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(role(ROLE_ID, ROLE_CODE, false));
            when(systemUserRoleMapper.selectList(any())).thenReturn(Collections.emptyList());

            roleService.configurePermissions(dto(null));

            verify(systemRoleMenuMapper).deleteByRoleId(ROLE_ID);
            verify(systemRoleMenuMapper, never()).insert(any(SystemRoleMenu.class));
        }
    }

    @Nested
    @DisplayName("分配用户")
    class AssignUsers {

        @Test
        @DisplayName("角色不存在 - 抛出 999904")
        void assignUsers_roleNotFound_throws999904() {
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(null);

            assertThatThrownBy(() -> roleService.assignUsers(ROLE_ID, List.of(1L)))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999904");
        }

        @Test
        @DisplayName("分配成功 - 重建用户关联并清权限缓存")
        void assignUsers_success_replacesRelationsAndClearsCache() {
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(role(ROLE_ID, ROLE_CODE, false));

            roleService.assignUsers(ROLE_ID, List.of(1L, 2L));

            verify(systemUserRoleMapper).deleteByRoleId(ROLE_ID);
            ArgumentCaptor<SystemUserRole> captor = ArgumentCaptor.forClass(SystemUserRole.class);
            verify(systemUserRoleMapper, Mockito.times(2)).insert(captor.capture());
            assertThat(captor.getAllValues()).extracting(SystemUserRole::getUserId)
                    .containsExactly(1L, 2L);
            verify(stringRedisTemplate).delete(SystemConstants.REDIS_USER_PERMS_PREFIX + 1L);
            verify(stringRedisTemplate).delete(SystemConstants.REDIS_USER_PERMS_PREFIX + 2L);
        }

        @Test
        @DisplayName("用户列表为 null - 仅清空关联")
        void assignUsers_nullUserIds_onlyDeletes() {
            when(systemRoleMapper.selectById(ROLE_ID)).thenReturn(role(ROLE_ID, ROLE_CODE, false));

            roleService.assignUsers(ROLE_ID, null);

            verify(systemUserRoleMapper).deleteByRoleId(ROLE_ID);
            verify(systemUserRoleMapper, never()).insert(any(SystemUserRole.class));
            verify(stringRedisTemplate, never()).delete(anyString());
        }
    }

    @Nested
    @DisplayName("查询全部启用角色")
    class ListAll {

        @Test
        @DisplayName("带关键词 - 返回启用角色列表")
        void listAll_withKeyword_returnsEnabledRoles() {
            when(systemRoleMapper.selectList(any())).thenReturn(List.of(role(ROLE_ID, ROLE_CODE, false)));
            when(systemUserRoleMapper.selectCount(any())).thenReturn(4L);

            List<RoleListVO> result = roleService.listAll("审计");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCode()).isEqualTo(ROLE_CODE);
            assertThat(result.get(0).getMemberCount()).isEqualTo(4);
        }

        @Test
        @DisplayName("无关键词 - 返回全部启用角色")
        void listAll_noKeyword_returnsAllEnabled() {
            when(systemRoleMapper.selectList(any())).thenReturn(Collections.emptyList());

            assertThat(roleService.listAll(null)).isEmpty();
            verify(systemRoleMapper).selectList(any());
        }
    }
}
