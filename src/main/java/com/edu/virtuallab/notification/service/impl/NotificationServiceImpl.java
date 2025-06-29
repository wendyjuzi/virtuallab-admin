package com.edu.virtuallab.notification.service.impl;

import com.edu.virtuallab.common.enums.NotificationType;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import com.edu.virtuallab.experiment.dao.ExperimentProjectDao; // 需要添加的导入
import com.edu.virtuallab.notification.dao.NotificationMapper;
import com.edu.virtuallab.notification.model.Notification;
import com.edu.virtuallab.notification.service.NotificationService;
import com.edu.virtuallab.auth.dao.UserDao;
import com.edu.virtuallab.auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;
    private final UserDao userDao;
    private final ExperimentProjectDao experimentProjectDao; // 添加的依赖

    @Autowired
    public NotificationServiceImpl(NotificationMapper notificationMapper,
                                   UserDao userDao,
                                   ExperimentProjectDao experimentProjectDao) {
        this.notificationMapper = notificationMapper;
        this.userDao = userDao;
        this.experimentProjectDao = experimentProjectDao;
    }

    @Override
    public void sendProjectAuditNotification(Long projectId, Long uploaderId) {
        User uploader = userDao.findById(uploaderId);
        if (uploader == null) return;

        Notification notification = new Notification();
        notification.setType(NotificationType.fromValue("project_submitted"));
        notification.setTitle("新实验项目待审核");
        notification.setContent(String.format(
                "教师 %s 提交了新实验项目等待审核",
                uploader.getRealName()
        ));
        notification.setLink("/admin/experiment/project/audit/" + projectId);

        // 发送给所有管理员 - 需要实现 findAdmins() 方法
        List<User> admins = userDao.findAdmins();
        for (User admin : admins) {
            Notification copy = notification.copy();
            copy.setUserId(admin.getId());
            notificationMapper.insert(copy);
        }
    }

    @Override
    public void sendProjectAuditResultNotification(
            Long projectId,
            boolean approved,
            String comment) {

        ExperimentProject project = experimentProjectDao.selectById(projectId);
        if (project == null) return;

        Notification notification = new Notification();
        notification.setType(approved ?
                NotificationType.PROJECT_APPROVED :
                NotificationType.PROJECT_REJECTED);
        notification.setTitle(approved ? "实验项目审核通过" : "实验项目审核驳回");
        notification.setContent(approved ?
                "您的实验项目已通过审核" :
                "您的实验项目被驳回" + (comment != null ? "，原因：" + comment : ""));
        notification.setLink("/experiment/project/detail/" + projectId);
        notification.setUserId(project.getUploaderId());

        notificationMapper.insert(notification);
    }

    @Override
    public void sendProjectPublishNotification(Long projectId, List<Long> classIds) {
        ExperimentProject project = experimentProjectDao.selectById(projectId);
        if (project == null) return;

        Notification notification = new Notification();
        notification.setType(NotificationType.PROJECT_PUBLISHED);
        notification.setTitle("实验项目已发布");
        notification.setContent(String.format(
                "实验项目【%s】已发布到您的班级",
                project.getName()
        ));
        notification.setLink("/experiment/project/detail/" + projectId);

        // 获取所有相关班级的师生 - 需要实现 findUserIdsByClassIds() 方法
        List<Long> userIds = userDao.findUserIdsByClassIds(classIds);
        for (Long userId : userIds) {
            Notification copy = notification.copy();
            copy.setUserId(userId);
            notificationMapper.insert(copy);
        }
    }

    // 实现接口的其他方法
    @Override
    public Notification createNotification(Notification notification) {
        notificationMapper.insert(notification);
        return notification;
    }

    @Override
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationMapper.findUnreadByUserId(userId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        notificationMapper.markAsRead(notificationId);
    }
}
