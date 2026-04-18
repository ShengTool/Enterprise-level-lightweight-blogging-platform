package com.example.blog.controller;

import com.example.blog.model.User;
import com.example.blog.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class SettingsController {
    
    @Autowired
    private SettingsService settingsService;
    
    /**
     * GET /api/settings - 公开获取网站设置（不需要登录）
     */
    @GetMapping("/settings")
    public ResponseEntity<?> getPublicSettings() {
        Map<String, String> publicSettings = settingsService.getPublicSettings();
        return ResponseEntity.ok(publicSettings);
    }
    
    /**
     * GET /api/admin/settings - 获取所有设置（需要管理员）
     */
    @GetMapping("/admin/settings")
    public ResponseEntity<?> getSettings(@AuthenticationPrincipal User currentUser) {
        if (currentUser == null || !currentUser.isAdmin()) {
            return ResponseEntity.status(403).body(Map.of("error", "需要管理员权限"));
        }
        
        Map<String, String> settings = settingsService.getAllSettings();
        return ResponseEntity.ok(settings);
    }
    
    /**
     * PUT /api/admin/settings - 保存设置（需要管理员）
     */
    @PutMapping("/admin/settings")
    public ResponseEntity<?> saveSettings(
            @RequestBody Map<String, String> settings,
            @AuthenticationPrincipal User currentUser) {
        
        if (currentUser == null || !currentUser.isAdmin()) {
            return ResponseEntity.status(403).body(Map.of("error", "需要管理员权限"));
        }
        
        Map<String, String> saved = settingsService.saveSettings(settings);
        return ResponseEntity.ok(saved);
    }
}
