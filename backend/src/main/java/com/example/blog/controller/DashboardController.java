package com.example.blog.controller;

import com.example.blog.model.User;
import com.example.blog.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class DashboardController {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("articleCount", articleRepository.count());
        stats.put("commentCount", commentRepository.count());
        stats.put("userCount", userRepository.count());
        stats.put("tagCount", tagRepository.count());

        // 今日新增
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        stats.put("todayArticles", articleRepository.countByCreatedAtAfter(todayStart));
        stats.put("todayComments", commentRepository.countByCreatedAtAfter(todayStart));
        stats.put("todayUsers", userRepository.countByCreatedAtAfter(todayStart));

        // 最近文章(5条)
        List<Map<String, Object>> recentArticles = new ArrayList<>();
        articleRepository.findTop5ByOrderByCreatedAtDesc().forEach(a -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", a.getId());
            m.put("title", a.getTitle());
            m.put("status", a.getStatus());
            m.put("viewCount", a.getViewCount());
            m.put("createdAt", a.getCreatedAt() != null ? a.getCreatedAt().toString() : null);
            recentArticles.add(m);
        });
        stats.put("recentArticles", recentArticles);

        // 最近评论(5条)
        List<Map<String, Object>> recentComments = new ArrayList<>();
        commentRepository.findTop5ByOrderByCreatedAtDesc().forEach(c -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", c.getId());
            m.put("content", c.getContent());
            m.put("createdAt", c.getCreatedAt() != null ? c.getCreatedAt().toString() : null);
            if (c.getUser() != null) {
                Map<String, Object> u = new LinkedHashMap<>();
                u.put("id", c.getUser().getId());
                u.put("username", c.getUser().getUsername());
                m.put("user", u);
            }
            if (c.getArticle() != null) {
                Map<String, Object> a = new LinkedHashMap<>();
                a.put("id", c.getArticle().getId());
                a.put("title", c.getArticle().getTitle());
                m.put("article", a);
            }
            recentComments.add(m);
        });
        stats.put("recentComments", recentComments);

        return ResponseEntity.ok(stats);
    }
}