package com.prism.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.prism.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户部门关联实体
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("system_user_dept")
public class SystemUserDept extends BaseEntity {

    @TableField("user_id")
    private Long userId;

    @TableField("dept_id")
    private Long deptId;
}
