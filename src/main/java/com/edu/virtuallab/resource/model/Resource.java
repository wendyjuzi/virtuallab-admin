package com.edu.virtuallab.resource.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("resource")
public class Resource {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    private String type;
    private String category;
    private String url;
    private String status;
    private Long fileSize;
    private String fileType;
    private String uploader;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    // 资源类型枚举
    public static final String TYPE_DOCUMENT = "document";
    public static final String TYPE_VIDEO = "video";
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_AUDIO = "audio";
    public static final String TYPE_OTHER = "other";
    
    // 状态枚举
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_DELETED = "deleted";
    
    // 分类枚举
    public static final String CATEGORY_TEACHING = "teaching";
    public static final String CATEGORY_EXPERIMENT = "experiment";
    public static final String CATEGORY_REFERENCE = "reference";
    public static final String CATEGORY_TOOL = "tool";
}