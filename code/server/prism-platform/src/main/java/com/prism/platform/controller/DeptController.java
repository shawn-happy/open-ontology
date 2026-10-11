package com.prism.platform.controller;

import com.prism.common.result.Result;
import com.prism.common.security.annotation.RequirePermission;
import com.prism.platform.dto.AssignUsersDTO;
import com.prism.platform.dto.CreateDeptDTO;
import com.prism.platform.dto.DeptTreeQuery;
import com.prism.platform.dto.IdStatusDTO;
import com.prism.platform.dto.SetLeaderDTO;
import com.prism.platform.dto.TransferDeptDTO;
import com.prism.platform.dto.UpdateDeptDTO;
import com.prism.platform.dto.UpdateSortDTO;
import com.prism.platform.service.DeptService;
import com.prism.platform.vo.DeptTreeVO;
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
 * 部门管理控制器
 */
@Tag(name = "部门管理")
@RestController
@RequestMapping("/web/platform")
@RequiredArgsConstructor
public class DeptController {

    private final DeptService deptService;

    @Operation(summary = "部门树")
    @GetMapping("/system/depts/tree")
    @RequirePermission("system:dept:list")
    public Result<List<DeptTreeVO>> tree(DeptTreeQuery query) {
        List<DeptTreeVO> tree = deptService.tree(query);
        return Result.ok(tree);
    }

    @Operation(summary = "部门详情")
    @GetMapping("/system/depts/{id}")
    @RequirePermission("system:dept:detail")
    public Result<DeptTreeVO> getById(@PathVariable Long id) {
        List<DeptTreeVO> tree = deptService.tree(new DeptTreeQuery());
        DeptTreeVO dept = findInTree(tree, id);
        return Result.ok(dept);
    }

    @Operation(summary = "新增部门")
    @PostMapping("/system/depts/create")
    @RequirePermission("system:dept:create")
    public Result<Void> create(@Valid @RequestBody CreateDeptDTO dto) {
        deptService.create(dto);
        return Result.ok();
    }

    @Operation(summary = "编辑部门")
    @PostMapping("/system/depts/update")
    @RequirePermission("system:dept:update")
    public Result<Void> update(@Valid @RequestBody UpdateDeptDTO dto) {
        deptService.update(dto);
        return Result.ok();
    }

    @Operation(summary = "删除部门")
    @PostMapping("/system/depts/{id}/delete")
    @RequirePermission("system:dept:delete")
    public Result<Void> delete(@PathVariable Long id) {
        deptService.delete(id);
        return Result.ok();
    }

    @Operation(summary = "启用/停用部门")
    @PostMapping("/system/depts/enable-disable")
    @RequirePermission("system:dept:update")
    public Result<Void> enableDisable(@Valid @RequestBody IdStatusDTO dto) {
        deptService.enableDisable(dto.getId(), dto.getStatus());
        return Result.ok();
    }

    @Operation(summary = "分配用户到部门")
    @PostMapping("/system/depts/assign-users")
    @RequirePermission("system:dept:update")
    public Result<Void> assignUsers(@Valid @RequestBody AssignUsersDTO dto) {
        deptService.assignUsers(dto.getTargetId(), dto.getUserIds());
        return Result.ok();
    }

    @Operation(summary = "设置负责人")
    @PostMapping("/system/depts/set-leader")
    @RequirePermission("system:dept:update")
    public Result<Void> setLeader(@Valid @RequestBody SetLeaderDTO dto) {
        deptService.setLeader(dto.getDeptId(), dto.getLeaderId());
        return Result.ok();
    }

    @Operation(summary = "部门转移")
    @PostMapping("/system/depts/transfer")
    @RequirePermission("system:dept:update")
    public Result<Void> transfer(@Valid @RequestBody TransferDeptDTO dto) {
        deptService.transfer(dto.getSourceDeptId(), dto.getTargetDeptId());
        return Result.ok();
    }

    @Operation(summary = "更新排序")
    @PostMapping("/system/depts/update-sort")
    @RequirePermission("system:dept:update")
    public Result<Void> updateSort(@Valid @RequestBody UpdateSortDTO dto) {
        deptService.updateSort(dto.getId(), dto.getSortOrder());
        return Result.ok();
    }

    private DeptTreeVO findInTree(List<DeptTreeVO> tree, Long id) {
        for (DeptTreeVO node : tree) {
            if (node.getId().equals(id)) {
                return node;
            }
            if (node.getChildren() != null) {
                DeptTreeVO found = findInTree(node.getChildren(), id);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}
