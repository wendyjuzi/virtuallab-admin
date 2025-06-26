package com.edu.virtuallab.project.service.impl;

import com.edu.virtuallab.project.model.Project;
import com.edu.virtuallab.project.dao.ProjectDao;
import com.edu.virtuallab.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectDao projectDao;

    @Override
    public int create(Project project) {
        return projectDao.insert(project);
    }

    @Override
    public int update(Project project) {
        return projectDao.update(project);
    }

    @Override
    public int delete(Long id) {
        return projectDao.delete(id);
    }

    @Override
    public Project getById(Long id) {
        return projectDao.selectById(id);
    }

    @Override
    public List<Project> listAll() {
        return projectDao.selectAll();
    }
} 