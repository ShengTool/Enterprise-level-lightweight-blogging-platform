<template>
  <div class="page-container">
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">{{ isEdit ? '编辑文章' : '发布文章' }}</h2>
        <p class="page-subtitle">{{ isEdit ? '修改已发布的文章内容' : '创作你的精彩内容并分享给读者' }}</p>
      </div>
      <div class="header-actions">
        <button class="btn btn-secondary" @click="handleCancel">取消</button>
        <button class="btn btn-secondary" @click="handleSaveDraft" :disabled="saving">
          保存草稿
        </button>
        <button class="btn btn-primary" @click="handlePublish" :disabled="saving">
          {{ isEdit ? '更新发布' : '发布文章' }}
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
              placeholder="给文章起个标题..."
            />
          </div>

          <div class="form-group">
            <label>文章摘要</label>
            <textarea
              v-model="form.summary"
              class="form-input"
              rows="3"
              placeholder="简要描述文章内容，帮助读者快速了解..."
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
                placeholder="在这里开始写作，支持 Markdown 语法..."
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
            <label>选择标签</label>
            <div class="tags-select">
              <label
                v-for="tag in tagStore.tags"
                :key="tag.id"
                class="tag-option"
                :style="{
                  borderColor: selectedTags.includes(tag.id) ? tag.color : 'transparent',
                  backgroundColor: selectedTags.includes(tag.id) ? tag.color + '18' : 'var(--color-bg-secondary)'
                }"
              >
                <input
                  type="checkbox"
                  :value="tag.id"
                  v-model="selectedTags"
                  class="tag-checkbox"
                />
                <span class="tag-dot" :style="{ backgroundColor: tag.color }"></span>
                <span
                  class="tag-name"
                  :style="{
                    color: selectedTags.includes(tag.id) ? tag.color : 'var(--color-text-secondary)'
                  }"
                >
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
              :class="{ active: !showPreview }"
              @click="showPreview = false"
            >
              编辑
            </button>
            <button
              class="toggle-btn"
              :class="{ active: showPreview }"
              @click="showPreview = true"
            >
              预览
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
import { useArticleStore } from '../stores/article'
import { useTagStore } from '../stores/tag'
import { marked } from 'marked'
import { useToast } from '../composables/useToast'

const route = useRoute()
const router = useRouter()
const articleStore = useArticleStore()
const tagStore = useTagStore()
const toast = useToast()

const isEdit = computed(() => !!route.params.id)
const articleId = computed(() => Number(route.params.id))

const saving = ref(false)
const showPreview = ref(false)
const lastSaved = ref<Date | null>(null)

const form = ref({
  title: '',
  content: '',
  summary: '',
  status: 'DRAFT' as 'PUBLISHED' | 'DRAFT'
})

const selectedTags = ref<number[]>([])

// 自动保存到本地
let autoSaveTimer: number | null = null

const startAutoSave = () => {
  if (autoSaveTimer) clearInterval(autoSaveTimer)
  autoSaveTimer = window.setInterval(() => {
    if (form.value.title || form.value.content) {
      saveToLocal()
    }
  }, 30000)
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
  toast.success('草稿已保存', { duration: 1500 })
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

const renderedContent = computed(() => marked(form.value.content))

const getTagById = (id: number) => tagStore.tags.find(t => t.id === id)

const insertMarkdown = (prefix: string, suffix: string) => {
  const textarea = document.querySelector('.editor-textarea') as HTMLTextAreaElement
  if (!textarea) return
  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  const text = form.value.content
  form.value.content = text.substring(0, start) + prefix + text.substring(start, end) + suffix + text.substring(end)
  setTimeout(() => {
    textarea.focus()
    textarea.setSelectionRange(start + prefix.length, end + prefix.length)
  }, 0)
}

const handleKeydown = (e: KeyboardEvent) => {
  if (e.key === 'Tab') { e.preventDefault(); insertMarkdown('  ', '') }
  if ((e.metaKey || e.ctrlKey) && e.key === 's') { e.preventDefault(); handleSaveDraft() }
}

const handleSaveDraft = async () => {
  saving.value = true
  try {
    const articleData = buildArticleData()
    if (isEdit.value) {
      await articleStore.saveDraft(articleId.value, articleData)
    } else {
      // 新建草稿走 createArticle（后端强制PUBLISHED，仅做本地保存）
      await articleStore.createArticle(articleData)
    }
    clearLocalDraft()
    toast.success('草稿已保存')
    router.push('/profile')
  } catch (error: any) {
    toast.error('保存失败：' + (error.response?.data?.message || error.message))
  } finally {
    saving.value = false
  }
}

const handlePublish = async () => {
  if (!form.value.title.trim()) {
    toast.warning('请输入文章标题')
    return
  }
  if (!form.value.content.trim()) {
    toast.warning('请输入文章内容')
    return
  }

  saving.value = true
  try {
    const articleData = buildArticleData()
    articleData.status = 'PUBLISHED'

    if (isEdit.value) {
      await articleStore.updateArticle(articleId.value, articleData)
    } else {
      await articleStore.createArticle(articleData)
    }
    clearLocalDraft()
    toast.success('文章发布成功！')
    router.push('/profile')
  } catch (error: any) {
    toast.error('发布失败：' + (error.response?.data?.message || error.message))
  } finally {
    saving.value = false
  }
}

const buildArticleData = () => ({
  title: form.value.title,
  content: form.value.content,
  summary: form.value.summary,
  status: form.value.status as 'PUBLISHED' | 'DRAFT',
  tags: selectedTags.value.map(id => getTagById(id)).filter(Boolean) as any
})

const handleCancel = () => {
  router.back()
}

onMounted(async () => {
  // 未登录则跳转登录
  const { useUserStore } = await import('../stores/user')
  const userStore = useUserStore()
  if (!userStore.token) {
    router.push('/login')
    return
  }

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
    loadFromLocal()
  }

  startAutoSave()
})

onUnmounted(() => {
  if (autoSaveTimer) clearInterval(autoSaveTimer)
})
</script>

<style scoped>
.page-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem 1.5rem;
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

.status-dot.published { background: var(--color-success); }
.status-dot.draft { background: var(--color-text-tertiary); }

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
  border-color: var(--color-border-primary) !important;
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

@media (max-width: 1024px) {
  .preview-panel {
    width: 100%;
    max-width: none;
  }
}
</style>
