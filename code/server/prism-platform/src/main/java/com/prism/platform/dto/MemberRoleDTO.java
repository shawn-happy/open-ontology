package com.prism.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 分配 / 覆盖成员空间角色 DTO
 * workspaceRole 传 null = 清除个人角色覆盖，恢复部门默认（仅对部门来源成员有效）
 */
@Data
@Schema(description = "分配/覆盖成员空间角色请求")
public class MemberRoleDTO {

    @NotNull(message = "用户 ID 不能为空")
    @Schema(description = "目标用户 ID")
    private Long userId;

    @Schema(description = "新空间角色编码；传 null/空 = 清除个人角色覆盖，恢复部门默认")
    private String workspaceRole;
}
