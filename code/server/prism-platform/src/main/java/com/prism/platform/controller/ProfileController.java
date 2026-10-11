package com.prism.platform.controller;

import com.prism.common.result.Result;
import com.prism.platform.dto.ChangePasswordDTO;
import com.prism.platform.dto.UpdateProfileDTO;
import com.prism.platform.service.ProfileService;
import com.prism.platform.vo.UserProfileVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 个人信息控制器
 */
@Tag(name = "个人中心")
@RestController
@RequestMapping("/web/platform")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @Operation(summary = "获取个人信息")
    @GetMapping("/system/profile")
    public Result<UserProfileVO> getProfile() {
        UserProfileVO vo = profileService.getProfile();
        return Result.ok(vo);
    }

    @Operation(summary = "修改个人信息")
    @PostMapping("/system/profile/update")
    public Result<Void> updateProfile(@Valid @RequestBody UpdateProfileDTO dto) {
        profileService.updateProfile(dto);
        return Result.ok();
    }

    @Operation(summary = "修改密码")
    @PostMapping("/system/profile/change-password")
    public Result<Void> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        profileService.changePassword(dto);
        return Result.ok();
    }

    @Operation(summary = "上传头像")
    @PostMapping("/system/profile/upload-avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        String url = profileService.uploadAvatar(file);
        return Result.ok(url);
    }
}
