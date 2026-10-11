package com.prism.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 修改密码 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "修改密码请求")
public class ChangePasswordDTO {

    @NotBlank(message = "旧密码不能为空")
    @Size(max = 64, message = "密码长度不超过 64")
    @Schema(description = "旧密码")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度须为 8-20 位")
    @Schema(description = "新密码（8-20 位，含大写字母、小写字母、数字、特殊字符）")
    private String newPassword;

    @NotBlank(message = "确认密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度须为 8-20 位")
    @Schema(description = "确认新密码")
    private String confirmPassword;
}
