package com.edu.virtuallab.resource.service;

import com.edu.virtuallab.resource.model.ResourceLike;
import java.util.List;

public interface ResourceLikeService {
    boolean likeResource(Long userId, Long resourceId);
    boolean unlikeResource(Long userId, Long resourceId);
    boolean isResourceLiked(Long userId, Long resourceId);
    List<ResourceLike> getUserLikes(Long userId, int page, int size);
    int countUserLikes(Long userId);
} 