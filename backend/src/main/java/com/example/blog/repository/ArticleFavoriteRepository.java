package com.example.blog.repository;

import com.example.blog.model.ArticleFavorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleFavoriteRepository extends JpaRepository<ArticleFavorite, Integer> {
    Optional<ArticleFavorite> findByArticleIdAndUserId(Integer articleId, Integer userId);
    long countByArticleId(Integer articleId);
    boolean existsByArticleIdAndUserId(Integer articleId, Integer userId);
    void deleteByArticleIdAndUserId(Integer articleId, Integer userId);
    Page<ArticleFavorite> findByUserIdOrderByCreatedAtDesc(Integer userId, Pageable pageable);
}
