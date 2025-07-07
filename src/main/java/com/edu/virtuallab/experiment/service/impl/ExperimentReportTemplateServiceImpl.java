package com.edu.virtuallab.experiment.service.impl;

import com.edu.virtuallab.experiment.dao.ExperimentReportTemplateDao;
import com.edu.virtuallab.experiment.model.ExperimentReportTemplate;
import com.edu.virtuallab.experiment.service.ExperimentReportTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExperimentReportTemplateServiceImpl implements ExperimentReportTemplateService {
    @Autowired
    private ExperimentReportTemplateDao templateDao;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private com.edu.virtuallab.experiment.dao.ExperimentProjectDao experimentProjectDao;

    @Override
    public ExperimentReportTemplate aiGenerateTemplate(Long experimentProjectId) {
        com.edu.virtuallab.experiment.model.ExperimentProject project = experimentProjectDao.findById(experimentProjectId);
        if (project == null) throw new RuntimeException("实验项目不存在");
        String prompt = "请根据以下实验项目信息生成一份标准实验报告模板：\n"
            + "实验名称：" + project.getName() + "\n"
            + "实验类别：" + project.getCategory() + "\n"
            + "实验描述：" + project.getDescription() + "\n"
            + "实验难度：" + project.getLevel() + "\n"
            + "协作类型：" + project.getProjectType() + "\n"
            + "请输出包含实验目的、实验原理、实验步骤、注意事项、实验结论等结构化内容的模板。";
        Map<String, String> req = new HashMap<>();
        req.put("message", prompt);
        Map result = restTemplate.postForObject("http://localhost:8080/ai/chat", req, Map.class);
        String content = result != null ? (String) result.get("reply") : "AI生成失败";
        ExperimentReportTemplate template = new ExperimentReportTemplate();
        template.setExperimentId(experimentProjectId);
        template.setTitle(project.getName() + " 实验报告模板");
        template.setContent(content);
        template.setCreateTime(new Date());
        template.setUpdateTime(new Date());
        templateDao.insert(template);
        return template;
    }

    @Override
    public int create(ExperimentReportTemplate template) {
        template.setCreateTime(new Date());
        template.setUpdateTime(new Date());
        return templateDao.insert(template);
    }

    @Override
    public int update(ExperimentReportTemplate template) {
        template.setUpdateTime(new Date());
        return templateDao.update(template);
    }

    @Override
    public int delete(Long id) {
        return templateDao.deleteById(id);
    }

    @Override
    public ExperimentReportTemplate getById(Long id) {
        return templateDao.findById(id);
    }

    @Override
    public List<ExperimentReportTemplate> getByExperimentId(Long experimentId) {
        return templateDao.findByExperimentId(experimentId);
    }

    @Override
    public List<ExperimentReportTemplate> getAll() {
        return templateDao.findAll();
    }
} 