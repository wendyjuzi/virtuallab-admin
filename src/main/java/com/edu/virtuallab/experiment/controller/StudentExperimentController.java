package com.edu.virtuallab.experiment.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.virtuallab.auth.dao.UserDao;
import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.experiment.dto.ExperimentStatsResponse;
import com.edu.virtuallab.experiment.dto.StudentExperimentProjectDTO;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import com.edu.virtuallab.experiment.model.ExperimentReport;
import com.edu.virtuallab.experiment.service.ExperimentProjectService;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.experiment.service.ExperimentReportService;
import com.edu.virtuallab.experiment.service.impl.ExperimentReportServiceImpl;
import com.sun.tools.jconsole.JConsoleContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student/experiments")
@Api(tags = "学生实验管理")
public class StudentExperimentController {

    private final ExperimentProjectService projectService;
    private final UserDao userDao;
    private final ExperimentReportService reportService;

    @Autowired
    public StudentExperimentController(ExperimentProjectService projectService, UserDao userDao, ExperimentReportService reportService) {
        this.projectService = projectService;
        this.userDao = userDao;
        this.reportService = reportService;
    }

    @ApiOperation("获取学生实验项目列表（分页 + 统计）")
    @GetMapping
    public CommonResult<ExperimentStatsResponse> getExperimentStats(
            @RequestParam Long userId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            // 1. 根据userId查询用户信息
            User user = userDao.findById(userId);
            if (user == null) {
                return CommonResult.failed("用户不存在");
            }
            // 2. 获取studentId（学号）
            String studentId = user.getStudentId();
            if (studentId == null || studentId.isEmpty()) {
                return CommonResult.failed("用户未关联学号");
            }

            // 3.获取分页数据
            Page<StudentExperimentProjectDTO> page =
                    projectService.getProjectsByStudentId(Long.valueOf(studentId), keyword, pageNum, pageSize);

            // 4.计算平均数
            Double averageScore = calculateAverageScore(Long.valueOf(studentId));

            // 创建响应对象
            ExperimentStatsResponse response = new ExperimentStatsResponse();
            response.setPageData(page);
            response.setTotalExperiments(page.getTotal());
            response.setCompletedExperiments(
                    page.getRecords().stream()
                            .filter(dto -> "completed".equals(dto.getProgressStatus()))
                            .count()
            );
            response.setAverageScore(averageScore != null ?
                    Math.round(averageScore * 100) / 100.0 : 0.0); // 保留两位小数

            return CommonResult.success(response, "获取学生实验项目成功");
        } catch (Exception e) {
            return CommonResult.failed("获取学生实验项目失败: " + e.getMessage());
        }
    }

    private Double calculateAverageScore(Long studentId) {
        try {
            return reportService.calculateAverageScore(studentId);
        } catch (Exception e) {
            return null; // 计算失败返回null，外层会处理为0.0
        }
    }
}


