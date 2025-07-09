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
}
