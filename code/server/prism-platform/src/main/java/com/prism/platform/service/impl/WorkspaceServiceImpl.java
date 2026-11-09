package com.prism.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prism.common.aspect.AuditLog;
import com.prism.common.enums.DataScope;
import com.prism.common.enums.MemberSource;
import com.prism.common.enums.WorkspaceRole;
import com.prism.common.exception.BusinessException;
import com.prism.common.result.PageResult;
import com.prism.common.security.SecurityUtils;
import com.prism.platform.constants.SystemConstants;
import com.prism.platform.constants.WorkspaceConstants;
import com.prism.platform.dto.WorkspacePageQuery;
import com.prism.platform.dto.WorkspaceSaveDTO;
import com.prism.platform.entity.SystemDept;
import com.prism.platform.entity.SystemRole;
import com.prism.platform.entity.SystemUser;
import com.prism.platform.entity.SystemUserDept;
import com.prism.platform.entity.Workspace;
import com.prism.platform.entity.WorkspaceDept;
import com.prism.platform.entity.WorkspaceMember;
import com.prism.platform.exception.WorkspaceErrorCode;
import com.prism.platform.mapper.SystemDeptMapper;
import com.prism.platform.mapper.SystemRoleMapper;
import com.prism.platform.mapper.SystemUserDeptMapper;
import com.prism.platform.mapper.SystemUserMapper;
import com.prism.platform.mapper.WorkspaceDeptMapper;
import com.prism.platform.mapper.WorkspaceMapper;
import com.prism.platform.mapper.WorkspaceMemberMapper;
import com.prism.platform.service.WorkspaceService;
import com.prism.platform.service.support.WorkspaceCacheSupport;
import com.prism.platform.service.support.WorkspaceDefaultRoleResolver;
import com.prism.platform.vo.WorkspaceDetailVO;
import com.prism.platform.vo.WorkspaceListVO;
import com.prism.platform.vo.WorkspaceMemberCountVO;
import com.prism.platform.vo.WorkspaceOverviewVO;
import com.prism.platform.vo.WorkspaceSimpleVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 工作空间管理服务实现（平台级）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceMapper workspaceMapper;
    private final WorkspaceMemberMapper workspaceMemberMapper;
    private final WorkspaceDeptMapper workspaceDeptMapper;
    private final SystemUserMapper systemUserMapper;
    private final SystemUserDeptMapper systemUserDeptMapper;
    private final SystemDeptMapper systemDeptMapper;
    private final SystemRoleMapper systemRoleMapper;
    private final WorkspaceDefaultRoleResolver defaultRoleResolver;
    private final WorkspaceCacheSupport cacheSupport;

    @Override
    public PageResult<WorkspaceListVO> page(WorkspacePageQuery query) {
        Page<Workspace> page = new Page<>(query.getCurrentPage(), query.getPageSize());
        LambdaQueryWrapper<Workspace> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(query.getKeyword())) {
            wrapper.and(w -> w.likeRight(Workspace::getName, query.getKeyword())
                    .or().likeRight(Workspace::getCode, query.getKeyword())
                    .or().likeRight(Workspace::getDescription, query.getKeyword()));
        }
        if (StringUtils.isNotBlank(query.getStatus())) {
            wrapper.eq(Workspace::getStatus, query.getStatus());
        }
        wrapper.orderByDesc(Workspace::getCreatedAt);

        IPage<Workspace> result = workspaceMapper.selectPage(page, wrapper);
        List<Workspace> records = result.getRecords();
        if (records.isEmpty()) {
            return PageResult.from(result, Collections.emptyList());
        }

        // 批量统计有效成员数
        List<String> codes = records.stream().map(Workspace::getCode).toList();
        Map<String, Integer> memberCountMap = workspaceMapper.countEffectiveMembersBatch(codes).stream()
                .collect(Collectors.toMap(WorkspaceMemberCountVO::getWorkspaceCode,
                        WorkspaceMemberCountVO::getMemberCount));

        // 批量取 Owner 姓名
        Set<Long> ownerIds = records.stream().map(Workspace::getOwnerUserId).collect(Collectors.toSet());
        Map<Long, String> ownerNameMap = systemUserMapper.selectByIds(ownerIds).stream()
                .collect(Collectors.toMap(SystemUser::getId, SystemUser::getName));

        List<WorkspaceListVO> list = records.stream()
                .map(ws -> WorkspaceListVO.builder()
                        .id(ws.getId())
                        .name(ws.getName())
                        .code(ws.getCode())
                        .description(ws.getDescription())
                        .ownerUserId(ws.getOwnerUserId())
                        .ownerUserName(ownerNameMap.get(ws.getOwnerUserId()))
                        .status(ws.getStatus())
                        .memberCount(memberCountMap.getOrDefault(ws.getCode(), 0))
                        .assetCount(0)
                        .lastActiveAt(ws.getLastActiveAt())
                        .createdAt(ws.getCreatedAt())
                        .build())
                .toList();
        return PageResult.from(result, list);
    }

    @Override
    public WorkspaceDetailVO detail(Long id) {
        Workspace ws = requireWorkspace(id);

        String ownerUserName = null;
        String ownerUserDeptName = null;
        SystemUser owner = systemUserMapper.selectById(ws.getOwnerUserId());
        if (owner != null) {
            ownerUserName = owner.getName();
            SystemUserDept ownerDept = systemUserDeptMapper.selectByUserId(owner.getId());
            if (ownerDept != null) {
                SystemDept dept = systemDeptMapper.selectById(ownerDept.getDeptId());
                if (dept != null) {
                    ownerUserDeptName = dept.getName();
                }
            }
        }

        List<WorkspaceMemberCountVO> counts =
                workspaceMapper.countEffectiveMembersBatch(List.of(ws.getCode()));
        int memberCount = counts.isEmpty() ? 0 : counts.get(0).getMemberCount();

        return WorkspaceDetailVO.builder()
                .id(ws.getId())
                .name(ws.getName())
                .code(ws.getCode())
                .description(ws.getDescription())
                .ownerUserId(ws.getOwnerUserId())
                .ownerUserName(ownerUserName)
                .ownerUserDeptName(ownerUserDeptName)
                .status(ws.getStatus())
                .memberCount(memberCount)
                .assetCount(0)
                .lastActiveAt(ws.getLastActiveAt())
                .createdAt(ws.getCreatedAt())
                .depts(workspaceDeptMapper.selectDeptAssignments(ws.getCode()))
                .build();
    }

    @Override
    @AuditLog(module = "工作空间管理", operation = "创建空间", type = "CREATE")
    @Transactional
    public Long create(WorkspaceSaveDTO dto) {
        // 保留字校验
        if (WorkspaceConstants.RESERVED_CODE.equals(dto.getCode())) {
            throw businessException(WorkspaceErrorCode.WORKSPACE_CODE_RESERVED);
        }
        // 编码唯一
        if (workspaceMapper.selectByCode(dto.getCode()) != null) {
            throw businessException(WorkspaceErrorCode.WORKSPACE_CODE_EXISTS);
        }
        // 名称唯一
        if (workspaceMapper.selectCount(new LambdaQueryWrapper<Workspace>()
                .eq(Workspace::getName, dto.getName())) > 0) {
            throw businessException(WorkspaceErrorCode.WORKSPACE_NAME_EXISTS);
        }
        // 空间管理员须存在且启用
        SystemUser owner = requireEnabledUser(dto.getOwnerUserId());

        Workspace workspace = new Workspace();
        workspace.setName(dto.getName());
        workspace.setCode(dto.getCode());
        workspace.setDescription(dto.getDescription());
        workspace.setOwnerUserId(dto.getOwnerUserId());
        workspace.setStatus(StringUtils.isNotBlank(dto.getStatus())
                ? dto.getStatus() : WorkspaceConstants.STATUS_ENABLED);
        workspaceMapper.insert(workspace);

        Long currentUserId = SecurityUtils.getCurrentUserId();

        // 空间管理员 DIRECT + OWNER 成员记录（技术设计 §8.1.3）
        insertDirectMember(workspace.getCode(), owner.getId(), WorkspaceRole.OWNER.getCode(), currentUserId);

        // 创建人 DIRECT 成员记录，角色按默认角色判定规则（PRD FR-19/FR-23）
        if (currentUserId != null && !currentUserId.equals(owner.getId())) {
            WorkspaceRole creatorRole = defaultRoleResolver.resolve(currentUserId);
            insertDirectMember(workspace.getCode(), currentUserId, creatorRole.getCode(), currentUserId);
        }

        // 失效概览与相关用户的可访问空间缓存
        cacheSupport.evictOverview();
        cacheSupport.evictAccessible(owner.getId());
        cacheSupport.evictAccessible(currentUserId);

        return workspace.getId();
    }

    @Override
    @AuditLog(module = "工作空间管理", operation = "编辑空间", type = "UPDATE")
    @Transactional
    public void update(WorkspaceSaveDTO dto) {
        Workspace ws = requireWorkspace(dto.getId());

        // 名称唯一（排除自身）
        if (StringUtils.isNotBlank(dto.getName())
                && workspaceMapper.selectCount(new LambdaQueryWrapper<Workspace>()
                        .eq(Workspace::getName, dto.getName())
                        .ne(Workspace::getId, dto.getId())) > 0) {
            throw businessException(WorkspaceErrorCode.WORKSPACE_NAME_EXISTS);
        }

        // 变更空间管理员：旧 Owner 降 MAINTAINER，新 Owner 升 OWNER（FR-18 保证至少一名 Owner）
        boolean ownerChanged = dto.getOwnerUserId() != null && !dto.getOwnerUserId().equals(ws.getOwnerUserId());
        if (ownerChanged) {
            SystemUser newOwner = requireEnabledUser(dto.getOwnerUserId());
            demoteOldOwner(ws.getCode(), ws.getOwnerUserId());
            upsertDirectRole(ws.getCode(), newOwner.getId(), WorkspaceRole.OWNER.getCode(),
                    SecurityUtils.getCurrentUserId());
        }

        LambdaUpdateWrapper<Workspace> update = new LambdaUpdateWrapper<Workspace>()
                .eq(Workspace::getId, dto.getId())
                .set(StringUtils.isNotBlank(dto.getName()), Workspace::getName, dto.getName())
                .set(Workspace::getDescription, dto.getDescription())
                .set(dto.getOwnerUserId() != null, Workspace::getOwnerUserId, dto.getOwnerUserId());
        workspaceMapper.update(null, update);

        // 失效缓存：空间信息 + 概览；换 Owner 时含新旧 Owner 的成员/可访问缓存
        cacheSupport.evictWorkspaceInfo(ws.getCode());
        cacheSupport.evictOverview();
        if (ownerChanged) {
            cacheSupport.evictMember(ws.getCode(), ws.getOwnerUserId());
            cacheSupport.evictMember(ws.getCode(), dto.getOwnerUserId());
        }
    }

    @Override
    @AuditLog(module = "工作空间管理", operation = "启停用空间", type = "UPDATE")
    public void updateStatus(Long id, String status) {
        Workspace ws = requireWorkspace(id);
        Workspace update = new Workspace();
        update.setId(id);
        update.setStatus(status);
        workspaceMapper.updateById(update);

        // 停用后空间级请求被 Filter 拒绝（020401）→ 失效空间信息 / 概览 / 全量可访问缓存
        cacheSupport.evictWorkspaceInfo(ws.getCode());
        cacheSupport.evictOverview();
        cacheSupport.evictAllAccessible();
    }

    @Override
    @AuditLog(module = "工作空间管理", operation = "删除空间", type = "DELETE")
    @Transactional
    public void delete(Long id) {
        Workspace ws = requireWorkspace(id);
        // 已发布资产校验：本期预留扩展点，恒通过（本体模块上线后接入 020105）

        // 逻辑删除空间 + 成员记录 + 部门分配记录（同事务）
        workspaceMapper.deleteById(id);
        workspaceMemberMapper.delete(new LambdaQueryWrapper<WorkspaceMember>()
                .eq(WorkspaceMember::getWorkspaceCode, ws.getCode()));
        workspaceDeptMapper.delete(new LambdaQueryWrapper<WorkspaceDept>()
                .eq(WorkspaceDept::getWorkspaceCode, ws.getCode()));

        // 失效空间信息 / 成员判定 / 概览 / 全量可访问缓存
        cacheSupport.evictWorkspaceInfo(ws.getCode());
        cacheSupport.evictWorkspaceMembers(ws.getCode());
        cacheSupport.evictOverview();
        cacheSupport.evictAllAccessible();
    }

    @Override
    public WorkspaceOverviewVO overview() {
        WorkspaceOverviewVO cached = cacheSupport.getOverview();
        if (cached != null) {
            return cached;
        }

        Long totalSpaces = workspaceMapper.selectCount(null);

        List<String> codes = workspaceMapper.selectList(new LambdaQueryWrapper<Workspace>()
                        .select(Workspace::getCode)).stream()
                .map(Workspace::getCode)
                .toList();
        int totalMembers = codes.isEmpty() ? 0
                : workspaceMapper.countEffectiveMembersBatch(codes).stream()
                        .mapToInt(WorkspaceMemberCountVO::getMemberCount)
                        .sum();

        WorkspaceOverviewVO overview = WorkspaceOverviewVO.builder()
                .totalSpaces(totalSpaces.intValue())
                .totalMembers(totalMembers)
                .totalAssets(0)
                .activeSpaces30d(workspaceMapper.countActiveSpaces30d())
                .build();
        cacheSupport.putOverview(overview);
        return overview;
    }

    @Override
    public List<WorkspaceSimpleVO> accessible(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }

        List<WorkspaceSimpleVO> cached = cacheSupport.getAccessible(userId);
        if (cached != null) {
            return cached;
        }

        List<WorkspaceSimpleVO> result = computeAccessible(userId);
        cacheSupport.putAccessible(userId, result);
        return result;
    }

    private List<WorkspaceSimpleVO> computeAccessible(Long userId) {
        List<SystemRole> roles = systemRoleMapper.selectByUserId(userId);
        boolean superAdmin = SystemConstants.ADMIN_USER_ID.equals(userId);
        Set<String> customCodes = new LinkedHashSet<>();
        if (roles != null) {
            for (SystemRole role : roles) {
                if (!"ENABLED".equals(role.getStatus())) {
                    continue;
                }
                if (SystemConstants.ROLE_SUPER_ADMIN.equals(role.getCode())) {
                    superAdmin = true;
                }
                if (DataScope.ALL.getValue().equals(role.getDataScope())) {
                    superAdmin = true;
                }
                if (DataScope.CUSTOM.getValue().equals(role.getDataScope())
                        && role.getCustomWorkspaceCodes() != null) {
                    customCodes.addAll(role.getCustomWorkspaceCodes());
                }
            }
        }

        LambdaQueryWrapper<Workspace> wrapper = new LambdaQueryWrapper<Workspace>()
                .eq(Workspace::getStatus, WorkspaceConstants.STATUS_ENABLED)
                .orderByAsc(Workspace::getName);
        if (!superAdmin) {
            Set<String> codes = new LinkedHashSet<>(customCodes);
            codes.addAll(workspaceMemberMapper.selectMemberWorkspaceCodes(userId));
            if (codes.isEmpty()) {
                return Collections.emptyList();
            }
            wrapper.in(Workspace::getCode, codes);
        }

        List<Workspace> workspaces = workspaceMapper.selectList(wrapper);
        List<WorkspaceSimpleVO> list = new ArrayList<>(workspaces.size());
        for (Workspace ws : workspaces) {
            list.add(WorkspaceSimpleVO.builder()
                    .code(ws.getCode())
                    .name(ws.getName())
                    .workspaceRole(workspaceMemberMapper.selectEffectiveRole(ws.getCode(), userId))
                    .build());
        }
        return list;
    }

    @Override
    public boolean isCodeAvailable(String code) {
        if (StringUtils.isBlank(code) || WorkspaceConstants.RESERVED_CODE.equals(code)) {
            return false;
        }
        return workspaceMapper.selectByCode(code) == null;
    }

    @Override
    public Workspace getByCode(String code) {
        Workspace ws = workspaceMapper.selectByCode(code);
        if (ws == null) {
            throw businessException(WorkspaceErrorCode.WORKSPACE_NOT_FOUND);
        }
        return ws;
    }

    // ===== 私有方法 =====

    private Workspace requireWorkspace(Long id) {
        Workspace ws = id == null ? null : workspaceMapper.selectById(id);
        if (ws == null) {
            throw businessException(WorkspaceErrorCode.WORKSPACE_NOT_FOUND);
        }
        return ws;
    }

    private SystemUser requireEnabledUser(Long userId) {
        SystemUser user = userId == null ? null : systemUserMapper.selectById(userId);
        if (user == null || !"ENABLED".equals(user.getStatus())) {
            throw new BusinessException("999904", "空间管理员不存在或已停用");
        }
        return user;
    }

    private void insertDirectMember(String workspaceCode, Long userId, String role, Long operatorId) {
        WorkspaceMember member = new WorkspaceMember();
        member.setWorkspaceCode(workspaceCode);
        member.setUserId(userId);
        member.setWorkspaceRole(role);
        member.setSource(MemberSource.DIRECT.getValue());
        member.setCreatedBy(operatorId);
        member.setUpdatedBy(operatorId);
        workspaceMemberMapper.insert(member);
    }

    /**
     * 旧 Owner 的 DIRECT 记录降级为 MAINTAINER（无 DIRECT 记录则不处理，其部门来源角色不受换 Owner 影响）
     */
    private void demoteOldOwner(String workspaceCode, Long oldOwnerId) {
        WorkspaceMember direct = workspaceMemberMapper.selectOne(new LambdaQueryWrapper<WorkspaceMember>()
                .eq(WorkspaceMember::getWorkspaceCode, workspaceCode)
                .eq(WorkspaceMember::getUserId, oldOwnerId)
                .eq(WorkspaceMember::getSource, MemberSource.DIRECT.getValue())
                .last("LIMIT 1"));
        if (direct != null && WorkspaceRole.OWNER.getCode().equals(direct.getWorkspaceRole())) {
            WorkspaceMember update = new WorkspaceMember();
            update.setId(direct.getId());
            update.setWorkspaceRole(WorkspaceRole.MAINTAINER.getCode());
            workspaceMemberMapper.updateById(update);
        }
    }

    /**
     * 已有 DIRECT 记录则改写角色，否则插入
     */
    private void upsertDirectRole(String workspaceCode, Long userId, String role, Long operatorId) {
        WorkspaceMember direct = workspaceMemberMapper.selectOne(new LambdaQueryWrapper<WorkspaceMember>()
                .eq(WorkspaceMember::getWorkspaceCode, workspaceCode)
                .eq(WorkspaceMember::getUserId, userId)
                .eq(WorkspaceMember::getSource, MemberSource.DIRECT.getValue())
                .last("LIMIT 1"));
        if (direct != null) {
            WorkspaceMember update = new WorkspaceMember();
            update.setId(direct.getId());
            update.setWorkspaceRole(role);
            update.setUpdatedBy(operatorId);
            workspaceMemberMapper.updateById(update);
        }
        else {
            insertDirectMember(workspaceCode, userId, role, operatorId);
        }
    }

    private BusinessException businessException(WorkspaceErrorCode errorCode) {
        return new BusinessException(errorCode.getCode(), errorCode.getMessage());
    }
}
