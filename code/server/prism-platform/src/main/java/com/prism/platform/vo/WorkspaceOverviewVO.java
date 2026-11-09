package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 平台级工作空间概览统计 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "工作空间概览统计")
public class WorkspaceOverviewVO {

    @Schema(description = "空间总数")
    private Integer totalSpaces;

    @Schema(description = "成员总数（跨空间去重前求和口径）")
    private Integer totalMembers;

    @Schema(description = "本体资产总数（扩展点，本期恒为 0）")
    private Integer totalAssets;

    @Schema(description = "活跃空间数（30 天内有活跃记录的启用空间）")
    private Integer activeSpaces30d;
}
