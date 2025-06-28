package com.edu.virtuallab.experiment.controller;

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

    @PostMapping("/image")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File savePath = new File(UPLOAD_DIR);
        if (!savePath.exists()) savePath.mkdirs();
        file.transferTo(new File(savePath, filename));

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String url = baseUrl + "/images/uploads/" + filename;

        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        return map;
    }
}

