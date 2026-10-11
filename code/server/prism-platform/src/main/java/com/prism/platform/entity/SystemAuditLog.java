package com.prism.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.prism.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 审计日志实体
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("system_audit_log")
public class SystemAuditLog extends BaseEntity {

    @TableField("user_id")
    private Long userId;

    @TableField("username")
    private String username;

    @TableField("module")
    private String module;

    @TableField("operation")
    private String operation;

    @TableField("type")
    private String type;

    @TableField("result")
    private String result;

    @TableField("ip")
    private String ip;

    @TableField("request_id")
    private String requestId;

    @TableField("user_agent")
    private String userAgent;

    @TableField("detail")
    private String detail;

    @TableField("operated_at")
    private LocalDateTime operatedAt;
}
