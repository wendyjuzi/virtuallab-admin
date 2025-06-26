package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.experiment.model.ExperimentRecord;
import com.edu.virtuallab.experiment.service.ExperimentRecordService;
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
@RequestMapping("/experiment/record")
public class ExperimentRecordController {
    @Autowired
    private ExperimentRecordService recordService;

    @PostMapping
    public int create(@RequestBody ExperimentRecord record) {
        return recordService.create(record);
    }

    @PutMapping
    public int update(@RequestBody ExperimentRecord record) {
        return recordService.update(record);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        return recordService.delete(id);
    }

    @GetMapping("/{id}")
    public ExperimentRecord getById(@PathVariable Long id) {
        return recordService.getById(id);
    }

    @GetMapping("/list")
    public List<ExperimentRecord> listAll() {
        return recordService.listAll();
    }
} 