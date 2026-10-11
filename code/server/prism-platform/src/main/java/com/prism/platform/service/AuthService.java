package com.prism.platform.service;

import com.prism.platform.dto.ChangePasswordDTO;
import com.prism.platform.dto.ForgotPasswordDTO;
import com.prism.platform.dto.LoginDTO;
import com.prism.platform.dto.RefreshTokenDTO;
import com.prism.platform.vo.LoginVO;

/**
 * 认证服务
 */
public interface AuthService {

    /**
     * 登录
     */
    LoginVO login(LoginDTO dto);

    /**
     * 登出
     */
    void logout(String accessToken);

    /**
     * 刷新 Token
     */
    String refreshToken(RefreshTokenDTO dto);

    /**
     * 忘记密码（重置为初始密码）
     */
    void forgotPassword(ForgotPasswordDTO dto);

    /**
     * 首次登录强制修改密码
     */
    void changePasswordFirstTime(ChangePasswordDTO dto);
}
