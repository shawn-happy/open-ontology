package com.prism.platform.service;

import com.prism.common.result.PageResult;
import com.prism.platform.dto.MemberPageQuery;
import com.prism.platform.dto.MemberRemoveDTO;
import com.prism.platform.dto.MemberRoleDTO;
import com.prism.platform.dto.WorkspaceAssignDTO;
import com.prism.platform.dto.WorkspaceRemoveDeptDTO;
import com.prism.platform.vo.AssignResultVO;
import com.prism.platform.vo.MemberListVO;
import com.prism.platform.vo.MemberStatsVO;
import com.prism.platform.vo.WorkspaceRoleVO;

import java.util.List;

/**
 * 空间成员分配服务
 */
public interface WorkspaceMemberService {

    /**
     * 平台级：批量分配用户（幂等；EXCLUDED 记录改写为 DIRECT）
     */
    AssignResultVO assignUsers(Long workspaceId, WorkspaceAssignDTO dto);

    /**
     * 平台级：批量分配部门（幂等；成员动态展开不落库）
     */
    AssignResultVO assignDepts(Long workspaceId, WorkspaceAssignDTO dto);

    /**
     * 平台级：移除分配部门（逻辑删除 + 清理不再被覆盖的 PERSONAL/EXCLUDED 记录）
     */
    void removeDept(Long workspaceId, WorkspaceRemoveDeptDTO dto);

    /**
     * 分配记录分页（动态展开去重，直接分配优先）
     */
    PageResult<MemberListVO> pageMembers(String workspaceCode, MemberPageQuery query);

    /**
     * 成员统计（总数 / 直接分配 / 部门分配 / Owner）
     */
    MemberStatsVO getMemberStats(String workspaceCode);

    /**
     * 空间角色列表（内置五级 + 绑定该空间的自定义角色）
     */
    List<WorkspaceRoleVO> listRoles(String workspaceCode);

    /**
     * 分配 / 覆盖成员角色（workspaceRole 传空 = 清除部门来源成员的个人覆盖）
     */
    void updateMemberRole(String workspaceCode, MemberRoleDTO dto);

    /**
     * 移除成员（PRD 流程 C 三分支语义）
     */
    void removeMember(String workspaceCode, MemberRemoveDTO dto);

    /**
     * 获取用户在空间内的有效角色（Redis 缓存 10min），非成员返回 null
     */
    String getEffectiveRole(String workspaceCode, Long userId);
}
