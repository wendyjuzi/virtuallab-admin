package com.edu.virtuallab.experiment.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "experiment_report", autoResultMap = true)
public class ExperimentReport {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String sessionId;
    @TableField("student_id")
    private String studentId;
    @TableField("project_id")
    private String projectId;
    private String principle;
    private String purpose;
    private String category;
    private String method;
    private String steps;
    private String description;
    private String title;
    private String comment;
    private BigDecimal score;


    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private BigDecimal score;
    private String comment;

    @TableField("manual_content")
    private String manualContent;//用户填入的实验结果

    @TableField("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    @TableField("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;

    // 附件
    @TableField("attachment_path")
    private String attachmentPath; // 文件存储路径

    @TableField("original_filename")
    private String originalFilename; // 原始文件名

    @TableField("file_size")
    private Long fileSize; // 文件大小(字节)

    @TableField("mime_type")
    private String mimeType; // 文件类型

    public enum Status{
        DRAFT, SUBMITTED, GRADED, SAVED
    }

    @Enumerated(EnumType.STRING)
    private Status status;

    // 无参构造函数（初始化默认值）
    public ExperimentReport() {
        this.status = Status.DRAFT;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // 辅助方法：获取可下载的文件名（带原始扩展名）
    public String getDownloadFilename() {
        if (originalFilename != null && !originalFilename.isEmpty()) {
            return originalFilename;
        }
        if (attachmentPath != null && !attachmentPath.isEmpty()) {
            return attachmentPath.substring(attachmentPath.lastIndexOf('/') + 1);
        }
        return "attachment";
    }

    // 辅助方法：检查是否有附件
    public boolean hasAttachment() {
        return attachmentPath != null && !attachmentPath.isEmpty();
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

}