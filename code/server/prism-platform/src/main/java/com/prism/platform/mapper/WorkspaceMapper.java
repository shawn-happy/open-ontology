package com.prism.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prism.platform.entity.Workspace;
import com.prism.platform.vo.WorkspaceMemberCountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工作空间 Mapper
 */
@Mapper
public interface WorkspaceMapper extends BaseMapper<Workspace> {

    Workspace selectByCode(@Param("code") String code);

    /**
     * 批量统计各空间有效成员数（动态展开去重口径）
     */
    List<WorkspaceMemberCountVO> countEffectiveMembersBatch(@Param("codes") List<String> codes);

    /**
     * 统计活跃空间数（30 天内有活跃记录的启用空间）
     */
    int countActiveSpaces30d();
}
