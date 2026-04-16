<template>
  <div class="article-detail-page">
    <!-- 阅读进度条 -->
    <div class="reading-progress" :style="{ width: readingProgress + '%' }"></div>
    
    <div class="container-app">
      <!-- 加载状态 -->
      <div v-if="articleStore.loading" class="loading-state">
        <div class="skeleton-hero">
          <div class="skeleton-title"></div>
          <div class="skeleton-meta"></div>
        </div>
        <div class="skeleton-content">
          <div class="skeleton-paragraph"></div>
          <div class="skeleton-paragraph"></div>
          <div class="skeleton-paragraph short"></div>
        </div>
      </div>
      
      <!-- 文章内容 -->
      <article v-else-if="articleStore.currentArticle" class="article-wrapper">
        <!-- 文章头部 -->
        <header class="article-header">
          <!-- 面包屑 -->
          <nav class="breadcrumb">
            <router-link to="/">首页</router-link>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="9 18 15 12 9 6"/>
            </svg>
            <span>文章详情</span>
          </nav>
          
          <!-- 标签 -->
          <div class="article-tags">
            <router-link
              v-for="tag in articleStore.currentArticle.tags"
              :key="tag.id"
              :to="`/tag/${tag.name}`"
              class="article-tag"
              :style="{ color: tag.color, backgroundColor: tag.color + '15' }"
            >
              {{ tag.name }}
            </router-link>
          </div>
          
          <!-- 标题 -->
          <h1 class="article-title">{{ articleStore.currentArticle.title }}</h1>
          
          <!-- 元信息 -->
          <div class="article-meta">
            <div class="author-info">
              <img 
                :src="articleStore.currentArticle.user?.avatar || defaultAvatar"
                :alt="articleStore.currentArticle.user?.username"
                class="author-avatar"
                loading="lazy"
              />
              <div class="author-details">
                <span class="author-name">{{ articleStore.currentArticle.user?.username || '匿名用户' }}</span>
                <div class="article-stats">
                  <span>{{ formatDate(articleStore.currentArticle.created_at) }}</span>
                  <span class="dot">·</span>
                  <span>预计阅读 {{ readingTime }} 分钟</span>
                  <span class="dot">·</span>
                  <span>{{ articleStore.currentArticle.view_count }} 次阅读</span>
                </div>
              </div>
            </div>
            
            <!-- 分享按钮 -->
            <div class="article-actions">
              <button class="action-btn" title="分享" @click="shareArticle">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <circle cx="18" cy="5" r="3"/>
                  <circle cx="6" cy="12" r="3"/>
                  <circle cx="18" cy="19" r="3"/>
                  <line x1="8.59" y1="13.51" x2="15.42" y2="17.49"/>
                  <line x1="15.41" y1="6.51" x2="8.59" y2="10.49"/>
                </svg>
              </button>
            </div>
          </div>
        </header>
        
        <!-- 文章主体 -->
        <div class="article-body">
          <div class="article-content prose" v-html="renderedContent"></div>
          
          <!-- 文章底部 -->
          <div class="article-footer">
            <div class="article-footer-tags">
              <span class="footer-label">标签：</span>
              <router-link
                v-for="tag in articleStore.currentArticle.tags"
                :key="tag.id"
                :to="`/tag/${tag.name}`"
                class="footer-tag"
                :style="{ borderColor: tag.color, color: tag.color }"
              >
                #{{ tag.name }}
              </router-link>
            </div>
            
            <!-- 互动按钮 -->
            <div class="article-footer-actions">
              <button 
                class="footer-action-btn like" 
                :class="{ active: isLiked }"
                @click="toggleLike"
              >
                <svg width="20" height="20" viewBox="0 0 24 24" :fill="isLiked ? 'currentColor' : 'none'" stroke="currentColor" stroke-width="2">
                  <path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3"/>
                </svg>
                <span>{{ likeCount }}</span>
              </button>
              <button 
                class="footer-action-btn favorite" 
                :class="{ active: isFavorited }"
                @click="toggleFavorite"
              >
                <svg width="20" height="20" viewBox="0 0 24 24" :fill="isFavorited ? 'currentColor' : 'none'" stroke="currentColor" stroke-width="2">
                  <path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/>
                </svg>
                <span>{{ favoriteCount }}</span>
              </button>
              <button class="footer-action-btn share" @click="shareArticle">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <circle cx="18" cy="5" r="3"/>
                  <circle cx="6" cy="12" r="3"/>
                  <circle cx="18" cy="19" r="3"/>
                  <line x1="8.59" y1="13.51" x2="15.42" y2="17.49"/>
                  <line x1="15.41" y1="6.51" x2="8.59" y2="10.49"/>
                </svg>
                <span>分享</span>
              </button>
            </div>
          </div>
          
          <!-- 作者卡片 -->
          <div class="author-card">
            <img 
              :src="articleStore.currentArticle.user?.avatar || defaultAvatar"
              :alt="articleStore.currentArticle.user?.username"
              class="author-card-avatar"
              loading="lazy"
            />
            <div class="author-card-info">
              <div class="author-card-header">
                <span class="author-card-name">{{ articleStore.currentArticle.user?.username || '匿名用户' }}</span>
                <router-link :to="`/user/${(articleStore.currentArticle as ArticleWithStats)?.user?.id}`" class="author-card-link">
                  查看主页
                </router-link>
              </div>
              <p class="author-card-bio">{{ authorBio || '这个人很懒，还没有写简介' }}</p>
            </div>
          </div>
        </div>
        
        <!-- 评论区 -->
        <section class="comments-section">
          <h2 class="section-title">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
            </svg>
            评论 ({{ (articleStore.currentArticle as ArticleWithStats)?.comment_count || 0 }})
          </h2>
          
          <!-- 评论输入 -->
          <div class="comment-form" v-if="userStore.isLoggedIn">
            <div class="comment-form-avatar">
              <img :src="userStore.user?.avatar || defaultAvatar" alt="用户头像" loading="lazy" />
            </div>
            <div class="comment-form-input">
              <textarea
                v-model="commentText"
                placeholder="写下你的评论..."
                rows="3"
              ></textarea>
              <div class="comment-form-actions">
                <span class="comment-hint">支持 Markdown 语法</span>
                <button class="btn btn-primary" :disabled="!commentText.trim()">
                  发布评论
                </button>
              </div>
            </div>
          </div>
          <div v-else class="comment-login-prompt">
            <p>
              <router-link to="/login">登录</router-link> 后才能评论
            </p>
          </div>
          
          <!-- 评论列表 -->
          <div class="comments-list">
            <!-- 示例评论 -->
            <div class="comment-item">
              <div class="comment-avatar">
                <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=1" alt="评论者头像" loading="lazy" />
              </div>
              <div class="comment-content">
                <div class="comment-header">
                  <span class="comment-author">示例用户</span>
                  <span class="comment-time">3 天前</span>
                </div>
                <p class="comment-text">
                  这是一篇很棒的文章，感谢分享！内容讲解得很清晰，对我帮助很大。
                </p>
                <div class="comment-actions">
                  <button class="comment-action">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3"/>
                    </svg>
                    <span>12</span>
                  </button>
                  <button class="comment-action">回复</button>
                </div>
              </div>
            </div>
          </div>
        </section>
      </article>
      
      <!-- 文章不存在 -->
      <div v-else class="not-found">
        <div class="not-found-icon">
          <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1">
            <circle cx="12" cy="12" r="10"/>
            <line x1="12" y1="8" x2="12" y2="12"/>
            <line x1="12" y1="16" x2="12.01" y2="16"/>
          </svg>
        </div>
        <h2 class="not-found-title">文章不存在</h2>
        <p class="not-found-text">该文章可能已被删除或链接有误</p>
        <router-link to="/" class="btn btn-primary">返回首页</router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, computed, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useArticleStore } from '../stores/article'

