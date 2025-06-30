package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.auth.util.JwtUtil;
import com.edu.virtuallab.experiment.dto.ExperimentProjectPublishRequest;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import com.edu.virtuallab.experiment.service.ExperimentProjectService;
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
import java.util.List;

@RestController
@RequestMapping("/experiment/project")
public class ExperimentProjectController {
    @Autowired
    private ExperimentProjectService projectService;

    @PostMapping
    public int create(@RequestBody ExperimentProject project) {
        return projectService.create(project);
    }

    @PutMapping
    public int update(@RequestBody ExperimentProject project) {
        return projectService.update(project);
    }

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


    @PostMapping("/publish")
    public int publishProject(HttpServletRequest request, @RequestBody ExperimentProjectPublishRequest req) {
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header = " + authHeader); // 打印出来看看

        String username = JwtUtil.getUsernameFromRequest(request);
        if (username == null) {
            throw new RuntimeException("用户未登录");
        }
        return projectService.publishProject(req, username);
    }

    @GetMapping("/my-projects")
    public List<ExperimentProject> getMyProjects(HttpServletRequest request) {
        String username = JwtUtil.getUsernameFromRequest(request);
        if (username == null) {
            throw new RuntimeException("用户未登录");
        }
        return projectService.getProjectsByCreatedBy(username);
    }

}