package com.edu.virtuallab.resource.controller;

import com.edu.virtuallab.resource.model.ResourceLike;
import com.edu.virtuallab.resource.service.LikeFavoriteService;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;
import com.edu.virtuallab.resource.model.LikeFavoriteStatus;

@RestController
@RequestMapping("/like-favorite")
@Validated
public class LikeFavoriteController {
    @Autowired
    private LikeFavoriteService likeFavoriteService;

    /**
     * 查询某个实验/资源的点赞/收藏状态
     */
    @GetMapping("/experiment/{experimentId}")
    public CommonResult<?> getLikeFavoriteStatus(
            @PathVariable @NotNull Long experimentId,
            @RequestParam @NotNull Long userId) {
        try {
            LikeFavoriteStatus status = likeFavoriteService.getStatus(experimentId, userId);
            return CommonResult.success(status, "查询成功");
        } catch (Exception e) {
            throw new BusinessException("查询点赞/收藏状态失败: " + e.getMessage());
        }
    }

    /**
     * 点赞
     */
    @PostMapping("/like/{resourceId}")
    public CommonResult<?> like(@PathVariable Long resourceId, @RequestParam @NotNull Long userId) {
        likeFavoriteService.like(resourceId, userId);
        return CommonResult.success(null, "点赞成功");
    }

    /**
     * 取消点赞
     */
    @PostMapping("/unlike/{resourceId}")
    public CommonResult<?> unlike(@PathVariable Long resourceId, @RequestParam @NotNull Long userId) {
        likeFavoriteService.unlike(resourceId, userId);
        return CommonResult.success(null, "取消点赞成功");
    }

    /**
     * 收藏
     */
    @PostMapping("/favorite/{resourceId}")
    public CommonResult<?> favorite(@PathVariable Long resourceId, @RequestParam @NotNull Long userId) {
        likeFavoriteService.favorite(resourceId, userId);
        return CommonResult.success(null, "收藏成功");
    }

    /**
     * 取消收藏
     */
    @PostMapping("/unfavorite/{resourceId}")
    public CommonResult<?> unfavorite(@PathVariable Long resourceId, @RequestParam @NotNull Long userId) {
        likeFavoriteService.unfavorite(resourceId, userId);
        return CommonResult.success(null, "取消收藏成功");
    }

    @GetMapping("/user/{userId}/likes")
    public List<ResourceLike> getUserLikes(@PathVariable Long userId,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return likeFavoriteService.getUserLikes(userId, page, size);
    }

    @GetMapping("/user/{userId}/likes/count")
    public int countUserLikes(@PathVariable Long userId) {
        return likeFavoriteService.countUserLikes(userId);
    }

    @GetMapping("/is-liked")
    public boolean isLiked(@RequestParam Long userId, @RequestParam Long resourceId) {
        return likeFavoriteService.isResourceLiked(userId, resourceId);
    }
} 