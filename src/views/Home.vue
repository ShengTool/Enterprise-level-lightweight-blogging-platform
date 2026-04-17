<template>
  <div class="home-page">
    <!-- Hero Section -->
    <section class="hero">
      <div class="hero-container">
        <div class="hero-content">
          <div class="hero-badge">
            <span class="badge-dot"></span>
            <span>企业级轻量博客平台</span>
          </div>
          <h1 class="hero-title">
            用文字记录
            <span class="gradient-text">精彩故事</span>
          </h1>
          <p class="hero-description">
            一个优雅、简洁、高效的博客平台，专注于内容创作与分享。
            支持 Markdown 语法，让写作更简单。
          </p>
          <div class="hero-actions">
            <router-link to="/register" class="btn btn-primary btn-lg">
              开始写作
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M5 12h14M12 5l7 7-7 7"/>
              </svg>
            </router-link>
            <router-link to="/about" class="btn btn-secondary btn-lg">
              了解更多
            </router-link>
          </div>
        </div>

        <!-- Hero 右侧装饰 -->
        <div class="hero-visual">
          <div class="hero-card-stack">
            <div class="hero-card hero-card-1">
              <div class="hero-card-header">
                <div class="hero-card-avatar"></div>
                <div class="hero-card-info">
                  <div class="hero-card-name"></div>
                  <div class="hero-card-date"></div>
                </div>
              </div>
              <div class="hero-card-content">
                <div class="hero-card-title"></div>
                <div class="hero-card-text"></div>
                <div class="hero-card-text short"></div>
              </div>
              <div class="hero-card-tags">
                <div class="hero-card-tag"></div>
                <div class="hero-card-tag"></div>
              </div>
            </div>
            <div class="hero-card hero-card-2">
              <div class="hero-card-header">
                <div class="hero-card-avatar"></div>
                <div class="hero-card-info">
                  <div class="hero-card-name"></div>
                  <div class="hero-card-date"></div>
                </div>
              </div>
              <div class="hero-card-content">
                <div class="hero-card-title"></div>
                <div class="hero-card-text"></div>
                <div class="hero-card-text short"></div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Hero 背景装饰 -->
      <div class="hero-bg">
        <div class="hero-grid"></div>
        <div class="hero-glow"></div>
      </div>
    </section>

    <!-- 标签筛选 -->
    <section class="tags-section">
      <div class="container-app">
        <div class="tags-wrapper">
          <button 
            class="tag-btn"
            :class="{ active: !selectedTag }"
            @click="selectTag('')"
          >
            全部
          </button>
          <template v-if="Array.isArray(tagStore.tags) && tagStore.tags.length > 0">
            <button 
              v-for="tag in tagStore.tags"
              :key="tag.id"
              class="tag-btn"
              :class="{ active: selectedTag === tag.name }"
              :style="selectedTag === tag.name ? {
                backgroundColor: tag.color + '20',
                color: tag.color,
                borderColor: tag.color
              } : {}"
              @click="selectTag(tag.name)"
            >
              <span class="tag-indicator" :style="{ backgroundColor: tag.color }"></span>
              {{ tag.name }}
              <span class="tag-count">{{ tag.count }}</span>
            </button>
          </template>
        </div>
      </div>
    </section>

    <!-- 文章列表 -->
    <section class="articles-section">
      <div class="container-app">
        <!-- 加载状态 -->
        <div v-if="articleStore.loading" class="articles-grid">
          <div v-for="i in 6" :key="i" class="article-card-skeleton">
            <div class="skeleton-image"></div>
            <div class="skeleton-content">
              <div class="skeleton-title"></div>
              <div class="skeleton-text"></div>
              <div class="skeleton-text short"></div>
              <div class="skeleton-footer">
                <div class="skeleton-avatar"></div>
                <div class="skeleton-meta"></div>
              </div>
            </div>
          </div>
        </div>

        <!-- 文章列表 -->
        <div v-else-if="Array.isArray(articleStore.articles) && articleStore.articles.length > 0" class="articles-grid">
          <article
            v-for="(article, index) in articleStore.articles"
            :key="article.id"
            class="article-card"
            :style="{ animationDelay: `${index * 50}ms` }"
          >
            <router-link :to="`/article/${article.id}`" class="article-card-link">
              <!-- 文章封面图 -->
              <div class="article-cover">
                <div class="article-cover-placeholder">
                  <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                    <path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"/>
                    <polyline points="14 2 14 8 20 8"/>
                    <line x1="16" y1="13" x2="8" y2="13"/>
                    <line x1="16" y1="17" x2="8" y2="17"/>
                    <line x1="10" y1="9" x2="8" y2="9"/>
                  </svg>
                </div>
                <div class="article-cover-overlay"></div>
              </div>

              <!-- 文章内容 -->
              <div class="article-content">
                <!-- 标签 -->
                <div class="article-tags">
                  <span
                    v-for="tag in article.tags?.slice(0, 2)"
                    :key="tag.id"
                    class="article-tag"
                    :style="{ color: tag.color, backgroundColor: tag.color + '15' }"
                  >
                    {{ tag.name }}
                  </span>
                </div>

                <!-- 标题 -->
                <h2 class="article-title">{{ article.title }}</h2>

                <!-- 摘要 -->
                <p class="article-summary">{{ article.summary }}</p>

                <!-- 元信息 -->
                <div class="article-meta">
                  <div class="article-author">
                    <img
                      :src="article.user?.avatar || defaultAvatar"
                      :alt="article.user?.username"
                      class="author-avatar"
                      loading="lazy"
                    />
                    <span class="author-name">{{ article.user?.username || '匿名用户' }}</span>
                  </div>
                  <div class="article-stats">
                    <span class="stat-item">
                      <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                        <circle cx="12" cy="12" r="3"/>
                      </svg>
                      {{ article.viewCount }}
                    </span>
                    <span class="stat-item">
                      <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
                        <line x1="16" y1="2" x2="16" y2="6"/>
                        <line x1="8" y1="2" x2="8" y2="6"/>
                        <line x1="3" y1="10" x2="21" y2="10"/>
                      </svg>
                      {{ formatDate(article.createdAt) }}
                    </span>
                  </div>
                </div>
              </div>
            </router-link>
          </article>
        </div>

        <!-- 空状态 -->
        <div v-else class="empty-state">
          <div class="empty-icon">
            <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"/>
              <polyline points="14 2 14 8 20 8"/>
            </svg>
          </div>
          <h3 class="empty-title">暂无文章</h3>
          <p class="empty-description">还没有任何文章发布，敬请期待！</p>
        </div>

        <!-- 分页 -->
        <div v-if="(articleStore.total || 0) > pageSize" class="pagination-wrapper">
          <div class="pagination">
            <button
              class="pagination-btn"
              :disabled="currentPage === 1"
              @click="changePage(currentPage - 1)"
            >
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="15 18 9 12 15 6"/>
              </svg>
            </button>

            <div class="pagination-pages">
              <button
                v-for="page in (displayedPages || [])"
                :key="page"
                class="pagination-page"
                :class="{ active: page === currentPage, ellipsis: page === '...' }"
                :disabled="page === '...'"
                @click="page !== '...' && changePage(page as number)"
              >
                {{ page }}
              </button>
            </div>

            <button
              class="pagination-btn"
              :disabled="currentPage >= totalPages"
              @click="changePage(currentPage + 1)"
            >
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="9 18 15 12 9 6"/>
              </svg>
            </button>
          </div>

          <div class="pagination-info">
            共 {{ articleStore.total }} 篇文章
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useArticleStore } from '../stores/article'
import { useTagStore } from '../stores/tag'

