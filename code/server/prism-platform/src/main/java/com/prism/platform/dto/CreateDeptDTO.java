package com.prism.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新增部门 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "新增部门请求")
public class CreateDeptDTO {

    @NotNull(message = "上级部门 ID 不能为空")
    @Schema(description = "上级部门 ID（0 表示根节点下）")
    private Long parentId;

    @NotBlank(message = "部门名称不能为空")
    @Size(max = 50, message = "部门名称长度不超过 50")
    @Schema(description = "部门名称")
    private String name;

    @NotBlank(message = "部门编码不能为空")
    @Size(max = 50, message = "部门编码长度不超过 50")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "部门编码仅允许字母、数字、下划线")
    @Schema(description = "部门编码")
    private String code;

    @Schema(description = "负责人用户 ID")
    private Long leaderId;

    @Schema(description = "排序")
    private Integer sortOrder;
}
