package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 执行日志 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "执行日志")
public class ExecLogVO {

    @Schema(description = "日志 ID")
    private Long id;

    @Schema(description = "执行类型：ACTION/FUNCTION/PIPELINE")
    private String execType;

    @Schema(description = "任务名称")
    private String taskName;

    @Schema(description = "触发者 ID")
    private Long triggerUserId;

    @Schema(description = "触发者用户名")
    private String triggerUsername;

    @Schema(description = "状态：RUNNING/SUCCESS/FAIL")
    private String status;

    @Schema(description = "执行耗时（毫秒）")
    private Long durationMs;

    @Schema(description = "执行详情")
    private String detail;

    @Schema(description = "请求 ID")
    private String requestId;

    @Schema(description = "开始时间")
    private LocalDateTime startedAt;

    @Schema(description = "结束时间")
    private LocalDateTime finishedAt;
}
