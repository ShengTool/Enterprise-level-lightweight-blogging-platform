<template>
  <div class="page-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">文章管理</h2>
        <p class="page-subtitle">管理您的所有文章，包括发布、编辑和删除</p>
      </div>
      <router-link to="/admin/articles/edit" class="btn btn-primary">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="12" y1="5" x2="12" y2="19"/>
          <line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        发布文章
      </router-link>
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
            placeholder="搜索文章标题..."
            @keyup.enter="handleSearch"
          />
        </div>
        
        <select v-model="statusFilter" class="filter-select">
          <option value="">全部状态</option>
          <option value="PUBLISHED">已发布</option>
          <option value="DRAFT">草稿</option>
        </select>
        
        <select v-model="sortBy" class="filter-select">
          <option value="newest">最新发布</option>
          <option value="oldest">最早发布</option>
          <option value="views">最多阅读</option>
        </select>
      </div>
      
      <div class="filter-actions">
        <button 
          v-if="selectedArticles.length > 0"
          class="btn btn-ghost danger"
          @click="confirmBatchDelete"
        >
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="3 6 5 6 21 6"/>
            <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
          </svg>
          删除选中 ({{ selectedArticles.length }})
        </button>
      </div>
    </div>
    
    <!-- 文章列表 -->
    <div class="content-card">
      <div v-if="articleStore.loading" class="loading-state">
        <div v-for="i in 5" :key="i" class="skeleton-row">
          <div class="skeleton-cell checkbox"></div>
          <div class="skeleton-cell title"></div>
          <div class="skeleton-cell status"></div>
          <div class="skeleton-cell date"></div>
          <div class="skeleton-cell views"></div>
          <div class="skeleton-cell actions"></div>
        </div>
      </div>
      
      <div v-else-if="filteredArticles.length > 0" class="table-container">
        <table class="data-table">
          <thead>
            <tr>
              <th class="checkbox-col">
                <input 
                  type="checkbox" 
                  :checked="isAllSelected"
                  @change="toggleSelectAll"
                />
              </th>
              <th>文章标题</th>
              <th>状态</th>
              <th>发布时间</th>
              <th>阅读量</th>
              <th class="actions-col">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="article in filteredArticles" :key="article.id">
              <td class="checkbox-col">
                <input 
                  type="checkbox" 
                  :checked="selectedArticles.includes(article.id)"
                  @change="toggleSelect(article.id)"
                />
              </td>
              <td>
                <div class="article-info">
                  <router-link :to="`/article/${article.id}`" class="article-title">
                    {{ article.title }}
                  </router-link>
                  <div class="article-tags">
                    <span 
                      v-for="tag in article.tags?.slice(0, 3)" 
                      :key="tag.id"
                      class="tag-badge"
                      :style="{ backgroundColor: tag.color + '20', color: tag.color }"
                    >
                      {{ tag.name }}
                    </span>
                  </div>
                </div>
              </td>
              <td>
                <span 
                  class="status-badge"
                  :class="article.status.toLowerCase()"
                >
                  {{ article.status === 'PUBLISHED' ? '已发布' : '草稿' }}
                </span>
              </td>
              <td>{{ formatDate(article.created_at) }}</td>
              <td>
                <div class="views-count">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                    <circle cx="12" cy="12" r="3"/>
                  </svg>
                  {{ article.view_count }}
                </div>
              </td>
              <td class="actions-col">
                <div class="action-buttons">
                  <router-link 
                    :to="`/article/${article.id}`" 
                    class="action-btn"
                    title="查看"
                  >
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                      <circle cx="12" cy="12" r="3"/>
                    </svg>
                  </router-link>
                  <router-link 
                    :to="`/admin/articles/edit/${article.id}`" 
                    class="action-btn"
                    title="编辑"
                  >
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                      <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
                    </svg>
                  </router-link>
                  <button 
                    class="action-btn danger"
                    title="删除"
                    @click="confirmDelete(article)"
                  >
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <polyline points="3 6 5 6 21 6"/>
                      <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
                    </svg>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <div v-else class="empty-state">
        <div class="empty-icon">
          <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"/>
            <polyline points="14 2 14 8 20 8"/>
          </svg>
        </div>
        <h3>暂无文章</h3>
        <p>还没有任何文章，开始创作吧！</p>
        <router-link to="/admin/articles/edit" class="btn btn-primary">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="12" y1="5" x2="12" y2="19"/>
            <line x1="5" y1="12" x2="19" y2="12"/>
          </svg>
          发布文章
        </router-link>
      </div>
      
      <!-- 分页 -->
      <div v-if="filteredArticles.length > 0" class="pagination-bar">
        <div class="pagination-info">
          共 {{ articleStore.total }} 篇文章
        </div>
        <div class="pagination">
          <button 
            class="pagination-btn"
            :disabled="currentPage === 1"
            @click="changePage(currentPage - 1)"
          >
            上一页
          </button>
          <span class="pagination-current">
            第 {{ currentPage }} 页
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
            <p v-if="articleToDelete">
              确定要删除文章「{{ articleToDelete.title }}」吗？此操作不可恢复。
            </p>
            <p v-else>
              确定要删除选中的 {{ selectedArticles.length }} 篇文章吗？此操作不可恢复。
            </p>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="showDeleteModal = false">取消</button>
            <button class="btn btn-primary danger" @click="executeDelete">删除</button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useArticleStore } from '../../stores/article'

