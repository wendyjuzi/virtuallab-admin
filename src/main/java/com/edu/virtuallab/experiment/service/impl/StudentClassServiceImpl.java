package com.edu.virtuallab.experiment.service.impl;

import com.edu.virtuallab.experiment.dao.StudentClassDao;
import com.edu.virtuallab.experiment.service.StudentClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StudentClassServiceImpl implements StudentClassService {
    @Autowired
    private StudentClassDao studentClassDao;

    @Override
    public void selectClass(Long studentId, Long classId) {
        // TODO: 实现
    }

    @Override
    public List<Long> getClassIdsByStudent(Long studentId) {
        // TODO: 实现
        return null;
    }

    @Override
    public void quitClass(Long studentId, Long classId) {
        // TODO: 实现
    }

    @Override
    public List<Map<String, Object>> getClassDetailsByStudent(Long studentId) {
        // TODO: 实现
        return null;
    }

    @Override
    public List<Map<String, Object>> getAllClasses() {
        return studentClassDao.findAllClasses();
    }
} 