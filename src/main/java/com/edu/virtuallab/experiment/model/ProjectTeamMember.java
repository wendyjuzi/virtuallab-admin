package com.edu.virtuallab.experiment.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectTeamMember {
    private Long id;
    private Long teamId;
    private Long studentId;
    private LocalDateTime joinedAt;
}