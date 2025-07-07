package com.edu.virtuallab.audit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.virtuallab.audit.dto.AuditLogDTO;

public interface AuditLogService {
    Page<AuditLogDTO> getAuditLogsByDepartment(
            String department,
            String keyword,
            int pageNum,
            int pageSize);
}
