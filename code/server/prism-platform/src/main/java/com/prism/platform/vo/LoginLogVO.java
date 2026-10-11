package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 登录日志 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "登录日志")
public class LoginLogVO {

    @Schema(description = "日志 ID")
    private Long id;

    @Schema(description = "用户 ID")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "登录 IP")
    private String ip;

    @Schema(description = "登录位置")
    private String location;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "登录结果：SUCCESS/FAIL")
    private String result;

    @Schema(description = "失败原因")
    private String failReason;

    @Schema(description = "请求 ID")
    private String requestId;

    @Schema(description = "登录时间")
    private LocalDateTime loginAt;
}
