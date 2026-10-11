package com.prism.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录请求 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "登录请求")
public class LoginDTO {

    @NotBlank(message = "登录类型不能为空")
    @Schema(description = "登录类型：USERNAME/PHONE/EMAIL", example = "USERNAME")
    private String loginType;

    @Size(max = 32, message = "用户名长度不超过 32")
    @Schema(description = "用户名（loginType=USERNAME 时必填）")
    private String username;

    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号（loginType=PHONE 时必填）")
    private String phone;

    @Size(max = 100, message = "邮箱长度不超过 100")
    @Schema(description = "邮箱（loginType=EMAIL 时必填）")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(max = 64, message = "密码长度不超过 64")
    @Schema(description = "密码")
    private String password;

    @NotBlank(message = "验证码 Key 不能为空")
    @Schema(description = "验证码 Key")
    private String captchaKey;

    @NotBlank(message = "验证码不能为空")
    @Size(max = 4, message = "验证码为 4 位")
    @Schema(description = "验证码")
    private String captchaCode;

    @Schema(description = "记住我")
    private Boolean rememberMe;
}
