<template>
  <div class="user-page">
    <div class="container-app">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-state">
        <div class="skeleton-header">
          <div class="skeleton-avatar"></div>
          <div class="skeleton-info">
            <div class="skeleton-name"></div>
            <div class="skeleton-bio"></div>
          </div>
        </div>
      </div>
      
      <!-- 用户不存在 -->
      <div v-else-if="!user" class="empty-state">
        <div class="empty-icon">
          <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
            <circle cx="12" cy="7" r="4"/>
          </svg>
        </div>
        <h3>用户不存在</h3>
        <p>该用户可能已被删除或链接有误</p>
        <router-link to="/" class="btn btn-primary">返回首页</router-link>
      </div>
      
      <!-- 用户资料 -->
      <div v-else class="user-profile">
        <!-- 用户信息卡片 -->
        <div class="profile-card">
          <div class="profile-header">
            <img :src="user.avatar || defaultAvatar" :alt="user.username" class="profile-avatar" loading="lazy" />
            <div class="profile-info">
              <h1 class="profile-name">{{ user.username }}</h1>
              <p class="profile-bio">{{ user.bio || '这个人很懒，还没有写简介' }}</p>
              <div class="profile-stats">
                <div class="stat-item">
                  <span class="stat-value">{{ userArticles.length }}</span>
                  <span class="stat-label">文章</span>
                </div>
                <div class="stat-item">
                  <span class="stat-value">{{ totalViews }}</span>
                  <span class="stat-label">阅读</span>
                </div>
                <div class="stat-item">
                  <span class="stat-value">{{ formatDate(user.created_at) }}</span>
                  <span class="stat-label">加入时间</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 文章列表 -->
        <div class="articles-section">
          <h2 class="section-title">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"/>
              <polyline points="14 2 14 8 20 8"/>
            </svg>
            发布的文章
          </h2>
          
          <div v-if="userArticles.length > 0" class="articles-list">
            <article 
              v-for="article in userArticles" 
              :key="article.id"
              class="article-item"
            >
              <router-link :to="`/article/${article.id}`" class="article-link">
                <h3 class="article-title">{{ article.title }}</h3>
                <p class="article-summary">{{ article.summary }}</p>
                <div class="article-meta">
                  <span class="meta-item">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                      <circle cx="12" cy="12" r="3"/>
                    </svg>
                    {{ article.view_count }} 阅读
                  </span>
                  <span class="meta-item">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
                      <line x1="16" y1="2" x2="16" y2="6"/>
                      <line x1="8" y1="2" x2="8" y2="6"/>
                      <line x1="3" y1="10" x2="21" y2="10"/>
                    </svg>
                    {{ formatDate(article.created_at) }}
                  </span>
                  <div class="article-tags">
                    <span 
                      v-for="tag in article.tags" 
                      :key="tag.id"
                      class="tag-badge"
                      :style="{ backgroundColor: tag.color + '20', color: tag.color }"
                    >
                      {{ tag.name }}
                    </span>
                  </div>
                </div>
              </router-link>
            </article>
          </div>
          
          <div v-else class="empty-articles">
            <p>该用户还没有发布任何文章</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'

// const route = useRoute()
const loading = ref(true)
const user = ref<any>(null)

const defaultAvatar = 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA0MCA0MCI+PHJlY3Qgd2lkdGg9IjQwIiBoZWlnaHQ9IjQwIiByeD0iMjAiIGZpbGw9IiNFOEU4RTgiLz48Y2lyY2xlIGN4PSIyMCIgY3k9IjE2IiByPSI4IiBmaWxsPSIjQThBOEE4Ii8+PHBhdGggZD0iTTYgMzVjMC04LjI4NCA2LjcxNi0xNSAxNS0xNXMxNSA2LjcxNiAxNSAxNSIgZmlsbD0iI0E4QThBOCIvPjwvc3ZnPg=='

// 模拟用户数据
const mockUser = {
  id: 1,
  username: '示例用户',
  bio: '热爱技术，喜欢分享',
  avatar: '',
  created_at: '2024-01-01T00:00:00Z'
}

