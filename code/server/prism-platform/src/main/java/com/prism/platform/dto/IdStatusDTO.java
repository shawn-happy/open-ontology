package com.prism.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 通用启用/停用 DTO
 */
@Data
@Schema(description = "启用/停用请求")
public class IdStatusDTO {

    @NotNull(message = "ID 不能为空")
    @Schema(description = "主键 ID")
    private Long id;

    @NotBlank(message = "状态不能为空")
    @Pattern(regexp = "^(ENABLED|DISABLED)$", message = "状态仅允许 ENABLED 或 DISABLED")
    @Schema(description = "状态：ENABLED/DISABLED")
    private String status;
}
