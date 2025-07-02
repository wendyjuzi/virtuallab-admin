package com.edu.virtuallab.resource.dao;

import com.edu.virtuallab.resource.model.ResourceShare;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ResourceShareDao {
    int insert(ResourceShare resourceShare);
    int update(ResourceShare resourceShare);
    int delete(Long id);
    ResourceShare selectById(Long id);
    List<ResourceShare> selectByResourceId(Long resourceId);
    List<ResourceShare> selectBySharedWith(String sharedWith);
    List<ResourceShare> selectBySharedBy(String sharedBy);
    ResourceShare selectByResourceAndUser(@Param("resourceId") Long resourceId, @Param("sharedWith") String sharedWith);
}