const articleStore = useArticleStore()

const searchQuery = ref('')
const statusFilter = ref('')
const sortBy = ref('newest')
const currentPage = ref(1)
const pageSize = ref(10)
const selectedArticles = ref<number[]>([])
const showDeleteModal = ref(false)
const articleToDelete = ref<any>(null)
const isBatchDelete = ref(false)

const filteredArticles = computed(() => {
  let articles = [...(articleStore.articles || [])]
  
  // 搜索过滤
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    articles = articles.filter(a => 
      a.title.toLowerCase().includes(query) ||
      a.summary.toLowerCase().includes(query)
    )
  }
  
  // 状态过滤
  if (statusFilter.value) {
    articles = articles.filter(a => a.status === statusFilter.value)
  }
  
  // 排序
  switch (sortBy.value) {
    case 'newest':
      articles.sort((a, b) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime())
      break
    case 'oldest':
      articles.sort((a, b) => new Date(a.created_at).getTime() - new Date(b.created_at).getTime())
      break
    case 'views':
      articles.sort((a, b) => b.view_count - a.view_count)
      break
  }
  
  return articles
})

const totalPages = computed(() => Math.ceil(articleStore.total / pageSize.value))

const isAllSelected = computed(() => {
  return filteredArticles.value.length > 0 && 
    filteredArticles.value.every(a => selectedArticles.value.includes(a.id))
})

const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}

const handleSearch = () => {
  currentPage.value = 1
}

const toggleSelectAll = () => {
  if (isAllSelected.value) {
    selectedArticles.value = []
  } else {
    selectedArticles.value = filteredArticles.value.map(a => a.id)
  }
}

const toggleSelect = (id: number) => {
  const index = selectedArticles.value.indexOf(id)
  if (index > -1) {
    selectedArticles.value.splice(index, 1)
  } else {
    selectedArticles.value.push(id)
  }
}

const confirmDelete = (article: any) => {
  articleToDelete.value = article
  isBatchDelete.value = false
  showDeleteModal.value = true
}

const confirmBatchDelete = () => {
  articleToDelete.value = null
  isBatchDelete.value = true
  showDeleteModal.value = true
}

const executeDelete = async () => {
  if (isBatchDelete.value) {
    for (const id of selectedArticles.value) {
      await articleStore.deleteArticle(id)
    }
    selectedArticles.value = []
  } else if (articleToDelete.value) {
    await articleStore.deleteArticle(articleToDelete.value.id)
  }
  showDeleteModal.value = false
  articleToDelete.value = null
}

const changePage = (page: number) => {
  currentPage.value = page
  loadArticles()
}

const loadArticles = async () => {
  await articleStore.getArticles(currentPage.value, pageSize.value)
}

