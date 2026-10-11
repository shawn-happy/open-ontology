package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 登录响应 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "登录响应")
public class LoginVO {

    @Schema(description = "Access Token")
    private String accessToken;

    @Schema(description = "Refresh Token")
    private String refreshToken;

    @Schema(description = "是否首次登录（须强制修改密码）")
    private Boolean firstLogin;

    @Schema(description = "密码是否已过期")
    private Boolean passwordExpired;

    @Schema(description = "用户信息")
    private UserInfoVO userInfo;

    @Schema(description = "权限标识列表")
    private List<String> permissions;
}
