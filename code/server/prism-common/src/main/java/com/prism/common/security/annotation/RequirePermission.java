package com.prism.common.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限控制注解，标注在 Controller 方法上
 * value 为权限标识，如 "system:user:create"
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {

    /**
     * 权限标识（如 system:user:create）
     */
    String value();
}
