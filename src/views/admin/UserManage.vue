<template>
  <div class="page-container">
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">用户管理</h2>
        <p class="page-subtitle">管理系统用户，分配权限</p>
      </div>
      <button class="btn btn-primary" @click="showCreateModal = true">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="12" y1="5" x2="12" y2="19"/>
          <line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        添加用户
      </button>
    </div>
    
    <!-- 筛选栏 -->
    <div class="filter-bar">
      <div class="search-input">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="11" cy="11" r="8"/>
          <path d="m21 21-4.35-4.35"/>
        </svg>
        <input 
          v-model="searchQuery" 
          type="text" 
          placeholder="搜索用户名或邮箱..."
        />
      </div>
    </div>
    
    <!-- 用户列表 -->
    <div class="content-card">
      <div v-if="loading" class="loading-table">
        <div v-for="i in 5" :key="i" class="skeleton-row">
          <div class="skeleton-cell avatar"></div>
          <div class="skeleton-cell name"></div>
          <div class="skeleton-cell email"></div>
          <div class="skeleton-cell role"></div>
          <div class="skeleton-cell date"></div>
          <div class="skeleton-cell actions"></div>
        </div>
      </div>
      
      <div v-else-if="filteredUsers.length > 0" class="table-container">
        <table class="data-table">
          <thead>
            <tr>
              <th class="avatar-col"></th>
              <th>用户名</th>
              <th>邮箱</th>
              <th>角色</th>
              <th>注册时间</th>
              <th class="actions-col">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in filteredUsers" :key="user.id">
              <td class="avatar-col">
                <img :src="user.avatar || defaultAvatar" :alt="user.username" class="user-avatar" />
              </td>
              <td>
                <span class="user-name">{{ user.username }}</span>
              </td>
              <td>{{ user.email }}</td>
              <td>
                <span 
                  class="role-badge"
                  :class="user.is_admin ? 'admin' : 'user'"
                >
                  {{ user.is_admin ? '管理员' : '普通用户' }}
                </span>
              </td>
              <td>{{ formatDate(user.created_at) }}</td>
              <td class="actions-col">
                <div class="action-buttons">
                  <button class="action-btn" @click="editUser(user)" title="编辑">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                      <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
                    </svg>
                  </button>
                  <button class="action-btn danger" @click="confirmDelete(user)" title="删除">
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
            <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
            <circle cx="9" cy="7" r="4"/>
            <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
            <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
          </svg>
        </div>
        <h3>暂无用户</h3>
        <p>还没有任何用户注册</p>
      </div>
      
      <!-- 分页 -->
      <div v-if="filteredUsers.length > 0" class="pagination-bar">
        <div class="pagination-info">共 {{ users.length }} 位用户</div>
        <div class="pagination">
          <button class="pagination-btn" :disabled="currentPage === 1" @click="currentPage--">上一页</button>
          <span class="pagination-current">第 {{ currentPage }} 页</span>
          <button class="pagination-btn" :disabled="currentPage >= totalPages" @click="currentPage++">下一页</button>
        </div>
      </div>
    </div>
    
    <!-- 创建/编辑用户弹窗 -->
    <Teleport to="body">
      <div v-if="showCreateModal || showEditModal" class="modal-overlay" @click.self="closeModal">
        <div class="modal">
          <div class="modal-header">
            <h3>{{ editingUser ? '编辑用户' : '添加用户' }}</h3>
            <button class="modal-close" @click="closeModal">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/>
                <line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
            </button>
          </div>
          <div class="modal-body">
            <div class="form-group">
              <label>用户名</label>
              <input v-model="userForm.username" type="text" class="form-input" placeholder="请输入用户名" />
            </div>
            <div class="form-group">
              <label>邮箱</label>
              <input v-model="userForm.email" type="email" class="form-input" placeholder="请输入邮箱" />
            </div>
            <div class="form-group" v-if="!editingUser">
              <label>密码</label>
              <input v-model="userForm.password" type="password" class="form-input" placeholder="请输入密码" />
            </div>
            <div class="form-group">
              <label class="checkbox-label">
                <input v-model="userForm.is_admin" type="checkbox" />
                <span>设为管理员</span>
              </label>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="closeModal">取消</button>
            <button class="btn btn-primary" @click="saveUser">保存</button>
          </div>
        </div>
      </div>
    </Teleport>
    
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
            <p>确定要删除用户「{{ userToDelete?.username }}」吗？此操作不可恢复。</p>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="showDeleteModal = false">取消</button>
            <button class="btn btn-primary danger" @click="deleteUser">删除</button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, reactive, onMounted } from 'vue'

const loading = ref(false)
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const showCreateModal = ref(false)
const showEditModal = ref(false)
const showDeleteModal = ref(false)
const editingUser = ref<any>(null)
const userToDelete = ref<any>(null)

