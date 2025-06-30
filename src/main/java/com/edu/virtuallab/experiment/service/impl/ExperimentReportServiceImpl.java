package com.edu.virtuallab.experiment.service.impl;

import com.edu.virtuallab.experiment.dao.ExperimentReportDao;
import com.edu.virtuallab.experiment.model.ExperimentReport;
import com.edu.virtuallab.experiment.service.ExperimentReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Slf4j
@Service
public class ExperimentReportServiceImpl implements ExperimentReportService {

    @Autowired
    private ExperimentReportDao experimentReportDao;

    @Transactional(readOnly = true)
    public ExperimentReport getReportBySession(String sessionId) {
        ExperimentReport report = experimentReportDao.findBySessionId(sessionId);
        return report != null ? report : createDefaultReport(sessionId);
    }

    @Override
    @Transactional
    public void saveReportContent(String sessionId, String manualContent) {
        try {
            log.info("保存报告内容，sessionId: {}, 内容长度: {}", sessionId, manualContent.length());

            int updated = experimentReportDao.updateManualContent(sessionId, manualContent);

            if (updated == 0) {
                log.warn("没有记录被更新，可能sessionId不存在，将创建新报告");
                ExperimentReport report = createDefaultReport(sessionId);
                report.setManualContent(manualContent);
                experimentReportDao.insert(report);
            }

        } catch (Exception e) {
            log.error("保存报告内容失败", e);
            throw new RuntimeException("保存报告内容失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void saveReportAttachment(String sessionId, String attachmentPath) {
        try {
            log.info("保存报告附件，sessionId: {}, 附件路径: {}", sessionId, attachmentPath);

            int updated = experimentReportDao.updateAttachment(sessionId, attachmentPath);

            if (updated == 0) {
                log.warn("没有记录被更新，将创建新报告记录");
                ExperimentReport report = createDefaultReport(sessionId);
                report.setAttachment(attachmentPath);
                experimentReportDao.insert(report);
            }

        } catch (Exception e) {
            log.error("保存报告附件失败", e);
            throw new RuntimeException("保存附件失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void submitReport(String sessionId) {
        try {
            log.info("提交实验报告，sessionId: {}", sessionId);

            int updated = experimentReportDao.submitBySessionId(sessionId);

            if (updated == 0) {
                log.error("提交失败，报告不存在，sessionId: {}", sessionId);
                throw new IllegalArgumentException("报告不存在");
            }

        } catch (Exception e) {
            log.error("提交报告失败", e);
            throw new RuntimeException("提交报告失败: " + e.getMessage());
        }
    }

    // 创建默认报告结构
    private ExperimentReport createDefaultReport(String sessionId) {
        ExperimentReport report = new ExperimentReport();
        report.setSessionId(sessionId);
        report.setStatus("DRAFT");
        report.setPrinciple("");
        report.setPurpose("");
        report.setCategory("");
        report.setMethod("");
        report.setSteps("");
        report.setDescription("");
        report.setManualContent("");
        report.setAttachment(null);
        report.setCreatedAt(new Date());
        report.setUpdatedAt(new Date());
        return report;
    }

    // 确保所有字段不为null
    private void initializeNullFields(ExperimentReport report) {
        if (report.getManualContent() == null) {
            report.setManualContent("");
        }
        if (report.getAttachment() == null) {
            report.setAttachment("[]");
        }
        if (report.getStatus() == null) {
            report.setStatus("DRAFT");
        }
    }
}
