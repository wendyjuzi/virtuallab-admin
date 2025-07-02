package com.edu.virtuallab.resource.dao;

import com.edu.virtuallab.resource.model.ResourceCopy;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResourceCopyDao {
    int insert(ResourceCopy resourceCopy);
}