package com.edu.virtuallab.experiment.dao;

import com.edu.virtuallab.experiment.model.Clazz;
import com.edu.virtuallab.experiment.model.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
@Mapper
public interface StudentClassDao {

    List<Long> findStudentIdsByClassIds(@Param("list") List<Long> classIds);

    void insert(@Param("studentId") Long studentId, @Param("classId") Long classId);

    void deleteByStudentId(@Param("studentId") Long studentId);

    List<Long> findClassIdsByStudentId(@Param("studentId") Long studentId);

    int deleteByStudentIdAndClassId(@Param("studentId") Long studentId, @Param("classId") Long classId);
    List<Map<String, Object>> findByIds(@Param("classIds") List<Long> classIds);
    List<Map<String, Object>> findAllClasses();
    List<Clazz> getAllClasses();
    List<Student> getStudentsByClassId(@Param("classId") Long classId);
    int countStudentClassRecords();

}

