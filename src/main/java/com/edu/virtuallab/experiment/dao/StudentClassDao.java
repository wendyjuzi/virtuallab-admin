package com.edu.virtuallab.experiment.dao;

import com.edu.virtuallab.experiment.model.Clazz;
import com.edu.virtuallab.experiment.model.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface StudentClassDao {

    List<Long> findStudentIdsByClassIds(@Param("list") List<Long> classIds);

    void insert(@Param("studentId") Long studentId, @Param("classId") Long classId);

    void deleteByStudentId(@Param("studentId") Long studentId);

    List<Long> findClassIdsByStudentId(@Param("studentId") Long studentId);
    List<Clazz> getAllClasses();
    List<Student> getStudentsByClassId(@Param("classId") Long classId);
    int countStudentClassRecords();

}

