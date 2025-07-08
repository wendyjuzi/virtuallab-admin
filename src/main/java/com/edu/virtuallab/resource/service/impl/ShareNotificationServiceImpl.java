package com.edu.virtuallab.resource.service.impl;

import com.edu.virtuallab.resource.dto.ShareNotification;
import com.edu.virtuallab.resource.mapper.ShareNotificationMapper;
import com.edu.virtuallab.resource.service.ShareNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShareNotificationServiceImpl implements ShareNotificationService {

    @Autowired
    private ShareNotificationMapper shareNotificationMapper;

    @Override
    public List<ShareNotification> getUnreadNotifications(String username) {
        return shareNotificationMapper.selectUnreadNotifications(username);
    }

    @Override
    public List<ShareNotification> getAllNotifications(String username) {
        return shareNotificationMapper.selectAllNotifications(username);
    }

    @Override
    public boolean markRead(Long notificationId) {
        return shareNotificationMapper.updateReadStatus(notificationId, true) > 0;
    }

    @Override
    public boolean markAllRead(String username) {
        return shareNotificationMapper.updateAllReadStatus(username, true) > 0;
    }

    @Override
    public void createNotification(ShareNotification notification) {
        shareNotificationMapper.insertNotification(notification);
    }
} 