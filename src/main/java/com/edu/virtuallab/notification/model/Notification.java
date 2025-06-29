package com.edu.virtuallab.notification.model;

import com.baomidou.mybatisplus.annotation.*;
import com.edu.virtuallab.common.enums.NotificationType;
import com.edu.virtuallab.common.handler.EnumValueTypeHandler;
import lombok.Data;

import java.util.Date;

@Data
@TableName("notification")
public class Notification {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField(value = "type", typeHandler = EnumValueTypeHandler.class)
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

    // 添加 copy() 方法
    public Notification copy() {
        Notification copy = new Notification();
        copy.setUserId(this.userId);
        copy.setType(this.type);
        copy.setTitle(this.title);
        copy.setContent(this.content);
        copy.setIsRead(this.isRead);
        copy.setLink(this.link);
        copy.setRelatedId(this.relatedId);
        copy.setCreatedAt(this.createdAt != null ? new Date(this.createdAt.getTime()) : null);
        return copy;
    }
}