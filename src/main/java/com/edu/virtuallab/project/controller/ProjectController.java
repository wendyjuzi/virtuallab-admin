package com.edu.virtuallab.project.controller;

import com.edu.virtuallab.project.model.Project;
import com.edu.virtuallab.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping
    public int create(@RequestBody Project project) {
        return projectService.create(project);
    }

    @PutMapping
    public int update(@RequestBody Project project) {
        return projectService.update(project);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        return projectService.delete(id);
    }

    @GetMapping("/{id}")
    public Project getById(@PathVariable Long id) {
        return projectService.getById(id);
    }

    @GetMapping("/list")
    public List<Project> listAll() {
        return projectService.listAll();
    }
} 