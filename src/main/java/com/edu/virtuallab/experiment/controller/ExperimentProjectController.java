package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.auth.util.JwtUtil;
import com.edu.virtuallab.experiment.dto.ExperimentProjectPublishRequest;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import com.edu.virtuallab.experiment.model.ProjectTeam;
import com.edu.virtuallab.experiment.service.ExperimentProjectService;
import com.edu.virtuallab.experiment.dto.ExperimentProjectListDTO;
import com.edu.virtuallab.common.api.PageResult;
import com.edu.virtuallab.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.edu.virtuallab.log.annotation.OperationLogRecord;

@RestController
@RequestMapping("/experiment/project")
public class ExperimentProjectController {
    @Autowired
    private ExperimentProjectService projectService;

    @OperationLogRecord(operation = "CREATE_EXPERIMENT_PROJECT", module = "EXPERIMENT", action = "创建实验项目", description = "用户创建实验项目", permissionCode = "EXPERIMENT_MANAGE")
    @PostMapping("/create")
    public int create(@RequestBody ExperimentProject project) {
        return projectService.create(project);
    }

    @OperationLogRecord(operation = "UPDATE_EXPERIMENT_PROJECT", module = "EXPERIMENT", action = "更新实验项目", description = "用户更新实验项目", permissionCode = "EXPERIMENT_MANAGE")
    @PutMapping("/update")
    public int update(@RequestBody ExperimentProject project) {
        return projectService.update(project);
    }

    @OperationLogRecord(operation = "DELETE_EXPERIMENT_PROJECT", module = "EXPERIMENT", action = "删除实验项目", description = "用户删除实验项目", permissionCode = "EXPERIMENT_MANAGE")
    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        return projectService.delete(id);
    }

    @GetMapping("/{id}")
    public ExperimentProject getById(@PathVariable Long id) {
        return projectService.getById(id);
    }
    @GetMapping("/list")
    public List<ExperimentProject> listAll() {
        return projectService.listAll();
    }

    @GetMapping("/viewlist")
    public CommonResult<PageResult<ExperimentProjectListDTO>> list(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "latest") String sort
    ) {
        List<ExperimentProject> projects = projectService.listPage(category, sort, page, size);
        long total = projectService.countPage(category);
        // 实体转DTO
        List<ExperimentProjectListDTO> dtoList = projects.stream().map(p -> {
            ExperimentProjectListDTO dto = new ExperimentProjectListDTO();
            dto.setId(p.getId());
            dto.setTitle(p.getName());
            dto.setDescription(p.getDescription());
            dto.setCategory(p.getCategory());
            dto.setImageUrl(p.getImageUrl());
            dto.setViews(null); // 预留
            dto.setLikes(null); // 预留
            dto.setFavorites(null); // 预留
            dto.setCreateTime(p.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
        PageResult<ExperimentProjectListDTO> pageResult = new PageResult<>(total, dtoList);
        return CommonResult.success(pageResult, "success");
    }

    @GetMapping("/search")
    public List<ExperimentProject> search(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String keyword
    ) {
        // 为防止前端传null，service层也可以处理
        if (category != null && category.trim().isEmpty()) {
            category = null;
        }
        if (level != null && level.trim().isEmpty()) {
            level = null;
        }
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }
        return projectService.search(category, level, keyword);
    }

    /**
     * 标准实验项目分页/条件查询接口
     * GET /experiment/project/query?page=1&size=10&category=xxx&level=xxx&keyword=xxx
     */
    @GetMapping("/query")
    public CommonResult<Map<String, Object>> queryProjects(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String keyword
    ) {
        List<ExperimentProject> records = projectService.search(category, level, keyword);
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, records.size());
        List<ExperimentProject> pageList = fromIndex < records.size() ? records.subList(fromIndex, toIndex) : java.util.Collections.emptyList();
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageList);
        result.put("total", records.size());
        return CommonResult.success(result, "查询成功");
    }


    // Controller 层
    @PostMapping("/publish")
    public Map<String, Object> publishProject(HttpServletRequest request, @RequestBody ExperimentProjectPublishRequest req) {
        String username = JwtUtil.getUsernameFromRequest(request);
        if (username == null) {
            throw new RuntimeException("用户未登录");
        }

        Long projectId = projectService.publishProject(req, username);

        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("msg", "发布成功");
        res.put("projectId", projectId);
        return res;
    }



    @GetMapping("/my-projects")
    public List<ExperimentProject> getMyProjects(HttpServletRequest request) {
        String username = JwtUtil.getUsernameFromRequest(request);
        if (username == null) {
            throw new RuntimeException("用户未登录");
        }
        return projectService.getProjectsByCreatedBy(username);
    }
    // 获取当前学生参与的所有实验项目小组聊天
    @GetMapping("/chat/teams")
    public List<Long> getTeamsByUserId(@RequestParam Long userId) {
        Long studentId = projectService.getStudentIdByUserId(userId);
        List<Long> teams = projectService.getTeamsByStudentId(studentId);
        System.out.println("Debug: teams = " + teams);
        return projectService.getTeamsByStudentId(studentId);
    }








class ApproveRequest {
    private boolean approve;
    private String comment;
    public boolean isApprove() { return approve; }
    public void setApprove(boolean approve) { this.approve = approve; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
}
