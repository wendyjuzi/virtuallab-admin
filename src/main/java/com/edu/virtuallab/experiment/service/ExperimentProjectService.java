package com.edu.virtuallab.experiment.service;

import com.edu.virtuallab.experiment.dto.ExperimentProjectPublishRequest;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import java.util.List;

public interface ExperimentProjectService {
    int create(ExperimentProject project);
    int update(ExperimentProject project);
    int delete(Long id);
    ExperimentProject getById(Long id);
    List<ExperimentProject> listAll();
    List<ExperimentProject> search(String category, String level, String keyword);
    void publishToClasses(Long projectId, List<Long> classIds);
    Long publishProject(ExperimentProjectPublishRequest request, String createdBy);
    List<ExperimentProject> getProjectsByCreatedBy(String createdBy);
    /**
     * 分页获取实验项目列表，支持分类、排序
     */
    List<ExperimentProject> listPage(String category, String sort, int page, int size);
    long countPage(String category);
    List<Long> getTeamsByStudentId(Long studentId);
    Long getStudentIdByUserId(Long userId);

    void save(ExperimentProject project);
    void approve(Long id, boolean approve, String comment);
    ExperimentProject findById(Long id);
}