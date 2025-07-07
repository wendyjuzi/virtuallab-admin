package com.edu.virtuallab.experiment.dao;

import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExperimentProjectDao {
    int insert(ExperimentProject project);
    int update(ExperimentProject project);
    int delete(Long id);
    ExperimentProject selectById(Long id);
    List<ExperimentProject> selectAll();

    // 多条件搜索方法
    List<ExperimentProject> search(
            @Param("category") String category,
            @Param("level") String level,
            @Param("keyword") String keyword
    );
    List<ExperimentProject> getProjectsByCreatedBy(String createdBy);
    List<Long> getTeamsByStudentId(@Param("studentId") Long studentId);

    Long getStudentIdByUserId(Long userId);
    int updateStatusToInProgress(@Param("projectId") Integer projectId,
                                 @Param("studentId") String studentId);
    int updateStatusToCompleted(@Param("projectId") Integer projectId,
                                @Param("studentId") String studentId);


    List<ExperimentProject> listPage(@Param("category") String category, @Param("sort") String sort, @Param("offset") int offset, @Param("size") int size);
    long countPage(@Param("category") String category);
    int countPendingGradingReports(@Param("teacherName") String teacherName);
    List<Integer> getScoresByProjectId(Long projectId);

    ExperimentProject  findById(@Param("id") Long id);
}