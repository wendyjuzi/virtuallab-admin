package com.edu.virtuallab.experiment.service;

import java.util.List;
import java.util.Map;

public interface StudentClassService {
    /**
     * 学生选择班级
     */
    void selectClass(Long studentId, Long classId);

    /**
     * 查询学生已选班级
     */
    List<Long> getClassIdsByStudent(Long studentId);

    /**
     * 退出班级
     */
    void quitClass(Long studentId, Long classId);

    /**
     * 获取学生已选班级的详细信息
     */
    List<Map<String, Object>> getClassDetailsByStudent(Long studentId);

    /**
     * 获取所有班级
     */
    List<Map<String, Object>> getAllClasses();
} 