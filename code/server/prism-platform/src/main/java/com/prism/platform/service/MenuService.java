package com.prism.platform.service;

import com.prism.platform.dto.CreateMenuDTO;
import com.prism.platform.dto.UpdateMenuDTO;
import com.prism.platform.vo.MenuTreeVO;

import java.util.List;

/**
 * 菜单权限服务
 */
public interface MenuService {

    void create(CreateMenuDTO dto);

    void update(UpdateMenuDTO dto);

    void delete(Long id);

    List<MenuTreeVO> tree();

    void enableDisable(Long id, String status);

    /**
     * 根据用户 ID 获取权限标识列表
     */
    List<String> getPermissionsByUserId(Long userId);
}
