package com.example.blog.controller;

import com.example.blog.model.User;
import com.example.blog.service.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/admin/backup")
@PreAuthorize("hasRole('ADMIN')")
public class BackupController {
    @Autowired
    private BackupService backupService;

    @PostMapping("/create")
    public ResponseEntity<?> createBackup() {
        try {
            String backupPath = backupService.backupDatabase();
            return ResponseEntity.ok("备份成功，文件路径：" + backupPath);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("备份失败：" + e.getMessage());
        }
    }

    @PostMapping("/restore")
    public ResponseEntity<?> restoreBackup(@RequestParam("file") MultipartFile file) {
        try {
            // 验证文件类型
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".sql")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("仅支持SQL文件");
            }
            
            // 保存上传的文件
            File tempFile = File.createTempFile("backup", ".sql");
            file.transferTo(tempFile);

            // 恢复数据库
            backupService.restoreDatabase(tempFile.getAbsolutePath());

            // 删除临时文件
            tempFile.delete();

            return ResponseEntity.ok("恢复成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("恢复失败：" + e.getMessage());
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadBackup(@PathVariable String fileName) {
        try {
            // 防止路径遍历攻击
            String safeFileName = Paths.get(fileName).getFileName().toString();
            if (safeFileName.contains("..") || safeFileName.contains("/") || safeFileName.contains("\\")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无效的文件名");
            }
            
            File file = new File("backup" + File.separator + safeFileName);
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("备份文件不存在");
            }

            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            fis.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", safeFileName);

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("下载失败：" + e.getMessage());
        }
    }
}