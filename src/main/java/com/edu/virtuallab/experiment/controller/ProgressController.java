package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.experiment.dto.ProgressDTO;
import com.edu.virtuallab.experiment.service.ProgressService;
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

    /**
     * 保存或更新进度
     */
//    @PostMapping("/save")
//    public ResponseEntity<String> saveOrUpdateProgress(@RequestBody ProgressDTO progressDTO) {
//        boolean success = progressService.saveOrUpdateProgress(progressDTO);
//        if (success) {
//            return ResponseEntity.ok("保存成功");
//        } else {
//            return ResponseEntity.status(500).body("保存失败");
//        }
//    }
}
