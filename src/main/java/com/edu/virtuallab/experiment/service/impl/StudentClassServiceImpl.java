package com.edu.virtuallab.experiment.service.impl;

import com.edu.virtuallab.experiment.dao.ExperimentProjectDao;
import com.edu.virtuallab.experiment.dao.StudentClassDao;
import com.edu.virtuallab.experiment.service.StudentClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentClassServiceImpl implements StudentClassService {
    private final StudentClassDao studentClassDao;
    @Autowired
    public StudentClassServiceImpl(StudentClassDao studentClassDao) {
        this.studentClassDao = studentClassDao;
    }


    @Override
    public void selectClass(Long studentId, Long classId) {
        studentClassDao.insert(studentId, classId);
    }

    @Override
    public List<Long> getClassIdsByStudent(Long studentId) {
        return studentClassDao.findClassIdsByStudentId(studentId);
    }

    @Override
    public void quitClass(Long studentId, Long classId) {
        int rows = studentClassDao.deleteByStudentIdAndClassId(studentId, classId);
        if (rows == 0) {
            throw new RuntimeException("数据库无此班级记录，删除失败！");
        }
    }

    @Override
    public List<Map<String, Object>> getClassDetailsByStudent(Long studentId) {
        List<Long> classIds = studentClassDao.findClassIdsByStudentId(studentId);
        if (classIds == null || classIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return studentClassDao.findByIds(classIds);
    }

    @Override
    public List<Map<String, Object>> getAllClasses() {
        return studentClassDao.findAllClasses();
    }

    @Override
    public List<Long> getStudentIdsByClassId(Long classId) {
        List<Long> studentIds = studentClassDao.findStudentIdsByClassId(classId);
        System.out.println("[autoGroupStudents] 查询到学生ID: " + studentIds);
        return studentIds;
    }

} 