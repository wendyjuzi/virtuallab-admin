package com.edu.virtuallab.resource.model;

import java.util.Date;
import lombok.Data;

@Data
public class ResourceCopy {
    private Long id;
    private Long originalId;
    private Long copyId;
    private Long userId;
    private String copyReason;
    private Date createTime;
    private Long sourceResourceId;
    private String newName;
    private String newDescription;
    private String targetCategory;
    private String copiedBy;
    private String targetUrl;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }

    public Long getCopyId() {
        return copyId;
    }

    public void setCopyId(Long copyId) {
        this.copyId = copyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCopyReason() {
        return copyReason;
    }

    public void setCopyReason(String copyReason) {
        this.copyReason = copyReason;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}