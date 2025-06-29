package com.edu.virtuallab.auth.service.impl;

import com.edu.virtuallab.auth.dao.EmailVerificationCodeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 邮箱验证码清理任务
 */
@Component
public class EmailVerificationCleanupTask {

    private static final Logger logger = LoggerFactory.getLogger(EmailVerificationCleanupTask.class);

    @Autowired
    private EmailVerificationCodeDao emailVerificationCodeDao;

    /**
     * 每天凌晨2点清理过期的验证码
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredCodes() {
        try {
            int deletedCount = emailVerificationCodeDao.deleteExpiredCodes();
            logger.info("清理过期验证码完成，共删除 {} 条记录", deletedCount);
        } catch (Exception e) {
            logger.error("清理过期验证码失败", e);
        }
    }
} 