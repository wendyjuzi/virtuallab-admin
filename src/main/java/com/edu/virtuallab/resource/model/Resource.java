package com.edu.virtuallab.resource.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("experiment_project")
public class Resource {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    private String category;
    private String description;
    private String level;
    private String imageUrl;
    private String videoUrl;
    private String auditStatus;
    private String auditComment;
    private Long auditorId;
    private LocalDateTime auditTime;
    private String publishStatus;
    private LocalDateTime publishTime;
    private String createdBy;
    private String collaborationType;
    private String principle;
    private String purpose;
    private String method;
    private String steps;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    // 为了兼容原有代码，添加type字段的getter和setter
    public String getType() {
        return level; // 使用level作为type
    }
    
    public void setType(String type) {
        this.level = type;
    }
    
    public String getStatus() {
        return publishStatus; // 使用publishStatus作为status
    }
    
    public void setStatus(String status) {
        this.publishStatus = status;
    }
    
    public String getUploader() {
        return createdBy; // 使用createdBy作为uploader
    }
    
    public void setUploader(String uploader) {
        this.createdBy = uploader;
    }
    
    // 资源类型枚举（对应level字段）
    public static final String TYPE_DOCUMENT = "document";
    public static final String TYPE_VIDEO = "video";
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_AUDIO = "audio";
    public static final String TYPE_OTHER = "other";
    
    // 状态枚举（对应publishStatus字段）
    public static final String STATUS_ACTIVE = "published";
    public static final String STATUS_INACTIVE = "unpublished";
    public static final String STATUS_DELETED = "deleted";
    
    // 分类枚举
    public static final String CATEGORY_TEACHING = "teaching";
    public static final String CATEGORY_EXPERIMENT = "experiment";
    public static final String CATEGORY_REFERENCE = "reference";
    public static final String CATEGORY_TOOL = "tool";
}