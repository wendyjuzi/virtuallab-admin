package com.edu.virtuallab.experiment.service.impl;

import com.edu.virtuallab.common.exception.BusinessException;
import com.edu.virtuallab.experiment.dao.ExperimentReportDao;
import com.edu.virtuallab.experiment.model.ExperimentReport;
import com.edu.virtuallab.experiment.service.ExperimentReportService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;


@Slf4j
@Service
public class ExperimentReportServiceImpl implements ExperimentReportService {

    @Autowired
    private ExperimentReportDao experimentReportDao;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional(readOnly = true)
    public ExperimentReport getReportBySession(String sessionId) {
        ExperimentReport report = experimentReportDao.findBySessionId(sessionId);
        return report != null ? report : createDefaultReport(sessionId);
    }

    @Override
    @Transactional
    public ExperimentReport saveReportContent(String sessionId, String manualContent, ExperimentReport.Status status) {
        try {
            log.info("保存报告内容，sessionId: {}, 状态: {}", sessionId, status);

            int updated = experimentReportDao.updateManualContent(
                    sessionId,
                    manualContent,
                    status
            );

            ExperimentReport report;
            if (updated == 0) {
                log.warn("没有记录被更新，可能sessionId不存在，将创建新报告");
                report = createDefaultReport(sessionId);
                report.setManualContent(manualContent);
                report.setStatus(status);
                experimentReportDao.insert(report);
            }else{
                // 获取更新后的报告
                report = experimentReportDao.findBySessionId(sessionId);
                report.setStatus(status); // 确保状态正确
            }

            return report;

        } catch (Exception e) {
            log.error("保存报告内容失败", e);
            throw new RuntimeException("保存报告内容失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ExperimentReport submitReport(String sessionId, ExperimentReport.Status status) {
        // 1. 先查询当前报告状态
        ExperimentReport report = experimentReportDao.findBySessionId(sessionId);
        if (report == null) {
            throw new IllegalArgumentException("报告不存在，sessionId: " + sessionId);
        }

        // 3. 验证目标状态必须是SUBMITTED
        if (status != ExperimentReport.Status.SUBMITTED) {
            throw new IllegalArgumentException("提交操作只能将状态改为SUBMITTED");
        }

        // 4. 更新状态为 SUBMITTED
        int updated = experimentReportDao.submitBySessionId(
                sessionId,
                ExperimentReport.Status.SUBMITTED);

        ExperimentReport report1;
        if (updated == 0) {
            log.warn("没有记录被更新，可能sessionId不存在，将创建新报告");
            report1 = createDefaultReport(sessionId);
            report1.setStatus(status);
            experimentReportDao.insert(report1);
        }else{
            // 获取更新后的报告
            report1 = experimentReportDao.findBySessionId(sessionId);
            report1.setStatus(status); // 确保状态正确
        }

        // 5. 返回更新后的报告
        ExperimentReport updatedReport = experimentReportDao.findBySessionId(sessionId);
        log.info("报告提交成功，sessionId: {}, 新状态: {}", sessionId, status);

        return updatedReport;
    }


    @Override
    @Transactional
    public ExperimentReport gradeReport(String sessionId, ExperimentReport.Status status, String comment, BigDecimal score){

        // 更新状态为 GRADED
        int updated = experimentReportDao.gradeBySessionId(
                sessionId,
                ExperimentReport.Status.GRADED,
                comment,
                score);

        ExperimentReport report2;
        if (updated == 0) {
            log.warn("没有记录被更新，可能sessionId不存在，将创建新报告");
            report2 = createDefaultReport(sessionId);
            report2.setStatus(status);
            experimentReportDao.insert(report2);
        }else{
            // 获取更新后的报告
            report2 = experimentReportDao.findBySessionId(sessionId);
            report2.setStatus(status); // 确保状态正确
        }

        // 5. 返回更新后的报告
        ExperimentReport updatedReport = experimentReportDao.findBySessionId(sessionId);
        log.info("报告提交成功，sessionId: {}, 新状态: {}", sessionId, status);

        return updatedReport;
    }


    @Override
    @Transactional
    public void uploadAttachment(String sessionId, MultipartFile file) throws BusinessException,IOException {

        // 验证文件大小和类型
        validateFile(file);

        ExperimentReport experimentReport = experimentReportDao.findBySessionId(sessionId);

        try {
            // 实现文件存储逻辑
            FileInfo fileInfo = storeFile(file);

            //更新报告附件信息
            experimentReport.setAttachmentPath(fileInfo.getStoredPath());
            experimentReport.setOriginalFilename(file.getOriginalFilename());
            experimentReport.setFileSize(file.getSize());
            experimentReport.setMimeType(file.getContentType());
            experimentReport.setUpdatedAt(new Date());

            experimentReportDao.updateById(experimentReport);
        }catch(IOException e){
            throw new RuntimeException("文件上传失败:" + e.getMessage(), e);
        }
    }

    private void validateFile(MultipartFile file) throws BusinessException {

        // 限制文件大小 (例如10MB)
        long maxFileSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxFileSize) {
            throw new BusinessException("文件大小不能超过10MB");
        }

        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !isAllowedContentType(contentType)) {
            throw new BusinessException("不支持的文件类型");
        }
    }

