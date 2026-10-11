package com.prism.platform.controller;

import com.prism.common.result.Result;
import com.prism.platform.dto.ChangePasswordDTO;
import com.prism.platform.dto.ForgotPasswordDTO;
import com.prism.platform.dto.LoginDTO;
import com.prism.platform.dto.RefreshTokenDTO;
import com.prism.platform.service.AuthService;
import com.prism.platform.service.CaptchaService;
import com.prism.platform.vo.CaptchaVO;
import com.prism.platform.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 */
@Tag(name = "认证管理")
@RestController
@RequestMapping("/web/platform")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CaptchaService captchaService;

    @Operation(summary = "登录")
    @PostMapping("/auth/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        LoginVO vo = authService.login(dto);
        return Result.ok(vo);
    }

    @Operation(summary = "登出")
    @PostMapping("/auth/logout")
    public Result<Void> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        authService.logout(token);
        return Result.ok();
    }

    @Operation(summary = "刷新 Token")
    @PostMapping("/auth/refresh-token")
    public Result<String> refreshToken(@Valid @RequestBody RefreshTokenDTO dto) {
        String newToken = authService.refreshToken(dto);
        return Result.ok(newToken);
    }

    @Operation(summary = "获取验证码")
    @GetMapping("/auth/captcha")
    public Result<CaptchaVO> captcha() {
        CaptchaVO vo = captchaService.generateCaptcha();
        return Result.ok(vo);
    }

    @Operation(summary = "忘记密码")
    @PostMapping("/auth/forgot-password")
    public Result<Void> forgotPassword(@Valid @RequestBody ForgotPasswordDTO dto) {
        authService.forgotPassword(dto);
        return Result.ok();
    }

    @Operation(summary = "首次登录修改密码")
    @PostMapping("/auth/change-password-first-time")
    public Result<Void> changePasswordFirstTime(@Valid @RequestBody ChangePasswordDTO dto) {
        authService.changePasswordFirstTime(dto);
        return Result.ok();
    }
}
