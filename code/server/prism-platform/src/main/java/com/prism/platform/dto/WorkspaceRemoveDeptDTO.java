package com.prism.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 移除分配部门 DTO
 */
@Data
@Schema(description = "移除分配部门请求")
public class WorkspaceRemoveDeptDTO {

    @NotNull(message = "部门 ID 不能为空")
    @Schema(description = "部门 ID")
    private Long deptId;
}
