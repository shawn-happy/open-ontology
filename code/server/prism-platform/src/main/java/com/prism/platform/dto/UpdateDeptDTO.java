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
 * 编辑部门 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "编辑部门请求")
public class UpdateDeptDTO {

    @NotNull(message = "部门 ID 不能为空")
    @Schema(description = "部门 ID")
    private Long id;

    @Size(max = 50, message = "部门名称长度不超过 50")
    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "上级部门 ID")
    private Long parentId;

    @Schema(description = "负责人用户 ID")
    private Long leaderId;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "状态：ENABLED/DISABLED")
    private String status;
}
