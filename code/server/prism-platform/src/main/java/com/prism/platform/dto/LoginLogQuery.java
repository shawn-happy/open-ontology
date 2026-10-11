package com.prism.platform.dto;

import com.prism.common.result.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 登录日志查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "登录日志查询条件")
public class LoginLogQuery extends PageQuery {

    @Schema(description = "用户名筛选")
    private String username;

    @Schema(description = "登录结果筛选：SUCCESS/FAIL")
    private String result;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;
}
