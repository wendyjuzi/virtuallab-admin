package com.edu.virtuallab.monitor.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface OperationLogMapper {
    int countTodayOperations();
    List<Map<String, Object>> countOperationByHour(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
    int countTodayLoginOperations();
    int countTodayLoginUserCount();
    java.util.List<java.util.Map<String, Object>> countActiveByHourAndDay(java.sql.Timestamp start, java.sql.Timestamp end);
}