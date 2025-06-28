package com.edu.virtuallab.notification.service;

import com.edu.virtuallab.common.enums.NotificationType;
import com.edu.virtuallab.notification.model.Notification;

import java.util.List;

public interface NotificationService {

    // 创建通知
    Notification createNotification(Notification notification);

    // 获取用户未读通知
    List<Notification> getUnreadNotifications(Long userId);

    // 标记通知为已读
    void markAsRead(Long notificationId);

    // 发送项目审核通知
    void sendProjectAuditNotification(Long projectId, NotificationType type, Long receiverId);
}