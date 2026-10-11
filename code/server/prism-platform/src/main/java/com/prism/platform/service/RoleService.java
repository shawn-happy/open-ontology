package com.prism.platform.service;

import com.prism.common.result.PageResult;
import com.prism.platform.dto.CreateRoleDTO;
import com.prism.platform.dto.RolePageQuery;
import com.prism.platform.dto.RolePermissionDTO;
import com.prism.platform.dto.UpdateRoleDTO;
import com.prism.platform.vo.RoleDetailVO;
import com.prism.platform.vo.RoleListVO;

import java.util.List;

/**
 * 角色管理服务
 */
public interface RoleService {

    void create(CreateRoleDTO dto);

    void update(UpdateRoleDTO dto);

    void delete(Long id);

    RoleDetailVO getById(Long id);

    PageResult<RoleListVO> page(RolePageQuery query);

    void enableDisable(Long id, String status);

    void configurePermissions(RolePermissionDTO dto);

    void assignUsers(Long roleId, List<Long> userIds);

    List<RoleListVO> listAll(String keyword);
}
