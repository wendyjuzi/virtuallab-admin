package com.edu.virtuallab.resource.service;

import com.edu.virtuallab.resource.model.ResourceShare;
import com.edu.virtuallab.resource.dto.ShareRequest;
import com.edu.virtuallab.resource.dto.ShareResponse;
import com.edu.virtuallab.resource.dto.ShareNotification;
import java.util.List;

public interface ResourceShareService {
    String generateShareLink(Long resourceId, String sharedBy, Long expireMinutes);
    ResourceShare getShareByLink(String shareLink);
    List<ResourceShare> getSharesByResourceId(Long resourceId);
    boolean cancelShare(Long id, String sharedBy);
    
    // 新增：通过用户名分享资源
    boolean shareResourceByUsername(Long resourceId, String sharedBy, String targetUsername, String permission);
    
    // 新增：获取用户分享给其他人的资源列表
    List<ResourceShare> getSharesBySharedBy(String sharedBy);
    
    // 新增：获取分享给用户的资源列表
    List<ResourceShare> getSharesBySharedWith(String sharedWith);
    
    // 新增：检查用户是否有资源的访问权限
    boolean hasResourceAccess(Long resourceId, String username, String permission);
    
    // 新增：撤销分享
    boolean revokeShare(Long shareId, String sharedBy);
    
    // 新增：更新分享权限
    boolean updateSharePermission(Long shareId, String permission, String sharedBy);
    
    // 新增方法
    ShareResponse createShare(ShareRequest request, String sharedBy);
    ShareResponse createClassShare(Long resourceId, String sharedBy, String classId, String permission);
    ShareResponse createLinkShare(Long resourceId, String sharedBy, String title, String description);
    boolean deleteShare(Long shareId, String sharedBy);
    List<ShareResponse> getMyShares(String username);
    List<ShareResponse> getSharedWithMe(String username);
    ShareResponse getShareById(Long shareId);
    boolean updateShare(Long shareId, ShareRequest request, String sharedBy);
    void incrementViewCount(Long shareId);
    void incrementDownloadCount(Long shareId);
    List<ResourceShare> getExpiredShares();
    void cleanupExpiredShares();
    
    // 通知相关
    void sendShareNotification(String sender, String receiver, Long resourceId, String shareType);
    List<ShareNotification> getUserNotifications(String username);
    List<ShareNotification> getUnreadNotifications(String username);
    boolean markNotificationAsRead(Long notificationId, String username);
    boolean markAllNotificationsAsRead(String username);
    int getUnreadNotificationCount(String username);
    
    // 统计相关
    int getShareCountByResource(Long resourceId);
    int getShareCountByUser(String username);
    int getClassShareCount(String classId);
    int getLinkShareCount();
} 