interface ArticleWithStats {
  id: number
  title: string
  content: string
  summary: string
  status: 'DRAFT' | 'PUBLISHED'
  user_id: number
  view_count: number
  created_at: string
  updated_at: string
  tags: { id: number; name: string; color: string; count: number; }[]
  user?: {
    id?: number
    username: string
    avatar: string
  }
  likeCount?: number
  favoriteCount?: number
  comment_count?: number
}
import { useUserStore } from '../stores/user'
import { marked } from 'marked'
import DOMPurify from 'dompurify'
import axios from 'axios'

const route = useRoute()
const articleStore = useArticleStore()
const userStore = useUserStore()

const commentText = ref('')
const readingProgress = ref(0)
const isLiked = ref(false)
const isFavorited = ref(false)
const likeCount = ref(0)
const favoriteCount = ref(0)
const authorBio = ref('')

const defaultAvatar = 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA0MCA0MCI+PHJlY3Qgd2lkdGg9IjQwIiBoZWlnaHQ9IjQwIiByeD0iMjAiIGZpbGw9IiNFOEU4RTgiLz48Y2lyY2xlIGN4PSIyMCIgY3k9IjE2IiByPSI4IiBmaWxsPSIjQThBOEE4Ii8+PHBhdGggZD0iTTYgMzVjMC04LjI4NCA2LjcxNi0xNSAxNS0xNXMxNSA2LjcxNiAxNSAxNSIgZmlsbD0iI0E4QThBOCIvPjwvc3ZnPg=='

