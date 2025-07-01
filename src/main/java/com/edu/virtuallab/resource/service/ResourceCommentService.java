package com.edu.virtuallab.resource.service;

import com.edu.virtuallab.resource.model.ResourceComment;
import java.util.List;

public interface ResourceCommentService {
    int addComment(ResourceComment comment);
    List<ResourceComment> getCommentsByResourceId(Long resourceId);
    ResourceComment getCommentById(Long id);
    int updateComment(ResourceComment comment);
    int deleteComment(Long id);
}