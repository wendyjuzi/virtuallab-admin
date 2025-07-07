package com.edu.virtuallab.resource.controller;

import com.edu.virtuallab.resource.model.ResourceFavorite;
import com.edu.virtuallab.resource.service.ResourceFavoriteService;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;
import com.edu.virtuallab.log.annotation.OperationLogRecord;

@RestController
@RequestMapping("/resource-favorite")
@Validated
public class ResourceFavoriteController {
    @Autowired
    private ResourceFavoriteService resourceFavoriteService;

    @OperationLogRecord(operation = "ADD_RESOURCE_FAVORITE", module = "RESOURCE", action = "添加资源收藏", description = "用户添加资源收藏", permissionCode = "RESOURCE_MANAGE")
    @PostMapping("/add")
    public CommonResult<Integer> addFavorite(@RequestBody ResourceFavorite resourceFavorite) {
        try {
            int result = resourceFavoriteService.addFavorite(resourceFavorite);
            return CommonResult.success(result, "收藏成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("添加收藏失败: " + e.getMessage());
        }
    }

    @OperationLogRecord(operation = "REMOVE_RESOURCE_FAVORITE", module = "RESOURCE", action = "移除资源收藏", description = "用户移除资源收藏", permissionCode = "RESOURCE_MANAGE")
    @DeleteMapping("/remove/{id}")
    public CommonResult<Integer> removeFavorite(@PathVariable Long id) {
        try {
            int result = resourceFavoriteService.removeFavorite(id);
            return CommonResult.success(result, "移除收藏成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("移除收藏失败: " + e.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    public CommonResult<ResourceFavorite> getFavoriteById(@PathVariable Long id) {
        try {
            ResourceFavorite favorite = resourceFavoriteService.getFavoriteById(id);
            return CommonResult.success(favorite, "查询成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("查询收藏失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/{userId}")
    public CommonResult<List<ResourceFavorite>> getFavoritesByUserId(@PathVariable Long userId,
                                                      @RequestParam(defaultValue = "0") int offset,
                                                      @RequestParam(defaultValue = "10") int size) {
        try {
            List<ResourceFavorite> favorites = resourceFavoriteService.getFavoritesByUserId(userId, offset, size);
            return CommonResult.success(favorites, "查询成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("查询用户收藏列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/count/{userId}")
    public CommonResult<Integer> countFavoritesByUserId(@PathVariable Long userId) {
        try {
            int count = resourceFavoriteService.countFavoritesByUserId(userId);
            return CommonResult.success(count, "查询成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("统计用户收藏数量失败: " + e.getMessage());
        }
    }

    @GetMapping("/getByUserAndResource")
    public CommonResult<ResourceFavorite> getFavoriteByUserAndResource(@RequestParam Long userId, @RequestParam Long resourceId) {
        try {
            ResourceFavorite favorite = resourceFavoriteService.getFavoriteByUserAndResource(userId, resourceId);
            return CommonResult.success(favorite, "查询成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("查询用户资源收藏失败: " + e.getMessage());
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
            ResourceFavorite favorite = resourceFavoriteService.getFavoriteByUserAndResource(userId, resourceId);
            boolean isFavorited = favorite != null;
            return CommonResult.success(isFavorited, "查询成功");
        } catch (BusinessException e) {
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed("查询收藏状态失败: " + e.getMessage());
        }
    }
} 