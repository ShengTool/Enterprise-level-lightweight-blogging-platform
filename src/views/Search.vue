<template>
  <div class="search-page">
    <div class="container-app">
      <!-- 搜索头部 -->
      <header class="search-header">
        <h1 class="search-title">
          <span v-if="searchQuery">「{{ searchQuery }}」的搜索结果</span>
          <span v-else>搜索文章</span>
        </h1>
        <p class="search-stats" v-if="searchQuery">
          找到 {{ filteredArticles.length }} 篇相关文章
        </p>
        
        <!-- 搜索框 -->
        <div class="search-box">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="11" cy="11" r="8"/>
            <path d="m21 21-4.35-4.35"/>
          </svg>
          <input 
            v-model="searchInput" 
            type="text" 
            class="search-input"
            placeholder="搜索文章标题、内容..."
            @keyup.enter="handleSearch"
          />
          <button class="search-btn" @click="handleSearch">搜索</button>
        </div>
        
        <!-- 热门标签 -->
        <div v-if="!searchQuery" class="hot-tags">
          <span class="hot-label">热门标签：</span>
          <router-link 
            v-for="tag in (tagStore.tags || []).slice(0, 5)" 
            :key="tag.id"
            :to="`/tag/${tag.name}`"
            class="hot-tag"
            :style="{ color: tag.color, backgroundColor: tag.color + '15' }"
          >
            {{ tag.name }}
          </router-link>
        </div>
      </header>
      
      <!-- 搜索结果 -->
      <div v-if="searchQuery" class="search-results">
        <div v-if="articleStore.loading" class="results-skeleton">
          <div v-for="i in 3" :key="i" class="result-skeleton">
            <div class="skeleton-title"></div>
            <div class="skeleton-summary"></div>
            <div class="skeleton-meta"></div>
          </div>
        </div>
        
        <div v-else-if="filteredArticles.length > 0" class="results-list">
          <article 
            v-for="article in filteredArticles" 
            :key="article.id"
            class="result-item"
          >
            <router-link :to="`/article/${article.id}`" class="result-link">
              <h2 class="result-title" v-html="highlightText(article.title)"></h2>
              <p class="result-summary" v-html="highlightText(article.summary)"></p>
              <div class="result-meta">
                <span class="meta-item">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                    <circle cx="12" cy="7" r="4"/>
                  </svg>
                  {{ article.user?.username || '匿名用户' }}
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
                <span class="meta-item">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                    <circle cx="12" cy="12" r="3"/>
                  </svg>
                  {{ article.view_count }} 阅读
                </span>
                <div class="result-tags">
                  <span 
                    v-for="tag in article.tags" 
                    :key="tag.id"
                    class="result-tag"
                    :style="{ backgroundColor: tag.color + '20', color: tag.color }"
                  >
                    {{ tag.name }}
                  </span>
                </div>
              </div>
            </router-link>
          </article>
        </div>
        
        <div v-else class="empty-results">
          <div class="empty-icon">
            <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <circle cx="11" cy="11" r="8"/>
              <path d="m21 21-4.35-4.35"/>
            </svg>
          </div>
          <h3>未找到相关文章</h3>
          <p>换个关键词试试，或浏览全部文章</p>
          <router-link to="/" class="btn btn-primary">浏览全部文章</router-link>
        </div>
      </div>
      
      <!-- 默认展示 -->
      <div v-else class="search-default">
        <div class="recent-articles">
          <h2 class="section-title">最新文章</h2>
          <div class="articles-grid">
            <article 
              v-for="article in recentArticles" 
              :key="article.id"
              class="article-card"
            >
              <router-link :to="`/article/${article.id}`" class="card-link">
                <h3 class="card-title">{{ article.title }}</h3>
                <p class="card-summary">{{ article.summary }}</p>
                <div class="card-meta">
                  <span>{{ formatDate(article.created_at) }}</span>
                  <span>{{ article.view_count }} 阅读</span>
                </div>
              </router-link>
            </article>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useArticleStore } from '../stores/article'
import { useTagStore } from '../stores/tag'

const route = useRoute()
const router = useRouter()
const articleStore = useArticleStore()
const tagStore = useTagStore()

const searchInput = ref('')
const searchQuery = computed(() => route.query.q as string || '')

const filteredArticles = computed(() => {
  if (!searchQuery.value) return []
  const query = searchQuery.value.toLowerCase()
  return articleStore.articles.filter(article =>
    article.title.toLowerCase().includes(query) ||
    article.summary.toLowerCase().includes(query) ||
    article.content?.toLowerCase().includes(query)
  )
})

const recentArticles = computed(() => {
  return (articleStore.articles || []).slice(0, 6)
})

const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}

const highlightText = (text: string) => {
  if (!searchQuery.value || !text) return text
  const regex = new RegExp(`(${searchQuery.value})`, 'gi')
  return text.replace(regex, '<mark>$1</mark>')
}

