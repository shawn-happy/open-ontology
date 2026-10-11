package com.prism.common.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 审计日志注解，标注在 Service 方法上
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLog {

    /**
     * 模块名（如"用户管理"）
     */
    String module();

    /**
     * 操作内容（如"新增用户"）
     */
    String operation();

    /**
     * 操作类型（CREATE/UPDATE/DELETE/ENABLE/DISABLE 等）
     */
    String type();
}
