package com.prism.platform.service;

import com.prism.common.result.PageResult;
import com.prism.platform.dto.CreateUserDTO;
import com.prism.platform.dto.UpdateUserDTO;
import com.prism.platform.dto.UserPageQuery;
import com.prism.platform.vo.ImportResultVO;
import com.prism.platform.vo.UserDetailVO;
import com.prism.platform.vo.UserListVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户管理服务
 */
public interface UserService {

    void create(CreateUserDTO dto);

    void update(UpdateUserDTO dto);

    void delete(Long id);

    UserDetailVO getById(Long id);

    PageResult<UserListVO> page(UserPageQuery query);

    void enableDisable(Long id, String status);

    void resetPassword(Long id);

    void assignRoles(Long userId, List<Long> roleIds);

    void assignDept(Long userId, Long deptId);

    ImportResultVO importUsers(MultipartFile file);

    List<UserListVO> exportUsers(UserPageQuery query);
}
