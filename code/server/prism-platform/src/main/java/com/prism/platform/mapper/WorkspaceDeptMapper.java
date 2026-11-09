package com.prism.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prism.platform.entity.WorkspaceDept;
import com.prism.platform.vo.WorkspaceDeptVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 空间部门分配 Mapper
 */
@Mapper
public interface WorkspaceDeptMapper extends BaseMapper<WorkspaceDept> {

    /**
     * 查询空间的部门分配列表（含部门名称与分配人姓名）
     */
    List<WorkspaceDeptVO> selectDeptAssignments(@Param("workspaceCode") String workspaceCode);

    /**
     * 批量插入部门分配记录
     */
    int batchInsert(@Param("list") List<WorkspaceDept> list);

    /**
     * 查询覆盖指定用户的分配部门 ID 列表（部门子树展开命中用户所在部门）
     *
     * @param excludeDeptId 排除的部门 ID（可空；移除部门时排除自身）
     */
    List<Long> selectCoveringDeptIds(@Param("workspaceCode") String workspaceCode,
            @Param("userId") Long userId,
            @Param("excludeDeptId") Long excludeDeptId);
}
