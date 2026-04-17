package com.example.blog.controller;

import com.example.blog.dto.UserDTO;
import com.example.blog.model.User;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * POST /api/users — 创建用户（管理员）
     */
    @PostMapping
    public ResponseEntity<?> createUser(
            @RequestBody Map<String, Object> body,
            @AuthenticationPrincipal User currentUser) {

        if (currentUser == null || !currentUser.isAdmin()) {
            return ResponseEntity.status(403).body(Map.of("error", "需要管理员权限"));
        }

        String username = (String) body.get("username");
        String email = (String) body.get("email");
        String password = (String) body.get("password");

        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "用户名不能为空"));
        }
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "邮箱不能为空"));
        }
        if (password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "密码不能为空"));
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        if (body.containsKey("isAdmin")) {
            user.setIsAdmin((Boolean) body.get("isAdmin"));
        }
        if (body.containsKey("bio")) {
            user.setBio((String) body.get("bio"));
        }

        User saved = userService.register(user);
        return ResponseEntity.ok(UserDTO.fromEntity(saved).toMap());
    }

    /**
     * GET /api/users — 获取所有用户（管理员）
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<Map<String, Object>> result = users.stream()
                .map(UserDTO::fromEntity)
                .map(UserDTO::toMap)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/users/{id} — 获取单个用户
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        User user = userService.getById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(UserDTO.fromEntity(user).toMap());
    }

    /**
     * PUT /api/users/{id} — 更新用户（管理员可更新任意用户，普通用户只能更新自己）
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> updates,
            @AuthenticationPrincipal User currentUser) {

        User target = userService.getById(id);
        if (target == null) {
            return ResponseEntity.notFound().build();
        }

        // 普通用户只能修改自己
        if (currentUser != null && !currentUser.isAdmin() && !currentUser.getId().equals(id)) {
            return ResponseEntity.status(403).body(Map.of("error", "无权修改其他用户"));
        }

        if (updates.containsKey("username")) {
            target.setUsername((String) updates.get("username"));
        }
        if (updates.containsKey("email")) {
            target.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("password") && updates.get("password") != null) {
            String pwd = (String) updates.get("password");
            if (!pwd.isBlank()) {
                target.setPassword(pwd); // UserService.update 会处理加密
            }
        }
        if (updates.containsKey("bio")) {
            target.setBio((String) updates.get("bio"));
        }
        if (updates.containsKey("avatar")) {
            target.setAvatar((String) updates.get("avatar"));
        }
        // 只有管理员可以修改 isAdmin 和 isEnabled
        if (currentUser != null && currentUser.isAdmin() && updates.containsKey("isAdmin")) {
            target.setIsAdmin((Boolean) updates.get("isAdmin"));
        }
        if (currentUser != null && currentUser.isAdmin() && updates.containsKey("isEnabled")) {
            target.setIsEnabled((Boolean) updates.get("isEnabled"));
        }

        User saved = userService.update(target);
        return ResponseEntity.ok(UserDTO.fromEntity(saved).toMap());
    }

    /**
     * DELETE /api/users/{id} — 删除用户（仅管理员）
     */
    @DeleteMapping("/{id}")

    public ResponseEntity<?> deleteUser(
            @PathVariable Integer id,
            @AuthenticationPrincipal User currentUser) {

        User target = userService.getById(id);
        if (target == null) {
            return ResponseEntity.notFound().build();
        }

        // 禁止删除自己
        if (currentUser != null && currentUser.getId().equals(id)) {
            return ResponseEntity.badRequest().body(Map.of("error", "不能删除自己"));
        }

        userService.delete(id);
        return ResponseEntity.ok(Map.of("message", "删除成功"));
    }
}
