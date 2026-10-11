package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 部门树 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "部门树节点")
public class DeptTreeVO {

    @Schema(description = "部门 ID")
    private Long id;

    @Schema(description = "上级部门 ID")
    private Long parentId;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "部门编码")
    private String code;

    @Schema(description = "层级深度")
    private Integer depth;

    @Schema(description = "负责人姓名")
    private String leaderName;

    @Schema(description = "成员数")
    private Integer memberCount;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "状态：ENABLED/DISABLED")
    private String status;

    @Schema(description = "子部门列表")
    private List<DeptTreeVO> children;
}
