package com.edu.virtuallab.experiment.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.virtuallab.experiment.model.Laboratory;

public interface LaboratoryService extends IService<Laboratory> {
    Page<Laboratory> queryLaboratories(String department, String keyword, int pageNum, int pageSize);
    boolean customDelete(Integer id);
    Laboratory updateLaboratory(Laboratory laboratory);
}