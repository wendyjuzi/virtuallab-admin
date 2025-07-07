package com.edu.virtuallab.audit.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditLogDTO {
    private Long id;
    private String projectName;       // 项目名称
    private String auditorName;      // 审核人姓名
    private String fromStatus;       // 原状态
    private String toStatus;         // 新状态
    private String comment;          // 审核意见
    private LocalDateTime createdAt; // 创建时间
}
