package com.edu.virtuallab.resource.dao;

import com.edu.virtuallab.resource.model.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ResourceDao {
    int insert(Resource resource);
    int update(Resource resource);
    int delete(Long id);
    Resource selectById(Long id);
    List<Resource> selectAll();
    
    // 新增查询方法
    List<Resource> selectByType(@Param("type") String type);
    List<Resource> selectByCategory(@Param("category") String category);
    List<Resource> selectByStatus(@Param("status") String status);
    List<Resource> selectByUploader(@Param("uploader") String uploader);
    List<Resource> searchByName(@Param("name") String name);
    List<Resource> selectByTypeAndCategory(@Param("type") String type, @Param("category") String category);
} 