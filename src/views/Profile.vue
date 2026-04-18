<template>
  <div class="profile-page">
    <div class="container-app">
      <!-- 页面头部 -->
      <header class="profile-header">
        <nav class="breadcrumb">
          <router-link to="/">首页</router-link>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="9 18 15 12 9 6"/>
          </svg>
          <span>个人中心</span>
        </nav>
      </header>
      
      <!-- 加载状态 -->
      <div v-if="userStore.loading" class="loading-state">
        <div class="skeleton-profile">
          <div class="skeleton-avatar"></div>
          <div class="skeleton-info">
            <div class="skeleton-name"></div>
            <div class="skeleton-email"></div>
          </div>
        </div>
      </div>
      
      <!-- 未登录状态 -->
      <div v-else-if="!userStore.isLoggedIn" class="empty-state">
        <div class="empty-icon">
          <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
            <circle cx="12" cy="7" r="4"/>
          </svg>
        </div>
        <h3 class="empty-title">请先登录</h3>
        <p class="empty-description">登录后即可查看个人中心</p>
        <router-link to="/login" class="btn btn-primary btn-lg">立即登录</router-link>
      </div>
      
      <!-- 个人资料 -->
      <div v-else class="profile-content">
        <!-- 用户信息卡片 -->
        <div class="profile-card">
          <div class="profile-main">
            <div class="avatar-section">
              <div class="avatar-wrapper">
                <img 
                  :src="userStore.user?.avatar || defaultAvatar" 
                  :alt="userStore.user?.username"
                  class="profile-avatar"
                  loading="lazy"
                />
                <button class="avatar-edit" @click="showAvatarModal = true">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                    <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
                  </svg>
                </button>
              </div>
            </div>
            
            <div class="info-section">
              <div class="info-header">
                <div>
                  <h1 class="profile-name">{{ userStore.user?.username }}</h1>
                  <p class="profile-email">{{ userStore.user?.email }}</p>
                </div>
                <button class="btn btn-secondary" @click="showEditModal = true">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                    <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
                  </svg>
                  编辑资料
                </button>
              </div>
              
              <div class="profile-stats">
                <div class="stat-item">
                  <span class="stat-value">{{ myArticles.length }}</span>
                  <span class="stat-label">文章</span>
                </div>
                <div class="stat-item">
                  <span class="stat-value">{{ totalViews }}</span>
                  <span class="stat-label">阅读</span>
                </div>
                <div class="stat-item">
                  <span class="stat-value">{{ myArticles.filter(a => a.status === 'DRAFT').length }}</span>
                  <span class="stat-label">草稿</span>
                </div>
                <div class="stat-item">
                  <span class="stat-value">{{ userStore.user?.isAdmin ? '管理员' : '普通用户' }}</span>
                  <span class="stat-label">角色</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 标签页 -->
        <div class="tabs-section">
          <div class="tabs-header">
            <button 
              v-for="tab in tabs" 
              :key="tab.key"
              class="tab-btn"
              :class="{ active: activeTab === tab.key }"
              @click="activeTab = tab.key"
            >
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <component :is="tab.icon" />
              </svg>
              {{ tab.label }}
              <span v-if="tab.count" class="tab-count">{{ tab.count }}</span>
            </button>
          </div>
          
          <div class="tabs-content">
            <!-- 我的文章 -->
            <div v-if="activeTab === 'articles'" class="tab-panel">
              <div v-if="articleStore.loading" class="articles-skeleton">
                <div v-for="i in 3" :key="i" class="article-skeleton">
                  <div class="skeleton-title"></div>
                  <div class="skeleton-meta"></div>
                </div>
              </div>
              
              <div v-else-if="myArticles.length > 0" class="articles-list">
                <article
                  v-for="article in myArticles"
                  :key="article.id"
                  class="article-item"
                >
                  <div class="article-info">
                    <div class="article-header-row">
                      <h3 class="article-title">
                        <router-link :to="`/article/${article.id}`">{{ article.title }}</router-link>
                      </h3>
                      <span 
                        class="article-status"
                        :class="article.status.toLowerCase()"
                      >
                        {{ article.status === 'PUBLISHED' ? '已发布' : '草稿' }}
                      </span>
                    </div>
                    <p class="article-summary">{{ article.summary }}</p>
                    <div class="article-meta">
                      <span class="meta-item">
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                          <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                          <circle cx="12" cy="12" r="3"/>
                        </svg>
                        {{ article.viewCount }} 阅读
                      </span>
                      <span class="meta-item">
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                          <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
                          <line x1="16" y1="2" x2="16" y2="6"/>
                          <line x1="8" y1="2" x2="8" y2="6"/>
                          <line x1="3" y1="10" x2="21" y2="10"/>
                        </svg>
                        {{ formatDate(article.createdAt) }}
                      </span>
                      <div class="article-tags-inline">
                        <span 
                          v-for="tag in article.tags" 
                          :key="tag.id"
                          class="mini-tag"
                          :style="{ backgroundColor: tag.color + '20', color: tag.color }"
                        >
                          {{ tag.name }}
                        </span>
                      </div>
                    </div>
                  </div>
                  <div class="article-actions">
                    <router-link 
                      :to="`/admin/articles/edit/${article.id}`" 
                      class="action-btn edit"
                    >
                      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                        <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
                      </svg>
                      编辑
                    </router-link>
                    <button class="action-btn delete" @click="confirmDelete(article)">
                      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <polyline points="3 6 5 6 21 6"/>
                        <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
                      </svg>
                      删除
                    </button>
                  </div>
                </article>
              </div>
              
              <div v-else class="empty-tab">
                <div class="empty-icon">
                  <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                    <path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"/>
                    <polyline points="14 2 14 8 20 8"/>
                  </svg>
                </div>
                <p>还没有发布任何文章</p>
                <router-link to="/admin/articles/edit" class="btn btn-primary">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <line x1="12" y1="5" x2="12" y2="19"/>
                    <line x1="5" y1="12" x2="19" y2="12"/>
                  </svg>
                  写文章
                </router-link>
              </div>
            </div>
            
            <!-- 账户设置 -->
            <div v-else-if="activeTab === 'settings'" class="tab-panel">
              <div class="settings-card">
                <h3 class="settings-title">账户安全</h3>
                <div class="settings-list">
                  <div class="setting-item">
                    <div class="setting-info">
                      <h4>修改密码</h4>
                      <p>定期更换密码可以保护账户安全</p>
                    </div>
                    <button class="btn btn-secondary" @click="showPasswordModal = true">
                      修改
                    </button>
                  </div>
                  <div class="setting-item danger">
                    <div class="setting-info">
                      <h4>退出登录</h4>
                      <p>退出当前账户</p>
                    </div>
                    <button class="btn btn-ghost danger" @click="handleLogout">
                      退出
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 修改头像弹窗 -->
    <Teleport to="body">
      <Transition name="modal">
      <div v-if="showAvatarModal" class="modal-overlay" @click.self="showAvatarModal = false">
        <div class="modal">
          <div class="modal-header">
            <h3>修改头像</h3>
            <button class="modal-close" @click="showAvatarModal = false">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/>
                <line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
            </button>
          </div>
          <div class="modal-body">
            <div style="text-align: center; margin-bottom: 16px;">
              <img :src="avatarUrl || defaultAvatar" style="width: 96px; height: 96px; border-radius: 50%; object-fit: cover;" />
            </div>
            <div class="form-group">
              <label>头像链接</label>
              <input v-model="avatarUrl" type="text" class="form-input" placeholder="请输入头像图片 URL" />
              <span class="form-hint">支持任意图片链接，建议正方形图片</span>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="showAvatarModal = false">取消</button>
            <button class="btn btn-primary" @click="saveAvatar">保存</button>
          </div>
        </div>
      </div>
      </Transition>
    </Teleport>

    <!-- 编辑资料弹窗 -->
    <Teleport to="body">
      <Transition name="modal">
      <div v-if="showEditModal" class="modal-overlay" @click.self="showEditModal = false">
        <div class="modal">
          <div class="modal-header">
            <h3>编辑资料</h3>
            <button class="modal-close" @click="showEditModal = false">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/>
                <line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
            </button>
          </div>
          <div class="modal-body">
            <div class="form-group">
              <label>用户名</label>
              <input v-model="editForm.username" type="text" class="form-input" />
            </div>
            <div class="form-group">
              <label>邮箱</label>
              <input v-model="editForm.email" type="email" class="form-input" disabled />
              <span class="form-hint">邮箱暂不支持修改</span>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="showEditModal = false">取消</button>
            <button class="btn btn-primary" @click="saveProfile">保存</button>
          </div>
        </div>
      </div>
      </Transition>
    </Teleport>
    
    <!-- 修改密码弹窗 -->
    <Teleport to="body">
      <Transition name="modal">
      <div v-if="showPasswordModal" class="modal-overlay" @click.self="showPasswordModal = false">
        <div class="modal">
          <div class="modal-header">
            <h3>修改密码</h3>
            <button class="modal-close" @click="showPasswordModal = false">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/>
                <line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
            </button>
          </div>
          <div class="modal-body">
            <div class="form-group">
              <label>当前密码</label>
              <input v-model="passwordForm.current" type="password" class="form-input" placeholder="请输入当前密码" />
            </div>
            <div class="form-group">
              <label>新密码</label>
              <input v-model="passwordForm.new" type="password" class="form-input" placeholder="请输入新密码" />
            </div>
            <div class="form-group">
              <label>确认新密码</label>
              <input v-model="passwordForm.confirm" type="password" class="form-input" placeholder="请再次输入新密码" />
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="showPasswordModal = false">取消</button>
            <button class="btn btn-primary" @click="savePassword">确认修改</button>
          </div>
        </div>
      </div>
      </Transition>
    </Teleport>
    
    <!-- 删除确认弹窗 -->
    <Teleport to="body">
      <Transition name="modal">
      <div v-if="showDeleteModal" class="modal-overlay" @click.self="showDeleteModal = false">
        <div class="modal modal-sm">
          <div class="modal-body text-center">
            <div class="confirm-icon">
              <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/>
                <line x1="12" y1="9" x2="12" y2="13"/>
                <line x1="12" y1="17" x2="12.01" y2="17"/>
              </svg>
            </div>
            <h3>确认删除</h3>
            <p>确定要删除文章「{{ articleToDelete?.title }}」吗？此操作不可恢复。</p>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="showDeleteModal = false">取消</button>
            <button class="btn btn-primary danger" @click="deleteArticle">删除</button>
          </div>
        </div>
      </div>
      </Transition>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { useArticleStore } from '../stores/article'

