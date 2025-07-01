package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.common.exception.BusinessException;
import com.edu.virtuallab.experiment.model.ExperimentReport;
import com.edu.virtuallab.experiment.service.ExperimentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/experiment/report")
public class ExperimentReportController {

    private final ExperimentReportService experimentReportService;
    // 构造器注入
    public ExperimentReportController(ExperimentReportService experimentReportService) {
        this.experimentReportService = experimentReportService;
    }

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

    @GetMapping("/{sessionId}/details")
    public ResponseEntity<ExperimentReport> getReportDetails(@PathVariable String sessionId) {
        ExperimentReport experimentReport = experimentreportService.getReportBySession(sessionId);
        return ResponseEntity.ok(experimentReport);
    }

    @PostMapping("/{sessionId}/save")
    public ResponseEntity<Void> saveContent(
            @PathVariable String sessionId,
            @RequestParam String manualContent) {
        experimentreportService.saveReportContent(sessionId, manualContent);
        return ResponseEntity.ok().build();
    }

    // 获取附件列表
    @GetMapping("/{sessionId}/attachments")
    public ResponseEntity<List<String>> listAttachments(@PathVariable String sessionId) {
        return ResponseEntity.ok(experimentReportService.listAttachments(sessionId));
    }

    // 下载单个附件
    @GetMapping("/{sessionId}/attachments/{filename}")
    public ResponseEntity<byte[]> downloadAttachment(
            @PathVariable String sessionId,
            @PathVariable String filename) {
        byte[] fileData = experimentReportService.downloadAttachment(sessionId, filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileData);
    }

    // 上传附件
    @PostMapping("/{sessionId}/attachments")
    public ResponseEntity<String> uploadAttachment(
            @PathVariable String sessionId,
            @RequestParam("file") MultipartFile file) {
        try {
            experimentReportService.saveReportAttachment(sessionId, file);
            return ResponseEntity.ok("附件上传成功");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("文件处理错误: " + e.getMessage());
        }
    }

    // 删除附件
    @DeleteMapping("/{sessionId}/attachments/{filename}")
    public ResponseEntity<Void> deleteAttachment(
            @PathVariable String sessionId,
            @PathVariable String filename) {
        experimentReportService.deleteAttachment(sessionId, filename);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{sessionId}/submit")
    public ResponseEntity<Void> submitReport(@PathVariable String sessionId) {
        experimentreportService.submitReport(sessionId);
        return ResponseEntity.ok().build();
    }
}
