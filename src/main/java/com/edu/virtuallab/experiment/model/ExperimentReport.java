package com.edu.virtuallab.experiment.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "experiment_report", autoResultMap = true)
public class ExperimentReport {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String sessionId;
    private String principle;
    private String purpose;
    private String category;
    private String method;
    private String steps;
    private String description;
    private String title;

    @TableField("manual_content")
    private String manualContent;//用户填入的实验结果

    @TableField("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    @TableField("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;

    private String attachment;//用户上传的附件
    private String status;//实验报告的状态

    // 无参构造函数（初始化默认值）
    public ExperimentReport() {
        this.attachment = "";
        this.status = "DRAFT";
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

}