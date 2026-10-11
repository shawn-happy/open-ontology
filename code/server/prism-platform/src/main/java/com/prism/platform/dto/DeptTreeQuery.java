package com.prism.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 部门树查询条件
 */
@Data
@Schema(description = "部门树查询条件")
public class DeptTreeQuery {

    @Schema(description = "状态筛选：ENABLED/DISABLED")
    private String status;
}
