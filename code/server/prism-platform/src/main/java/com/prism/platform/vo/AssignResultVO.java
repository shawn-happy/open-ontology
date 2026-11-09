package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 批量分配结果 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "批量分配结果")
public class AssignResultVO {

    @Schema(description = "成功分配数")
    private Integer successCount;

    @Schema(description = "跳过数（已是成员/已分配，幂等）")
    private Integer skipCount;
}
