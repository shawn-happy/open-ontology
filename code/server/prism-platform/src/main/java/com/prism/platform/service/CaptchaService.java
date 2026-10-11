package com.prism.platform.service;

import com.prism.platform.vo.CaptchaVO;

/**
 * 验证码服务
 */
public interface CaptchaService {

    /**
     * 生成验证码
     */
    CaptchaVO generateCaptcha();

    /**
     * 校验验证码
     */
    boolean verifyCaptcha(String key, String code);
}
