package com.prism.platform.service.impl;

import com.prism.common.exception.BusinessException;
import com.prism.common.security.SecurityUtils;
import com.prism.platform.dto.ChangePasswordDTO;
import com.prism.platform.dto.UpdateProfileDTO;
import com.prism.platform.entity.SystemDept;
import com.prism.platform.entity.SystemRole;
import com.prism.platform.entity.SystemUser;
import com.prism.platform.entity.SystemUserDept;
import com.prism.platform.mapper.SystemDeptMapper;
import com.prism.platform.mapper.SystemRoleMapper;
import com.prism.platform.mapper.SystemUserDeptMapper;
import com.prism.platform.mapper.SystemUserMapper;
import com.prism.platform.vo.UserProfileVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 个人信息服务测试（查询/修改个人信息、修改密码、上传头像）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("个人信息服务")
class ProfileServiceImplTest {

    private static final Long USER_ID = 20L;
    private static final String VALID_NEW_PWD = "Abcd1234!";

    @Mock
    private SystemUserMapper systemUserMapper;

    @Mock
    private SystemRoleMapper systemRoleMapper;

    @Mock
    private SystemUserDeptMapper systemUserDeptMapper;

    @Mock
    private SystemDeptMapper systemDeptMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private MockedStatic<SecurityUtils> securityUtils;

    @BeforeEach
    void setUp() {
        securityUtils = Mockito.mockStatic(SecurityUtils.class);
        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(USER_ID);
    }

    @AfterEach
    void tearDown() {
        securityUtils.close();
    }

    private SystemUser user() {
        SystemUser user = new SystemUser();
        user.setId(USER_ID);
        user.setUsername("zhangsan");
        user.setName("张三");
        user.setPhone("13800000000");
        user.setEmail("zhangsan@example.com");
        user.setAvatar("/avatars/a.png");
        user.setStatus("ENABLED");
        user.setPassword("encoded-old");
        user.setLastLoginAt(LocalDateTime.of(2026, 11, 1, 9, 0));
        user.setPwdUpdateTime(LocalDateTime.of(2026, 10, 1, 9, 0));
        user.setCreatedAt(LocalDateTime.of(2026, 1, 1, 9, 0));
        return user;
    }

    private SystemRole role(String name) {
        SystemRole role = new SystemRole();
        role.setName(name);
        return role;
    }

    private String codeOf(BusinessException e) {
        return e.getCode();
    }

    private void unauthenticated() {
        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(null);
    }

    // ===== getProfile =====

    @Test
    @DisplayName("查询个人信息：未认证 → 999902")
    void getProfile_unauthenticated_throws999902() {
        unauthenticated();

        assertThatThrownBy(() -> profileService.getProfile())
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999902");
    }

    @Test
    @DisplayName("查询个人信息：用户不存在 → 999904")
    void getProfile_userNotFound_throws999904() {
        when(systemUserMapper.selectById(USER_ID)).thenReturn(null);

        assertThatThrownBy(() -> profileService.getProfile())
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999904");
    }

