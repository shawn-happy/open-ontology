package com.prism.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.prism.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 登录日志实体
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("system_login_log")
public class SystemLoginLog extends BaseEntity {

    @TableField("user_id")
    private Long userId;

    @TableField("username")
    private String username;

    @TableField("ip")
    private String ip;

    @TableField("location")
    private String location;

    @TableField("browser")
    private String browser;

    @TableField("os")
    private String os;

    @TableField("result")
    private String result;

    @TableField("fail_reason")
    private String failReason;

    @TableField("user_agent")
    private String userAgent;

    @TableField("request_id")
    private String requestId;

    @TableField("login_at")
    private LocalDateTime loginAt;
}
