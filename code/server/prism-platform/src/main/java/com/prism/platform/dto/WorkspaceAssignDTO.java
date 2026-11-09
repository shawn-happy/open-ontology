package com.prism.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 批量分配用户/部门至工作空间 DTO（校验分组区分场景：分配用户必填 userIds，分配部门必填 deptIds；部门成员动态展开不落库）
 */
@Data
@Schema(description = "批量分配用户/部门请求")
public class WorkspaceAssignDTO {

    /**
     * 分配用户场景校验分组
     */
    public interface AssignUsers {
    }

    /**
     * 分配部门场景校验分组
     */
    public interface AssignDepts {
    }

    @NotEmpty(message = "用户列表不能为空", groups = AssignUsers.class)
    @Size(max = 200, message = "单次分配用户数不能超过 200")
    @Schema(description = "用户 ID 列表（分配用户时必填）")
    private List<Long> userIds;

    @NotEmpty(message = "部门列表不能为空", groups = AssignDepts.class)
    @Size(max = 200, message = "单次分配部门数不能超过 200")
    @Schema(description = "部门 ID 列表（分配部门时必填）")
    private List<Long> deptIds;

    @Schema(description = "空间角色编码，可空，默认 REPORTER（分配部门时作为部门成员默认空间角色）")
    private String workspaceRole;
}
