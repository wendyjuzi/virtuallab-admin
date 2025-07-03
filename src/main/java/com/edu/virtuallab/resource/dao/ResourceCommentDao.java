package com.edu.virtuallab.resource.dao;

import com.edu.virtuallab.resource.model.ResourceComment;

import java.util.List;

public interface ResourceCommentDao {
    int insert(ResourceComment comment);
    List<ResourceComment> getCommentsByResourceId(Long resourceId);
    ResourceComment selectById(Long id);
    int update(ResourceComment comment);
    int deleteById(Long id);
}