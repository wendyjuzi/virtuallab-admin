package com.edu.virtuallab.resource.service.impl;

import com.edu.virtuallab.resource.dao.ResourceDao;
import com.edu.virtuallab.resource.model.Resource;
import com.edu.virtuallab.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private ResourceDao resourceDao;

    @Override
    public int create(Resource resource) {
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
} 