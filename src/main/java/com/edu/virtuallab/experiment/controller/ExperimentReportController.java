package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.experiment.model.ExperimentReport;
import com.edu.virtuallab.experiment.service.ExperimentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/experiment/report")
public class ExperimentReportController {

    @Autowired
    private ExperimentReportService experimentreportService;

    // 文件上传目录
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    // 通过sessionId查询（业务标识）
    @GetMapping("/{sessionId}")
    public ResponseEntity<ExperimentReport> getReportBySession(@PathVariable String sessionId) {
        ExperimentReport experimentReport = experimentreportService.getReportBySession(sessionId);
        return ResponseEntity.ok(experimentReport != null ? experimentReport : new ExperimentReport());
    }

    @PostMapping("/content/{sessionId}")
    public ResponseEntity<Void> saveContent(
            @PathVariable String sessionId,
            @RequestParam String manualContent) {
        experimentreportService.saveReportContent(sessionId, manualContent);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/attachment/{sessionId}")
    public ResponseEntity<String> uploadAttachment(
            @PathVariable String sessionId,
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("文件不能为空");
        }

        try {
            // 确保上传目录存在
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 生成唯一文件名
            String orginalFilename = file.getOriginalFilename();
            String fileExtension = orginalFilename.substring(orginalFilename.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // 保存文件
            Path filePath = uploadPath.resolve(uniqueFileName);
            file.transferTo(filePath);

            // 更新数据库记录
            experimentreportService.saveReportAttachment(sessionId, uniqueFileName);

            return ResponseEntity.ok(uniqueFileName);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("文件上传失败: " + e.getMessage());
        }
    }

    @PostMapping("/submit/{sessionId}")
    public ResponseEntity<Void> submitReport(@PathVariable String sessionId) {
        experimentreportService.submitReport(sessionId);
        return ResponseEntity.ok().build();
    }
}
