<template>
  <div class="markdown-editor">
    <div class="editor-toolbar">
      <div class="toolbar-group">
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
      <div class="toolbar-divider"></div>
      <div class="toolbar-group">
        <button class="toolbar-btn" title="预览" @click="togglePreview">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
            <circle cx="12" cy="12" r="3"/>
          </svg>
        </button>
        <button class="toolbar-btn" title="全屏" @click="toggleFullscreen">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M8 3H5a2 2 0 0 0-2 2v3m18 0V5a2 2 0 0 0-2-2h-3m0 18h3a2 2 0 0 0 2-2v-3M3 16v3a2 2 0 0 0 2 2h3"/>
          </svg>
        </button>
      </div>
    </div>
    
    <div class="editor-container" :class="{ 'preview-mode': showPreview, fullscreen: isFullscreen }">
      <div class="editor-pane">
        <textarea 
          ref="textareaRef"
          v-model="content" 
          class="editor-textarea"
          placeholder="开始写作...支持 Markdown 语法"
          @keydown="handleKeydown"
        ></textarea>
      </div>
      
      <div v-if="showPreview" class="preview-pane">
        <div class="preview-header">
          <span>预览</span>
        </div>
        <div class="preview-content prose" v-html="renderedContent"></div>
      </div>
    </div>
    
    <div class="editor-footer">
      <span class="word-count">{{ wordCount }} 字</span>
      <span class="save-status">{{ saveStatus }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, h } from 'vue'
import { marked } from 'marked'
import DOMPurify from 'dompurify'

const props = defineProps<{
  modelValue: string
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void
}>()

const content = ref(props.modelValue)
const showPreview = ref(false)
const isFullscreen = ref(false)
const saveStatus = ref('已保存')
const textareaRef = ref<HTMLTextAreaElement>()

const toolbarTools = [
  { key: 'bold', title: '粗体 (Ctrl+B)', prefix: '**', suffix: '**', icon: () => h('b', { style: 'font-size: 14px' }, 'B') },
  { key: 'italic', title: '斜体 (Ctrl+I)', prefix: '*', suffix: '*', icon: () => h('i', { style: 'font-size: 14px' }, 'I') },
  { key: 'heading', title: '标题', prefix: '## ', suffix: '', icon: () => h('span', { style: 'font-size: 12px; font-weight: bold' }, 'H') },
  { key: 'quote', title: '引用', prefix: '> ', suffix: '', icon: () => h('span', { style: 'font-size: 14px' }, '"') },
  { key: 'code', title: '代码', prefix: '`', suffix: '`', icon: () => h('span', { style: 'font-size: 12px; font-family: monospace' }, '</>') },
  { key: 'link', title: '链接', prefix: '[', suffix: '](url)', icon: () => h('span', { style: 'font-size: 12px' }, '🔗') },
  { key: 'image', title: '图片', prefix: '![alt](', suffix: ')', icon: () => h('span', { style: 'font-size: 12px' }, '🖼️') },
  { key: 'ul', title: '无序列表', prefix: '- ', suffix: '', icon: () => h('span', { style: 'font-size: 14px' }, '•') },
  { key: 'ol', title: '有序列表', prefix: '1. ', suffix: '', icon: () => h('span', { style: 'font-size: 12px' }, '1.') },
]

const renderedContent = computed(() => {
  const html = marked.parse(content.value, { async: false }) as string
  return DOMPurify.sanitize(html)
})

const wordCount = computed(() => {
  return content.value.replace(/\s/g, '').length
})

watch(content, (newValue) => {
  emit('update:modelValue', newValue)
  saveStatus.value = '未保存'
  
  // 模拟自动保存
  setTimeout(() => {
    saveStatus.value = '已保存'
  }, 1000)
})

watch(() => props.modelValue, (newValue) => {
  content.value = newValue
})

const insertMarkdown = (prefix: string, suffix: string) => {
  const textarea = textareaRef.value
  if (!textarea) return
  
  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  const text = content.value
  const before = text.substring(0, start)
  const selection = text.substring(start, end)
  const after = text.substring(end)
  
  content.value = before + prefix + selection + suffix + after
  
  setTimeout(() => {
    textarea.focus()
    const newCursor = start + prefix.length + selection.length
    textarea.setSelectionRange(newCursor, newCursor)
  }, 0)
}

const handleKeydown = (e: KeyboardEvent) => {
  // Tab 键插入空格
  if (e.key === 'Tab') {
    e.preventDefault()
    insertMarkdown('  ', '')
  }
  
  // Ctrl/Cmd + B 粗体
  if ((e.metaKey || e.ctrlKey) && e.key === 'b') {
    e.preventDefault()
    insertMarkdown('**', '**')
  }
  
  // Ctrl/Cmd + I 斜体
  if ((e.metaKey || e.ctrlKey) && e.key === 'i') {
    e.preventDefault()
    insertMarkdown('*', '*')
  }
  
  // Ctrl/Cmd + S 保存
  if ((e.metaKey || e.ctrlKey) && e.key === 's') {
    e.preventDefault()
    saveStatus.value = '已保存'
  }
}

