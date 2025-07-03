package com.edu.virtuallab.experiment.service;

import com.edu.virtuallab.experiment.model.ProjectTeam;
import com.edu.virtuallab.experiment.model.ProjectTeamMember;

import java.util.List;

public interface ProjectTeamService {

    // 创建小组
    ProjectTeam createTeam(Long projectId, String teamName);

    // 查询某项目所有小组
    List<ProjectTeam> getTeamsByProjectId(Long projectId);

    // 查询小组详情（含成员）
    ProjectTeam getTeamById(Long teamId);

    // 修改小组名称
    boolean updateTeamName(Long teamId, String newName);

    // 删除小组（及其成员）
    boolean deleteTeam(Long teamId);

    // 给小组添加成员
    boolean addMember(Long teamId, Long studentId);

    // 移除小组成员
    boolean removeMember(Long teamId, Long studentId);

    // 查询某学生在某项目对应小组
    ProjectTeam getTeamByStudentAndProject(Long studentId, Long projectId);

    // 查询小组成员列表
    List<ProjectTeamMember> getMembersByTeamId(Long teamId);

    int autoGroupStudents(Long projectId, int groupSize);

    // 可根据需要添加方法
}
