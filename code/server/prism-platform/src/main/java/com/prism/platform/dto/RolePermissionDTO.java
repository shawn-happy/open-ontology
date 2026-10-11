package com.prism.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 角色权限配置 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "角色权限配置请求")
public class RolePermissionDTO {

    @NotNull(message = "角色 ID 不能为空")
    @Schema(description = "角色 ID")
    private Long roleId;

    @Schema(description = "菜单 ID 列表")
    private List<Long> menuIds;

    @NotBlank(message = "数据权限范围不能为空")
    @Schema(description = "数据权限范围：ALL/DEPT_AND_CHILDREN/DEPT/SELF/CUSTOM")
    private String dataScope;

    @Schema(description = "自定义可见空间编码列表（dataScope=CUSTOM 时使用）")
    private List<String> customWorkspaceCodes;
}
