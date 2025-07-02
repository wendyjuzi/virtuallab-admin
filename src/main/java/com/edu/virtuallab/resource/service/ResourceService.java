package com.edu.virtuallab.resource.service;

import com.edu.virtuallab.resource.model.Resource;
import com.edu.virtuallab.resource.model.ResourceShare;
import com.edu.virtuallab.resource.model.ResourceCopy;
import com.edu.virtuallab.resource.model.ResourceInteraction;

import java.util.List;

public interface ResourceService {
    int create(Resource resource);
    int update(Resource resource);
    int delete(Long id);
    Resource getById(Long id);
    List<Resource> listAll();
    
    List<Resource> getByType(String type);
    List<Resource> getByCategory(String category);
    List<Resource> getByStatus(String status);
    List<Resource> getByUploader(String uploader);
    List<Resource> searchByName(String name);
    List<Resource> getByTypeAndCategory(String type, String category);
    
    boolean activateResource(Long id);
    boolean deactivateResource(Long id);
    boolean softDelete(Long id);
    int getResourceCount();
    int getResourceCountByType(String type);
    int getResourceCountByCategory(String category);

    int setSharePermission(ResourceShare resourceShare);
    boolean revokeSharePermission(Long shareId);
    List<ResourceShare> getSharedResources(String username);
    boolean hasPermission(Long resourceId, String username, String permission);
    
    Resource copyResource(ResourceCopy resourceCopy);
    
    int addInteraction(ResourceInteraction interaction);
    List<ResourceInteraction> getInteractions(Long resourceId);
    double getAverageRating(Long resourceId);
    int getInteractionCount(Long resourceId, String interactionType);
} 