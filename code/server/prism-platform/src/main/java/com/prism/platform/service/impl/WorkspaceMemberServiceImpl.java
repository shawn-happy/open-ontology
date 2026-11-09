package com.prism.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prism.common.aspect.AuditLog;
import com.prism.common.enums.MemberSource;
import com.prism.common.enums.WorkspaceRole;
import com.prism.common.exception.BusinessException;
import com.prism.common.result.PageResult;
import com.prism.common.security.SecurityUtils;
import com.prism.platform.constants.SystemConstants;
import com.prism.platform.constants.WorkspaceConstants;
import com.prism.platform.dto.MemberPageQuery;
import com.prism.platform.dto.MemberRemoveDTO;
import com.prism.platform.dto.MemberRoleDTO;
import com.prism.platform.dto.WorkspaceAssignDTO;
import com.prism.platform.dto.WorkspaceRemoveDeptDTO;
import com.prism.platform.entity.SystemRole;
import com.prism.platform.entity.Workspace;
import com.prism.platform.entity.WorkspaceDept;
import com.prism.platform.entity.WorkspaceMember;
import com.prism.platform.exception.WorkspaceErrorCode;
import com.prism.platform.mapper.SystemDeptMapper;
import com.prism.platform.mapper.SystemRoleMapper;
import com.prism.platform.mapper.WorkspaceDeptMapper;
import com.prism.platform.mapper.WorkspaceMapper;
import com.prism.platform.mapper.WorkspaceMemberMapper;
import com.prism.platform.service.WorkspaceMemberService;
import com.prism.platform.service.support.WorkspaceCacheSupport;
import com.prism.platform.service.support.WorkspaceRoleChecker;
import com.prism.platform.vo.AssignResultVO;
import com.prism.platform.vo.MemberListVO;
import com.prism.platform.vo.MemberStatsVO;
import com.prism.platform.vo.WorkspaceRoleVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 空间成员分配服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceMemberServiceImpl implements WorkspaceMemberService {

    private final WorkspaceMapper workspaceMapper;
    private final WorkspaceMemberMapper workspaceMemberMapper;
    private final WorkspaceDeptMapper workspaceDeptMapper;
    private final SystemRoleMapper systemRoleMapper;
    private final SystemDeptMapper systemDeptMapper;
    private final WorkspaceRoleChecker roleChecker;
    private final WorkspaceCacheSupport cacheSupport;

    // ===== 平台级：批量分配 =====

    @Override
    @AuditLog(module = "工作空间管理", operation = "批量分配用户", type = "ASSIGN")
    @Transactional
    public AssignResultVO assignUsers(Long workspaceId, WorkspaceAssignDTO dto) {
        Workspace ws = requireEnabledWorkspace(workspaceId);
        String role = StringUtils.isNotBlank(dto.getWorkspaceRole())
                ? dto.getWorkspaceRole() : WorkspaceRole.REPORTER.getCode();
        validateRole(ws.getCode(), role);

        Long operatorId = SecurityUtils.getCurrentUserId();
        int success = 0;
        int skip = 0;
        // 单事务先查后插，缓解并发双插（无唯一键约束）
        Set<Long> handled = new HashSet<>();
        for (Long userId : dto.getUserIds()) {
            if (userId == null || !handled.add(userId)) {
                continue;
            }
            WorkspaceMember direct = selectBySource(ws.getCode(), userId, MemberSource.DIRECT.getValue());
            if (direct != null) {
                // 已是直接分配成员 → 幂等跳过
                skip++;
                continue;
            }
            if (workspaceMemberMapper.selectEffectiveRole(ws.getCode(), userId) != null) {
                // 部门来源有效成员 → 幂等跳过
                skip++;
                continue;
            }
            WorkspaceMember excluded = selectBySource(ws.getCode(), userId, MemberSource.EXCLUDED.getValue());
            if (excluded != null) {
                // 存在排除记录 → 改写为 DIRECT（直接分配优先于排除；dept_id 显式置空）
                workspaceMemberMapper.update(null,
                        new LambdaUpdateWrapper<WorkspaceMember>()
                                .eq(WorkspaceMember::getId, excluded.getId())
                                .set(WorkspaceMember::getSource, MemberSource.DIRECT.getValue())
                                .set(WorkspaceMember::getWorkspaceRole, role)
                                .set(WorkspaceMember::getDeptId, null)
                                .set(WorkspaceMember::getUpdatedBy, operatorId));
                // 级联清理残留 PERSONAL 记录
                deleteBySource(ws.getCode(), userId, MemberSource.PERSONAL.getValue());
            }
            else {
                WorkspaceMember member = new WorkspaceMember();
                member.setWorkspaceCode(ws.getCode());
                member.setUserId(userId);
                member.setWorkspaceRole(role);
                member.setSource(MemberSource.DIRECT.getValue());
                member.setCreatedBy(operatorId);
                member.setUpdatedBy(operatorId);
                workspaceMemberMapper.insert(member);
            }
            success++;
            evictMemberCache(ws.getCode(), userId);
        }
        evictOverviewCache();
        return AssignResultVO.builder().successCount(success).skipCount(skip).build();
    }

    @Override
    @AuditLog(module = "工作空间管理", operation = "批量分配部门", type = "ASSIGN")
    @Transactional
    public AssignResultVO assignDepts(Long workspaceId, WorkspaceAssignDTO dto) {
        Workspace ws = requireEnabledWorkspace(workspaceId);
        String defaultRole = StringUtils.isNotBlank(dto.getWorkspaceRole())
                ? dto.getWorkspaceRole() : WorkspaceRole.REPORTER.getCode();
        validateRole(ws.getCode(), defaultRole);

        Long operatorId = SecurityUtils.getCurrentUserId();
        int success = 0;
        int skip = 0;
        Set<Long> handled = new HashSet<>();
        for (Long deptId : dto.getDeptIds()) {
            if (deptId == null || !handled.add(deptId)) {
                continue;
            }
            if (systemDeptMapper.selectById(deptId) == null) {
                throw businessException(WorkspaceErrorCode.DEPT_NOT_FOUND);
            }
            Long existing = workspaceDeptMapper.selectCount(new LambdaQueryWrapper<WorkspaceDept>()
                    .eq(WorkspaceDept::getWorkspaceCode, ws.getCode())
                    .eq(WorkspaceDept::getDeptId, deptId));
            if (existing > 0) {
                // 已分配 → 幂等跳过（020204 仅日志提示不报错）
                log.info("部门已分配至空间，跳过：workspace={}, deptId={}", ws.getCode(), deptId);
                skip++;
                continue;
            }
            WorkspaceDept dept = new WorkspaceDept();
            dept.setWorkspaceCode(ws.getCode());
            dept.setDeptId(deptId);
            dept.setDefaultRole(defaultRole);
            dept.setCreatedBy(operatorId);
            dept.setUpdatedBy(operatorId);
            workspaceDeptMapper.insert(dept);
            success++;
        }
        // 部门维度变更影响成员集合不确定 → 前缀 SCAN 清理空间成员缓存
        evictWorkspaceMemberCaches(ws.getCode());
        evictOverviewCache();
        return AssignResultVO.builder().successCount(success).skipCount(skip).build();
    }

    @Override
    @AuditLog(module = "工作空间管理", operation = "移除分配部门", type = "REMOVE")
    @Transactional
    public void removeDept(Long workspaceId, WorkspaceRemoveDeptDTO dto) {
        Workspace ws = requireWorkspace(workspaceId);
        WorkspaceDept record = workspaceDeptMapper.selectOne(new LambdaQueryWrapper<WorkspaceDept>()
                .eq(WorkspaceDept::getWorkspaceCode, ws.getCode())
                .eq(WorkspaceDept::getDeptId, dto.getDeptId())
                .last("LIMIT 1"));
        if (record == null) {
            // 幂等：无分配记录直接返回
            return;
        }
        workspaceDeptMapper.deleteById(record.getId());

        // 清理该部门来源的 PERSONAL/EXCLUDED 记录（用户不再被其他分配部门覆盖时删除，否则改挂新部门）
        List<WorkspaceMember> derived = workspaceMemberMapper.selectList(new LambdaQueryWrapper<WorkspaceMember>()
                .eq(WorkspaceMember::getWorkspaceCode, ws.getCode())
                .eq(WorkspaceMember::getDeptId, dto.getDeptId())
                .in(WorkspaceMember::getSource,
                        List.of(MemberSource.PERSONAL.getValue(), MemberSource.EXCLUDED.getValue())));
        for (WorkspaceMember member : derived) {
            List<Long> covering = workspaceDeptMapper.selectCoveringDeptIds(ws.getCode(), member.getUserId(),
                    dto.getDeptId());
            if (covering.isEmpty()) {
                workspaceMemberMapper.deleteById(member.getId());
            }
            else {
                WorkspaceMember update = new WorkspaceMember();
                update.setId(member.getId());
                update.setDeptId(covering.get(0));
                workspaceMemberMapper.updateById(update);
            }
            evictMemberCache(ws.getCode(), member.getUserId());
        }
        evictWorkspaceMemberCaches(ws.getCode());
        evictOverviewCache();
    }

    // ===== 空间级：查询 =====

    @Override
    public PageResult<MemberListVO> pageMembers(String workspaceCode, MemberPageQuery query) {
        Page<MemberListVO> page = new Page<>(query.getCurrentPage(), query.getPageSize());
        var result = workspaceMemberMapper.pageEffectiveMembers(page, workspaceCode, query);

        // 补充角色显示名与等级（内置枚举 + 自定义角色）
        Map<String, SystemRole> customRoles = systemRoleMapper.selectCustomWorkspaceRoles(workspaceCode).stream()
                .collect(Collectors.toMap(SystemRole::getCode, Function.identity(), (a, b) -> a));
        for (MemberListVO vo : result.getRecords()) {
            WorkspaceRole builtin = WorkspaceRole.fromCode(vo.getWorkspaceRole());
            if (builtin != null) {
                vo.setRoleDisplayName(builtin.getDisplayName());
                vo.setRoleLevel(builtin.getLevel());
            }
            else {
                SystemRole custom = customRoles.get(vo.getWorkspaceRole());
                vo.setRoleDisplayName(custom != null ? custom.getName() : vo.getWorkspaceRole());
                vo.setRoleLevel(WorkspaceRole.CUSTOM_ROLE_LEVEL);
            }
        }
        return PageResult.from(result);
    }

    @Override
    public MemberStatsVO getMemberStats(String workspaceCode) {
        int total = workspaceMemberMapper.countEffectiveMembers(workspaceCode, null, null);
        int direct = workspaceMemberMapper.countEffectiveMembers(workspaceCode,
                WorkspaceConstants.SOURCE_DIRECT, null);
        int owner = workspaceMemberMapper.countEffectiveMembers(workspaceCode, null,
                WorkspaceRole.OWNER.getCode());
        return MemberStatsVO.builder()
                .totalMembers(total)
                .directCount(direct)
                .deptCount(total - direct)
                .ownerCount(owner)
                .build();
    }

    @Override
    public List<WorkspaceRoleVO> listRoles(String workspaceCode) {
        List<WorkspaceRoleVO> list = new ArrayList<>();
        for (WorkspaceRole role : WorkspaceRole.values()) {
            list.add(WorkspaceRoleVO.builder()
                    .code(role.getCode())
                    .displayName(role.getDisplayName())
                    .description(role.getDescription())
                    .level(role.getLevel())
                    .builtin(true)
                    .build());
        }
        for (SystemRole custom : systemRoleMapper.selectCustomWorkspaceRoles(workspaceCode)) {
            list.add(WorkspaceRoleVO.builder()
                    .code(custom.getCode())
                    .displayName(custom.getName())
                    .description(custom.getRemark())
                    .level(WorkspaceRole.CUSTOM_ROLE_LEVEL)
                    .builtin(false)
                    .build());
        }
        return list;
    }

    @Override
    public String getEffectiveRole(String workspaceCode, Long userId) {
        if (workspaceCode == null || userId == null) {
            return null;
        }
        String cached = cacheSupport.getMemberRole(workspaceCode, userId);
        if (cached != null) {
            return WorkspaceConstants.MEMBER_CACHE_NONE.equals(cached) ? null : cached;
        }
        String role = workspaceMemberMapper.selectEffectiveRole(workspaceCode, userId);
        cacheSupport.putMemberRole(workspaceCode, userId,
                role != null ? role : WorkspaceConstants.MEMBER_CACHE_NONE);
        return role;
    }

    // ===== 空间级：角色调整与移除 =====

    @Override
    @AuditLog(module = "成员分配", operation = "分配/覆盖成员角色", type = "ASSIGN")
    @Transactional
    public void updateMemberRole(String workspaceCode, MemberRoleDTO dto) {
        Workspace ws = workspaceMapper.selectByCode(workspaceCode);
        if (ws == null) {
            throw businessException(WorkspaceErrorCode.WORKSPACE_NOT_FOUND);
        }
        Long operatorId = SecurityUtils.getCurrentUserId();
        boolean superAdmin = isSuperAdmin(operatorId);

        // 1. 目标必须是有效成员（020202）
        String targetRole = workspaceMemberMapper.selectEffectiveRole(workspaceCode, dto.getUserId());
        if (targetRole == null) {
            throw businessException(WorkspaceErrorCode.MEMBER_NOT_FOUND);
        }

        String newRole = StringUtils.trimToNull(dto.getWorkspaceRole());

        // 2. 空间创建人（owner_user_id）的 OWNER 角色不可覆盖或降级（020206）
        if (dto.getUserId().equals(ws.getOwnerUserId())
                && WorkspaceRole.OWNER.getCode().equals(targetRole)
                && !WorkspaceRole.OWNER.getCode().equals(newRole)) {
            throw businessException(WorkspaceErrorCode.OWNER_ROLE_CANNOT_OVERRIDE);
        }

        String operatorRole = superAdmin ? null
                : workspaceMemberMapper.selectEffectiveRole(workspaceCode, operatorId);

        // 3. 角色等级约束（FR-22）：仅能操作等级严格低于自己的成员（020207）
        if (!superAdmin) {
            int operatorLevel = operatorRole != null
                    ? WorkspaceRole.levelOf(operatorRole) : 0;
            roleChecker.assertCanOperate(operatorLevel, WorkspaceRole.levelOf(targetRole));
        }

        // 4. 新角色合法性（020301）+ Owner 仅能由 Owner 授予（020208）
        if (newRole != null) {
            validateRole(workspaceCode, newRole);
            if (!superAdmin) {
                roleChecker.assertCanGrant(operatorRole, newRole);
            }
        }

        // 5. 唯一 Owner 保护（FR-18）：降级 / 清除 Owner 覆盖时须保留至少一名 Owner（020203）
        boolean losingOwner = WorkspaceRole.OWNER.getCode().equals(targetRole)
                && !WorkspaceRole.OWNER.getCode().equals(newRole);
        if (losingOwner) {
            assertNotLastOwner(workspaceCode);
        }

        // 6. 落库：DIRECT 直接改角色；部门来源 upsert PERSONAL；newRole 为空清除 PERSONAL
        WorkspaceMember direct = selectBySource(workspaceCode, dto.getUserId(), MemberSource.DIRECT.getValue());
        if (direct != null) {
            if (newRole != null) {
                WorkspaceMember update = new WorkspaceMember();
                update.setId(direct.getId());
                update.setWorkspaceRole(newRole);
                update.setUpdatedBy(operatorId);
                workspaceMemberMapper.updateById(update);
            }
            // DIRECT 成员无「个人覆盖」概念，newRole 为空时不做变更
        }
        else {
            WorkspaceMember personal = selectBySource(workspaceCode, dto.getUserId(),
                    MemberSource.PERSONAL.getValue());
            if (newRole == null) {
                // 清除个人角色覆盖，恢复部门默认
                if (personal != null) {
                    workspaceMemberMapper.deleteById(personal.getId());
                }
            }
            else if (personal != null) {
                WorkspaceMember update = new WorkspaceMember();
                update.setId(personal.getId());
                update.setWorkspaceRole(newRole);
                update.setUpdatedBy(operatorId);
                workspaceMemberMapper.updateById(update);
            }
            else {
                List<Long> covering = workspaceDeptMapper.selectCoveringDeptIds(workspaceCode, dto.getUserId(), null);
                WorkspaceMember insert = new WorkspaceMember();
                insert.setWorkspaceCode(workspaceCode);
                insert.setUserId(dto.getUserId());
                insert.setWorkspaceRole(newRole);
                insert.setSource(MemberSource.PERSONAL.getValue());
                insert.setDeptId(covering.isEmpty() ? null : covering.get(0));
                insert.setCreatedBy(operatorId);
                insert.setUpdatedBy(operatorId);
                workspaceMemberMapper.insert(insert);
            }
        }

        evictMemberCache(workspaceCode, dto.getUserId());
        evictOverviewCache();
    }

    @Override
    @AuditLog(module = "成员分配", operation = "移除成员", type = "REMOVE")
    @Transactional
    public void removeMember(String workspaceCode, MemberRemoveDTO dto) {
        Long operatorId = SecurityUtils.getCurrentUserId();
        boolean superAdmin = isSuperAdmin(operatorId);

        // 1. 目标必须是有效成员（020202）
        String targetRole = workspaceMemberMapper.selectEffectiveRole(workspaceCode, dto.getUserId());
        if (targetRole == null) {
            throw businessException(WorkspaceErrorCode.MEMBER_NOT_FOUND);
        }

        // 2. 角色等级约束（FR-22）：不可移除等级不低于自己的成员（含 Owner 移除自身 → 020207）
        if (!superAdmin) {
            String operatorRole = workspaceMemberMapper.selectEffectiveRole(workspaceCode, operatorId);
            int operatorLevel = operatorRole != null ? WorkspaceRole.levelOf(operatorRole) : 0;
            roleChecker.assertCanOperate(operatorLevel, WorkspaceRole.levelOf(targetRole));
        }

        WorkspaceMember direct = selectBySource(workspaceCode, dto.getUserId(), MemberSource.DIRECT.getValue());
        if (direct != null) {
            // 分支一：直接分配 → 唯一 Owner 校验后逻辑删除 + 级联清理 PERSONAL/EXCLUDED
            if (WorkspaceRole.OWNER.getCode().equals(direct.getWorkspaceRole())) {
                assertNotLastOwner(workspaceCode);
            }
            deleteBySource(workspaceCode, dto.getUserId(), MemberSource.DIRECT.getValue());
            deleteBySource(workspaceCode, dto.getUserId(), MemberSource.PERSONAL.getValue());
            deleteBySource(workspaceCode, dto.getUserId(), MemberSource.EXCLUDED.getValue());
        }
        else {
            WorkspaceMember personal = selectBySource(workspaceCode, dto.getUserId(),
                    MemberSource.PERSONAL.getValue());
            List<Long> covering = workspaceDeptMapper.selectCoveringDeptIds(workspaceCode, dto.getUserId(), null);
            if (personal != null && !covering.isEmpty()) {
                // 分支二：部门来源已覆盖个人角色 → 仅清除覆盖，角色恢复部门默认
                if (WorkspaceRole.OWNER.getCode().equals(personal.getWorkspaceRole())) {
                    assertNotLastOwner(workspaceCode);
                }
                workspaceMemberMapper.deleteById(personal.getId());
            }
            else {
                // 分支三：部门来源未覆盖 → 拒绝（020209，前端引导部门维度移除，此处仅兜底）
                throw businessException(WorkspaceErrorCode.DEPT_SOURCE_MEMBER_NOT_REMOVABLE);
            }
        }

        evictMemberCache(workspaceCode, dto.getUserId());
        evictOverviewCache();
    }

    // ===== 私有方法 =====

    private Workspace requireWorkspace(Long id) {
        Workspace ws = id == null ? null : workspaceMapper.selectById(id);
        if (ws == null) {
            throw businessException(WorkspaceErrorCode.WORKSPACE_NOT_FOUND);
        }
        return ws;
    }

    private Workspace requireEnabledWorkspace(Long id) {
        Workspace ws = requireWorkspace(id);
        if (!WorkspaceConstants.STATUS_ENABLED.equals(ws.getStatus())) {
            throw businessException(WorkspaceErrorCode.WORKSPACE_DISABLED);
        }
        return ws;
    }

    /**
     * 角色合法性：内置五级角色，或绑定该空间的自定义空间角色（type=WORKSPACE）；否则 020301
     */
    private void validateRole(String workspaceCode, String role) {
        if (WorkspaceRole.fromCode(role) != null) {
            return;
        }
        boolean customHit = systemRoleMapper.selectCustomWorkspaceRoles(workspaceCode).stream()
                .anyMatch(r -> r.getCode().equals(role));
        if (!customHit) {
            throw businessException(WorkspaceErrorCode.WORKSPACE_ROLE_NOT_FOUND);
        }
    }

    /**
     * 唯一 Owner 保护（FR-18）：FOR UPDATE 锁定有效 Owner 记录，去重后 ≤1 名则拒绝（020203）
     */
    private void assertNotLastOwner(String workspaceCode) {
        List<WorkspaceMember> owners = workspaceMemberMapper.selectOwnersForUpdate(workspaceCode);
        long distinctOwners = owners.stream().map(WorkspaceMember::getUserId).distinct().count();
        if (distinctOwners <= 1) {
            throw businessException(WorkspaceErrorCode.LAST_OWNER_CANNOT_REMOVE);
        }
    }

    private boolean isSuperAdmin(Long userId) {
        if (userId == null) {
            return false;
        }
        if (SystemConstants.ADMIN_USER_ID.equals(userId)) {
            return true;
        }
        List<SystemRole> roles = systemRoleMapper.selectByUserId(userId);
        return roles != null && roles.stream()
                .anyMatch(r -> "ENABLED".equals(r.getStatus())
                        && SystemConstants.ROLE_SUPER_ADMIN.equals(r.getCode()));
    }

    private WorkspaceMember selectBySource(String workspaceCode, Long userId, String source) {
        return workspaceMemberMapper.selectOne(new LambdaQueryWrapper<WorkspaceMember>()
                .eq(WorkspaceMember::getWorkspaceCode, workspaceCode)
                .eq(WorkspaceMember::getUserId, userId)
                .eq(WorkspaceMember::getSource, source)
                .last("LIMIT 1"));
    }

    private void deleteBySource(String workspaceCode, Long userId, String source) {
        workspaceMemberMapper.delete(new LambdaQueryWrapper<WorkspaceMember>()
                .eq(WorkspaceMember::getWorkspaceCode, workspaceCode)
                .eq(WorkspaceMember::getUserId, userId)
                .eq(WorkspaceMember::getSource, source));
    }

    private void evictMemberCache(String workspaceCode, Long userId) {
        cacheSupport.evictMember(workspaceCode, userId);
    }

    private void evictOverviewCache() {
        cacheSupport.evictOverview();
    }

    private void evictWorkspaceMemberCaches(String workspaceCode) {
        cacheSupport.evictWorkspaceMembers(workspaceCode);
    }

    private BusinessException businessException(WorkspaceErrorCode errorCode) {
        return new BusinessException(errorCode.getCode(), errorCode.getMessage());
    }
}
