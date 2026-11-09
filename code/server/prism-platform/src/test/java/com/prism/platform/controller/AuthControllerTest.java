package com.prism.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prism.common.exception.BusinessException;
import com.prism.common.exception.GlobalExceptionHandler;
import com.prism.platform.dto.ChangePasswordDTO;
import com.prism.platform.dto.ForgotPasswordDTO;
import com.prism.platform.dto.LoginDTO;
import com.prism.platform.dto.RefreshTokenDTO;
import com.prism.platform.dto.SendVerifyCodeDTO;
import com.prism.platform.service.AuthService;
import com.prism.platform.service.CaptchaService;
import com.prism.platform.vo.CaptchaVO;
import com.prism.platform.vo.LoginVO;
import com.prism.platform.vo.UserInfoVO;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 认证控制器 MockMvc 薄壳测试（路由 / 参数绑定与校验 / 统一响应结构）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("认证控制器")
class AuthControllerTest {

    private static final String BASE = "/web/auth";

    @Mock
    private AuthService authService;

    @Mock
    private CaptchaService captchaService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AuthController(authService, captchaService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /login：合法请求返回 Token 与用户信息")
    void login_validRequest_returnsTokenAndUserInfo() throws Exception {
        LoginVO vo = LoginVO.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .firstLogin(false)
                .passwordExpired(false)
                .userInfo(UserInfoVO.builder()
                        .id(1L)
                        .username("admin")
                        .name("管理员")
                        .build())
                .permissions(List.of("system:user:list"))
                .build();
        when(authService.login(any(LoginDTO.class))).thenReturn(vo);

        mockMvc.perform(post(BASE + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "loginType", "USERNAME",
                        "username", "admin",
                        "password", "Admin@123",
                        "captchaKey", "key-1",
                        "captchaCode", "1234"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.accessToken").value("access-token"))
                .andExpect(jsonPath("$.data.userInfo.username").value("admin"))
                .andExpect(jsonPath("$.data.permissions[0]").value("system:user:list"));

        ArgumentCaptor<LoginDTO> captor = ArgumentCaptor.forClass(LoginDTO.class);
        verify(authService).login(captor.capture());
        assertThat(captor.getValue().getLoginType()).isEqualTo("USERNAME");
        assertThat(captor.getValue().getUsername()).isEqualTo("admin");
        assertThat(captor.getValue().getCaptchaKey()).isEqualTo("key-1");
    }

    @Test
    @DisplayName("POST /login：密码缺失 → 999901 参数校验失败")
    void login_missingPassword_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "loginType", "USERNAME",
                        "username", "admin",
                        "captchaKey", "key-1",
                        "captchaCode", "1234"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /login：验证码错误 → 透出业务码 010102")
    void login_captchaError_propagatesBusinessCode() throws Exception {
        when(authService.login(any(LoginDTO.class)))
                .thenThrow(new BusinessException("010102", "验证码错误或已过期"));

        mockMvc.perform(post(BASE + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "loginType", "USERNAME",
                        "username", "admin",
                        "password", "Admin@123",
                        "captchaKey", "key-1",
                        "captchaCode", "0000"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("010102"));
    }

    @Test
    @DisplayName("POST /logout：携带 Authorization 头 → 透传给服务")
    void logout_withTokenHeader_delegatesToService() throws Exception {
        mockMvc.perform(post(BASE + "/logout").header("Authorization", "Bearer token-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        verify(authService).logout("Bearer token-1");
    }

    @Test
    @DisplayName("POST /logout：未携带 Authorization 头 → 以 null 透传仍返回成功")
    void logout_withoutTokenHeader_stillReturnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        verify(authService).logout(null);
    }

    @Test
    @DisplayName("POST /refresh-token：合法请求返回新 Token")
    void refreshToken_validRequest_returnsNewToken() throws Exception {
        when(authService.refreshToken(any(RefreshTokenDTO.class))).thenReturn("new-token");

        mockMvc.perform(post(BASE + "/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("refreshToken", "old-refresh"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data").value("new-token"));

        ArgumentCaptor<RefreshTokenDTO> captor = ArgumentCaptor.forClass(RefreshTokenDTO.class);
        verify(authService).refreshToken(captor.capture());
        assertThat(captor.getValue().getRefreshToken()).isEqualTo("old-refresh");
    }

    @Test
    @DisplayName("POST /refresh-token：Refresh Token 为空 → 999901 参数校验失败")
    void refreshToken_blankToken_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("refreshToken", ""))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("GET /captcha：返回验证码 Key 与图片")
    void captcha_returnsKeyAndImage() throws Exception {
        when(captchaService.generateCaptcha()).thenReturn(CaptchaVO.builder()
                .captchaKey("key-1")
                .captchaImage("base64-image")
                .build());

        mockMvc.perform(get(BASE + "/captcha"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.captchaKey").value("key-1"))
                .andExpect(jsonPath("$.data.captchaImage").value("base64-image"));
    }

    @Test
    @DisplayName("POST /send-verify-code：合法请求返回成功")
    void sendVerifyCode_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/send-verify-code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        Map.of("accountType", "phone", "account", "13800138000"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        ArgumentCaptor<SendVerifyCodeDTO> captor = ArgumentCaptor.forClass(SendVerifyCodeDTO.class);
        verify(authService).sendVerifyCode(captor.capture());
        assertThat(captor.getValue().getAccountType()).isEqualTo("phone");
        assertThat(captor.getValue().getAccount()).isEqualTo("13800138000");
    }

    @Test
    @DisplayName("POST /send-verify-code：账号缺失 → 999901 参数校验失败")
    void sendVerifyCode_missingAccount_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/send-verify-code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("accountType", "phone"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }

    @Test
    @DisplayName("POST /send-verify-code：发送过于频繁 → 透出业务码 010109")
    void sendVerifyCode_tooFrequent_propagatesBusinessCode() throws Exception {
        doThrow(new BusinessException("010109", "验证码发送过于频繁，请稍后再试"))
                .when(authService)
                .sendVerifyCode(any(SendVerifyCodeDTO.class));

        mockMvc.perform(post(BASE + "/send-verify-code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        Map.of("accountType", "email", "account", "a@b.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("010109"));
    }

    @Test
    @DisplayName("POST /forgot-password：合法请求返回成功")
    void forgotPassword_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "accountType", "email",
                        "account", "a@b.com",
                        "verifyCode", "123456"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        ArgumentCaptor<ForgotPasswordDTO> captor = ArgumentCaptor.forClass(ForgotPasswordDTO.class);
        verify(authService).forgotPassword(captor.capture());
        assertThat(captor.getValue().getVerifyCode()).isEqualTo("123456");
    }

    @Test
    @DisplayName("POST /forgot-password：验证码错误 → 透出业务码 010110")
    void forgotPassword_verifyCodeError_propagatesBusinessCode() throws Exception {
        doThrow(new BusinessException("010110", "验证码错误或已过期"))
                .when(authService)
                .forgotPassword(any(ForgotPasswordDTO.class));

        mockMvc.perform(post(BASE + "/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "accountType", "email",
                        "account", "a@b.com",
                        "verifyCode", "000000"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("010110"));
    }

    @Test
    @DisplayName("POST /change-password-first-time：合法请求返回成功")
    void changePasswordFirstTime_validRequest_returnsOk() throws Exception {
        mockMvc.perform(post(BASE + "/change-password-first-time")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "oldPassword", "Init@123",
                        "newPassword", "NewPass@123",
                        "confirmPassword", "NewPass@123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000000"));

        ArgumentCaptor<ChangePasswordDTO> captor = ArgumentCaptor.forClass(ChangePasswordDTO.class);
        verify(authService).changePasswordFirstTime(captor.capture());
        assertThat(captor.getValue().getNewPassword()).isEqualTo("NewPass@123");
    }

    @Test
    @DisplayName("POST /change-password-first-time：新密码长度不足 8 位 → 999901 参数校验失败")
    void changePasswordFirstTime_shortNewPassword_returns999901() throws Exception {
        mockMvc.perform(post(BASE + "/change-password-first-time")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "oldPassword", "Init@123",
                        "newPassword", "short",
                        "confirmPassword", "short"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("999901"));
    }
}
