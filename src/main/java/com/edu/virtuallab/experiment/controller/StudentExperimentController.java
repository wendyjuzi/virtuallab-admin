package com.edu.virtuallab.experiment.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.virtuallab.experiment.dto.StudentExperimentProjectDTO;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import com.edu.virtuallab.experiment.service.ExperimentProjectService;
import com.edu.virtuallab.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/student/experiments")
@Api(tags = "学生实验管理")
public class StudentExperimentController {

    private final ExperimentProjectService projectService;

    @Autowired
    public StudentExperimentController(ExperimentProjectService projectService) {
        this.projectService = projectService;
    }

    @ApiOperation("获取学生实验项目列表（分页）")
    @GetMapping
    public CommonResult<Page<StudentExperimentProjectDTO>> getMyExperiments(
            @RequestParam Long userId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        try {
            Page<StudentExperimentProjectDTO> page =
                    projectService.getProjectsByStudentId(userId, keyword, pageNum, pageSize);
            return CommonResult.success(page, "获取实验项目成功");
        } catch (Exception e) {
            return CommonResult.failed("获取实验项目失败: " + e.getMessage());
        }
    }
}
