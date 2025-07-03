package com.edu.virtuallab.experiment.dto;

public class ExperimentResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String config;
    private String status;
    private String creatorRole;
    private String approveComment;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getConfig() { return config; }
    public void setConfig(String config) { this.config = config; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatorRole() { return creatorRole; }
    public void setCreatorRole(String creatorRole) { this.creatorRole = creatorRole; }
    public String getApproveComment() { return approveComment; }
    public void setApproveComment(String approveComment) { this.approveComment = approveComment; }
} 