const defaultAvatar = 'data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCA0MCA0MCI+PHJlY3Qgd2lkdGg9IjQwIiBoZWlnaHQ9IjQwIiByeD0iMjAiIGZpbGw9IiNFOEU4RTgiLz48Y2lyY2xlIGN4PSIyMCIgY3k9IjE2IiByPSI4IiBmaWxsPSIjQThBOEE4Ii8+PHBhdGggZD0iTTYgMzVjMC04LjI4NCA2LjcxNi0xNSAxNS0xNXMxNSA2LjcxNiAxNSAxNSIgZmlsbD0iI0E4QThBOCIvPjwvc3ZnPg=='

const userForm = reactive({
  username: '',
  email: '',
  password: '',
  is_admin: false
})

// 模拟用户数据
const users = ref([
  { id: 1, username: 'admin', email: 'admin@example.com', is_admin: true, avatar: '', created_at: '2024-01-01T00:00:00Z' },
  { id: 2, username: 'user1', email: 'user1@example.com', is_admin: false, avatar: '', created_at: '2024-01-02T10:00:00Z' },
  { id: 3, username: 'user2', email: 'user2@example.com', is_admin: false, avatar: '', created_at: '2024-01-03T15:30:00Z' },
  { id: 4, username: 'editor', email: 'editor@example.com', is_admin: false, avatar: '', created_at: '2024-01-04T09:00:00Z' }
])

const filteredUsers = computed(() => {
  let result = users.value
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(u => 
      u.username.toLowerCase().includes(query) ||
      u.email.toLowerCase().includes(query)
    )
  }
  return result
})

const totalPages = computed(() => Math.ceil(filteredUsers.value.length / pageSize.value))

const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}

const editUser = (user: any) => {
  editingUser.value = user
  userForm.username = user.username
  userForm.email = user.email
  userForm.is_admin = user.is_admin
  showEditModal.value = true
}

const confirmDelete = (user: any) => {
  userToDelete.value = user
  showDeleteModal.value = true
}

const closeModal = () => {
  showCreateModal.value = false
  showEditModal.value = false
  editingUser.value = null
  userForm.username = ''
  userForm.email = ''
  userForm.password = ''
  userForm.is_admin = false
}

const saveUser = () => {
  if (editingUser.value) {
    const index = users.value.findIndex(u => u.id === editingUser.value.id)
    if (index > -1) {
      users.value[index] = { ...users.value[index], ...userForm }
    }
  } else {
    users.value.push({
      id: Date.now(),
      ...userForm,
      avatar: '',
      created_at: new Date().toISOString()
    })
  }
  closeModal()
}

const deleteUser = () => {
  if (userToDelete.value) {
    users.value = users.value.filter(u => u.id !== userToDelete.value.id)
    showDeleteModal.value = false
    userToDelete.value = null
  }
}

onMounted(() => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
  }, 500)
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
  flex-wrap: wrap;
  gap: var(--space-4);
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
  margin-bottom: var(--space-4);
}

.search-input {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  max-width: 400px;
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

.avatar-col {
  width: 60px;
}

.actions-col {
  width: 100px;
  text-align: right;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.user-name {
  font-weight: var(--font-medium);
  color: var(--color-text-primary);
}

.role-badge {
  display: inline-flex;
  padding: var(--space-1) var(--space-3);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
  border-radius: var(--radius-full);
}

.role-badge.admin {
  background: rgba(0, 102, 255, 0.1);
  color: var(--color-primary);
}

.role-badge.user {
  background: var(--color-bg-tertiary);
  color: var(--color-text-tertiary);
}

.action-buttons {
  display: flex;
  justify-content: flex-end;
  gap: var(--space-1);
}

.action-btn {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
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

/* Loading Skeleton */
.loading-table {
  padding: var(--space-4);
}

.skeleton-row {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  padding: var(--space-3) 0;
  border-bottom: 1px solid var(--color-border-secondary);
}

.skeleton-cell {
  height: 20px;
  background: var(--color-bg-tertiary);
  border-radius: var(--radius-md);
}

.skeleton-cell.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.skeleton-cell.name {
  width: 100px;
}

.skeleton-cell.email {
  width: 180px;
}

.skeleton-cell.role {
  width: 80px;
}

.skeleton-cell.date {
  width: 100px;
}

.skeleton-cell.actions {
  width: 80px;
  margin-left: auto;
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
  padding: var(--space-4) var(--space-5);
  border-top: 1px solid var(--color-border-secondary);
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

.checkbox-label {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  cursor: pointer;
}

.checkbox-label input[type="checkbox"] {
  width: 18px;
  height: 18px;
  cursor: pointer;
}

.checkbox-label span {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
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
