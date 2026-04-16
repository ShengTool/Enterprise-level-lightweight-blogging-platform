package com.example.blog.service;

import com.example.blog.model.Article;
import com.example.blog.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getAllPublished() {
        return articleRepository.findByStatus("PUBLISHED");
    }

    public Article getById(Integer id) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article != null && "PUBLISHED".equals(article.getStatus())) {
            article.setViewCount(article.getViewCount() + 1);
            articleRepository.save(article);
        }
        return article;
    }

    public Article create(Article article) {
        return articleRepository.save(article);
    }

    public Article update(Article article) {
        return articleRepository.save(article);
    }

    public void delete(Integer id) {
        articleRepository.deleteById(id);
    }

    public List<Article> getByUserId(Integer userId) {
        return articleRepository.findByUserId(userId);
    }

    public Page<Article> getArticles(Integer page, Integer limit, String tag, String search) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        if (tag != null && !tag.isEmpty() && search != null && !search.isEmpty()) {
            // 组合查询：标签+搜索 - 先获取所有标签匹配的文章，然后在内存中过滤
            Page<Article> tagPage = articleRepository.findByTagName(tag, Pageable.unpaged());
            List<Article> filteredArticles = tagPage.getContent().stream()
                .filter(article -> article.getTitle().toLowerCase().contains(search.toLowerCase()) 
                    || article.getContent().toLowerCase().contains(search.toLowerCase())
                    || article.getSummary().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
            
            // 手动分页
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), filteredArticles.size());
            if (start > filteredArticles.size()) {
                return new PageImpl<>(Collections.emptyList(), pageable, filteredArticles.size());
            }
            return new PageImpl<>(filteredArticles.subList(start, end), pageable, filteredArticles.size());
        } else if (tag != null && !tag.isEmpty()) {
            // 按标签查询
            return articleRepository.findByTagName(tag, pageable);
        } else if (search != null && !search.isEmpty()) {
            // 搜索查询
            return articleRepository.searchArticles(search, pageable);
        } else {
            // 所有文章
            return articleRepository.findByStatusOrderByCreatedAtDesc("PUBLISHED", pageable);
        }
    }

    public long getArticleCount(String tag, String search) {
        if (tag != null && !tag.isEmpty() && search != null && !search.isEmpty()) {
            // 对于组合查询，需要特殊处理
            Page<Article> page = articleRepository.findByTagName(tag, Pageable.unpaged());
            long count = page.getContent().stream()
                .filter(article -> article.getTitle().toLowerCase().contains(search.toLowerCase()) 
                    || article.getContent().toLowerCase().contains(search.toLowerCase())
                    || article.getSummary().toLowerCase().contains(search.toLowerCase()))
                .count();
            return count;
        } else if (tag != null && !tag.isEmpty()) {
            return articleRepository.findByTagName(tag, Pageable.unpaged()).getTotalElements();
        } else if (search != null && !search.isEmpty()) {
            return articleRepository.searchArticles(search, Pageable.unpaged()).getTotalElements();
        } else {
            return articleRepository.findByStatusOrderByCreatedAtDesc("PUBLISHED", Pageable.unpaged()).getTotalElements();
        }
    }
}