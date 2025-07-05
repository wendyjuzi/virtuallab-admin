package com.edu.virtuallab.resource.service.impl;

import com.edu.virtuallab.auth.dao.UserDao;
import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.resource.dao.ResourceLikeDao;
import com.edu.virtuallab.resource.dao.ResourceFavoriteDao;
import com.edu.virtuallab.resource.dao.ResourceDao;
import com.edu.virtuallab.resource.model.Resource;
import com.edu.virtuallab.resource.model.ResourceLike;
import com.edu.virtuallab.resource.model.ResourceFavorite;
import com.edu.virtuallab.resource.dto.*;
import com.edu.virtuallab.resource.service.AdminLikeFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 管理员点赞收藏服务实现类
 */
@Service
public class AdminLikeFavoriteServiceImpl implements AdminLikeFavoriteService {

    @Autowired
    private ResourceLikeDao resourceLikeDao;

    @Autowired
    private ResourceFavoriteDao resourceFavoriteDao;

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private UserDao userDao;

    @Override
    public AdminStatsResponse getAllUsersLikeFavoriteStats() {
        // 获取基础统计数据
        Long totalUsers = (long) userDao.findAll().size();
        
        // 获取所有实验项目的点赞和收藏总数
        List<Resource> allResources = resourceDao.selectAll();
        Long totalResources = (long) allResources.size();
        
        // 使用新的统计方法
        Integer totalLikes = resourceLikeDao.countTotalLikes();
        Integer totalFavorites = resourceFavoriteDao.countTotalFavorites();
        
        // 计算平均值
        Double avgLikesPerUser = totalUsers > 0 ? (double) totalLikes / totalUsers : 0.0;
        Double avgFavoritesPerUser = totalUsers > 0 ? (double) totalFavorites / totalUsers : 0.0;

        // 获取TOP用户（前10名）
        List<AdminStatsResponse.UserLikeFavoriteStats> topUsers = new ArrayList<>();
        List<UserLikeFavoriteStats> topUsersByLikes = resourceLikeDao.selectTopUsersByLikes(10);
        List<UserLikeFavoriteStats> topUsersByFavorites = resourceFavoriteDao.selectTopUsersByFavorites(10);
        
        // 合并用户统计，去重并计算总交互数
        Map<Long, UserLikeFavoriteStats> userStatsMap = new HashMap<>();
        
        // 处理点赞统计
        for (UserLikeFavoriteStats user : topUsersByLikes) {
            userStatsMap.put(user.getUserId(), user);
        }
        
        // 处理收藏统计，合并数据
        for (UserLikeFavoriteStats user : topUsersByFavorites) {
            if (userStatsMap.containsKey(user.getUserId())) {
                UserLikeFavoriteStats existing = userStatsMap.get(user.getUserId());
                existing.setFavoriteCount(user.getFavoriteCount());
                existing.setTotalInteractions(existing.getLikeCount() + existing.getFavoriteCount());
            } else {
                userStatsMap.put(user.getUserId(), user);
            }
        }
        
        // 转换为AdminStatsResponse格式
        for (UserLikeFavoriteStats user : userStatsMap.values()) {
            AdminStatsResponse.UserLikeFavoriteStats stats = new AdminStatsResponse.UserLikeFavoriteStats(
                user.getUserId(), user.getUsername(), user.getNickname(), 
                user.getLikeCount().longValue(), user.getFavoriteCount().longValue()
            );
            stats.setLastActivity(user.getLastActivity());
            topUsers.add(stats);
        }
        
        // 按总交互数排序
        topUsers.sort((a, b) -> Long.compare(b.getTotalInteractions(), a.getTotalInteractions()));

        // 获取TOP资源（前10名）
        List<AdminStatsResponse.ResourceLikeFavoriteStats> topResources = new ArrayList<>();
        List<ResourceLikeFavoriteStats> topResourcesByLikes = resourceLikeDao.selectTopResourcesByLikes(10);
        List<ResourceLikeFavoriteStats> topResourcesByFavorites = resourceFavoriteDao.selectTopResourcesByFavorites(10);
        
        // 合并资源统计，去重并计算总交互数
        Map<Long, ResourceLikeFavoriteStats> resourceStatsMap = new HashMap<>();
        
        // 处理点赞统计
        for (ResourceLikeFavoriteStats resource : topResourcesByLikes) {
            resourceStatsMap.put(resource.getResourceId(), resource);
        }
        
        // 处理收藏统计，合并数据
        for (ResourceLikeFavoriteStats resource : topResourcesByFavorites) {
            if (resourceStatsMap.containsKey(resource.getResourceId())) {
                ResourceLikeFavoriteStats existing = resourceStatsMap.get(resource.getResourceId());
                existing.setFavoriteCount(resource.getFavoriteCount());
                existing.setTotalInteractions(existing.getLikeCount() + existing.getFavoriteCount());
            } else {
                resourceStatsMap.put(resource.getResourceId(), resource);
            }
        }
        
        // 转换为AdminStatsResponse格式
        for (ResourceLikeFavoriteStats resource : resourceStatsMap.values()) {
            AdminStatsResponse.ResourceLikeFavoriteStats stats = new AdminStatsResponse.ResourceLikeFavoriteStats(
                resource.getResourceId(), resource.getResourceName(), resource.getResourceType(),
                resource.getLikeCount().longValue(), resource.getFavoriteCount().longValue()
            );
            stats.setCategory(resource.getCategory());
            topResources.add(stats);
        }
        
        // 按总交互数排序
        topResources.sort((a, b) -> Long.compare(b.getTotalInteractions(), a.getTotalInteractions()));

        AdminStatsResponse response = new AdminStatsResponse(totalUsers, totalLikes.longValue(), totalFavorites.longValue(), totalResources);
        response.setAvgLikesPerUser(avgLikesPerUser);
        response.setAvgFavoritesPerUser(avgFavoritesPerUser);
        response.setTopUsers(topUsers);
        response.setTopResources(topResources);

        return response;
    }

