package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.experiment.dto.StudentClassActionRequest;
import com.edu.virtuallab.experiment.service.StudentClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student/class")
public class StudentClassController {

    @Autowired
    private StudentClassService studentClassService;

    /**
     * 学生选择班级
     */
    @PostMapping("/select")
    public Map<String, Object> selectClass(@RequestBody StudentClassActionRequest request) {
        Map<String, Object> res = new HashMap<>();
        try {
            studentClassService.selectClass(request.getStudentId(), request.getClassId());
            res.put("code", 200);
            res.put("msg", "选择班级成功");
        } catch (Exception e) {
            res.put("code", 400);
            res.put("msg", e.getMessage());
        }
        return res;
    }

    /**
     * 查询学生已选班级
     */
    @GetMapping("/list")
    public Map<String, Object> getClassIds(@RequestParam Long studentId) {
        Map<String, Object> res = new HashMap<>();
        List<Long> classIds = studentClassService.getClassIdsByStudent(studentId);
        res.put("code", 200);
        res.put("classIds", classIds);
        return res;
    }

    /**
     * 退出班级
     */
    @PostMapping("/quit")
    public Map<String, Object> quitClass(@RequestBody StudentClassActionRequest request) {
        Map<String, Object> res = new HashMap<>();
        try {
            studentClassService.quitClass(request.getStudentId(), request.getClassId());
            res.put("code", 200);
            res.put("msg", "退出班级成功");
        } catch (Exception e) {
            res.put("code", 400);
            res.put("msg", e.getMessage());
        }
        return res;
    }

    /**
     * 获取学生已选班级的详细信息
     */
    @GetMapping("/details")
    public Map<String, Object> getClassDetails(@RequestParam Long studentId) {
        Map<String, Object> res = new HashMap<>();
        List<Map<String, Object>> classDetails = studentClassService.getClassDetailsByStudent(studentId);
        res.put("code", 200);
        res.put("classDetails", classDetails);
        return res;
    }

    /**
     * 获取所有班级列表（供学生自主选择）
     */
    @GetMapping("/all")
    public Map<String, Object> getAllClasses() {
        Map<String, Object> res = new HashMap<>();
        List<Map<String, Object>> classList = studentClassService.getAllClasses();
        res.put("code", 200);
        res.put("classList", classList);
        return res;
    }
} 