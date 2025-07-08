package com.edu.virtuallab.experiment.dto;

public class AiTemplateDTO {
    private Long experimentId;
    private String title;
    private String experimentDesc;
    public Long getExperimentId() { return experimentId; }
    public void setExperimentId(Long experimentId) { this.experimentId = experimentId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getExperimentDesc() { return experimentDesc; }
    public void setExperimentDesc(String experimentDesc) { this.experimentDesc = experimentDesc; }
} 