const route = useRoute()
// const router = useRouter()
const articleStore = useArticleStore()
const tagStore = useTagStore()

const currentPage = ref(1)
const pageSize = ref(12)
const selectedTag = ref('')

const defaultAvatar = 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA0MCA0MCI+PHJlY3Qgd2lkdGg9IjQwIiBoZWlnaHQ9IjQwIiByeD0iMjAiIGZpbGw9IiNFOEU4RTgiLz48Y2lyY2xlIGN4PSIyMCIgY3k9IjE2IiByPSI4IiBmaWxsPSIjQThBOEE4Ii8+PHBhdGggZD0iTTYgMzVjMC04LjI4NCA2LjcxNi0xNSAxNS0xNXMxNSA2LjcxNiAxNSAxNSIgZmlsbD0iI0E4QThBOCIvPjwvc3ZnPg=='

const totalPages = computed(() => Math.ceil((articleStore.total || 0) / pageSize.value))

const displayedPages = computed(() => {
  const pages: (number | string)[] = []
  const total = totalPages.value
  const current = currentPage.value

  if (total <= 7) {
    for (let i = 1; i <= total; i++) pages.push(i)
  } else {
    if (current <= 4) {
      for (let i = 1; i <= 5; i++) pages.push(i)
      pages.push('...')
      pages.push(total)
    } else if (current >= total - 3) {
      pages.push(1)
      pages.push('...')
      for (let i = total - 4; i <= total; i++) pages.push(i)
    } else {
      pages.push(1)
      pages.push('...')
      for (let i = current - 1; i <= current + 1; i++) pages.push(i)
      pages.push('...')
      pages.push(total)
    }
  }

  return pages
})

const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}

