package com.edu.virtuallab.resource.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("resource_interaction")
public class ResourceInteraction {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long resourceId;
    private String userId;
    private String interactionType; // view, download, like, comment, rate
    private String content; // 评论内容或评分
    private Integer rating; // 评分 1-5
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    // 交互类型枚举
    public static final String TYPE_VIEW = "view";
    public static final String TYPE_DOWNLOAD = "download";
    public static final String TYPE_LIKE = "like";
    public static final String TYPE_COMMENT = "comment";
    public static final String TYPE_RATE = "rate";
}