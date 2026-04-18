package com.example.blog.repository;

import com.example.blog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByArticleId(Integer articleId);
    List<Comment> findByUserId(Integer userId);
    List<Comment> findAllByOrderByCreatedAtDesc();
}
