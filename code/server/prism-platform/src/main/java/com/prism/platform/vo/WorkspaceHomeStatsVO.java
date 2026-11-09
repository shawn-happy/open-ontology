package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 空间首页统计卡片 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "空间首页统计")
public class WorkspaceHomeStatsVO {

    @Schema(description = "空间成员数（真实统计）")
    private Integer memberCount;

    @Schema(description = "成员数环比（%，null 表示不展示）")
    private Double memberCountTrend;

    @Schema(description = "本体资产数（扩展点，本期恒为 0）")
    private Integer ontologyAssetCount;

    @Schema(description = "本体资产环比（%，null 表示不展示）")
    private Double ontologyAssetTrend;

    @Schema(description = "数据集数（扩展点，本期恒为 0）")
    private Integer datasetCount;

    @Schema(description = "数据集环比（%，null 表示不展示）")
    private Double datasetTrend;

    @Schema(description = "Pipeline 数（扩展点，本期恒为 0）")
    private Integer pipelineCount;

    @Schema(description = "Pipeline 环比（%，null 表示不展示）")
    private Double pipelineTrend;

    @Schema(description = "今日 API 调用量（扩展点，本期恒为 0）")
    private Long apiCallToday;

    @Schema(description = "API 调用环比（%，null 表示不展示）")
    private Double apiCallTrend;
}
