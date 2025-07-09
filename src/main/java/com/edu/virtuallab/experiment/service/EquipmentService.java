package com.edu.virtuallab.experiment.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.virtuallab.experiment.model.Equipment;

public interface EquipmentService extends IService<Equipment> {
    Page<Equipment> queryEquipmentPage(Integer labId, String keyword, Integer pageNum, Integer pageSize);
    Page<Equipment> queryEquipmentByDepartment(String department, String keyword, Integer pageNum, Integer pageSize);
}
