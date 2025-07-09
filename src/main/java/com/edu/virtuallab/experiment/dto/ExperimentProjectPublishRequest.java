package com.edu.virtuallab.experiment.dto;

import java.util.List;

public class ExperimentProjectPublishRequest {
    private String name;
    private String description;
    private String category;
    private String level;
    private String imageUrl;
    private String videoUrl;
    private List<Long> classIds;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public List<Long> getClassIds() { return classIds; }
    public void setClassIds(List<Long> classIds) { this.classIds = classIds; }

    private String projectType;
    // 新增 getProjectType 方法
    public String getProjectType() {
        return projectType;
    }
    // 新增 setProjectType 方法，方便设置属性值
    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
    private String principle;
    private String purpose;
    private String method;
    private String steps;
    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPrinciple() {
        return principle;
    }

    public void setPrinciple(String principle) {
        this.principle = principle;
    }

    private String screenshot;
    private String sceneData;
    private String experimentParams;
    private String sceneJsonPath;
    public String getSceneJsonPath() { return sceneJsonPath; }
    public void setSceneJsonPath(String sceneJsonPath) { this.sceneJsonPath = sceneJsonPath; }

    public String getScreenshot() { return screenshot; }
    public void setScreenshot(String screenshot) { this.screenshot = screenshot; }
    public String getSceneData() { return sceneData; }
    public void setSceneData(String sceneData) { this.sceneData = sceneData; }
    public String getExperimentParams() { return experimentParams; }
    public void setExperimentParams(String experimentParams) { this.experimentParams = experimentParams; }

}

