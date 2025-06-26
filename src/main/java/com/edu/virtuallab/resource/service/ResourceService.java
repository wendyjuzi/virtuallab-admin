package com.edu.virtuallab.resource.service;

import com.edu.virtuallab.resource.model.Resource;
import java.util.List;

public interface ResourceService {
    int create(Resource resource);
    int update(Resource resource);
    int delete(Long id);
    Resource getById(Long id);
    List<Resource> listAll();
} 