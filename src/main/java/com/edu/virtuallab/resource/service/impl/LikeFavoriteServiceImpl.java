package com.edu.virtuallab.resource.service.impl;

import com.edu.virtuallab.resource.dao.ResourceLikeDao;
import com.edu.virtuallab.resource.dao.ResourceFavoriteDao;
import com.edu.virtuallab.resource.model.LikeFavoriteStatus;
import com.edu.virtuallab.resource.model.ResourceLike;
import com.edu.virtuallab.resource.service.LikeFavoriteService;
import com.edu.virtuallab.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeFavoriteServiceImpl implements LikeFavoriteService {
    @Autowired
    private ResourceLikeDao resourceLikeDao;
    @Autowired
    private ResourceFavoriteDao resourceFavoriteDao;

    @Override
    public LikeFavoriteStatus getStatus(Long resourceId, Long userId) {
        try {
            LikeFavoriteStatus status = new LikeFavoriteStatus();
            status.setLiked(resourceLikeDao.countByUserIdAndResourceId(userId, resourceId) > 0);
            status.setFavorited(resourceFavoriteDao.countByUserIdAndResourceId(userId, resourceId) > 0);
            return status;
        } catch (Exception e) {
            throw new BusinessException("查询点赞/收藏状态失败: " + e.getMessage());
        }
    }

    @Override
    public void like(Long resourceId, Long userId) {
        try {
            // 检查是否已经点赞
            if (resourceLikeDao.countByUserIdAndResourceId(userId, resourceId) > 0) {
                throw new BusinessException("您已经点赞过该资源");
            }
            int result = resourceLikeDao.insertByUserAndResource(userId, resourceId);
            if (result <= 0) {
                throw new BusinessException("点赞失败，请稍后重试");
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("点赞失败: " + e.getMessage());
        }
    }

    @Override
    public void unlike(Long resourceId, Long userId) {
        try {
            // 检查是否已经点赞
            if (resourceLikeDao.countByUserIdAndResourceId(userId, resourceId) <= 0) {
                throw new BusinessException("您还没有点赞该资源");
            }
            int result = resourceLikeDao.delete(userId, resourceId);
            if (result <= 0) {
                throw new BusinessException("取消点赞失败，请稍后重试");
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("取消点赞失败: " + e.getMessage());
        }
    }

    @Override
    public void favorite(Long resourceId, Long userId) {
        try {
            // 检查是否已经收藏
            if (resourceFavoriteDao.countByUserIdAndResourceId(userId, resourceId) > 0) {
                throw new BusinessException("您已经收藏过该资源");
            }
            int result = resourceFavoriteDao.insertByUserAndResource(userId, resourceId);
            if (result <= 0) {
                throw new BusinessException("收藏失败，请稍后重试");
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("收藏失败: " + e.getMessage());
        }
    }

    @Override
    public void unfavorite(Long resourceId, Long userId) {
        try {
            // 检查是否已经收藏
            if (resourceFavoriteDao.countByUserIdAndResourceId(userId, resourceId) <= 0) {
                throw new BusinessException("您还没有收藏该资源");
            }
            int result = resourceFavoriteDao.delete(userId, resourceId);
            if (result <= 0) {
                throw new BusinessException("取消收藏失败，请稍后重试");
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("取消收藏失败: " + e.getMessage());
        }
    }

    @Override
    public List<ResourceLike> getUserLikes(Long userId, int page, int size) {
        try {
            int offset = (page - 1) * size;
            return resourceLikeDao.selectByUserId(userId, offset, size);
        } catch (Exception e) {
            throw new BusinessException("查询用户点赞列表失败: " + e.getMessage());
        }
    }

    @Override
    public int countUserLikes(Long userId) {
        try {
            Integer count = resourceLikeDao.countByUserId(userId);
            return count == null ? 0 : count;
        } catch (Exception e) {
            throw new BusinessException("统计用户点赞数量失败: " + e.getMessage());
        }
    }

    @Override
    public boolean isResourceLiked(Long userId, Long resourceId) {
        try {
            return resourceLikeDao.countByUserIdAndResourceId(userId, resourceId) > 0;
        } catch (Exception e) {
            throw new BusinessException("查询点赞状态失败: " + e.getMessage());
        }
    }

    @Override
    public int countResourceLikes(Long resourceId) {
        try {
            Integer count = resourceLikeDao.countByResourceId(resourceId);
            return count == null ? 0 : count;
        } catch (Exception e) {
            throw new BusinessException("统计资源点赞数量失败: " + e.getMessage());
        }
    }
} 