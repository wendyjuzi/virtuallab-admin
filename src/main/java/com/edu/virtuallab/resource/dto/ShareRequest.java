package com.edu.virtuallab.resource.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ShareRequest {
    private Long resourceId;
    private String shareType; // user, class, link
    private List<String> userIds; // 用户ID列表
    private List<String> classIds; // 班级ID列表
    private String permission; // read, write, admin
    private LocalDateTime expiresAt; // 过期时间
    private String shareTitle; // 分享标题
    private String shareDescription; // 分享描述
    private String shareImage; // 分享图片
    private Boolean enableDownload; // 是否允许下载
    private Boolean enableComment; // 是否允许评论
} 