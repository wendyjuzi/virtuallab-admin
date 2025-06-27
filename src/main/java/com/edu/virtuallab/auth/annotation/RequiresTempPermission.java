package com.edu.virtuallab.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 临时权限验证注解
 * 用于标记需要临时权限验证的方法
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresTempPermission {
    
    /**
     * 权限ID
     * @return 权限ID
     */
    long permissionId() default 0;
    
    /**
     * 权限名称
     * @return 权限名称
     */
    String permissionName() default "";
    
    /**
     * 角色ID
     * @return 角色ID
     */
    long roleId() default 0;
    
    /**
     * 角色名称
     * @return 角色名称
     */
    String roleName() default "";
    
    /**
     * 是否允许永久权限
     * @return 是否允许永久权限
     */
    boolean allowPermanent() default true;
    
    /**
     * 错误消息
     * @return 错误消息
     */
    String message() default "没有临时权限访问此功能";
} 