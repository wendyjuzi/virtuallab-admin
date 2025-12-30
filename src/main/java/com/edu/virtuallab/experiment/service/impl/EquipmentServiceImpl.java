package com.edu.virtuallab.experiment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.virtuallab.experiment.dao.EquipmentMapper;
import com.edu.virtuallab.experiment.model.Equipment;
import com.edu.virtuallab.experiment.service.EquipmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements EquipmentService {

    @Override
    public Page<Equipment> queryEquipmentPage(Integer labId, String keyword, Integer pageNum, Integer pageSize) {
        Page<Equipment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Equipment> queryWrapper = new LambdaQueryWrapper<>();

        if (labId != null) {
            queryWrapper.eq(Equipment::getLabId, labId);
        }

        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.and(wrapper -> wrapper
                    .like(Equipment::getEquipmentName, keyword)
                    .or()
                    .like(Equipment::getModel, keyword)
                    .or()
                    .like(Equipment::getSerialNumber, keyword)
                    .or()
                    .like(Equipment::getManufacturer, keyword)
            );
        }

        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Page<Equipment> queryEquipmentByDepartment(String department, String keyword, Integer pageNum, Integer pageSize) {
        Page<Equipment> page = new Page<>(pageNum, pageSize);
        return (Page<Equipment>) baseMapper.selectByDepartment(page, department, keyword);
    }
}
