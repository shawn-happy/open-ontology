package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 空间成员统计 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "空间成员统计")
public class MemberStatsVO {

    @Schema(description = "有效成员总数（去重后）")
    private Integer totalMembers;

    @Schema(description = "直接分配成员数")
    private Integer directCount;

    @Schema(description = "部门分配成员数（去重后）")
    private Integer deptCount;

    @Schema(description = "Owner 数量")
    private Integer ownerCount;
}
