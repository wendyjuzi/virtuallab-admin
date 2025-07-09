package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.auth.util.JwtUtil;
import com.edu.virtuallab.experiment.dao.ExperimentProjectDao;
import com.edu.virtuallab.experiment.dao.StudentClassDao;
import com.edu.virtuallab.experiment.dto.ExperimentProjectPublishRequest;
import com.edu.virtuallab.experiment.model.Clazz;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import com.edu.virtuallab.experiment.model.ProjectTeam;
import com.edu.virtuallab.experiment.model.Student;
import com.edu.virtuallab.experiment.service.ExperimentProjectService;
import com.edu.virtuallab.experiment.dto.ExperimentProjectListDTO;
import com.edu.virtuallab.common.api.PageResult;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.project.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import com.edu.virtuallab.log.annotation.OperationLogRecord;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/experiment/project")
public class ExperimentProjectController {
    @Autowired
    private ExperimentProjectService projectService;
    @Resource
    private StudentClassDao classDao;
    @OperationLogRecord(operation = "CREATE_EXPERIMENT_PROJECT", module = "EXPERIMENT", action = "创建实验项目", description = "用户创建实验项目", permissionCode = "EXPERIMENT_MANAGE")
    @PostMapping("/create")
    public int create(@RequestBody ExperimentProject project) {
        return projectService.create(project);
    }

    @OperationLogRecord(operation = "UPDATE_EXPERIMENT_PROJECT", module = "EXPERIMENT", action = "更新实验项目", description = "用户更新实验项目", permissionCode = "EXPERIMENT_MANAGE")
    @PutMapping("/update")
    public int update(@RequestBody ExperimentProject project) {
        return projectService.update(project);
    }

