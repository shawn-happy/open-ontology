package com.prism.platform.service.impl;

import com.prism.common.exception.BusinessException;
import com.prism.common.security.JwtTokenProvider;
import com.prism.common.security.SecurityUtils;
import com.prism.platform.constants.SystemConstants;
import com.prism.platform.dto.ChangePasswordDTO;
import com.prism.platform.dto.ForgotPasswordDTO;
import com.prism.platform.dto.LoginDTO;
import com.prism.platform.dto.RefreshTokenDTO;
import com.prism.platform.dto.SendVerifyCodeDTO;
import com.prism.platform.entity.SystemUser;
import com.prism.platform.mapper.SystemUserMapper;
import com.prism.platform.service.CaptchaService;
import com.prism.platform.service.MenuService;
import com.prism.platform.service.RoleService;
import com.prism.platform.vo.LoginVO;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 认证服务测试（登录/登出/刷新Token/忘记密码/首次改密）
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("认证服务")
class AuthServiceImplTest {

    private static final Long USER_ID = 10L;
    private static final String USERNAME = "u1";
    private static final String PASSWORD = "Pwd@1234";
    private static final String CAPTCHA_KEY = "captcha-key";
    private static final String CAPTCHA_CODE = "ab12";
    private static final String ACCESS_TOKEN = "access-token";
    private static final String REFRESH_TOKEN = "refresh-token";
    private static final int ACCESS_EXPIRE_SECONDS = 7200;

    @Mock
    private SystemUserMapper systemUserMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private CaptchaService captchaService;

    @Mock
    private RoleService roleService;

    @Mock
    private MenuService menuService;

    @InjectMocks
    private AuthServiceImpl authService;

    private MockedStatic<SecurityUtils> securityUtils;

