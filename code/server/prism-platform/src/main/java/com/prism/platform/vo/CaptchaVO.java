package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证码响应 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "验证码响应")
public class CaptchaVO {

    @Schema(description = "验证码 Key（用于后续校验）")
    private String captchaKey;

    @Schema(description = "验证码图片 Base64")
    private String captchaImage;
}
