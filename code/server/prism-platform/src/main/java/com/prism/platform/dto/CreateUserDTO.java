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
 * 新增用户 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "新增用户请求")
public class CreateUserDTO {

    @NotBlank(message = "用户名不能为空")
    @Size(max = 32, message = "用户名长度不超过 32")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名仅允许字母、数字、下划线")
    @Schema(description = "用户名")
    private String username;

    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不超过 50")
    @Schema(description = "姓名")
    private String name;

    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号")
    private String phone;

    @Size(max = 100, message = "邮箱长度不超过 100")
    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "部门 ID")
    private Long deptId;

    @NotBlank(message = "状态不能为空")
    @Schema(description = "状态：ENABLED/DISABLED")
    private String status;
}
