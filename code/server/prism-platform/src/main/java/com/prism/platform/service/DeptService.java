package com.prism.platform.service;

import com.prism.platform.dto.CreateDeptDTO;
import com.prism.platform.dto.DeptTreeQuery;
import com.prism.platform.dto.UpdateDeptDTO;
import com.prism.platform.vo.DeptTreeVO;

import java.util.List;

/**
 * 部门管理服务
 */
public interface DeptService {

    void create(CreateDeptDTO dto);

    void update(UpdateDeptDTO dto);

    void delete(Long id);

    List<DeptTreeVO> tree(DeptTreeQuery query);

    void enableDisable(Long id, String status);

    void assignUsers(Long deptId, List<Long> userIds);

    void setLeader(Long deptId, Long leaderId);

    void transfer(Long sourceDeptId, Long targetDeptId);

    void updateSort(Long id, Integer sortOrder);
}
