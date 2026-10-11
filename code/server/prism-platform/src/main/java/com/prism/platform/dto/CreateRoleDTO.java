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
 * 新增角色 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "新增角色请求")
public class CreateRoleDTO {

    @NotBlank(message = "角色名称不能为空")
    @Size(max = 50, message = "角色名称长度不超过 50")
    @Schema(description = "角色名称")
    private String name;

    @NotBlank(message = "角色编码不能为空")
    @Size(max = 50, message = "角色编码长度不超过 50")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "角色编码仅允许字母、数字、下划线")
    @Schema(description = "角色编码")
    private String code;

    @NotBlank(message = "角色类型不能为空")
    @Schema(description = "角色类型：GLOBAL/WORKSPACE")
    private String type;

    @Size(max = 200, message = "备注长度不超过 200")
    @Schema(description = "备注")
    private String remark;
}
