package com.edu.virtuallab.experiment.service.impl;

import com.edu.virtuallab.experiment.model.ExperimentReportTemplate;
import com.edu.virtuallab.experiment.service.ExperimentReportTemplateService;
import com.edu.virtuallab.experiment.dao.ExperimentReportTemplateDao;
import com.edu.virtuallab.experiment.dto.AiTemplateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class ExperimentReportTemplateServiceImpl implements ExperimentReportTemplateService {
    @Autowired
    private ExperimentReportTemplateDao templateDao;

    @Override
    public ExperimentReportTemplate createManualTemplate(ExperimentReportTemplate template) {
        template.setCreateTime(new Date());
        template.setUpdateTime(new Date());
        templateDao.insert(template);
        return template;
    }

    @Override
    public ExperimentReportTemplate createAiTemplate(AiTemplateDTO dto) {
        String aiContent = callBaiduWenxin(dto.getExperimentDesc());
        ExperimentReportTemplate template = new ExperimentReportTemplate();
        template.setExperimentId(dto.getExperimentId());
        template.setTitle(dto.getTitle());
        template.setContent(aiContent);
        template.setCreateTime(new Date());
        template.setUpdateTime(new Date());
        templateDao.insert(template);
        return template;
    }

    // 百度文心一言对接（实际API调用）
    private String callBaiduWenxin(String experimentDesc) {
        try {
            // 百度文心一言API Key
            String apiKey = "bce-v3/ALTAK-k87IEh7RcU9zgpWlb4Eka/25647de14cfb128ec15708065c496cf4025a8279";
            String url = "https://wenxin.baidu.com/moduleApi/ernieBotStream";
            // 构造请求体
            String prompt = "请根据以下实验描述生成一份结构化的实验报告模板：" + experimentDesc;
            String json = "{\"prompt\":\"" + prompt + "\"}";

            java.net.URL apiUrl = new java.net.URL(url);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) apiUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setDoOutput(true);
            try (java.io.OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            }
            int code = conn.getResponseCode();
            if (code == 200) {
                try (java.io.InputStream is = conn.getInputStream();
                     java.util.Scanner scanner = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A")) {
                    String response = scanner.hasNext() ? scanner.next() : "";
                    // 解析返回内容（假设返回json含有"result"字段）
                    int idx = response.indexOf("\"result\":");
                    if (idx != -1) {
                        int start = response.indexOf('"', idx + 9) + 1;
                        int end = response.indexOf('"', start);
                        return response.substring(start, end);
                    }
                    return response;
                }
            } else {
                return "AI生成失败，HTTP状态码:" + code;
            }
        } catch (Exception e) {
            return "AI生成异常: " + e.getMessage();
        }
    }
} 