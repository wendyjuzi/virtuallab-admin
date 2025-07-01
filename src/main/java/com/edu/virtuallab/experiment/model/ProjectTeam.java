package com.edu.virtuallab.experiment.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectTeam {
    private Long id;
    private Long projectId;
    private String teamName;
    private LocalDateTime createdAt;
    public String getName() {
        return teamName;
    }

    public void setName(String name) {
        this.teamName = name;
    }
}
