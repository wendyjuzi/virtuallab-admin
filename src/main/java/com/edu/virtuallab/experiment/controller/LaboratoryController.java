package com.edu.virtuallab.experiment.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.experiment.model.Laboratory;
import com.edu.virtuallab.experiment.service.LaboratoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/laboratories")
@Api(tags = "实验室管理")
public class LaboratoryController {

    @Autowired
    private LaboratoryService laboratoryService;

    @ApiOperation("新增实验室")
    @PostMapping
    public CommonResult<Laboratory> addLaboratory(@RequestBody Laboratory laboratory) {
        boolean success = laboratoryService.save(laboratory);
        return success ? CommonResult.success(laboratory, "实验室创建成功") :
                CommonResult.failed("创建实验室失败");
    }

    @ApiOperation("删除实验室")
    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteLaboratory(@PathVariable Integer id) {
        boolean success = laboratoryService.customDelete(id);
        return success ? CommonResult.success(null, "实验室删除成功") :
                CommonResult.failed("删除实验室失败");
    }

    @ApiOperation("更新实验室")
    @PutMapping
    public CommonResult<Laboratory> updateLaboratory(@RequestBody @Valid Laboratory laboratory) {
        // 使用服务层方法处理完整业务逻辑
        try {
            Laboratory updatedLab = laboratoryService.updateLaboratory(laboratory);
            return CommonResult.success(updatedLab, "实验室更新成功");
        } catch (IllegalArgumentException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            System.out.println("更新实验室异常");
            return CommonResult.failed("系统错误，更新失败");
        }
    }

    @ApiOperation("获取实验室详情")
    @GetMapping("/{id}")
    public CommonResult<Laboratory> getLaboratory(@PathVariable Integer id) {
        Laboratory laboratory = laboratoryService.getById(id);
        return laboratory != null ? CommonResult.success(laboratory, "获取实验室详情成功") :
                CommonResult.failed("实验室不存在");
    }

    @ApiOperation("分页查询实验室")
    @GetMapping
    public CommonResult<Page<Laboratory>> queryLaboratories(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<Laboratory> page = laboratoryService.queryLaboratories(department, keyword, pageNum, pageSize);
        return CommonResult.success(page, "获取实验室列表成功");
    }
}
