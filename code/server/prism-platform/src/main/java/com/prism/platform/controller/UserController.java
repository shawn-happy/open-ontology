package com.prism.platform.controller;

import com.prism.common.result.PageResult;
import com.prism.common.result.Result;
import com.prism.common.security.annotation.RequirePermission;
import com.prism.platform.dto.AssignDeptDTO;
import com.prism.platform.dto.AssignRolesDTO;
import com.prism.platform.dto.CreateUserDTO;
import com.prism.platform.dto.IdStatusDTO;
import com.prism.platform.dto.UpdateUserDTO;
import com.prism.platform.dto.UserPageQuery;
import com.prism.platform.service.UserService;
import com.prism.platform.vo.ImportResultVO;
import com.prism.platform.vo.UserDetailVO;
import com.prism.platform.vo.UserListVO;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户管理控制器
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/web/platform")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "用户分页列表")
    @GetMapping("/system/users")
    @RequirePermission("system:user:list")
    public Result<PageResult<UserListVO>> page(UserPageQuery query) {
        PageResult<UserListVO> result = userService.page(query);
        return Result.ok(result);
    }

    @Operation(summary = "用户详情")
    @GetMapping("/system/users/{id}")
    @RequirePermission("system:user:detail")
    public Result<UserDetailVO> getById(@PathVariable Long id) {
        UserDetailVO vo = userService.getById(id);
        return Result.ok(vo);
    }

    @Operation(summary = "新增用户")
    @PostMapping("/system/users/create")
    @RequirePermission("system:user:create")
    public Result<Void> create(@Valid @RequestBody CreateUserDTO dto) {
        userService.create(dto);
        return Result.ok();
    }

    @Operation(summary = "编辑用户")
    @PostMapping("/system/users/update")
    @RequirePermission("system:user:update")
    public Result<Void> update(@Valid @RequestBody UpdateUserDTO dto) {
        userService.update(dto);
        return Result.ok();
    }

    @Operation(summary = "删除用户")
    @PostMapping("/system/users/{id}/delete")
    @RequirePermission("system:user:delete")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.ok();
    }

    @Operation(summary = "启用/停用用户")
    @PostMapping("/system/users/enable-disable")
    @RequirePermission("system:user:update")
    public Result<Void> enableDisable(@Valid @RequestBody IdStatusDTO dto) {
        userService.enableDisable(dto.getId(), dto.getStatus());
        return Result.ok();
    }

    @Operation(summary = "重置密码")
    @PostMapping("/system/users/{id}/reset-password")
    @RequirePermission("system:user:update")
    public Result<Void> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return Result.ok();
    }

    @Operation(summary = "分配角色")
    @PostMapping("/system/users/assign-roles")
    @RequirePermission("system:user:update")
    public Result<Void> assignRoles(@Valid @RequestBody AssignRolesDTO dto) {
        userService.assignRoles(dto.getUserId(), dto.getRoleIds());
        return Result.ok();
    }

    @Operation(summary = "分配部门")
    @PostMapping("/system/users/assign-dept")
    @RequirePermission("system:user:update")
    public Result<Void> assignDept(@Valid @RequestBody AssignDeptDTO dto) {
        userService.assignDept(dto.getUserId(), dto.getDeptId());
        return Result.ok();
    }

    @Operation(summary = "批量导入用户")
    @PostMapping("/system/users/import")
    @RequirePermission("system:user:create")
    public Result<ImportResultVO> importUsers(@RequestParam("file") MultipartFile file) {
        ImportResultVO vo = userService.importUsers(file);
        return Result.ok(vo);
    }

    @Operation(summary = "导出用户")
    @GetMapping("/system/users/export")
    @RequirePermission("system:user:export")
    public Result<List<UserListVO>> exportUsers(UserPageQuery query) {
        List<UserListVO> list = userService.exportUsers(query);
        return Result.ok(list);
    }
}
