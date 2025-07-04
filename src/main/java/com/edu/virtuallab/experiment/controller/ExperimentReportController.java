package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.common.exception.BusinessException;
import com.edu.virtuallab.experiment.model.ExperimentReport;
import com.edu.virtuallab.experiment.service.ExperimentReportService;
import com.edu.virtuallab.resource.model.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import com.edu.virtuallab.log.annotation.OperationLogRecord;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
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
            @RequestParam(required = false) List<String> status) {
        List<ExperimentReport> reports = experimentReportService.getSubmittedAndGradedReports();
        return ResponseEntity.ok(reports != null ? reports : Collections.emptyList());
    }

    @GetMapping("/report/{sessionId}/details")
    public ResponseEntity<ExperimentReport> getReportDetails(@PathVariable String sessionId) {
        ExperimentReport experimentReport = experimentreportService.getReportBySession(sessionId);
        return ResponseEntity.ok(experimentReport);
    }

    @OperationLogRecord(operation = "CREATE_EXPERIMENT_REPORT", module = "EXPERIMENT", action = "创建实验报告", description = "用户创建实验报告", permissionCode = "EXPERIMENT_MANAGE")
    @PostMapping("/report/{sessionId}/save")
    public ResponseEntity<ExperimentReport> saveContent(
            @PathVariable String sessionId,
            @RequestParam String manualContent,
            @RequestParam ExperimentReport.Status status) {
        ExperimentReport report = experimentReportService.saveReportContent(
                sessionId,
                manualContent,
                status
        );
        return ResponseEntity.ok(report);
    }


    @OperationLogRecord(operation = "SUBMIT_EXPERIMENT_REPORT", module = "EXPERIMENT", action = "提交实验报告", description = "用户提交实验报告", permissionCode = "EXPERIMENT_MANAGE")
    @PostMapping("/report/{sessionId}/submit")
    public ResponseEntity<ExperimentReport> submitReport(
            @PathVariable String sessionId,
            @RequestParam ExperimentReport.Status status) {
        ExperimentReport report = experimentReportService.submitReport(
                sessionId,
                status
        );
        return ResponseEntity.ok(report);
    }


    // 上传附件
    @OperationLogRecord(operation = "UPLOAD_EXPERIMENT_REPORT_ATTACHMENT", module = "EXPERIMENT", action = "上传实验报告附件", description = "用户上传实验报告附件", permissionCode = "EXPERIMENT_MANAGE")
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

    // 下载附件
    @GetMapping("/report/{sessionId}/attachments/{filename}")
    public ResponseEntity<byte[]> downloadAttachment(
            @PathVariable String sessionId,
            @PathVariable String filename) {
        try {
            ExperimentReport report = experimentReportService.getReportBySession(sessionId);
            byte[] fileContent = experimentReportService.downloadAttachment(sessionId, filename);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + report.getDownloadFilename() + "\"")
                    .contentType(MediaType.parseMediaType(report.getMimeType()))
                    .body(fileContent);
        } catch (BusinessException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 删除附件
    @OperationLogRecord(operation = "DELETE_EXPERIMENT_REPORT_ATTACHMENT", module = "EXPERIMENT", action = "删除实验报告附件", description = "用户删除实验报告附件", permissionCode = "EXPERIMENT_MANAGE")
    @DeleteMapping("/report/{sessionId}/attachments/{filename}")
    public ResponseEntity<Void> deleteAttachment(
            @PathVariable String sessionId,
            @PathVariable String filename) {
        try {
            experimentReportService.deleteAttachment(sessionId, filename);
            return ResponseEntity.ok().build();
        } catch (BusinessException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
