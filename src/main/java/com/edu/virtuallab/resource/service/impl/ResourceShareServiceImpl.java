package com.edu.virtuallab.resource.service.impl;

import com.edu.virtuallab.resource.dao.ResourceShareDao;
import com.edu.virtuallab.resource.model.ResourceShare;
import com.edu.virtuallab.resource.service.ResourceShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ResourceShareServiceImpl implements ResourceShareService {
    @Autowired
    private ResourceShareDao resourceShareDao;

    @Override
    public String generateShareLink(Long resourceId, String sharedBy, Long expireMinutes) {
        String shareLink = UUID.randomUUID().toString().replace("-", "");
        ResourceShare share = new ResourceShare();
        share.setResourceId(resourceId);
        share.setSharedBy(sharedBy);
        share.setShareLink(shareLink);
        share.setCreateTime(new Date());
        if (expireMinutes != null && expireMinutes > 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, expireMinutes.intValue());
            share.setExpireTime(cal.getTime());
        }
        resourceShareDao.insert(share);
        return shareLink;
    }

    @Override
    public ResourceShare getShareByLink(String shareLink) {
        // 你可以在Dao里加一个selectByShareLink方法
        // 这里假设selectByResourceAndUser可用
        return null; // TODO: 实现
    }

    @Override
    public List<ResourceShare> getSharesByResourceId(Long resourceId) {
        return resourceShareDao.selectByResourceId(resourceId);
    }

    @Override
    public boolean cancelShare(Long id, String sharedBy) {
        ResourceShare share = resourceShareDao.selectById(id);
        if (share == null || !Objects.equals(share.getSharedBy(), sharedBy)) {
            throw new RuntimeException("无权限取消该分享");
        }
        return resourceShareDao.delete(id) > 0;
    }
} 