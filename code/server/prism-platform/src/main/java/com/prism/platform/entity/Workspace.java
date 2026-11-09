package com.prism.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.prism.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 工作空间实体（全局资源，不含 workspace_code）
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("platform_workspace")
public class Workspace extends BaseEntity {

    @TableField("name")
    private String name;

    @TableField("code")
    private String code;

    @TableField("description")
    private String description;

    @TableField("owner_user_id")
    private Long ownerUserId;

    @TableField("status")
    private String status;

    @TableField("last_active_at")
    private LocalDateTime lastActiveAt;
}
