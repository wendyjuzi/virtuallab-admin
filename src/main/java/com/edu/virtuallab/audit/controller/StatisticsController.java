package com.edu.virtuallab.audit.controller;

import com.edu.virtuallab.auth.dao.UserDao;
import com.edu.virtuallab.audit.dao.ExperimentProjectMapper;
import com.edu.virtuallab.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StatisticsController {

    @Autowired
    private  UserDao userDao;
    @Autowired
    private  ExperimentProjectMapper experimentProjectMapper;

    @Autowired
    public StatisticsController(UserDao userDao, ExperimentProjectMapper experimentProjectMapper) {
        this.userDao = userDao;
        this.experimentProjectMapper = experimentProjectMapper;
    }

    @GetMapping("/department/projects/count")
    public CommonResult<Map<String, Integer>> countDepartmentProjects(@RequestParam Long adminUserId) {
        // 1. 通过管理员ID获取院系信息
        String department = userDao.findById(adminUserId).getDepartment();

        if (department == null || department.isEmpty()) {
            return CommonResult.success(createResult(0), "管理员无院系信息");
        }

        // 2. 获取本院系所有用户的用户名
        List<String> usernames = userDao.findUsernamesByDepartment(department);

        if (usernames == null || usernames.isEmpty()) {
            return CommonResult.success(createResult(0), "院系无用户");
        }

        // 3. 统计本院系创建的项目数量
        int count = experimentProjectMapper.countProjectsByUsernames(usernames);
        return CommonResult.success(createResult(count), "查询成功");
    }

    private Map<String, Integer> createResult(int count) {
        Map<String, Integer> result = new HashMap<>();
        result.put("count", count);
        return result;
    }
}
