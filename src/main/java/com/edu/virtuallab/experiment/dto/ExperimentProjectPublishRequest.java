package com.edu.virtuallab.experiment.dto;

import lombok.Data;
import java.util.List;

@Data
public class ExperimentProjectPublishRequest {
    private String name;
    private String description;
    private String category;
    private String level;
    private String imageUrl;
    private String videoUrl;
    private List<Long> classIds;
    private String projectType; // 值可能是 "personal" 或 "team"

}

