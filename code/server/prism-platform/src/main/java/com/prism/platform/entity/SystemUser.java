package com.prism.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.prism.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("system_user")
public class SystemUser extends BaseEntity {

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("name")
    private String name;

    @TableField("phone")
    private String phone;

    @TableField("email")
    private String email;

    @TableField("avatar")
    private String avatar;

    @TableField("status")
    private String status;

    @TableField("pwd_update_time")
    private LocalDateTime pwdUpdateTime;

    @TableField("login_fail_count")
    private Integer loginFailCount;

    @TableField("lock_until")
    private LocalDateTime lockUntil;

    @TableField("last_login_at")
    private LocalDateTime lastLoginAt;

    @TableField("last_login_ip")
    private String lastLoginIp;
}
