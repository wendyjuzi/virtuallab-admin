package com.edu.virtuallab.resource.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShareResponse {
    private Long id;
    private Long resourceId;
    private String shareType;
    private String shareLink;
    private String shareCode;
    private String shareTitle;
    private String shareDescription;
    private String shareImage;
    private String permission;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private Integer viewCount;
    private Integer downloadCount;
    private String status;
} 