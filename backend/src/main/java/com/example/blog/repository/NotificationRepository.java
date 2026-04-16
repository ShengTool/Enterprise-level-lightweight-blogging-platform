package com.example.blog.repository;

import com.example.blog.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Page<Notification> findByUserIdOrderByCreatedAtDesc(Integer userId, Pageable pageable);
    Page<Notification> findByUserIdAndIsReadOrderByCreatedAtDesc(Integer userId, Boolean isRead, Pageable pageable);
    long countByUserIdAndIsRead(Integer userId, Boolean isRead);
    void deleteByUserId(Integer userId);
}