    @Test
    @DisplayName("查询个人信息：组装角色名列表与部门名称")
    void getProfile_withRolesAndDept_buildsVO() {
        when(systemUserMapper.selectById(USER_ID)).thenReturn(user());
        when(systemRoleMapper.selectByUserId(USER_ID))
                .thenReturn(List.of(role("系统管理员"), role("普通用户")));
        SystemUserDept ud = new SystemUserDept();
        ud.setUserId(USER_ID);
        ud.setDeptId(10L);
        when(systemUserDeptMapper.selectByUserId(USER_ID)).thenReturn(ud);
        SystemDept dept = new SystemDept();
        dept.setId(10L);
        dept.setName("技术部");
        when(systemDeptMapper.selectById(10L)).thenReturn(dept);

        UserProfileVO vo = profileService.getProfile();

        assertThat(vo.getId()).isEqualTo(USER_ID);
        assertThat(vo.getUsername()).isEqualTo("zhangsan");
        assertThat(vo.getName()).isEqualTo("张三");
        assertThat(vo.getPhone()).isEqualTo("13800000000");
        assertThat(vo.getEmail()).isEqualTo("zhangsan@example.com");
        assertThat(vo.getAvatar()).isEqualTo("/avatars/a.png");
        assertThat(vo.getStatus()).isEqualTo("ENABLED");
        assertThat(vo.getRoleNames()).containsExactly("系统管理员", "普通用户");
        assertThat(vo.getDeptName()).isEqualTo("技术部");
        assertThat(vo.getLastLoginAt()).isEqualTo(LocalDateTime.of(2026, 11, 1, 9, 0));
        assertThat(vo.getPwdUpdateTime()).isEqualTo(LocalDateTime.of(2026, 10, 1, 9, 0));
        assertThat(vo.getCreatedAt()).isEqualTo(LocalDateTime.of(2026, 1, 1, 9, 0));
    }

    @Test
    @DisplayName("查询个人信息：无部门关联时部门名称为 null")
    void getProfile_noUserDept_returnsNullDeptName() {
        when(systemUserMapper.selectById(USER_ID)).thenReturn(user());
        when(systemRoleMapper.selectByUserId(USER_ID)).thenReturn(List.of());
        when(systemUserDeptMapper.selectByUserId(USER_ID)).thenReturn(null);

        UserProfileVO vo = profileService.getProfile();

        assertThat(vo.getDeptName()).isNull();
        assertThat(vo.getRoleNames()).isEmpty();
    }

    @Test
    @DisplayName("查询个人信息：部门关联指向的部门已删除时部门名称为 null")
    void getProfile_deptMissing_returnsNullDeptName() {
        when(systemUserMapper.selectById(USER_ID)).thenReturn(user());
        when(systemRoleMapper.selectByUserId(USER_ID)).thenReturn(List.of());
        SystemUserDept ud = new SystemUserDept();
        ud.setUserId(USER_ID);
        ud.setDeptId(10L);
        when(systemUserDeptMapper.selectByUserId(USER_ID)).thenReturn(ud);
        when(systemDeptMapper.selectById(10L)).thenReturn(null);

        assertThat(profileService.getProfile().getDeptName()).isNull();
    }

    // ===== updateProfile =====

    @Test
    @DisplayName("修改个人信息：未认证 → 999902")
    void updateProfile_unauthenticated_throws999902() {
        unauthenticated();

        assertThatThrownBy(() -> profileService.updateProfile(UpdateProfileDTO.builder().build()))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999902");
    }

    @Test
    @DisplayName("修改个人信息：仅更新手机号与邮箱")
    void updateProfile_updatesPhoneAndEmailOnly() {
        UpdateProfileDTO dto = UpdateProfileDTO.builder()
                .phone("13911112222").email("new@example.com")
                .build();

        profileService.updateProfile(dto);

        ArgumentCaptor<SystemUser> captor = ArgumentCaptor.forClass(SystemUser.class);
        verify(systemUserMapper).updateById(captor.capture());
        SystemUser updated = captor.getValue();
        assertThat(updated.getId()).isEqualTo(USER_ID);
        assertThat(updated.getPhone()).isEqualTo("13911112222");
        assertThat(updated.getEmail()).isEqualTo("new@example.com");
        assertThat(updated.getName()).isNull();
    }

    // ===== changePassword =====

    private ChangePasswordDTO.ChangePasswordDTOBuilder pwdBuilder() {
        return ChangePasswordDTO.builder()
                .oldPassword("OldPass1!")
                .newPassword(VALID_NEW_PWD)
                .confirmPassword(VALID_NEW_PWD);
    }

