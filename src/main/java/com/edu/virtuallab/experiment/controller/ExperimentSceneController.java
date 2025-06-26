package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.experiment.model.ExperimentScene;
import com.edu.virtuallab.experiment.service.ExperimentSceneService;
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
@RequestMapping("/experiment/scene")
public class ExperimentSceneController {
    @Autowired
    private ExperimentSceneService sceneService;

    @PostMapping
    public int create(@RequestBody ExperimentScene scene) {
        return sceneService.create(scene);
    }

    @PutMapping
    public int update(@RequestBody ExperimentScene scene) {
        return sceneService.update(scene);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        return sceneService.delete(id);
    }

    @GetMapping("/{id}")
    public ExperimentScene getById(@PathVariable Long id) {
        return sceneService.getById(id);
    }

    @GetMapping("/list")
    public List<ExperimentScene> listAll() {
        return sceneService.listAll();
    }
} 