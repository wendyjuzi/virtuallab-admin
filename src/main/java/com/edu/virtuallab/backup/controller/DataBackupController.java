package com.edu.virtuallab.backup.controller;

import com.edu.virtuallab.backup.dto.BackupRecordDTO;
import com.edu.virtuallab.common.api.CommonResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/backup")
public class DataBackupController {

    @Value("${backup.dir:backup}")
    private String backupDir;
    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String dbUser;
    @Value("${spring.datasource.password}")
    private String dbPassword;

    // 1. 发起备份
    @PostMapping("/start")
    public CommonResult<String> startBackup() {
        try {
            // 解析数据库名
            String url = dbUrl;
            String dbName = url.substring(url.lastIndexOf("/") + 1, url.contains("?") ? url.indexOf("?") : url.length());
            String dateStr = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "backup_" + dateStr + ".sql";
            String operator = "admin"; // 可根据实际登录用户获取

            File dir = new File(backupDir);
            if (!dir.exists()) dir.mkdirs();

            File backupFile = new File(dir, fileName);

            // 拼接 Windows 下 mysqldump 命令
            // 注意：如密码有特殊字符，建议配置免密或用配置文件
            String command = String.format("cmd /c mysqldump -u%s -p%s %s", dbUser, dbPassword, dbName);

            ProcessBuilder pb = new ProcessBuilder(command.split(" "));
            pb.redirectOutput(backupFile);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                return CommonResult.failed("数据库备份失败，exitCode=" + exitCode);
            }

            // 记录备份信息
            saveRecord(new BackupRecordDTO() {{
                setDate(dateStr);
                setStatus("成功");
                setOperator(operator);
                setFileName(fileName);
            }});

            return CommonResult.success("数据库备份成功", "数据库备份成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed("数据库备份失败: " + e.getMessage());
        }
    }

    // 2. 获取备份记录
    @GetMapping("/records")
    public CommonResult<List<BackupRecordDTO>> getBackupRecords() {
        try {
            List<BackupRecordDTO> list = loadRecords();
            return CommonResult.success(list, "获取备份记录成功");
        } catch (Exception e) {
            return CommonResult.failed("获取备份记录失败: " + e.getMessage());
        }
    }

    // 3. 下载备份文件
    @GetMapping("/download")
    public void downloadBackup(@RequestParam String fileName, HttpServletResponse response) {
        File file = new File(backupDir, fileName);
        if (!file.exists()) {
            response.setStatus(404);
            return;
        }
        response.setContentType("application/sql");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        try (InputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ====== 简单的本地json记录实现 ======
    private File getRecordFile() {
        return new File(backupDir, "records.json");
    }

    private void saveRecord(BackupRecordDTO record) throws IOException {
        List<BackupRecordDTO> list = loadRecords();
        list.add(0, record); // 新记录放前面
        try (Writer writer = new FileWriter(getRecordFile())) {
            writer.write(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(list));
        }
    }

    private List<BackupRecordDTO> loadRecords() throws IOException {
        File file = getRecordFile();
        if (!file.exists()) return new ArrayList<>();
        return Arrays.asList(new com.fasterxml.jackson.databind.ObjectMapper().readValue(file, BackupRecordDTO[].class));
    }
} 