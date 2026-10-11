package com.prism.platform.dto;

import com.prism.common.result.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色分页查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色分页查询条件")
public class RolePageQuery extends PageQuery {
}
