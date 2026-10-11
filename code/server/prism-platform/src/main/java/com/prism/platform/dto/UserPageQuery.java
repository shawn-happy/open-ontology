package com.prism.platform.dto;

import com.prism.common.result.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户分页查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户分页查询条件")
public class UserPageQuery extends PageQuery {

    @Schema(description = "部门 ID 筛选")
    private Long deptId;

    @Schema(description = "状态筛选：ENABLED/DISABLED")
    private String status;
}
