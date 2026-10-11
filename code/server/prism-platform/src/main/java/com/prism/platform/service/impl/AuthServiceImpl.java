package com.prism.platform.service.impl;

import com.prism.common.aspect.LoginLog;
import com.prism.common.exception.BusinessException;
import com.prism.common.security.JwtTokenProvider;
import com.prism.common.security.SecurityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import com.prism.platform.constants.SystemConstants;
import com.prism.platform.dto.ChangePasswordDTO;
import com.prism.platform.dto.ForgotPasswordDTO;
import com.prism.platform.dto.LoginDTO;
import com.prism.platform.dto.RefreshTokenDTO;
import com.prism.platform.entity.SystemUser;
import com.prism.platform.exception.SystemErrorCode;
import com.prism.platform.mapper.SystemUserMapper;
import com.prism.platform.service.AuthService;
import com.prism.platform.service.CaptchaService;
import com.prism.platform.service.MenuService;
import com.prism.platform.service.RoleService;
import com.prism.platform.vo.LoginVO;
import com.prism.platform.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 认证服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Pattern PWD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,20}$");

    private final SystemUserMapper systemUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate stringRedisTemplate;
    private final CaptchaService captchaService;
    private final RoleService roleService;
    private final MenuService menuService;

    @Override
    @LoginLog("用户登录")
    public LoginVO login(LoginDTO dto) {
        // 1. 验证码校验
        boolean captchaValid = captchaService.verifyCaptcha(dto.getCaptchaKey(), dto.getCaptchaCode());
        if (!captchaValid) {
            throw new BusinessException(SystemErrorCode.CAPTCHA_ERROR.getCode(),
                    SystemErrorCode.CAPTCHA_ERROR.getMessage());
        }

        // 2. 根据 loginType 查询用户
        SystemUser user = findUserByLoginType(dto);

        // 3. 用户不存在 -> 失败计数
        if (user == null) {
            throw new BusinessException(SystemErrorCode.USERNAME_OR_PASSWORD_ERROR.getCode(),
                    SystemErrorCode.USERNAME_OR_PASSWORD_ERROR.getMessage());
        }

        // 4. 检查状态
        if ("DISABLED".equals(user.getStatus())) {
            throw new BusinessException(SystemErrorCode.ACCOUNT_DISABLED.getCode(),
                    SystemErrorCode.ACCOUNT_DISABLED.getMessage());
        }

        // 5. 检查锁定
        if (user.getLoginFailCount() != null
                && user.getLoginFailCount() >= SystemConstants.DEFAULT_MAX_FAIL_COUNT
                && user.getLockUntil() != null
                && user.getLockUntil().isAfter(LocalDateTime.now())) {
            throw new BusinessException(SystemErrorCode.ACCOUNT_LOCKED.getCode(),
                    SystemErrorCode.ACCOUNT_LOCKED.getMessage());
        }

        // 6. 检查首次登录
        boolean firstLogin = user.getPwdUpdateTime() == null;

        // 7. 验证密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            int failCount = (user.getLoginFailCount() != null ? user.getLoginFailCount() : 0) + 1;
            LocalDateTime lockUntil = null;
            if (failCount >= SystemConstants.DEFAULT_MAX_FAIL_COUNT) {
                lockUntil = LocalDateTime.now().plusMinutes(SystemConstants.DEFAULT_LOCK_MINUTES);
            }
            systemUserMapper.updateFailCount(user.getId(), failCount, lockUntil);
            throw new BusinessException(SystemErrorCode.USERNAME_OR_PASSWORD_ERROR.getCode(),
                    SystemErrorCode.USERNAME_OR_PASSWORD_ERROR.getMessage());
        }

        // 8. 清除失败计数
        systemUserMapper.updateFailCount(user.getId(), 0, null);

        // 9. 生成双 Token，存入 Redis
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getUsername(),
                Boolean.TRUE.equals(dto.getRememberMe()));

        String tokenHash = String.valueOf(accessToken.hashCode());
        stringRedisTemplate.opsForValue().set(
                SystemConstants.REDIS_TOKEN_ACCESS_PREFIX + user.getId() + ":" + tokenHash,
                "1",
                jwtTokenProvider.getAccessExpireSeconds(),
                TimeUnit.SECONDS
        );
        stringRedisTemplate.opsForValue().set(
                SystemConstants.REDIS_TOKEN_SESSION_PREFIX + user.getId(),
                tokenHash,
                jwtTokenProvider.getAccessExpireSeconds(),
                TimeUnit.SECONDS
        );

        // 11. 更新 lastLoginAt/lastLoginIp
        systemUserMapper.updateLoginInfo(user.getId(), "", LocalDateTime.now());

        // 12. 构建 LoginVO
        UserInfoVO userInfo = UserInfoVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .build();

        List<String> permissions = menuService.getPermissionsByUserId(user.getId());

        // 缓存权限到 Redis（与 access token 同 TTL）
        String permsKey = SystemConstants.REDIS_USER_PERMS_PREFIX + user.getId();
        stringRedisTemplate.opsForValue().set(permsKey,
                String.join(",", permissions),
                jwtTokenProvider.getAccessExpireSeconds(),
                TimeUnit.SECONDS);

        return LoginVO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .firstLogin(firstLogin)
                .passwordExpired(false)
                .userInfo(userInfo)
                .permissions(permissions)
                .build();
    }

    @Override
    @LoginLog("用户登出")
    public void logout(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            return;
        }
        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }
        try {
            Jwt jwt = jwtTokenProvider.parseToken(accessToken);
            Long userId = Long.valueOf(jwt.getSubject());
            String tokenHash = String.valueOf(accessToken.hashCode());

            stringRedisTemplate.delete(SystemConstants.REDIS_TOKEN_ACCESS_PREFIX + userId + ":" + tokenHash);
            stringRedisTemplate.delete(SystemConstants.REDIS_TOKEN_SESSION_PREFIX + userId);
        } catch (Exception e) {
            log.warn("登出 Token 解析失败: {}", e.getMessage());
        }
    }

    @Override
    public String refreshToken(RefreshTokenDTO dto) {
        String refreshToken = dto.getRefreshToken();
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(SystemErrorCode.REFRESH_TOKEN_EXPIRED.getCode(),
                    SystemErrorCode.REFRESH_TOKEN_EXPIRED.getMessage());
        }

        Jwt jwt = jwtTokenProvider.parseToken(refreshToken);
        String type = jwt.getClaimAsString("type");
        if (!"refresh".equals(type)) {
            throw new BusinessException(SystemErrorCode.REFRESH_TOKEN_EXPIRED.getCode(),
                    SystemErrorCode.REFRESH_TOKEN_EXPIRED.getMessage());
        }

        Long userId = Long.valueOf(jwt.getSubject());
        String username = jwt.getClaimAsString("username");

        // 生成新的 Access Token
        String newAccessToken = jwtTokenProvider.generateAccessToken(userId, username);
        String tokenHash = String.valueOf(newAccessToken.hashCode());

        stringRedisTemplate.opsForValue().set(
                SystemConstants.REDIS_TOKEN_ACCESS_PREFIX + userId + ":" + tokenHash,
                "1",
                jwtTokenProvider.getAccessExpireSeconds(),
                TimeUnit.SECONDS
        );
        stringRedisTemplate.opsForValue().set(
                SystemConstants.REDIS_TOKEN_SESSION_PREFIX + userId,
                tokenHash,
                jwtTokenProvider.getAccessExpireSeconds(),
                TimeUnit.SECONDS
        );

        return newAccessToken;
    }

    @Override
    public void forgotPassword(ForgotPasswordDTO dto) {
        // 根据手机号或邮箱查找用户
        SystemUser user = systemUserMapper.selectByPhone(dto.getContact());
        if (user == null) {
            user = systemUserMapper.selectByEmail(dto.getContact());
        }
        if (user == null) {
            // 不暴露用户是否存在，统一返回成功
            return;
        }
        // 重置密码为初始密码
        String encodedPwd = passwordEncoder.encode(SystemConstants.INITIAL_PASSWORD);
        SystemUser update = new SystemUser();
        update.setId(user.getId());
        update.setPassword(encodedPwd);
        update.setPwdUpdateTime(null);
        systemUserMapper.updateById(update);
    }

    @Override
    public void changePasswordFirstTime(ChangePasswordDTO dto) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException("999902", "未认证");
        }

        // 验证新密码与确认密码一致
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException("999901", "两次输入的密码不一致");
        }

        // 验证密码复杂度
        if (!PWD_PATTERN.matcher(dto.getNewPassword()).matches()) {
            throw new BusinessException("999901", "密码须为 8-20 位，含大小写字母、数字和特殊字符");
        }

        SystemUser user = systemUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("999902", "用户不存在");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BusinessException(SystemErrorCode.USERNAME_OR_PASSWORD_ERROR.getCode(),
                    SystemErrorCode.USERNAME_OR_PASSWORD_ERROR.getMessage());
        }

        // 更新密码
        SystemUser update = new SystemUser();
        update.setId(userId);
        update.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        update.setPwdUpdateTime(LocalDateTime.now());
        systemUserMapper.updateById(update);
    }

    private SystemUser findUserByLoginType(LoginDTO dto) {
        return switch (dto.getLoginType()) {
            case SystemConstants.LOGIN_TYPE_PHONE -> systemUserMapper.selectByPhone(dto.getPhone());
            case SystemConstants.LOGIN_TYPE_EMAIL -> systemUserMapper.selectByEmail(dto.getEmail());
            default -> systemUserMapper.selectByUsername(dto.getUsername());
        };
    }
}
