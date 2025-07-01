package com.edu.virtuallab.notification.service;

import com.edu.virtuallab.common.api.PageResult;
import com.edu.virtuallab.notification.model.Notification;
import java.util.List;

public interface NotificationService {

    // 创建通知
    Notification createNotification(Notification notification);

    // 获取用户未读通知
    List<Notification> getUnreadNotifications(Long userId);

    // 标记通知为已读
    void markAsRead(Long notificationId);

    // 发送项目审核通知（修改方法签名以匹配实现）
    void sendProjectAuditNotification(Long projectId, Long uploaderId);

    // 添加缺失的方法声明
    void sendProjectAuditResultNotification(
            Long projectId,
            boolean approved,
            String comment);

    void sendProjectPublishNotification(Long projectId, List<Long> classIds);

    void deleteNotification(Long notificationId);
    Notification getNotification(Long notificationId);
    void markAllAsRead(Long userId);
    // 在 NotificationService 接口中添加方法声明
    List<Notification> getAllNotifications(Long userId);
}