package com.prism.platform.controller;

import com.prism.common.enums.WorkspaceRole;
import com.prism.common.result.PageResult;
import com.prism.common.result.Result;
import com.prism.platform.dto.MemberPageQuery;
import com.prism.platform.dto.MemberRemoveDTO;
import com.prism.platform.dto.MemberRoleDTO;
import com.prism.platform.security.annotation.RequireWorkspaceRole;
import com.prism.platform.service.WorkspaceMemberService;
import com.prism.platform.vo.MemberListVO;
import com.prism.platform.vo.MemberStatsVO;
import com.prism.platform.vo.WorkspaceRoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 空间成员分配记录控制器（空间级）
 * <p>
 * 鉴权说明：仅使用 @RequireWorkspaceRole（空间角色矩阵），有意不叠加 @RequirePermission ——
 * 空间角色与全局权限码是两套体系，叠加会挡掉仅持有空间角色（如空间 Owner）的合法操作人
 * （对技术设计 §9.5 的有意偏离，见实现计划）。
 * URL 中的 workspaceCode 一律经 WorkspaceContextFilter 校验并注入上下文。
 */
@Tag(name = "空间成员分配")
@RestController
@RequestMapping("/web/workspace/{workspaceCode}")
@RequiredArgsConstructor
public class WorkspaceMemberController {

    private final WorkspaceMemberService workspaceMemberService;

    @Operation(summary = "分配记录分页（动态展开去重，直接分配优先）")
    @GetMapping("/members")
    @RequireWorkspaceRole(WorkspaceRole.DEVELOPER)
    public Result<PageResult<MemberListVO>> pageMembers(@PathVariable String workspaceCode,
            MemberPageQuery query) {
        return Result.ok(workspaceMemberService.pageMembers(workspaceCode, query));
    }

    @Operation(summary = "成员统计")
    @GetMapping("/members/stats")
    @RequireWorkspaceRole(WorkspaceRole.DEVELOPER)
    public Result<MemberStatsVO> stats(@PathVariable String workspaceCode) {
        return Result.ok(workspaceMemberService.getMemberStats(workspaceCode));
    }

    @Operation(summary = "空间角色列表（内置五级 + 自定义角色）")
    @GetMapping("/roles")
    @RequireWorkspaceRole(WorkspaceRole.DEVELOPER)
    public Result<List<WorkspaceRoleVO>> roles(@PathVariable String workspaceCode) {
        return Result.ok(workspaceMemberService.listRoles(workspaceCode));
    }

    @Operation(summary = "分配/覆盖成员角色（workspaceRole 传空 = 清除个人覆盖）")
    @PostMapping("/members/role")
    @RequireWorkspaceRole(WorkspaceRole.MAINTAINER)
    public Result<Void> updateRole(@PathVariable String workspaceCode,
            @Valid @RequestBody MemberRoleDTO dto) {
        workspaceMemberService.updateMemberRole(workspaceCode, dto);
        return Result.ok();
    }

    @Operation(summary = "移除成员（PRD 流程 C 三分支语义）")
    @PostMapping("/members/remove")
    @RequireWorkspaceRole(WorkspaceRole.MAINTAINER)
    public Result<Void> removeMember(@PathVariable String workspaceCode,
            @Valid @RequestBody MemberRemoveDTO dto) {
        workspaceMemberService.removeMember(workspaceCode, dto);
        return Result.ok();
    }
}
