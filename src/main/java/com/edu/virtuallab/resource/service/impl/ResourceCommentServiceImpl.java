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
    public int deleteComment(Long id) {
        // 仅管理员可删任意，普通用户只能删自己
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new RuntimeException("请先登录");
        }
        Object principal = auth.getPrincipal();
        boolean isAdmin = false;
        Long currentUserId = null;
        if (principal instanceof com.edu.virtuallab.auth.model.User) {
            com.edu.virtuallab.auth.model.User user = (com.edu.virtuallab.auth.model.User) principal;
            currentUserId = user.getId();
            isAdmin = "SYSTEM_ADMIN".equals(user.getRoleList() != null && !user.getRoleList().isEmpty() ? user.getRoleList().get(0).getCode() : "");
        }
        ResourceComment comment = resourceCommentDao.selectById(id);
        if (comment == null) throw new RuntimeException("评论不存在");
        if (!isAdmin && (currentUserId == null || !currentUserId.equals(comment.getUserId()))) {
            throw new RuntimeException("无权限删除他人评论");
        }
        return resourceCommentDao.deleteById(id);
    }
}