package com.edu.virtuallab.resource.dao;

import com.edu.virtuallab.resource.model.ResourceShare;
import com.edu.virtuallab.resource.dto.ShareNotification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ResourceShareDao {
    int insert(ResourceShare resourceShare);
    int update(ResourceShare resourceShare);
    int delete(Long id);
    ResourceShare selectById(Long id);
    List<ResourceShare> selectByResourceId(Long resourceId);
    List<ResourceShare> selectBySharedWith(String sharedWith);
    List<ResourceShare> selectBySharedBy(String sharedBy);
    ResourceShare selectByResourceAndUser(@Param("resourceId") Long resourceId, @Param("sharedWith") String sharedWith);
    
    // 新增方法
    ResourceShare selectByShareLink(String shareLink);
    ResourceShare selectByShareCode(String shareCode);
    List<ResourceShare> selectByShareType(String shareType);
    List<ResourceShare> selectByClassId(String classId);
    List<ResourceShare> selectByUserId(String userId);
    List<ResourceShare> selectExpiredShares();
    int updateViewCount(Long id);
    int updateDownloadCount(Long id);
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    // 统计方法
    int countByResourceId(Long resourceId);
    int countBySharedBy(String sharedBy);
    int countBySharedWith(String sharedWith);
    int countByShareType(String shareType);
    
    // 通知相关
    List<ShareNotification> selectNotificationsByReceiver(String receiver);
    List<ShareNotification> selectUnreadNotifications(String receiver);
    int markNotificationAsRead(Long notificationId);
    int markAllNotificationsAsRead(String receiver);
}