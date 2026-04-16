package com.example.blog.controller;

import com.example.blog.model.User;
import com.example.blog.service.ArticleInteractionService;
import com.example.blog.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class InteractionController {

    @Autowired
    private ArticleInteractionService interactionService;

    @Autowired
    private NotificationService notificationService;

    private Integer getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) {
            return ((User) auth.getPrincipal()).getId();
        }
        return null;
    }

    // 点赞
    @PostMapping("/articles/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Integer id) {
        Integer userId = getCurrentUserId();
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "未授权"));
        try {
            return ResponseEntity.ok(interactionService.toggleLike(id, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/articles/{id}/like/status")
    public ResponseEntity<?> getLikeStatus(@PathVariable Integer id) {
        Integer userId = getCurrentUserId();
        boolean liked = userId != null && interactionService.isLiked(id, userId);
        return ResponseEntity.ok(Map.of("liked", liked));
    }

    // 收藏
    @PostMapping("/articles/{id}/favorite")
    public ResponseEntity<?> toggleFavorite(@PathVariable Integer id) {
        Integer userId = getCurrentUserId();
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "未授权"));
        try {
            return ResponseEntity.ok(interactionService.toggleFavorite(id, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/articles/{id}/favorite/status")
    public ResponseEntity<?> getFavoriteStatus(@PathVariable Integer id) {
        Integer userId = getCurrentUserId();
        boolean favorited = userId != null && interactionService.isFavorited(id, userId);
        return ResponseEntity.ok(Map.of("favorited", favorited));
    }

    // 通知
    @GetMapping("/notifications")
    public ResponseEntity<?> getNotifications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Integer userId = getCurrentUserId();
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "未授权"));
        Page<?> notifications = notificationService.getUserNotifications(userId, page - 1, size);
        return ResponseEntity.ok(Map.of(
            "notifications", notifications.getContent(),
            "total", notifications.getTotalElements(),
            "unreadCount", notificationService.getUnreadCount(userId)
        ));
    }

    @GetMapping("/notifications/unread-count")
    public ResponseEntity<?> getUnreadCount() {
        Integer userId = getCurrentUserId();
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "未授权"));
        return ResponseEntity.ok(Map.of("count", notificationService.getUnreadCount(userId)));
    }

    @PostMapping("/notifications/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Integer id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(Map.of("message", "已标记为已读"));
    }

    @PostMapping("/notifications/read-all")
    public ResponseEntity<?> markAllAsRead() {
        Integer userId = getCurrentUserId();
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "未授权"));
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(Map.of("message", "已标记全部为已读"));
    }
}
