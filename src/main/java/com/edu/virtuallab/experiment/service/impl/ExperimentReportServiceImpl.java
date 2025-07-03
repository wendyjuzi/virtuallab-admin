package com.edu.virtuallab.experiment.service.impl;

import com.edu.virtuallab.common.exception.BusinessException;
import com.edu.virtuallab.experiment.dao.ExperimentReportDao;
import com.edu.virtuallab.experiment.model.ExperimentReport;
import com.edu.virtuallab.experiment.service.ExperimentReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;


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
    public void uploadAttachment(String sessionId, MultipartFile file) throws IOException {
        ExperimentReport experimentReport = experimentReportDao.findBySessionId(sessionId);

        try {
            // 实现文件存储逻辑
            String filename = storeFile(file);

            //更新报告附件信息
            experimentReport.setAttachmentPath("/uploads/" + filename);
            experimentReport.setOriginalFilename(file.getOriginalFilename());
            experimentReport.setFileSize(file.getSize());
            experimentReport.setMimeType(file.getContentType());
            experimentReport.setUpdatedAt(new Date());

            experimentReportDao.updateById(experimentReport);
        }catch(IOException e){
            throw new RuntimeException("文件上传失败:" + e.getMessage(), e);
        }
    }

    private String storeFile(MultipartFile file) throws IOException{
        // 确保上传目录存在
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名，防止冲突
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String storedFilename = UUID.randomUUID() + fileExtension;

        // 存储文件
        Path destination = uploadPath.resolve(storedFilename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return storedFilename;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] downloadAttachment(String sessionId, String filename) throws BusinessException {
        ExperimentReport experimentReport = experimentReportDao.findBySessionId(sessionId);

        if(experimentReport == null || !experimentReport.hasAttachment()){
            throw new BusinessException("附件不存在");
        }

        try{
            Path filePath = Paths.get(uploadDir).resolve(
                    experimentReport.getAttachmentPath().replace("/uploads/"," "));
                    return Files.readAllBytes(filePath);
            }catch (IOException e){
                log.error("下载附件失败", e);
                throw new BusinessException("下载附件失败:" + e.getMessage());
            }
    }

    @Override
    @Transactional
    public void deleteAttachment(String sessionId, String filename) throws BusinessException{
        ExperimentReport experimentReport = experimentReportDao.findBySessionId(sessionId);
        if( experimentReport == null || !experimentReport.hasAttachment()){
            throw new BusinessException("附件不存在");
        }

        try{
            // 删除文件系统中的文件
            deleteAttachment(experimentReport.getAttachmentPath());

            // 清楚数据库中的附件信息
            experimentReport.setAttachmentPath(null);
            experimentReport.setOriginalFilename(null);
            experimentReport.setFileSize(null);
            experimentReport.setMimeType(null);
            experimentReport.setUpdatedAt(new Date());

            experimentReportDao.updateById(experimentReport);
        }catch (IOException e) {
            throw new BusinessException("删除附件失败: " + e.getMessage());
        }
    }

    private void deleteAttachment(String filePath) throws IOException {
        if (filePath != null && !filePath.isEmpty()) {
            Path path = Paths.get(uploadDir).resolve(filePath.replace("/uploads/", ""));
            Files.deleteIfExists(path);
        }
    }

    @Override
    @Transactional
    public void submitReport(String sessionId) {
        // 1. 先查询当前报告状态
        ExperimentReport report = experimentReportDao.findBySessionId(sessionId);
        if (report == null) {
            throw new IllegalArgumentException("报告不存在，sessionId: " + sessionId);
        }

        // 2. 只有草稿状态的报告允许提交
        if (!"DRAFT".equals(report.getStatus())) {
            throw new IllegalStateException("报告已提交或锁定，无法重复提交");
        }

        // 3. 更新状态为 SUBMITTED
        int updated = experimentReportDao.submitBySessionId(sessionId);
        if (updated == 0) {
            throw new RuntimeException("提交失败，请重试");
        }
        log.info("报告提交成功，sessionId: {}", sessionId);
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
}
