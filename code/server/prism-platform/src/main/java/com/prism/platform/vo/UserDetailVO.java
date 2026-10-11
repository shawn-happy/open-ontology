package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户详情 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "用户详情")
public class UserDetailVO {

    @Schema(description = "用户 ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像 URL")
    private String avatar;

    @Schema(description = "状态：ENABLED/DISABLED")
    private String status;

    @Schema(description = "部门 ID")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "角色 ID 列表")
    private List<Long> roleIds;

    @Schema(description = "角色名称列表")
    private List<String> roleNames;

    @Schema(description = "最近登录时间")
    private LocalDateTime lastLoginAt;

    @Schema(description = "最近登录 IP")
    private String lastLoginIp;

    @Schema(description = "密码最后修改时间")
    private LocalDateTime pwdUpdateTime;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
