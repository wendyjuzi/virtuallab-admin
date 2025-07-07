package com.edu.virtuallab.resource.service.impl;

import com.edu.virtuallab.resource.dao.ResourceShareDao;
import com.edu.virtuallab.resource.model.ResourceShare;
import com.edu.virtuallab.resource.service.ResourceShareService;
import com.edu.virtuallab.resource.service.ResourceService;
import com.edu.virtuallab.resource.model.Resource;
import com.edu.virtuallab.auth.service.UserService;
import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.notification.service.NotificationService;
import com.edu.virtuallab.notification.model.Notification;
import com.edu.virtuallab.common.enums.NotificationType;
import com.edu.virtuallab.common.exception.BusinessException;
import com.edu.virtuallab.resource.dto.ShareRequest;
import com.edu.virtuallab.resource.dto.ShareResponse;
import com.edu.virtuallab.resource.dto.ShareNotification;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class ResourceShareServiceImpl implements ResourceShareService {
    @Autowired
    private ResourceShareDao resourceShareDao;
    
    @Autowired
    private ResourceService resourceService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private NotificationService notificationService;

    @Override
    public String generateShareLink(Long resourceId, String sharedBy, Long expireMinutes) {
        try {
        String shareLink = UUID.randomUUID().toString().replace("-", "");
        ResourceShare share = new ResourceShare();
        share.setResourceId(resourceId);
        share.setSharedBy(sharedBy);
        share.setShareLink(shareLink);
        share.setCreatedAt(LocalDateTime.now());
        if (expireMinutes != null && expireMinutes > 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, expireMinutes.intValue());
            share.setExpiresAt(LocalDateTime.now().plusMinutes(expireMinutes));
        }
        resourceShareDao.insert(share);
        return shareLink;
        } catch (Exception e) {
            throw new BusinessException("生成分享链接失败: " + e.getMessage());
        }
    }

    @Override
    public ResourceShare getShareByLink(String shareLink) {
        try {
            // 这里需要在Dao里加一个selectByShareLink方法
            // 暂时返回null，后续实现
            return null;
        } catch (Exception e) {
            throw new BusinessException("获取分享信息失败: " + e.getMessage());
        }
    }

    @Override
    public List<ResourceShare> getSharesByResourceId(Long resourceId) {
        try {
        return resourceShareDao.selectByResourceId(resourceId);
        } catch (Exception e) {
            throw new BusinessException("获取资源分享列表失败: " + e.getMessage());
        }
    }

    @Override
    public boolean cancelShare(Long id, String sharedBy) {
        try {
        ResourceShare share = resourceShareDao.selectById(id);
        if (share == null || !Objects.equals(share.getSharedBy(), sharedBy)) {
                throw new BusinessException("无权限取消该分享");
            }
            return resourceShareDao.delete(id) > 0;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("取消分享失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ShareResponse shareResourceByUsername(Long resourceId, String sharedBy, String targetUsername, String permission) {
        try {
            // 新增：校验targetUsername不能为空
            if (targetUsername == null || targetUsername.trim().isEmpty()) {
                throw new BusinessException("被分享者用户名不能为空");
            }
            // 1. 验证资源是否存在
            Resource resource = resourceService.getById(resourceId);
            if (resource == null) {
                throw new BusinessException("资源不存在");
            }
            
            // 2. 允许所有人分享资源，移除上传者校验
            // if (resource.getUploader() == null) {
            //     throw new BusinessException("资源缺少上传者信息，无法分享");
            // }
            // if (!resource.getUploader().equals(sharedBy)) {
            //     throw new BusinessException("您没有权限分享该资源");
            // }
            
            // 3. 验证目标用户是否存在
            User targetUser = userService.getByUsername(targetUsername);
            if (targetUser == null) {
                throw new BusinessException("目标用户不存在");
            }
            
            // 4. 检查是否已经分享给该用户
            ResourceShare existingShare = resourceShareDao.selectByResourceAndUser(resourceId, targetUsername);
            if (existingShare != null) {
                throw new BusinessException("该资源已经分享给该用户");
            }
            
            // 5. 创建分享记录
            ResourceShare share = new ResourceShare();
            share.setResourceId(resourceId);
            share.setSharedBy(sharedBy);
            share.setSharedWith(targetUsername);
            share.setPermission(permission != null ? permission : ResourceShare.PERMISSION_READ);
            share.setStatus(ResourceShare.STATUS_ACTIVE);
            share.setCreatedAt(LocalDateTime.now());
            
            int result = resourceShareDao.insert(share);
            if (result <= 0) {
                throw new BusinessException("分享失败");
            }
            
            // 6. 发送通知给目标用户
            sendShareNotification(resource, targetUser, sharedBy);
            
            // 构造返回对象
            ShareResponse response = new ShareResponse();
            response.setId(share.getId());
            response.setResourceId(resourceId);
            response.setShareType("user");
            response.setPermission(share.getPermission());
            response.setCreatedAt(share.getCreatedAt());
            response.setStatus(share.getStatus());
            // 其它字段可按需补充
            return response;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("分享资源失败: " + e.getMessage());
        }
    }

    @Override
    public List<ResourceShare> getSharesBySharedBy(String sharedBy) {
        try {
            return resourceShareDao.selectBySharedBy(sharedBy);
        } catch (Exception e) {
            throw new BusinessException("获取分享列表失败: " + e.getMessage());
        }
    }

    @Override
    public List<ResourceShare> getSharesBySharedWith(String sharedWith) {
        try {
            return resourceShareDao.selectBySharedWith(sharedWith);
        } catch (Exception e) {
            throw new BusinessException("获取被分享列表失败: " + e.getMessage());
        }
    }

    @Override
    public boolean hasResourceAccess(Long resourceId, String username, String permission) {
        try {
            // 1. 检查是否是资源所有者
            Resource resource = resourceService.getById(resourceId);
            if (resource != null && resource.getUploader().equals(username)) {
                return true;
            }
            
            // 2. 检查分享权限
            ResourceShare share = resourceShareDao.selectByResourceAndUser(resourceId, username);
            if (share != null && ResourceShare.STATUS_ACTIVE.equals(share.getStatus())) {
                // 检查权限级别
                if (ResourceShare.PERMISSION_ADMIN.equals(share.getPermission())) {
                    return true;
                } else if (ResourceShare.PERMISSION_WRITE.equals(share.getPermission())) {
                    return ResourceShare.PERMISSION_READ.equals(permission) || 
                           ResourceShare.PERMISSION_WRITE.equals(permission);
                } else if (ResourceShare.PERMISSION_READ.equals(share.getPermission())) {
                    return ResourceShare.PERMISSION_READ.equals(permission);
                }
            }
            
            return false;
        } catch (Exception e) {
            throw new BusinessException("检查资源访问权限失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean revokeShare(Long shareId, String sharedBy) {
        try {
            ResourceShare share = resourceShareDao.selectById(shareId);
            if (share == null) {
                throw new BusinessException("分享记录不存在");
            }
            
            if (!share.getSharedBy().equals(sharedBy)) {
                throw new BusinessException("无权限撤销该分享");
            }
            
            // 软删除：将状态设置为inactive
            share.setStatus(ResourceShare.STATUS_INACTIVE);
            int result = resourceShareDao.update(share);
            
            if (result > 0) {
                // 发送撤销分享通知
                sendRevokeShareNotification(share);
            }
            
            return result > 0;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("撤销分享失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean updateSharePermission(Long shareId, String permission, String sharedBy) {
        try {
            ResourceShare share = resourceShareDao.selectById(shareId);
            if (share == null) {
                throw new BusinessException("分享记录不存在");
            }
            
            if (!share.getSharedBy().equals(sharedBy)) {
                throw new BusinessException("无权限修改该分享");
            }
            
            share.setPermission(permission);
            int result = resourceShareDao.update(share);
            
            if (result > 0) {
                // 发送权限更新通知
                sendPermissionUpdateNotification(share);
            }
            
            return result > 0;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("更新分享权限失败: " + e.getMessage());
        }
    }

    /**
     * 发送分享通知
     */
    private void sendShareNotification(Resource resource, User targetUser, String sharedBy) {
        try {
            Notification notification = new Notification();
            notification.setUserId(targetUser.getId());
            notification.setType(NotificationType.RESOURCE_SHARED);
            notification.setTitle("资源分享通知");
            notification.setContent(String.format("用户 %s 向您分享了资源【%s】", sharedBy, resource.getName()));
            notification.setLink("/resource/shared/" + resource.getId());
            notification.setRelatedId(resource.getId());
            notification.setCreatedAt(new Date());
            
            notificationService.createNotification(notification);
        } catch (Exception e) {
            // 通知发送失败不影响主流程
            System.err.println("发送分享通知失败: " + e.getMessage());
        }
    }

    /**
     * 发送撤销分享通知
     */
    private void sendRevokeShareNotification(ResourceShare share) {
        try {
            User targetUser = userService.getByUsername(share.getSharedWith());
            if (targetUser != null) {
                Resource resource = resourceService.getById(share.getResourceId());
                
                Notification notification = new Notification();
                notification.setUserId(targetUser.getId());
                notification.setType(NotificationType.RESOURCE_SHARE_REVOKED);
                notification.setTitle("分享撤销通知");
                notification.setContent(String.format("用户 %s 撤销了与您分享的资源【%s】", 
                    share.getSharedBy(), resource != null ? resource.getName() : "未知资源"));
                notification.setLink("/resource/shared");
                notification.setCreatedAt(new Date());
                
                notificationService.createNotification(notification);
            }
        } catch (Exception e) {
            System.err.println("发送撤销分享通知失败: " + e.getMessage());
        }
    }

    /**
     * 发送权限更新通知
     */
    private void sendPermissionUpdateNotification(ResourceShare share) {
        try {
            User targetUser = userService.getByUsername(share.getSharedWith());
            if (targetUser != null) {
                Resource resource = resourceService.getById(share.getResourceId());
                
                Notification notification = new Notification();
                notification.setUserId(targetUser.getId());
                notification.setType(NotificationType.RESOURCE_SHARE_PERMISSION_UPDATED);
                notification.setTitle("分享权限更新通知");
                notification.setContent(String.format("用户 %s 更新了与您分享的资源【%s】的权限为：%s", 
                    share.getSharedBy(), 
                    resource != null ? resource.getName() : "未知资源",
                    share.getPermission()));
                notification.setLink("/resource/shared/" + share.getResourceId());
                notification.setRelatedId(share.getResourceId());
                notification.setCreatedAt(new Date());
                
                notificationService.createNotification(notification);
            }
        } catch (Exception e) {
            System.err.println("发送权限更新通知失败: " + e.getMessage());
        }
    }

    // 新增：通用分享创建
    @Override
    @Transactional
    public ShareResponse createShare(ShareRequest request, String sharedBy) {
        if (request == null || request.getResourceId() == null || !StringUtils.hasText(request.getShareType())) {
            throw new BusinessException("参数不完整");
        }
        ResourceShare share = new ResourceShare();
        share.setResourceId(request.getResourceId());
        share.setSharedBy(sharedBy);
        share.setShareType(request.getShareType());
        share.setPermission(request.getPermission() != null ? request.getPermission() : ResourceShare.PERMISSION_READ);
        share.setStatus(ResourceShare.STATUS_ACTIVE);
        share.setCreatedAt(LocalDateTime.now());
        share.setExpiresAt(request.getExpiresAt());
        share.setShareTitle(request.getShareTitle());
        share.setShareDescription(request.getShareDescription());
        share.setShareImage(request.getShareImage());
        share.setViewCount(0);
        share.setDownloadCount(0);
        // 链接分享生成唯一链接和分享码
        if (ResourceShare.SHARE_TYPE_LINK.equals(request.getShareType())) {
            share.setShareLink(UUID.randomUUID().toString().replace("-", ""));
            share.setShareCode(UUID.randomUUID().toString().substring(0, 8));
        }
        // 用户分享
        if (ResourceShare.SHARE_TYPE_USER.equals(request.getShareType()) && !CollectionUtils.isEmpty(request.getUserIds())) {
            for (String userId : request.getUserIds()) {
                ResourceShare userShare = new ResourceShare();
                BeanUtils.copyProperties(share, userShare);
                userShare.setSharedWith(userId);
                resourceShareDao.insert(userShare);
                sendShareNotification(sharedBy, userId, request.getResourceId(), ResourceShare.SHARE_TYPE_USER);
            }
        } else if (ResourceShare.SHARE_TYPE_CLASS.equals(request.getShareType()) && !CollectionUtils.isEmpty(request.getClassIds())) {
            for (String classId : request.getClassIds()) {
                ResourceShare classShare = new ResourceShare();
                BeanUtils.copyProperties(share, classShare);
                classShare.setSharedWith(classId);
                resourceShareDao.insert(classShare);
                sendShareNotification(sharedBy, classId, request.getResourceId(), ResourceShare.SHARE_TYPE_CLASS);
            }
        } else if (ResourceShare.SHARE_TYPE_LINK.equals(request.getShareType())) {
            resourceShareDao.insert(share);
        } else {
            throw new BusinessException("不支持的分享类型或缺少目标");
        }
        ShareResponse resp = new ShareResponse();
        BeanUtils.copyProperties(share, resp);
        return resp;
    }

    @Override
    public ShareResponse createClassShare(Long resourceId, String sharedBy, String classId, String permission) {
        ShareRequest req = new ShareRequest();
        req.setResourceId(resourceId);
        req.setShareType(ResourceShare.SHARE_TYPE_CLASS);
        req.setClassIds(Collections.singletonList(classId));
        req.setPermission(permission);
        return createShare(req, sharedBy);
    }

    @Override
    public ShareResponse createLinkShare(Long resourceId, String sharedBy, String title, String description) {
        ShareRequest req = new ShareRequest();
        req.setResourceId(resourceId);
        req.setShareType(ResourceShare.SHARE_TYPE_LINK);
        req.setShareTitle(title);
        req.setShareDescription(description);
        return createShare(req, sharedBy);
    }

    @Override
    public boolean deleteShare(Long shareId, String sharedBy) {
        ResourceShare share = resourceShareDao.selectById(shareId);
        if (share == null || !Objects.equals(share.getSharedBy(), sharedBy)) {
            throw new BusinessException("无权限删除该分享");
        }
        return resourceShareDao.delete(shareId) > 0;
    }

    @Override
    public List<ShareResponse> getMyShares(String username) {
        List<ResourceShare> list = resourceShareDao.selectBySharedBy(username);
        List<ShareResponse> resp = new ArrayList<>();
        for (ResourceShare share : list) {
            ShareResponse r = new ShareResponse();
            BeanUtils.copyProperties(share, r);
            resp.add(r);
        }
        return resp;
    }

    @Override
    public List<ShareResponse> getSharedWithMe(String username) {
        List<ResourceShare> list = resourceShareDao.selectBySharedWith(username);
        List<ShareResponse> resp = new ArrayList<>();
        for (ResourceShare share : list) {
            ShareResponse r = new ShareResponse();
            BeanUtils.copyProperties(share, r);
            resp.add(r);
        }
        return resp;
    }

    @Override
    public ShareResponse getShareById(Long shareId) {
        ResourceShare share = resourceShareDao.selectById(shareId);
        if (share == null) return null;
        ShareResponse resp = new ShareResponse();
        BeanUtils.copyProperties(share, resp);
        return resp;
    }

    @Override
    public boolean updateShare(Long shareId, ShareRequest request, String sharedBy) {
        ResourceShare share = resourceShareDao.selectById(shareId);
        if (share == null || !Objects.equals(share.getSharedBy(), sharedBy)) {
            throw new BusinessException("无权限修改该分享");
        }
        if (request.getPermission() != null) share.setPermission(request.getPermission());
        if (request.getExpiresAt() != null) share.setExpiresAt(request.getExpiresAt());
        if (StringUtils.hasText(request.getShareTitle())) share.setShareTitle(request.getShareTitle());
        if (StringUtils.hasText(request.getShareDescription())) share.setShareDescription(request.getShareDescription());
        if (StringUtils.hasText(request.getShareImage())) share.setShareImage(request.getShareImage());
        return resourceShareDao.update(share) > 0;
    }

    @Override
    public void incrementViewCount(Long shareId) {
        resourceShareDao.updateViewCount(shareId);
    }

    @Override
    public void incrementDownloadCount(Long shareId) {
        resourceShareDao.updateDownloadCount(shareId);
    }

    @Override
    public List<ResourceShare> getExpiredShares() {
        return resourceShareDao.selectExpiredShares();
    }

    @Override
    public void cleanupExpiredShares() {
        List<ResourceShare> expired = getExpiredShares();
        for (ResourceShare share : expired) {
            share.setStatus(ResourceShare.STATUS_EXPIRED);
            resourceShareDao.update(share);
        }
    }

    // 通知相关
    @Override
    public void sendShareNotification(String sender, String receiver, Long resourceId, String shareType) {
        // 这里可以插入share_notifications表，或调用通知服务
        // 示例：
        // Notification notification = new Notification();
        // notification.setUserId(receiver);
        // notification.setType("share");
        // notification.setTitle("资源分享通知");
        // notification.setContent(String.format("用户 %s 向您分享了资源", sender));
        // notification.setLink("/resource/shared/" + resourceId);
        // notificationService.createNotification(notification);
    }

    @Override
    public List<ShareNotification> getUserNotifications(String username) {
        return resourceShareDao.selectNotificationsByReceiver(username);
    }

    @Override
    public List<ShareNotification> getUnreadNotifications(String username) {
        return resourceShareDao.selectUnreadNotifications(username);
    }

    @Override
    public boolean markNotificationAsRead(Long notificationId, String username) {
        return resourceShareDao.markNotificationAsRead(notificationId) > 0;
    }

    @Override
    public boolean markAllNotificationsAsRead(String username) {
        return resourceShareDao.markAllNotificationsAsRead(username) > 0;
    }

    @Override
    public int getUnreadNotificationCount(String username) {
        List<ShareNotification> list = getUnreadNotifications(username);
        return list == null ? 0 : list.size();
    }

    // 统计相关
    @Override
    public int getShareCountByResource(Long resourceId) {
        return resourceShareDao.countByResourceId(resourceId);
    }

    @Override
    public int getShareCountByUser(String username) {
        return resourceShareDao.countBySharedBy(username);
    }

    @Override
    public int getClassShareCount(String classId) {
        return resourceShareDao.countBySharedWith(classId);
    }

    @Override
    public int getLinkShareCount() {
        return resourceShareDao.countByShareType(ResourceShare.SHARE_TYPE_LINK);
    }
} 