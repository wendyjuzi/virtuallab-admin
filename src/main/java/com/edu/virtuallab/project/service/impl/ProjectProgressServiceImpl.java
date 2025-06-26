package com.edu.virtuallab.project.service.impl;

import com.edu.virtuallab.project.model.ProjectProgress;
import com.edu.virtuallab.project.dao.ProjectProgressDao;
import com.edu.virtuallab.project.service.ProjectProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectProgressServiceImpl implements ProjectProgressService {
    @Autowired
    private ProjectProgressDao progressDao;

    @Override
    public int create(ProjectProgress progress) {
        return progressDao.insert(progress);
    }

    @Override
    public int update(ProjectProgress progress) {
        return progressDao.update(progress);
    }

    @Override
    public int delete(Long id) {
        return progressDao.delete(id);
    }

    @Override
    public ProjectProgress getById(Long id) {
        return progressDao.selectById(id);
    }

    @Override
    public List<ProjectProgress> listAll() {
        return progressDao.selectAll();
    }
} 