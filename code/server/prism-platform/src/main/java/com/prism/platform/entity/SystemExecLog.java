package com.prism.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.prism.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 执行日志实体（Action/Function/Pipeline）
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("system_exec_log")
public class SystemExecLog extends BaseEntity {

    @TableField("exec_type")
    private String execType;

    @TableField("task_name")
    private String taskName;

    @TableField("trigger_user_id")
    private Long triggerUserId;

    @TableField("trigger_username")
    private String triggerUsername;

    @TableField("status")
    private String status;

    @TableField("duration_ms")
    private Long durationMs;

    @TableField("detail")
    private String detail;

    @TableField("request_id")
    private String requestId;

    @TableField("started_at")
    private LocalDateTime startedAt;

    @TableField("finished_at")
    private LocalDateTime finishedAt;
}
