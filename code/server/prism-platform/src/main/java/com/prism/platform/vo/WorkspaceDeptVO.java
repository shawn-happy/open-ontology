package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 空间部门分配记录 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "空间部门分配记录")
public class WorkspaceDeptVO {

    @Schema(description = "部门 ID")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "部门成员默认空间角色编码")
    private String defaultRole;

    @Schema(description = "分配人姓名")
    private String assignedByName;

    @Schema(description = "分配时间")
    private LocalDateTime assignedAt;
}
