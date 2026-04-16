<template>
  <div class="tag-page">
    <div class="container-app">
      <!-- 标签头部 -->
      <header class="tag-header">
        <nav class="breadcrumb">
          <router-link to="/">首页</router-link>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="9 18 15 12 9 6"/>
          </svg>
          <span>标签</span>
        </nav>
        
        <div class="tag-info">
          <div class="tag-icon" :style="{ backgroundColor: tagColor + '20', color: tagColor }">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"/>
              <line x1="7" y1="7" x2="7.01" y2="7"/>
            </svg>
          </div>
          <div>
            <h1 class="tag-title">{{ tagName }}</h1>
            <p class="tag-count">{{ articleStore.total }} 篇文章</p>
          </div>
        </div>
      </header>
      
      <!-- 文章列表 -->
      <div class="articles-section">
        <!-- 加载状态 -->
        <div v-if="articleStore.loading" class="articles-list">
          <div v-for="i in 3" :key="i" class="article-card-skeleton">
            <div class="skeleton-content">
              <div class="skeleton-title"></div>
              <div class="skeleton-text"></div>
              <div class="skeleton-meta"></div>
            </div>
          </div>
        </div>
        
        <!-- 文章列表 -->
        <div v-else-if="articleStore.articles.length > 0" class="articles-list">
          <article
            v-for="(article, index) in articleStore.articles"
            :key="article.id"
            class="article-card"
            :style="{ animationDelay: `${index * 50}ms` }"
          >
            <router-link :to="`/article/${article.id}`" class="article-link">
              <div class="article-content">
                <div class="article-meta">
                  <span class="article-date">{{ formatDate(article.created_at) }}</span>
                  <span class="article-views">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                      <circle cx="12" cy="12" r="3"/>
                    </svg>
                    {{ article.view_count }} 阅读
                  </span>
                </div>
                
                <h2 class="article-title">{{ article.title }}</h2>
                <p class="article-summary">{{ article.summary }}</p>
                
                <div class="article-tags">
                  <router-link
                    v-for="tag in article.tags"
                    :key="tag.id"
                    :to="`/tag/${tag.name}`"
                    class="article-tag"
                    :style="{ color: tag.color, backgroundColor: tag.color + '15' }"
                  >
                    {{ tag.name }}
                  </router-link>
                </div>
              </div>
              
              <div class="article-arrow">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M5 12h14M12 5l7 7-7 7"/>
                </svg>
              </div>
            </router-link>
          </article>
        </div>
        
        <!-- 空状态 -->
        <div v-else class="empty-state">
          <div class="empty-icon">
            <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"/>
              <line x1="7" y1="7" x2="7.01" y2="7"/>
            </svg>
          </div>
          <h3 class="empty-title">暂无文章</h3>
          <p class="empty-description">该标签下还没有任何文章</p>
          <router-link to="/" class="btn btn-primary">返回首页</router-link>
        </div>
        
        <!-- 分页 -->
        <div v-if="articleStore.total > pageSize" class="pagination">
          <button 
            class="pagination-btn"
            :disabled="currentPage === 1"
            @click="changePage(currentPage - 1)"
          >
            上一页
          </button>
          <span class="pagination-info">
            第 {{ currentPage }} 页 / 共 {{ totalPages }} 页
          </span>
          <button 
            class="pagination-btn"
            :disabled="currentPage >= totalPages"
            @click="changePage(currentPage + 1)"
          >
            下一页
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useArticleStore } from '../stores/article'
import { useTagStore } from '../stores/tag'

const route = useRoute()
const articleStore = useArticleStore()
const tagStore = useTagStore()

const currentPage = ref(1)
const pageSize = ref(10)

const tagName = computed(() => route.params.name as string)

const tagColor = computed(() => {
  const tag = tagStore.tags.find(t => t.name === tagName.value)
  return tag?.color || '#0066FF'
})

const totalPages = computed(() => Math.ceil(articleStore.total / pageSize.value))

const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

