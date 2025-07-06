package com.edu.virtuallab.log.dao;

import com.edu.virtuallab.log.model.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface OperationLogSimpleDao {
    int insert(OperationLog log);
    List<OperationLog> selectAll();
} 