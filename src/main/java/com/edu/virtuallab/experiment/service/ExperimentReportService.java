package com.edu.virtuallab.experiment.service;

import com.edu.virtuallab.common.exception.BusinessException;
import com.edu.virtuallab.experiment.model.ExperimentReport;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ExperimentReportService {
    ExperimentReport getReportBySession(String sessionId);
    ExperimentReport saveReportContent(String sessionId, String manualContent, ExperimentReport.Status status);
    void uploadAttachment(String sessionId, MultipartFile file) throws BusinessException, IOException;
    void deleteAttachment(String sessionId) throws BusinessException;
    ExperimentReport submitReport(String sessionId, ExperimentReport.Status status);
    List<ExperimentReport> getReportList(Long studentId);
    List<ExperimentReport> getSubmittedAndGradedReports();
    boolean updateManualScore(String sessionId, BigDecimal score, String comment);
    ExperimentReport getManualScore(String sessionId);
    boolean deleteManualScore(String sessionId);
    Double calculateAverageScore(Long studentId);

//
//    List<ExperimentReport> listAll();
//
//    ExperimentReport getById(Long id);
//
//    int create(ExperimentReport report);
//
//    int delete(Long id);

//    int update(ExperimentReport report);
//    ExperimentReport gradeReport(String sessionId, ExperimentReport.Status status, String comment, BigDecimal score);
//    List<ExperimentReport> getReportByProject(Long projectId);
}