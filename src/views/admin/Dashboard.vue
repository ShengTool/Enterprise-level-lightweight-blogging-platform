<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const stats = ref<{
  articleCount: number
  commentCount: number
  userCount: number
  tagCount: number
  todayArticles: number
  todayComments: number
  todayUsers: number
  recentArticles: Array<{
    id: number
    title: string
    status: string
    viewCount: number
    createdAt: string
  }>
  recentComments: Array<{
    id: number
    content: string
    createdAt: string
    user?: { id: number; username: string }
    article?: { id: number; title: string }
  }>
} | null>(null)

onMounted(async () => {
  loading.value = true
  try {
    const res = await fetch('/api/admin/dashboard', {
      headers: { Authorization: `Bearer ${userStore.token}` }
    })
    if (res.ok) {
      stats.value = await res.json()
    }
  } catch (e) {
    console.error('Failed to load dashboard', e)
  } finally {
    loading.value = false
  }
})

const formatDate = (dateStr: string) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleDateString('zh-CN', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}
</script>

<template>
  <div class="dashboard">
    <div class="page-header">
      <h2>仪表盘</h2>
    </div>

    <div v-if="loading" class="loading">加载中...</div>

    <template v-else-if="stats">
      <!-- 统计卡片 -->
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon articles">📄</div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.articleCount }}</div>
            <div class="stat-label">文章总数</div>
            <div class="stat-today">今日 +{{ stats.todayArticles }}</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon comments">💬</div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.commentCount }}</div>
            <div class="stat-label">评论总数</div>
            <div class="stat-today">今日 +{{ stats.todayComments }}</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon users">👥</div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.userCount }}</div>
            <div class="stat-label">用户总数</div>
            <div class="stat-today">今日 +{{ stats.todayUsers }}</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon tags">🏷️</div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.tagCount }}</div>
            <div class="stat-label">标签总数</div>
          </div>
        </div>
      </div>

      <!-- 最近内容 -->
      <div class="recent-grid">
        <div class="recent-card">
          <div class="card-header">
            <h3>最近文章</h3>
            <router-link to="/admin/articles" class="view-all">查看全部 →</router-link>
          </div>
          <div class="recent-list">
            <div v-for="article in stats.recentArticles" :key="article.id" class="recent-item">
              <div class="recent-title">{{ article.title }}</div>
              <div class="recent-meta">
                <span class="status-badge" :class="article.status.toLowerCase()">{{ article.status }}</span>
                <span>👁 {{ article.viewCount }}</span>
                <span>{{ formatDate(article.createdAt) }}</span>
              </div>
            </div>
            <div v-if="!stats.recentArticles?.length" class="empty">暂无文章</div>
          </div>
        </div>

        <div class="recent-card">
          <div class="card-header">
            <h3>最近评论</h3>
            <router-link to="/admin/comments" class="view-all">查看全部 →</router-link>
          </div>
          <div class="recent-list">
            <div v-for="comment in stats.recentComments" :key="comment.id" class="recent-item">
              <div class="recent-content">{{ comment.content }}</div>
              <div class="recent-meta">
                <span>{{ comment.user?.username || '未知用户' }}</span>
                <span>·</span>
                <span>{{ comment.article?.title || '未知文章' }}</span>
                <span>·</span>
                <span>{{ formatDate(comment.createdAt) }}</span>
              </div>
            </div>
            <div v-if="!stats.recentComments?.length" class="empty">暂无评论</div>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<style scoped>
.dashboard {
  padding: 24px;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h2 {
  font-size: 24px;
  font-weight: 600;
  color: #1f2937;
}

.loading {
  text-align: center;
  padding: 48px;
  color: #6b7280;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.08);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.stat-icon.articles { background: #fef3c7; }
.stat-icon.comments { background: #dbeafe; }
.stat-icon.users { background: #d1fae5; }
.stat-icon.tags { background: #f3e8ff; }

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #1f2937;
}

.stat-label {
  font-size: 14px;
  color: #6b7280;
}

.stat-today {
  font-size: 12px;
  color: #10b981;
  margin-top: 4px;
}

.recent-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 16px;
}

.recent-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.card-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
}

.view-all {
  font-size: 13px;
  color: #6366f1;
  text-decoration: none;
}

.view-all:hover {
  text-decoration: underline;
}

.recent-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.recent-item {
  padding: 12px;
  background: #f9fafb;
  border-radius: 8px;
}

.recent-title {
  font-size: 14px;
  font-weight: 500;
  color: #1f2937;
  margin-bottom: 6px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.recent-content {
  font-size: 13px;
  color: #4b5563;
  margin-bottom: 6px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.recent-meta {
  display: flex;
  gap: 8px;
  font-size: 12px;
  color: #9ca3af;
}

.status-badge {
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 500;
}

.status-badge.published {
  background: #d1fae5;
  color: #059669;
}

.status-badge.draft {
  background: #fef3c7;
  color: #d97706;
}

.empty {
  text-align: center;
  padding: 24px;
  color: #9ca3af;
  font-size: 14px;
}
</style>