    @Test
    @DisplayName("修改密码：未认证 → 999902")
    void changePassword_unauthenticated_throws999902() {
        unauthenticated();

        assertThatThrownBy(() -> profileService.changePassword(pwdBuilder().build()))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999902");
    }

    @Test
    @DisplayName("修改密码：两次输入不一致 → 999901")
    void changePassword_confirmMismatch_throws999901() {
        ChangePasswordDTO dto = pwdBuilder().confirmPassword("Other123!").build();

        assertThatThrownBy(() -> profileService.changePassword(dto))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999901");
    }

    @ParameterizedTest(name = "弱密码 {0} → 999901")
    @ValueSource(strings = {
            "abcd1234!",              // 无大写
            "ABCD1234!",              // 无小写
            "Abcdefgh!",              // 无数字
            "Abcd12345",              // 无特殊字符
            "Ab1!",                   // 少于 8 位
            "Abcd1234567890123456!",  // 超过 20 位
    })
    @DisplayName("修改密码：新密码不满足复杂度规则 → 999901")
    void changePassword_weakNewPassword_throws999901(String weakPassword) {
        ChangePasswordDTO dto = pwdBuilder()
                .newPassword(weakPassword)
                .confirmPassword(weakPassword)
                .build();

        assertThatThrownBy(() -> profileService.changePassword(dto))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999901");
    }

    @Test
    @DisplayName("修改密码：用户不存在 → 999904")
    void changePassword_userNotFound_throws999904() {
        when(systemUserMapper.selectById(USER_ID)).thenReturn(null);

        assertThatThrownBy(() -> profileService.changePassword(pwdBuilder().build()))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999904");
    }

    @Test
    @DisplayName("修改密码：旧密码错误 → 010101")
    void changePassword_oldPasswordWrong_throws010101() {
        when(systemUserMapper.selectById(USER_ID)).thenReturn(user());
        when(passwordEncoder.matches("OldPass1!", "encoded-old")).thenReturn(false);

        assertThatThrownBy(() -> profileService.changePassword(pwdBuilder().build()))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("010101");
    }

    @Test
    @DisplayName("修改密码成功：写入加密新密码并刷新密码更新时间")
    void changePassword_success_updatesEncodedPasswordAndTime() {
        when(systemUserMapper.selectById(USER_ID)).thenReturn(user());
        when(passwordEncoder.matches("OldPass1!", "encoded-old")).thenReturn(true);
        when(passwordEncoder.encode(VALID_NEW_PWD)).thenReturn("encoded-new");

        profileService.changePassword(pwdBuilder().build());

        ArgumentCaptor<SystemUser> captor = ArgumentCaptor.forClass(SystemUser.class);
        verify(systemUserMapper).updateById(captor.capture());
        SystemUser updated = captor.getValue();
        assertThat(updated.getId()).isEqualTo(USER_ID);
        assertThat(updated.getPassword()).isEqualTo("encoded-new");
        assertThat(updated.getPwdUpdateTime()).isNotNull();
    }

    // ===== uploadAvatar =====

    @Test
    @DisplayName("上传头像：未认证 → 999902")
    void uploadAvatar_unauthenticated_throws999902() {
        unauthenticated();

        assertThatThrownBy(() -> profileService.uploadAvatar(multipartFile))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> codeOf((BusinessException) e))
                .isEqualTo("999902");
    }

    @Test
    @DisplayName("上传头像：返回占位 URL 并持久化头像字段")
    void uploadAvatar_returnsUrlAndPersists() {
        when(multipartFile.getOriginalFilename()).thenReturn("avatar.png");

        String url = profileService.uploadAvatar(multipartFile);

        assertThat(url).isEqualTo("/avatars/" + USER_ID + "_avatar.png");
        ArgumentCaptor<SystemUser> captor = ArgumentCaptor.forClass(SystemUser.class);
        verify(systemUserMapper).updateById(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(USER_ID);
        assertThat(captor.getValue().getAvatar()).isEqualTo(url);
    }
}
