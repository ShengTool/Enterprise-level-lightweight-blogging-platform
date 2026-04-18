<template>
  <div class="page-container">
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">评论管理</h2>
        <p class="page-subtitle">审核和管理用户评论</p>
      </div>
    </div>
    
    <!-- 筛选栏 -->
    <div class="filter-bar">
      <div class="filter-group">
        <div class="search-input">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="11" cy="11" r="8"/>
            <path d="m21 21-4.35-4.35"/>
          </svg>
          <input 
            v-model="searchQuery" 
            type="text" 
            placeholder="搜索评论内容..."
          />
        </div>
      </div>
      <div class="filter-actions">
        <button 
          v-if="selectedComments.length > 0"
          class="btn btn-ghost danger"
          @click="confirmBatchDelete"
        >
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="3 6 5 6 21 6"/>
            <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
          </svg>
          删除选中 ({{ selectedComments.length }})
        </button>
      </div>
    </div>
    
    <!-- 评论列表 -->
    <div class="content-card">
      <div v-if="loading" class="loading-list">
        <div v-for="i in 3" :key="i" class="skeleton-comment">
          <div class="skeleton-avatar"></div>
          <div class="skeleton-content">
            <div class="skeleton-header"></div>
            <div class="skeleton-text"></div>
          </div>
        </div>
      </div>
      
      <div v-else-if="filteredComments.length > 0" class="comments-list">
        <div 
          v-for="comment in filteredComments" 
          :key="comment.id"
          class="comment-item"
        >
          <div class="comment-select">
            <input 
              type="checkbox" 
              :checked="selectedComments.includes(comment.id)"
              @change="toggleSelect(comment.id)"
            />
          </div>
          
          <div class="comment-avatar">
            <img :src="comment.user?.avatar || defaultAvatar" :alt="comment.user?.username" />
          </div>
          
          <div class="comment-content">
            <div class="comment-header">
              <span class="comment-author">{{ comment.user?.username || '匿名用户' }}</span>
              <span class="comment-time">{{ formatDate(comment.createdAt) }}</span>
              <router-link
                v-if="comment.article"
                :to="`/article/${comment.article.id}`"
                class="comment-article"
              >
                {{ comment.article.title }}
              </router-link>
            </div>
            <p class="comment-text">{{ comment.content }}</p>
            <div class="comment-actions">
              <button class="action-link" @click="replyComment(comment)">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
                </svg>
                回复
              </button>
              <button class="action-link danger" @click="confirmDelete(comment)">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <polyline points="3 6 5 6 21 6"/>
                  <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
                </svg>
                删除
              </button>
            </div>
          </div>
        </div>
      </div>
      
      <div v-else class="empty-state">
        <div class="empty-icon">
          <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
          </svg>
        </div>
        <h3>暂无评论</h3>
        <p>还没有收到任何评论</p>
      </div>
    </div>
    
    <!-- 删除确认弹窗 -->
    <Teleport to="body">
      <div v-if="showDeleteModal" class="modal-overlay" @click.self="showDeleteModal = false">
        <div class="modal modal-sm">
          <div class="modal-body text-center">
            <div class="confirm-icon warning">
              <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/>
                <line x1="12" y1="9" x2="12" y2="13"/>
                <line x1="12" y1="17" x2="12.01" y2="17"/>
              </svg>
            </div>
            <h3>确认删除</h3>
            <p v-if="commentToDelete">确定要删除这条评论吗？此操作不可恢复。</p>
            <p v-else>确定要删除选中的 {{ selectedComments.length }} 条评论吗？</p>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="showDeleteModal = false">取消</button>
            <button class="btn btn-primary danger" :disabled="deleteLoading" @click="executeDelete">
              {{ deleteLoading ? '删除中...' : '删除' }}
            </button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import axios from '../../utils/axios'

const loading = ref(false)
const searchQuery = ref('')
const selectedComments = ref<number[]>([])
const showDeleteModal = ref(false)
const commentToDelete = ref<any>(null)
const isBatchDelete = ref(false)
const deleteLoading = ref(false)

const defaultAvatar = 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA0MCA0MCI+PHJlY3Qgd2lkdGg9IjQwIiBoZWlnaHQ9IjQwIiByeD0iMjAiIGZpbGw9IiNFOEU4RTgiLz48Y2lyY2xlIGN4PSIyMCIgY3k9IjE2IiByPSI4IiBmaWxsPSIjQThBOEE4Ii8+PHBhdGggZD0iTTYgMzVjMC04LjI4NCA2LjcxNi0xNSAxNS0xNXMxNSA2LjcxNiAxNSAxNSIgZmlsbD0iI0E4QThBOCIvPjwvc3ZnPg=='

const comments = ref<any[]>([])

const loadComments = async () => {
  loading.value = true
  try {
    const res = await axios.get('/comments')
    comments.value = res.data
  } catch (err: any) {
    console.error('加载评论失败', err)
  } finally {
    loading.value = false
  }
}

