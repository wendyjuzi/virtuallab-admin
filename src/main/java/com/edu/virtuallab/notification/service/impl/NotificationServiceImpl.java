package com.edu.virtuallab.notification.service.impl;

import com.edu.virtuallab.common.enums.NotificationType;
import com.edu.virtuallab.notification.dao.NotificationMapper;
import com.edu.virtuallab.notification.model.Notification;
import com.edu.virtuallab.notification.service.NotificationService;
import com.edu.virtuallab.project.model.Project;
import com.edu.virtuallab.project.dao.ProjectDao;
import com.edu.virtuallab.auth.dao.UserDao;
import com.edu.virtuallab.auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private UserDao userDao;

    @Override
    public void sendProjectAuditNotification(Long projectId, NotificationType type, Long receiverId) {
        Project project = projectDao.selectById(projectId);
        if (project == null) return;

        Notification notification = new Notification();
        notification.setType(type);
        notification.setRelatedId(projectId);

        switch (type) {
            case PROJECT_SUBMITTED:
                User teacher = userDao.findById(project.getUploaderId());
                notification.setTitle("新项目待审核");
                notification.setContent(String.format(
                        "教师 %s 提交了新项目【%s】等待审核",
                        teacher.getRealName(),
                        project.getName()
                ));
                // 系统通知，不需要特定接收者
                break;

            case PROJECT_APPROVED:
                notification.setUserId(project.getUploaderId());
                notification.setTitle("项目审核通过");
                notification.setContent(String.format(
                        "您的项目【%s】已通过审核",
                        project.getName()
                ));
                break;

            case PROJECT_REJECTED:
                notification.setUserId(project.getUploaderId());
                notification.setTitle("项目审核驳回");
                notification.setContent(String.format(
                        "您的项目【%s】被驳回，原因：%s",
                        project.getName(),
                        project.getAuditComment()
                ));
                break;

            case PROJECT_PUBLISHED:
                notification.setUserId(receiverId);
                notification.setTitle("新实训项目发布");
                notification.setContent(String.format(
                        "您有新的实训项目【%s】待完成",
                        project.getName()
                ));
                break;
        }

        notification.setLink("/project/detail/" + projectId);
        notificationMapper.insert(notification);
    }

    @Override
    public Notification createNotification(Notification notification) {
        notificationMapper.insert(notification);
        return notification; // 返回插入后的对象（包含生成的ID）
    }

    @Override
    public List<Notification> getUnreadNotifications(Long userId) {
        // 实现获取未读通知的逻辑
        return notificationMapper.selectUnreadByUserId(userId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        // 实现标记为已读的逻辑
        notificationMapper.updateStatus(notificationId, true); // 假设true表示已读
    }
}