package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.experiment.dto.ProgressDTO;
import com.edu.virtuallab.experiment.service.ProgressService;
import com.edu.virtuallab.log.annotation.OperationLogRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/progress")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    /**
     * 根据项目ID获取所有学生进度
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ProgressDTO>> getProgressByProject(@PathVariable Long projectId) {
        List<ProgressDTO> progressList = progressService.getProgressByProjectId(projectId);
        return ResponseEntity.ok(progressList);
    }

    /**
     * 获取某个学生某个项目的进度详情
     */
//    @GetMapping("/student/{studentId}/project/{projectId}")
//    public ResponseEntity<ProgressDTO> getProgress(@PathVariable Long studentId, @PathVariable Long projectId) {
//        ProgressDTO dto = progressService.getProgress(studentId, projectId);
//        if (dto == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(dto);
//    }
//
//    /**
//     * 保存或更新进度
//     */
//    @OperationLogRecord(operation = "CREATE_PROGRESS", module = "EXPERIMENT", action = "创建实验进度", description = "用户创建实验进度", permissionCode = "EXPERIMENT_MANAGE")
//    @PostMapping("/create")
//    public int create(@RequestBody Progress progress) {
//        // ... existing code ...
//    }
//
//    @OperationLogRecord(operation = "UPDATE_PROGRESS", module = "EXPERIMENT", action = "更新实验进度", description = "用户更新实验进度", permissionCode = "EXPERIMENT_MANAGE")
//    @PutMapping("/update")
//    public int update(@RequestBody Progress progress) {
//        // ... existing code ...
//    }
//
//    @OperationLogRecord(operation = "DELETE_PROGRESS", module = "EXPERIMENT", action = "删除实验进度", description = "用户删除实验进度", permissionCode = "EXPERIMENT_MANAGE")
//    @DeleteMapping("/{id}")
//    public int delete(@PathVariable Long id) {
//        // ... existing code ...
//    }
}