// 模拟文章数据
const userArticles = ref([
  {
    id: 1,
    title: 'Vue 3 组合式 API 最佳实践',
    summary: '本文介绍了 Vue 3 组合式 API 的使用技巧和最佳实践，帮助你写出更优雅的代码。',
    view_count: 1234,
    created_at: '2024-01-15T10:00:00Z',
    tags: [
      { id: 1, name: 'Vue', color: '#42b883' },
      { id: 2, name: '前端', color: '#0066FF' }
    ]
  },
  {
    id: 2,
    title: 'TypeScript 高级类型详解',
    summary: '深入理解 TypeScript 的高级类型系统，包括泛型、条件类型、映射类型等。',
    view_count: 892,
    created_at: '2024-01-10T14:30:00Z',
    tags: [
      { id: 3, name: 'TypeScript', color: '#3178c6' }
    ]
  },
  {
    id: 3,
    title: '构建高性能的 Web 应用',
    summary: '从多个维度探讨如何构建高性能的 Web 应用，包括加载优化、渲染优化等。',
    view_count: 2156,
    created_at: '2024-01-05T09:00:00Z',
    tags: [
      { id: 2, name: '前端', color: '#0066FF' },
      { id: 4, name: '性能优化', color: '#F59E0B' }
    ]
  }
])

const totalViews = computed(() => {
  return userArticles.value.reduce((sum, a) => sum + a.view_count, 0)
})

const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}

onMounted(() => {
  // 模拟加载
  setTimeout(() => {
    // const userId = Number(route.params.id)
    // 这里应该根据 userId 获取用户数据
    user.value = mockUser
    loading.value = false
  }, 500)
})
</script>

<style scoped>
.user-page {
  padding: var(--space-8) 0 var(--space-16);
  min-height: calc(100vh - 64px);
}

/* Loading State */
.loading-state {
  padding: var(--space-8) 0;
}

.skeleton-header {
  display: flex;
  gap: var(--space-6);
  padding: var(--space-8);
  background: var(--color-bg-primary);
  border-radius: var(--radius-xl);
}

.skeleton-avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: var(--color-bg-tertiary);
}

.skeleton-info {
  flex: 1;
}

.skeleton-name {
  width: 200px;
  height: 32px;
  background: var(--color-bg-tertiary);
  border-radius: var(--radius-md);
  margin-bottom: var(--space-3);
}

.skeleton-bio {
  width: 300px;
  height: 20px;
  background: var(--color-bg-hover);
  border-radius: var(--radius-md);
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: var(--space-16) 0;
}

.empty-state .empty-icon {
  color: var(--color-text-tertiary);
  opacity: 0.5;
  margin-bottom: var(--space-4);
}

.empty-state h3 {
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-2);
}

.empty-state p {
  font-size: var(--text-base);
  color: var(--color-text-tertiary);
  margin-bottom: var(--space-6);
}

/* User Profile */
.user-profile {
  max-width: 800px;
  margin: 0 auto;
}

.profile-card {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  padding: var(--space-8);
  margin-bottom: var(--space-8);
}

.profile-header {
  display: flex;
  gap: var(--space-6);
}

.profile-avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid var(--color-bg-secondary);
}

.profile-info {
  flex: 1;
}

.profile-name {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-2);
}

.profile-bio {
  font-size: var(--text-base);
  color: var(--color-text-secondary);
  margin-bottom: var(--space-6);
}

.profile-stats {
  display: flex;
  gap: var(--space-8);
}

.stat-item {
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
}

.stat-value {
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
}

.stat-label {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

/* Articles Section */
.articles-section {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  padding: var(--space-6);
}

.section-title {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-6);
}

.articles-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.article-item {
  padding: var(--space-5);
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border-secondary);
  border-radius: var(--radius-lg);
  transition: all var(--transition-fast);
}

.article-item:hover {
  border-color: var(--color-border-primary);
  box-shadow: var(--shadow-md);
}

.article-link {
  display: block;
  text-decoration: none;
}

.article-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-2);
  transition: color var(--transition-fast);
}

.article-item:hover .article-title {
  color: var(--color-primary);
}

.article-summary {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
  line-height: 1.6;
  margin-bottom: var(--space-4);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-meta {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: var(--space-4);
}

.meta-item {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

.article-tags {
  display: flex;
  gap: var(--space-1);
  margin-left: auto;
}

.tag-badge {
  padding: 2px 8px;
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
  border-radius: var(--radius-full);
}

.empty-articles {
  text-align: center;
  padding: var(--space-8);
  color: var(--color-text-tertiary);
}

/* Responsive */
@media (max-width: 640px) {
  .profile-header {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .profile-stats {
    justify-content: center;
  }
  
  .article-meta {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .article-tags {
    margin-left: 0;
  }
}
</style>
