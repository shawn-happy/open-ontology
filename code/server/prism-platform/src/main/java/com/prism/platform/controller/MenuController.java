package com.prism.platform.controller;

import com.prism.common.result.Result;
import com.prism.common.security.annotation.RequirePermission;
import com.prism.platform.dto.CreateMenuDTO;
import com.prism.platform.dto.IdStatusDTO;
import com.prism.platform.dto.UpdateMenuDTO;
import com.prism.platform.service.MenuService;
import com.prism.platform.vo.MenuTreeVO;
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
 * 菜单权限管理控制器
 */
@Tag(name = "菜单权限管理")
@RestController
@RequestMapping("/web/platform")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @Operation(summary = "菜单树")
    @GetMapping("/system/menus/tree")
    @RequirePermission("system:menu:list")
    public Result<List<MenuTreeVO>> tree() {
        List<MenuTreeVO> tree = menuService.tree();
        return Result.ok(tree);
    }

    @Operation(summary = "菜单详情")
    @GetMapping("/system/menus/{id}")
    @RequirePermission("system:menu:detail")
    public Result<MenuTreeVO> getById(@PathVariable Long id) {
        List<MenuTreeVO> tree = menuService.tree();
        MenuTreeVO menu = findInTree(tree, id);
        return Result.ok(menu);
    }

    @Operation(summary = "新增菜单")
    @PostMapping("/system/menus/create")
    @RequirePermission("system:menu:create")
    public Result<Void> create(@Valid @RequestBody CreateMenuDTO dto) {
        menuService.create(dto);
        return Result.ok();
    }

    @Operation(summary = "编辑菜单")
    @PostMapping("/system/menus/update")
    @RequirePermission("system:menu:update")
    public Result<Void> update(@Valid @RequestBody UpdateMenuDTO dto) {
        menuService.update(dto);
        return Result.ok();
    }

    @Operation(summary = "删除菜单")
    @PostMapping("/system/menus/{id}/delete")
    @RequirePermission("system:menu:delete")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return Result.ok();
    }

    @Operation(summary = "启用/停用菜单")
    @PostMapping("/system/menus/enable-disable")
    @RequirePermission("system:menu:update")
    public Result<Void> enableDisable(@Valid @RequestBody IdStatusDTO dto) {
        menuService.enableDisable(dto.getId(), dto.getStatus());
        return Result.ok();
    }

    private MenuTreeVO findInTree(List<MenuTreeVO> tree, Long id) {
        for (MenuTreeVO node : tree) {
            if (node.getId().equals(id)) {
                return node;
            }
            if (node.getChildren() != null) {
                MenuTreeVO found = findInTree(node.getChildren(), id);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}
