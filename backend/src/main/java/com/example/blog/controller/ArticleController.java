package com.example.blog.controller;

import com.example.blog.model.Article;
import com.example.blog.repository.ArticleRepository;
import com.example.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getArticles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String search) {

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Article> articlePage;

        if (search != null && !search.isEmpty()) {
            articlePage = articleRepository.searchArticles(search, pageRequest);
        } else if (tag != null && !tag.isEmpty()) {
            articlePage = articleRepository.findByTagName(tag, pageRequest);
        } else {
            articlePage = articleRepository.findByStatusOrderByCreatedAtDesc("PUBLISHED", pageRequest);
        }

        List<Map<String, Object>> articles = articlePage.getContent().stream()
            .map(this::toArticleMap)
            .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
            "articles", articles,
            "total", articlePage.getTotalElements(),
            "page", page,
            "size", size,
            "totalPages", articlePage.getTotalPages()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArticle(@PathVariable Integer id) {
        Optional<Article> articleOpt = articleRepository.findByIdForDetail(id);
        if (!articleOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Article article = articleOpt.get();
        articleRepository.incrementViewCount(id);
        return ResponseEntity.ok(toArticleMap(article));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Map<String, Object>>> getPopularArticles(
            @RequestParam(defaultValue = "5") int limit) {
        Page<Article> popularPage = articleRepository.findPopularArticles(PageRequest.of(0, limit));
        List<Map<String, Object>> articles = popularPage.getContent().stream()
            .map(this::toArticleMap)
            .collect(Collectors.toList());
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/my")
    public ResponseEntity<Map<String, Object>> getMyArticles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        User user = getCurrentUser();
        if (user == null) return ResponseEntity.status(401).body(Map.of("error", "未授权"));
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Article> articlePage = articleRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageRequest);
        List<Map<String, Object>> articles = articlePage.getContent().stream()
            .map(this::toArticleMap)
            .collect(Collectors.toList());
        return ResponseEntity.ok(Map.of(
            "articles", articles,
            "total", articlePage.getTotalElements()
        ));
    }

    @PostMapping
    public ResponseEntity<?> createArticle(@RequestBody Article article) {
        User user = getCurrentUser();
        if (user == null) return ResponseEntity.status(401).body(Map.of("error", "未授权"));
        article.setUser(user);
        article.setStatus("PUBLISHED");
        return ResponseEntity.ok(articleRepository.save(article));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateArticle(@PathVariable Integer id, @RequestBody Article article) {
        User user = getCurrentUser();
        if (user == null) return ResponseEntity.status(401).body(Map.of("error", "未授权"));
        Optional<Article> existing = articleRepository.findById(id);
        if (!existing.isPresent()) return ResponseEntity.notFound().build();
        Article a = existing.get();
        if (!a.getUser().getId().equals(user.getId()) && !user.getIsAdmin()) {
            return ResponseEntity.status(403).body(Map.of("error", "无权限"));
        }
        article.setId(id);
        article.setUser(user);
        return ResponseEntity.ok(articleRepository.save(article));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Integer id) {
        User user = getCurrentUser();
        if (user == null) return ResponseEntity.status(401).body(Map.of("error", "未授权"));
        Optional<Article> existing = articleRepository.findById(id);
        if (!existing.isPresent()) return ResponseEntity.notFound().build();
        Article a = existing.get();
        if (!a.getUser().getId().equals(user.getId()) && !user.getIsAdmin()) {
            return ResponseEntity.status(403).body(Map.of("error", "无权限"));
        }
        articleRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "删除成功"));
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) {
            return (User) auth.getPrincipal();
        }
        return null;
    }

    private Map<String, Object> toArticleMap(Article article) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", article.getId());
        map.put("title", article.getTitle());
        map.put("summary", article.getSummary());
        map.put("content", article.getContent());
        map.put("coverImage", article.getCoverImage());
        map.put("viewCount", article.getViewCount());
        map.put("likeCount", article.getLikeCount());
        map.put("favoriteCount", article.getFavoriteCount());
        map.put("commentCount", article.getCommentCount());
        map.put("status", article.getStatus());
        map.put("createdAt", article.getCreatedAt());
        map.put("updatedAt", article.getUpdatedAt());

        if (article.getTags() != null) {
            map.put("tags", article.getTags().stream().map(tag ->
                Map.of("id", tag.getId(), "name", tag.getName(), "color", tag.getColor() != null ? tag.getColor() : "#3B82F6")
            ).collect(Collectors.toList()));
        } else {
            map.put("tags", new ArrayList<>());
        }

        if (article.getUser() != null) {
            map.put("user", Map.of(
                "id", article.getUser().getId(),
                "username", article.getUser().getUsername(),
                "avatar", article.getUser().getAvatar() != null ? article.getUser().getAvatar() : ""
            ));
        }

        return map;
    }
}
