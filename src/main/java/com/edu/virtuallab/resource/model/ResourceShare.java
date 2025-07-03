package com.edu.virtuallab.resource.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("resource_share")
public class ResourceShare {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long resourceId;
    private String sharedBy;
    private String sharedWith;
    private String permission; // read, write, admin
    private String status; // active, inactive, expired
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    private LocalDateTime expiresAt;
    
    // 权限枚举
    public static final String PERMISSION_READ = "read";
    public static final String PERMISSION_WRITE = "write";
    public static final String PERMISSION_ADMIN = "admin";
    
    // 状态枚举
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_EXPIRED = "expired";
}