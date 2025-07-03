package com.edu.virtuallab.experiment.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;
import java.time.LocalDateTime;

@Data
@TableName("experiment_project")
public class ExperimentProject {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String category;
    private String description;
    private String level;
    private String imageUrl;
    private String videoUrl;
    private String projectType; // 值可能是 "personal" 或 "team"

    private Long uploaderId; // 上传者ID

    //    private Date createdAt;
//    private Date updatedAt;
//    private Long teacherId;
    private String createdBy; // 教师用户名
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    // 新增审核字段
    private String auditStatus; // 审核状态: draft/pending/approved/rejected
    private String auditComment; // 审核意见
    private Long auditorId; // 审核人ID
    private LocalDateTime auditTime; // 审核时间
    private String publishStatus; // 发布状态: unpublished/published
    private LocalDateTime publishTime; // 发布时间

    private String config; // three.js参数(JSON字符串)
    private Long creatorId;
    private String creatorRole; // "ADMIN" or "TEACHER"
    private String status; // "DRAFT", "PENDING_APPROVAL", "APPROVED", "REJECTED", "PUBLISHED"
    private String approveComment;
    private Date createTime;
    private Date updateTime;

    // getter & setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

//    public Date getCreatedAt() { return createdAt; }
//    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
//
//    public Date getUpdatedAt() { return updatedAt; }
//    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

//    public Long getTeacherId() { return teacherId; }
//    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }

    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public Long getUploaderId() {
        return uploaderId;
    }
    public void setUploaderId(Long uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getConfig() { return config; }
    public void setConfig(String config) { this.config = config; }
    public Long getCreatorId() { return creatorId; }
    public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }
    public String getCreatorRole() { return creatorRole; }
    public void setCreatorRole(String creatorRole) { this.creatorRole = creatorRole; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getApproveComment() { return approveComment; }
    public void setApproveComment(String approveComment) { this.approveComment = approveComment; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
