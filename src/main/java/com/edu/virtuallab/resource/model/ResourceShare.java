package com.edu.virtuallab.resource.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("resource_share")
public class ResourceShare {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long resourceId;
    private String sharedBy; // 分享者用户名
    private String sharedWith; // 被分享者用户名或班级ID
    private String shareType; // user, class, link
    private String permission; // read, write, admin
    private String status; // active, inactive, expired
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    private LocalDateTime expiresAt;
    
    private String shareLink; // 分享链接
    private String shareCode; // 分享码
    private String shareTitle; // 分享标题
    private String shareDescription; // 分享描述
    private String shareImage; // 分享图片
    private Integer viewCount; // 查看次数
    private Integer downloadCount; // 下载次数
    
    // 权限枚举
    public static final String PERMISSION_READ = "read";
    public static final String PERMISSION_WRITE = "write";
    public static final String PERMISSION_ADMIN = "admin";
    
    // 状态枚举
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_EXPIRED = "expired";
    
    // 分享类型枚举
    public static final String SHARE_TYPE_USER = "user";
    public static final String SHARE_TYPE_CLASS = "class";
    public static final String SHARE_TYPE_LINK = "link";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getResourceId() { return resourceId; }
    public void setResourceId(Long resourceId) { this.resourceId = resourceId; }
    public String getSharedBy() { return sharedBy; }
    public void setSharedBy(String sharedBy) { this.sharedBy = sharedBy; }
    public String getSharedWith() { return sharedWith; }
    public void setSharedWith(String sharedWith) { this.sharedWith = sharedWith; }
    public String getShareType() { return shareType; }
    public void setShareType(String shareType) { this.shareType = shareType; }
    public String getPermission() { return permission; }
    public void setPermission(String permission) { this.permission = permission; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public String getShareLink() { return shareLink; }
    public void setShareLink(String shareLink) { this.shareLink = shareLink; }
    public String getShareCode() { return shareCode; }
    public void setShareCode(String shareCode) { this.shareCode = shareCode; }
    public String getShareTitle() { return shareTitle; }
    public void setShareTitle(String shareTitle) { this.shareTitle = shareTitle; }
    public String getShareDescription() { return shareDescription; }
    public void setShareDescription(String shareDescription) { this.shareDescription = shareDescription; }
    public String getShareImage() { return shareImage; }
    public void setShareImage(String shareImage) { this.shareImage = shareImage; }
    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
    public Integer getDownloadCount() { return downloadCount; }
    public void setDownloadCount(Integer downloadCount) { this.downloadCount = downloadCount; }
}