package com.edu.virtuallab.experiment.service.impl;

import com.edu.virtuallab.experiment.dao.ExperimentProjectClassDao;
import com.edu.virtuallab.experiment.dao.ProjectTeamDao;
import com.edu.virtuallab.experiment.dao.ProjectTeamMemberDao;
import com.edu.virtuallab.experiment.dao.StudentClassDao;
import com.edu.virtuallab.experiment.model.ProjectTeam;
import com.edu.virtuallab.experiment.model.ProjectTeamMember;
import com.edu.virtuallab.experiment.service.ProjectTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProjectTeamServiceImpl implements ProjectTeamService {

    private final ProjectTeamDao teamDao;
    private final ProjectTeamMemberDao memberDao;
    @Autowired
    private ExperimentProjectClassDao experimentProjectClassDao;
    @Autowired
    private StudentClassDao studentClassDao;
    public ProjectTeamServiceImpl(ProjectTeamDao teamDao, ProjectTeamMemberDao memberDao) {
        this.teamDao = teamDao;
        this.memberDao = memberDao;
    }

    @Override
    @Transactional
    public ProjectTeam createTeam(Long projectId, String teamName) {
        ProjectTeam team = new ProjectTeam();
        team.setProjectId(projectId);
        team.setTeamName(teamName);
        int inserted = teamDao.insertTeam(team);
        if (inserted > 0) {
            return team;
        }
        return null;
    }

    @Override
    public List<ProjectTeam> getTeamsByProjectId(Long projectId) {
        return teamDao.findTeamsByProjectId(projectId);
    }

    @Override
    public ProjectTeam getTeamById(Long teamId) {
        return teamDao.findTeamById(teamId);
    }

    @Override
    @Transactional
    public boolean updateTeamName(Long teamId, String newName) {
        ProjectTeam team = new ProjectTeam();
        team.setId(teamId);
        team.setTeamName(newName);
        return teamDao.updateTeamName(team) > 0;
    }

    @Override
    @Transactional
    public boolean deleteTeam(Long teamId) {
        // 先删除小组成员
        List<ProjectTeamMember> members = memberDao.findMembersByTeamId(teamId);
        for (ProjectTeamMember member : members) {
            memberDao.deleteMemberById(member.getId());
        }
        // 再删除小组
        return teamDao.deleteTeamById(teamId) > 0;
    }

    @Override
    @Transactional
    public boolean addMember(Long teamId, Long studentId) {
        ProjectTeamMember member = new ProjectTeamMember();
        member.setTeamId(teamId);
        member.setStudentId(studentId);
        return memberDao.insertMember(member) > 0;
    }

    @Override
    @Transactional
    public boolean removeMember(Long teamId, Long studentId) {
        return memberDao.deleteMemberByTeamAndStudent(teamId, studentId) > 0;
    }

    @Override
    public ProjectTeam getTeamByStudentAndProject(Long studentId, Long projectId) {
        return memberDao.findTeamByStudentAndProject(
                java.util.Map.of("studentId", studentId, "projectId", projectId)
        );
    }

    @Override
    public List<ProjectTeamMember> getMembersByTeamId(Long teamId) {
        return memberDao.findMembersByTeamId(teamId);
    }

    @Override
    @Transactional
    public int autoGroupStudents(Long projectId, int groupSize) {
        System.out.println("[autoGroupStudents] 开始自动分组，projectId = " + projectId + ", groupSize = " + groupSize);

        // 1. 查询该实验项目对应班级的所有学生
        List<Long> classIds = experimentProjectClassDao.findClassIdsByProjectId(projectId);
        System.out.println("[autoGroupStudents] 查询到班级ID: " + classIds);

        List<Long> studentIds = studentClassDao.findStudentIdsByClassIds(classIds);
        System.out.println("[autoGroupStudents] 查询到学生ID: " + studentIds);

        if (studentIds == null || studentIds.isEmpty()) {
            System.out.println("[autoGroupStudents] 没有找到参与该项目的学生，抛出异常");
            throw new RuntimeException("没有找到参与该项目的学生");
        }

        // 2. 平均分组
        int total = studentIds.size();
        int groupCount = (int) Math.ceil((double) total / groupSize);
        System.out.println("[autoGroupStudents] 总学生数: " + total + ", 将分为 " + groupCount + " 组");

        List<List<Long>> groups = new ArrayList<>();
        for (int i = 0; i < groupCount; i++) {
            groups.add(new ArrayList<>());
        }

        for (int i = 0; i < studentIds.size(); i++) {
            groups.get(i % groupCount).add(studentIds.get(i));
        }

        // 3. 为每个小组创建 team + 添加成员
        char teamLabel = 'A';
        int teamIndex = 1;
        for (List<Long> group : groups) {
            String teamName = "组" + teamLabel++;
            System.out.println("[autoGroupStudents] 创建小组: " + teamName + "，成员数: " + group.size());

            ProjectTeam team = new ProjectTeam();
            team.setProjectId(projectId);
            team.setName(teamName);
            teamDao.insertTeam(team);
            System.out.println("[autoGroupStudents] 已插入小组，teamId = " + team.getId());

            for (Long studentId : group) {
                ProjectTeamMember member = new ProjectTeamMember();
                member.setTeamId(team.getId());
                member.setStudentId(studentId);
                memberDao.insertMember(member);
                System.out.println("[autoGroupStudents] 添加成员 studentId = " + studentId + " 到小组 teamId = " + team.getId());
            }

            teamIndex++;
        }

        System.out.println("[autoGroupStudents] 自动分组完成，总共创建 " + groupCount + " 个小组");
        return groupCount;
    }


}
