package com.edu.virtuallab.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    List<Map<String, Object>> countUserByRole();
    List<Map<String, Object>> countUserByRegion();
}