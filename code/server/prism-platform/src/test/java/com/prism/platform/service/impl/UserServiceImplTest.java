package com.prism.platform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prism.common.exception.BusinessException;
import com.prism.common.result.PageResult;
import com.prism.platform.constants.SystemConstants;
import com.prism.platform.dto.CreateUserDTO;
import com.prism.platform.dto.UpdateUserDTO;
import com.prism.platform.dto.UserPageQuery;
import com.prism.platform.entity.SystemDept;
import com.prism.platform.entity.SystemRole;
import com.prism.platform.entity.SystemUser;
import com.prism.platform.entity.SystemUserDept;
import com.prism.platform.entity.SystemUserRole;
import com.prism.platform.mapper.SystemDeptMapper;
import com.prism.platform.mapper.SystemRoleMapper;
import com.prism.platform.mapper.SystemUserDeptMapper;
import com.prism.platform.mapper.SystemUserMapper;
import com.prism.platform.mapper.SystemUserRoleMapper;
import com.prism.platform.vo.ImportResultVO;
import com.prism.platform.vo.UserDetailVO;
import com.prism.platform.vo.UserListVO;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
 * 用户管理服务测试（增删改查/分配角色部门/导入导出）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("用户管理服务")
class UserServiceImplTest {

    private static final Long USER_ID = 10L;
    private static final String USERNAME = "u1";
    private static final String PHONE = "13800000000";
    private static final String EMAIL = "u1@example.com";
    private static final Long DEPT_ID = 3L;
    private static final Long ROLE_ID = 100L;

    @Mock
    private SystemUserMapper systemUserMapper;

    @Mock
    private SystemUserRoleMapper systemUserRoleMapper;

    @Mock
    private SystemUserDeptMapper systemUserDeptMapper;

    @Mock
    private SystemRoleMapper systemRoleMapper;

    @Mock
    private SystemDeptMapper systemDeptMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @InjectMocks
    private UserServiceImpl userService;

    private SystemUser user(Long id, String username) {
        SystemUser user = new SystemUser();
        user.setId(id);
        user.setUsername(username);
        user.setName("用户" + id);
        user.setPhone(PHONE);
        user.setEmail(EMAIL);
        user.setStatus("ENABLED");
        return user;
    }

    private SystemRole role(Long id, String name, String code) {
        SystemRole role = new SystemRole();
        role.setId(id);
        role.setName(name);
        role.setCode(code);
        return role;
    }

    private SystemDept dept(Long id, String name) {
        SystemDept dept = new SystemDept();
        dept.setId(id);
        dept.setName(name);
        dept.setPath("/1/");
        return dept;
    }

    @Nested
    @DisplayName("新增用户")
    class Create {

        private CreateUserDTO dto() {
            return CreateUserDTO.builder()
                    .username(USERNAME).name("用户一").phone(PHONE).email(EMAIL)
                    .status("ENABLED").build();
        }

