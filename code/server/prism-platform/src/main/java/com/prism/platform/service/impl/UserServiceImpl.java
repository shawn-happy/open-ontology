package com.prism.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prism.common.aspect.AuditLog;
import com.prism.common.exception.BusinessException;
import com.prism.common.result.PageResult;
import com.prism.platform.constants.SystemConstants;
import com.prism.platform.dto.CreateUserDTO;
import com.prism.platform.dto.UpdateUserDTO;
import com.prism.platform.dto.UserPageQuery;
import com.prism.platform.entity.SystemDept;
import com.prism.platform.entity.SystemRole;
import com.prism.platform.entity.SystemUser;
import com.prism.platform.entity.SystemUserDept;
import com.prism.platform.entity.SystemUserRole;
import com.prism.platform.exception.SystemErrorCode;
import com.prism.platform.mapper.SystemDeptMapper;
import com.prism.platform.mapper.SystemRoleMapper;
import com.prism.platform.mapper.SystemUserDeptMapper;
import com.prism.platform.mapper.SystemUserMapper;
import com.prism.platform.mapper.SystemUserRoleMapper;
import com.prism.platform.service.UserService;
import com.prism.platform.vo.ImportResultVO;
import com.prism.platform.vo.UserDetailVO;
import com.prism.platform.vo.UserListVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户管理服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SystemUserMapper systemUserMapper;
    private final SystemUserRoleMapper systemUserRoleMapper;
    private final SystemUserDeptMapper systemUserDeptMapper;
    private final SystemRoleMapper systemRoleMapper;
    private final SystemDeptMapper systemDeptMapper;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    @AuditLog(module = "用户管理", operation = "新增用户", type = "CREATE")
    @Transactional
    public void create(CreateUserDTO dto) {
        // 校验用户名唯一
        SystemUser existing = systemUserMapper.selectByUsername(dto.getUsername());
        if (existing != null) {
            throw new BusinessException(SystemErrorCode.USERNAME_EXISTS.getCode(),
                    SystemErrorCode.USERNAME_EXISTS.getMessage());
        }

        // 校验手机号唯一
        if (StringUtils.isNotBlank(dto.getPhone())) {
            SystemUser byPhone = systemUserMapper.selectByPhone(dto.getPhone());
            if (byPhone != null) {
                throw new BusinessException(SystemErrorCode.PHONE_EXISTS.getCode(),
                        SystemErrorCode.PHONE_EXISTS.getMessage());
            }
        }

        // 校验邮箱唯一
        if (StringUtils.isNotBlank(dto.getEmail())) {
            SystemUser byEmail = systemUserMapper.selectByEmail(dto.getEmail());
            if (byEmail != null) {
                throw new BusinessException(SystemErrorCode.EMAIL_EXISTS.getCode(),
                        SystemErrorCode.EMAIL_EXISTS.getMessage());
            }
        }

        // 创建用户
        SystemUser user = new SystemUser();
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setStatus(dto.getStatus());
        user.setPassword(passwordEncoder.encode(SystemConstants.INITIAL_PASSWORD));
        user.setLoginFailCount(0);
        systemUserMapper.insert(user);

        // 关联部门
        if (dto.getDeptId() != null) {
            SystemUserDept userDept = new SystemUserDept();
            userDept.setUserId(user.getId());
            userDept.setDeptId(dto.getDeptId());
            systemUserDeptMapper.insert(userDept);
        }
    }

    @Override
    @AuditLog(module = "用户管理", operation = "编辑用户", type = "UPDATE")
    @Transactional
    public void update(UpdateUserDTO dto) {
        SystemUser user = systemUserMapper.selectById(dto.getId());
        if (user == null) {
            throw new BusinessException("999904", "用户不存在");
        }

        // admin 用户名不可修改
        if (SystemConstants.ADMIN_USERNAME.equals(user.getUsername())
                && dto.getUsername() != null
                && !SystemConstants.ADMIN_USERNAME.equals(dto.getUsername())) {
            throw new BusinessException(SystemErrorCode.ADMIN_USERNAME_READONLY.getCode(),
                    SystemErrorCode.ADMIN_USERNAME_READONLY.getMessage());
        }

        // 校验手机号唯一
        if (StringUtils.isNotBlank(dto.getPhone())) {
            SystemUser byPhone = systemUserMapper.selectByPhone(dto.getPhone());
            if (byPhone != null && !byPhone.getId().equals(dto.getId())) {
                throw new BusinessException(SystemErrorCode.PHONE_EXISTS.getCode(),
                        SystemErrorCode.PHONE_EXISTS.getMessage());
            }
        }

        // 校验邮箱唯一
        if (StringUtils.isNotBlank(dto.getEmail())) {
            SystemUser byEmail = systemUserMapper.selectByEmail(dto.getEmail());
            if (byEmail != null && !byEmail.getId().equals(dto.getId())) {
                throw new BusinessException(SystemErrorCode.EMAIL_EXISTS.getCode(),
                        SystemErrorCode.EMAIL_EXISTS.getMessage());
            }
        }

        SystemUser update = new SystemUser();
        update.setId(dto.getId());
        update.setName(dto.getName());
        update.setPhone(dto.getPhone());
        update.setEmail(dto.getEmail());
        update.setStatus(dto.getStatus());
        systemUserMapper.updateById(update);

        // 更新部门关联
        if (dto.getDeptId() != null) {
            systemUserDeptMapper.deleteByUserId(dto.getId());
            SystemUserDept userDept = new SystemUserDept();
            userDept.setUserId(dto.getId());
            userDept.setDeptId(dto.getDeptId());
            systemUserDeptMapper.insert(userDept);
        }
    }

    @Override
    @AuditLog(module = "用户管理", operation = "删除用户", type = "DELETE")
    @Transactional
    public void delete(Long id) {
        SystemUser user = systemUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("999904", "用户不存在");
        }
        if (SystemConstants.ADMIN_USER_ID.equals(id)) {
            throw new BusinessException(SystemErrorCode.ADMIN_CANNOT_DISABLE.getCode(),
                    "admin 账号不可删除");
        }
        systemUserMapper.deleteById(id);
        systemUserRoleMapper.deleteByUserId(id);
        systemUserDeptMapper.deleteByUserId(id);
        // 清除权限缓存
        stringRedisTemplate.delete(SystemConstants.REDIS_USER_PERMS_PREFIX + id);
    }

    @Override
    public UserDetailVO getById(Long id) {
        SystemUser user = systemUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("999904", "用户不存在");
        }

        // 查询角色
        List<SystemRole> roles = systemRoleMapper.selectByUserId(id);
        List<Long> roleIds = roles.stream().map(SystemRole::getId).collect(Collectors.toList());
        List<String> roleNames = roles.stream().map(SystemRole::getName).collect(Collectors.toList());

        // 查询部门
        SystemUserDept userDept = systemUserDeptMapper.selectByUserId(id);
        Long deptId = userDept != null ? userDept.getDeptId() : null;
        String deptName = null;
        if (deptId != null) {
            SystemDept dept = systemDeptMapper.selectById(deptId);
            if (dept != null) {
                deptName = dept.getName();
            }
        }

        return UserDetailVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .status(user.getStatus())
                .deptId(deptId)
                .deptName(deptName)
                .roleIds(roleIds)
                .roleNames(roleNames)
                .lastLoginAt(user.getLastLoginAt())
                .lastLoginIp(user.getLastLoginIp())
                .pwdUpdateTime(user.getPwdUpdateTime())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public PageResult<UserListVO> page(UserPageQuery query) {
        Page<SystemUser> page = new Page<>(query.getCurrentPage(), query.getPageSize());
        LambdaQueryWrapper<SystemUser> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(query.getKeyword())) {
            wrapper.and(w -> w.like(SystemUser::getUsername, query.getKeyword())
                    .or().like(SystemUser::getName, query.getKeyword())
                    .or().like(SystemUser::getPhone, query.getKeyword()));
        }
        if (StringUtils.isNotBlank(query.getStatus())) {
            wrapper.eq(SystemUser::getStatus, query.getStatus());
        }

        // 按部门筛选（含子部门）：子查询下推给数据库，避免搬运用户 ID 列表
        if (query.getDeptId() != null) {
            applyDeptSubtreeFilter(wrapper, query.getDeptId());
        }
        wrapper.orderByDesc(SystemUser::getCreatedAt);

        IPage<SystemUser> result = systemUserMapper.selectPage(page, wrapper);

        List<UserListVO> list = convertToListVOs(result.getRecords());

        return PageResult.from(result, list);
    }

    @Override
    @AuditLog(module = "用户管理", operation = "启用/停用用户", type = "ENABLE")
    public void enableDisable(Long id, String status) {
        SystemUser user = systemUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("999904", "用户不存在");
        }
        if (SystemConstants.ADMIN_USER_ID.equals(id) && "DISABLED".equals(status)) {
            throw new BusinessException(SystemErrorCode.ADMIN_CANNOT_DISABLE.getCode(),
                    SystemErrorCode.ADMIN_CANNOT_DISABLE.getMessage());
        }

        SystemUser update = new SystemUser();
        update.setId(id);
        update.setStatus(status);
        systemUserMapper.updateById(update);
    }

    @Override
    @AuditLog(module = "用户管理", operation = "重置密码", type = "UPDATE")
    public void resetPassword(Long id) {
        SystemUser user = systemUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("999904", "用户不存在");
        }
        SystemUser update = new SystemUser();
        update.setId(id);
        update.setPassword(passwordEncoder.encode(SystemConstants.INITIAL_PASSWORD));
        update.setPwdUpdateTime(null);
        systemUserMapper.updateById(update);
    }

    @Override
    @AuditLog(module = "用户管理", operation = "分配角色", type = "UPDATE")
    @Transactional
    public void assignRoles(Long userId, List<Long> roleIds) {
        SystemUser user = systemUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("999904", "用户不存在");
        }

        // 超管角色仅 admin 可分配
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                SystemRole role = systemRoleMapper.selectById(roleId);
                if (role != null && SystemConstants.ROLE_SUPER_ADMIN.equals(role.getCode())) {
                    if (!SystemConstants.ADMIN_USER_ID.equals(userId)) {
                        throw new BusinessException(SystemErrorCode.SUPER_ADMIN_ONLY_FOR_ADMIN.getCode(),
                                SystemErrorCode.SUPER_ADMIN_ONLY_FOR_ADMIN.getMessage());
                    }
                }
            }
        }

        // 删除旧关联
        systemUserRoleMapper.deleteByUserId(userId);

        // 插入新关联
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                SystemUserRole ur = new SystemUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                systemUserRoleMapper.insert(ur);
            }
        }

        // 清除权限缓存
        stringRedisTemplate.delete(SystemConstants.REDIS_USER_PERMS_PREFIX + userId);
    }

    @Override
    @AuditLog(module = "用户管理", operation = "分配部门", type = "UPDATE")
    @Transactional
    public void assignDept(Long userId, Long deptId) {
        SystemUser user = systemUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("999904", "用户不存在");
        }
        if (deptId != null) {
            SystemDept dept = systemDeptMapper.selectById(deptId);
            if (dept == null) {
                throw new BusinessException("999904", "部门不存在");
            }
        }

        systemUserDeptMapper.deleteByUserId(userId);
        if (deptId != null) {
            SystemUserDept userDept = new SystemUserDept();
            userDept.setUserId(userId);
            userDept.setDeptId(deptId);
            systemUserDeptMapper.insert(userDept);
        }
    }

    @Override
    @AuditLog(module = "用户管理", operation = "批量导入用户", type = "CREATE")
    @Transactional
    public ImportResultVO importUsers(MultipartFile file) {
        List<ImportResultVO.FailDetail> failDetails = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int rowNum = 0;
            while ((line = reader.readLine()) != null) {
                rowNum++;
                if (rowNum == 1) {
                    continue; // 跳过表头
                }
                String[] fields = line.split(",");
                if (fields.length < 2) {
                    failCount++;
                    failDetails.add(ImportResultVO.FailDetail.builder()
                            .rowNum(rowNum).username("").reason("字段不足").build());
                    continue;
                }
                String username = fields[0].trim();
                String name = fields[1].trim();
                String phone = fields.length > 2 ? fields[2].trim() : "";
                String email = fields.length > 3 ? fields[3].trim() : "";

                // 校验用户名唯一
                SystemUser existing = systemUserMapper.selectByUsername(username);
                if (existing != null) {
                    failCount++;
                    failDetails.add(ImportResultVO.FailDetail.builder()
                            .rowNum(rowNum).username(username).reason("用户名已存在").build());
                    continue;
                }

                SystemUser user = new SystemUser();
                user.setUsername(username);
                user.setName(name);
                user.setPhone(phone);
                user.setEmail(email);
                user.setStatus("ENABLED");
                user.setPassword(passwordEncoder.encode(SystemConstants.INITIAL_PASSWORD));
                user.setLoginFailCount(0);
                systemUserMapper.insert(user);
                successCount++;
            }
        } catch (Exception e) {
            log.error("导入用户失败", e);
            throw new BusinessException("999999", "导入失败：" + e.getMessage());
        }

        return ImportResultVO.builder()
                .totalCount(successCount + failCount)
                .successCount(successCount)
                .failCount(failCount)
                .failDetails(failDetails)
                .build();
    }

    @Override
    public List<UserListVO> exportUsers(UserPageQuery query) {
        LambdaQueryWrapper<SystemUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(query.getKeyword())) {
            wrapper.and(w -> w.like(SystemUser::getUsername, query.getKeyword())
                    .or().like(SystemUser::getName, query.getKeyword()));
        }
        if (StringUtils.isNotBlank(query.getStatus())) {
            wrapper.eq(SystemUser::getStatus, query.getStatus());
        }
        // 与分页查询保持一致：按部门筛选（含子部门）
        if (query.getDeptId() != null) {
            applyDeptSubtreeFilter(wrapper, query.getDeptId());
        }
        wrapper.orderByDesc(SystemUser::getCreatedAt);

        List<SystemUser> users = systemUserMapper.selectList(wrapper);
        return convertToListVOs(users);
    }

    /**
     * 追加「部门子树」过滤条件（含子部门），子查询下推给数据库执行
     *
     * <p>部门树通过 system_dept.path 物化路径维护：子孙部门的 path 以
     * {@code 父path + 父id + "/"} 为前缀（如部门 3、path=/1/ 的前缀为 /1/3/），
     * 据此在子查询中圈定整棵子树，再关联 system_user_dept 过滤用户。
     * 子查询为原生 SQL，需显式携带 is_deleted = 0（逻辑删除不会自动生效）。
     */
    private void applyDeptSubtreeFilter(LambdaQueryWrapper<SystemUser> wrapper, Long deptId) {
        SystemDept dept = systemDeptMapper.selectById(deptId);
        if (dept == null) {
            throw new BusinessException("999904", "部门不存在");
        }
        // path 仅由系统生成（数字与 /），此处仍对单引号做防御性转义
        String subtreePrefix = (dept.getPath() + dept.getId() + "/").replace("'", "''");
        wrapper.inSql(SystemUser::getId,
                "SELECT user_id FROM system_user_dept WHERE is_deleted = 0 AND dept_id IN ("
                        + "SELECT id FROM system_dept WHERE is_deleted = 0 AND (id = " + deptId
                        + " OR path LIKE '" + subtreePrefix + "%'))");
    }

    /**
     * 批量转换用户列表 VO：固定 4 条 SQL 查出角色与部门关联，避免逐行 N+1 查询
     */
    private List<UserListVO> convertToListVOs(List<SystemUser> users) {
        if (users.isEmpty()) {
            return List.of();
        }
        List<Long> userIds = users.stream().map(SystemUser::getId).collect(Collectors.toList());

        // 批量查角色关联及角色名
        List<SystemUserRole> userRoles = systemUserRoleMapper.selectList(
                new LambdaQueryWrapper<SystemUserRole>().in(SystemUserRole::getUserId, userIds));
        Map<Long, List<Long>> userRoleIdsMap = userRoles.stream()
                .collect(Collectors.groupingBy(SystemUserRole::getUserId,
                        Collectors.mapping(SystemUserRole::getRoleId, Collectors.toList())));
        Set<Long> allRoleIds = userRoles.stream()
                .map(SystemUserRole::getRoleId).collect(Collectors.toSet());
        Map<Long, String> roleNameMap = allRoleIds.isEmpty() ? Map.of()
                : systemRoleMapper.selectBatchIds(allRoleIds).stream()
                        .collect(Collectors.toMap(SystemRole::getId, SystemRole::getName));

        // 批量查部门关联及部门名
        List<SystemUserDept> userDepts = systemUserDeptMapper.selectList(
                new LambdaQueryWrapper<SystemUserDept>().in(SystemUserDept::getUserId, userIds));
        Map<Long, Long> userDeptIdMap = userDepts.stream()
                .collect(Collectors.toMap(SystemUserDept::getUserId, SystemUserDept::getDeptId,
                        (first, second) -> first));
        Set<Long> allDeptIds = new HashSet<>(userDeptIdMap.values());
        Map<Long, String> deptNameMap = allDeptIds.isEmpty() ? Map.of()
                : systemDeptMapper.selectBatchIds(allDeptIds).stream()
                        .collect(Collectors.toMap(SystemDept::getId, SystemDept::getName));

        return users.stream().map(user -> {
            List<Long> roleIds = userRoleIdsMap.getOrDefault(user.getId(), List.of());
            List<String> roleNames = roleIds.stream()
                    .map(roleNameMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            Long deptId = userDeptIdMap.get(user.getId());
            String deptName = deptId != null ? deptNameMap.get(deptId) : null;

            return UserListVO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .name(user.getName())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .avatar(user.getAvatar())
                    .status(user.getStatus())
                    .deptId(deptId)
                    .deptName(deptName)
                    .roleIds(roleIds)
                    .roleNames(roleNames)
                    .lastLoginAt(user.getLastLoginAt())
                    .build();
        }).collect(Collectors.toList());
    }
}
