package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 菜单树 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "菜单树节点")
public class MenuTreeVO {

    @Schema(description = "节点 ID")
    private Long id;

    @Schema(description = "上级节点 ID")
    private Long parentId;

    @Schema(description = "节点名称")
    private String name;

    @Schema(description = "节点类型：DIRECTORY/MENU/BUTTON")
    private String type;

    @Schema(description = "权限标识")
    private String permission;

    @Schema(description = "前端路由路径")
    private String path;

    @Schema(description = "前端组件路径")
    private String component;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "状态：ENABLED/DISABLED")
    private String status;

    @Schema(description = "子节点列表")
    private List<MenuTreeVO> children;
}