        @Test
        @DisplayName("用户名已存在 - 抛出 010201")
        void create_usernameExists_throws010201() {
            when(systemUserMapper.selectByUsername(USERNAME)).thenReturn(user(99L, USERNAME));

            assertThatThrownBy(() -> userService.create(dto()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010201");
        }

        @Test
        @DisplayName("手机号已被使用 - 抛出 010202")
        void create_phoneExists_throws010202() {
            when(systemUserMapper.selectByUsername(USERNAME)).thenReturn(null);
            when(systemUserMapper.selectByPhone(PHONE)).thenReturn(user(99L, "other"));

            assertThatThrownBy(() -> userService.create(dto()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010202");
        }

        @Test
        @DisplayName("邮箱已被使用 - 抛出 010203")
        void create_emailExists_throws010203() {
            when(systemUserMapper.selectByUsername(USERNAME)).thenReturn(null);
            when(systemUserMapper.selectByPhone(PHONE)).thenReturn(null);
            when(systemUserMapper.selectByEmail(EMAIL)).thenReturn(user(99L, "other"));

            assertThatThrownBy(() -> userService.create(dto()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010203");
        }

        @Test
        @DisplayName("创建成功 - 写入初始密码且不关联部门")
        void create_success_insertsUserWithInitialPassword() {
            when(systemUserMapper.selectByUsername(USERNAME)).thenReturn(null);
            when(systemUserMapper.selectByPhone(PHONE)).thenReturn(null);
            when(systemUserMapper.selectByEmail(EMAIL)).thenReturn(null);
            when(passwordEncoder.encode(SystemConstants.INITIAL_PASSWORD)).thenReturn("encoded-initial");

            userService.create(dto());

            ArgumentCaptor<SystemUser> captor = ArgumentCaptor.forClass(SystemUser.class);
            verify(systemUserMapper).insert(captor.capture());
            SystemUser saved = captor.getValue();
            assertThat(saved.getUsername()).isEqualTo(USERNAME);
            assertThat(saved.getPassword()).isEqualTo("encoded-initial");
            assertThat(saved.getLoginFailCount()).isZero();
            assertThat(saved.getStatus()).isEqualTo("ENABLED");
            verify(systemUserDeptMapper, never()).insert(any(SystemUserDept.class));
        }

        @Test
        @DisplayName("创建成功且指定部门 - 同步写入用户部门关联")
        void create_withDept_insertsUserDept() {
            when(systemUserMapper.selectByUsername(USERNAME)).thenReturn(null);
            when(passwordEncoder.encode(SystemConstants.INITIAL_PASSWORD)).thenReturn("encoded-initial");
            when(systemUserMapper.insert(any(SystemUser.class))).thenAnswer(invocation -> {
                SystemUser inserted = invocation.getArgument(0);
                inserted.setId(USER_ID);
                return 1;
            });
            CreateUserDTO withDept = dto();
            withDept.setDeptId(DEPT_ID);

            userService.create(withDept);

            ArgumentCaptor<SystemUserDept> captor = ArgumentCaptor.forClass(SystemUserDept.class);
            verify(systemUserDeptMapper).insert(captor.capture());
            assertThat(captor.getValue().getUserId()).isEqualTo(USER_ID);
            assertThat(captor.getValue().getDeptId()).isEqualTo(DEPT_ID);
        }
    }

    @Nested
    @DisplayName("编辑用户")
    class Update {

        private UpdateUserDTO dto() {
            return UpdateUserDTO.builder()
                    .id(USER_ID).username(USERNAME).name("新名字")
                    .phone(PHONE).email(EMAIL).status("ENABLED").build();
        }

        @Test
        @DisplayName("用户不存在 - 抛出 999904")
        void update_userNotFound_throws999904() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(null);

            assertThatThrownBy(() -> userService.update(dto()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999904");
        }

        @Test
        @DisplayName("修改 admin 用户名 - 抛出 010205")
        void update_adminUsernameChanged_throws010205() {
            when(systemUserMapper.selectById(SystemConstants.ADMIN_USER_ID))
                    .thenReturn(user(SystemConstants.ADMIN_USER_ID, SystemConstants.ADMIN_USERNAME));
            UpdateUserDTO dto = dto();
            dto.setId(SystemConstants.ADMIN_USER_ID);
            dto.setUsername("not_admin");

            assertThatThrownBy(() -> userService.update(dto))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010205");
        }

        @Test
        @DisplayName("手机号被他人占用 - 抛出 010202")
        void update_phoneTakenByOther_throws010202() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(user(USER_ID, USERNAME));
            when(systemUserMapper.selectByPhone(PHONE)).thenReturn(user(99L, "other"));

            assertThatThrownBy(() -> userService.update(dto()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010202");
        }

        @Test
        @DisplayName("邮箱被他人占用 - 抛出 010203")
        void update_emailTakenByOther_throws010203() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(user(USER_ID, USERNAME));
            when(systemUserMapper.selectByPhone(PHONE)).thenReturn(user(USER_ID, USERNAME));
            when(systemUserMapper.selectByEmail(EMAIL)).thenReturn(user(99L, "other"));

            assertThatThrownBy(() -> userService.update(dto()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010203");
        }

        @Test
        @DisplayName("编辑成功 - 更新基础信息并重建部门关联")
        void update_success_updatesUserAndRebindsDept() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(user(USER_ID, USERNAME));
            when(systemUserMapper.selectByPhone(PHONE)).thenReturn(user(USER_ID, USERNAME));
            when(systemUserMapper.selectByEmail(EMAIL)).thenReturn(user(USER_ID, USERNAME));
            UpdateUserDTO dto = dto();
            dto.setDeptId(DEPT_ID);

            userService.update(dto);

            ArgumentCaptor<SystemUser> captor = ArgumentCaptor.forClass(SystemUser.class);
            verify(systemUserMapper).updateById(captor.capture());
            assertThat(captor.getValue().getName()).isEqualTo("新名字");
            assertThat(captor.getValue().getStatus()).isEqualTo("ENABLED");
            verify(systemUserDeptMapper).deleteByUserId(USER_ID);
            ArgumentCaptor<SystemUserDept> deptCaptor = ArgumentCaptor.forClass(SystemUserDept.class);
            verify(systemUserDeptMapper).insert(deptCaptor.capture());
            assertThat(deptCaptor.getValue().getDeptId()).isEqualTo(DEPT_ID);
        }
    }

    @Nested
    @DisplayName("删除用户")
    class Delete {

        @Test
        @DisplayName("用户不存在 - 抛出 999904")
        void delete_userNotFound_throws999904() {
            when(systemUserMapper.selectById(99L)).thenReturn(null);

            assertThatThrownBy(() -> userService.delete(99L))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999904");
        }

        @Test
        @DisplayName("删除 admin 账号 - 抛出 010204")
        void delete_adminUser_throws010204() {
            when(systemUserMapper.selectById(SystemConstants.ADMIN_USER_ID))
                    .thenReturn(user(SystemConstants.ADMIN_USER_ID, SystemConstants.ADMIN_USERNAME));

            assertThatThrownBy(() -> userService.delete(SystemConstants.ADMIN_USER_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010204");
        }

        @Test
        @DisplayName("删除成功 - 级联删除角色部门关联并清权限缓存")
        void delete_success_cascadesAndClearsCache() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(user(USER_ID, USERNAME));

            userService.delete(USER_ID);

            verify(systemUserMapper).deleteById(USER_ID);
            verify(systemUserRoleMapper).deleteByUserId(USER_ID);
            verify(systemUserDeptMapper).deleteByUserId(USER_ID);
            verify(stringRedisTemplate).delete(SystemConstants.REDIS_USER_PERMS_PREFIX + USER_ID);
        }
    }

    @Nested
    @DisplayName("查询用户详情")
    class GetById {

        @Test
        @DisplayName("用户不存在 - 抛出 999904")
        void getById_userNotFound_throws999904() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(null);

            assertThatThrownBy(() -> userService.getById(USER_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999904");
        }

        @Test
        @DisplayName("查询成功 - 组装角色与部门信息")
        void getById_success_assemblesRolesAndDept() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(user(USER_ID, USERNAME));
            when(systemRoleMapper.selectByUserId(USER_ID)).thenReturn(List.of(role(ROLE_ID, "管理员", "admin_role")));
            SystemUserDept userDept = new SystemUserDept();
            userDept.setUserId(USER_ID);
            userDept.setDeptId(DEPT_ID);
            when(systemUserDeptMapper.selectByUserId(USER_ID)).thenReturn(userDept);
            when(systemDeptMapper.selectById(DEPT_ID)).thenReturn(dept(DEPT_ID, "研发部"));

            UserDetailVO vo = userService.getById(USER_ID);

            assertThat(vo.getId()).isEqualTo(USER_ID);
            assertThat(vo.getRoleIds()).containsExactly(ROLE_ID);
            assertThat(vo.getRoleNames()).containsExactly("管理员");
            assertThat(vo.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(vo.getDeptName()).isEqualTo("研发部");
        }

        @Test
        @DisplayName("用户无部门关联 - 部门信息为空")
        void getById_noDept_returnsNullDeptInfo() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(user(USER_ID, USERNAME));
            when(systemRoleMapper.selectByUserId(USER_ID)).thenReturn(Collections.emptyList());
            when(systemUserDeptMapper.selectByUserId(USER_ID)).thenReturn(null);

            UserDetailVO vo = userService.getById(USER_ID);

            assertThat(vo.getDeptId()).isNull();
            assertThat(vo.getDeptName()).isNull();
            assertThat(vo.getRoleIds()).isEmpty();
        }
    }

    @Nested
    @DisplayName("分页查询用户")
    class PageQuery {

        private void stubSelectPage(List<SystemUser> records) {
            when(systemUserMapper.selectPage(any(), any())).thenAnswer(invocation -> {
                Page<SystemUser> page = invocation.getArgument(0);
                page.setRecords(records);
                page.setTotal(records.size());
                return page;
            });
        }

        @Test
        @DisplayName("带关键词与状态过滤 - 返回用户列表并组装角色部门")
        void page_withFilters_returnsAssembledVOs() {
            UserPageQuery query = new UserPageQuery();
            query.setKeyword("u1");
            query.setStatus("ENABLED");
            stubSelectPage(List.of(user(USER_ID, USERNAME)));

            SystemUserRole userRole = new SystemUserRole();
            userRole.setUserId(USER_ID);
            userRole.setRoleId(ROLE_ID);
            when(systemUserRoleMapper.selectList(any())).thenReturn(List.of(userRole));
            when(systemRoleMapper.selectBatchIds(any())).thenReturn(List.of(role(ROLE_ID, "管理员", "admin_role")));
            SystemUserDept userDept = new SystemUserDept();
            userDept.setUserId(USER_ID);
            userDept.setDeptId(DEPT_ID);
            when(systemUserDeptMapper.selectList(any())).thenReturn(List.of(userDept));
            when(systemDeptMapper.selectBatchIds(any())).thenReturn(List.of(dept(DEPT_ID, "研发部")));

            PageResult<UserListVO> result = userService.page(query);

            assertThat(result.getData()).hasSize(1);
            UserListVO vo = result.getData().get(0);
            assertThat(vo.getUsername()).isEqualTo(USERNAME);
            assertThat(vo.getRoleIds()).containsExactly(ROLE_ID);
            assertThat(vo.getRoleNames()).containsExactly("管理员");
            assertThat(vo.getDeptId()).isEqualTo(DEPT_ID);
            assertThat(vo.getDeptName()).isEqualTo("研发部");
            assertThat(result.getPageInfo().getTotal()).isEqualTo(1);
        }

        @Test
        @DisplayName("查询结果为空 - 返回空列表且不查关联表")
        void page_emptyRecords_returnsEmptyWithoutRelationQueries() {
            stubSelectPage(Collections.emptyList());

            PageResult<UserListVO> result = userService.page(new UserPageQuery());

            assertThat(result.getData()).isEmpty();
            verify(systemUserRoleMapper, never()).selectList(any());
        }

        @Test
        @DisplayName("按部门筛选但部门不存在 - 抛出 999904")
        void page_deptNotFound_throws999904() {
            UserPageQuery query = new UserPageQuery();
            query.setDeptId(DEPT_ID);
            when(systemDeptMapper.selectById(DEPT_ID)).thenReturn(null);

            assertThatThrownBy(() -> userService.page(query))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999904");
        }

        @Test
        @DisplayName("按部门子树筛选 - 部门存在时正常分页")
        void page_deptSubtreeFilter_queriesPage() {
            UserPageQuery query = new UserPageQuery();
            query.setDeptId(DEPT_ID);
            when(systemDeptMapper.selectById(DEPT_ID)).thenReturn(dept(DEPT_ID, "研发部"));
            stubSelectPage(List.of(user(USER_ID, USERNAME)));
            when(systemUserRoleMapper.selectList(any())).thenReturn(Collections.emptyList());
            when(systemUserDeptMapper.selectList(any())).thenReturn(Collections.emptyList());

            PageResult<UserListVO> result = userService.page(query);

            assertThat(result.getData()).hasSize(1);
            verify(systemUserMapper).selectPage(any(), any());
        }
    }

    @Nested
    @DisplayName("启用停用用户")
    class EnableDisable {

        @Test
        @DisplayName("用户不存在 - 抛出 999904")
        void enableDisable_userNotFound_throws999904() {
            when(systemUserMapper.selectById(99L)).thenReturn(null);

            assertThatThrownBy(() -> userService.enableDisable(99L, "DISABLED"))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999904");
        }

        @Test
        @DisplayName("停用 admin 账号 - 抛出 010204")
        void enableDisable_disableAdmin_throws010204() {
            when(systemUserMapper.selectById(SystemConstants.ADMIN_USER_ID))
                    .thenReturn(user(SystemConstants.ADMIN_USER_ID, SystemConstants.ADMIN_USERNAME));

            assertThatThrownBy(() -> userService.enableDisable(SystemConstants.ADMIN_USER_ID, "DISABLED"))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010204");
        }

        @Test
        @DisplayName("启用 admin 账号 - 允许更新状态")
        void enableDisable_enableAdmin_allowed() {
            when(systemUserMapper.selectById(SystemConstants.ADMIN_USER_ID))
                    .thenReturn(user(SystemConstants.ADMIN_USER_ID, SystemConstants.ADMIN_USERNAME));

            userService.enableDisable(SystemConstants.ADMIN_USER_ID, "ENABLED");

            verify(systemUserMapper).updateById(any(SystemUser.class));
        }

        @Test
        @DisplayName("停用普通用户 - 更新状态为 DISABLED")
        void enableDisable_normalUser_updatesStatus() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(user(USER_ID, USERNAME));

            userService.enableDisable(USER_ID, "DISABLED");

            ArgumentCaptor<SystemUser> captor = ArgumentCaptor.forClass(SystemUser.class);
            verify(systemUserMapper).updateById(captor.capture());
            assertThat(captor.getValue().getStatus()).isEqualTo("DISABLED");
        }
    }

    @Nested
    @DisplayName("重置密码")
    class ResetPassword {

        @Test
        @DisplayName("用户不存在 - 抛出 999904")
        void resetPassword_userNotFound_throws999904() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(null);

            assertThatThrownBy(() -> userService.resetPassword(USER_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999904");
        }

        @Test
        @DisplayName("重置成功 - 密码置为初始密码并清空改密时间")
        void resetPassword_success_resetsToInitialPassword() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(user(USER_ID, USERNAME));
            when(passwordEncoder.encode(SystemConstants.INITIAL_PASSWORD)).thenReturn("encoded-initial");

            userService.resetPassword(USER_ID);

            ArgumentCaptor<SystemUser> captor = ArgumentCaptor.forClass(SystemUser.class);
            verify(systemUserMapper).updateById(captor.capture());
            assertThat(captor.getValue().getPassword()).isEqualTo("encoded-initial");
            assertThat(captor.getValue().getPwdUpdateTime()).isNull();
        }
    }

    @Nested
    @DisplayName("分配角色")
    class AssignRoles {

        @Test
        @DisplayName("用户不存在 - 抛出 999904")
        void assignRoles_userNotFound_throws999904() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(null);

            assertThatThrownBy(() -> userService.assignRoles(USER_ID, List.of(ROLE_ID)))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999904");
        }

        @Test
        @DisplayName("给非 admin 分配超管角色 - 抛出 010206")
        void assignRoles_superAdminToNonAdmin_throws010206() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(user(USER_ID, USERNAME));
            when(systemRoleMapper.selectById(ROLE_ID))
                    .thenReturn(role(ROLE_ID, "超级管理员", SystemConstants.ROLE_SUPER_ADMIN));

            assertThatThrownBy(() -> userService.assignRoles(USER_ID, List.of(ROLE_ID)))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010206");
        }

        @Test
        @DisplayName("给 admin 分配超管角色 - 允许并重建关联")
        void assignRoles_superAdminToAdmin_allowed() {
            Long adminId = SystemConstants.ADMIN_USER_ID;
            when(systemUserMapper.selectById(adminId))
                    .thenReturn(user(adminId, SystemConstants.ADMIN_USERNAME));
            when(systemRoleMapper.selectById(ROLE_ID))
                    .thenReturn(role(ROLE_ID, "超级管理员", SystemConstants.ROLE_SUPER_ADMIN));

            userService.assignRoles(adminId, List.of(ROLE_ID));

            verify(systemUserRoleMapper).deleteByUserId(adminId);
            verify(systemUserRoleMapper).insert(any(SystemUserRole.class));
        }

        @Test
        @DisplayName("分配成功 - 先删旧关联再插入新关联并清权限缓存")
        void assignRoles_success_replacesRelationsAndClearsCache() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(user(USER_ID, USERNAME));
            when(systemRoleMapper.selectById(any())).thenReturn(role(ROLE_ID, "普通角色", "normal"));

            userService.assignRoles(USER_ID, List.of(ROLE_ID, 101L));

            verify(systemUserRoleMapper).deleteByUserId(USER_ID);
            ArgumentCaptor<SystemUserRole> captor = ArgumentCaptor.forClass(SystemUserRole.class);
            verify(systemUserRoleMapper, Mockito.times(2)).insert(captor.capture());
            assertThat(captor.getAllValues()).extracting(SystemUserRole::getRoleId)
                    .containsExactly(ROLE_ID, 101L);
            verify(stringRedisTemplate).delete(SystemConstants.REDIS_USER_PERMS_PREFIX + USER_ID);
        }

        @Test
        @DisplayName("角色列表为 null - 仅删除旧关联并清缓存")
        void assignRoles_nullRoleIds_onlyDeletes() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(user(USER_ID, USERNAME));

            userService.assignRoles(USER_ID, null);

            verify(systemUserRoleMapper).deleteByUserId(USER_ID);
            verify(systemUserRoleMapper, never()).insert(any(SystemUserRole.class));
            verify(stringRedisTemplate).delete(SystemConstants.REDIS_USER_PERMS_PREFIX + USER_ID);
        }
    }

    @Nested
    @DisplayName("分配部门")
    class AssignDept {

        @Test
        @DisplayName("用户不存在 - 抛出 999904")
        void assignDept_userNotFound_throws999904() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(null);

            assertThatThrownBy(() -> userService.assignDept(USER_ID, DEPT_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999904");
        }

        @Test
        @DisplayName("部门不存在 - 抛出 999904")
        void assignDept_deptNotFound_throws999904() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(user(USER_ID, USERNAME));
            when(systemDeptMapper.selectById(DEPT_ID)).thenReturn(null);

            assertThatThrownBy(() -> userService.assignDept(USER_ID, DEPT_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999904");
        }

        @Test
        @DisplayName("分配成功 - 重建用户部门关联")
        void assignDept_success_rebindsDept() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(user(USER_ID, USERNAME));
            when(systemDeptMapper.selectById(DEPT_ID)).thenReturn(dept(DEPT_ID, "研发部"));

            userService.assignDept(USER_ID, DEPT_ID);

            verify(systemUserDeptMapper).deleteByUserId(USER_ID);
            ArgumentCaptor<SystemUserDept> captor = ArgumentCaptor.forClass(SystemUserDept.class);
            verify(systemUserDeptMapper).insert(captor.capture());
            assertThat(captor.getValue().getDeptId()).isEqualTo(DEPT_ID);
        }

        @Test
        @DisplayName("部门为 null - 仅清空关联不插入")
        void assignDept_nullDeptId_onlyDeletes() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(user(USER_ID, USERNAME));

            userService.assignDept(USER_ID, null);

            verify(systemUserDeptMapper).deleteByUserId(USER_ID);
            verify(systemUserDeptMapper, never()).insert(any(SystemUserDept.class));
        }
    }

    @Nested
    @DisplayName("批量导入用户")
    class ImportUsers {

        private MultipartFile csv(String content) {
            return new MockMultipartFile("file", "users.csv", "text/csv",
                    content.getBytes(StandardCharsets.UTF_8));
        }

        @Test
        @DisplayName("全部合法行 - 导入成功")
        void importUsers_validRows_allSuccess() {
            when(passwordEncoder.encode(SystemConstants.INITIAL_PASSWORD)).thenReturn("encoded-initial");
            when(systemUserMapper.selectByUsername(anyString())).thenReturn(null);

            ImportResultVO result = userService.importUsers(csv("""
                    username,name,phone,email
                    u1,张三,13800000001,z1@example.com
                    u2,李四,,
                    """));

            assertThat(result.getTotalCount()).isEqualTo(2);
            assertThat(result.getSuccessCount()).isEqualTo(2);
            assertThat(result.getFailCount()).isZero();
            assertThat(result.getFailDetails()).isEmpty();
            verify(systemUserMapper, Mockito.times(2)).insert(any(SystemUser.class));
        }

        @Test
        @DisplayName("字段不足与用户名重复 - 记录失败明细")
        void importUsers_invalidRows_recordsFailDetails() {
            when(systemUserMapper.selectByUsername("dup")).thenReturn(user(99L, "dup"));

            ImportResultVO result = userService.importUsers(csv("""
                    username,name,phone,email
                    badrow
                    dup,王五,,
                    """));

            assertThat(result.getTotalCount()).isEqualTo(2);
            assertThat(result.getSuccessCount()).isZero();
            assertThat(result.getFailCount()).isEqualTo(2);
            assertThat(result.getFailDetails()).hasSize(2);
            assertThat(result.getFailDetails().get(0).getRowNum()).isEqualTo(2);
            assertThat(result.getFailDetails().get(0).getReason()).isEqualTo("字段不足");
            assertThat(result.getFailDetails().get(1).getUsername()).isEqualTo("dup");
            assertThat(result.getFailDetails().get(1).getReason()).isEqualTo("用户名已存在");
        }

        @Test
        @DisplayName("读取文件异常 - 抛出 999999")
        void importUsers_ioError_throws999999() throws IOException {
            MultipartFile broken = Mockito.mock(MultipartFile.class);
            when(broken.getInputStream()).thenThrow(new IOException("boom"));

            assertThatThrownBy(() -> userService.importUsers(broken))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999999");
        }
    }

    @Nested
    @DisplayName("导出用户")
    class ExportUsers {

        @Test
        @DisplayName("带筛选条件导出 - 返回组装后的列表")
        void exportUsers_withFilters_returnsVOs() {
            UserPageQuery query = new UserPageQuery();
            query.setKeyword("u1");
            query.setStatus("ENABLED");
            query.setDeptId(DEPT_ID);
            when(systemDeptMapper.selectById(DEPT_ID)).thenReturn(dept(DEPT_ID, "研发部"));
            when(systemUserMapper.selectList(any())).thenReturn(List.of(user(USER_ID, USERNAME)));
            SystemUserRole userRole = new SystemUserRole();
            userRole.setUserId(USER_ID);
            userRole.setRoleId(ROLE_ID);
            when(systemUserRoleMapper.selectList(any())).thenReturn(List.of(userRole));
            when(systemRoleMapper.selectBatchIds(any())).thenReturn(List.of(role(ROLE_ID, "管理员", "admin_role")));
            SystemUserDept userDept = new SystemUserDept();
            userDept.setUserId(USER_ID);
            userDept.setDeptId(DEPT_ID);
            when(systemUserDeptMapper.selectList(any())).thenReturn(List.of(userDept));
            when(systemDeptMapper.selectBatchIds(any())).thenReturn(List.of(dept(DEPT_ID, "研发部")));

            List<UserListVO> result = userService.exportUsers(query);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getRoleNames()).containsExactly("管理员");
            assertThat(result.get(0).getDeptName()).isEqualTo("研发部");
        }

        @Test
        @DisplayName("导出时部门不存在 - 抛出 999904")
        void exportUsers_deptNotFound_throws999904() {
            UserPageQuery query = new UserPageQuery();
            query.setDeptId(DEPT_ID);
            when(systemDeptMapper.selectById(DEPT_ID)).thenReturn(null);

            assertThatThrownBy(() -> userService.exportUsers(query))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999904");
        }

        @Test
        @DisplayName("查询结果为空 - 返回空列表")
        void exportUsers_emptyResult_returnsEmpty() {
            when(systemUserMapper.selectList(any())).thenReturn(Collections.emptyList());

            assertThat(userService.exportUsers(new UserPageQuery())).isEmpty();
            verify(systemUserRoleMapper, never()).selectList(any());
        }
    }
}
