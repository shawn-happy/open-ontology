package com.prism.platform.service;

import com.prism.platform.dto.ChangePasswordDTO;
import com.prism.platform.dto.UpdateProfileDTO;
import com.prism.platform.vo.UserProfileVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 个人信息服务
 */
public interface ProfileService {

    UserProfileVO getProfile();

    void updateProfile(UpdateProfileDTO dto);

    void changePassword(ChangePasswordDTO dto);

    String uploadAvatar(MultipartFile file);
}
