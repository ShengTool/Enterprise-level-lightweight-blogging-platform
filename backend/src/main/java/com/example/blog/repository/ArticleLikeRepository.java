package com.example.blog.repository;

import com.example.blog.model.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Integer> {
    Optional<ArticleLike> findByArticleIdAndUserId(Integer articleId, Integer userId);
    long countByArticleId(Integer articleId);
    boolean existsByArticleIdAndUserId(Integer articleId, Integer userId);
    void deleteByArticleIdAndUserId(Integer articleId, Integer userId);
}
