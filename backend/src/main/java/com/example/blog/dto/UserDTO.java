package com.example.blog.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.blog.model.User;

public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private String avatar;
    private String bio;
    private Boolean isAdmin;
    private Boolean isEnabled;
    private Boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Map<String, Object>> articles; // 简要文章列表

    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAvatar(user.getAvatar());
        dto.setBio(user.getBio());
        dto.setIsAdmin(user.getIsAdmin());
        dto.setIsEnabled(user.getIsEnabled());
        dto.setEmailVerified(user.getEmailVerified());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    public Map<String, Object> toMap() {
        return Map.of(
            "id", id,
            "username", username,
            "email", email,
            "avatar", avatar != null ? avatar : "",
            "bio", bio != null ? bio : "",
            "isAdmin", isAdmin != null ? isAdmin : false,
            "isEnabled", isEnabled != null ? isEnabled : true,
            "emailVerified", emailVerified != null ? emailVerified : false,
            "createdAt", createdAt != null ? createdAt.toString() : "",
            "updatedAt", updatedAt != null ? updatedAt.toString() : ""
        );
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Boolean getIsAdmin() { return isAdmin; }
    public void setIsAdmin(Boolean isAdmin) { this.isAdmin = isAdmin; }

    public Boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }

    public Boolean getEmailVerified() { return emailVerified; }
    public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<Map<String, Object>> getArticles() { return articles; }
    public void setArticles(List<Map<String, Object>> articles) { this.articles = articles; }
}
