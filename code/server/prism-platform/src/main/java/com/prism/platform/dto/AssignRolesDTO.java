package com.prism.platform.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AssignRolesDTO {
    @NotNull(message = "userId不能为空")
    private Long userId;

    @NotNull(message = "roleIds不能为空")
    private List<Long> roleIds;
}
