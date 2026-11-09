package com.prism.platform.controller;

import com.prism.common.result.PageResult;
import com.prism.common.result.Result;
import com.prism.common.security.SecurityUtils;
import com.prism.common.security.annotation.RequirePermission;
import com.prism.platform.dto.IdStatusDTO;
import com.prism.platform.dto.WorkspaceAssignDTO;
import com.prism.platform.dto.WorkspaceIdDTO;
import com.prism.platform.dto.WorkspacePageQuery;
import com.prism.platform.dto.WorkspaceRemoveDeptDTO;
import com.prism.platform.dto.WorkspaceSaveDTO;
import com.prism.platform.service.WorkspaceMemberService;
import com.prism.platform.service.WorkspaceService;
import com.prism.platform.vo.AssignResultVO;
import com.prism.platform.vo.WorkspaceDetailVO;
import com.prism.platform.vo.WorkspaceListVO;
import com.prism.platform.vo.WorkspaceOverviewVO;
import com.prism.platform.vo.WorkspaceSimpleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 工作空间管理控制器（平台级）
 */
@Tag(name = "工作空间管理")
@RestController
@RequestMapping("/web/platform/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;
    private final WorkspaceMemberService workspaceMemberService;

    @Operation(summary = "空间列表分页")
    @GetMapping
    @RequirePermission("platform:workspace:list")
    public Result<PageResult<WorkspaceListVO>> page(WorkspacePageQuery query) {
        return Result.ok(workspaceService.page(query));
    }

    @Operation(summary = "平台概览统计")
    @GetMapping("/overview")
    @RequirePermission("platform:workspace:list")
    public Result<WorkspaceOverviewVO> overview() {
        return Result.ok(workspaceService.overview());
    }

    @Operation(summary = "可访问空间列表（空间切换器，登录即可）")
    @GetMapping("/accessible")
    public Result<List<WorkspaceSimpleVO>> accessible() {
        return Result.ok(workspaceService.accessible(SecurityUtils.getCurrentUserId()));
    }

    @Operation(summary = "空间编码唯一性校验（true=可用）")
    @GetMapping("/code/check")
    @RequirePermission("platform:workspace:create")
    public Result<Boolean> checkCode(@RequestParam String code) {
        return Result.ok(workspaceService.isCodeAvailable(code));
    }

    @Operation(summary = "空间详情")
    @GetMapping("/{id}")
    @RequirePermission("platform:workspace:detail")
    public Result<WorkspaceDetailVO> detail(@PathVariable Long id) {
        return Result.ok(workspaceService.detail(id));
    }

    @Operation(summary = "创建空间")
    @PostMapping("/create")
    @RequirePermission("platform:workspace:create")
    public Result<Long> create(
            @Validated({Default.class, WorkspaceSaveDTO.Create.class}) @RequestBody WorkspaceSaveDTO dto) {
        return Result.ok(workspaceService.create(dto));
    }

    @Operation(summary = "编辑空间")
    @PostMapping("/update")
    @RequirePermission("platform:workspace:update")
    public Result<Void> update(
            @Validated({Default.class, WorkspaceSaveDTO.Update.class}) @RequestBody WorkspaceSaveDTO dto) {
        workspaceService.update(dto);
        return Result.ok();
    }

    @Operation(summary = "启用/停用空间")
    @PostMapping("/status")
    @RequirePermission("platform:workspace:status")
    public Result<Void> updateStatus(@Valid @RequestBody IdStatusDTO dto) {
        workspaceService.updateStatus(dto.getId(), dto.getStatus());
        return Result.ok();
    }

    @Operation(summary = "删除空间")
    @PostMapping("/delete")
    @RequirePermission("platform:workspace:delete")
    public Result<Void> delete(@Valid @RequestBody WorkspaceIdDTO dto) {
        workspaceService.delete(dto.getId());
        return Result.ok();
    }

    @Operation(summary = "批量分配用户")
    @PostMapping("/{id}/members/assign")
    @RequirePermission("platform:workspace:assign")
    public Result<AssignResultVO> assignUsers(@PathVariable Long id,
            @Validated({Default.class, WorkspaceAssignDTO.AssignUsers.class})
            @RequestBody WorkspaceAssignDTO dto) {
        return Result.ok(workspaceMemberService.assignUsers(id, dto));
    }

    @Operation(summary = "批量分配部门")
    @PostMapping("/{id}/depts/assign")
    @RequirePermission("platform:workspace:assign")
    public Result<AssignResultVO> assignDepts(@PathVariable Long id,
            @Validated({Default.class, WorkspaceAssignDTO.AssignDepts.class})
            @RequestBody WorkspaceAssignDTO dto) {
        return Result.ok(workspaceMemberService.assignDepts(id, dto));
    }

    @Operation(summary = "移除分配部门")
    @PostMapping("/{id}/depts/remove")
    @RequirePermission("platform:workspace:assign")
    public Result<Void> removeDept(@PathVariable Long id,
            @Valid @RequestBody WorkspaceRemoveDeptDTO dto) {
        workspaceMemberService.removeDept(id, dto);
        return Result.ok();
    }
}
