package com.prism.platform.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignDeptDTO {
    @NotNull(message = "userId不能为空")
    private Long userId;

    @NotNull(message = "deptId不能为空")
    private Long deptId;
}
