<template>
  <div class="page-container">
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">{{ isEdit ? '编辑文章' : '发布文章' }}</h2>
        <p class="page-subtitle">{{ isEdit ? '修改文章内容' : '创作新的精彩内容' }}</p>
      </div>
      <div class="header-actions">
        <button class="btn btn-secondary" @click="$router.back()">取消</button>
        <button class="btn btn-secondary" @click="handleSaveDraft" :disabled="saving">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"/>
            <polyline points="17 21 17 13 7 13 7 21"/>
            <polyline points="7 3 7 8 15 8"/>
          </svg>
          保存草稿
        </button>
        <button class="btn btn-primary" @click="handlePublish" :disabled="saving">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="20 6 9 17 4 12"/>
          </svg>
          {{ isEdit ? '更新文章' : '发布文章' }}
        </button>
      </div>
    </div>
    
    <div class="editor-layout">
      <!-- 左侧编辑区 -->
      <div class="editor-main">
        <div class="editor-card">
          <div class="form-group">
            <label>文章标题</label>
            <input 
              v-model="form.title" 
              type="text" 
              class="form-input title-input"
              placeholder="请输入文章标题..."
            />
          </div>
          
          <div class="form-group">
            <label>文章摘要</label>
            <textarea 
              v-model="form.summary" 
              class="form-input"
              rows="3"
              placeholder="请输入文章摘要，用于列表展示..."
            ></textarea>
          </div>
          
          <div class="form-group">
            <label>文章内容</label>
            <div class="markdown-editor">
              <div class="editor-toolbar">
                <button 
                  v-for="tool in toolbarTools" 
                  :key="tool.key"
                  class="toolbar-btn"
                  :title="tool.title"
                  @click="insertMarkdown(tool.prefix, tool.suffix)"
                >
                  <component :is="tool.icon" />
                </button>
              </div>
              <textarea 
                v-model="form.content" 
                class="editor-textarea"
                placeholder="开始写作..."
                @keydown="handleKeydown"
              ></textarea>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 右侧设置区 -->
      <div class="editor-sidebar">
        <div class="sidebar-card">
          <h3 class="sidebar-title">发布设置</h3>
          
          <div class="setting-group">
            <label>文章状态</label>
            <div class="status-options">
              <button 
                class="status-option"
                :class="{ active: form.status === 'PUBLISHED' }"
                @click="form.status = 'PUBLISHED'"
              >
                <span class="status-dot published"></span>
                发布
              </button>
              <button 
                class="status-option"
                :class="{ active: form.status === 'DRAFT' }"
                @click="form.status = 'DRAFT'"
              >
                <span class="status-dot draft"></span>
                草稿
              </button>
            </div>
          </div>
          
          <div class="setting-group">
            <label>标签</label>
            <div class="tags-select">
              <label 
                v-for="tag in tagStore.tags" 
                :key="tag.id"
                class="tag-option"
                :style="{ 
                  borderColor: selectedTags.includes(tag.id) ? tag.color : 'transparent',
                  backgroundColor: selectedTags.includes(tag.id) ? tag.color + '15' : 'var(--color-bg-tertiary)'
                }"
              >
                <input 
                  type="checkbox" 
                  :value="tag.id"
                  v-model="selectedTags"
                  class="tag-checkbox"
                />
                <span class="tag-dot" :style="{ backgroundColor: tag.color }"></span>
                <span class="tag-name" :style="{ color: selectedTags.includes(tag.id) ? tag.color : 'var(--color-text-secondary)' }">
                  {{ tag.name }}
                </span>
              </label>
            </div>
          </div>
        </div>
        
        <div class="sidebar-card">
          <h3 class="sidebar-title">实时预览</h3>
          <div class="preview-toggle">
            <button 
              class="toggle-btn"
              :class="{ active: showPreview }"
              @click="showPreview = true"
            >
              预览
            </button>
            <button 
              class="toggle-btn"
              :class="{ active: !showPreview }"
              @click="showPreview = false"
            >
              编辑
            </button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 预览面板 -->
    <div v-if="showPreview" class="preview-panel">
      <div class="preview-header">
        <h3>文章预览</h3>
        <button class="preview-close" @click="showPreview = false">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="18" y1="6" x2="6" y2="18"/>
            <line x1="6" y1="6" x2="18" y2="18"/>
          </svg>
        </button>
      </div>
      <div class="preview-content">
        <h1 class="preview-title">{{ form.title || '无标题' }}</h1>
        <div class="preview-tags" v-if="selectedTags.length > 0">
          <span 
            v-for="tagId in selectedTags" 
            :key="tagId"
            class="preview-tag"
            :style="{ 
              backgroundColor: getTagById(tagId)?.color + '20',
              color: getTagById(tagId)?.color
            }"
          >
            {{ getTagById(tagId)?.name }}
          </span>
        </div>
        <p class="preview-summary">{{ form.summary }}</p>
        <div class="preview-body prose" v-html="renderedContent"></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, h, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useArticleStore } from '../../stores/article'
