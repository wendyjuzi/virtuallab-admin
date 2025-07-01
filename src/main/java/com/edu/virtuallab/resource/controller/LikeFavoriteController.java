package com.edu.virtuallab.resource.controller;

import com.edu.virtuallab.resource.model.ResourceLike;
import com.edu.virtuallab.resource.service.ResourceLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/like-favorite")
public class LikeFavoriteController {
    @Autowired
    private ResourceLikeService resourceLikeService;

    @PostMapping("/like")
    public boolean like(@RequestParam Long userId, @RequestParam Long resourceId) {
        return resourceLikeService.likeResource(userId, resourceId);
    }

    @PostMapping("/unlike")
    public boolean unlike(@RequestParam Long userId, @RequestParam Long resourceId) {
        return resourceLikeService.unlikeResource(userId, resourceId);
    }

    @GetMapping("/user/{userId}/likes")
    public List<ResourceLike> getUserLikes(@PathVariable Long userId,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return resourceLikeService.getUserLikes(userId, page, size);
    }

    @GetMapping("/user/{userId}/likes/count")
    public int countUserLikes(@PathVariable Long userId) {
        return resourceLikeService.countUserLikes(userId);
    }

    @GetMapping("/is-liked")
    public boolean isLiked(@RequestParam Long userId, @RequestParam Long resourceId) {
        return resourceLikeService.isResourceLiked(userId, resourceId);
    }
} 