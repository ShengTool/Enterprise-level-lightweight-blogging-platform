package com.example.blog.controller;

import com.example.blog.model.Comment;
import com.example.blog.model.User;
import com.example.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<?> getComments(@RequestParam Integer articleId) {
        List<Comment> comments = commentService.getByArticleId(articleId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<?> createComment(@RequestAttribute("user") User user, @RequestBody Comment comment) {
        comment.setUser(user);
        Comment createdComment = commentService.create(comment);
        return ResponseEntity.ok(createdComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@RequestAttribute("user") User user, @PathVariable Integer id) {
        // 检查权限：用户只能删除自己的评论或管理员可以删除所有评论
        Comment comment = commentService.getById(id);
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("评论不存在");
        }
        
        if (!comment.getUser().getId().equals(user.getId()) && !user.getIsAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("无权删除此评论");
        }
        
        commentService.delete(id);
        return ResponseEntity.ok("评论删除成功");
    }
}