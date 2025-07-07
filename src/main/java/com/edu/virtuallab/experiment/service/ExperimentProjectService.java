package com.edu.virtuallab.experiment.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.virtuallab.common.api.PageResult;
import com.edu.virtuallab.experiment.dto.ExperimentProjectPublishRequest;
import com.edu.virtuallab.experiment.dto.StudentExperimentProjectDTO;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import com.edu.virtuallab.project.model.Project;

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
    int markAsInProgress(Integer projectId, String studentId);
    int markAsCompleted(Integer projectId, String studentId);
    int countPendingGradingReports(String teacherName);
    List<Integer> getScoresByProjectId(Long projectId);
    boolean updateProject(ExperimentProject project);

    void save(ExperimentProject project);
    void approve(Long id, boolean approve, String comment);
    ExperimentProject findById(Long id);

    Page<StudentExperimentProjectDTO> getProjectsByStudentId(
            Long studentId,
            String keyword,
            int pageNum,
            int pageSize);

    // 首页分类查询
    PageResult<ExperimentProject> listWithSort(
            List<String> adminUsernames,
            String category,
            String level,
            String keyword,
            String sort,
            int page,
            int size
    );
    List<String> getAdminUsernames();
}