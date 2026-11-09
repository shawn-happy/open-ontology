package com.prism.platform.controller;

import com.prism.common.enums.WorkspaceRole;
import com.prism.common.result.Result;
import com.prism.platform.security.annotation.RequireWorkspaceRole;
import com.prism.platform.service.WorkspaceHomeService;
import com.prism.platform.vo.HomeActivityVO;
import com.prism.platform.vo.HomePipelineVO;
import com.prism.platform.vo.WorkspaceHomeStatsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 空间首页控制器（空间级）
 * <p>
 * 权限：空间有效成员即可（REPORTER 起），对齐 PRD 流程 D；
 * URL 中的 workspaceCode 经 WorkspaceContextFilter 校验并注入上下文。
 */
@Tag(name = "空间首页")
@RestController
@RequestMapping("/web/workspace/{workspaceCode}/home")
@RequiredArgsConstructor
public class WorkspaceHomeController {

    private final WorkspaceHomeService workspaceHomeService;

    @Operation(summary = "首页统计卡片")
    @GetMapping("/stats")
    @RequireWorkspaceRole(WorkspaceRole.REPORTER)
    public Result<WorkspaceHomeStatsVO> stats(@PathVariable String workspaceCode) {
        return Result.ok(workspaceHomeService.stats(workspaceCode));
    }

    @Operation(summary = "进行中 Pipeline（本期空数组）")
    @GetMapping("/pipelines")
    @RequireWorkspaceRole(WorkspaceRole.REPORTER)
    public Result<List<HomePipelineVO>> pipelines(@PathVariable String workspaceCode) {
        return Result.ok(workspaceHomeService.pipelines(workspaceCode));
    }

    @Operation(summary = "近期动态（本期空数组）")
    @GetMapping("/activities")
    @RequireWorkspaceRole(WorkspaceRole.REPORTER)
    public Result<List<HomeActivityVO>> activities(@PathVariable String workspaceCode) {
        return Result.ok(workspaceHomeService.activities(workspaceCode));
    }
}
