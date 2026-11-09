package com.prism.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送验证码 DTO（忘记密码流程）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "发送验证码请求")
public class SendVerifyCodeDTO {

    @NotBlank(message = "验证方式不能为空")
    @Schema(description = "验证方式：phone / email")
    private String accountType;

    @NotBlank(message = "手机号或邮箱不能为空")
    @Schema(description = "已绑定的手机号或邮箱")
    private String account;
}
