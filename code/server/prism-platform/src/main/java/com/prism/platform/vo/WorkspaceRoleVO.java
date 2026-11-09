package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 空间角色字典 VO（分配角色弹窗选项）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "空间角色字典项")
public class WorkspaceRoleVO {

    @Schema(description = "角色编码")
    private String code;

    @Schema(description = "角色显示名")
    private String displayName;

    @Schema(description = "权限说明")
    private String description;

    @Schema(description = "角色等级（OWNER=5 ... REPORTER=2）")
    private Integer level;

    @Schema(description = "是否内置角色")
    private Boolean builtin;
}
