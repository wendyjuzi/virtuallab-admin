package com.edu.virtuallab.resource.service;

import com.edu.virtuallab.resource.model.ResourceFavorite;
import java.util.List;

public interface ResourceFavoriteService {
    int addFavorite(ResourceFavorite resourceFavorite);
    int removeFavorite(Long id);
    ResourceFavorite getFavoriteById(Long id);
    List<ResourceFavorite> getFavoritesByUserId(Long userId, int offset, int size);
    int countFavoritesByUserId(Long userId);
    ResourceFavorite getFavoriteByUserAndResource(Long userId, Long resourceId);
}
