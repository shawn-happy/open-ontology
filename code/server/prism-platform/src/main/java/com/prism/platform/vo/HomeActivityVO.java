package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 空间首页近期动态 VO（本期空数组，字段预留）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "近期动态")
public class HomeActivityVO {

    @Schema(description = "动态 ID")
    private Long id;

    @Schema(description = "动态类型")
    private String type;

    @Schema(description = "动态内容")
    private String content;

    @Schema(description = "操作人姓名")
    private String operatorName;

    @Schema(description = "发生时间")
    private LocalDateTime occurredAt;
}
