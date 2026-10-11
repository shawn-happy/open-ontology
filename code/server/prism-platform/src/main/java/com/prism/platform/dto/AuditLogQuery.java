package com.prism.platform.dto;

import com.prism.common.result.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 审计日志查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "审计日志查询条件")
public class AuditLogQuery extends PageQuery {

    @Schema(description = "模块筛选")
    private String module;

    @Schema(description = "操作类型筛选：CREATE/UPDATE/DELETE/ENABLE/DISABLE 等")
    private String type;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;
}
