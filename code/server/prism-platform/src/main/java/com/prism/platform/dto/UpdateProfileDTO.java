package com.prism.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 修改个人信息 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "修改个人信息请求")
public class UpdateProfileDTO {

    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号")
    private String phone;

    @Size(max = 100, message = "邮箱长度不超过 100")
    @Schema(description = "邮箱")
    private String email;
}
