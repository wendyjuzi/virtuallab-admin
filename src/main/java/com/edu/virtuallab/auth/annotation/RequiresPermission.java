package com.edu.virtuallab.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注解，用于标记需要特定权限的方法
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {
    
    /**
     * 权限编码
     */
    String value();
    
    /**
     * 权限描述
     */
    String description() default "";
    
    /**
     * 模块名称
     */
    String module() default "";
    
    /**
     * 操作类型
     */
    String action() default "";
    
    /**
     * 是否记录日志
     */
    boolean log() default true;
} 