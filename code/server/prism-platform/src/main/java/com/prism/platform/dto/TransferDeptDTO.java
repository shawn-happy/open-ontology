package com.prism.platform.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransferDeptDTO {
    @NotNull(message = "sourceDeptId不能为空")
    private Long sourceDeptId;

    @NotNull(message = "targetDeptId不能为空")
    private Long targetDeptId;
}
