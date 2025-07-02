package com.edu.virtuallab.resource.service.impl;

import com.edu.virtuallab.resource.dao.ResourceLikeDao;
import com.edu.virtuallab.resource.dao.ResourceFavoriteDao;
import com.edu.virtuallab.resource.model.LikeFavoriteStatus;
import com.edu.virtuallab.resource.service.LikeFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeFavoriteServiceImpl implements LikeFavoriteService {
    @Autowired
    private ResourceLikeDao resourceLikeDao;
    @Autowired
    private ResourceFavoriteDao resourceFavoriteDao;

    @Override
    public LikeFavoriteStatus getStatus(Long resourceId, Long userId) {
        LikeFavoriteStatus status = new LikeFavoriteStatus();
        status.setLiked(resourceLikeDao.countByUserIdAndResourceId(userId, resourceId) > 0);
        status.setFavorited(resourceFavoriteDao.countByUserIdAndResourceId(userId, resourceId) > 0);
        return status;
    }

    @Override
    public void like(Long resourceId, Long userId) {
        resourceLikeDao.insertByUserAndResource(userId, resourceId);
    }

    @Override
    public void unlike(Long resourceId, Long userId) {
        resourceLikeDao.delete(userId, resourceId);
    }

    @Override
    public void favorite(Long resourceId, Long userId) {
        resourceFavoriteDao.insertByUserAndResource(userId, resourceId);
    }

    @Override
    public void unfavorite(Long resourceId, Long userId) {
        resourceFavoriteDao.delete(userId, resourceId);
    }

    @Override
    public java.util.List<com.edu.virtuallab.resource.model.ResourceLike> getUserLikes(Long userId, int page, int size) {
        // TODO: 实现分页查询用户点赞资源
        return new java.util.ArrayList<>();
    }

    @Override
    public int countUserLikes(Long userId) {
        // TODO: 实现统计用户点赞数量
        return 0;
    }

    @Override
    public boolean isResourceLiked(Long userId, Long resourceId) {
        return resourceLikeDao.countByUserIdAndResourceId(userId, resourceId) > 0;
    }
} 