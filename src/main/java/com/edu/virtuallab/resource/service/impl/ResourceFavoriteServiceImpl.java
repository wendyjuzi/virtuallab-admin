package com.edu.virtuallab.resource.service.impl;

import com.edu.virtuallab.resource.dao.ResourceFavoriteDao;
import com.edu.virtuallab.resource.model.ResourceFavorite;
import com.edu.virtuallab.resource.service.ResourceFavoriteService;
import com.edu.virtuallab.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceFavoriteServiceImpl implements ResourceFavoriteService {
    @Autowired
    private ResourceFavoriteDao resourceFavoriteDao;

    @Override
    public int addFavorite(ResourceFavorite resourceFavorite) {
        try {
            // 检查是否已经收藏
            if (resourceFavoriteDao.countByUserIdAndResourceId(resourceFavorite.getUserId(), resourceFavorite.getResourceId()) > 0) {
                throw new BusinessException("您已经收藏过该资源");
            }
            return resourceFavoriteDao.insert(resourceFavorite);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("添加收藏失败: " + e.getMessage());
        }
    }

    @Override
    public int removeFavorite(Long id) {
        try {
            return resourceFavoriteDao.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("移除收藏失败: " + e.getMessage());
        }
    }

    @Override
    public ResourceFavorite getFavoriteById(Long id) {
        try {
            return resourceFavoriteDao.selectById(id);
        } catch (Exception e) {
            throw new BusinessException("查询收藏失败: " + e.getMessage());
        }
    }

    @Override
    public List<ResourceFavorite> getFavoritesByUserId(Long userId, int offset, int size) {
        try {
            return resourceFavoriteDao.selectByUserId(userId, offset, size);
        } catch (Exception e) {
            throw new BusinessException("查询用户收藏列表失败: " + e.getMessage());
        }
    }

    @Override
    public int countFavoritesByUserId(Long userId) {
        try {
            return resourceFavoriteDao.countByUserId(userId);
        } catch (Exception e) {
            throw new BusinessException("统计用户收藏数量失败: " + e.getMessage());
        }
    }

    @Override
    public ResourceFavorite getFavoriteByUserAndResource(Long userId, Long resourceId) {
        try {
            return resourceFavoriteDao.selectByUserAndResource(userId, resourceId);
        } catch (Exception e) {
            throw new BusinessException("查询用户资源收藏失败: " + e.getMessage());
        }
    }

    @Override
    public int countResourceFavorites(Long resourceId) {
        try {
            Integer count = resourceFavoriteDao.countByResourceId(resourceId);
            return count == null ? 0 : count;
        } catch (Exception e) {
            throw new BusinessException("统计资源收藏数量失败: " + e.getMessage());
        }
    }
} 