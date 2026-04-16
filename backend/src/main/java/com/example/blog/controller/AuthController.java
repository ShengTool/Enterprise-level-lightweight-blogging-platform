package com.example.blog.controller;

import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import com.example.blog.security.JwtUtil;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // 登录请求
    public static class LoginRequest {
        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        public String email;
        @NotBlank(message = "密码不能为空")
        public String password;
    }

    // 注册请求
    public static class RegisterRequest {
        @NotBlank(message = "用户名不能为空")
        @Size(min = 3, max = 50)
        public String username;
        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        public String email;
        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 100)
        public String password;
    }

    // 修改密码请求
    public static class ChangePasswordRequest {
        @NotBlank(message = "旧密码不能为空")
        public String oldPassword;
        @NotBlank(message = "新密码不能为空")
        @Size(min = 6, max = 100)
        public String newPassword;
    }

    private Map<String, Object> userToMap(User user) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", user.getId());
        m.put("username", user.getUsername());
        m.put("email", user.getEmail());
        m.put("avatar", user.getAvatar() != null ? user.getAvatar() : "");
        m.put("isAdmin", user.getIsAdmin());
        return m;
    }

    // 登录
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "邮箱或密码错误"));
        }
        if (!user.getIsEnabled()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "账户已被禁用"));
        }
        if (!passwordEncoder.matches(request.password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "邮箱或密码错误"));
        }
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(Map.of("token", token, "user", userToMap(user)));
    }

    // 注册
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.username) != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "用户名已被注册"));
        }
        if (userRepository.findByEmail(request.email) != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "邮箱已被注册"));
        }
        User user = new User();
        user.setUsername(request.username);
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setIsAdmin(false);
        user.setIsEnabled(true);
        user.setEmailVerified(false);
        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser.getEmail());
        return ResponseEntity.ok(Map.of("token", token, "user", userToMap(savedUser)));
    }

    // 修改密码
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestAttribute("user") User user,
                                            @Valid @RequestBody ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.oldPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("error", "旧密码不正确"));
        }
        user.setPassword(passwordEncoder.encode(request.newPassword));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "密码修改成功"));
    }

    // 忘记密码
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            // 生成随机令牌并哈希存储
            String resetToken = UUID.randomUUID().toString();
            String hashedToken = passwordEncoder.encode(resetToken);
            user.setPasswordResetToken(hashedToken);
            user.setPasswordResetTokenExpire(LocalDateTime.now().plusHours(1));
            userRepository.save(user);
            
            // 在实际应用中，这里应该发送包含resetToken的邮件
            // 为了演示目的，我们记录令牌（生产环境中不要这样做）
            System.out.println("Password reset token for " + email + ": " + resetToken);
        }
        return ResponseEntity.ok(Map.of("message", "如果邮箱存在，重置链接已发送"));
    }

    // 重置密码
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token,
                                            @RequestParam String newPassword) {
        // 查找所有未过期的重置令牌并验证
        List<User> users = userRepository.findAll().stream()
            .filter(u -> u.getPasswordResetToken() != null 
                && u.getPasswordResetTokenExpire() != null
                && u.getPasswordResetTokenExpire().isAfter(LocalDateTime.now()))
            .collect(Collectors.toList());
            
        User user = null;
        for (User u : users) {
            if (passwordEncoder.matches(token, u.getPasswordResetToken())) {
                user = u;
                break;
            }
        }
        
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "无效的重置链接或链接已过期"));
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpire(null);
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "密码重置成功"));
    }

    // 验证 token
    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestAttribute("user") User user) {
        return ResponseEntity.ok(userToMap(user));
    }
}
