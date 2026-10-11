package com.prism.platform.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateSortDTO {
    @NotNull(message = "id不能为空")
    private Long id;

    @NotNull(message = "sortOrder不能为空")
    private Integer sortOrder;
}