const changePage = (page: number) => {
  currentPage.value = page
  loadArticles()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const loadArticles = async () => {
  await articleStore.getArticles(currentPage.value, pageSize.value, tagName.value, '')
}

onMounted(async () => {
  await loadArticles()
  if (tagStore.tags.length === 0) {
    await tagStore.getTags()
  }
})
</script>

<style scoped>
.tag-page {
  min-height: calc(100vh - 64px);
  padding: var(--space-8) 0 var(--space-16);
}

/* Tag Header */
.tag-header {
  margin-bottom: var(--space-8);
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
  margin-bottom: var(--space-6);
}

.breadcrumb a {
  color: var(--color-text-secondary);
  text-decoration: none;
  transition: color var(--transition-fast);
}

.breadcrumb a:hover {
  color: var(--color-primary);
}

.tag-info {
  display: flex;
  align-items: center;
  gap: var(--space-4);
}

.tag-icon {
  width: 64px;
  height: 64px;
  border-radius: var(--radius-xl);
  display: flex;
  align-items: center;
  justify-content: center;
}

.tag-title {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-1);
}

.tag-count {
  font-size: var(--text-base);
  color: var(--color-text-tertiary);
}

/* Articles Section */
.articles-section {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  overflow: hidden;
}

.articles-list {
  display: flex;
  flex-direction: column;
}

.article-card {
  border-bottom: 1px solid var(--color-border-secondary);
  animation: fadeInUp 0.4s ease-out forwards;
  opacity: 0;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.article-card:last-child {
  border-bottom: none;
}

.article-link {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--space-6);
  text-decoration: none;
  transition: background var(--transition-fast);
}

.article-link:hover {
  background: var(--color-bg-secondary);
}

.article-content {
  flex: 1;
}

.article-meta {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  margin-bottom: var(--space-3);
}

.article-date {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

.article-views {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

.article-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-2);
  transition: color var(--transition-fast);
}

.article-link:hover .article-title {
  color: var(--color-primary);
}

.article-summary {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
  line-height: var(--leading-relaxed);
  margin-bottom: var(--space-3);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-tags {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
}

.article-tag {
  padding: var(--space-1) var(--space-3);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
  border-radius: var(--radius-full);
  text-decoration: none;
  transition: all var(--transition-fast);
}

.article-tag:hover {
  opacity: 0.8;
}

.article-arrow {
  color: var(--color-text-tertiary);
  transition: all var(--transition-fast);
  flex-shrink: 0;
  margin-left: var(--space-4);
}

.article-link:hover .article-arrow {
  color: var(--color-primary);
  transform: translateX(4px);
}

/* Skeleton */
.article-card-skeleton {
  padding: var(--space-6);
  border-bottom: 1px solid var(--color-border-secondary);
}

.skeleton-title {
  width: 60%;
  height: 20px;
  background: var(--color-bg-tertiary);
  border-radius: var(--radius-md);
  margin-bottom: var(--space-3);
}

.skeleton-text {
  width: 100%;
  height: 14px;
  background: var(--color-bg-hover);
  border-radius: var(--radius-md);
  margin-bottom: var(--space-2);
}

.skeleton-meta {
  width: 30%;
  height: 12px;
  background: var(--color-bg-tertiary);
  border-radius: var(--radius-md);
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: var(--space-16) var(--space-6);
}

.empty-icon {
  color: var(--color-text-tertiary);
  margin-bottom: var(--space-6);
  opacity: 0.5;
}

.empty-title {
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-2);
}

.empty-description {
  font-size: var(--text-base);
  color: var(--color-text-tertiary);
  margin-bottom: var(--space-6);
}

/* Pagination */
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-4);
  padding: var(--space-6);
  border-top: 1px solid var(--color-border-secondary);
}

.pagination-btn {
  padding: var(--space-2) var(--space-4);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-secondary);
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.pagination-btn:hover:not(:disabled) {
  background: var(--color-bg-hover);
  border-color: var(--color-text-tertiary);
}

.pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination-info {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

/* Responsive */
@media (max-width: 640px) {
  .article-link {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .article-arrow {
    display: none;
  }
}
</style>
