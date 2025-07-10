package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.log.annotation.OperationLogRecord;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/images/uploads/";
    // 已彻底移除 SCENE_JSON_DIR 及 3D 场景 JSON 文件上传相关代码，3D 场景 JSON 由前端本地导出并手动放入 public/static/json/ 目录。

    @OperationLogRecord(operation = "UPLOAD_EXPERIMENT_FILE", module = "EXPERIMENT", action = "上传实验文件", description = "用户上传实验文件", permissionCode = "EXPERIMENT_MANAGE")
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File savePath = new File(UPLOAD_DIR);
        if (!savePath.exists()) savePath.mkdirs();
        try {
            file.transferTo(new File(savePath, filename));
        } catch (IOException e) {
            e.printStackTrace();
            return "上传失败";
        }

        String baseUrl = "http://localhost:8080"; // Assuming a default base URL for demonstration
        String url = baseUrl + "/images/uploads/" + filename;

        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        return "上传成功";
    }

    // 自动上传 3D 场景 JSON 文件到前端 public/static/json 目录
    @PostMapping("/scene-json-to-frontend")
    public Map<String, Object> uploadSceneJsonToFrontend(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("文件为空");
        }
        String frontendJsonDir = "D:/大二下/实训/virtuallab/virtuallab-frontend/public/static/json/";
        String fileName = java.util.UUID.randomUUID() + "_" + file.getOriginalFilename();
        java.io.File dir = new java.io.File(frontendJsonDir);
        if (!dir.exists()) dir.mkdirs();
        java.io.File dest = new java.io.File(dir, fileName);
        file.transferTo(dest);
        String path = "/static/json/" + fileName;
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("path", path);
        return result;
    }
}

