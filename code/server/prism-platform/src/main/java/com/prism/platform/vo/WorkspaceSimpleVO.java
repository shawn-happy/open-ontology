package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 可访问空间简要 VO（空间切换器）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "可访问空间简要信息")
public class WorkspaceSimpleVO {

    @Schema(description = "空间编码")
    private String code;

    @Schema(description = "空间名称")
    private String name;

    @Schema(description = "当前用户在该空间的最高角色编码；非成员（dataScope 放行）时为 null")
    private String workspaceRole;
}
