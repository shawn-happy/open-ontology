package com.prism.platform.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SetLeaderDTO {
    @NotNull(message = "deptId不能为空")
    private Long deptId;

    @NotNull(message = "leaderId不能为空")
    private Long leaderId;
}
