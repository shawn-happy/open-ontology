package com.prism.platform.controller;

import com.prism.common.result.PageResult;
import com.prism.common.result.Result;
import com.prism.common.security.annotation.RequirePermission;
import com.prism.platform.dto.AssignUsersDTO;
import com.prism.platform.dto.CreateRoleDTO;
import com.prism.platform.dto.IdStatusDTO;
import com.prism.platform.dto.RolePageQuery;
import com.prism.platform.dto.RolePermissionDTO;
import com.prism.platform.dto.UpdateRoleDTO;
import com.prism.platform.service.RoleService;
import com.prism.platform.vo.RoleDetailVO;
import com.prism.platform.vo.RoleListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色管理控制器
 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/web/platform")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "角色分页列表")
    @GetMapping("/system/roles")
    @RequirePermission("system:role:list")
    public Result<PageResult<RoleListVO>> page(RolePageQuery query) {
        PageResult<RoleListVO> result = roleService.page(query);
        return Result.ok(result);
    }

    @Operation(summary = "全部角色（下拉选择）")
    @GetMapping("/system/roles/all")
    @RequirePermission("system:role:list")
    public Result<List<RoleListVO>> listAll(@RequestParam(required = false) String keyword) {
        return Result.ok(roleService.listAll(keyword));
    }

    @Operation(summary = "角色详情")
    @GetMapping("/system/roles/{id}")
    @RequirePermission("system:role:detail")
    public Result<RoleDetailVO> getById(@PathVariable Long id) {
        RoleDetailVO vo = roleService.getById(id);
        return Result.ok(vo);
    }

    @Operation(summary = "新增角色")
    @PostMapping("/system/roles/create")
    @RequirePermission("system:role:create")
    public Result<Void> create(@Valid @RequestBody CreateRoleDTO dto) {
        roleService.create(dto);
        return Result.ok();
    }

    @Operation(summary = "编辑角色")
    @PostMapping("/system/roles/update")
    @RequirePermission("system:role:update")
    public Result<Void> update(@Valid @RequestBody UpdateRoleDTO dto) {
        roleService.update(dto);
        return Result.ok();
    }

    @Operation(summary = "删除角色")
    @PostMapping("/system/roles/{id}/delete")
    @RequirePermission("system:role:delete")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.ok();
    }

    @Operation(summary = "启用/停用角色")
    @PostMapping("/system/roles/enable-disable")
    @RequirePermission("system:role:update")
    public Result<Void> enableDisable(@Valid @RequestBody IdStatusDTO dto) {
        roleService.enableDisable(dto.getId(), dto.getStatus());
        return Result.ok();
    }

    @Operation(summary = "配置权限")
    @PostMapping("/system/roles/configure-permissions")
    @RequirePermission("system:role:update")
    public Result<Void> configurePermissions(@Valid @RequestBody RolePermissionDTO dto) {
        roleService.configurePermissions(dto);
        return Result.ok();
    }

    @Operation(summary = "分配用户")
    @PostMapping("/system/roles/assign-users")
    @RequirePermission("system:role:update")
    public Result<Void> assignUsers(@Valid @RequestBody AssignUsersDTO dto) {
        roleService.assignUsers(dto.getTargetId(), dto.getUserIds());
        return Result.ok();
    }
}