const renderedContent = computed(() => {
  if (!articleStore.currentArticle) return ''
  const html = marked.parse(articleStore.currentArticle.content, { async: false }) as string
  return DOMPurify.sanitize(html)
})

// 计算阅读时间（按每分钟300字）
const readingTime = computed(() => {
  if (!articleStore.currentArticle?.content) return 1
  const words = articleStore.currentArticle.content.length
  return Math.max(1, Math.ceil(words / 300))
})

// SEO Meta
watch(() => articleStore.currentArticle, (article) => {
  if (article) {
    document.title = `${article.title} - LiteBlog`
    const metaDesc = document.querySelector('meta[name="description"]')
    if (metaDesc) metaDesc.setAttribute('content', article.summary || '')
    const articleWithStats = article as ArticleWithStats
    likeCount.value = articleWithStats.likeCount || 0
    favoriteCount.value = articleWithStats.favoriteCount || 0
  }
}, { immediate: true })

const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

// 阅读进度
const updateReadingProgress = () => {
  const windowHeight = window.innerHeight
  const documentHeight = document.documentElement.scrollHeight - windowHeight
  const scrollTop = window.scrollY
  if (documentHeight > 0) {
    readingProgress.value = Math.min(100, Math.round((scrollTop / documentHeight) * 100))
  }
}

// 点赞
const toggleLike = async () => {
  if (!userStore.isLoggedIn) {
    window.location.href = '/login'
    return
  }
  try {
    const articleId = route.params.id
    const response = await axios.post(`/api/articles/${articleId}/like`)
    isLiked.value = response.data.liked
    likeCount.value = response.data.likeCount
  } catch (error) {
    console.error('点赞失败:', error)
  }
}

// 收藏
const toggleFavorite = async () => {
  if (!userStore.isLoggedIn) {
    window.location.href = '/login'
    return
  }
  try {
    const articleId = route.params.id
    const response = await axios.post(`/api/articles/${articleId}/favorite`)
    isFavorited.value = response.data.favorited
    favoriteCount.value = response.data.favoriteCount
  } catch (error) {
    console.error('收藏失败:', error)
  }
}

// 分享
const shareArticle = async () => {
  const url = window.location.href
  const title = articleStore.currentArticle?.title || ''
  
  if (navigator.share) {
    try {
      await navigator.share({ title, url })
    } catch (error) {
      // 用户取消分享
    }
  } else {
    // 复制链接
    try {
      await navigator.clipboard.writeText(url)
      alert('链接已复制到剪贴板')
    } catch {
      prompt('复制链接:', url)
    }
  }
}

