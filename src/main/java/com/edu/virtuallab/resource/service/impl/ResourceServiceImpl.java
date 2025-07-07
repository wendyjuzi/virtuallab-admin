package com.edu.virtuallab.resource.service.impl;

import com.edu.virtuallab.resource.dao.ResourceDao;
import com.edu.virtuallab.resource.dao.ResourceShareDao;
import com.edu.virtuallab.resource.dao.ResourceInteractionDao;
import com.edu.virtuallab.resource.model.*;
import com.edu.virtuallab.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private ResourceDao resourceDao;
    
    @Autowired
    private ResourceShareDao resourceShareDao;
    
    @Autowired
    private ResourceInteractionDao resourceInteractionDao;

    @Override
    public int create(Resource resource) {
        // 设置默认状态
        if (resource.getPublishStatus() == null) {
            resource.setPublishStatus(Resource.STATUS_ACTIVE);
        }
        return resourceDao.insert(resource);
    }

    @Override
    public int update(Resource resource) {
        return resourceDao.update(resource);
    }

    @Override
    public int delete(Long id) {
        return resourceDao.delete(id);
    }

    @Override
    public Resource getById(Long id) {
        return resourceDao.selectById(id);
    }

    @Override
    public List<Resource> listAll() {
        return resourceDao.selectAll();
    }

    @Override
    public List<Resource> getByType(String type) {
        return resourceDao.selectByType(type);
    }

    @Override
    public List<Resource> getByCategory(String category) {
        return resourceDao.selectByCategory(category);
    }

    @Override
    public List<Resource> getByStatus(String status) {
        return resourceDao.selectByStatus(status);
    }

    @Override
    public List<Resource> getByUploader(String uploader) {
        return resourceDao.selectByUploader(uploader);
    }

    @Override
    public List<Resource> searchByName(String name) {
        return resourceDao.searchByName(name);
    }

    @Override
    public List<Resource> getByTypeAndCategory(String type, String category) {
        return resourceDao.selectByTypeAndCategory(type, category);
    }

    @Override
    public boolean activateResource(Long id) {
        Resource resource = getById(id);
        if (resource != null) {
            resource.setPublishStatus(Resource.STATUS_ACTIVE);
            return update(resource) > 0;
        }
        return false;
    }

    @Override
    public boolean deactivateResource(Long id) {
        Resource resource = getById(id);
        if (resource != null) {
            resource.setPublishStatus(Resource.STATUS_INACTIVE);
            return update(resource) > 0;
        }
        return false;
    }

    @Override
    public boolean softDelete(Long id) {
        Resource resource = getById(id);
        if (resource != null) {
            resource.setPublishStatus(Resource.STATUS_DELETED);
            return update(resource) > 0;
        }
        return false;
    }

    @Override
    public int getResourceCount() {
        return listAll().size();
    }

    @Override
    public int getResourceCountByType(String type) {
        return getByType(type).size();
    }

    @Override
    public int getResourceCountByCategory(String category) {
        return getByCategory(category).size();
    }

    @Override
    public int setSharePermission(ResourceShare resourceShare) {
        // 检查是否已经存在相同的分享记录
        ResourceShare existingShare = resourceShareDao.selectByResourceAndUser(
            resourceShare.getResourceId(), resourceShare.getSharedWith());
        
        if (existingShare != null) {
            // 更新现有记录
            existingShare.setPermission(resourceShare.getPermission());
            existingShare.setStatus(resourceShare.getStatus());
            existingShare.setExpiresAt(resourceShare.getExpiresAt());
            return resourceShareDao.update(existingShare);
        } else {
            // 创建新记录
            if (resourceShare.getStatus() == null) {
                resourceShare.setStatus(ResourceShare.STATUS_ACTIVE);
            }
            return resourceShareDao.insert(resourceShare);
        }
    }

    @Override
    public boolean revokeSharePermission(Long shareId) {
        ResourceShare share = resourceShareDao.selectById(shareId);
        if (share != null) {
            share.setStatus(ResourceShare.STATUS_INACTIVE);
            return resourceShareDao.update(share) > 0;
        }
        return false;
    }

    @Override
    public List<ResourceShare> getSharedResources(String username) {
        return resourceShareDao.selectBySharedWith(username);
    }

    @Override
    public boolean hasPermission(Long resourceId, String username, String permission) {
        // 检查资源是否存在
        Resource resource = getById(resourceId);
        if (resource == null) {
            return false;
        }
        
        // 检查是否是资源所有者
        if (resource.getCreatedBy().equals(username)) {
            return true;
        }
        
        // 检查分享权限
        ResourceShare share = resourceShareDao.selectByResourceAndUser(resourceId, username);
        if (share != null && ResourceShare.STATUS_ACTIVE.equals(share.getStatus())) {
            // 检查权限级别
            if (ResourceShare.PERMISSION_ADMIN.equals(share.getPermission())) {
                return true;
            } else if (ResourceShare.PERMISSION_WRITE.equals(share.getPermission())) {
                return ResourceShare.PERMISSION_READ.equals(permission) || 
                       ResourceShare.PERMISSION_WRITE.equals(permission);
            } else if (ResourceShare.PERMISSION_READ.equals(share.getPermission())) {
                return ResourceShare.PERMISSION_READ.equals(permission);
            }
        }
        
        return false;
    }

    @Override
    public Resource copyResource(ResourceCopy resourceCopy) {
        // 获取源资源
        Resource sourceResource = getById(resourceCopy.getSourceResourceId());
        if (sourceResource == null) {
            throw new RuntimeException("源资源不存在");
        }
        
        // 创建新资源
        Resource newResource = new Resource();
        newResource.setName(resourceCopy.getNewName() != null ? resourceCopy.getNewName() : 
                          sourceResource.getName() + " (副本)");
        newResource.setLevel(sourceResource.getLevel());
        newResource.setCategory(resourceCopy.getTargetCategory() != null ? 
                              resourceCopy.getTargetCategory() : sourceResource.getCategory());
        newResource.setDescription(sourceResource.getDescription());
        newResource.setImageUrl(sourceResource.getImageUrl());
        newResource.setVideoUrl(sourceResource.getVideoUrl());
        newResource.setCreatedBy(resourceCopy.getCopiedBy());
        newResource.setPublishStatus(Resource.STATUS_ACTIVE);
        
        // 保存新资源
        int result = create(newResource);
        if (result > 0) {
            return newResource;
        } else {
            throw new RuntimeException("资源复制失败");
        }
    }

    @Override
    public int addInteraction(ResourceInteraction interaction) {
        // 检查资源是否存在
        Resource resource = getById(interaction.getResourceId());
        if (resource == null) {
            throw new RuntimeException("资源不存在");
        }
        
        // 对于某些交互类型，检查是否已存在
        if (ResourceInteraction.TYPE_LIKE.equals(interaction.getInteractionType()) ||
            ResourceInteraction.TYPE_RATE.equals(interaction.getInteractionType())) {
            ResourceInteraction existing = resourceInteractionDao.selectByUserAndResource(
                interaction.getResourceId(), interaction.getUserId(), interaction.getInteractionType());
            if (existing != null) {
                // 更新现有记录
                existing.setContent(interaction.getContent());
                existing.setRating(interaction.getRating());
                return resourceInteractionDao.update(existing);
            }
        }
        
        return resourceInteractionDao.insert(interaction);
    }

    @Override
    public List<ResourceInteraction> getInteractions(Long resourceId) {
        return resourceInteractionDao.selectByResourceId(resourceId);
    }

    @Override
    public double getAverageRating(Long resourceId) {
        List<ResourceInteraction> ratings = resourceInteractionDao.selectByType(resourceId, ResourceInteraction.TYPE_RATE);
        if (ratings.isEmpty()) {
            return 0.0;
        }
        
        double totalRating = 0.0;
        int count = 0;
        for (ResourceInteraction interaction : ratings) {
            if (interaction.getRating() != null) {
                totalRating += interaction.getRating();
                count++;
            }
        }
        
        return count > 0 ? totalRating / count : 0.0;
    }

    @Override
    public int getInteractionCount(Long resourceId, String interactionType) {
        List<ResourceInteraction> interactions = resourceInteractionDao.selectByType(resourceId, interactionType);
        return interactions.size();
    }
} 