package com.edu.virtuallab.experiment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.virtuallab.experiment.model.Equipment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EquipmentMapper extends BaseMapper<Equipment> {
    // 添加通过院系查询设备的方法
    IPage<Equipment> selectByDepartment(Page<Equipment> page,
                                        @Param("department") String department,
                                        @Param("keyword") String keyword);
}
