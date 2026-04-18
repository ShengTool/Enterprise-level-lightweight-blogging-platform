-- =============================================
-- LiteBlog 数据库初始化脚本 v2.0
-- 与所有 JPA 实体完全匹配
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS liteblog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE liteblog;

-- =============================================
-- 1. 用户表 (users)
-- =============================================
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    avatar VARCHAR(500),
    bio VARCHAR(200),
    is_admin BOOLEAN NOT NULL DEFAULT FALSE,
    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    email_verified BOOLEAN DEFAULT FALSE,
    email_verification_token VARCHAR(100),
    password_reset_token VARCHAR(100),
    password_reset_token_expire DATETIME,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    INDEX idx_users_email (email),
    INDEX idx_users_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 2. 文章标签表 (tag)
-- =============================================
CREATE TABLE IF NOT EXISTS tag (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    color VARCHAR(20) DEFAULT '#3B82F6',
    count INT DEFAULT 0,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    INDEX idx_tag_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 3. 文章表 (article)
-- =============================================
CREATE TABLE IF NOT EXISTS article (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    summary VARCHAR(255),
    cover_image VARCHAR(500),
    status ENUM('DRAFT', 'PUBLISHED') DEFAULT 'DRAFT',
    user_id INT NOT NULL,
    view_count INT DEFAULT 0,
    like_count INT DEFAULT 0,
    favorite_count INT DEFAULT 0,
    comment_count INT DEFAULT 0,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_article_status (status),
    INDEX idx_article_user (user_id),
    INDEX idx_article_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 4. 文章-标签关联表 (article_tag)
-- =============================================
CREATE TABLE IF NOT EXISTS article_tag (
    article_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (article_id, tag_id),
    FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tag(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 5. 评论表 (comment)
-- =============================================
CREATE TABLE IF NOT EXISTS comment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    article_id INT NOT NULL,
    user_id INT NOT NULL,
    parent_id INT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES comment(id) ON DELETE CASCADE,
    INDEX idx_comment_article (article_id),
    INDEX idx_comment_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 6. 文章点赞表 (article_likes)
-- =============================================
CREATE TABLE IF NOT EXISTS article_likes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    article_id INT NOT NULL,
    user_id INT NOT NULL,
    created_at DATETIME NOT NULL,
    UNIQUE KEY uk_article_user (article_id, user_id),
    FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_likes_article (article_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 7. 文章收藏表 (article_favorites)
-- =============================================
CREATE TABLE IF NOT EXISTS article_favorites (
    id INT AUTO_INCREMENT PRIMARY KEY,
    article_id INT NOT NULL,
    user_id INT NOT NULL,
    created_at DATETIME NOT NULL,
    UNIQUE KEY uk_fav_article_user (article_id, user_id),
    FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_fav_article (article_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 8. 通知表 (notifications)
-- =============================================
CREATE TABLE IF NOT EXISTS notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    type VARCHAR(20) NOT NULL,
    title VARCHAR(200) NOT NULL,
    content VARCHAR(500),
    link VARCHAR(500),
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    from_user_id INT,
    created_at DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (from_user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_notification_user (user_id),
    INDEX idx_notification_read (is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 插入初始数据
-- =============================================

-- 插入管理员用户 (密码: admin123)
-- BCrypt hash from Spring Security BCryptPasswordEncoder
INSERT INTO users (username, email, password, avatar, bio, is_admin, is_enabled, email_verified, created_at, updated_at)
VALUES ('admin', 'admin@liteblog.com', '$2a$10$eACCYoYRKqHMIGfOXgy.HOYGvWQn1Eqf0bOKLQf8z/oq2mKQX1O5K', NULL, 'LiteBlog 系统管理员', TRUE, TRUE, TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 插入测试用户 (密码: test123)
INSERT INTO users (username, email, password, avatar, bio, is_admin, is_enabled, email_verified, created_at, updated_at)
VALUES ('testuser', 'test@liteblog.com', '$2a$10$3IqM3yGJ.xPQEaY3mXy3/.N1r2pWvZGz1z6wPMmQfL3Y2pK0cF7We', NULL, '这是测试用户', FALSE, TRUE, TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 插入示例标签
INSERT INTO tag (name, color, count, created_at, updated_at) VALUES
('前端', '#0066FF', 0, NOW(), NOW()),
('后端', '#10B981', 0, NOW(), NOW()),
('Vue', '#42B883', 0, NOW(), NOW()),
('Java', '#ED8B00', 0, NOW(), NOW()),
('Spring Boot', '#6DB33F', 0, NOW(), NOW()),
('TypeScript', '#3178C6', 0, NOW(), NOW()),
('React', '#61DAFB', 0, NOW(), NOW()),
('Node.js', '#339933', 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 插入示例文章
INSERT INTO article (title, summary, content, cover_image, status, user_id, view_count, like_count, favorite_count, comment_count, created_at, updated_at)
SELECT 
    '欢迎使用 LiteBlog 企业级博客平台',
    'LiteBlog 是一个功能强大、界面精美的企业级博客系统，基于 Vue 3 + Spring Boot 构建，支持 Markdown 写作、点赞收藏、评论互动等功能。',
    CONCAT('# 欢迎使用 LiteBlog\n\nLiteBlog 是一个现代化的企业级博客平台，提供了丰富的功能和优美的用户体验。\n\n## 主要特性\n\n- 🎨 **精美UI设计** - 现代化企业级界面，参考 Linear、Vercel 风格\n- ⚡ **高性能** - 基于 Vue 3 + Vite + TypeScript\n- 🔐 **安全可靠** - JWT 认证 + BCrypt 密码加密\n- 📱 **响应式** - 完美适配桌面和移动设备\n\n## 技术栈\n\n| 分类 | 技术 |\n|------|------|\n| 前端 | Vue 3 + TypeScript + TailwindCSS |\n| 后端 | Spring Boot 3.2 + JPA |\n| 数据库 | MySQL 8.0 |\n| 安全 | Spring Security + JWT |\n\n## 快速开始\n\n```bash\n# 前端\nnpm install\nnpm run dev\n\n# 后端\nmvn spring-boot:run\n```\n\n## 功能预览\n\n- ✅ 用户注册/登录\n- ✅ 文章发布/编辑\n- ✅ Markdown 编辑器\n- ✅ 点赞/收藏\n- ✅ 评论互动\n- ✅ 暗黑模式\n- ✅ PWA 支持'),
    NULL,
    'PUBLISHED',
    (SELECT id FROM users WHERE email = 'admin@liteblog.com' LIMIT 1),
    128,
    12,
    6,
    3,
    NOW(),
    NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM article WHERE title = '欢迎使用 LiteBlog 企业级博客平台');

-- 插入第二篇示例文章
INSERT INTO article (title, summary, content, cover_image, status, user_id, view_count, like_count, favorite_count, comment_count, created_at, updated_at)
SELECT 
    'Spring Boot 3.2 企业级开发实战',
    '深入讲解 Spring Boot 3.2 的新特性，结合实际项目展示企业级应用开发的最佳实践。',
    CONCAT('# Spring Boot 3.2 企业级开发实战\n\nSpring Boot 3.2带来了众多新特性和性能改进，本篇文章将带你深入了解。\n\n## 新特性概览\n\n- 🌱 **Native Image** - 启动时间大幅缩短\n- 🔒 **虚拟线程支持** - 高并发处理能力提升\n- 📦 **AOT 编译** - 构建时预编译，性能飞跃\n\n## 代码示例\n\n```java\n@SpringBootApplication\npublic class Application {\n    public static void main(String[] args) {\n        SpringApplication.run(Application.class, args);\n    }\n}\n```\n\n更多内容持续更新中...'),
    NULL,
    'PUBLISHED',
    (SELECT id FROM users WHERE email = 'admin@liteblog.com' LIMIT 1),
    89,
    5,
    3,
    1,
    DATE_SUB(NOW(), INTERVAL 1 DAY),
    DATE_SUB(NOW(), INTERVAL 1 DAY)
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM article WHERE title = 'Spring Boot 3.2 企业级开发实战');

-- 插入示例评论
INSERT INTO comment (content, article_id, user_id, parent_id, created_at, updated_at)
SELECT '写得真好！非常感谢分享！', a.id, u.id, NULL, NOW(), NOW()
FROM article a, users u
WHERE a.title = '欢迎使用 LiteBlog 企业级博客平台'
AND u.email = 'test@liteblog.com'
AND NOT EXISTS (SELECT 1 FROM comment WHERE content = '写得真好！非常感谢分享！');

INSERT INTO comment (content, article_id, user_id, parent_id, created_at, updated_at)
SELECT '请问支持哪些数据库？', a.id, u.id, NULL, DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR)
FROM article a, users u
WHERE a.title = '欢迎使用 LiteBlog 企业级博客平台'
AND u.email = 'test@liteblog.com'
AND NOT EXISTS (SELECT 1 FROM comment WHERE content = '请问支持哪些数据库？');

-- 将文章关联到标签
INSERT INTO article_tag (article_id, tag_id)
SELECT a.id, t.id
FROM article a
CROSS JOIN tag t
WHERE a.title = '欢迎使用 LiteBlog 企业级博客平台'
AND t.name IN ('前端', '后端', 'Vue', 'Spring Boot')
AND NOT EXISTS (
    SELECT 1 FROM article_tag WHERE article_id = a.id AND tag_id = t.id
);

INSERT INTO article_tag (article_id, tag_id)
SELECT a.id, t.id
FROM article a
CROSS JOIN tag t
WHERE a.title = 'Spring Boot 3.2 企业级开发实战'
AND t.name IN ('后端', 'Java', 'Spring Boot')
AND NOT EXISTS (
    SELECT 1 FROM article_tag WHERE article_id = a.id AND tag_id = t.id
);

-- 更新标签计数
UPDATE tag t
SET t.count = (
    SELECT COUNT(*) FROM article_tag at WHERE at.tag_id = t.id
);
