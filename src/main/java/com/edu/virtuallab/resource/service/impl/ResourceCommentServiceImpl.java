package com.edu.virtuallab.resource.service.impl;

import com.edu.virtuallab.resource.dao.ResourceCommentDao;
import com.edu.virtuallab.resource.model.ResourceComment;
import com.edu.virtuallab.resource.service.ResourceCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ResourceCommentServiceImpl implements ResourceCommentService {
    @Autowired
    private ResourceCommentDao resourceCommentDao;
    @Autowired
    private com.edu.virtuallab.auth.dao.UserRoleDao userRoleDao;
    @Autowired
    private com.edu.virtuallab.auth.dao.RoleDao roleDao;

    @Override
    public int addComment(ResourceComment comment) {
        if (comment.getContent() == null || comment.getContent().trim().isEmpty() || comment.getContent().length() > 200) {
            throw new RuntimeException("评论内容不能为空且不超过200字");
        }
        comment.setCreateTime(new java.util.Date());
        return resourceCommentDao.insert(comment);
    }

    @Override
    public List<ResourceComment> getCommentsByResourceId(Long resourceId) {
        return resourceCommentDao.getCommentsByResourceId(resourceId);
    }

    @Override
    public ResourceComment getCommentById(Long id) {
        return resourceCommentDao.selectById(id);
    }

    @Override
    public int updateComment(ResourceComment comment) {
        return resourceCommentDao.update(comment);
    }

    @Override
    public int deleteComment(Long id, Long userId) {
        // 仅管理员可删任意，普通用户只能删自己
        boolean isAdmin = false;
        if (userId != null) {
            // 通过 user_role 联查是否为管理员
            java.util.List<Long> roleIds = userRoleDao.findRoleIdsByUserId(userId);
            if (roleIds != null && !roleIds.isEmpty()) {
                java.util.List<String> adminCodes = roleDao.findCodesByIds(roleIds);
                isAdmin = adminCodes.stream().anyMatch(code -> code.equals("SYSTEM_ADMIN"));
            }
        }
        ResourceComment comment = resourceCommentDao.selectById(id);
        if (comment == null) throw new RuntimeException("评论不存在");
        if (!isAdmin && (userId == null || !userId.equals(comment.getUserId()))) {
            throw new RuntimeException("无权限删除他人评论");
        }
        return resourceCommentDao.deleteById(id);
    }
}