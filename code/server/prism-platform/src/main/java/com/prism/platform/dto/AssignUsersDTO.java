package com.prism.platform.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AssignUsersDTO {
    @NotNull(message = "roleId/deptId不能为空")
    private Long targetId;

    @NotNull(message = "userIds不能为空")
    private List<Long> userIds;
}