const filteredComments = computed(() => {
  if (!searchQuery.value) return comments.value
  const query = searchQuery.value.toLowerCase()
  return comments.value.filter(c =>
    c.content.toLowerCase().includes(query) ||
    c.user?.username.toLowerCase().includes(query) ||
    c.article?.title.toLowerCase().includes(query)
  )
})

const formatDate = (date: string) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const toggleSelect = (id: number) => {
  const index = selectedComments.value.indexOf(id)
  if (index > -1) {
    selectedComments.value.splice(index, 1)
  } else {
    selectedComments.value.push(id)
  }
}

const replyComment = (comment: any) => {
  console.log('回复评论', comment)
}

const confirmDelete = (comment: any) => {
  commentToDelete.value = comment
  isBatchDelete.value = false
  showDeleteModal.value = true
}

const confirmBatchDelete = () => {
  commentToDelete.value = null
  isBatchDelete.value = true
  showDeleteModal.value = true
}

const executeDelete = async () => {
  deleteLoading.value = true
  try {
    if (isBatchDelete.value) {
      for (const id of selectedComments.value) {
        await axios.delete(`/comments/${id}`)
      }
      selectedComments.value = []
    } else if (commentToDelete.value) {
      await axios.delete(`/comments/${commentToDelete.value.id}`)
    }
    showDeleteModal.value = false
    commentToDelete.value = null
    await loadComments()
  } catch (err: any) {
    console.error('删除评论失败', err)
    alert('删除失败，请重试')
  } finally {
    deleteLoading.value = false
  }
}

onMounted(() => {
  loadComments()
})
</script>

<style scoped>
.page-container {
  max-width: 1200px;
}

.page-header {
  margin-bottom: var(--space-6);
}

.page-title {
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-1);
}

.page-subtitle {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

/* Filter Bar */
.filter-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--space-4);
  flex-wrap: wrap;
  gap: var(--space-3);
}

.filter-group {
  flex: 1;
  min-width: 280px;
}

.search-input {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-4);
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-lg);
}

.search-input svg {
  color: var(--color-text-tertiary);
}

.search-input input {
  flex: 1;
  background: none;
  border: none;
  outline: none;
  font-size: var(--text-sm);
  color: var(--color-text-primary);
}

/* Content Card */
.content-card {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  overflow: hidden;
}

/* Comments List */
.comments-list {
  display: flex;
  flex-direction: column;
}

.comment-item {
  display: flex;
  gap: var(--space-4);
  padding: var(--space-5);
  border-bottom: 1px solid var(--color-border-secondary);
  transition: background var(--transition-fast);
}

.comment-item:hover {
  background: var(--color-bg-secondary);
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-select {
  padding-top: var(--space-1);
}

.comment-avatar {
  flex-shrink: 0;
}

.comment-avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.comment-content {
  flex: 1;
  min-width: 0;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  margin-bottom: var(--space-2);
  flex-wrap: wrap;
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

.comment-article {
  font-size: var(--text-xs);
  color: var(--color-primary);
  text-decoration: none;
}

.comment-article:hover {
  text-decoration: underline;
}

.comment-text {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  line-height: 1.6;
  margin-bottom: var(--space-3);
}

.comment-actions {
  display: flex;
  gap: var(--space-4);
}

.action-link {
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

.action-link:hover {
  color: var(--color-primary);
}

.action-link.danger:hover {
  color: var(--color-error);
}

/* Loading Skeleton */
.loading-list {
  padding: var(--space-5);
}

.skeleton-comment {
  display: flex;
  gap: var(--space-4);
  padding: var(--space-4) 0;
  border-bottom: 1px solid var(--color-border-secondary);
}

.skeleton-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: var(--color-bg-tertiary);
  flex-shrink: 0;
}

.skeleton-content {
  flex: 1;
}

.skeleton-header {
  width: 120px;
  height: 16px;
  background: var(--color-bg-tertiary);
  border-radius: var(--radius-md);
  margin-bottom: var(--space-2);
}

.skeleton-text {
  width: 80%;
  height: 14px;
  background: var(--color-bg-hover);
  border-radius: var(--radius-md);
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: var(--space-12);
}

.empty-state .empty-icon {
  color: var(--color-text-tertiary);
  opacity: 0.5;
  margin-bottom: var(--space-4);
}

.empty-state h3 {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-2);
}

.empty-state p {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

/* Modal */
.modal-overlay {
  position: fixed;
  inset: 0;
  z-index: var(--z-modal);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: var(--space-4);
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(4px);
}

.modal {
  width: 100%;
  max-width: 400px;
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-2xl);
}

.modal-body {
  padding: var(--space-6);
  text-align: center;
}

.modal-body h3 {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-2);
}

.modal-body p {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: var(--space-3);
  padding: var(--space-4) var(--space-6);
  border-top: 1px solid var(--color-border-secondary);
}

.confirm-icon.warning {
  color: var(--color-warning);
  margin-bottom: var(--space-4);
}

.btn.danger {
  background: var(--color-error);
  border-color: var(--color-error);
}
</style>
