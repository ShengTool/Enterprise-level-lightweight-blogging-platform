package com.example.blog.config;

import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Value("${admin.default-password:#{null}}")
    private String defaultAdminPassword;
    
    @Value("${admin.email:admin@liteblog.com}")
    private String adminEmail;
    
    @EventListener(ApplicationReadyEvent.class)
    public void initAdminUser() {
        // 检查是否已存在管理员用户
        User admin = userRepository.findByEmail(adminEmail);
        
        if (admin == null) {
            // 从环境变量获取默认密码，如果没有则生成随机密码
            String password = defaultAdminPassword;
            if (password == null || password.trim().isEmpty()) {
                password = generateRandomPassword();
            }
            
            // 创建新的管理员用户
            admin = new User();
            admin.setUsername("admin");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setIsAdmin(true);
            admin.setIsEnabled(true);
            admin.setEmailVerified(true);
            
            userRepository.save(admin);
            System.out.println("Admin user created successfully: " + adminEmail + " / " + password);
            System.out.println("WARNING: Please change the admin password immediately!");
        } else {
            // 不自动更新密码，保持现有密码
            System.out.println("Admin user already exists: " + adminEmail);
        }
    }
    
    private String generateRandomPassword() {
        // 生成12位随机密码
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }
}
