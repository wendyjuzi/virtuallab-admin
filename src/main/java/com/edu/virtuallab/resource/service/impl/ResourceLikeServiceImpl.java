package com.edu.virtuallab.resource.service.impl;

import com.edu.virtuallab.resource.dao.ResourceLikeDao;
import com.edu.virtuallab.resource.model.ResourceLike;
import com.edu.virtuallab.resource.service.ResourceLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ResourceLikeServiceImpl implements ResourceLikeService {
    @Autowired
    private ResourceLikeDao resourceLikeDao;

    @Override
    public boolean likeResource(Long userId, Long resourceId) {
        if (isResourceLiked(userId, resourceId)) return false;
        ResourceLike like = new ResourceLike();
        like.setUserId(userId);
        like.setResourceId(resourceId);
        like.setCreateTime(new Date());
        return resourceLikeDao.insert(like) > 0;
    }

    @Override
    public boolean unlikeResource(Long userId, Long resourceId) {
        ResourceLike like = resourceLikeDao.selectByUserAndResource(userId, resourceId);
        if (like == null) return false;
        return resourceLikeDao.deleteById(like.getId()) > 0;
    }

    @Override
    public boolean isResourceLiked(Long userId, Long resourceId) {
        ResourceLike like = resourceLikeDao.selectByUserAndResource(userId, resourceId);
        return like != null;
    }

    @Override
    public List<ResourceLike> getUserLikes(Long userId, int page, int size) {
        int offset = (page - 1) * size;
        return resourceLikeDao.selectByUserId(userId, offset, size);
    }

    @Override
    public int countUserLikes(Long userId) {
        Integer count = resourceLikeDao.countByUserId(userId);
        return count == null ? 0 : count;
    }
} 