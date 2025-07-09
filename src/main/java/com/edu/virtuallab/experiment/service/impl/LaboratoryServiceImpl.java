package com.edu.virtuallab.experiment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.virtuallab.experiment.dao.LaboratoryMapper;
import com.edu.virtuallab.experiment.model.Laboratory;
import com.edu.virtuallab.experiment.service.LaboratoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class LaboratoryServiceImpl extends ServiceImpl<LaboratoryMapper, Laboratory> implements LaboratoryService {

    @Override
    public Page<Laboratory> queryLaboratories(String department, String keyword, int pageNum, int pageSize) {
        QueryWrapper<Laboratory> queryWrapper = new QueryWrapper<>();

        // 按部门查询条件
        if (StringUtils.isNotBlank(department)) {
            queryWrapper.eq("department", department);
        }

        // 关键词查询条件
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.and(wrapper -> wrapper
                    .like("lab_name", keyword)
                    .or()
                    .like("location", keyword)
                    .or()
                    .like("description", keyword));
        }

        // 分页查询
        Page<Laboratory> page = new Page<>(pageNum, pageSize);
        return baseMapper.selectPage(page, queryWrapper);
    }
    public boolean customDelete(Integer id) {
        return getBaseMapper().customDeleteById(id) > 0;
    }

    public Laboratory updateLaboratory(Laboratory laboratory) {
        try {
            // 参数校验
            if (laboratory.getLabId() == null) {
                log.error("实验室ID不能为空");
                throw new IllegalArgumentException("实验室ID不能为空");
            }

            // 检查存在性
            Laboratory existingLab = getById(laboratory.getLabId());
            if (existingLab == null) {
                System.out.println("实验室不存在，ID: {}");
                throw new IllegalArgumentException("实验室不存在");
            }

            System.out.println("更新实验室数据，ID: {}，变更内容: {}");

            // 执行更新
            if (!updateById(laboratory)) {
                System.out.println("数据库更新操作失败，ID: {}");
                throw new RuntimeException("更新实验室失败");
            }

            return getById(laboratory.getLabId());
        } catch (Exception e) {
            System.out.println("更新实验室异常，ID: {}，错误: {}");
            throw e;
        }
    }
}
