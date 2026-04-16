package com.example.blog.service;

import com.example.blog.model.Article;
import com.example.blog.model.ArticleLike;
import com.example.blog.model.ArticleFavorite;
import com.example.blog.model.User;
import com.example.blog.repository.ArticleLikeRepository;
import com.example.blog.repository.ArticleFavoriteRepository;
import com.example.blog.repository.ArticleRepository;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ArticleInteractionService {

    @Autowired
    private ArticleLikeRepository likeRepository;

    @Autowired
    private ArticleFavoriteRepository favoriteRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    // ==================== 点赞 ====================

    @Transactional
    public Map<String, Object> toggleLike(Integer articleId, Integer userId) {
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new RuntimeException("文章不存在"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));

        Optional<ArticleLike> existingLike = likeRepository.findByArticleIdAndUserId(articleId, userId);

        boolean liked;
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            liked = false;
        } else {
            ArticleLike like = new ArticleLike();
            like.setArticle(article);
            like.setUser(user);
            likeRepository.save(like);
            liked = true;

            if (!article.getUser().getId().equals(userId)) {
                notificationService.createNotification(
                    article.getUser(), user, "LIKE",
                    "有人点赞了你的文章", article.getTitle(),
                    "/article/" + articleId);
            }
        }

        long likeCount = likeRepository.countByArticleId(articleId);
        article.setLikeCount((int) likeCount);
        articleRepository.save(article);

        Map<String, Object> result = new HashMap<>();
        result.put("liked", liked);
        result.put("likeCount", likeCount);
        return result;
    }

    public boolean isLiked(Integer articleId, Integer userId) {
        return likeRepository.existsByArticleIdAndUserId(articleId, userId);
    }

    // ==================== 收藏 ====================

    @Transactional
    public Map<String, Object> toggleFavorite(Integer articleId, Integer userId) {
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new RuntimeException("文章不存在"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));

        Optional<ArticleFavorite> existingFavorite = favoriteRepository.findByArticleIdAndUserId(articleId, userId);

        boolean favorited;
        if (existingFavorite.isPresent()) {
            favoriteRepository.delete(existingFavorite.get());
            favorited = false;
        } else {
            ArticleFavorite favorite = new ArticleFavorite();
            favorite.setArticle(article);
            favorite.setUser(user);
            favoriteRepository.save(favorite);
            favorited = true;

            if (!article.getUser().getId().equals(userId)) {
                notificationService.createNotification(
                    article.getUser(), user, "FAVORITE",
                    "有人收藏了你的文章", article.getTitle(),
                    "/article/" + articleId);
            }
        }

        long favoriteCount = favoriteRepository.countByArticleId(articleId);
        article.setFavoriteCount((int) favoriteCount);
        articleRepository.save(article);

        Map<String, Object> result = new HashMap<>();
        result.put("favorited", favorited);
        result.put("favoriteCount", favoriteCount);
        return result;
    }

    public boolean isFavorited(Integer articleId, Integer userId) {
        return favoriteRepository.existsByArticleIdAndUserId(articleId, userId);
    }
}
