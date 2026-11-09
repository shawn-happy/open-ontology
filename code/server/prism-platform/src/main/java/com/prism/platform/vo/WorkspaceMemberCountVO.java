package com.prism.platform.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 空间成员数批量统计中间结果（内部使用）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceMemberCountVO {

    private String workspaceCode;

    private Integer memberCount;
}