onMounted(() => {
  loadArticles()
})
</script>

<style scoped>
.page-container {
  max-width: 1200px;
}

.page-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: var(--space-6);
}

.header-content {
  flex: 1;
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
  gap: var(--space-4);
  margin-bottom: var(--space-4);
  flex-wrap: wrap;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  flex-wrap: wrap;
}

.search-input {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-4);
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-lg);
  min-width: 280px;
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

.filter-select {
  padding: var(--space-2) var(--space-4);
  font-size: var(--text-sm);
  color: var(--color-text-primary);
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-lg);
  cursor: pointer;
  outline: none;
}

.filter-select:focus {
  border-color: var(--color-primary);
}

/* Content Card */
.content-card {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  overflow: hidden;
}

/* Table */
.table-container {
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: var(--space-4);
  text-align: left;
  border-bottom: 1px solid var(--color-border-secondary);
}

.data-table th {
  font-size: var(--text-xs);
  font-weight: var(--font-semibold);
  color: var(--color-text-tertiary);
  text-transform: uppercase;
  letter-spacing: 0.05em;
  background: var(--color-bg-secondary);
}

.data-table tbody tr:hover {
  background: var(--color-bg-secondary);
}

.checkbox-col {
  width: 48px;
  text-align: center;
}

.actions-col {
  width: 120px;
  text-align: right;
}

.article-info {
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
}

.article-title {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-primary);
  text-decoration: none;
}

.article-title:hover {
  color: var(--color-primary);
}

.article-tags {
  display: flex;
  gap: var(--space-1);
  flex-wrap: wrap;
}

.tag-badge {
  padding: 2px 8px;
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
  border-radius: var(--radius-full);
}

.status-badge {
  display: inline-flex;
  padding: var(--space-1) var(--space-3);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
  border-radius: var(--radius-full);
}

.status-badge.published {
  background: rgba(16, 185, 129, 0.1);
  color: var(--color-success);
}

.status-badge.draft {
  background: var(--color-bg-tertiary);
  color: var(--color-text-tertiary);
}

.views-count {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
}

.action-buttons {
  display: flex;
  justify-content: flex-end;
  gap: var(--space-1);
}

.action-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  background: transparent;
  border: 1px solid transparent;
  border-radius: var(--radius-lg);
  cursor: pointer;
  color: var(--color-text-tertiary);
  transition: all var(--transition-fast);
}

.action-btn:hover {
  background: var(--color-bg-hover);
  color: var(--color-text-primary);
}

.action-btn.danger:hover {
  color: var(--color-error);
  background: rgba(239, 68, 68, 0.1);
}

/* Pagination */
.pagination-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--space-4);
  border-top: 1px solid var(--color-border-secondary);
}

.pagination-info {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

.pagination {
  display: flex;
  align-items: center;
  gap: var(--space-2);
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

.pagination-current {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  padding: 0 var(--space-2);
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
  margin-bottom: var(--space-4);
}

/* Loading Skeleton */
.loading-state {
  padding: var(--space-4);
}

.skeleton-row {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  padding: var(--space-4) 0;
  border-bottom: 1px solid var(--color-border-secondary);
}

.skeleton-cell {
  height: 20px;
  background: var(--color-bg-tertiary);
  border-radius: var(--radius-md);
}

.skeleton-cell.checkbox {
  width: 20px;
}

.skeleton-cell.title {
  flex: 1;
}

.skeleton-cell.status,
.skeleton-cell.date,
.skeleton-cell.views {
  width: 80px;
}

.skeleton-cell.actions {
  width: 100px;
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
  max-width: 480px;
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-2xl);
}

.modal-sm {
  max-width: 400px;
}

.modal-body {
  padding: var(--space-6);
}

.modal-body.text-center {
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

.confirm-icon {
  margin-bottom: var(--space-4);
}

.confirm-icon.warning {
  color: var(--color-warning);
}

.btn.danger {
  background: var(--color-error);
  border-color: var(--color-error);
}

.btn.danger:hover {
  background: #dc2626;
  border-color: #dc2626;
}
</style>
