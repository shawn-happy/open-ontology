package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 空间首页进行中 Pipeline VO（本期空数组，字段预留）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "进行中 Pipeline")
public class HomePipelineVO {

    @Schema(description = "Pipeline ID")
    private Long id;

    @Schema(description = "Pipeline 名称")
    private String name;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "进度（0-100）")
    private Integer progress;

    @Schema(description = "开始时间")
    private LocalDateTime startedAt;
}
