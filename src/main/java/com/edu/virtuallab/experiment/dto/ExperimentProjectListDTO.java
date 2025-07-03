package com.edu.virtuallab.experiment.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExperimentProjectListDTO {
    private Long id;
    private String title; // name字段映射
    private String description;
    private String category;
    private String imageUrl;
    private Integer views; // 预留，暂时可为null
    private Integer likes; // 预留，暂时可为null
    private Integer favorites; // 预留，暂时可为null
    private LocalDateTime createTime; // createdAt字段映射
} 