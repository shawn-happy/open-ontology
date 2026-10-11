package com.prism.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prism.common.aspect.AuditLog;
import com.prism.common.exception.BusinessException;
import com.prism.common.result.PageResult;
import com.prism.platform.constants.SystemConstants;
import com.prism.platform.dto.CreateRoleDTO;
import com.prism.platform.dto.RolePageQuery;
import com.prism.platform.dto.RolePermissionDTO;
import com.prism.platform.dto.UpdateRoleDTO;
import com.prism.platform.entity.SystemRole;
import com.prism.platform.entity.SystemRoleMenu;
import com.prism.platform.entity.SystemUserRole;
import com.prism.platform.exception.SystemErrorCode;
import com.prism.platform.mapper.SystemRoleMapper;
import com.prism.platform.mapper.SystemRoleMenuMapper;
import com.prism.platform.mapper.SystemUserRoleMapper;
import com.prism.platform.service.RoleService;
import com.prism.platform.vo.RoleDetailVO;
import com.prism.platform.vo.RoleListVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色管理服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final SystemRoleMapper systemRoleMapper;
    private final SystemRoleMenuMapper systemRoleMenuMapper;
    private final SystemUserRoleMapper systemUserRoleMapper;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    @AuditLog(module = "角色管理", operation = "新增角色", type = "CREATE")
    public void create(CreateRoleDTO dto) {
        // 校验角色名唯一
        LambdaQueryWrapper<SystemRole> nameWrapper = new LambdaQueryWrapper<>();
        nameWrapper.eq(SystemRole::getName, dto.getName());
        if (systemRoleMapper.selectCount(nameWrapper) > 0) {
            throw new BusinessException(SystemErrorCode.ROLE_NAME_EXISTS.getCode(),
                    SystemErrorCode.ROLE_NAME_EXISTS.getMessage());
        }

        // 校验角色编码唯一
        SystemRole byCode = systemRoleMapper.selectByCode(dto.getCode());
        if (byCode != null) {
            throw new BusinessException(SystemErrorCode.ROLE_CODE_EXISTS.getCode(),
                    SystemErrorCode.ROLE_CODE_EXISTS.getMessage());
        }

        SystemRole role = new SystemRole();
        role.setName(dto.getName());
        role.setCode(dto.getCode());
        role.setType(dto.getType());
        role.setDataScope("SELF");
        role.setStatus("ENABLED");
        role.setBuiltin(false);
        role.setRemark(dto.getRemark());
        systemRoleMapper.insert(role);
    }

    @Override
    @AuditLog(module = "角色管理", operation = "编辑角色", type = "UPDATE")
    public void update(UpdateRoleDTO dto) {
        SystemRole role = systemRoleMapper.selectById(dto.getId());
        if (role == null) {
            throw new BusinessException("999904", "角色不存在");
        }

        // 编辑操作必填校验
        if (StringUtils.isBlank(dto.getName()) || StringUtils.isBlank(dto.getType())) {
            throw new BusinessException("999900", "角色名称和类型不能为空");
        }

        // 内置角色不可修改
        if (Boolean.TRUE.equals(role.getBuiltin())) {
            throw new BusinessException(SystemErrorCode.BUILTIN_ROLE_READONLY.getCode(),
                    SystemErrorCode.BUILTIN_ROLE_READONLY.getMessage());
        }

        // 超管角色不可修改
        if (SystemConstants.ROLE_SUPER_ADMIN.equals(role.getCode())) {
            throw new BusinessException(SystemErrorCode.SUPER_ADMIN_ROLE_READONLY.getCode(),
                    SystemErrorCode.SUPER_ADMIN_ROLE_READONLY.getMessage());
        }

        // 校验角色名唯一
        LambdaQueryWrapper<SystemRole> nameWrapper = new LambdaQueryWrapper<>();
        nameWrapper.eq(SystemRole::getName, dto.getName())
                .ne(SystemRole::getId, dto.getId());
        if (systemRoleMapper.selectCount(nameWrapper) > 0) {
            throw new BusinessException(SystemErrorCode.ROLE_NAME_EXISTS.getCode(),
                    SystemErrorCode.ROLE_NAME_EXISTS.getMessage());
        }

        SystemRole update = new SystemRole();
        update.setId(dto.getId());
        update.setName(dto.getName());
        update.setType(dto.getType());
        update.setRemark(dto.getRemark());
        systemRoleMapper.updateById(update);
    }

    @Override
    @AuditLog(module = "角色管理", operation = "删除角色", type = "DELETE")
    @Transactional
    public void delete(Long id) {
        SystemRole role = systemRoleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("999904", "角色不存在");
        }

        // 内置角色不可删除
        if (Boolean.TRUE.equals(role.getBuiltin())) {
            throw new BusinessException(SystemErrorCode.BUILTIN_ROLE_READONLY.getCode(),
                    "内置角色不可删除");
        }

        // 先获取关联用户，再删除
        LambdaQueryWrapper<SystemUserRole> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(SystemUserRole::getRoleId, id);
        List<SystemUserRole> affectedUserRoles = systemUserRoleMapper.selectList(userWrapper);

        systemRoleMapper.deleteById(id);
        systemRoleMenuMapper.deleteByRoleId(id);
        systemUserRoleMapper.deleteByRoleId(id);

        // 清除该角色下所有用户的权限缓存
        for (SystemUserRole ur : affectedUserRoles) {
            stringRedisTemplate.delete(SystemConstants.REDIS_USER_PERMS_PREFIX + ur.getUserId());
        }
    }

    @Override
    public RoleDetailVO getById(Long id) {
        SystemRole role = systemRoleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("999904", "角色不存在");
        }

        // 查询关联菜单
        List<SystemRoleMenu> roleMenus = systemRoleMenuMapper.selectByRoleId(id);
        List<Long> menuIds = roleMenus.stream().map(SystemRoleMenu::getMenuId).collect(Collectors.toList());

        // 查询成员数
        LambdaQueryWrapper<SystemUserRole> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(SystemUserRole::getRoleId, id);
        int memberCount = Math.toIntExact(systemUserRoleMapper.selectCount(userWrapper));

        return RoleDetailVO.builder()
                .id(role.getId())
                .name(role.getName())
                .code(role.getCode())
                .type(role.getType())
                .dataScope(role.getDataScope())
                .customWorkspaceCodes(role.getCustomWorkspaceCodes())
                .builtin(role.getBuiltin())
                .status(role.getStatus())
                .remark(role.getRemark())
                .menuIds(menuIds)
                .memberCount(memberCount)
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }

    @Override
    public PageResult<RoleListVO> page(RolePageQuery query) {
        Page<SystemRole> page = new Page<>(query.getCurrentPage(), query.getPageSize());
        LambdaQueryWrapper<SystemRole> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(query.getKeyword())) {
            wrapper.and(w -> w.like(SystemRole::getName, query.getKeyword())
                    .or().like(SystemRole::getCode, query.getKeyword()));
        }
        wrapper.orderByDesc(SystemRole::getCreatedAt);

        IPage<SystemRole> result = systemRoleMapper.selectPage(page, wrapper);

        List<RoleListVO> list = result.getRecords().stream()
                .map(this::convertToListVO)
                .collect(Collectors.toList());

        return PageResult.from(result, list);
    }

    @Override
    @AuditLog(module = "角色管理", operation = "启用/停用角色", type = "ENABLE")
    public void enableDisable(Long id, String status) {
        SystemRole role = systemRoleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("999904", "角色不存在");
        }

        if (Boolean.TRUE.equals(role.getBuiltin())) {
            throw new BusinessException(SystemErrorCode.BUILTIN_ROLE_CANNOT_DISABLE.getCode(),
                    SystemErrorCode.BUILTIN_ROLE_CANNOT_DISABLE.getMessage());
        }

        SystemRole update = new SystemRole();
        update.setId(id);
        update.setStatus(status);
        systemRoleMapper.updateById(update);

        // 启用/停用角色影响用户权限
        clearRoleUsersPermissionCache(id);
    }

    @Override
    @AuditLog(module = "角色管理", operation = "配置权限", type = "UPDATE")
    @Transactional
    public void configurePermissions(RolePermissionDTO dto) {
        SystemRole role = systemRoleMapper.selectById(dto.getRoleId());
        if (role == null) {
            throw new BusinessException("999904", "角色不存在");
        }

        // 超管角色不可修改
        if (SystemConstants.ROLE_SUPER_ADMIN.equals(role.getCode())) {
            throw new BusinessException(SystemErrorCode.SUPER_ADMIN_ROLE_READONLY.getCode(),
                    SystemErrorCode.SUPER_ADMIN_ROLE_READONLY.getMessage());
        }

        // 更新 dataScope
        SystemRole update = new SystemRole();
        update.setId(dto.getRoleId());
        update.setDataScope(dto.getDataScope());
        update.setCustomWorkspaceCodes(dto.getCustomWorkspaceCodes());
        systemRoleMapper.updateById(update);

        // 删除旧菜单关联
        systemRoleMenuMapper.deleteByRoleId(dto.getRoleId());

        // 插入新菜单关联
        if (dto.getMenuIds() != null) {
            for (Long menuId : dto.getMenuIds()) {
                SystemRoleMenu rm = new SystemRoleMenu();
                rm.setRoleId(dto.getRoleId());
                rm.setMenuId(menuId);
                systemRoleMenuMapper.insert(rm);
            }
        }

        // 清除关联用户权限缓存
        clearRoleUsersPermissionCache(dto.getRoleId());
    }

    @Override
    @AuditLog(module = "角色管理", operation = "分配用户", type = "UPDATE")
    @Transactional
    public void assignUsers(Long roleId, List<Long> userIds) {
        SystemRole role = systemRoleMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException("999904", "角色不存在");
        }

        // 删除该角色的所有用户关联
        systemUserRoleMapper.deleteByRoleId(roleId);

        // 插入新关联
        if (userIds != null) {
            for (Long userId : userIds) {
                SystemUserRole ur = new SystemUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                systemUserRoleMapper.insert(ur);
            }
            // 清除关联用户权限缓存
            for (Long userId : userIds) {
                stringRedisTemplate.delete(SystemConstants.REDIS_USER_PERMS_PREFIX + userId);
            }
        }
    }

    private void clearRoleUsersPermissionCache(Long roleId) {
        LambdaQueryWrapper<SystemUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemUserRole::getRoleId, roleId);
        List<SystemUserRole> userRoles = systemUserRoleMapper.selectList(wrapper);
        for (SystemUserRole ur : userRoles) {
            stringRedisTemplate.delete(SystemConstants.REDIS_USER_PERMS_PREFIX + ur.getUserId());
        }
    }

    @Override
    public List<RoleListVO> listAll(String keyword) {
        LambdaQueryWrapper<SystemRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemRole::getStatus, "ENABLED");
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(SystemRole::getName, keyword)
                    .or().like(SystemRole::getCode, keyword));
        }
        wrapper.orderByAsc(SystemRole::getCreatedAt);
        List<SystemRole> roles = systemRoleMapper.selectList(wrapper);
        return roles.stream().map(this::convertToListVO).collect(Collectors.toList());
    }

    private RoleListVO convertToListVO(SystemRole role) {
        LambdaQueryWrapper<SystemUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemUserRole::getRoleId, role.getId());
        int memberCount = Math.toIntExact(systemUserRoleMapper.selectCount(wrapper));

        return RoleListVO.builder()
                .id(role.getId())
                .name(role.getName())
                .code(role.getCode())
                .type(role.getType())
                .dataScope(role.getDataScope())
                .builtin(role.getBuiltin())
                .status(role.getStatus())
                .memberCount(memberCount)
                .remark(role.getRemark())
                .updatedAt(role.getUpdatedAt())
                .build();
    }
}
