package com.example.blog;

import com.example.blog.model.User;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class TestAdminAccount {
    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        // 测试管理员账号是否存在
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("Admin password hash: " + passwordEncoder.encode("123456"));
    }
}
