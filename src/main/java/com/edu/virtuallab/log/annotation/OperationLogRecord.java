package com.edu.virtuallab.log.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLogRecord {
    String operation(); // 操作名称
    String permissionCode(); // 权限码
    String module(); // 模块
    String action(); // 动作
    String description() default ""; // 描述
} 