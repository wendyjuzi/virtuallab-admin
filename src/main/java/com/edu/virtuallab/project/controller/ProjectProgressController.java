package com.edu.virtuallab.project.controller;

import com.edu.virtuallab.project.model.ProjectProgress;
import com.edu.virtuallab.project.service.ProjectProgressService;
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
@RequestMapping("/project/progress")
public class ProjectProgressController {
    @Autowired
    private ProjectProgressService progressService;

    @PostMapping
    public int create(@RequestBody ProjectProgress progress) {
        return progressService.create(progress);
    }

    @PutMapping
    public int update(@RequestBody ProjectProgress progress) {
        return progressService.update(progress);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        return progressService.delete(id);
    }

    @GetMapping("/{id}")
    public ProjectProgress getById(@PathVariable Long id) {
        return progressService.getById(id);
    }

    @GetMapping("/list")
    public List<ProjectProgress> listAll() {
        return progressService.listAll();
    }
} 