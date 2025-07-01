package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.experiment.dto.AutoGroupRequest;
import com.edu.virtuallab.experiment.model.ProjectTeam;
import com.edu.virtuallab.experiment.model.ProjectTeamMember;
import com.edu.virtuallab.experiment.service.ProjectTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projectTeam")
public class ProjectTeamController {

    private final ProjectTeamService projectTeamService;
    @PostMapping("/auto-group")
    public Map<String, Object> autoGroup(@RequestBody AutoGroupRequest request) {
        System.out.println("准备开始分组");

        int groupCount = projectTeamService.autoGroupStudents(request.getProjectId(), request.getGroupSize());
        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("msg", "分组完成，共分 " + groupCount + " 组");
        res.put("data", Map.of(
                "groupCount", groupCount
        ));
        return res;
    }
    @Autowired
    public ProjectTeamController(ProjectTeamService projectTeamService) {
        this.projectTeamService = projectTeamService;
    }

    // 创建小组
    @PostMapping
    public ProjectTeam createTeam(@RequestParam Long projectId, @RequestParam String teamName) {
        return projectTeamService.createTeam(projectId, teamName);
    }

    // 查询某项目所有小组
    @GetMapping("/project/{projectId}")
    public List<ProjectTeam> getTeamsByProjectId(@PathVariable Long projectId) {
        return projectTeamService.getTeamsByProjectId(projectId);
    }

    // 查询小组详情（含成员）
    @GetMapping("/{teamId}")
    public ProjectTeam getTeamById(@PathVariable Long teamId) {
        return projectTeamService.getTeamById(teamId);
    }

    // 修改小组名称
    @PutMapping("/{teamId}")
    public boolean updateTeamName(@PathVariable Long teamId, @RequestParam String newName) {
        return projectTeamService.updateTeamName(teamId, newName);
    }

    // 删除小组
    @DeleteMapping("/{teamId}")
    public boolean deleteTeam(@PathVariable Long teamId) {
        return projectTeamService.deleteTeam(teamId);
    }

    // 添加成员
    @PostMapping("/{teamId}/member/{studentId}")
    public boolean addMember(@PathVariable Long teamId, @PathVariable Long studentId) {
        return projectTeamService.addMember(teamId, studentId);
    }

    // 移除成员
    @DeleteMapping("/{teamId}/member/{studentId}")
    public boolean removeMember(@PathVariable Long teamId, @PathVariable Long studentId) {
        return projectTeamService.removeMember(teamId, studentId);
    }

    // 查询某学生在某项目下的小组
    @GetMapping("/ofStudent")
    public ProjectTeam getTeamByStudentAndProject(@RequestParam Long studentId, @RequestParam Long projectId) {
        return projectTeamService.getTeamByStudentAndProject(studentId, projectId);
    }

    // 查询某小组的所有成员
    @GetMapping("/{teamId}/members")
    public List<ProjectTeamMember> getMembersByTeamId(@PathVariable Long teamId) {
        return projectTeamService.getMembersByTeamId(teamId);
    }

    @GetMapping("/project/{projectId}/with-members")
    public List<Map<String, Object>> getTeamsWithMembers(@PathVariable Long projectId) {
        List<ProjectTeam> teams = projectTeamService.getTeamsByProjectId(projectId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (ProjectTeam team : teams) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", team.getId());
            map.put("name", team.getName());

            List<ProjectTeamMember> members = projectTeamService.getMembersByTeamId(team.getId());
            map.put("members", members);

            // 打印每个小组及其成员（调试用）
            System.out.println("小组: " + team.getName() + "（ID: " + team.getId() + "）");
            for (ProjectTeamMember member : members) {
                System.out.println(" - 成员: studentId = " + member.getStudentId());
            }

            result.add(map);
        }

        // 如果你想看看整体结构
        System.out.println("返回给前端的数据: " + result);

        return result;
    }


}
