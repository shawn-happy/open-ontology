package com.prism.platform.security.annotation;

import com.prism.common.enums.WorkspaceRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 空间角色鉴权注解，标注在空间级 Controller 方法上
 * <p>
 * value 为所需最低内置空间角色（按 level 比较，自定义空间角色视为 DEVELOPER 等级）。
 * 由 WorkspaceRoleAspect 拦截校验：内置超管 / super_admin 角色放行；
 * 其余取当前空间上下文中用户的有效角色，等级不足抛 020403。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireWorkspaceRole {

    /**
     * 所需最低空间角色
     */
    WorkspaceRole value();
}