    @Override
    public PageResponse<LikeRecordWithUser> getAllLikes(int page, int size) {
        int offset = (page - 1) * size;
        
        // 获取所有点赞记录
        List<ResourceLike> allLikes = resourceLikeDao.selectAll();
        Long total = (long) allLikes.size();
        
        // 分页处理
        List<LikeRecordWithUser> likes = new ArrayList<>();
        int endIndex = Math.min(offset + size, allLikes.size());
        
        for (int i = offset; i < endIndex; i++) {
            ResourceLike like = allLikes.get(i);
            User user = userDao.findById(like.getUserId());
            Resource resource = resourceDao.selectById(like.getResourceId());
            
            LikeRecordWithUser likeRecord = new LikeRecordWithUser();
            likeRecord.setId(like.getId());
            likeRecord.setUserId(like.getUserId());
            likeRecord.setUsername(user != null ? user.getUsername() : "未知用户");
            likeRecord.setResourceId(like.getResourceId());
            likeRecord.setResourceName(resource != null ? resource.getName() : "未知资源");
            likeRecord.setCreateTime(like.getCreateTime());
            
            likes.add(likeRecord);
        }
        
        return new PageResponse<>(likes, total, page, size);
    }

    @Override
    public PageResponse<FavoriteRecordWithUser> getAllFavorites(int page, int size) {
        int offset = (page - 1) * size;
        
        // 获取所有收藏记录
        List<ResourceFavorite> allFavorites = resourceFavoriteDao.selectAll();
        Long total = (long) allFavorites.size();
        
        // 分页处理
        List<FavoriteRecordWithUser> favorites = new ArrayList<>();
        int endIndex = Math.min(offset + size, allFavorites.size());
        
        for (int i = offset; i < endIndex; i++) {
            ResourceFavorite favorite = allFavorites.get(i);
            User user = userDao.findById(favorite.getUserId());
            Resource resource = resourceDao.selectById(favorite.getResourceId());
            
            FavoriteRecordWithUser favoriteRecord = new FavoriteRecordWithUser();
            favoriteRecord.setId(favorite.getId());
            favoriteRecord.setUserId(favorite.getUserId());
            favoriteRecord.setUsername(user != null ? user.getUsername() : "未知用户");
            favoriteRecord.setResourceId(favorite.getResourceId());
            favoriteRecord.setResourceName(resource != null ? resource.getName() : "未知资源");
            favoriteRecord.setCreateTime(favorite.getCreateTime());
            
            favorites.add(favoriteRecord);
        }
        
        return new PageResponse<>(favorites, total, page, size);
    }

