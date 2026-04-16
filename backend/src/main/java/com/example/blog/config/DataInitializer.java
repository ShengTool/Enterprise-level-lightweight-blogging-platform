package com.example.blog.config;

import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Value("${admin.default-password:#{null}}")
    private String defaultAdminPassword;

    @Bean
    public ApplicationRunner initializeData() {
        return args -> {
            // 检查是否已存在管理员账号（根据用户名检查，MySQL varchar 不区分大小写）
            User adminUser = userRepository.findByUsername("admin");
            if (adminUser == null) {
                // 从环境变量获取默认密码，如果没有则跳过创建
                if (defaultAdminPassword != null && !defaultAdminPassword.trim().isEmpty()) {
                    // 创建管理员账号
                    User admin = new User();
                    admin.setUsername("admin");
                    admin.setEmail("admin@liteblog.com");
                    admin.setPassword(passwordEncoder.encode(defaultAdminPassword));
                    admin.setBio("LiteBlog 系统管理员");
                    admin.setIsAdmin(true);
                    admin.setIsEnabled(true);
                    admin.setEmailVerified(true);
                    userRepository.save(admin);
                    System.out.println("Admin account created: admin@liteblog.com");
                    System.out.println("WARNING: Please change the admin password immediately!");
                } else {
                    System.out.println("Skipping admin account creation: No default password provided in environment");
                }
            } else {
                System.out.println("Admin account already exists: " + adminUser.getEmail());
            }
        };
    }
}
