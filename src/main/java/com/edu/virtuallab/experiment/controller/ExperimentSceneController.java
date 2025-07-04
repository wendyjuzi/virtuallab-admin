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
import com.edu.virtuallab.log.annotation.OperationLogRecord;

@RestController
@RequestMapping("/experiment/scene")
public class ExperimentSceneController {
    @Autowired
    private ExperimentSceneService sceneService;

    @OperationLogRecord(operation = "CREATE_EXPERIMENT_SCENE", module = "EXPERIMENT", action = "创建实验场景", description = "用户创建实验场景", permissionCode = "EXPERIMENT_MANAGE")
    @PostMapping("/create")
    public int create(@RequestBody ExperimentScene scene) {
        return sceneService.create(scene);
    }

    @OperationLogRecord(operation = "UPDATE_EXPERIMENT_SCENE", module = "EXPERIMENT", action = "更新实验场景", description = "用户更新实验场景", permissionCode = "EXPERIMENT_MANAGE")
    @PutMapping("/update")
    public int update(@RequestBody ExperimentScene scene) {
        return sceneService.update(scene);
    }

    @OperationLogRecord(operation = "DELETE_EXPERIMENT_SCENE", module = "EXPERIMENT", action = "删除实验场景", description = "用户删除实验场景", permissionCode = "EXPERIMENT_MANAGE")
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