<template>
  <div class="page-container">
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">标签管理</h2>
        <p class="page-subtitle">管理文章标签，方便内容分类</p>
      </div>
      <button class="btn btn-primary" @click="showCreateModal = true">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="12" y1="5" x2="12" y2="19"/>
          <line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        创建标签
      </button>
    </div>
    
    <div class="content-card">
      <div v-if="tagStore.loading" class="loading-grid">
        <div v-for="i in 6" :key="i" class="skeleton-card">
          <div class="skeleton-header">
            <div class="skeleton-dot"></div>
            <div class="skeleton-name"></div>
          </div>
          <div class="skeleton-count"></div>
        </div>
      </div>
      
      <div v-else-if="tagStore.tags.length > 0" class="tags-grid">
        <div 
          v-for="tag in tagStore.tags" 
          :key="tag.id"
          class="tag-card"
        >
          <div class="tag-header">
            <div class="tag-icon" :style="{ backgroundColor: tag.color }">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
                <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"/>
                <line x1="7" y1="7" x2="7.01" y2="7"/>
              </svg>
            </div>
            <div class="tag-info">
              <h3 class="tag-name">{{ tag.name }}</h3>
              <p class="tag-count">{{ tag.count || 0 }} 篇文章</p>
            </div>
          </div>
          
          <div class="tag-actions">
            <button class="action-btn" @click="editTag(tag)" title="编辑">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
              </svg>
            </button>
            <button class="action-btn danger" @click="confirmDelete(tag)" title="删除">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="3 6 5 6 21 6"/>
                <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
              </svg>
            </button>
          </div>
        </div>
      </div>
      
      <div v-else class="empty-state">
        <div class="empty-icon">
          <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"/>
            <line x1="7" y1="7" x2="7.01" y2="7"/>
          </svg>
        </div>
        <h3>暂无标签</h3>
        <p>创建第一个标签开始分类管理文章</p>
        <button class="btn btn-primary" @click="showCreateModal = true">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="12" y1="5" x2="12" y2="19"/>
            <line x1="5" y1="12" x2="19" y2="12"/>
          </svg>
          创建标签
        </button>
      </div>
    </div>
    
    <!-- 创建/编辑标签弹窗 -->
    <Teleport to="body">
      <div v-if="showCreateModal || showEditModal" class="modal-overlay" @click.self="closeModal">
        <div class="modal">
          <div class="modal-header">
            <h3>{{ editingTag ? '编辑标签' : '创建标签' }}</h3>
            <button class="modal-close" @click="closeModal">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/>
                <line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
            </button>
          </div>
          <div class="modal-body">
            <div class="form-group">
              <label>标签名称</label>
              <input 
                v-model="tagForm.name" 
                type="text" 
                class="form-input"
                placeholder="请输入标签名称"
              />
            </div>
            <div class="form-group">
              <label>标签颜色</label>
              <div class="color-picker">
                <button
                  v-for="color in presetColors"
                  :key="color"
                  class="color-option"
                  :class="{ active: tagForm.color === color }"
                  :style="{ backgroundColor: color }"
                  @click="tagForm.color = color"
                ></button>
                <input 
                  v-model="tagForm.color" 
                  type="color" 
                  class="color-input"
                />
              </div>
            </div>
            <div class="color-preview" :style="{ backgroundColor: tagForm.color + '20', borderColor: tagForm.color }">
              <span class="preview-dot" :style="{ backgroundColor: tagForm.color }"></span>
              <span class="preview-name" :style="{ color: tagForm.color }">
                {{ tagForm.name || '标签预览' }}
              </span>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="closeModal">取消</button>
            <button class="btn btn-primary" @click="saveTag" :disabled="!tagForm.name.trim()">
              {{ editingTag ? '保存' : '创建' }}
            </button>
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
            <p>确定要删除标签「{{ tagToDelete?.name }}」吗？此操作不可恢复。</p>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="showDeleteModal = false">取消</button>
            <button class="btn btn-primary danger" @click="deleteTag">删除</button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useTagStore } from '../../stores/tag'

const tagStore = useTagStore()

const showCreateModal = ref(false)
const showEditModal = ref(false)
const showDeleteModal = ref(false)
const editingTag = ref<any>(null)
const tagToDelete = ref<any>(null)

const presetColors = [
  '#0066FF', '#8B5CF6', '#EC4899', '#EF4444', '#F59E0B',
  '#10B981', '#06B6D4', '#6366F1', '#84CC16', '#F97316'
]

const tagForm = reactive({
  name: '',
  color: '#0066FF'
})

const editTag = (tag: any) => {
  editingTag.value = tag
  tagForm.name = tag.name
  tagForm.color = tag.color
  showEditModal.value = true
}

const confirmDelete = (tag: any) => {
  tagToDelete.value = tag
  showDeleteModal.value = true
}

const closeModal = () => {
  showCreateModal.value = false
  showEditModal.value = false
  editingTag.value = null
  tagForm.name = ''
  tagForm.color = '#0066FF'
}

const saveTag = async () => {
  if (!tagForm.name.trim()) return
  
  if (editingTag.value) {
    await tagStore.updateTag(editingTag.value.id, {
      name: tagForm.name,
      color: tagForm.color
    })
  } else {
    await tagStore.createTag({
      name: tagForm.name,
      color: tagForm.color
    })
  }
  closeModal()
}

const deleteTag = async () => {
  if (tagToDelete.value) {
    await tagStore.deleteTag(tagToDelete.value.id)
    showDeleteModal.value = false
    tagToDelete.value = null
  }
}

onMounted(() => {
  tagStore.getTags()
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

.content-card {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  padding: var(--space-6);
}

/* Tags Grid */
.tags-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: var(--space-4);
}

.tag-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--space-4);
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border-secondary);
  border-radius: var(--radius-xl);
  transition: all var(--transition-fast);
}

.tag-card:hover {
  border-color: var(--color-border-primary);
  box-shadow: var(--shadow-md);
}

.tag-header {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.tag-icon {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
}

.tag-info h3 {
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
}

.tag-count {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

.tag-actions {
  display: flex;
  gap: var(--space-1);
}

.action-btn {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
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
  border-color: var(--color-error);
}

/* Loading Skeleton */
.loading-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: var(--space-4);
}

.skeleton-card {
  padding: var(--space-4);
  background: var(--color-bg-secondary);
  border-radius: var(--radius-xl);
}

.skeleton-header {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  margin-bottom: var(--space-3);
}

.skeleton-dot {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-lg);
  background: var(--color-bg-tertiary);
}

.skeleton-name {
  width: 100px;
  height: 16px;
  background: var(--color-bg-tertiary);
  border-radius: var(--radius-md);
}

.skeleton-count {
  width: 60px;
  height: 12px;
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
  margin-bottom: var(--space-4);
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

/* Color Picker */
.color-picker {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
}

.color-option {
  width: 36px;
  height: 36px;
  border-radius: var(--radius-lg);
  border: 3px solid transparent;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.color-option:hover {
  transform: scale(1.1);
}

.color-option.active {
  border-color: var(--color-text-primary);
}

.color-input {
  width: 36px;
  height: 36px;
  padding: 0;
  border: none;
  border-radius: var(--radius-lg);
  cursor: pointer;
}

.color-preview {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-3) var(--space-4);
  border: 2px solid;
  border-radius: var(--radius-lg);
  margin-top: var(--space-4);
}

.preview-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
}

.preview-name {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
}

.confirm-icon.warning {
  color: var(--color-warning);
}

.btn.danger {
  background: var(--color-error);
  border-color: var(--color-error);
}
</style>
