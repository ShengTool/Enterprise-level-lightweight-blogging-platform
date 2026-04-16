package com.example.blog;

import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class TestPasswordMatch {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        // 创建一个BCryptPasswordEncoder实例
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        // 测试密码匹配
        String rawPassword = "123456";
        
        // 生成密码哈希
        String hashedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Password hash: " + hashedPassword);
        
        // 验证密码
        boolean matches = passwordEncoder.matches(rawPassword, hashedPassword);
        System.out.println("Password matches: " + matches);
        
        // 注意：在实际运行时，需要在Spring Boot上下文启动后执行以下代码
        // User admin = userRepository.findByEmail("admin@liteblog.com");
        // if (admin != null) {
        //     System.out.println("Admin user found: " + admin.getEmail());
        //     System.out.println("Password hash: " + admin.getPassword());
        //     boolean passwordMatches = passwordEncoder.matches("123456", admin.getPassword());
        //     System.out.println("Password matches: " + passwordMatches);
        // } else {
        //     System.out.println("Admin user not found");
        // }
    }
}
