package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.experiment.model.ExperimentReportTemplate;
import com.edu.virtuallab.experiment.service.ExperimentReportTemplateService;
import com.edu.virtuallab.experiment.dto.AiTemplateDTO;
import com.edu.virtuallab.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/experiment/report-template")
public class ExperimentReportTemplateController {

    @Autowired
    private ExperimentReportTemplateService templateService;

    // 手动生成实验报告模板
    @PostMapping("/manual")
    public CommonResult<ExperimentReportTemplate> manualTemplate(@RequestBody ExperimentReportTemplate template) {
        ExperimentReportTemplate saved = templateService.createManualTemplate(template);
        return CommonResult.success(saved, "手动生成实验报告模板成功");
    }

    // AI自动生成实验报告模板
    @PostMapping("/ai")
    public CommonResult<ExperimentReportTemplate> aiTemplate(@RequestBody AiTemplateDTO dto) {
        ExperimentReportTemplate saved = templateService.createAiTemplate(dto);
        return CommonResult.success(saved, "AI自动生成实验报告模板成功");
    }
} 