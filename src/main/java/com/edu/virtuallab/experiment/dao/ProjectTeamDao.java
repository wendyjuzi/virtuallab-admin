package com.edu.virtuallab.experiment.dao;

import com.edu.virtuallab.experiment.model.ProjectTeam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ProjectTeamDao {

    // 插入小组，插入后小组ID自动回填
    int insertTeam(ProjectTeam team);

    // 根据项目ID查询所有小组
    List<ProjectTeam> findTeamsByProjectId(Long projectId);

    // 根据小组ID查询小组
    ProjectTeam findTeamById(Long teamId);

    // 根据ID删除小组
    int deleteTeamById(Long teamId);

    // 更新小组名称
    int updateTeamName(ProjectTeam team);
}