const handleSearch = () => {
  if (searchInput.value.trim()) {
    router.push({ path: '/search', query: { q: searchInput.value.trim() } })
  }
}

watch(() => route.query.q, (newQuery) => {
  searchInput.value = newQuery as string || ''
  if (newQuery) {
    articleStore.getArticles(1, 100, '', newQuery as string)
  }
}, { immediate: true })

onMounted(() => {
  tagStore.getTags()
  if (!searchQuery.value) {
    articleStore.getArticles(1, 6)
  }
})
</script>

<style scoped>
.search-page {
  padding: var(--space-8) 0 var(--space-16);
  min-height: calc(100vh - 64px);
}

/* Search Header */
.search-header {
  text-align: center;
  max-width: 700px;
  margin: 0 auto var(--space-12);
}

.search-title {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-3);
}

.search-stats {
  font-size: var(--text-base);
  color: var(--color-text-tertiary);
  margin-bottom: var(--space-6);
}

.search-box {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-2);
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  margin-bottom: var(--space-6);
}

.search-box svg {
  margin-left: var(--space-3);
  color: var(--color-text-tertiary);
}

.search-input {
  flex: 1;
  padding: var(--space-3);
  font-size: var(--text-lg);
  color: var(--color-text-primary);
  background: none;
  border: none;
  outline: none;
}

.search-input::placeholder {
  color: var(--color-text-placeholder);
}

.search-btn {
  padding: var(--space-3) var(--space-6);
  font-size: var(--text-base);
  font-weight: var(--font-medium);
  color: white;
  background: var(--color-primary);
  border: none;
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.search-btn:hover {
  background: var(--color-primary-dark);
}

/* Hot Tags */
.hot-tags {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-2);
  flex-wrap: wrap;
}

.hot-label {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

.hot-tag {
  padding: var(--space-1) var(--space-3);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  border-radius: var(--radius-full);
  text-decoration: none;
  transition: all var(--transition-fast);
}

.hot-tag:hover {
  opacity: 0.8;
}

/* Search Results */
.search-results {
  max-width: 800px;
  margin: 0 auto;
}

.results-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.result-item {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  transition: all var(--transition-fast);
}

.result-item:hover {
  border-color: var(--color-primary);
  box-shadow: var(--shadow-lg);
}

.result-link {
  display: block;
  padding: var(--space-6);
  text-decoration: none;
}

.result-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-3);
}

.result-title :deep(mark) {
  background: rgba(0, 102, 255, 0.2);
  color: var(--color-primary);
  padding: 0 2px;
  border-radius: 2px;
}

.result-summary {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  line-height: 1.6;
  margin-bottom: var(--space-4);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.result-summary :deep(mark) {
  background: rgba(0, 102, 255, 0.2);
  color: var(--color-primary);
  padding: 0 2px;
  border-radius: 2px;
}

.result-meta {
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

.result-tags {
  display: flex;
  gap: var(--space-1);
  margin-left: auto;
}

.result-tag {
  padding: 2px 8px;
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
  border-radius: var(--radius-full);
}

/* Empty Results */
.empty-results {
  text-align: center;
  padding: var(--space-12);
}

.empty-results .empty-icon {
  color: var(--color-text-tertiary);
  opacity: 0.5;
  margin-bottom: var(--space-4);
}

.empty-results h3 {
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-2);
}

.empty-results p {
  font-size: var(--text-base);
  color: var(--color-text-tertiary);
  margin-bottom: var(--space-6);
}

/* Default View */
.search-default {
  margin-top: var(--space-8);
}

.section-title {
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-6);
}

.articles-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: var(--space-4);
}

.article-card {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  transition: all var(--transition-fast);
}

.article-card:hover {
  border-color: var(--color-primary);
  box-shadow: var(--shadow-md);
}

.card-link {
  display: block;
  padding: var(--space-5);
  text-decoration: none;
}

.card-title {
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-2);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-summary {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
  margin-bottom: var(--space-4);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-meta {
  display: flex;
  justify-content: space-between;
  font-size: var(--text-xs);
  color: var(--color-text-tertiary);
}

/* Skeleton */
.results-skeleton {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.result-skeleton {
  padding: var(--space-6);
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
}

.skeleton-title {
  width: 60%;
  height: 24px;
  background: var(--color-bg-tertiary);
  border-radius: var(--radius-md);
  margin-bottom: var(--space-3);
}

.skeleton-summary {
  width: 100%;
  height: 40px;
  background: var(--color-bg-hover);
  border-radius: var(--radius-md);
  margin-bottom: var(--space-4);
}

.skeleton-meta {
  width: 40%;
  height: 16px;
  background: var(--color-bg-tertiary);
  border-radius: var(--radius-md);
}
</style>
