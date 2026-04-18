package com.example.blog.repository;

import com.example.blog.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findByStatus(String status);
    List<Article> findByUserId(Integer userId);
    List<Article> findByStatusAndTagsName(String status, String tagName);
    List<Article> findByStatusAndTitleContainingOrContentContaining(String status, String search1, String search2);
    List<Article> findByStatusAndTagsNameAndTitleContainingOrContentContaining(String status, String tagName, String search1, String search2);

    long countByCreatedAtAfter(LocalDateTime date);
    List<Article> findTop5ByOrderByCreatedAtDesc();

    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT a FROM Article a LEFT JOIN FETCH a.tags LEFT JOIN FETCH a.user WHERE a.status = :status ORDER BY a.createdAt DESC")
    Page<Article> findByStatusOrderByCreatedAtDesc(@Param("status") String status, Pageable pageable);

    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT a FROM Article a LEFT JOIN FETCH a.tags LEFT JOIN FETCH a.user WHERE a.user.id = :userId ORDER BY a.createdAt DESC")
    Page<Article> findByUserIdOrderByCreatedAtDesc(@Param("userId") Integer userId, Pageable pageable);

    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT a FROM Article a LEFT JOIN FETCH a.tags t LEFT JOIN FETCH a.user WHERE a.status = 'PUBLISHED' AND t.name = :tagName ORDER BY a.createdAt DESC")
    Page<Article> findByTagName(@Param("tagName") String tagName, Pageable pageable);

    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT a FROM Article a LEFT JOIN FETCH a.tags LEFT JOIN FETCH a.user WHERE a.status = 'PUBLISHED' AND (a.title LIKE %:keyword% OR a.content LIKE %:keyword% OR a.summary LIKE %:keyword%) ORDER BY a.createdAt DESC")
    Page<Article> searchArticles(@Param("keyword") String keyword, Pageable pageable);

    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT a FROM Article a LEFT JOIN FETCH a.tags LEFT JOIN FETCH a.user WHERE a.status = 'PUBLISHED' ORDER BY a.viewCount DESC")
    Page<Article> findPopularArticles(Pageable pageable);

    @Transactional(readOnly = true)
    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.tags LEFT JOIN FETCH a.user WHERE a.id = :id")
    Optional<Article> findByIdForDetail(@Param("id") Integer id);
    
    @Transactional
    @Modifying
    @Query("UPDATE Article a SET a.viewCount = a.viewCount + 1 WHERE a.id = :articleId")
    int incrementViewCount(@Param("articleId") Integer articleId);
}
