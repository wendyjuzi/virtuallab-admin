package com.edu.virtuallab.resource.dao;

import com.edu.virtuallab.resource.model.ResourceInteraction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ResourceInteractionDao {
    int insert(ResourceInteraction interaction);
    int update(ResourceInteraction interaction);
    int delete(Long id);
    ResourceInteraction selectById(Long id);
    List<ResourceInteraction> selectByResourceId(Long resourceId);
    List<ResourceInteraction> selectByUserId(String userId);
    List<ResourceInteraction> selectByType(@Param("resourceId") Long resourceId, @Param("interactionType") String interactionType);
    ResourceInteraction selectByUserAndResource(@Param("resourceId") Long resourceId, @Param("userId") String userId, @Param("interactionType") String interactionType);
}