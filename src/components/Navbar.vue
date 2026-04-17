<template>
  <header class="navbar">
    <div class="container-app">
      <div class="navbar-content">
        <!-- Logo -->
        <router-link to="/" class="navbar-logo">
          <span class="logo-text">
            <span class="logo-main">Lite</span>
            <span class="logo-accent">Blog</span>
          </span>
        </router-link>
        
        <!-- Navigation Links -->
        <nav class="navbar-nav">
          <router-link to="/" class="nav-link" active-class="active">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
              <polyline points="9 22 9 12 15 12 15 22"/>
            </svg>
            首页
          </router-link>
          <router-link to="/about" class="nav-link" active-class="active">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
              <circle cx="12" cy="7" r="4"/>
            </svg>
            关于
          </router-link>
          <router-link to="/search" class="nav-link" active-class="active">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="11" cy="11" r="8"/>
              <line x1="21" y1="21" x2="16.65" y2="16.65"/>
            </svg>
            搜索
          </router-link>
        </nav>
        
        <!-- User Actions -->
        <div class="navbar-actions">
          <template v-if="userStore.user">
            <!-- Logged in user -->
            <router-link
              to="/write"
              class="write-btn"
              title="写文章"
            >
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
              </svg>
              <span>写文章</span>
            </router-link>
            <router-link to="/profile" class="user-menu">
              <img
                :src="userStore.user.avatar || defaultAvatar"
                :alt="userStore.user.username"
                class="user-avatar"
              />
              <span class="user-name">{{ userStore.user.username }}</span>
            </router-link>
            <router-link
              v-if="userStore.user.isAdmin"
              to="/admin"
              class="action-btn"
              title="管理后台"
            >
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
                <line x1="3" y1="9" x2="21" y2="9"/>
                <line x1="9" y1="21" x2="9" y2="9"/>
              </svg>
            </router-link>
          </template>
          <template v-else>
            <!-- Guest user -->
            <router-link to="/login" class="btn btn-secondary">
              登录
            </router-link>
            <router-link to="/register" class="btn btn-primary">
              注册
            </router-link>
          </template>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { useUserStore } from '../stores/user'

const userStore = useUserStore()

const defaultAvatar = 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA0MCA0MCI+PHJlY3Qgd2lkdGg9IjQwIiBoZWlnaHQ9IjQwIiByeD0iMjAiIGZpbGw9IiNFOEU4RTgiLz48Y2lyY2xlIGN4PSIyMCIgY3k9IjE2IiByPSI4IiBmaWxsPSIjQThBOEE4Ii8+PHBhdGggZD0iTTYgMzVjMC04LjI4NCA2LjcxNi0xNSAxNS0xNXMxNSA2LjcxNiAxNSAxNSIgZmlsbD0iI0E4QThBOCIvPjwvc3ZnPg=='
</script>

<style scoped>
.navbar {
  position: sticky;
  top: 0;
  z-index: var(--z-fixed);
  background: var(--color-bg-primary);
  border-bottom: 1px solid var(--color-border-primary);
  height: 64px;
  box-shadow: var(--shadow-sm);
}

.navbar-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  gap: var(--space-8);
}

.navbar-logo {
  display: flex;
  align-items: center;
  text-decoration: none;
}

.logo-text {
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  letter-spacing: -0.02em;
}

.logo-main {
  color: var(--color-text-primary);
}

.logo-accent {
  color: var(--color-primary);
}

.navbar-nav {
  display: flex;
  align-items: center;
  gap: var(--space-6);
}

.nav-link {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-3) var(--space-4);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-secondary);
  text-decoration: none;
  border-radius: var(--radius-lg);
  transition: all var(--transition-fast);
}

.nav-link:hover {
  background: var(--color-bg-hover);
  color: var(--color-text-primary);
}

.nav-link.active {
  background: var(--color-primary);
  color: white;
}

.navbar-actions {
  display: flex;
  align-items: center;
  gap: var(--space-4);
}

.user-menu {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-2) var(--space-3);
  border-radius: var(--radius-lg);
  background: var(--color-bg-tertiary);
  text-decoration: none;
  transition: all var(--transition-fast);
}

.user-menu:hover {
  background: var(--color-bg-hover);
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

.user-name {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-primary);
  white-space: nowrap;
}

.action-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: var(--radius-lg);
  background: var(--color-bg-tertiary);
  color: var(--color-text-secondary);
  text-decoration: none;
  transition: all var(--transition-fast);
}

.action-btn:hover {
  background: var(--color-bg-hover);
  color: var(--color-primary);
}

.write-btn {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-4);
  border-radius: var(--radius-lg);
  background: var(--color-primary);
  color: white;
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  text-decoration: none;
  transition: all var(--transition-fast);
}

.write-btn:hover {
  background: #0052d4;
  color: white;
}

/* Responsive */
@media (max-width: 768px) {
  .navbar-content {
    gap: var(--space-4);
  }
  
  .navbar-nav {
    gap: var(--space-2);
  }
  
  .nav-link {
    padding: var(--space-2) var(--space-3);
    font-size: var(--text-xs);
  }
  
  .nav-link span {
    display: none;
  }
  
  .user-name {
    display: none;
  }
}
</style>