const router = useRouter()
const userStore = useUserStore()
const articleStore = useArticleStore()

const defaultAvatar = 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA0MCA0MCI+PHJlY3Qgd2lkdGg9IjQwIiBoZWlnaHQ9IjQwIiByeD0iMjAiIGZpbGw9IiNFOEU4RTgiLz48Y2lyY2xlIGN4PSIyMCIgY3k9IjE2IiByPSI4IiBmaWxsPSIjQThBOEE4Ii8+PHBhdGggZD0iTTYgMzVjMC04LjI4NCA2LjcxNi0xNSAxNS0xNXMxNSA2LjcxNiAxNSAxNSIgZmlsbD0iI0E4QThBOCIvPjwvc3ZnPg=='

const activeTab = ref('articles')
const showEditModal = ref(false)
const showPasswordModal = ref(false)
const showDeleteModal = ref(false)
const showAvatarModal = ref(false)
const avatarUrl = ref('')
const articleToDelete = ref<any>(null)

const tabs = [
  { key: 'articles', label: '我的文章', icon: 'FileText', count: 0 },
  { key: 'settings', label: '账户设置', icon: 'Settings' }
]

const editForm = reactive({
  username: '',
  email: ''
})

const passwordForm = reactive({
  current: '',
  new: '',
  confirm: ''
})

