package com.prism.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 移除空间成员 DTO
 */
@Data
@Schema(description = "移除空间成员请求")
public class MemberRemoveDTO {

    @NotNull(message = "用户 ID 不能为空")
    @Schema(description = "目标用户 ID")
    private Long userId;
}
