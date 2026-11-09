package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 工作空间详情 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "工作空间详情")
public class WorkspaceDetailVO {

    @Schema(description = "空间 ID")
    private Long id;

    @Schema(description = "空间名称")
    private String name;

    @Schema(description = "空间编码")
    private String code;

    @Schema(description = "空间描述")
    private String description;

    @Schema(description = "空间管理员用户 ID")
    private Long ownerUserId;

    @Schema(description = "空间管理员姓名")
    private String ownerUserName;

    @Schema(description = "空间管理员部门名称")
    private String ownerUserDeptName;

    @Schema(description = "状态：ENABLED/DISABLED")
    private String status;

    @Schema(description = "有效成员数（动态展开去重）")
    private Integer memberCount;

    @Schema(description = "本体资产数（扩展点，本期恒为 0）")
    private Integer assetCount;

    @Schema(description = "最近活跃时间")
    private LocalDateTime lastActiveAt;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "部门分配列表")
    private List<WorkspaceDeptVO> depts;
}