const togglePreview = () => {
  showPreview.value = !showPreview.value
}

const toggleFullscreen = () => {
  isFullscreen.value = !isFullscreen.value
}
</script>

<style scoped>
.markdown-editor {
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-lg);
  overflow: hidden;
  background: var(--color-bg-primary);
}

.editor-toolbar {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  padding: var(--space-2);
  background: var(--color-bg-tertiary);
  border-bottom: 1px solid var(--color-border-primary);
}

.toolbar-group {
  display: flex;
  gap: var(--space-1);
}

.toolbar-divider {
  width: 1px;
  height: 24px;
  background: var(--color-border-primary);
  margin: 0 var(--space-2);
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
  transition: all var(--transition-fast);
}

.toolbar-btn:hover {
  background: var(--color-bg-hover);
  color: var(--color-text-primary);
  border-color: var(--color-text-tertiary);
}

.editor-container {
  display: flex;
  height: 400px;
}

.editor-container.preview-mode {
  display: grid;
  grid-template-columns: 1fr 1fr;
}

.editor-container.fullscreen {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
  height: 100vh;
}

.editor-pane {
  flex: 1;
  min-width: 0;
}

.editor-textarea {
  width: 100%;
  height: 100%;
  padding: var(--space-4);
  font-family: var(--font-mono);
  font-size: var(--text-sm);
  line-height: 1.8;
  color: var(--color-text-primary);
  background: var(--color-bg-secondary);
  border: none;
  resize: none;
  outline: none;
}

.preview-pane {
  border-left: 1px solid var(--color-border-primary);
  background: var(--color-bg-primary);
  overflow-y: auto;
}

.preview-header {
  padding: var(--space-3) var(--space-4);
  font-size: var(--text-xs);
  font-weight: var(--font-semibold);
  color: var(--color-text-tertiary);
  text-transform: uppercase;
  letter-spacing: 0.05em;
  border-bottom: 1px solid var(--color-border-secondary);
}

.preview-content {
  padding: var(--space-4);
  font-size: var(--text-sm);
  line-height: 1.8;
}

.editor-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-2) var(--space-4);
  font-size: var(--text-xs);
  color: var(--color-text-tertiary);
  background: var(--color-bg-tertiary);
  border-top: 1px solid var(--color-border-primary);
}

.word-count {
  font-family: var(--font-mono);
}

.save-status {
  color: var(--color-success);
}

/* Prose styles for preview */
.preview-content :deep(h1),
.preview-content :deep(h2),
.preview-content :deep(h3) {
  margin-top: 1.5em;
  margin-bottom: 0.75em;
  color: var(--color-text-primary);
}

.preview-content :deep(p) {
  margin-bottom: 1em;
  color: var(--color-text-secondary);
}

.preview-content :deep(code) {
  background: var(--color-bg-tertiary);
  color: var(--color-primary);
  padding: 0.2em 0.4em;
  border-radius: var(--radius-md);
  font-family: var(--font-mono);
  font-size: 0.9em;
}

.preview-content :deep(pre) {
  background: #1E1E1E;
  color: #D4D4D4;
  padding: var(--space-4);
  border-radius: var(--radius-lg);
  overflow-x: auto;
  margin-bottom: 1em;
}

.preview-content :deep(pre code) {
  background: none;
  color: inherit;
  padding: 0;
}

.preview-content :deep(blockquote) {
  border-left: 4px solid var(--color-primary);
  padding-left: var(--space-4);
  margin: 1em 0;
  font-style: italic;
  color: var(--color-text-secondary);
}

.preview-content :deep(ul),
.preview-content :deep(ol) {
  margin: 1em 0;
  padding-left: var(--space-6);
}

.preview-content :deep(li) {
  margin-bottom: 0.5em;
}

.preview-content :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: var(--radius-lg);
  margin: 1em 0;
}

.preview-content :deep(a) {
  color: var(--color-primary);
  text-decoration: none;
}

.preview-content :deep(a:hover) {
  text-decoration: underline;
}

.preview-content :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 1em 0;
}

.preview-content :deep(th),
.preview-content :deep(td) {
  border: 1px solid var(--color-border-primary);
  padding: var(--space-2) var(--space-3);
  text-align: left;
}

.preview-content :deep(th) {
  background: var(--color-bg-tertiary);
  font-weight: var(--font-semibold);
}
</style>
