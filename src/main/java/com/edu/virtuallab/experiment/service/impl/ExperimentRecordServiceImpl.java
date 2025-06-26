package com.edu.virtuallab.experiment.service.impl;

import com.edu.virtuallab.experiment.model.ExperimentRecord;
import com.edu.virtuallab.experiment.dao.ExperimentRecordDao;
import com.edu.virtuallab.experiment.service.ExperimentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExperimentRecordServiceImpl implements ExperimentRecordService {
    @Autowired
    private ExperimentRecordDao recordDao;

    @Override
    public int create(ExperimentRecord record) {
        return recordDao.insert(record);
    }

    @Override
    public int update(ExperimentRecord record) {
        return recordDao.update(record);
    }

    @Override
    public int delete(Long id) {
        return recordDao.delete(id);
    }

    @Override
    public ExperimentRecord getById(Long id) {
        return recordDao.selectById(id);
    }

    @Override
    public List<ExperimentRecord> listAll() {
        return recordDao.selectAll();
    }
} 