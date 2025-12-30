package com.edu.virtuallab.experiment.dao;

import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import java.util.List;

import com.edu.virtuallab.project.model.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.edu.virtuallab.experiment.dto.NameValueDTO;
import org.apache.ibatis.annotations.Select;

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
    int updateProject(ExperimentProject project);

    ExperimentProject  findById(@Param("id") Long id);

    // 新增分页查询方法（支持不同排序）
    List<ExperimentProject> listPageWithSort(
            @Param("adminUsernames") List<String> adminUsernames,
            @Param("category") String category,
            @Param("level") String level,
            @Param("keyword") String keyword,
            @Param("sort") String sort,
            @Param("offset") int offset,
            @Param("size") int size
    );

    // 新增计数方法
    long countWithSort(
            @Param("adminUsernames") List<String> adminUsernames,
            @Param("category") String category,
            @Param("level") String level,
            @Param("keyword") String keyword
    );

    // 实验类型分布
    List<NameValueDTO> countByType();
    // 实验活跃度趋势
    List<NameValueDTO> countActiveByDay();
    // 实验完成率排行
    List<NameValueDTO> rankByFinishRate();
    // 实验参与人数Top5
    List<NameValueDTO> topByParticipants();
    int countAll();

    @Select("SELECT class_id as classId FROM experiment_project_class WHERE project_id = #{projectId}")
    List<Long> findClassIdsByProjectId(@Param("projectId") Long projectId);
}