const myArticles = computed(() => {
  return (articleStore.articles || []).filter(a => a.user?.id === userStore.user?.id)
})

const totalViews = computed(() => {
  return myArticles.value.reduce((sum, a) => sum + (a.viewCount || 0), 0)
})

const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}

const handleLogout = () => {
  userStore.logout()
  router.push('/')
}

const saveAvatar = async () => {
  try {
    await userStore.updateProfile({ username: userStore.user?.username || '', avatar: avatarUrl.value })
    showAvatarModal.value = false
  } catch (e: any) {
    alert(userStore.error || '保存头像失败')
  }
}

const saveProfile = async () => {
  try {
    await userStore.updateProfile({ username: editForm.username })
    showEditModal.value = false
  } catch (e: any) {
    alert(userStore.error || '保存失败')
  }
}

const savePassword = async () => {
  if (passwordForm.new !== passwordForm.confirm) {
    alert('两次密码不一致')
    return
  }
  try {
    await userStore.changePassword(passwordForm.current, passwordForm.new)
    showPasswordModal.value = false
    passwordForm.current = ''
    passwordForm.new = ''
    passwordForm.confirm = ''
    alert('密码修改成功')
  } catch (e: any) {
    alert(userStore.error || '修改密码失败')
  }
}

const confirmDelete = (article: any) => {
  articleToDelete.value = article
  showDeleteModal.value = true
}

