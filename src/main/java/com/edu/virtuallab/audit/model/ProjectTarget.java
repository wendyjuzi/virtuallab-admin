package com.edu.virtuallab.audit.model;

import com.baomidou.mybatisplus.annotation.*;
import com.edu.virtuallab.common.enums.TargetType;
import lombok.Data;

import java.util.Date;

@Data
@TableName("project_target")
public class ProjectTarget {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("project_id")
    private Long projectId;

    @TableField("target_id")
    private Long targetId;

    @TableField("target_type")
    private TargetType targetType;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Date createdAt;
}
