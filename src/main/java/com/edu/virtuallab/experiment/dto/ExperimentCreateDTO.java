package com.edu.virtuallab.experiment.dto;

public class ExperimentCreateDTO {
    private String name;
    private String description;
    private String config; // three.js参数(JSON字符串)

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getConfig() { return config; }
    public void setConfig(String config) { this.config = config; }
} 