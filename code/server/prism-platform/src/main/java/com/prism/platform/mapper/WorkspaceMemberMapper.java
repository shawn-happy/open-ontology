package com.prism.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.prism.platform.dto.MemberPageQuery;
import com.prism.platform.entity.WorkspaceMember;
import com.prism.platform.vo.MemberListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 空间成员 Mapper
 */
@Mapper
public interface WorkspaceMemberMapper extends BaseMapper<WorkspaceMember> {

    /**
     * 有效成员分页（动态展开去重：DIRECT 优先，部门展开 + PERSONAL 覆盖，排除 EXCLUDED）
     */
    IPage<MemberListVO> pageEffectiveMembers(IPage<MemberListVO> page,
            @Param("workspaceCode") String workspaceCode,
            @Param("query") MemberPageQuery query);

    /**
     * pageEffectiveMembers 显式 count（PaginationInnerInterceptor 约定）
     */
    Long pageEffectiveMembers_mpCount(IPage<MemberListVO> page,
            @Param("workspaceCode") String workspaceCode,
            @Param("query") MemberPageQuery query);

    /**
     * 有效成员数统计（source/role 可选过滤，null 表示不限）
     */
    int countEffectiveMembers(@Param("workspaceCode") String workspaceCode,
            @Param("source") String source,
            @Param("workspaceRole") String workspaceRole);

    /**
     * 查询用户在空间内的有效角色（DIRECT 优先，其次部门展开 + PERSONAL 覆盖），非成员返回 null
     */
    String selectEffectiveRole(@Param("workspaceCode") String workspaceCode, @Param("userId") Long userId);

    /**
     * 锁定空间全部有效 Owner 记录（FOR UPDATE，用于唯一 Owner 保护，须在事务内调用）
     */
    List<WorkspaceMember> selectOwnersForUpdate(@Param("workspaceCode") String workspaceCode);

    /**
     * 批量插入成员记录
     */
    int batchInsert(@Param("list") List<WorkspaceMember> list);

    /**
     * 查询用户作为有效成员（直接分配或部门展开）的空间编码集合（用于可访问空间列表）
     */
    List<String> selectMemberWorkspaceCodes(@Param("userId") Long userId);
}
