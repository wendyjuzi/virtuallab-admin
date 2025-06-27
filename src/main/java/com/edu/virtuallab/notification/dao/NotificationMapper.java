package com.edu.virtuallab.notification.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.virtuallab.notification.model.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {

    // 没有写过程
    List<Notification> selectUnreadByUserId(@Param("userId") Long userId);

    // ?status可能有问题
    int updateStatus(@Param("notificationId") Long notificationId,
                     @Param("status") Boolean status);

}
