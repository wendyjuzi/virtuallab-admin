package com.edu.virtuallab.resource.dao;

import com.edu.virtuallab.resource.model.Resource;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResourceDao {
    int insert(Resource resource);
    int update(Resource resource);
    int delete(Long id);
    Resource selectById(Long id);
    List<Resource> selectAll();
} 