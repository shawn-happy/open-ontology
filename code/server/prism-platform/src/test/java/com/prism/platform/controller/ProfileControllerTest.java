package com.prism.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prism.common.exception.BusinessException;
import com.prism.common.exception.GlobalExceptionHandler;
import com.prism.platform.dto.ChangePasswordDTO;
import com.prism.platform.dto.UpdateProfileDTO;
import com.prism.platform.service.ProfileService;
import com.prism.platform.vo.UserProfileVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 个人信息控制器 MockMvc 薄壳测试（路由 / 参数绑定与校验 / 统一响应结构）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("个人信息控制器")
class ProfileControllerTest {

    private static final String BASE = "/web/system/profile";

    @Mock
    private ProfileService profileService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ProfileController(profileService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /system/profile：返回当前用户个人信息")
    void getProfile_returnsProfile() throws Exception {
        when(profileService.getProfile()).thenReturn(UserProfileVO.builder()
                .id(100L)
                .username("zhangsan")
                .name("张三")
                .phone("13800138000")
                .email("zhangsan@example.com")
                .status("ENABLED")
                .roleNames(List.of("数据开发者"))
                .deptName("研发部")
                .build());

        mockMvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.username").value("zhangsan"))
                .andExpect(jsonPath("$.data.deptName").value("研发部"))
                .andExpect(jsonPath("$.data.roleNames[0]").value("数据开发者"));

        verify(profileService).getProfile();
    }

    @Test
    @DisplayName("POST /system/profile/update：合法请求转发服务并返回成功")
    void updateProfile_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        Map.of("phone", "13900139000", "email", "new@example.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        ArgumentCaptor<UpdateProfileDTO> captor = ArgumentCaptor.forClass(UpdateProfileDTO.class);
        verify(profileService).updateProfile(captor.capture());
        assertThat(captor.getValue().getPhone()).isEqualTo("13900139000");
        assertThat(captor.getValue().getEmail()).isEqualTo("new@example.com");
    }

    @Test
    @DisplayName("POST /system/profile/update：手机号格式非法 → 999901 参数校验失败")
    void updateProfile_invalidPhone_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("phone", "12345"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /system/profile/change-password：合法请求转发服务并返回成功")
    void changePassword_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "oldPassword", "OldPass@123",
                        "newPassword", "NewPass@123",
                        "confirmPassword", "NewPass@123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        ArgumentCaptor<ChangePasswordDTO> captor = ArgumentCaptor.forClass(ChangePasswordDTO.class);
        verify(profileService).changePassword(captor.capture());
        assertThat(captor.getValue().getOldPassword()).isEqualTo("OldPass@123");
        assertThat(captor.getValue().getNewPassword()).isEqualTo("NewPass@123");
    }

    @Test
    @DisplayName("POST /system/profile/change-password：确认密码缺失 → 999901 参数校验失败")
    void changePassword_missingConfirmPassword_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "oldPassword", "OldPass@123",
                        "newPassword", "NewPass@123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /system/profile/change-password：服务校验失败 → 透出业务码 999901")
    void changePassword_serviceThrows_propagatesBusinessCode() throws Exception {
        doThrow(new BusinessException("999901", "两次输入的密码不一致"))
                .when(profileService)
                .changePassword(any(ChangePasswordDTO.class));

        mockMvc.perform(post(BASE + "/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "oldPassword", "OldPass@123",
                        "newPassword", "NewPass@123",
                        "confirmPassword", "OtherPass@123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"))
                .andExpect(jsonPath("$.msg").value("两次输入的密码不一致"));
    }

    @Test
    @DisplayName("POST /system/profile/upload-avatar：上传文件返回头像 URL")
    void uploadAvatar_validFile_returnsUrl() throws Exception {
        when(profileService.uploadAvatar(any(MultipartFile.class)))
                .thenReturn("http://minio/avatar/100.png");
        MockMultipartFile file = new MockMultipartFile(
                "file", "avatar.png", "image/png", new byte[] { 1, 2, 3 });

        mockMvc.perform(multipart(BASE + "/upload-avatar").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data").value("http://minio/avatar/100.png"));
    }
}
