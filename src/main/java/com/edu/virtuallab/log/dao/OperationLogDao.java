package com.edu.virtuallab.log.dao;

import com.edu.virtuallab.log.model.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OperationLogDao {
    List<OperationLog> findByUserId(Long userId);
    int insert(OperationLog log);
    List<OperationLog> findAll();
    
    // 根据条件查询操作日志
    List<OperationLog> findByConditions(@Param("username") String username,
                                       @Param("operation") String operation,
                                       @Param("module") String module,
                                       @Param("startTime") String startTime,
                                       @Param("endTime") String endTime,
                                       @Param("offset") int offset,
                                       @Param("size") int size);
    
    // 根据条件统计操作日志数量
    int countByConditions(@Param("username") String username,
                         @Param("operation") String operation,
                         @Param("module") String module,
                         @Param("startTime") String startTime,
                         @Param("endTime") String endTime);
} 