package com.prism.platform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户基本信息 VO（登录后返回）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "用户基本信息")
public class UserInfoVO {

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

    @Schema(description = "角色名称列表")
    private List<String> roleNames;

    @Schema(description = "部门名称")
    private String deptName;
}
