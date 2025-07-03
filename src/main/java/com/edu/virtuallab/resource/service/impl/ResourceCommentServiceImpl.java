package com.edu.virtuallab.resource.service.impl;

import com.edu.virtuallab.resource.dao.ResourceCommentDao;
import com.edu.virtuallab.resource.model.ResourceComment;
import com.edu.virtuallab.resource.service.ResourceCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceCommentServiceImpl implements ResourceCommentService {
    @Autowired
    private ResourceCommentDao resourceCommentDao;

    @Override
    public int addComment(ResourceComment comment) {
        return resourceCommentDao.insert(comment);
    }

    @Override
    public List<ResourceComment> getCommentsByResourceId(Long resourceId) {
        return resourceCommentDao.getCommentsByResourceId(resourceId);
    }

    @Override
    public ResourceComment getCommentById(Long id) {
        return resourceCommentDao.selectById(id);
    }

    @Override
    public int updateComment(ResourceComment comment) {
        return resourceCommentDao.update(comment);
    }

    @Override
    public int deleteComment(Long id) {
        return resourceCommentDao.deleteById(id);
    }
}