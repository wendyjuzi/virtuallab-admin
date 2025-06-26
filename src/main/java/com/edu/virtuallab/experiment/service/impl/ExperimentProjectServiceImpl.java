package com.edu.virtuallab.experiment.service.impl;

import com.edu.virtuallab.experiment.model.ExperimentProject;
import com.edu.virtuallab.experiment.dao.ExperimentProjectDao;
import com.edu.virtuallab.experiment.service.ExperimentProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExperimentProjectServiceImpl implements ExperimentProjectService {
    @Autowired
    private ExperimentProjectDao projectDao;

    @Override
    public int create(ExperimentProject project) {
        return projectDao.insert(project);
    }

    @Override
    public int update(ExperimentProject project) {
        return projectDao.update(project);
    }

    @Override
    public int delete(Long id) {
        return projectDao.delete(id);
    }

    @Override
    public ExperimentProject getById(Long id) {
        return projectDao.selectById(id);
    }

    @Override
    public List<ExperimentProject> listAll() {
        return projectDao.selectAll();
    }
} 