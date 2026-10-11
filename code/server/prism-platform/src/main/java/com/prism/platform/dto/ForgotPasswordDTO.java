package com.prism.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 忘记密码 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "忘记密码请求")
public class ForgotPasswordDTO {

    @NotBlank(message = "联系方式不能为空")
    @Schema(description = "手机号或邮箱")
    private String contact;

    @NotBlank(message = "验证码不能为空")
    @Size(max = 6, message = "验证码为 6 位")
    @Schema(description = "短信/邮件验证码")
    private String verifyCode;
}