const selectTag = (tag: string) => {
  selectedTag.value = tag
  currentPage.value = 1
  loadArticles()
}

const changePage = (page: number) => {
  currentPage.value = page
  loadArticles()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const loadArticles = async () => {
  try {
    await articleStore.getArticles(currentPage.value, pageSize.value, selectedTag.value, '')
  } catch (error) {
    console.warn('Failed to load articles:', error)
  }
}

// 监听路由查询参数
watch(() => route.query.search, (search) => {
  if (search) {
    loadArticles()
  }
}, { immediate: true })

onMounted(async () => {
  try {
    await Promise.all([
      loadArticles(),
      tagStore.getTags().catch(e => console.warn('Failed to load tags:', e))
    ])
  } catch (error) {
    console.warn('Failed to load initial data:', error)
  }
})
</script>

<style scoped>
.home-page {
  min-height: calc(100vh - 64px);
}

/* ============================================
   HERO SECTION
   ============================================ */

.hero {
  position: relative;
  padding: var(--space-16) 0 var(--space-20);
  overflow: hidden;
}

.hero-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 var(--space-6);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: var(--space-12);
}

.hero-content {
  max-width: 560px;
  position: relative;
  z-index: 1;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-4);
  background: var(--color-bg-tertiary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-full);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-secondary);
  margin-bottom: var(--space-6);
}

.badge-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--color-success);
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.hero-title {
  font-size: clamp(2.5rem, 5vw, 3.5rem);
  font-weight: var(--font-bold);
  line-height: 1.1;
  letter-spacing: -0.03em;
  color: var(--color-text-primary);
  margin-bottom: var(--space-6);
}

.hero-description {
  font-size: var(--text-lg);
  line-height: var(--leading-relaxed);
  color: var(--color-text-secondary);
  margin-bottom: var(--space-8);
}

.hero-actions {
  display: flex;
  gap: var(--space-4);
}

/* Hero Visual */
.hero-visual {
  display: none;
  position: relative;
  z-index: 1;
}

@media (min-width: 1024px) {
  .hero-visual {
    display: block;
  }
}

.hero-card-stack {
  position: relative;
  width: 400px;
  height: 320px;
}

.hero-card {
  position: absolute;
  width: 320px;
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  padding: var(--space-5);
  box-shadow: var(--shadow-lg);
}

.hero-card-1 {
  top: 0;
  left: 0;
  z-index: 2;
  animation: float 6s ease-in-out infinite;
}

.hero-card-2 {
  top: 40px;
  left: 80px;
  z-index: 1;
  opacity: 0.8;
  animation: float 6s ease-in-out infinite 0.5s;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.hero-card-header {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  margin-bottom: var(--space-4);
}

.hero-card-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--color-primary) 0%, #8B5CF6 100%);
}

.hero-card-info {
  flex: 1;
}

.hero-card-name {
  width: 80px;
  height: 14px;
  background: var(--color-bg-tertiary);
  border-radius: var(--radius-sm);
  margin-bottom: var(--space-1);
}

.hero-card-date {
  width: 50px;
  height: 10px;
  background: var(--color-bg-hover);
  border-radius: var(--radius-sm);
}

.hero-card-content {
  margin-bottom: var(--space-4);
}

.hero-card-title {
  width: 90%;
  height: 16px;
  background: var(--color-bg-tertiary);
  border-radius: var(--radius-sm);
  margin-bottom: var(--space-3);
}

.hero-card-text {
  width: 100%;
  height: 10px;
  background: var(--color-bg-hover);
  border-radius: var(--radius-sm);
  margin-bottom: var(--space-2);
}

.hero-card-text.short {
  width: 60%;
}

.hero-card-tags {
  display: flex;
  gap: var(--space-2);
}

.hero-card-tag {
  width: 50px;
  height: 20px;
  background: rgba(0, 102, 255, 0.1);
  border-radius: var(--radius-full);
}

/* Hero Background */
.hero-bg {
  position: absolute;
  inset: 0;
  overflow: hidden;
  pointer-events: none;
}

.hero-grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(var(--color-border-secondary) 1px, transparent 1px),
    linear-gradient(90deg, var(--color-border-secondary) 1px, transparent 1px);
  background-size: 60px 60px;
  opacity: 0.5;
  mask-image: radial-gradient(ellipse 80% 50% at 50% 0%, black 70%, transparent 100%);
}

.hero-glow {
  position: absolute;
  top: -50%;
  left: 50%;
  transform: translateX(-50%);
  width: 800px;
  height: 600px;
  background: radial-gradient(circle, rgba(0, 102, 255, 0.15) 0%, transparent 70%);
  filter: blur(60px);
}

/* ============================================
   TAGS SECTION
   ============================================ */

