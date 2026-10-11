package com.prism.common.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登录日志注解，标注在登录/登出方法上
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginLog {

    /**
     * 登录描述
     */
    String value() default "";
}
