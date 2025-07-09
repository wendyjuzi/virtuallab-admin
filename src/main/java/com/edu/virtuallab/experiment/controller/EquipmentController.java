package com.edu.virtuallab.experiment.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.experiment.model.Equipment;
import com.edu.virtuallab.experiment.service.EquipmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipment")
@Api(tags = "设备管理")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @ApiOperation("创建设备")
    @PostMapping
    public CommonResult<Equipment> createEquipment(@RequestBody Equipment equipment) {
        boolean success = equipmentService.save(equipment);
        return success ? CommonResult.success(equipment, "设备创建成功") :
                CommonResult.failed("创建设备失败");
    }

    @ApiOperation("更新设备")
    @PutMapping("/{id}")
    public CommonResult<Equipment> updateEquipment(@PathVariable Integer id, @RequestBody Equipment equipment) {
        equipment.setEquipmentId(id);
        boolean success = equipmentService.updateById(equipment);
        return success ? CommonResult.success(equipment, "设备更新成功") :
                CommonResult.failed("更新设备失败");
    }

    @ApiOperation("删除设备")
    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteEquipment(@PathVariable Integer id) {
        boolean success = equipmentService.removeById(id);
        return success ? CommonResult.success(null, "设备删除成功") :
                CommonResult.failed("删除设备失败");
    }

    @ApiOperation("获取设备详情")
    @GetMapping("/{id}")
    public CommonResult<Equipment> getEquipmentById(@PathVariable Integer id) {
        Equipment equipment = equipmentService.getById(id);
        return equipment != null ? CommonResult.success(equipment, "获取设备详情成功") :
                CommonResult.failed("设备不存在");
    }

    @ApiOperation("分页查询设备")
    @GetMapping("/page")
    public CommonResult<Page<Equipment>> queryEquipmentPage(
            @RequestParam(required = false) Integer labId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Equipment> page = equipmentService.queryEquipmentPage(labId, keyword, pageNum, pageSize);
        return CommonResult.success(page, "获取设备列表成功");
    }

    @ApiOperation("通过院系分页查询设备")
    @GetMapping("/department/page")
    public CommonResult<Page<Equipment>> queryEquipmentByDepartment(
            @RequestParam String department,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        if (StringUtils.isEmpty(department)) {
            return CommonResult.failed("院系参数不能为空");
        }
        Page<Equipment> page = equipmentService.queryEquipmentByDepartment(department, keyword, pageNum, pageSize);
        return CommonResult.success(page, "获取设备列表成功");
    }
}
