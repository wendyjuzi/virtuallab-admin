package com.edu.virtuallab.progress.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.virtuallab.progress.entity.StudentProjectProgress;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProgressMapper extends BaseMapper<StudentProjectProgress> {
    // 如果需要复杂查询，可以在这里写自定义方法
}
