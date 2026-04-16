package com.example.blog;

import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class TestCreateAdmin {
    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        // 创建一个BCryptPasswordEncoder实例
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        // 生成密码哈希
        String hashedPassword = passwordEncoder.encode("123456");
        System.out.println("Admin password hash: " + hashedPassword);
        
        // 注意：在实际运行时，需要在Spring Boot上下文启动后执行以下代码
        // User admin = new User();
        // admin.setUsername("admin");
        // admin.setEmail("admin@liteblog.com");
        // admin.setPassword(hashedPassword);
        // admin.setIsAdmin(true);
        // admin.setIsEnabled(true);
        // userRepository.save(admin);
        // System.out.println("Admin user created successfully!");
    }
}
