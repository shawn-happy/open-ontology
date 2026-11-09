package com.prism.platform.dto;

import com.prism.common.result.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 工作空间分页查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "工作空间分页查询条件")
public class WorkspacePageQuery extends PageQuery {

    @Schema(description = "状态筛选：ENABLED/DISABLED")
    private String status;
}
