package com.edu.virtuallab.audit.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.virtuallab.audit.dao.ExperimentProjectAuditLogMapper;
import com.edu.virtuallab.audit.dto.AuditLogDTO;
import com.edu.virtuallab.audit.service.AuditLogService;
import com.edu.virtuallab.auth.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final ExperimentProjectAuditLogMapper auditLogMapper;
    private final UserDao userDao;

    @Autowired
    public AuditLogServiceImpl(ExperimentProjectAuditLogMapper auditLogMapper,
                               UserDao userDao) {
        this.auditLogMapper = auditLogMapper;
        this.userDao = userDao;
    }

    @Override
    public Page<AuditLogDTO> getAuditLogsByDepartment(
            String department,
            String keyword,
            int pageNum,
            int pageSize) {

        // 获取院系所有用户名
        List<String> usernames = userDao.findUsernamesByDepartment(department);
        if (usernames.isEmpty()) {
            return new Page<>(pageNum, pageSize, 0);
        }

        // 创建分页对象
        Page<AuditLogDTO> page = new Page<>(pageNum, pageSize);

        // 计算偏移量
        long offset = (long) (pageNum - 1) * pageSize;

        // 执行查询
        List<AuditLogDTO> logs = auditLogMapper.findAuditLogsByDepartment(
                usernames,
                keyword,
                offset,
                pageSize
        );

        // 设置分页数据
        page.setRecords(logs);

        // 查询总数（需要单独实现）
        long total = countAuditLogsByDepartment(usernames, keyword);
        page.setTotal(total);

        return page;
    }

    // 新增：查询总数的方法
    private long countAuditLogsByDepartment(List<String> usernames, String keyword) {
        // 实现总数查询逻辑
        return auditLogMapper.countAuditLogsByDepartment(usernames, keyword);
    }
}