import { useTagStore } from '../../stores/tag'
import { marked } from 'marked'
import { useToast } from '../../composables/useToast'

const route = useRoute()
const router = useRouter()
const articleStore = useArticleStore()
const tagStore = useTagStore()
const toast = useToast()

const isEdit = computed(() => !!route.params.id)
const articleId = computed(() => Number(route.params.id))

const saving = ref(false)
const showPreview = ref(false)
const autoSaveStatus = ref('')
const lastSaved = ref<Date | null>(null)

const form = ref({
  title: '',
  content: '',
  summary: '',
  status: 'DRAFT' as 'PUBLISHED' | 'DRAFT'
})

const selectedTags = ref<number[]>([])

// 自动保存
let autoSaveTimer: number | null = null

const startAutoSave = () => {
  if (autoSaveTimer) clearInterval(autoSaveTimer)
  autoSaveTimer = window.setInterval(() => {
    if (form.value.title || form.value.content) {
      saveToLocal()
    }
  }, 30000) // 每30秒自动保存
}

const saveToLocal = () => {
  const draftKey = `article_draft_${isEdit.value ? articleId.value : 'new'}`
  const data = {
    title: form.value.title,
    content: form.value.content,
    summary: form.value.summary,
    tags: selectedTags.value,
    savedAt: new Date().toISOString()
  }
  localStorage.setItem(draftKey, JSON.stringify(data))
  lastSaved.value = new Date()
  autoSaveStatus.value = '已自动保存'
  
  setTimeout(() => {
    autoSaveStatus.value = ''
  }, 3000)
}

const loadFromLocal = () => {
  const draftKey = `article_draft_${isEdit.value ? articleId.value : 'new'}`
  const saved = localStorage.getItem(draftKey)
  if (saved) {
    try {
      const data = JSON.parse(saved)
      const savedTime = new Date(data.savedAt)
      const now = new Date()
      const diff = Math.floor((now.getTime() - savedTime.getTime()) / 1000 / 60)
      
      if (diff < 60 && (data.title || data.content)) {
        if (confirm(`发现 ${diff} 分钟前的草稿，是否恢复？`)) {
          form.value.title = data.title || ''
          form.value.content = data.content || ''
          form.value.summary = data.summary || ''
          selectedTags.value = data.tags || []
        }
      }
    } catch (e) {
      // ignore
    }
  }
}

const clearLocalDraft = () => {
  const draftKey = `article_draft_${isEdit.value ? articleId.value : 'new'}`
  localStorage.removeItem(draftKey)
}

// 监听表单变化，标记为未保存
watch(form, () => {
  if (!autoSaveStatus.value.includes('保存')) {
    autoSaveStatus.value = '未保存'
  }
}, { deep: true })

