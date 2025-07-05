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
    byte[] downloadAttachment(String sessionId, String filename) throws BusinessException;
    void deleteAttachment(String sessionId, String filename) throws BusinessException;
    ExperimentReport submitReport(String sessionId, ExperimentReport.Status status);
    List<ExperimentReport> getReportList(Long studentId);
    List<ExperimentReport> getSubmittedAndGradedReports();
    boolean updateManualScore(Long sessionId, BigDecimal score, String comment);

//
//    List<ExperimentReport> listAll();
//
//    ExperimentReport getById(Long id);
//
//    int create(ExperimentReport report);
//
//    int delete(Long id);

//    int update(ExperimentReport report);
}