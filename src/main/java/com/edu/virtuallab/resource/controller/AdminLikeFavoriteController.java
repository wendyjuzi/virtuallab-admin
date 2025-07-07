package com.edu.virtuallab.resource.controller;

import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.resource.dto.*;
import com.edu.virtuallab.resource.service.AdminLikeFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员点赞收藏控制器
 */
@RestController
@RequestMapping("/admin/like-favorite")
public class AdminLikeFavoriteController {

    @Autowired
    private AdminLikeFavoriteService adminLikeFavoriteService;

    /**
     * 获取所有用户点赞收藏统计
     */
    @GetMapping("/stats")
    public CommonResult<AdminStatsResponse> getAllUsersLikeFavoriteStats() {
        AdminStatsResponse stats = adminLikeFavoriteService.getAllUsersLikeFavoriteStats();
        return CommonResult.success(stats, "获取统计成功");
    }

    /**
     * 获取所有点赞记录（分页）
     */
    @GetMapping("/likes")
    public CommonResult<PageResponse<LikeRecordWithUser>> getAllLikes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<LikeRecordWithUser> likes = adminLikeFavoriteService.getAllLikes(page, size);
        return CommonResult.success(likes, "获取点赞记录成功");
    }

    /**
     * 获取所有收藏记录（分页）
     */
    @GetMapping("/favorites")
    public CommonResult<PageResponse<FavoriteRecordWithUser>> getAllFavorites(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<FavoriteRecordWithUser> favorites = adminLikeFavoriteService.getAllFavorites(page, size);
        return CommonResult.success(favorites, "获取收藏记录成功");
    }

    /**
     * 删除指定点赞记录
     */
    @DeleteMapping("/like/{likeId}")
    public CommonResult<Integer> adminDeleteLike(@PathVariable Long likeId) {
        Integer result = adminLikeFavoriteService.adminDeleteLike(likeId);
        return CommonResult.success(result, "删除点赞记录成功");
    }

    /**
     * 批量删除点赞记录
     */
    @DeleteMapping("/likes/batch")
    public CommonResult<Integer> adminBatchDeleteLikes(@RequestBody BatchDeleteRequest request) {
        Integer result = adminLikeFavoriteService.adminBatchDeleteLikes(request.getIds());
        return CommonResult.success(result, "批量删除点赞记录成功");
    }

    /**
     * 删除指定收藏记录
     */
    @DeleteMapping("/favorite/{favoriteId}")
    public CommonResult<Integer> adminDeleteFavorite(@PathVariable Long favoriteId) {
        Integer result = adminLikeFavoriteService.adminDeleteFavorite(favoriteId);
        return CommonResult.success(result, "删除收藏记录成功");
    }

    /**
     * 批量删除收藏记录
     */
    @DeleteMapping("/favorites/batch")
    public CommonResult<Integer> adminBatchDeleteFavorites(@RequestBody BatchDeleteRequest request) {
        Integer result = adminLikeFavoriteService.adminBatchDeleteFavorites(request.getIds());
        return CommonResult.success(result, "批量删除收藏记录成功");
    }

    /**
     * 重置用户统计
     */
    @PostMapping("/user/{userId}/reset-stats")
    public CommonResult<Void> adminResetUserStats(@PathVariable Long userId) {
        adminLikeFavoriteService.adminResetUserStats(userId);
        return CommonResult.success(null, "重置用户统计成功");
    }

    /**
     * 获取资源统计
     */
    @GetMapping("/resource-stats")
    public CommonResult<ResourceStatsResponse> getResourceLikeFavoriteStats() {
        ResourceStatsResponse stats = adminLikeFavoriteService.getResourceLikeFavoriteStats();
        return CommonResult.success(stats, "获取资源统计成功");
    }

    /**
     * 获取指定资源统计
     */
    @GetMapping("/resource/{resourceId}/stats")
    public CommonResult<ResourceDetailStats> getResourceDetailStats(@PathVariable Long resourceId) {
        ResourceDetailStats stats = adminLikeFavoriteService.getResourceDetailStats(resourceId);
        return CommonResult.success(stats, "获取资源详细统计成功");
    }

    /**
     * 获取指定用户统计
     */
    @GetMapping("/user/{userId}/stats")
    public CommonResult<UserDetailStats> getUserDetailStats(@PathVariable Long userId) {
        UserDetailStats stats = adminLikeFavoriteService.getUserDetailStats(userId);
        return CommonResult.success(stats, "获取用户详细统计成功");
    }

    /**
     * 导出数据
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportLikeFavoriteData(@RequestParam String format) {
        byte[] data = adminLikeFavoriteService.exportLikeFavoriteData(format);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "like-favorite-data." + format);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }

    /**
     * 系统概览
     */
    @GetMapping("/overview")
    public CommonResult<SystemOverview> getSystemOverview() {
        SystemOverview overview = adminLikeFavoriteService.getSystemOverview();
        return CommonResult.success(overview, "获取系统概览成功");
    }
} 