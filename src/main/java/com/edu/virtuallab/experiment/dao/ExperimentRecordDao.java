package com.edu.virtuallab.experiment.dao;

import com.edu.virtuallab.experiment.model.ExperimentRecord;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExperimentRecordDao {
    int insert(ExperimentRecord record);
    int update(ExperimentRecord record);
    int delete(Long id);
    ExperimentRecord selectById(Long id);
    List<ExperimentRecord> selectAll();
} 