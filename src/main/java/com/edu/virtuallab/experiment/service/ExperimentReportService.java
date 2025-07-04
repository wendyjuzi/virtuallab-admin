package com.edu.virtuallab.experiment.service;

import com.edu.virtuallab.common.exception.BusinessException;
import com.edu.virtuallab.experiment.model.ExperimentReport;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ExperimentReportService {
    ExperimentReport getReportBySession(String sessionId);
    void saveReportContent(String sessionId, String manualContent);
    void uploadAttachment(String sessionId, MultipartFile file) throws BusinessException, IOException;
    byte[] downloadAttachment(String sessionId, String filename) throws BusinessException;
    void deleteAttachment(String sessionId, String filename) throws BusinessException;
    void submitReport(String sessionId);
    List<ExperimentReport> getReportList(Long studentId);
    List<ExperimentReport> getSubmittedAndGradedReports();
}