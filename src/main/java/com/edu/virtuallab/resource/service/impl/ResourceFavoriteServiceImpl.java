package com.edu.virtuallab.resource.service.impl;

import com.edu.virtuallab.resource.dao.ResourceFavoriteDao;
import com.edu.virtuallab.resource.model.ResourceFavorite;
import com.edu.virtuallab.resource.service.ResourceFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceFavoriteServiceImpl implements ResourceFavoriteService {
    @Autowired
    private ResourceFavoriteDao resourceFavoriteDao;

    @Override
    public int addFavorite(ResourceFavorite resourceFavorite) {
        return resourceFavoriteDao.insert(resourceFavorite);
    }

    @Override
    public int removeFavorite(Long id) {
        return resourceFavoriteDao.deleteById(id);
    }

    @Override
    public ResourceFavorite getFavoriteById(Long id) {
        return resourceFavoriteDao.selectById(id);
    }

    @Override
    public List<ResourceFavorite> getFavoritesByUserId(Long userId, int offset, int size) {
        return resourceFavoriteDao.selectByUserId(userId, offset, size);
    }

    @Override
    public int countFavoritesByUserId(Long userId) {
        return resourceFavoriteDao.countByUserId(userId);
    }

    @Override
    public ResourceFavorite getFavoriteByUserAndResource(Long userId, Long resourceId) {
        return resourceFavoriteDao.selectByUserAndResource(userId, resourceId);
    }
} 