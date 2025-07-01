package com.edu.virtuallab.experiment.dao;

import com.edu.virtuallab.experiment.model.ProjectTeam;
import com.edu.virtuallab.experiment.model.ProjectTeamMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface ProjectTeamMemberDao {

    // 插入成员
    int insertMember(ProjectTeamMember member);

    // 根据小组ID查询成员列表
    List<ProjectTeamMember> findMembersByTeamId(Long teamId);

    // 删除成员
    int deleteMemberById(Long memberId);

    // 根据小组ID和学生ID删除成员
    int deleteMemberByTeamAndStudent(Long teamId, Long studentId);

    // 查询学生在某项目对应的小组
    ProjectTeam findTeamByStudentAndProject(Map<String, Object> params);
}
