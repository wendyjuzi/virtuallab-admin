package com.edu.virtuallab.notification.model;

import com.baomidou.mybatisplus.annotation.*;
import com.edu.virtuallab.common.enums.NotificationType;
import lombok.Data;

import java.util.Date;

@Data
@TableName("notification")
public class Notification {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    private NotificationType type;

    private String title;

    private String content;

    @TableField("is_read")
    private Boolean isRead = false;

    private String link;

    @TableField("related_id")
    private Long relatedId;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Date createdAt;
}