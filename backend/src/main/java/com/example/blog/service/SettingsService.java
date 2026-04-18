package com.example.blog.service;

import com.example.blog.model.Settings;
import com.example.blog.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SettingsService {
    
    @Autowired
    private SettingsRepository settingsRepository;
    
    // 初始化默认设置
    private static final Map<String, String> DEFAULT_SETTINGS = new HashMap<>();
    static {
        DEFAULT_SETTINGS.put("siteName", "LiteBlog");
        DEFAULT_SETTINGS.put("siteDescription", "企业级轻量博客平台");
        DEFAULT_SETTINGS.put("siteKeywords", "博客,技术,分享");
        DEFAULT_SETTINGS.put("adminEmail", "admin@example.com");
        DEFAULT_SETTINGS.put("icp", "");
        DEFAULT_SETTINGS.put("articlesPerPage", "12");
        DEFAULT_SETTINGS.put("commentsEnabled", "true");
        DEFAULT_SETTINGS.put("commentAudit", "false");
        DEFAULT_SETTINGS.put("registrationEnabled", "true");
        DEFAULT_SETTINGS.put("emailVerification", "false");
    }
    
    public Map<String, String> getAllSettings() {
        Map<String, String> result = new HashMap<>(DEFAULT_SETTINGS);
        
        List<Settings> dbSettings = settingsRepository.findAll();
        for (Settings s : dbSettings) {
            result.put(s.getSettingKey(), s.getSettingValue());
        }
        
        return result;
    }
    
    /**
     * 获取公开的网站设置（不需要管理员权限）
     */
    public Map<String, String> getPublicSettings() {
        Map<String, String> result = new HashMap<>();
        
        // 只返回公开的设置项
        String[] publicKeys = {"siteName", "siteDescription", "siteKeywords", "icp"};
        for (String key : publicKeys) {
            result.put(key, getSetting(key));
        }
        
        return result;
    }
    
    @Transactional
    public Map<String, String> saveSettings(Map<String, String> settings) {
        for (Map.Entry<String, String> entry : settings.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            
            Settings settingsEntity = settingsRepository.findBySettingKey(key)
                    .orElse(new Settings());
            
            settingsEntity.setSettingKey(key);
            settingsEntity.setSettingValue(value);
            settingsRepository.save(settingsEntity);
        }
        
        return getAllSettings();
    }
    
    public String getSetting(String key) {
        return settingsRepository.findBySettingKey(key)
                .map(Settings::getSettingValue)
                .orElse(DEFAULT_SETTINGS.get(key));
    }
    
    @Transactional
    public void setSetting(String key, String value) {
        Settings settingsEntity = settingsRepository.findBySettingKey(key)
                .orElse(new Settings());
        settingsEntity.setSettingKey(key);
        settingsEntity.setSettingValue(value);
        settingsRepository.save(settingsEntity);
    }
}
