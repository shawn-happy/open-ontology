package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 空间成员分配记录 VO（成员维度，动态展开去重后）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "空间成员分配记录")
public class MemberListVO {

    @Schema(description = "用户 ID")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "所属部门名称（用户真实部门）")
    private String deptName;

    @Schema(description = "空间角色编码")
    private String workspaceRole;

    @Schema(description = "空间角色显示名")
    private String roleDisplayName;

    @Schema(description = "空间角色等级（OWNER=5 ... REPORTER=2）")
    private Integer roleLevel;

    @Schema(description = "是否个人角色覆盖（部门成员被单独指定角色）")
    private Boolean isPersonalRole;

    @Schema(description = "来源：DIRECT=直接分配 / DEPT=部门分配")
    private String source;

    @Schema(description = "来源部门名称（部门分配时有值）")
    private String sourceDeptName;

    @Schema(description = "分配人用户 ID")
    private Long assignedById;

    @Schema(description = "分配人姓名")
    private String assignedByName;

    @Schema(description = "分配时间")
    private LocalDateTime assignedAt;
}
