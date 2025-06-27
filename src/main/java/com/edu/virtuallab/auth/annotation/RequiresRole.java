package com.edu.virtuallab.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 角色注解，用于标记需要特定角色的方法
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {
    
    /**
     * 角色编码
     */
    String value();
    
    /**
     * 角色描述
     */
    String description() default "";
    
    /**
     * 是否记录日志
     */
    boolean log() default true;
} 