    @OperationLogRecord(operation = "DELETE_EXPERIMENT_PROJECT", module = "EXPERIMENT", action = "删除实验项目", description = "用户删除实验项目", permissionCode = "EXPERIMENT_MANAGE")
    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        return projectService.delete(id);
    }

    @GetMapping("/{id}")
    public ExperimentProject getById(@PathVariable Long id) {
        return projectService.getById(id);
    }
    @GetMapping("/list")
    public List<ExperimentProject> listAll() {
        return projectService.listAll();
    }

    @GetMapping("/viewlist")
    public CommonResult<PageResult<ExperimentProjectListDTO>> list(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "latest") String sort
    ) {
        List<ExperimentProject> projects = projectService.listPage(category, sort, page, size);
        long total = projectService.countPage(category);
        // 实体转DTO
        List<ExperimentProjectListDTO> dtoList = projects.stream().map(p -> {
            ExperimentProjectListDTO dto = new ExperimentProjectListDTO();
            dto.setId(p.getId());
            dto.setTitle(p.getName());
            dto.setDescription(p.getDescription());
            dto.setCategory(p.getCategory());
            dto.setImageUrl(p.getImageUrl());
            dto.setViews(null); // 预留
            dto.setLikes(null); // 预留
            dto.setFavorites(null); // 预留
            dto.setCreateTime(p.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
        PageResult<ExperimentProjectListDTO> pageResult = new PageResult<>(total, dtoList);
        return CommonResult.success(pageResult, "success");
    }

    @GetMapping("/search")
    public List<ExperimentProject> search(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String keyword
    ) {
        // 为防止前端传null，service层也可以处理
        if (category != null && category.trim().isEmpty()) {
            category = null;
        }
        if (level != null && level.trim().isEmpty()) {
            level = null;
        }
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }
        return projectService.search(category, level, keyword);
    }

    /**
     * 标准实验项目分页/条件查询接口
     * GET /experiment/project/query?page=1&size=10&category=xxx&level=xxx&keyword=xxx
     */
    @GetMapping("/query")
    public CommonResult<Map<String, Object>> queryProjects(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String keyword
    ) {
        List<ExperimentProject> records = projectService.search(category, level, keyword);
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, records.size());
        List<ExperimentProject> pageList = fromIndex < records.size() ? records.subList(fromIndex, toIndex) : java.util.Collections.emptyList();
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageList);
        result.put("total", records.size());
        return CommonResult.success(result, "查询成功");
    }


    // Controller 层
    @PostMapping("/publish")
    public Map<String, Object> publishProject(HttpServletRequest request, @RequestBody ExperimentProjectPublishRequest req) {
        String username = JwtUtil.getUsernameFromRequest(request);
        if (username == null) {
            throw new RuntimeException("用户未登录");
        }
        // 调试打印：看请求体里的字段
        System.out.println("实验原理 principle: " + req.getPrinciple());
        System.out.println("实验目的 purpose: " + req.getPurpose());
        System.out.println("实验方法 method: " + req.getMethod());
        System.out.println("实验步骤 steps: " + req.getSteps());
        Long projectId = projectService.publishProject(req, username);

        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("msg", "发布成功");
        res.put("projectId", projectId);
        return res;
    }
    @Value("${file.upload-dir}")
    private String uploadDir;


    @PostMapping("/upload/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("文件为空");
        }
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            System.out.println("创建上传目录：" + uploadPath.toAbsolutePath());
        }

        Path savePath = uploadPath.resolve(fileName);
        System.out.println("准备保存文件： " + fileName);
        System.out.println("保存路径为： " + savePath.toAbsolutePath());

        try {
            Files.copy(file.getInputStream(), savePath);
            System.out.println("文件保存成功！");
        } catch (IOException e) {
            System.err.println("文件保存失败: " + e.getMessage());
            throw e;
        }

        // 这里返回前端的URL，映射到静态资源路径
        String url = "/images/uploads/" + fileName;
        System.out.println("返回文件URL: " + url);

        Map<String, Object> result = new HashMap<>();
        result.put("url", url);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/class/list")
    public List<Clazz> getAllClasses() {
        return classDao.getAllClasses();
    }

    @GetMapping("/class/{classId}/students")
    public List<Student> getStudentsByClass(@PathVariable Long classId) {
        return classDao.getStudentsByClassId(classId);
    }
    @GetMapping("/studentCount")
    public int getStudentCount() {
        int count = classDao.countStudentClassRecords();
        System.out.println("获取学生数量 count = " + count);
        return count;
    }
    @GetMapping("/pendingGradingCount")
    public ResponseEntity<Map<String, Object>> getPendingGradingCount(
            @RequestParam String teacherName) {
        try {
            // 使用System.out代替log
            System.out.println("获取待批改报告数量，教师: " + teacherName);

            int count = projectService.countPendingGradingReports(teacherName);
            System.out.println("待批改报告数量: " + count);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("code", 200);
            result.put("message", "成功");
            result.put("data", count);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // 打印错误堆栈
            System.err.println("获取待批改数量失败:");
            e.printStackTrace();

            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
            return ResponseEntity.status(500).body(result);
        }
    }
    @GetMapping("/class-student-list")
    public ResponseEntity<List<Map<String, Object>>> getClassStudentList() {
        try {
            List<Clazz> classes = classDao.getAllClasses(); // 查询所有班级
            List<Map<String, Object>> result = new ArrayList<>();

            for (Clazz clazz : classes) {
                Map<String, Object> item = new HashMap<>();
                item.put("classId", clazz.getId());
                item.put("className", clazz.getName());

                // 根据 classId 查询对应学生
                List<Student> studentList = classDao.getStudentsByClassId(clazz.getId());
                List<Map<String, Object>> students = new ArrayList<>();

                for (Student s : studentList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", s.getId());
                    // 如果还有其他字段可加入
                    students.add(map);
                }
                item.put("students", students);

                result.add(item);
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    @GetMapping("/statistics")
    public ResponseEntity<?> getScoreStatistics(@RequestParam Long projectId) {
        try {
            System.out.println("获取评分统计数据，项目ID：" + projectId);

            List<Integer> scores = projectService.getScoresByProjectId(projectId);
            double average = scores.stream().mapToInt(Integer::intValue).average().orElse(0);

            Map<String, Object> data = new HashMap<>();
            data.put("scores", scores);
            data.put("average", average);

            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("success", true);
            result.put("message", "成功");
            result.put("data", data);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }


    @GetMapping("/my-projects")
    public List<ExperimentProject> getMyProjects(HttpServletRequest request) {
        String username = JwtUtil.getUsernameFromRequest(request);
        if (username == null) {
            throw new RuntimeException("用户未登录");
        }
        return projectService.getProjectsByCreatedBy(username);
    }
    // 获取当前学生参与的所有实验项目小组聊天
    @GetMapping("/chat/teams")
    public List<Long> getTeamsByUserId(@RequestParam Long userId) {
        Long studentId = projectService.getStudentIdByUserId(userId);
        List<Long> teams = projectService.getTeamsByStudentId(studentId);
        System.out.println("Debug: teams = " + teams);
        return projectService.getTeamsByStudentId(studentId);
    }

    @PostMapping("/start")
    public ResponseEntity<String> startExperiment(@RequestBody Map<String, Object> payload) {
        Integer projectId = (Integer) payload.get("projectId");
        String studentId = (String) payload.get("studentId");

        if (projectId == null || studentId == null) {
            return ResponseEntity.badRequest().body("参数不完整");
        }

        int updated = projectService.markAsInProgress(projectId, studentId);

        if (updated > 0) {
            return ResponseEntity.ok("已开始实验");
        } else {
            // ⚠️更新为 0，不是异常，可能是 created_by = admin
            return ResponseEntity.ok("无需更新，实验可能由 admin 创建，已跳过");
            // 或者更语义化地写：
            // return ResponseEntity.status(204).body("跳过更新");
        }
    }

    @PostMapping("/complete")
    public ResponseEntity<String> completeExperiment(@RequestBody Map<String, Object> payload) {
        Object projectIdObj = payload.get("projectId");
        Integer projectId = projectIdObj != null ? Integer.parseInt(projectIdObj.toString()) : null;

        Object studentIdObj = payload.get("studentId");
        String studentId = studentIdObj != null ? studentIdObj.toString() : null;

        if (projectId == null || studentId == null) {
            return ResponseEntity.badRequest().body("缺少必要参数");
        }

        int updated = projectService.markAsCompleted(projectId, studentId);

        if (updated > 0) {
            return ResponseEntity.ok("已更新为 completed");
        } else {
            return ResponseEntity.ok("无需更新，可能由 admin 创建，已跳过");
            // 也可以选择：
            // return ResponseEntity.status(204).build();
        }
    }

    // ✅ 你需要有这个方法才能处理 PUT 请求
    @PutMapping("/editupdate")
    public ResponseEntity<?> updateProject(@RequestBody ExperimentProject project) {
        System.out.println("收到的更新内容: " + project);
        // TODO: 调用 service 更新项目逻辑
        boolean updated = projectService.updateProject(project); // 你自己的更新逻辑
        if (updated) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("更新失败");
        }
    }

    @GetMapping("/editdetail")
    public ResponseEntity<?> getProjectById(@RequestParam Long projectId) {
        System.out.println("收到前端请求，projectId = " + projectId); // 打印接收到的参数

        ExperimentProject project = projectService.findById(projectId);
        System.out.println("查询到的项目 = " + project); // 打印查询结果

        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            System.out.println("未找到对应实验项目");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("实验不存在");
        }
    }

    @GetMapping("/details/{projectId}")  // 明确接口路径
    public ExperimentProject getProjectDetailsForReport(@PathVariable Long projectId) {
        return projectService.getProjectDetailsForReport(projectId);
    }

class ApproveRequest {
    private boolean approve;
    private String comment;
    public boolean isApprove() { return approve; }
    public void setApprove(boolean approve) { this.approve = approve; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
}