// 加载互动状态
const loadInteractionStatus = async () => {
  if (!userStore.isLoggedIn) return
  const articleId = route.params.id
  
  try {
    const [likeRes, favRes] = await Promise.all([
      axios.get(`/api/articles/${articleId}/like/status`),
      axios.get(`/api/articles/${articleId}/favorite/status`)
    ])
    isLiked.value = likeRes.data.liked
    isFavorited.value = favRes.data.favorited
  } catch (error) {
    // ignore
  }
}

onMounted(async () => {
  const id = Number(route.params.id)
  await articleStore.getArticleById(id)
  
  window.addEventListener('scroll', updateReadingProgress, { passive: true })
  
  await loadInteractionStatus()
})

onUnmounted(() => {
  window.removeEventListener('scroll', updateReadingProgress)
})
</script>

<style scoped>
.article-detail-page {
  padding: var(--space-8) 0 var(--space-16);
  min-height: calc(100vh - 64px);
}

/* Reading Progress */
.reading-progress {
  position: fixed;
  top: 0;
  left: 0;
  height: 3px;
  background: linear-gradient(90deg, var(--color-primary), #8B5CF6);
  z-index: var(--z-fixed);
  transition: width 0.1s ease;
}

/* Loading State */
.loading-state {
  max-width: 720px;
  margin: 0 auto;
}

.skeleton-hero {
  margin-bottom: var(--space-8);
}

.skeleton-title {
  width: 80%;
  height: 40px;
  background: var(--color-bg-tertiary);
  border-radius: var(--radius-lg);
  margin-bottom: var(--space-4);
}

.skeleton-meta {
  width: 40%;
  height: 20px;
  background: var(--color-bg-hover);
  border-radius: var(--radius-md);
}

.skeleton-content {
  padding: var(--space-8);
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
}

.skeleton-paragraph {
  width: 100%;
  height: 16px;
  background: var(--color-bg-tertiary);
  border-radius: var(--radius-md);
  margin-bottom: var(--space-4);
}

.skeleton-paragraph.short {
  width: 60%;
}

/* Article Wrapper */
.article-wrapper {
  max-width: 720px;
  margin: 0 auto;
}

/* Article Header */
.article-header {
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
}

.breadcrumb a:hover {
  color: var(--color-primary);
}

.article-tags {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
  margin-bottom: var(--space-4);
}

.article-tag {
  padding: var(--space-1) var(--space-3);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  border-radius: var(--radius-full);
  text-decoration: none;
  transition: opacity var(--transition-fast);
}

.article-tag:hover {
  opacity: 0.8;
}

.article-title {
  font-size: var(--text-4xl);
  font-weight: var(--font-bold);
  line-height: var(--leading-tight);
  color: var(--color-text-primary);
  letter-spacing: -0.02em;
  margin-bottom: var(--space-6);
}

.article-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: var(--space-4);
  padding: var(--space-4) 0;
  border-top: 1px solid var(--color-border-secondary);
  border-bottom: 1px solid var(--color-border-secondary);
}

.author-info {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.author-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
}

.author-name {
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
}

.article-stats {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
  margin-top: var(--space-1);
}

.dot {
  opacity: 0.5;
}

.article-actions {
  display: flex;
  gap: var(--space-2);
}

.action-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: var(--color-bg-tertiary);
  border: 1px solid transparent;
  border-radius: var(--radius-lg);
  cursor: pointer;
  color: var(--color-text-secondary);
  transition: all var(--transition-fast);
}

.action-btn:hover {
  background: var(--color-bg-hover);
  color: var(--color-primary);
}

/* Article Body */
.article-body {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  overflow: hidden;
}

.article-content {
  padding: var(--space-8);
}

/* Article Footer */
.article-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: var(--space-4);
  padding: var(--space-6) var(--space-8);
  border-top: 1px solid var(--color-border-secondary);
  background: var(--color-bg-secondary);
}

.article-footer-tags {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: var(--space-2);
}

.footer-label {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

.footer-tag {
  padding: var(--space-1) var(--space-3);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  border: 1px solid;
  border-radius: var(--radius-full);
  text-decoration: none;
  transition: all var(--transition-fast);
}

.footer-tag:hover {
  opacity: 0.8;
}

.article-footer-actions {
  display: flex;
  gap: var(--space-3);
}

.footer-action-btn {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-4);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-lg);
  cursor: pointer;
  color: var(--color-text-secondary);
  transition: all var(--transition-fast);
}

