package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.common.exception.BusinessException;
import com.edu.virtuallab.experiment.model.ExperimentReport;
import com.edu.virtuallab.experiment.service.ExperimentReportService;
import com.edu.virtuallab.resource.model.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/experiment")
@Slf4j
public class ExperimentReportController {

    private final ExperimentReportService experimentReportService;

    // 构造器注入
    public ExperimentReportController(ExperimentReportService experimentReportService) {
        this.experimentReportService = experimentReportService;
    }

    @Autowired
    private ExperimentReportService experimentreportService;

    // 通过sessionId查询（业务标识）
    @GetMapping("/report/{sessionId}")
    public ResponseEntity<ExperimentReport> getReportBySession(@PathVariable String sessionId) {
        ExperimentReport experimentReport = experimentreportService.getReportBySession(sessionId);
        return ResponseEntity.ok(experimentReport != null ? experimentReport : new ExperimentReport());
    }

    @GetMapping("/students/{studentId}/reports")
    public ResponseEntity<List<ExperimentReport>> getReportList(@PathVariable Long studentId) {
        List<ExperimentReport> reports = experimentReportService.getReportList(studentId);
        return ResponseEntity.ok(reports != null ? reports : Collections.emptyList());
    }

    @GetMapping("/teacher/reports")
    public ResponseEntity<List<ExperimentReport>> getSubmittedAndGradedReports(
            @RequestParam(required = false) List<String> status){
        List<ExperimentReport> reports = experimentReportService.getSubmittedAndGradedReports();
        return ResponseEntity.ok(reports != null ? reports : Collections.emptyList());
    }

    @GetMapping("/report/{sessionId}/details")
    public ResponseEntity<ExperimentReport> getReportDetails(@PathVariable String sessionId) {
        ExperimentReport experimentReport = experimentreportService.getReportBySession(sessionId);
        return ResponseEntity.ok(experimentReport);
    }

    @PostMapping("/report/{sessionId}/save")
    public ResponseEntity<Void> saveContent(
            @PathVariable String sessionId,
            @RequestParam String manualContent) {
        experimentreportService.saveReportContent(sessionId, manualContent);
        return ResponseEntity.ok().build();
    }

    // 上传附件
    @PostMapping("/report/{sessionId}/attachments")
    public ResponseEntity<String> uploadAttachment(
            @PathVariable String sessionId,
            @RequestParam("file") MultipartFile file) {
        try {
            experimentReportService.uploadAttachment(sessionId, file);
            return ResponseEntity.ok("附件上传成功");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("文件处理错误: " + e.getMessage());
        }
    }

//    // 下载附件
//    @GetMapping("/report/{sessionId}/attachments/{filename}")
//    public ResponseEntity<byte[]> downloadAttachment(@PathVariable String sessionId){
//        ExperimentReport experimentReport = experimentReportService.getReportBySession(sessionId);
//
//        if(experimentReport == null || !experimentReport.hasAttachment()){
//            throw new RuntimeException("附件不存在");
//        }
//
//        try{
//            Path filePath = Path.get(uploadDir).resolve(experimentReport.getAttachmentPath().replace("/upload/",""));
//            Resource resource = new UrlResource(filePath.toUri());
//
//            if(!resource.exists() || !resource.isReadable()){
//                throw new RuntimeException("无法读取附件");
//            }
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION,
//                            "attachment; filename=\"" + experimentReport.getDownloadFilename() + "\"")
//                    .contentType(MediaType.parseMediaType(report.getMimeType()))
//                    .body(resource);
//        } catch (Exception e) {
//            throw new RuntimeException("下载失败: " + e.getMessage(), e);
//        }
//    }

    // 删除附件
    @DeleteMapping("/report/{sessionId}/attachments/{filename}")
    public ResponseEntity<Void> deleteAttachment(
            @PathVariable String sessionId,
            @PathVariable String filename) {
        experimentReportService.deleteAttachment(sessionId, filename);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/report/{sessionId}/submit")
    public ResponseEntity<Void> submitReport(@PathVariable String sessionId) {
        experimentreportService.submitReport(sessionId);
        return ResponseEntity.ok().build();
    }
}
