package com.edu.virtuallab.log.dao;

import com.edu.virtuallab.log.model.OperationLog;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogDao {
    List<OperationLog> findByUserId(Long userId);
    int insert(OperationLog log);
    List<OperationLog> findAll();
} 