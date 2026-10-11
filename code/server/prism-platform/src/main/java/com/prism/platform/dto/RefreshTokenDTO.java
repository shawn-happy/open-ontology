package com.prism.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 刷新 Token DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "刷新 Token 请求")
public class RefreshTokenDTO {

    @NotBlank(message = "Refresh Token 不能为空")
    @Schema(description = "Refresh Token")
    private String refreshToken;
}