.footer-action-btn:hover {
  border-color: var(--color-text-tertiary);
}

.footer-action-btn.like.active {
  color: #EF4444;
  border-color: #EF4444;
  background: rgba(239, 68, 68, 0.1);
}

.footer-action-btn.favorite.active {
  color: var(--color-warning);
  border-color: var(--color-warning);
  background: rgba(245, 158, 11, 0.1);
}

.footer-action-btn.share:hover {
  color: var(--color-primary);
  border-color: var(--color-primary);
  background: rgba(0, 102, 255, 0.1);
}

/* Author Card */
.author-card {
  display: flex;
  gap: var(--space-4);
  padding: var(--space-6) var(--space-8);
  border-top: 1px solid var(--color-border-secondary);
  background: var(--color-bg-secondary);
}

.author-card-avatar {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  object-fit: cover;
}

.author-card-info {
  flex: 1;
}

.author-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--space-2);
}

.author-card-name {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
}

.author-card-link {
  font-size: var(--text-sm);
  color: var(--color-primary);
  text-decoration: none;
}

.author-card-link:hover {
  text-decoration: underline;
}

.author-card-bio {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
  line-height: 1.6;
}

/* Comments Section */
.comments-section {
  margin-top: var(--space-8);
  padding: var(--space-8);
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
}

.section-title {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-6);
}

/* Comment Form */
.comment-form {
  display: flex;
  gap: var(--space-4);
  margin-bottom: var(--space-8);
}

.comment-form-avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.comment-form-input {
  flex: 1;
}

.comment-form-input textarea {
  width: 100%;
  padding: var(--space-4);
  font-size: var(--text-base);
  font-family: inherit;
  color: var(--color-text-primary);
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-lg);
  resize: vertical;
  min-height: 100px;
  transition: all var(--transition-fast);
}

.comment-form-input textarea:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px rgba(0, 102, 255, 0.15);
}

.comment-form-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: var(--space-3);
}

.comment-hint {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

.comment-login-prompt {
  padding: var(--space-6);
  text-align: center;
  background: var(--color-bg-secondary);
  border-radius: var(--radius-lg);
  margin-bottom: var(--space-8);
}

.comment-login-prompt p {
  font-size: var(--text-base);
  color: var(--color-text-tertiary);
}

.comment-login-prompt a {
  color: var(--color-primary);
  text-decoration: none;
}

.comment-login-prompt a:hover {
  text-decoration: underline;
}

/* Comments List */
.comments-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
}

.comment-item {
  display: flex;
  gap: var(--space-4);
}

.comment-avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  margin-bottom: var(--space-2);
}

.comment-author {
  font-size: var(--text-sm);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
}

.comment-time {
  font-size: var(--text-xs);
  color: var(--color-text-tertiary);
}

.comment-text {
  font-size: var(--text-base);
  color: var(--color-text-secondary);
  line-height: var(--leading-relaxed);
  margin-bottom: var(--space-3);
}

.comment-actions {
  display: flex;
  align-items: center;
  gap: var(--space-4);
}

.comment-action {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
  background: none;
  border: none;
  cursor: pointer;
  transition: color var(--transition-fast);
}

.comment-action:hover {
  color: var(--color-primary);
}

/* Not Found */
.not-found {
  text-align: center;
  padding: var(--space-16) 0;
}

.not-found-icon {
  color: var(--color-text-tertiary);
  margin-bottom: var(--space-6);
  opacity: 0.5;
}

.not-found-title {
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-3);
}

.not-found-text {
  font-size: var(--text-base);
  color: var(--color-text-tertiary);
  margin-bottom: var(--space-6);
}

/* Responsive */
@media (max-width: 640px) {
  .article-title {
    font-size: var(--text-2xl);
  }
  
  .article-meta {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .article-content {
    padding: var(--space-6);
  }
  
  .article-footer {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .author-card {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .author-card-header {
    flex-direction: column;
    gap: var(--space-2);
  }
}
</style>
