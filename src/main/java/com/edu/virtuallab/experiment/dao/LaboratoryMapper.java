package com.edu.virtuallab.experiment.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.virtuallab.experiment.model.Laboratory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LaboratoryMapper extends BaseMapper<Laboratory> {
    Laboratory selectById(Integer id);
    // 自定义删除方法
    int customDeleteById(@Param("id") Integer id);

    int updateById(@Param("laboratory") Laboratory laboratory);

}
