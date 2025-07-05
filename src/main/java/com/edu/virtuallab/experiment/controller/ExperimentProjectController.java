package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.auth.util.JwtUtil;
import com.edu.virtuallab.experiment.dto.ExperimentProjectPublishRequest;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import com.edu.virtuallab.experiment.model.ProjectTeam;
import com.edu.virtuallab.experiment.service.ExperimentProjectService;
import com.edu.virtuallab.experiment.dto.ExperimentProjectListDTO;
import com.edu.virtuallab.common.api.PageResult;
import com.edu.virtuallab.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import com.edu.virtuallab.log.annotation.OperationLogRecord;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/experiment/project")
public class ExperimentProjectController {
    @Autowired
    private ExperimentProjectService projectService;

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

        return ResponseEntity.ok(Map.of("url", url));
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
            return ResponseEntity.status(500).body("更新失败，可能不存在记录");
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
        return updated > 0 ?
                ResponseEntity.ok("已更新为 completed") :
                ResponseEntity.status(500).body("更新失败");
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
