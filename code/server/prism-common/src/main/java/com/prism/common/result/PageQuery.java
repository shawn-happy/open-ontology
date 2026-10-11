package com.prism.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 分页请求基类
 */
@Data
@Schema(description = "分页请求")
public class PageQuery {

    @Min(value = 1, message = "页码最小为 1")
    @Schema(description = "页码（从 1 开始）", example = "1")
    private int currentPage = 1;

    @Min(value = 1, message = "每页条数最小为 1")
    @Max(value = 100, message = "每页条数最大为 100")
    @Schema(description = "每页条数（默认 20，最大 100）", example = "20")
    private int pageSize = 20;

    @Schema(description = "搜索关键词")
    private String keyword;
}
