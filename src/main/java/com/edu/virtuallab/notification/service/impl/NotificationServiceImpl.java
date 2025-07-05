package com.edu.virtuallab.notification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.virtuallab.notification.model.PageResult;
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

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.mysql.cj.conf.PropertyKey.logger;

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
    public void sendProjectAuditNotification(Long projectId, String teacherUsername) {
        // 根据用户名查找用户信息
        User teacher = userDao.findByUsername(teacherUsername);
        if (teacher == null) {
            // 如果找不到用户，使用默认名称
            teacher = new User();
            teacher.setRealName(teacherUsername); // 使用用户名作为默认显示名称
        }

        // 2. 获取院系信息
        String department = teacher.getDepartment();
        if (department == null || department.isEmpty()) {
            department = "未分配院系";
        }

        // +++ 获取项目名称 +++
        ExperimentProject project = experimentProjectDao.selectById(projectId);
        if (project == null) return;

        Notification notification = new Notification();
        notification.setType(NotificationType.fromValue("project_submitted"));
        notification.setTitle("新实验项目待审核");
        notification.setContent(String.format(
                "[%s]教师 %s 提交了实验项目【%s】等待审核", // +++ 添加项目名称 +++
                department,
                teacher.getRealName(), // 使用老师的真实姓名
                project.getName()  // 显示项目名称
        ));
        notification.setLink("/admin/experiment/project/audit/" + projectId);
        // 显式设置创建时间为当前时间
        notification.setCreatedAt(new Date());  // 添加这一行
        notification.setRelatedId(projectId); // 关联项目ID

        // 5. 查询院系管理员 (role_id = 2)
        List<User> departmentAdmins = userDao.findByDepartmentAndRoleId(department, 2L);

        // 6. 发送通知
        for (User admin : departmentAdmins) {
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

        // 1. 获取项目信息
        ExperimentProject project = experimentProjectDao.selectById(projectId);
        if (project == null) return;

        // 2. 获取老师用户名
        String teacherUsername = project.getCreatedBy();
        if (teacherUsername == null || teacherUsername.isEmpty()) {
            return;
        }
        // 3. 根据用户名查找老师信息
        User teacher = userDao.findByUsername(teacherUsername);
        if (teacher == null) {
            return;
        }

        // 4. 创建通知
        Notification notification = new Notification();
        notification.setType(approved ?
                NotificationType.PROJECT_APPROVED :
                NotificationType.PROJECT_REJECTED);
        notification.setTitle(approved ? "实验项目审核通过" : "实验项目审核驳回");
        notification.setContent(approved ?
                "您的实验项目已通过审核" :
                "您的实验项目被驳回" + (comment != null ? "，原因：" + comment : ""));
        notification.setLink("/experiment/project/detail/" + projectId);
        notification.setUserId(teacher.getId()); // 设置接收通知的用户ID
        notification.setCreatedAt(new Date()); // 设置创建时间
        notification.setRelatedId(projectId); // 关联项目ID

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
        notification.setCreatedAt(new Date());
        notification.setRelatedId(projectId); // 关联项目ID

        // 获取所有相关班级的师生 - 需要实现 findUserIdsByClassIds() 方法
        List<Long> userIds = userDao.findUserIdsByClassIds(classIds);
        for (Long userId : userIds) {
            Notification copy = notification.copy();
            copy.setUserId(userId);
            notificationMapper.insert(copy);
        }
    }

    // 创建通知
    @Override
    public Notification createNotification(Notification notification) {
        // 设置默认值
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(new Date());
        }
        if (notification.getIsRead() == null) {
            notification.setIsRead(false);
        }

        // 插入数据库
        notificationMapper.insert(notification);

        // 返回包含ID的完整通知对象
        return notificationMapper.selectById(notification.getId());
    }

    // 获取未读通知
    @Override
    public List<Notification> getUnreadNotifications(Long userId) {
        // 验证用户ID有效性
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("无效的用户ID");
        }

        // 查询未读通知
        return notificationMapper.findUnreadByUserId(userId);
    }

    // 标记通知为已读
    @Override
    public void markAsRead(Long notificationId) {
        // 验证通知ID有效性
        if (notificationId == null || notificationId <= 0) {
            throw new IllegalArgumentException("无效的通知ID");
        }

        // 检查通知是否存在
        Notification notification = notificationMapper.selectById(notificationId);
        if (notification == null) {
            throw new RuntimeException("通知不存在: " + notificationId);
        }

        // 更新为已读状态
        notification.setIsRead(true);
        notificationMapper.update(notification);
    }


    @Override
    public void deleteNotification(Long notificationId) {
        if (notificationId == null || notificationId <= 0) {
            throw new IllegalArgumentException("无效的通知ID");
        }
        notificationMapper.delete(notificationId);
    }

    @Override
    public Notification getNotification(Long notificationId) {
        if (notificationId == null || notificationId <= 0) {
            throw new IllegalArgumentException("无效的通知ID");
        }
        return notificationMapper.selectById(notificationId);
    }

    @Override
    public void markAllAsRead(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("无效的用户ID");
        }
        notificationMapper.markAllAsRead(userId);
    }

    @Override
    public List<Notification> getAllNotifications(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("无效的用户ID");
        }

        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .orderByDesc("created_at");

        return notificationMapper.selectList(queryWrapper);
    }
}
