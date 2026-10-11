package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批量导入结果 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "批量导入结果")
public class ImportResultVO {

    @Schema(description = "成功数")
    private Integer successCount;

    @Schema(description = "失败数")
    private Integer failCount;

    @Schema(description = "总数")
    private Integer totalCount;

    @Schema(description = "失败明细")
    private List<FailDetail> failDetails;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "失败明细")
    public static class FailDetail {

        @Schema(description = "行号")
        private Integer rowNum;

        @Schema(description = "用户名")
        private String username;

        @Schema(description = "失败原因")
        private String reason;
    }
}
