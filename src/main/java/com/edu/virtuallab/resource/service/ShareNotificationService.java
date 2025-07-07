package com.edu.virtuallab.resource.service;

import com.edu.virtuallab.resource.dto.ShareNotification;
import java.util.List;

public interface ShareNotificationService {
    List<ShareNotification> getUnreadNotifications(String username);
    List<ShareNotification> getAllNotifications(String username);
    boolean markRead(Long notificationId);
    boolean markAllRead(String username);
    void createNotification(ShareNotification notification);
} 