const toolbarTools = [
  { key: 'bold', title: '粗体', prefix: '**', suffix: '**', icon: () => h('b', null, 'B') },
  { key: 'italic', title: '斜体', prefix: '*', suffix: '*', icon: () => h('i', null, 'I') },
  { key: 'heading', title: '标题', prefix: '## ', suffix: '', icon: () => h('span', null, 'H') },
  { key: 'quote', title: '引用', prefix: '> ', suffix: '', icon: () => h('span', null, '"') },
  { key: 'code', title: '代码', prefix: '`', suffix: '`', icon: () => h('span', null, '</>') },
  { key: 'link', title: '链接', prefix: '[', suffix: '](url)', icon: () => h('span', null, '🔗') },
  { key: 'image', title: '图片', prefix: '![alt](', suffix: ')', icon: () => h('span', null, '🖼️') },
  { key: 'list', title: '列表', prefix: '- ', suffix: '', icon: () => h('span', null, '•') },
]

const renderedContent = computed(() => {
  return marked(form.value.content)
})

const getTagById = (id: number) => {
  return tagStore.tags.find(t => t.id === id)
}

const insertMarkdown = (prefix: string, suffix: string) => {
  const textarea = document.querySelector('.editor-textarea') as HTMLTextAreaElement
  if (!textarea) return
  
  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  const text = form.value.content
  const before = text.substring(0, start)
  const selection = text.substring(start, end)
  const after = text.substring(end)
  
  form.value.content = before + prefix + selection + suffix + after
  
  setTimeout(() => {
    textarea.focus()
    textarea.setSelectionRange(start + prefix.length, end + prefix.length)
  }, 0)
}

const handleKeydown = (e: KeyboardEvent) => {
  if (e.key === 'Tab') {
    e.preventDefault()
    insertMarkdown('  ', '')
  }
  if ((e.metaKey || e.ctrlKey) && e.key === 's') {
    e.preventDefault()
    handleSaveDraft()
  }
}

const handleSaveDraft = async () => {
  saving.value = true
  try {
    const articleData = {
      ...form.value,
      tags: selectedTags.value.map(id => getTagById(id)).filter(Boolean) as any
    }
    
    if (isEdit.value) {
      await articleStore.saveDraft(articleId.value, articleData)
    } else {
      await articleStore.createArticle(articleData)
    }
    clearLocalDraft()
    toast.success('草稿已保存')
  } catch (error) {
    toast.error('保存失败，请重试')
  } finally {
    saving.value = false
  }
}

const handlePublish = async () => {
  if (!form.value.title.trim()) {
    toast.warning('请输入文章标题')
    return
  }
  
  saving.value = true
  try {
    form.value.status = 'PUBLISHED'
    const articleData = {
      ...form.value,
      tags: selectedTags.value.map(id => getTagById(id)).filter(Boolean) as any
    }
    
    if (isEdit.value) {
      await articleStore.updateArticle(articleId.value, articleData)
    } else {
      await articleStore.createArticle(articleData)
    }
    clearLocalDraft()
    toast.success('文章发布成功')
    router.push('/admin/articles')
  } catch (error) {
    toast.error('发布失败，请重试')
  } finally {
    saving.value = false
  }
}

onMounted(async () => {
  await tagStore.getTags()
  
  if (isEdit.value) {
    const article = await articleStore.getArticleById(articleId.value)
    if (article) {
      form.value.title = article.title
      form.value.content = article.content
      form.value.summary = article.summary
      form.value.status = article.status
      selectedTags.value = article.tags.map((t: any) => t.id)
    }
  } else {
    // 新建文章时检查本地草稿
    loadFromLocal()
  }
  
  // 启动自动保存
  startAutoSave()
})

onUnmounted(() => {
  if (autoSaveTimer) {
    clearInterval(autoSaveTimer)
  }
})
</script>

<style scoped>
.page-container {
  max-width: 1400px;
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

.header-actions {
  display: flex;
  gap: var(--space-3);
}

/* Editor Layout */
.editor-layout {
  display: grid;
  grid-template-columns: 1fr 320px;
  gap: var(--space-6);
}

@media (max-width: 1024px) {
  .editor-layout {
    grid-template-columns: 1fr;
  }
}

/* Editor Main */
.editor-main {
  min-width: 0;
}

.editor-card {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  padding: var(--space-6);
}

.form-group {
  margin-bottom: var(--space-6);
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

.title-input {
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
}

/* Markdown Editor */
.markdown-editor {
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-lg);
  overflow: hidden;
}

.editor-toolbar {
  display: flex;
  gap: var(--space-1);
  padding: var(--space-2);
  background: var(--color-bg-tertiary);
  border-bottom: 1px solid var(--color-border-primary);
}

.toolbar-btn {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-md);
  cursor: pointer;
  color: var(--color-text-secondary);
  font-size: var(--text-sm);
  transition: all var(--transition-fast);
}

