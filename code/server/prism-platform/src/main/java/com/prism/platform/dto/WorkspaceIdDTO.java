package com.prism.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 工作空间 ID DTO（删除空间等按 ID 操作的场景）
 */
@Data
@Schema(description = "工作空间 ID 请求")
public class WorkspaceIdDTO {

    @NotNull(message = "ID 不能为空")
    @Schema(description = "空间 ID")
    private Long id;
}