.tags-section {
  padding: var(--space-6) 0;
  border-bottom: 1px solid var(--color-border-secondary);
  background: var(--color-bg-primary);
  position: sticky;
  top: 64px;
  z-index: 50;
}

.tags-wrapper {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
}

.tag-btn {
  display: inline-flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-4);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-secondary);
  background: var(--color-bg-tertiary);
  border: 1px solid transparent;
  border-radius: var(--radius-full);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.tag-btn:hover {
  background: var(--color-bg-hover);
  color: var(--color-text-primary);
}

.tag-btn.active {
  background: var(--color-primary);
  color: white;
}

.tag-indicator {
  width: 6px;
  height: 6px;
  border-radius: 50%;
}

.tag-count {
  padding: var(--space-1) var(--space-2);
  font-size: var(--text-xs);
  background: var(--color-bg-primary);
  border-radius: var(--radius-md);
}

/* ============================================
   ARTICLES SECTION
   ============================================ */

.articles-section {
  padding: var(--space-12) 0;
}

.articles-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: var(--space-6);
}

@media (min-width: 640px) {
  .articles-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 1024px) {
  .articles-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

/* Article Card */
.article-card {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  overflow: hidden;
  transition: all var(--transition-base);
  animation: fadeInUp 0.5s ease-out forwards;
  opacity: 0;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.article-card:hover {
  border-color: var(--color-primary);
  box-shadow: var(--shadow-xl);
  transform: translateY(-4px);
}

.article-card-link {
  display: block;
  text-decoration: none;
}

.article-cover {
  position: relative;
  height: 160px;
  background: linear-gradient(135deg, var(--color-bg-tertiary) 0%, var(--color-bg-hover) 100%);
  overflow: hidden;
}

.article-cover-placeholder {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-text-tertiary);
  opacity: 0.5;
}

.article-cover-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, var(--color-bg-primary) 0%, transparent 100%);
  opacity: 0;
  transition: opacity var(--transition-base);
}

.article-card:hover .article-cover-overlay {
  opacity: 0.3;
}

.article-content {
  padding: var(--space-5);
}

.article-tags {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
  margin-bottom: var(--space-3);
}

.article-tag {
  padding: var(--space-1) var(--space-3);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
  border-radius: var(--radius-full);
}

.article-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  line-height: var(--leading-snug);
  margin-bottom: var(--space-3);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  transition: color var(--transition-fast);
}

.article-card:hover .article-title {
  color: var(--color-primary);
}

.article-summary {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
  line-height: var(--leading-relaxed);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: var(--space-4);
}

.article-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: var(--space-4);
  border-top: 1px solid var(--color-border-secondary);
}

.article-author {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.author-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  object-fit: cover;
}

.author-name {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-secondary);
}

.article-stats {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.stat-item {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  font-size: var(--text-xs);
  color: var(--color-text-tertiary);
}

/* Skeleton */
.article-card-skeleton {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  overflow: hidden;
}

.skeleton-image {
  height: 160px;
  background: linear-gradient(90deg, var(--color-bg-tertiary) 25%, var(--color-bg-hover) 50%, var(--color-bg-tertiary) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.skeleton-content {
  padding: var(--space-5);
}

.skeleton-title {
  width: 80%;
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

.skeleton-text.short {
  width: 60%;
}

.skeleton-footer {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  margin-top: var(--space-4);
  padding-top: var(--space-4);
  border-top: 1px solid var(--color-border-secondary);
}

.skeleton-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: var(--color-bg-tertiary);
}

.skeleton-meta {
  width: 80px;
  height: 14px;
  background: var(--color-bg-hover);
  border-radius: var(--radius-md);
}

@keyframes shimmer {
  0% { background-position: -200% 0; }
  100% { background-position: 200% 0; }
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: var(--space-16) 0;
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
}

/* Pagination */
.pagination-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-4);
  margin-top: var(--space-12);
  padding-top: var(--space-8);
  border-top: 1px solid var(--color-border-secondary);
}

.pagination {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.pagination-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-lg);
  cursor: pointer;
  color: var(--color-text-secondary);
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

.pagination-pages {
  display: flex;
  align-items: center;
  gap: var(--space-1);
}

.pagination-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 36px;
  height: 36px;
  padding: 0 var(--space-3);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  background: transparent;
  border: 1px solid transparent;
  border-radius: var(--radius-lg);
  cursor: pointer;
  color: var(--color-text-secondary);
  transition: all var(--transition-fast);
}

.pagination-page:hover:not(:disabled):not(.active) {
  background: var(--color-bg-hover);
}

.pagination-page.active {
  background: var(--color-primary);
  border-color: var(--color-primary);
  color: white;
}

.pagination-page.ellipsis {
  cursor: default;
}

.pagination-info {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}
</style>
