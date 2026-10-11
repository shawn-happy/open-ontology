package com.prism.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.prism.common.aspect.AuditLog;
import com.prism.common.exception.BusinessException;
import com.prism.platform.dto.CreateMenuDTO;
import com.prism.platform.dto.UpdateMenuDTO;
import com.prism.platform.entity.SystemMenu;
import com.prism.platform.entity.SystemRole;
import com.prism.platform.entity.SystemRoleMenu;
import com.prism.platform.exception.SystemErrorCode;
import com.prism.platform.mapper.SystemMenuMapper;
import com.prism.platform.mapper.SystemRoleMapper;
import com.prism.platform.mapper.SystemRoleMenuMapper;
import com.prism.platform.service.MenuService;
import com.prism.platform.vo.MenuTreeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单权限服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final SystemMenuMapper systemMenuMapper;
    private final SystemRoleMapper systemRoleMapper;
    private final SystemRoleMenuMapper systemRoleMenuMapper;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    @AuditLog(module = "菜单管理", operation = "新增菜单", type = "CREATE")
    public void create(CreateMenuDTO dto) {
        // 权限标识全局唯一
        if (StringUtils.isNotBlank(dto.getPermission())) {
            SystemMenu existing = systemMenuMapper.selectByPermission(dto.getPermission());
            if (existing != null) {
                throw new BusinessException(SystemErrorCode.PERMISSION_EXISTS.getCode(),
                        SystemErrorCode.PERMISSION_EXISTS.getMessage());
            }
        }

        SystemMenu menu = new SystemMenu();
        menu.setParentId(dto.getParentId());
        menu.setName(dto.getName());
        menu.setType(dto.getType());
        menu.setPermission(dto.getPermission());
        menu.setPath(dto.getPath());
        menu.setComponent(dto.getComponent());
        menu.setIcon(dto.getIcon());
        menu.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        menu.setStatus("ENABLED");
        systemMenuMapper.insert(menu);
    }

    @Override
    @AuditLog(module = "菜单管理", operation = "编辑菜单", type = "UPDATE")
    public void update(UpdateMenuDTO dto) {
        SystemMenu menu = systemMenuMapper.selectById(dto.getId());
        if (menu == null) {
            throw new BusinessException("999904", "菜单不存在");
        }

        // 编辑操作必填校验
        if (StringUtils.isBlank(dto.getName())) {
            throw new BusinessException("999900", "菜单名称不能为空");
        }

        // 权限标识唯一
        if (StringUtils.isNotBlank(dto.getPermission())) {
            SystemMenu existing = systemMenuMapper.selectByPermission(dto.getPermission());
            if (existing != null && !existing.getId().equals(dto.getId())) {
                throw new BusinessException(SystemErrorCode.PERMISSION_EXISTS.getCode(),
                        SystemErrorCode.PERMISSION_EXISTS.getMessage());
            }
        }

        SystemMenu update = new SystemMenu();
        update.setId(dto.getId());
        update.setName(dto.getName());
        update.setPermission(dto.getPermission());
        update.setPath(dto.getPath());
        update.setComponent(dto.getComponent());
        update.setIcon(dto.getIcon());
        update.setSortOrder(dto.getSortOrder());
        systemMenuMapper.updateById(update);
        clearAllUserPermissionCache();
    }

    @Override
    @AuditLog(module = "菜单管理", operation = "删除菜单", type = "DELETE")
    public void delete(Long id) {
        SystemMenu menu = systemMenuMapper.selectById(id);
        if (menu == null) {
            throw new BusinessException("999904", "菜单不存在");
        }

        // 校验无下级节点
        List<SystemMenu> children = systemMenuMapper.selectByParentId(id);
        if (children != null && !children.isEmpty()) {
            throw new BusinessException(SystemErrorCode.MENU_HAS_CHILDREN.getCode(),
                    SystemErrorCode.MENU_HAS_CHILDREN.getMessage());
        }

        systemMenuMapper.deleteById(id);
        clearAllUserPermissionCache();
    }

    @Override
    public List<MenuTreeVO> tree() {
        List<SystemMenu> allMenus = systemMenuMapper.selectAll();
        return buildTree(allMenus, 0L);
    }

    @Override
    @AuditLog(module = "菜单管理", operation = "启用/停用菜单", type = "ENABLE")
    public void enableDisable(Long id, String status) {
        SystemMenu menu = systemMenuMapper.selectById(id);
        if (menu == null) {
            throw new BusinessException("999904", "菜单不存在");
        }
        SystemMenu update = new SystemMenu();
        update.setId(id);
        update.setStatus(status);
        systemMenuMapper.updateById(update);
        clearAllUserPermissionCache();
    }

    /**
     * 清除所有用户的权限缓存（菜单变更影响全局）
     */
    private void clearAllUserPermissionCache() {
        var keys = stringRedisTemplate.keys("prism:user:perms:*");
        if (keys != null && !keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }
    }

    @Override
    public List<String> getPermissionsByUserId(Long userId) {
        // 查询用户角色
        List<SystemRole> roles = systemRoleMapper.selectByUserId(userId);
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }

        // 查询角色关联的菜单
        List<String> permissions = new ArrayList<>();
        for (SystemRole role : roles) {
            List<SystemRoleMenu> roleMenus = systemRoleMenuMapper.selectByRoleId(role.getId());
            for (SystemRoleMenu rm : roleMenus) {
                SystemMenu menu = systemMenuMapper.selectById(rm.getMenuId());
                if (menu != null && StringUtils.isNotBlank(menu.getPermission())
                        && "ENABLED".equals(menu.getStatus())) {
                    permissions.add(menu.getPermission());
                }
            }
        }

        return permissions.stream().distinct().collect(Collectors.toList());
    }

    private List<MenuTreeVO> buildTree(List<SystemMenu> menus, Long parentId) {
        Map<Long, List<SystemMenu>> grouped = menus.stream()
                .collect(Collectors.groupingBy(SystemMenu::getParentId));

        return buildChildren(grouped, parentId);
    }

    private List<MenuTreeVO> buildChildren(Map<Long, List<SystemMenu>> grouped, Long parentId) {
        List<SystemMenu> children = grouped.getOrDefault(parentId, List.of());
        return children.stream()
                .map(menu -> MenuTreeVO.builder()
                        .id(menu.getId())
                        .parentId(menu.getParentId())
                        .name(menu.getName())
                        .type(menu.getType())
                        .permission(menu.getPermission())
                        .path(menu.getPath())
                        .component(menu.getComponent())
                        .icon(menu.getIcon())
                        .sortOrder(menu.getSortOrder())
                        .status(menu.getStatus())
                        .children(buildChildren(grouped, menu.getId()))
                        .build())
                .collect(Collectors.toList());
    }
}
