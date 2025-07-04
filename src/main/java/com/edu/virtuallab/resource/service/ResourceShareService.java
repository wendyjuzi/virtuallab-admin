package com.edu.virtuallab.resource.service;

import com.edu.virtuallab.resource.model.ResourceShare;
import java.util.List;

public interface ResourceShareService {
    String generateShareLink(Long resourceId, String sharedBy, Long expireMinutes);
    ResourceShare getShareByLink(String shareLink);
    List<ResourceShare> getSharesByResourceId(Long resourceId);
    boolean cancelShare(Long id, String sharedBy);
} 