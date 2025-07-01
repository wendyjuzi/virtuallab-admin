package com.edu.virtuallab.notification.controller;

import com.edu.virtuallab.common.api.PageResult;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.common.api.ResultCode;
import com.edu.virtuallab.notification.model.Notification;
import com.edu.virtuallab.notification.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@Api(tags = "通知管理")
public class NotificationController {

    //删除通知有问题//获取通知详情有问题//创建通知？

    @Autowired
    private NotificationService notificationService;

    private Long getCurrentUserId() {
        // 从安全上下文中获取当前用户ID
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @ApiOperation("获取用户未读通知")
    @GetMapping("/unread")
    public CommonResult<List<Notification>> getUnreadNotifications(@RequestParam Long userId) {
        List<Notification> unread = notificationService.getUnreadNotifications(userId);
        return CommonResult.success(unread);
    }

    @ApiOperation("获取用户所有通知")
    @GetMapping("/all")
    public CommonResult<List<Notification>> getAllNotifications(
            @RequestParam Long userId) {
        // 调用服务层方法
        List<Notification> notifications = notificationService.getAllNotifications(userId);
        return CommonResult.success(notifications);
    }

    @ApiOperation("标记通知为已读")
    @PostMapping("/mark-as-read/{notificationId}")
    public CommonResult<Void> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        // 修改：使用构造方法创建Void结果
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), "通知已标记为已读", null);
    }

    @ApiOperation("批量标记为已读")
    @PostMapping("/mark-all-read")
    public CommonResult<Void> markAllAsRead(@RequestParam Long userId) {
        notificationService.markAllAsRead(userId);
        return CommonResult.success(null);
    }

    @ApiOperation("删除通知")
    @DeleteMapping("/{notificationId}")
    public CommonResult<Void> deleteNotification(@PathVariable Long notificationId) {
        // 实现删除通知
        notificationService.deleteNotification(notificationId);
        // 修改：使用构造方法创建Void结果
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), "通知已删除", null);
    }

    @ApiOperation("获取通知详情")
    @GetMapping("/{notificationId}")
    public CommonResult<Notification> getNotificationDetail(@PathVariable Long notificationId) {
        // 实现获取详情
        Notification notification = notificationService.getNotification(notificationId);
        return CommonResult.success(notification);
        // 使用构造方法返回带消息的未实现提示
    }

    @ApiOperation("创建通知（内部使用）")
    @PostMapping("/internal/create")
    public CommonResult<Notification> createNotification(@RequestBody Notification notification) {
        Notification created = notificationService.createNotification(notification);
        return CommonResult.success(created);
    }
}