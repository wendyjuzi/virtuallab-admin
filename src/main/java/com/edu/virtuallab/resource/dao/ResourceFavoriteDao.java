package com.edu.virtuallab.resource.dao;

import com.edu.virtuallab.resource.model.ResourceFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ResourceFavoriteDao {
    int insert(ResourceFavorite resourceFavorite);
    int deleteById(@Param("id") Long id);
    ResourceFavorite selectById(@Param("id") Long id);
    List<ResourceFavorite> selectByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("size") int size);
    int countByUserId(@Param("userId") Long userId);
    int updateById(ResourceFavorite resourceFavorite);
    ResourceFavorite selectByUserAndResource(@Param("userId") Long userId, @Param("resourceId") Long resourceId);
}
