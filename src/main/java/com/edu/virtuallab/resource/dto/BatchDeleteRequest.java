package com.edu.virtuallab.resource.dto;

import java.util.List;

/**
 * 批量删除请求DTO
 */
public class BatchDeleteRequest {
    private List<Long> ids;
    private String reason;

    public BatchDeleteRequest() {}

    public BatchDeleteRequest(List<Long> ids, String reason) {
        this.ids = ids;
        this.reason = reason;
    }

    // Getters and Setters
    public List<Long> getIds() { return ids; }
    public void setIds(List<Long> ids) { this.ids = ids; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
} 