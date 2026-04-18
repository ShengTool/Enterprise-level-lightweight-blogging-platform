package com.example.blog.controller;

import com.example.blog.model.Comment;
import com.example.blog.model.User;
import com.example.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    private Map<String, Object> toCommentMap(Comment c) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", c.getId());
        map.put("content", c.getContent());
        map.put("createdAt", c.getCreatedAt().toString());
        map.put("updatedAt", c.getUpdatedAt() != null ? c.getUpdatedAt().toString() : null);
        if (c.getArticle() != null) {
            Map<String, Object> article = new LinkedHashMap<>();
            article.put("id", c.getArticle().getId());
            article.put("title", c.getArticle().getTitle());
            map.put("article", article);
        }
        if (c.getUser() != null) {
            Map<String, Object> user = new LinkedHashMap<>();
            user.put("id", c.getUser().getId());
            user.put("username", c.getUser().getUsername());
            user.put("avatar", c.getUser().getAvatar());
            map.put("user", user);
        }
        if (c.getParent() != null) {
            map.put("parentId", c.getParent().getId());
        } else {
            map.put("parentId", null);
        }
        return map;
    }

    /**
     * GET /api/comments - 管理员获取所有评论
     */
    @GetMapping
    public ResponseEntity<?> getComments(
            @RequestParam(required = false) Integer articleId) {
        List<Comment> comments;
        if (articleId != null) {
            comments = commentService.getByArticleId(articleId);
        } else {
            comments = commentService.getAll();
        }
        List<Map<String, Object>> result = comments.stream()
                .map(this::toCommentMap)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> createComment(@RequestAttribute("user") User user, @RequestBody Comment comment) {
        comment.setUser(user);
        Comment createdComment = commentService.create(comment);
        return ResponseEntity.ok(toCommentMap(createdComment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@RequestAttribute("user") User user, @PathVariable Integer id) {
        Comment comment = commentService.getById(id);
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "评论不存在"));
        }
        if (!comment.getUser().getId().equals(user.getId()) && !user.getIsAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "无权删除此评论"));
        }
        commentService.delete(id);
        return ResponseEntity.ok(Map.of("message", "删除成功"));
    }
}