    private boolean isAllowedContentType(String contentType) {
        // 允许的文件类型列表
        Set<String> allowedTypes = Set.of(
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "image/jpeg",
                "image/png",
                "text/plain"
        );
        return allowedTypes.contains(contentType);
    }

    private FileInfo storeFile(MultipartFile file) throws IOException{
        // 确保上传目录存在
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名，防止冲突
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String storedFilename = UUID.randomUUID() + fileExtension;
        String storedPath = "/uploads/" + storedFilename;

        // 存储文件
        Path destination = uploadPath.resolve(storedFilename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return new FileInfo(
                originalFilename,
                storedPath,
                file.getSize(),
                file.getContentType()
        );
    }

    // 辅助类
    @Data
    @AllArgsConstructor
    private static class FileInfo {
        private String originalFilename;
        private String storedPath;
        private long size;
        private String mimeType;
    }

    // 文件删除
    @Override
    @Transactional
    public void deleteAttachment(String sessionId) throws BusinessException{
        ExperimentReport experimentReport = experimentReportDao.findBySessionId(sessionId);
        if( experimentReport == null || !experimentReport.hasAttachment()){
            throw new BusinessException("附件不存在");
        }

        // 清楚数据库中的附件信息
        experimentReport.setAttachmentPath(null);
        experimentReport.setOriginalFilename(null);
        experimentReport.setFileSize(null);
        experimentReport.setMimeType(null);
        experimentReport.setUpdatedAt(new Date());

        experimentReportDao.deleteAttachment(sessionId);
    }

    // 创建默认报告结构
    private ExperimentReport createDefaultReport(String sessionId) {
        ExperimentReport report = new ExperimentReport();
        report.setSessionId(sessionId);
        report.setStatus(ExperimentReport.Status.DRAFT);
        report.setPrinciple("");
        report.setPurpose("");
        report.setCategory("");
        report.setMethod("");
        report.setSteps("");
        report.setDescription("");
        report.setManualContent("");
        report.setCreatedAt(new Date());
        report.setUpdatedAt(new Date());
        return report;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentReport> getReportList(Long studentId){
        try {
            log.info("获取学生报告列表，studentId: {}", studentId);
            return experimentReportDao.findByStudentId(studentId);
        } catch (Exception e) {
            log.error("获取报告列表失败", e);
            throw new RuntimeException("获取报告列表失败: " + e.getMessage());
        }
    }

    @Override
    public List<ExperimentReport> getSubmittedAndGradedReports(){
        return experimentReportDao.findSubmittedAndGradedReports();
    }

    public boolean updateManualScore(String sessionId, BigDecimal score, String comment) {
        int updated = experimentReportDao.updateManualScore(sessionId, score, comment);
        return updated > 0;
    }
    @Override
    public ExperimentReport getManualScore(String sessionId) {
        return experimentReportDao.getManualScoreBySessionId(sessionId);
    }
    @Override
    public boolean deleteManualScore(String sessionId) {
        return experimentReportDao.deleteManualScore(sessionId);
    }
}
