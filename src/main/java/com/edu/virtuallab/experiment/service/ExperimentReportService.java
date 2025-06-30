package com.edu.virtuallab.experiment.service;

import com.edu.virtuallab.experiment.model.ExperimentReport;

public interface ExperimentReportService {
    ExperimentReport getReportBySession(String sessionId);
    void saveReportContent(String sessionId, String manualContent);
    void saveReportAttachment(String sessionId, String attachmentPath);
    void submitReport(String sessionId);

}