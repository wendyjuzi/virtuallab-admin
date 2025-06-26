package com.edu.virtuallab.experiment.service.impl;

import com.edu.virtuallab.experiment.model.ExperimentScene;
import com.edu.virtuallab.experiment.dao.ExperimentSceneDao;
import com.edu.virtuallab.experiment.service.ExperimentSceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExperimentSceneServiceImpl implements ExperimentSceneService {
    @Autowired
    private ExperimentSceneDao sceneDao;

    @Override
    public int create(ExperimentScene scene) {
        return sceneDao.insert(scene);
    }

    @Override
    public int update(ExperimentScene scene) {
        return sceneDao.update(scene);
    }

    @Override
    public int delete(Long id) {
        return sceneDao.delete(id);
    }

    @Override
    public ExperimentScene getById(Long id) {
        return sceneDao.selectById(id);
    }

    @Override
    public List<ExperimentScene> listAll() {
        return sceneDao.selectAll();
    }
} 