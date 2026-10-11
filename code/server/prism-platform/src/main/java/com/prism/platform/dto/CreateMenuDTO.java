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
 * 新增菜单权限节点 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "新增菜单权限节点请求")
public class CreateMenuDTO {

    @NotNull(message = "上级节点 ID 不能为空")
    @Schema(description = "上级节点 ID（0 表示顶级）")
    private Long parentId;

    @NotBlank(message = "节点名称不能为空")
    @Size(max = 50, message = "节点名称长度不超过 50")
    @Schema(description = "节点名称")
    private String name;

    @NotBlank(message = "节点类型不能为空")
    @Schema(description = "节点类型：DIRECTORY/MENU/BUTTON")
    private String type;

    @Size(max = 100, message = "权限标识长度不超过 100")
    @Schema(description = "权限标识（如 system:user:create）")
    private String permission;

    @Size(max = 200, message = "路由路径长度不超过 200")
    @Schema(description = "前端路由路径")
    private String path;

    @Size(max = 200, message = "组件路径长度不超过 200")
    @Schema(description = "前端组件路径")
    private String component;

    @Size(max = 50, message = "图标长度不超过 50")
    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "排序（数值越小越靠前）")
    private Integer sortOrder;
}
