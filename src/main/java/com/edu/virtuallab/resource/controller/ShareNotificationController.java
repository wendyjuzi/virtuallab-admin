package com.edu.virtuallab.resource.controller;

import com.edu.virtuallab.resource.dto.ShareNotification;
import com.edu.virtuallab.resource.service.ShareNotificationService;
import com.edu.virtuallab.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/resource/share/notifications")
public class ShareNotificationController {

    @Autowired
    private ShareNotificationService shareNotificationService;

    // 获取未读通知
    @GetMapping("/unread")
    public CommonResult<List<ShareNotification>> getUnreadShareNotifications(@RequestParam String username) {
        List<ShareNotification> notifications = shareNotificationService.getUnreadNotifications(username);
        return CommonResult.success(notifications, "查询成功");
    }

    // 获取所有通知
    @GetMapping
    public CommonResult<List<ShareNotification>> getAllShareNotifications(@RequestParam String username) {
        List<ShareNotification> notifications = shareNotificationService.getAllNotifications(username);
        return CommonResult.success(notifications, "查询成功");
    }

    // 标记单条为已读
    @PostMapping("/{notificationId}/read")
    public CommonResult<Boolean> markRead(@PathVariable Long notificationId) {
        boolean result = shareNotificationService.markRead(notificationId);
        return CommonResult.success(result, "标记成功");
    }

    // 标记全部为已读
    @PostMapping("/read-all")
    public CommonResult<Boolean> markAllRead(@RequestParam String username) {
        boolean result = shareNotificationService.markAllRead(username);
        return CommonResult.success(result, "全部标记成功");
    }
} 