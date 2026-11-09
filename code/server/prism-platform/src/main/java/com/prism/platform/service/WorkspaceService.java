package com.prism.platform.service;

import com.prism.common.result.PageResult;
import com.prism.platform.dto.WorkspacePageQuery;
import com.prism.platform.dto.WorkspaceSaveDTO;
import com.prism.platform.entity.Workspace;
import com.prism.platform.vo.WorkspaceDetailVO;
import com.prism.platform.vo.WorkspaceListVO;
import com.prism.platform.vo.WorkspaceOverviewVO;
import com.prism.platform.vo.WorkspaceSimpleVO;

import java.util.List;

/**
 * 工作空间管理服务（平台级）
 */
public interface WorkspaceService {

    /**
     * 空间列表分页（keyword 前缀模糊匹配名称/编码/描述）
     */
    PageResult<WorkspaceListVO> page(WorkspacePageQuery query);

    /**
     * 空间详情（含部门分配列表与统计）
     */
    WorkspaceDetailVO detail(Long id);

    /**
     * 创建空间（同事务写入 Owner 与创建人 DIRECT 成员记录，FR-19/FR-23）
     *
     * @return 新空间 ID
     */
    Long create(WorkspaceSaveDTO dto);

    /**
     * 编辑空间（换 Owner 时旧 Owner 降 MAINTAINER、新 Owner 升 OWNER）
     */
    void update(WorkspaceSaveDTO dto);

    /**
     * 启用/停用空间
     */
    void updateStatus(Long id, String status);

    /**
     * 删除空间（逻辑删除空间 + 成员 + 部门分配记录，同事务）
     */
    void delete(Long id);

    /**
     * 平台概览统计
     */
    WorkspaceOverviewVO overview();

    /**
     * 当前用户可访问空间列表（成员空间 ∪ dataScope=ALL ∪ dataScope=CUSTOM，过滤停用，按名称排序）
     */
    List<WorkspaceSimpleVO> accessible(Long userId);

    /**
     * 空间编码是否可用（true=可用）
     */
    boolean isCodeAvailable(String code);

    /**
     * 按编码获取空间（不存在 → 020103）
     */
    Workspace getByCode(String code);
}
