package com.prism.platform.dto;

import com.prism.common.result.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 空间成员分配记录分页查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "空间成员分配记录分页查询条件")
public class MemberPageQuery extends PageQuery {

    @Schema(description = "空间角色过滤（精确）：OWNER/MAINTAINER/DEVELOPER/REPORTER")
    private String workspaceRole;

    @Schema(description = "来源过滤：DIRECT=直接分配 / DEPT=部门分配（含个人角色覆盖）")
    private String source;
}
