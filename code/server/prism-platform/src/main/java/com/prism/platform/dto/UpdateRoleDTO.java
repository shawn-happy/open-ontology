package com.prism.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 编辑角色 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "编辑角色请求")
public class UpdateRoleDTO {

    @NotNull(message = "角色 ID 不能为空")
    @Schema(description = "角色 ID")
    private Long id;

    @Size(max = 50, message = "角色名称长度不超过 50")
    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色类型：GLOBAL/WORKSPACE")
    private String type;

    @Size(max = 200, message = "备注长度不超过 200")
    @Schema(description = "备注")
    private String remark;

    @Schema(description = "状态：ENABLED/DISABLED")
    private String status;
}
