package com.edu.virtuallab.resource.dao;

import com.edu.virtuallab.resource.model.ResourceLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ResourceLikeDao {
    int insert(ResourceLike resourceLike);
    int deleteById(@Param("id") Long id);
    ResourceLike selectById(@Param("id") Long id);
    List<ResourceLike> selectByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("size") int size);
    int countByUserId(@Param("userId") Long userId);
    int updateById(ResourceLike resourceLike);
    ResourceLike selectByUserAndResource(@Param("userId") Long userId, @Param("resourceId") Long resourceId);
    int countByUserIdAndResourceId(Long userId, Long resourceId);
    int insertByUserAndResource(Long userId, Long resourceId);
    int delete(Long userId, Long resourceId);
}