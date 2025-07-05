package com.edu.virtuallab.resource.controller;

import com.edu.virtuallab.resource.model.ResourceLike;
import com.edu.virtuallab.resource.service.LikeFavoriteService;
import com.edu.virtuallab.resource.service.ResourceFavoriteService;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;
import com.edu.virtuallab.resource.model.LikeFavoriteStatus;
import com.edu.virtuallab.log.annotation.OperationLogRecord;
import com.edu.virtuallab.auth.util.JwtUtil;
import com.edu.virtuallab.auth.dao.UserDao;
import com.edu.virtuallab.auth.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/like-favorite")
@Validated
public class LikeFavoriteController {
    @Autowired
    private LikeFavoriteService likeFavoriteService;
    
    @Autowired
    private ResourceFavoriteService resourceFavoriteService;
    
    @Autowired
    private UserDao userDao;

    /**
     * 查询某个实验/资源的点赞/收藏状态
     */
    @GetMapping("/experiment/{experimentId}")
    public CommonResult<LikeFavoriteStatus> getLikeFavoriteStatus(
            @PathVariable @NotNull Long experimentId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userDao.findByUsername(username);
            if (user == null) {
                return CommonResult.failed("用户不存在");
            }
            Long userId = user.getId();
            
            LikeFavoriteStatus status = likeFavoriteService.getStatus(experimentId, userId);
            return CommonResult.success(status, "查询成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("查询点赞/收藏状态失败: " + e.getMessage());
        }
    }

    /**
     * 查询某个资源的点赞/收藏状态（通过resourceId）
     */
    @GetMapping("/resource/{resourceId}")
    public CommonResult<LikeFavoriteStatus> getLikeFavoriteStatusByResource(
            @PathVariable @NotNull Long resourceId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userDao.findByUsername(username);
            if (user == null) {
                return CommonResult.failed("用户不存在");
            }
            Long userId = user.getId();
            
            LikeFavoriteStatus status = likeFavoriteService.getStatus(resourceId, userId);
            return CommonResult.success(status, "查询成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("查询点赞/收藏状态失败: " + e.getMessage());
        }
    }

    @OperationLogRecord(operation = "LIKE_RESOURCE", module = "RESOURCE", action = "点赞资源", description = "用户点赞资源", permissionCode = "RESOURCE_MANAGE")
    @PostMapping("/like/{resourceId}")
    public CommonResult<?> like(@PathVariable Long resourceId, @RequestParam @NotNull Long userId) {
        try {
        likeFavoriteService.like(resourceId, userId);
        return CommonResult.success(null, "点赞成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("点赞失败: " + e.getMessage());
        }
    }

    @OperationLogRecord(operation = "UNLIKE_RESOURCE", module = "RESOURCE", action = "取消点赞资源", description = "用户取消点赞资源", permissionCode = "RESOURCE_MANAGE")
    @PostMapping("/unlike/{resourceId}")
    public CommonResult<?> unlike(@PathVariable Long resourceId, @RequestParam @NotNull Long userId) {
        try {
        likeFavoriteService.unlike(resourceId, userId);
        return CommonResult.success(null, "取消点赞成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("取消点赞失败: " + e.getMessage());
        }
    }

    @OperationLogRecord(operation = "FAVORITE_RESOURCE", module = "RESOURCE", action = "收藏资源", description = "用户收藏资源", permissionCode = "RESOURCE_MANAGE")
    @PostMapping("/favorite/{resourceId}")
    public CommonResult<?> favorite(@PathVariable Long resourceId, @RequestParam @NotNull Long userId) {
        try {
        likeFavoriteService.favorite(resourceId, userId);
        return CommonResult.success(null, "收藏成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("收藏失败: " + e.getMessage());
        }
    }

    @OperationLogRecord(operation = "UNFAVORITE_RESOURCE", module = "RESOURCE", action = "取消收藏资源", description = "用户取消收藏资源", permissionCode = "RESOURCE_MANAGE")
    @PostMapping("/unfavorite/{resourceId}")
    public CommonResult<?> unfavorite(@PathVariable Long resourceId, @RequestParam @NotNull Long userId) {
        try {
        likeFavoriteService.unfavorite(resourceId, userId);
        return CommonResult.success(null, "取消收藏成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("取消收藏失败: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}/likes")
    public CommonResult<List<ResourceLike>> getUserLikes(@PathVariable Long userId,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        try {
            List<ResourceLike> likes = likeFavoriteService.getUserLikes(userId, page, size);
            return CommonResult.success(likes, "查询成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("查询用户点赞列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}/likes/count")
    public CommonResult<Integer> countUserLikes(@PathVariable Long userId) {
        try {
            int count = likeFavoriteService.countUserLikes(userId);
            return CommonResult.success(count, "查询成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("统计用户点赞数量失败: " + e.getMessage());
        }
    }

    @GetMapping("/is-liked")
    public CommonResult<Boolean> isLiked(@RequestParam Long userId, @RequestParam Long resourceId) {
        try {
            boolean isLiked = likeFavoriteService.isResourceLiked(userId, resourceId);
            return CommonResult.success(isLiked, "查询成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("查询点赞状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取资源的点赞数量
     */
    @GetMapping("/resource/{resourceId}/like-count")
    public CommonResult<Integer> getResourceLikeCount(@PathVariable Long resourceId) {
        try {
            int count = likeFavoriteService.countResourceLikes(resourceId);
            return CommonResult.success(count, "查询成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("查询资源点赞数量失败: " + e.getMessage());
        }
    }

    /**
     * 获取资源的收藏数量
     */
    @GetMapping("/resource/{resourceId}/favorite-count")
    public CommonResult<Integer> getResourceFavoriteCount(@PathVariable Long resourceId) {
        try {
            int count = resourceFavoriteService.countResourceFavorites(resourceId);
            return CommonResult.success(count, "查询成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("查询资源收藏数量失败: " + e.getMessage());
        }
    }

    /**
     * 检查用户是否已收藏某个资源
     */
    @GetMapping("/is-favorited")
    public CommonResult<Boolean> isFavorited(@RequestParam Long userId, @RequestParam Long resourceId) {
        try {
            boolean isFavorited = resourceFavoriteService.getFavoriteByUserAndResource(userId, resourceId) != null;
            return CommonResult.success(isFavorited, "查询成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("查询收藏状态失败: " + e.getMessage());
        }
    }
} 