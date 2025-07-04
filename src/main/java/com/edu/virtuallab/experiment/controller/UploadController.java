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
}

