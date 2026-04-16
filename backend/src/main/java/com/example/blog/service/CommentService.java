package com.example.blog.service;

import com.example.blog.model.Comment;
import com.example.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getByArticleId(Integer articleId) {
        return commentRepository.findByArticleId(articleId);
    }

    public Comment create(Comment comment) {
        return commentRepository.save(comment);
    }

    public void delete(Integer id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> getByUserId(Integer userId) {
        return commentRepository.findByUserId(userId);
    }

    public Comment getById(Integer id) {
        return commentRepository.findById(id).orElse(null);
    }

}