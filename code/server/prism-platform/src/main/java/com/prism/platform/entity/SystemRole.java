package com.prism.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.prism.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 角色实体
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "system_role", autoResultMap = true)
public class SystemRole extends BaseEntity {

    @TableField("name")
    private String name;

    @TableField("code")
    private String code;

    @TableField("type")
    private String type;

    @TableField("data_scope")
    private String dataScope;

    @TableField(value = "custom_workspace_codes", typeHandler = JacksonTypeHandler.class)
    private List<String> customWorkspaceCodes;

    @TableField("status")
    private String status;

    @TableField("is_builtin")
    private Boolean builtin;

    @TableField("remark")
    private String remark;
}
