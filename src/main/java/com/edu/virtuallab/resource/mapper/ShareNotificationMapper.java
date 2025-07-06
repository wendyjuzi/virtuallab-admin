package com.edu.virtuallab.resource.mapper;

import com.edu.virtuallab.resource.dto.ShareNotification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ShareNotificationMapper {
    List<ShareNotification> selectUnreadNotifications(@Param("username") String username);
    List<ShareNotification> selectAllNotifications(@Param("username") String username);
    int updateReadStatus(@Param("notificationId") Long notificationId, @Param("read") boolean read);
    int updateAllReadStatus(@Param("username") String username, @Param("read") boolean read);
    int insertNotification(ShareNotification notification);
} 