const deleteArticle = async () => {
  if (articleToDelete.value) {
    await articleStore.deleteArticle(articleToDelete.value.id)
    showDeleteModal.value = false
    articleToDelete.value = null
  }
}

onMounted(async () => {
  if (userStore.token) {
    await userStore.getProfile()
    await articleStore.getMyArticles()
    editForm.username = userStore.user?.username || ''
    editForm.email = userStore.user?.email || ''
    avatarUrl.value = userStore.user?.avatar || ''
  }
})
</script>

<style scoped>
.profile-page {
  padding: var(--space-8) 0 var(--space-16);
  min-height: calc(100vh - 64px);
}

.profile-header {
  margin-bottom: var(--space-6);
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

.breadcrumb a {
  color: var(--color-text-secondary);
  text-decoration: none;
}

.breadcrumb a:hover {
  color: var(--color-primary);
}

/* Loading State */
.loading-state {
  padding: var(--space-8) 0;
}

.skeleton-profile {
  display: flex;
  align-items: center;
  gap: var(--space-6);
  padding: var(--space-8);
  background: var(--color-bg-primary);
  border-radius: var(--radius-xl);
}

.skeleton-avatar {
  width: 96px;
  height: 96px;
  border-radius: 50%;
  background: var(--color-bg-tertiary);
}

.skeleton-info {
  flex: 1;
}

.skeleton-name {
  width: 200px;
  height: 28px;
  background: var(--color-bg-tertiary);
  border-radius: var(--radius-md);
  margin-bottom: var(--space-2);
}

.skeleton-email {
  width: 150px;
  height: 18px;
  background: var(--color-bg-hover);
  border-radius: var(--radius-md);
}

/* Profile Card */
.profile-card {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  padding: var(--space-8);
  margin-bottom: var(--space-6);
}

.profile-main {
  display: flex;
  gap: var(--space-6);
}

.avatar-section {
  flex-shrink: 0;
}

.avatar-wrapper {
  position: relative;
  width: 96px;
  height: 96px;
}

.profile-avatar {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid var(--color-bg-secondary);
}

.avatar-edit {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 32px;
  height: 32px;
  background: var(--color-primary);
  border: 3px solid var(--color-bg-primary);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.avatar-edit:hover {
  background: var(--color-primary-dark);
  transform: scale(1.1);
}

.info-section {
  flex: 1;
}

.info-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: var(--space-6);
}

.profile-name {
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-1);
}

.profile-email {
  font-size: var(--text-base);
  color: var(--color-text-tertiary);
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

/* Tabs */
.tabs-section {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  overflow: hidden;
}

.tabs-header {
  display: flex;
  border-bottom: 1px solid var(--color-border-secondary);
  padding: 0 var(--space-4);
}

.tab-btn {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-4) var(--space-6);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-secondary);
  background: none;
  border: none;
  border-bottom: 2px solid transparent;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.tab-btn:hover {
  color: var(--color-text-primary);
}

.tab-btn.active {
  color: var(--color-primary);
  border-bottom-color: var(--color-primary);
}

.tab-count {
  padding: var(--space-1) var(--space-2);
  font-size: var(--text-xs);
  background: var(--color-bg-tertiary);
  border-radius: var(--radius-full);
}

.tabs-content {
  padding: var(--space-6);
}

/* Articles List */
.articles-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.article-item {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: var(--space-4);
  padding: var(--space-4);
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border-secondary);
  border-radius: var(--radius-lg);
  transition: all var(--transition-fast);
}

.article-item:hover {
  border-color: var(--color-border-primary);
}

.article-info {
  flex: 1;
}

.article-header-row {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  margin-bottom: var(--space-2);
}

.article-title a {
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  text-decoration: none;
}

