package com.example.blog.service;

import com.example.blog.model.Notification;
import com.example.blog.model.User;
import com.example.blog.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void createNotification(User toUser, User fromUser, String type, String title, String content, String link) {
        Notification notification = new Notification();
        notification.setUser(toUser);
        notification.setFromUser(fromUser);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setLink(link);
        notification.setIsRead(false);
        notificationRepository.save(notification);
    }

    public Page<Notification> getUserNotifications(Integer userId, int page, int size) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, size));
    }

    public Page<Notification> getUnreadNotifications(Integer userId, int page, int size) {
        return notificationRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(userId, false, PageRequest.of(page, size));
    }

    public long getUnreadCount(Integer userId) {
        return notificationRepository.countByUserIdAndIsRead(userId, false);
    }

    public void markAsRead(Integer notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        });
    }

    public void markAllAsRead(Integer userId) {
        Page<Notification> unread = notificationRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(userId, false, PageRequest.of(0, 1000));
        unread.forEach(n -> {
            n.setIsRead(true);
            notificationRepository.save(n);
        });
    }
}