    @Override
    @Transactional
    public Integer adminDeleteLike(Long likeId) {
        return resourceLikeDao.deleteById(likeId);
    }

    @Override
    @Transactional
    public Integer adminBatchDeleteLikes(List<Long> likeIds) {
        if (likeIds == null || likeIds.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (Long id : likeIds) {
            count += resourceLikeDao.deleteById(id);
        }
        return count;
    }

    @Override
    @Transactional
    public Integer adminDeleteFavorite(Long favoriteId) {
        return resourceFavoriteDao.deleteById(favoriteId);
    }

    @Override
    @Transactional
    public Integer adminBatchDeleteFavorites(List<Long> favoriteIds) {
        if (favoriteIds == null || favoriteIds.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (Long id : favoriteIds) {
            count += resourceFavoriteDao.deleteById(id);
        }
        return count;
    }

    @Override
    @Transactional
    public void adminResetUserStats(Long userId) {
        // 删除用户的所有点赞和收藏记录
        // 这里需要实现具体的删除逻辑
    }

    @Override
    public ResourceStatsResponse getResourceLikeFavoriteStats() {
        List<Resource> allResources = resourceDao.selectAll();
        Long totalResources = (long) allResources.size();
        
        // 使用新的统计方法
        Integer totalLikes = resourceLikeDao.countTotalLikes();
        Integer totalFavorites = resourceFavoriteDao.countTotalFavorites();

        Double avgLikesPerResource = totalResources > 0 ? (double) totalLikes / totalResources : 0.0;
        Double avgFavoritesPerResource = totalResources > 0 ? (double) totalFavorites / totalResources : 0.0;

        // 获取TOP资源（前10名）
        List<ResourceStatsResponse.ResourceLikeFavoriteStats> topResources = new ArrayList<>();
        List<ResourceLikeFavoriteStats> topResourcesByLikes = resourceLikeDao.selectTopResourcesByLikes(10);
        List<ResourceLikeFavoriteStats> topResourcesByFavorites = resourceFavoriteDao.selectTopResourcesByFavorites(10);
        
        // 合并资源统计，去重并计算总交互数
        Map<Long, ResourceLikeFavoriteStats> resourceStatsMap = new HashMap<>();
        
        // 处理点赞统计
        for (ResourceLikeFavoriteStats resource : topResourcesByLikes) {
            resourceStatsMap.put(resource.getResourceId(), resource);
        }
        
        // 处理收藏统计，合并数据
        for (ResourceLikeFavoriteStats resource : topResourcesByFavorites) {
            if (resourceStatsMap.containsKey(resource.getResourceId())) {
                ResourceLikeFavoriteStats existing = resourceStatsMap.get(resource.getResourceId());
                existing.setFavoriteCount(resource.getFavoriteCount());
                existing.setTotalInteractions(existing.getLikeCount() + existing.getFavoriteCount());
            } else {
                resourceStatsMap.put(resource.getResourceId(), resource);
            }
        }
        
        // 转换为ResourceStatsResponse格式
        for (ResourceLikeFavoriteStats resource : resourceStatsMap.values()) {
            ResourceStatsResponse.ResourceLikeFavoriteStats stats = new ResourceStatsResponse.ResourceLikeFavoriteStats(
                resource.getResourceId(), resource.getResourceName(), resource.getResourceType(),
                resource.getLikeCount().longValue(), resource.getFavoriteCount().longValue()
            );
            stats.setCategory(resource.getCategory());
            stats.setCreateTime(resource.getCreateTime());
            stats.setLastInteraction(resource.getLastInteraction());
            topResources.add(stats);
        }
        
        // 按总交互数排序
        topResources.sort((a, b) -> Long.compare(b.getTotalInteractions(), a.getTotalInteractions()));

        ResourceStatsResponse response = new ResourceStatsResponse(totalResources, totalLikes.longValue(), totalFavorites.longValue());
        response.setAvgLikesPerResource(avgLikesPerResource);
        response.setAvgFavoritesPerResource(avgFavoritesPerResource);
        response.setTopResources(topResources);
        
        return response;
    }

    @Override
    public ResourceDetailStats getResourceDetailStats(Long resourceId) {
        // 获取资源基本信息
        var resource = resourceDao.selectById(resourceId);
        if (resource == null) {
            return null;
        }

        // 获取点赞和收藏数量
        Long likeCount = (long) resourceLikeDao.countByResourceId(resourceId);
        Long favoriteCount = (long) resourceFavoriteDao.countByResourceId(resourceId);

        // 获取最近的点赞记录
        List<LikeRecordWithUser> recentLikes = new ArrayList<>();

        // 获取最近的收藏记录
        List<FavoriteRecordWithUser> recentFavorites = new ArrayList<>();

        ResourceDetailStats stats = new ResourceDetailStats(resourceId, resource.getName(), resource.getType(), likeCount, favoriteCount);
        stats.setDescription(resource.getCategory());
        stats.setCreateTime(java.sql.Timestamp.valueOf(resource.getCreatedAt()));
        stats.setRecentLikes(recentLikes);
        stats.setRecentFavorites(recentFavorites);

        return stats;
    }

    @Override
    public UserDetailStats getUserDetailStats(Long userId) {
        // 获取用户基本信息
        User user = userDao.findById(userId);
        if (user == null) {
            return null;
        }

        // 获取点赞和收藏数量
        Long likeCount = (long) resourceLikeDao.countByUserId(userId);
        Long favoriteCount = (long) resourceFavoriteDao.countByUserId(userId);

        // 获取最近的点赞记录
        List<LikeRecordWithUser> recentLikes = new ArrayList<>();

        // 获取最近的收藏记录
        List<FavoriteRecordWithUser> recentFavorites = new ArrayList<>();

        UserDetailStats stats = new UserDetailStats(userId, user.getUsername(), user.getRealName(), likeCount, favoriteCount);
        stats.setEmail(user.getEmail());
        stats.setRegisterTime(user.getCreateTime());
        stats.setRecentLikes(recentLikes);
        stats.setRecentFavorites(recentFavorites);

        return stats;
    }

    @Override
    public byte[] exportLikeFavoriteData(String format) {
        // 这里实现数据导出功能
        // 可以根据format参数导出为CSV、Excel等格式
        // 暂时返回空数组，后续可以完善
        return new byte[0];
    }

    @Override
    public SystemOverview getSystemOverview() {
        // 获取基础统计数据
        Long totalUsers = (long) userDao.findAll().size();
        Long totalResources = (long) resourceDao.selectAll().size();
        Long totalLikes = (long) resourceLikeDao.countByResourceId(1L); // 临时统计
        Long totalFavorites = (long) resourceFavoriteDao.countByResourceId(1L); // 临时统计
        Long totalShares = 0L; // 暂时设为0，后续可以添加分享统计

        // 计算平均值
        Double avgLikesPerUser = totalUsers > 0 ? (double) totalLikes / totalUsers : 0.0;
        Double avgFavoritesPerUser = totalUsers > 0 ? (double) totalFavorites / totalUsers : 0.0;
        Double avgLikesPerResource = totalResources > 0 ? (double) totalLikes / totalResources : 0.0;
        Double avgFavoritesPerResource = totalResources > 0 ? (double) totalFavorites / totalResources : 0.0;

        // 获取TOP用户和资源
        List<AdminStatsResponse.UserLikeFavoriteStats> topUsers = new ArrayList<>();
        List<ResourceStatsResponse.ResourceLikeFavoriteStats> topResources = new ArrayList<>();

        // 获取每日统计数据（暂时返回空Map，后续可以完善）
        Map<String, Long> dailyStats = new HashMap<>();

        SystemOverview overview = new SystemOverview(totalUsers, totalResources, totalLikes, totalFavorites, totalShares);
        overview.setAvgLikesPerUser(avgLikesPerUser);
        overview.setAvgFavoritesPerUser(avgFavoritesPerUser);
        overview.setAvgLikesPerResource(avgLikesPerResource);
        overview.setAvgFavoritesPerResource(avgFavoritesPerResource);
        overview.setDailyStats(dailyStats);
        overview.setTopUsers(topUsers);
        overview.setTopResources(topResources);
        overview.setLastUpdateTime(new Date());

        return overview;
    }
} 