.article-title a:hover {
  color: var(--color-primary);
}

.article-status {
  padding: var(--space-1) var(--space-2);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
  border-radius: var(--radius-full);
}

.article-status.published {
  background: rgba(16, 185, 129, 0.1);
  color: var(--color-success);
}

.article-status.draft {
  background: var(--color-bg-tertiary);
  color: var(--color-text-tertiary);
}

.article-summary {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
  margin-bottom: var(--space-3);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-meta {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: var(--space-3);
}

.meta-item {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

.article-tags-inline {
  display: flex;
  gap: var(--space-1);
}

.mini-tag {
  padding: 2px 8px;
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
  border-radius: var(--radius-full);
}

.article-actions {
  display: flex;
  gap: var(--space-2);
}

.action-btn {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  padding: var(--space-2) var(--space-3);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-lg);
  cursor: pointer;
  color: var(--color-text-secondary);
  transition: all var(--transition-fast);
}

.action-btn:hover {
  background: var(--color-bg-hover);
}

.action-btn.edit:hover {
  color: var(--color-primary);
  border-color: var(--color-primary);
}

.action-btn.delete:hover {
  color: var(--color-error);
  border-color: var(--color-error);
}

/* Settings */
.settings-card {
  max-width: 600px;
}

.settings-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-4);
}

.settings-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.setting-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--space-4);
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border-secondary);
  border-radius: var(--radius-lg);
}

.setting-item.danger {
  border-color: rgba(239, 68, 68, 0.2);
}

.setting-info h4 {
  font-size: var(--text-base);
  font-weight: var(--font-medium);
  color: var(--color-text-primary);
  margin-bottom: var(--space-1);
}

.setting-info p {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

.btn.danger {
  color: var(--color-error);
}

.btn.danger:hover {
  background: rgba(239, 68, 68, 0.1);
}

/* Empty States */
.empty-state,
.empty-tab {
  text-align: center;
  padding: var(--space-12);
}

.empty-icon {
  color: var(--color-text-tertiary);
  opacity: 0.5;
  margin-bottom: var(--space-4);
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

.empty-tab p {
  font-size: var(--text-base);
  color: var(--color-text-tertiary);
  margin-bottom: var(--space-4);
}

/* Modal Transition */
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.25s ease;
}
.modal-enter-active .modal,
.modal-leave-active .modal {
  transition: transform 0.25s ease, opacity 0.25s ease;
}
.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}
.modal-enter-from .modal,
.modal-leave-to .modal {
  transform: translateY(16px) scale(0.97);
  opacity: 0;
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

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--space-5);
  border-bottom: 1px solid var(--color-border-secondary);
}

.modal-header h3 {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
}

.modal-close {
  background: none;
  border: none;
  cursor: pointer;
  color: var(--color-text-tertiary);
  padding: var(--space-1);
  transition: color var(--transition-fast);
}

.modal-close:hover {
  color: var(--color-text-primary);
}

.modal-body {
  padding: var(--space-5);
}

.modal-body.text-center {
  text-align: center;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: var(--space-3);
  padding: var(--space-5);
  border-top: 1px solid var(--color-border-secondary);
}

.confirm-icon {
  color: var(--color-warning);
  margin-bottom: var(--space-4);
}

/* Form */
.form-group {
  margin-bottom: var(--space-4);
}

.form-group label {
  display: block;
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-primary);
  margin-bottom: var(--space-2);
}

.form-input {
  width: 100%;
  padding: var(--space-3) var(--space-4);
  font-size: var(--text-base);
  color: var(--color-text-primary);
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-lg);
  transition: all var(--transition-fast);
}

.form-input:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px rgba(0, 102, 255, 0.15);
}

.form-input:disabled {
  background: var(--color-bg-tertiary);
  cursor: not-allowed;
}

.form-hint {
  display: block;
  font-size: var(--text-xs);
  color: var(--color-text-tertiary);
  margin-top: var(--space-1);
}

/* Responsive */
@media (max-width: 768px) {
  .profile-main {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .info-header {
    flex-direction: column;
    align-items: center;
    gap: var(--space-4);
  }
  
  .profile-stats {
    justify-content: center;
  }
  
  .article-item {
    flex-direction: column;
  }
  
  .article-actions {
    width: 100%;
    justify-content: flex-end;
  }
}
</style>