.toolbar-btn:hover {
  background: var(--color-bg-hover);
  color: var(--color-text-primary);
}

.editor-textarea {
  width: 100%;
  min-height: 400px;
  padding: var(--space-4);
  font-family: var(--font-mono);
  font-size: var(--text-sm);
  line-height: 1.6;
  color: var(--color-text-primary);
  background: var(--color-bg-secondary);
  border: none;
  resize: vertical;
  outline: none;
}

/* Sidebar */
.editor-sidebar {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.sidebar-card {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  padding: var(--space-5);
}

.sidebar-title {
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-4);
}

.setting-group {
  margin-bottom: var(--space-4);
}

.setting-group label {
  display: block;
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-secondary);
  margin-bottom: var(--space-3);
}

.status-options {
  display: flex;
  gap: var(--space-2);
}

.status-option {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-2);
  padding: var(--space-3);
  background: var(--color-bg-secondary);
  border: 2px solid transparent;
  border-radius: var(--radius-lg);
  cursor: pointer;
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-secondary);
  transition: all var(--transition-fast);
}

.status-option:hover {
  background: var(--color-bg-hover);
}

.status-option.active {
  border-color: var(--color-primary);
  background: rgba(0, 102, 255, 0.05);
  color: var(--color-primary);
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.status-dot.published {
  background: var(--color-success);
}

.status-dot.draft {
  background: var(--color-text-tertiary);
}

.tags-select {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
}

.tag-option {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-3);
  border: 2px solid transparent;
  border-radius: var(--radius-full);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.tag-option:hover {
  border-color: var(--color-border-primary);
}

.tag-checkbox {
  display: none;
}

.tag-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.tag-name {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
}

.preview-toggle {
  display: flex;
  gap: var(--space-1);
  background: var(--color-bg-secondary);
  padding: var(--space-1);
  border-radius: var(--radius-lg);
}

.toggle-btn {
  flex: 1;
  padding: var(--space-2);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  background: transparent;
  border: none;
  border-radius: var(--radius-md);
  cursor: pointer;
  color: var(--color-text-secondary);
  transition: all var(--transition-fast);
}

.toggle-btn.active {
  background: var(--color-bg-primary);
  color: var(--color-text-primary);
  box-shadow: var(--shadow-sm);
}

/* Preview Panel */
.preview-panel {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  width: 50%;
  max-width: 700px;
  background: var(--color-bg-primary);
  border-left: 1px solid var(--color-border-primary);
  box-shadow: var(--shadow-2xl);
  z-index: var(--z-modal);
  display: flex;
  flex-direction: column;
}

.preview-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--space-4) var(--space-6);
  border-bottom: 1px solid var(--color-border-secondary);
}

.preview-header h3 {
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
}

.preview-close {
  background: none;
  border: none;
  cursor: pointer;
  color: var(--color-text-tertiary);
  padding: var(--space-1);
  transition: color var(--transition-fast);
}

.preview-close:hover {
  color: var(--color-text-primary);
}

.preview-content {
  flex: 1;
  padding: var(--space-6);
  overflow-y: auto;
}

.preview-title {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-4);
}

.preview-tags {
  display: flex;
  gap: var(--space-2);
  margin-bottom: var(--space-4);
}

.preview-tag {
  padding: var(--space-1) var(--space-3);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  border-radius: var(--radius-full);
}

.preview-summary {
  font-size: var(--text-base);
  color: var(--color-text-secondary);
  margin-bottom: var(--space-6);
  padding-bottom: var(--space-6);
  border-bottom: 1px solid var(--color-border-secondary);
}

.preview-body {
  font-size: var(--text-base);
  line-height: 1.8;
}

/* Responsive */
@media (max-width: 1024px) {
  .preview-panel {
    width: 100%;
    max-width: none;
  }
}
</style>