    @BeforeEach
    void setUp() {
        securityUtils = Mockito.mockStatic(SecurityUtils.class);
        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(USER_ID);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @AfterEach
    void tearDown() {
        securityUtils.close();
    }

    private SystemUser user(String status) {
        SystemUser user = new SystemUser();
        user.setId(USER_ID);
        user.setUsername(USERNAME);
        user.setName("用户一");
        user.setPhone("13800000000");
        user.setEmail("u1@example.com");
        user.setPassword("encoded-old");
        user.setStatus(status);
        user.setLoginFailCount(0);
        return user;
    }

    private LoginDTO usernameLoginDTO() {
        return LoginDTO.builder()
                .loginType(SystemConstants.LOGIN_TYPE_USERNAME)
                .username(USERNAME)
                .password(PASSWORD)
                .captchaKey(CAPTCHA_KEY)
                .captchaCode(CAPTCHA_CODE)
                .rememberMe(Boolean.FALSE)
                .build();
    }

    private Jwt jwt(String tokenValue, String subject, String type, String username) {
        return new Jwt(tokenValue, Instant.now(), Instant.now().plusSeconds(3600),
                Map.of("alg", "RS256"),
                Map.of("sub", subject, "type", type, "username", username));
    }

    @Nested
    @DisplayName("用户登录")
    class Login {

        @Test
        @DisplayName("验证码错误 - 抛出 010102")
        void login_invalidCaptcha_throws010102() {
            when(captchaService.verifyCaptcha(CAPTCHA_KEY, CAPTCHA_CODE)).thenReturn(false);

            assertThatThrownBy(() -> authService.login(usernameLoginDTO()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010102");
        }

        @Test
        @DisplayName("用户不存在 - 抛出 010101")
        void login_userNotFound_throws010101() {
            when(captchaService.verifyCaptcha(CAPTCHA_KEY, CAPTCHA_CODE)).thenReturn(true);
            when(systemUserMapper.selectByUsername(USERNAME)).thenReturn(null);

            assertThatThrownBy(() -> authService.login(usernameLoginDTO()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010101");
        }

        @Test
        @DisplayName("账号已停用 - 抛出 010104")
        void login_disabledUser_throws010104() {
            when(captchaService.verifyCaptcha(CAPTCHA_KEY, CAPTCHA_CODE)).thenReturn(true);
            when(systemUserMapper.selectByUsername(USERNAME)).thenReturn(user("DISABLED"));

            assertThatThrownBy(() -> authService.login(usernameLoginDTO()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010104");
        }

        @Test
        @DisplayName("失败次数达上限且仍在锁定期 - 抛出 010103")
        void login_lockedUser_throws010103() {
            when(captchaService.verifyCaptcha(CAPTCHA_KEY, CAPTCHA_CODE)).thenReturn(true);
            SystemUser locked = user("ENABLED");
            locked.setLoginFailCount(SystemConstants.DEFAULT_MAX_FAIL_COUNT);
            locked.setLockUntil(LocalDateTime.now().plusMinutes(10));
            when(systemUserMapper.selectByUsername(USERNAME)).thenReturn(locked);

            assertThatThrownBy(() -> authService.login(usernameLoginDTO()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010103");
        }

        @Test
        @DisplayName("密码错误未达上限 - 抛出 010101 且失败计数加一不锁定")
        void login_wrongPasswordBelowMax_throws010101AndIncrementsFailCount() {
            when(captchaService.verifyCaptcha(CAPTCHA_KEY, CAPTCHA_CODE)).thenReturn(true);
            when(systemUserMapper.selectByUsername(USERNAME)).thenReturn(user("ENABLED"));
            when(passwordEncoder.matches(PASSWORD, "encoded-old")).thenReturn(false);

            assertThatThrownBy(() -> authService.login(usernameLoginDTO()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010101");

            verify(systemUserMapper).updateFailCount(eq(USER_ID), eq(1), isNull());
        }

        @Test
        @DisplayName("密码错误达到上限 - 抛出 010101 且写入锁定截止时间")
        void login_wrongPasswordReachesMax_throws010101AndSetsLockUntil() {
            when(captchaService.verifyCaptcha(CAPTCHA_KEY, CAPTCHA_CODE)).thenReturn(true);
            SystemUser almostLocked = user("ENABLED");
            almostLocked.setLoginFailCount(SystemConstants.DEFAULT_MAX_FAIL_COUNT - 1);
            when(systemUserMapper.selectByUsername(USERNAME)).thenReturn(almostLocked);
            when(passwordEncoder.matches(PASSWORD, "encoded-old")).thenReturn(false);

            assertThatThrownBy(() -> authService.login(usernameLoginDTO()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010101");

            ArgumentCaptor<LocalDateTime> lockCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
            verify(systemUserMapper).updateFailCount(eq(USER_ID),
                    eq(SystemConstants.DEFAULT_MAX_FAIL_COUNT), lockCaptor.capture());
            assertThat(lockCaptor.getValue()).isNotNull().isAfter(LocalDateTime.now());
        }

        @Test
        @DisplayName("登录成功 - 返回双 Token 并写入 Redis 与权限缓存")
        void login_success_returnsLoginVOAndCachesTokens() {
            when(captchaService.verifyCaptcha(CAPTCHA_KEY, CAPTCHA_CODE)).thenReturn(true);
            SystemUser ok = user("ENABLED");
            ok.setPwdUpdateTime(null);
            when(systemUserMapper.selectByUsername(USERNAME)).thenReturn(ok);
            when(passwordEncoder.matches(PASSWORD, "encoded-old")).thenReturn(true);
            when(jwtTokenProvider.generateAccessToken(USER_ID, USERNAME)).thenReturn(ACCESS_TOKEN);
            when(jwtTokenProvider.generateRefreshToken(USER_ID, USERNAME, false)).thenReturn(REFRESH_TOKEN);
            when(jwtTokenProvider.getAccessExpireSeconds()).thenReturn(ACCESS_EXPIRE_SECONDS);
            when(menuService.getPermissionsByUserId(USER_ID)).thenReturn(List.of("user:list"));

            LoginVO vo = authService.login(usernameLoginDTO());

            assertThat(vo.getAccessToken()).isEqualTo(ACCESS_TOKEN);
            assertThat(vo.getRefreshToken()).isEqualTo(REFRESH_TOKEN);
            assertThat(vo.getFirstLogin()).isTrue();
            assertThat(vo.getPasswordExpired()).isFalse();
            assertThat(vo.getUserInfo().getUsername()).isEqualTo(USERNAME);
            assertThat(vo.getPermissions()).containsExactly("user:list");

            String tokenHash = String.valueOf(ACCESS_TOKEN.hashCode());
            verify(valueOperations).set(
                    eq(SystemConstants.REDIS_TOKEN_ACCESS_PREFIX + USER_ID + ":" + tokenHash),
                    eq("1"), eq((long) ACCESS_EXPIRE_SECONDS), eq(TimeUnit.SECONDS));
            verify(valueOperations).set(
                    eq(SystemConstants.REDIS_TOKEN_SESSION_PREFIX + USER_ID),
                    eq(tokenHash), eq((long) ACCESS_EXPIRE_SECONDS), eq(TimeUnit.SECONDS));
            verify(valueOperations).set(
                    eq(SystemConstants.REDIS_USER_PERMS_PREFIX + USER_ID),
                    eq("user:list"), eq((long) ACCESS_EXPIRE_SECONDS), eq(TimeUnit.SECONDS));
            verify(systemUserMapper).updateFailCount(USER_ID, 0, null);
            verify(systemUserMapper).updateLoginInfo(eq(USER_ID), eq(""), any(LocalDateTime.class));
        }

        @Test
        @DisplayName("手机号登录 - 按手机号查询用户")
        void login_phoneType_queriesByPhone() {
            when(captchaService.verifyCaptcha(CAPTCHA_KEY, CAPTCHA_CODE)).thenReturn(true);
            LoginDTO dto = usernameLoginDTO();
            dto.setLoginType(SystemConstants.LOGIN_TYPE_PHONE);
            dto.setPhone("13800000000");
            when(systemUserMapper.selectByPhone("13800000000")).thenReturn(null);

            assertThatThrownBy(() -> authService.login(dto))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010101");

            verify(systemUserMapper).selectByPhone("13800000000");
            verify(systemUserMapper, never()).selectByUsername(anyString());
        }

        @Test
        @DisplayName("邮箱登录 - 按邮箱查询用户")
        void login_emailType_queriesByEmail() {
            when(captchaService.verifyCaptcha(CAPTCHA_KEY, CAPTCHA_CODE)).thenReturn(true);
            LoginDTO dto = usernameLoginDTO();
            dto.setLoginType(SystemConstants.LOGIN_TYPE_EMAIL);
            dto.setEmail("u1@example.com");
            when(systemUserMapper.selectByEmail("u1@example.com")).thenReturn(null);

            assertThatThrownBy(() -> authService.login(dto))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010101");

            verify(systemUserMapper).selectByEmail("u1@example.com");
            verify(systemUserMapper, never()).selectByUsername(anyString());
        }
    }

    @Nested
    @DisplayName("用户登出")
    class Logout {

        @Test
        @DisplayName("Token 为空 - 直接返回不做任何操作")
        void logout_blankToken_noInteraction() {
            authService.logout(" ");

            verify(jwtTokenProvider, never()).parseToken(anyString());
            verify(stringRedisTemplate, never()).delete(anyString());
        }

        @Test
        @DisplayName("Bearer 前缀 Token - 去前缀解析并删除 Redis 键")
        void logout_bearerToken_deletesRedisKeys() {
            when(jwtTokenProvider.parseToken(ACCESS_TOKEN))
                    .thenReturn(jwt(ACCESS_TOKEN, String.valueOf(USER_ID), "access", USERNAME));

            authService.logout("Bearer " + ACCESS_TOKEN);

            String tokenHash = String.valueOf(ACCESS_TOKEN.hashCode());
            verify(stringRedisTemplate)
                    .delete(SystemConstants.REDIS_TOKEN_ACCESS_PREFIX + USER_ID + ":" + tokenHash);
            verify(stringRedisTemplate).delete(SystemConstants.REDIS_TOKEN_SESSION_PREFIX + USER_ID);
        }

        @Test
        @DisplayName("Token 解析失败 - 吞掉异常不删除键")
        void logout_parseFailure_swallowed() {
            when(jwtTokenProvider.parseToken("bad-token")).thenThrow(new RuntimeException("invalid"));

            assertThatCode(() -> authService.logout("bad-token")).doesNotThrowAnyException();
            verify(stringRedisTemplate, never()).delete(anyString());
        }
    }

    @Nested
    @DisplayName("刷新 Token")
    class RefreshToken {

        @Test
        @DisplayName("Refresh Token 无效 - 抛出 010106")
        void refreshToken_invalidToken_throws010106() {
            when(jwtTokenProvider.validateToken(REFRESH_TOKEN)).thenReturn(false);

            assertThatThrownBy(() -> authService
                    .refreshToken(RefreshTokenDTO.builder().refreshToken(REFRESH_TOKEN).build()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010106");
        }

        @Test
        @DisplayName("Token 类型不是 refresh - 抛出 010106")
        void refreshToken_typeNotRefresh_throws010106() {
            when(jwtTokenProvider.validateToken(REFRESH_TOKEN)).thenReturn(true);
            when(jwtTokenProvider.parseToken(REFRESH_TOKEN))
                    .thenReturn(jwt(REFRESH_TOKEN, String.valueOf(USER_ID), "access", USERNAME));

            assertThatThrownBy(() -> authService
                    .refreshToken(RefreshTokenDTO.builder().refreshToken(REFRESH_TOKEN).build()))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010106");
        }

        @Test
        @DisplayName("刷新成功 - 返回新 Access Token 并写入 Redis")
        void refreshToken_success_returnsNewAccessToken() {
            when(jwtTokenProvider.validateToken(REFRESH_TOKEN)).thenReturn(true);
            when(jwtTokenProvider.parseToken(REFRESH_TOKEN))
                    .thenReturn(jwt(REFRESH_TOKEN, String.valueOf(USER_ID), "refresh", USERNAME));
            when(jwtTokenProvider.generateAccessToken(USER_ID, USERNAME)).thenReturn("new-access");
            when(jwtTokenProvider.getAccessExpireSeconds()).thenReturn(ACCESS_EXPIRE_SECONDS);

            String newToken = authService
                    .refreshToken(RefreshTokenDTO.builder().refreshToken(REFRESH_TOKEN).build());

            assertThat(newToken).isEqualTo("new-access");
            String tokenHash = String.valueOf("new-access".hashCode());
            verify(valueOperations).set(
                    eq(SystemConstants.REDIS_TOKEN_ACCESS_PREFIX + USER_ID + ":" + tokenHash),
                    eq("1"), eq((long) ACCESS_EXPIRE_SECONDS), eq(TimeUnit.SECONDS));
            verify(valueOperations).set(
                    eq(SystemConstants.REDIS_TOKEN_SESSION_PREFIX + USER_ID),
                    eq(tokenHash), eq((long) ACCESS_EXPIRE_SECONDS), eq(TimeUnit.SECONDS));
        }
    }

    @Nested
    @DisplayName("发送忘记密码验证码")
    class SendVerifyCode {

        private SendVerifyCodeDTO dto(String accountType, String account) {
            return SendVerifyCodeDTO.builder().accountType(accountType).account(account).build();
        }

        @Test
        @DisplayName("60 秒内重复发送 - 抛出 010109")
        void sendVerifyCode_rateLimited_throws010109() {
            when(valueOperations.setIfAbsent(
                    eq(SystemConstants.REDIS_VERIFY_CODE_LIMIT_PREFIX + "13800000000"),
                    eq("1"), eq(SystemConstants.VERIFY_CODE_SEND_INTERVAL_SECONDS), eq(TimeUnit.SECONDS)))
                    .thenReturn(Boolean.FALSE);

            assertThatThrownBy(() -> authService.sendVerifyCode(dto("phone", "13800000000")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010109");
        }

        @Test
        @DisplayName("账号不存在 - 静默返回不写验证码")
        void sendVerifyCode_userNotFound_silentSuccess() {
            when(valueOperations.setIfAbsent(anyString(), anyString(), anyLong(), any(TimeUnit.class)))
                    .thenReturn(Boolean.TRUE);
            when(systemUserMapper.selectByPhone("13800000000")).thenReturn(null);

            assertThatCode(() -> authService.sendVerifyCode(dto("phone", "13800000000")))
                    .doesNotThrowAnyException();

            verify(valueOperations, never()).set(anyString(), anyString(), anyLong(), any(TimeUnit.class));
        }

        @Test
        @DisplayName("邮箱方式 - 生成 6 位数字验证码写入 Redis 5 分钟")
        void sendVerifyCode_emailType_writesSixDigitCode() {
            when(valueOperations.setIfAbsent(anyString(), anyString(), anyLong(), any(TimeUnit.class)))
                    .thenReturn(Boolean.TRUE);
            when(systemUserMapper.selectByEmail("u1@example.com")).thenReturn(user("ENABLED"));

            authService.sendVerifyCode(dto("email", "u1@example.com"));

            ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
            verify(valueOperations).set(
                    eq(SystemConstants.REDIS_VERIFY_CODE_PREFIX + "email:u1@example.com"),
                    codeCaptor.capture(),
                    eq(SystemConstants.VERIFY_CODE_TTL_SECONDS), eq(TimeUnit.SECONDS));
            assertThat(codeCaptor.getValue()).matches("\\d{6}");
        }

        @Test
        @DisplayName("非 email 的验证方式 - 一律按手机号处理")
        void sendVerifyCode_unknownAccountType_fallsBackToPhone() {
            when(valueOperations.setIfAbsent(anyString(), anyString(), anyLong(), any(TimeUnit.class)))
                    .thenReturn(Boolean.TRUE);
            when(systemUserMapper.selectByPhone("13800000000")).thenReturn(user("ENABLED"));

            authService.sendVerifyCode(dto("SMS", "13800000000"));

            verify(systemUserMapper).selectByPhone("13800000000");
            verify(systemUserMapper, never()).selectByEmail(anyString());
            verify(valueOperations).set(
                    eq(SystemConstants.REDIS_VERIFY_CODE_PREFIX + "phone:13800000000"),
                    anyString(), eq(SystemConstants.VERIFY_CODE_TTL_SECONDS), eq(TimeUnit.SECONDS));
        }
    }

    @Nested
    @DisplayName("忘记密码重置")
    class ForgotPassword {

        private ForgotPasswordDTO dto(String accountType, String account, String code) {
            return ForgotPasswordDTO.builder()
                    .accountType(accountType).account(account).verifyCode(code).build();
        }

        @Test
        @DisplayName("账号不存在 - 静默返回不重置密码")
        void forgotPassword_userNotFound_silentReturn() {
            when(systemUserMapper.selectByPhone("13800000000")).thenReturn(null);

            assertThatCode(() -> authService.forgotPassword(dto("phone", "13800000000", "123456")))
                    .doesNotThrowAnyException();

            verify(systemUserMapper, never()).updateById(any(SystemUser.class));
        }

        @Test
        @DisplayName("Redis 无缓存验证码 - 抛出 010110")
        void forgotPassword_cachedCodeMissing_throws010110() {
            when(systemUserMapper.selectByEmail("u1@example.com")).thenReturn(user("ENABLED"));
            when(valueOperations.get(SystemConstants.REDIS_VERIFY_CODE_PREFIX + "email:u1@example.com"))
                    .thenReturn(null);

            assertThatThrownBy(() -> authService.forgotPassword(dto("email", "u1@example.com", "123456")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010110");
        }

        @Test
        @DisplayName("验证码不匹配 - 抛出 010110 且不删除缓存")
        void forgotPassword_codeMismatch_throws010110() {
            when(systemUserMapper.selectByEmail("u1@example.com")).thenReturn(user("ENABLED"));
            when(valueOperations.get(SystemConstants.REDIS_VERIFY_CODE_PREFIX + "email:u1@example.com"))
                    .thenReturn("123456");

            assertThatThrownBy(() -> authService.forgotPassword(dto("email", "u1@example.com", "000000")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010110");

            verify(stringRedisTemplate, never()).delete(anyString());
        }

        @Test
        @DisplayName("验证码正确 - 消费验证码并重置为初始密码")
        void forgotPassword_success_resetsToInitialPassword() {
            when(systemUserMapper.selectByPhone("13800000000")).thenReturn(user("ENABLED"));
            String codeKey = SystemConstants.REDIS_VERIFY_CODE_PREFIX + "phone:13800000000";
            when(valueOperations.get(codeKey)).thenReturn("123456");
            when(passwordEncoder.encode(SystemConstants.INITIAL_PASSWORD)).thenReturn("encoded-initial");

            authService.forgotPassword(dto("phone", "13800000000", "123456"));

            verify(stringRedisTemplate).delete(codeKey);
            ArgumentCaptor<SystemUser> captor = ArgumentCaptor.forClass(SystemUser.class);
            verify(systemUserMapper).updateById(captor.capture());
            assertThat(captor.getValue().getId()).isEqualTo(USER_ID);
            assertThat(captor.getValue().getPassword()).isEqualTo("encoded-initial");
            assertThat(captor.getValue().getPwdUpdateTime()).isNull();
        }
    }

    @Nested
    @DisplayName("首次登录修改密码")
    class ChangePasswordFirstTime {

        private ChangePasswordDTO dto(String oldPwd, String newPwd, String confirmPwd) {
            return ChangePasswordDTO.builder()
                    .oldPassword(oldPwd).newPassword(newPwd).confirmPassword(confirmPwd).build();
        }

        @Test
        @DisplayName("未认证用户 - 抛出 999902")
        void changePasswordFirstTime_unauthenticated_throws999902() {
            securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(null);

            assertThatThrownBy(() -> authService
                    .changePasswordFirstTime(dto("old", "Abcd1234!", "Abcd1234!")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999902");
        }

        @Test
        @DisplayName("两次密码不一致 - 抛出 999901")
        void changePasswordFirstTime_confirmMismatch_throws999901() {
            assertThatThrownBy(() -> authService
                    .changePasswordFirstTime(dto("old", "Abcd1234!", "Xbcd1234!")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999901");
        }

        @Test
        @DisplayName("新密码复杂度不足 - 抛出 999901")
        void changePasswordFirstTime_weakPassword_throws999901() {
            assertThatThrownBy(() -> authService
                    .changePasswordFirstTime(dto("old", "abcdefg1", "abcdefg1")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999901");
        }

        @Test
        @DisplayName("用户不存在 - 抛出 999902")
        void changePasswordFirstTime_userNotFound_throws999902() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(null);

            assertThatThrownBy(() -> authService
                    .changePasswordFirstTime(dto("old", "Abcd1234!", "Abcd1234!")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("999902");
        }

        @Test
        @DisplayName("旧密码错误 - 抛出 010101")
        void changePasswordFirstTime_wrongOldPassword_throws010101() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(user("ENABLED"));
            when(passwordEncoder.matches("old", "encoded-old")).thenReturn(false);

            assertThatThrownBy(() -> authService
                    .changePasswordFirstTime(dto("old", "Abcd1234!", "Abcd1234!")))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("010101");
        }

        @Test
        @DisplayName("修改成功 - 更新密码并记录密码更新时间")
        void changePasswordFirstTime_success_updatesPassword() {
            when(systemUserMapper.selectById(USER_ID)).thenReturn(user("ENABLED"));
            when(passwordEncoder.matches("old", "encoded-old")).thenReturn(true);
            when(passwordEncoder.encode("Abcd1234!")).thenReturn("encoded-new");

            authService.changePasswordFirstTime(dto("old", "Abcd1234!", "Abcd1234!"));

            ArgumentCaptor<SystemUser> captor = ArgumentCaptor.forClass(SystemUser.class);
            verify(systemUserMapper).updateById(captor.capture());
            assertThat(captor.getValue().getId()).isEqualTo(USER_ID);
            assertThat(captor.getValue().getPassword()).isEqualTo("encoded-new");
            assertThat(captor.getValue().getPwdUpdateTime()).isNotNull();
        }
    }
}
