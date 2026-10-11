package com.prism.platform.service.impl;

import com.prism.common.exception.BusinessException;
import com.prism.common.security.SecurityUtils;
import com.prism.platform.dto.ChangePasswordDTO;
import com.prism.platform.dto.UpdateProfileDTO;
import com.prism.platform.entity.SystemDept;
import com.prism.platform.entity.SystemRole;
import com.prism.platform.entity.SystemUser;
import com.prism.platform.entity.SystemUserDept;
import com.prism.platform.mapper.SystemDeptMapper;
import com.prism.platform.mapper.SystemRoleMapper;
import com.prism.platform.mapper.SystemUserDeptMapper;
import com.prism.platform.mapper.SystemUserMapper;
import com.prism.platform.service.ProfileService;
import com.prism.platform.vo.UserProfileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 个人信息服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private static final Pattern PWD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,20}$");

    private final SystemUserMapper systemUserMapper;
    private final SystemRoleMapper systemRoleMapper;
    private final SystemUserDeptMapper systemUserDeptMapper;
    private final SystemDeptMapper systemDeptMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserProfileVO getProfile() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException("999902", "未认证");
        }

        SystemUser user = systemUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("999904", "用户不存在");
        }

        List<SystemRole> roles = systemRoleMapper.selectByUserId(userId);
        List<String> roleNames = roles.stream().map(SystemRole::getName).collect(Collectors.toList());

        SystemUserDept userDept = systemUserDeptMapper.selectByUserId(userId);
        String deptName = null;
        if (userDept != null) {
            SystemDept dept = systemDeptMapper.selectById(userDept.getDeptId());
            if (dept != null) {
                deptName = dept.getName();
            }
        }

        return UserProfileVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .status(user.getStatus())
                .roleNames(roleNames)
                .deptName(deptName)
                .lastLoginAt(user.getLastLoginAt())
                .pwdUpdateTime(user.getPwdUpdateTime())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Override
    public void updateProfile(UpdateProfileDTO dto) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException("999902", "未认证");
        }

        SystemUser update = new SystemUser();
        update.setId(userId);
        update.setPhone(dto.getPhone());
        update.setEmail(dto.getEmail());
        systemUserMapper.updateById(update);
    }

    @Override
    public void changePassword(ChangePasswordDTO dto) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException("999902", "未认证");
        }

        // 验证两次输入一致
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException("999901", "两次输入的密码不一致");
        }

        // 验证密码复杂度
        if (!PWD_PATTERN.matcher(dto.getNewPassword()).matches()) {
            throw new BusinessException("999901", "密码须为 8-20 位，含大小写字母、数字和特殊字符");
        }

        SystemUser user = systemUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("999904", "用户不存在");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BusinessException("010101", "旧密码错误");
        }

        SystemUser update = new SystemUser();
        update.setId(userId);
        update.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        update.setPwdUpdateTime(LocalDateTime.now());
        systemUserMapper.updateById(update);
    }

    @Override
    public String uploadAvatar(MultipartFile file) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException("999902", "未认证");
        }

        // 简单处理：实际项目中应上传到 MinIO 等对象存储
        // 此处返回一个占位 URL
        String avatarUrl = "/avatars/" + userId + "_" + file.getOriginalFilename();

        SystemUser update = new SystemUser();
        update.setId(userId);
        update.setAvatar(avatarUrl);
        systemUserMapper.updateById(update);

        return avatarUrl;
    }
}
