package com.edu.virtuallab.admin.controller;

import com.edu.virtuallab.admin.service.DatabaseBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/admin/backup")
public class DatabaseBackupController {

    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String dbUsername;
    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Autowired
    private DatabaseBackupService databaseBackupService;

    @GetMapping("/database")
    public ResponseEntity<InputStreamResource> backupDatabase() throws IOException {
        try {
            String zipPath = databaseBackupService.backupAndZip();
            File zipFile = new File(zipPath);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + zipFile.getName())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(zipFile.length())
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("数据库备份失败: " + e.getMessage());
        }
    }

    private String performDatabaseBackup(String backupFileName) throws IOException {
        String dbName = extractDatabaseName(dbUrl);
        String backupDir = System.getProperty("user.dir") + "/backups";
        Files.createDirectories(Paths.get(backupDir));
        String backupPath = backupDir + "/" + backupFileName;
        String[] command = {
                "mysqldump",
                "-h", extractHost(dbUrl),
                "-P", extractPort(dbUrl),
                "-u", dbUsername,
                "-p" + dbPassword,
                "--single-transaction",
                "--routines",
                "--triggers",
                dbName
        };
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectOutput(new File(backupPath));
        pb.redirectErrorStream(true);
        Process process = pb.start();
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("数据库备份失败，退出码: " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("备份过程被中断");
        }
        return backupPath;
    }

    private String createBackupZip(String backupPath, String zipFileName) throws IOException {
        String zipPath = System.getProperty("user.dir") + "/backups/" + zipFileName;
        try (ZipOutputStream zs = new ZipOutputStream(new FileOutputStream(zipPath))) {
            Path backupFilePath = Paths.get(backupPath);
            ZipEntry zipEntry = new ZipEntry(backupFilePath.getFileName().toString());
            zs.putNextEntry(zipEntry);
            Files.copy(backupFilePath, zs);
            zs.closeEntry();
        }
        Files.deleteIfExists(Paths.get(backupPath));
        return zipPath;
    }

    private String extractDatabaseName(String jdbcUrl) {
        String[] parts = jdbcUrl.split("/");
        return parts[parts.length - 1].split("\\?")[0];
    }
    private String extractHost(String jdbcUrl) {
        String[] parts = jdbcUrl.split("//")[1].split(":");
        return parts[0];
    }
    private String extractPort(String jdbcUrl) {
        String[] parts = jdbcUrl.split("//")[1].split(":");
        return parts[1].split("/")[0];
    }
} 