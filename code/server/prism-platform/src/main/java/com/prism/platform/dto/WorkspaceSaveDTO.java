package com.prism.platform.dto;

import com.prism.platform.constants.WorkspaceConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建/编辑工作空间 DTO（校验分组区分创建与编辑：创建必填 code，编辑必填 id 且 code 不可修改）
 */
@Data
@Schema(description = "创建/编辑工作空间请求")
public class WorkspaceSaveDTO {

    /**
     * 创建场景校验分组
     */
    public interface Create {
    }

    /**
     * 编辑场景校验分组
     */
    public interface Update {
    }

    @NotNull(message = "ID 不能为空", groups = Update.class)
    @Schema(description = "空间 ID（编辑时必填）")
    private Long id;

    @NotBlank(message = "空间名称不能为空")
    @Size(max = 128, message = "空间名称长度不能超过 128")
    @Schema(description = "空间名称（应用层唯一）")
    private String name;

    @NotBlank(message = "空间编码不能为空", groups = Create.class)
    @Pattern(regexp = WorkspaceConstants.CODE_PATTERN, groups = Create.class,
            message = "空间编码须为大写字母开头，仅含大写字母/数字/中划线/下划线，长度 3-64")
    @Schema(description = "空间编码（创建时必填且全平台唯一，创建后不可修改）")
    private String code;

    @Size(max = 512, message = "空间描述长度不能超过 512")
    @Schema(description = "空间描述")
    private String description;

    @NotNull(message = "空间管理员不能为空")
    @Schema(description = "空间管理员用户 ID（须存在且启用；变更时旧 Owner 降级为 Maintainer，新 Owner 升为 Owner）")
    private Long ownerUserId;

    @Pattern(regexp = "^(ENABLED|DISABLED)$", message = "状态仅允许 ENABLED 或 DISABLED")
    @Schema(description = "状态：ENABLED/DISABLED，默认 ENABLED（仅创建时生效）")
    private String status;
}
