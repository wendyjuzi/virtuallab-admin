package com.edu.virtuallab.experiment.service.impl;

import com.edu.virtuallab.experiment.model.ExperimentReport;
import com.edu.virtuallab.experiment.dao.ExperimentReportDao;
import com.edu.virtuallab.experiment.service.ExperimentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExperimentReportServiceImpl implements ExperimentReportService {
    @Autowired
    private ExperimentReportDao reportDao;

    @Override
    public int create(ExperimentReport report) {
        return reportDao.insert(report);
    }

    @Override
    public int update(ExperimentReport report) {
        return reportDao.update(report);
    }

    @Override
    public int delete(Long id) {
        return reportDao.delete(id);
    }

    @Override
    public ExperimentReport getById(Long id) {
        return reportDao.selectById(id);
    }

    @Override
    public List<ExperimentReport> listAll() {
        return reportDao.selectAll();
    }
} 