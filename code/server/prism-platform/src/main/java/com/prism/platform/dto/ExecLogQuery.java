package com.prism.platform.dto;

import com.prism.common.result.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 执行日志查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "执行日志查询条件")
public class ExecLogQuery extends PageQuery {

    @Schema(description = "执行类型筛选：ACTION/FUNCTION/PIPELINE")
    private String execType;

    @Schema(description = "状态筛选：RUNNING/SUCCESS/FAIL")
    private String status;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;
}
