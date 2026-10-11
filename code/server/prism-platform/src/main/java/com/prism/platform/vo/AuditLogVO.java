package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 审计日志 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "审计日志")
public class AuditLogVO {

    @Schema(description = "日志 ID")
    private Long id;

    @Schema(description = "操作者 ID")
    private Long userId;

    @Schema(description = "操作者用户名")
    private String username;

    @Schema(description = "模块")
    private String module;

    @Schema(description = "操作内容")
    private String operation;

    @Schema(description = "操作类型")
    private String type;

    @Schema(description = "结果：SUCCESS/FAIL")
    private String result;

    @Schema(description = "IP 地址")
    private String ip;

    @Schema(description = "请求 ID")
    private String requestId;

    @Schema(description = "User-Agent")
    private String userAgent;

    @Schema(description = "操作详情")
    private String detail;

    @Schema(description = "操作时间")
    private LocalDateTime operatedAt;
}
