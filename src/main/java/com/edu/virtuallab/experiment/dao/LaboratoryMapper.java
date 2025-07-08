package com.edu.virtuallab.experiment.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.virtuallab.experiment.model.Laboratory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("laboratories") // 明确指定表名
public interface LaboratoryMapper extends BaseMapper<Laboratory> {
}
