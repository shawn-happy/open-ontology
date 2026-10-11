package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色详情 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "角色详情")
public class RoleDetailVO {

    @Schema(description = "角色 ID")
    private Long id;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色编码")
    private String code;

    @Schema(description = "角色类型：GLOBAL/WORKSPACE")
    private String type;

    @Schema(description = "数据权限范围")
    private String dataScope;

    @Schema(description = "自定义可见空间编码列表")
    private List<String> customWorkspaceCodes;

    @Schema(description = "是否内置角色")
    private Boolean builtin;

    @Schema(description = "状态：ENABLED/DISABLED")
    private String status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "关联菜单 ID 列表")
    private List<Long> menuIds;

    @Schema(description = "成员数")
    private Integer memberCount;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
