<template>
  <div v-if="headings.length > 0" class="toc-container">
    <div class="toc-header">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <line x1="8" y1="6" x2="21" y2="6"/>
        <line x1="8" y1="12" x2="21" y2="12"/>
        <line x1="8" y1="18" x2="21" y2="18"/>
        <line x1="3" y1="6" x2="3.01" y2="6"/>
        <line x1="3" y1="12" x2="3.01" y2="12"/>
        <line x1="3" y1="18" x2="3.01" y2="18"/>
      </svg>
      目录
    </div>
    <nav class="toc-nav">
      <a
        v-for="heading in headings"
        :key="heading.id"
        :href="`#${heading.id}`"
        class="toc-link"
        :class="{ 'toc-h3': heading.level === 3 }"
        :style="{ paddingLeft: heading.level === 3 ? '16px' : '8px' }"
        @click.prevent="scrollToHeading(heading.id)"
      >
        {{ heading.text }}
      </a>
    </nav>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'

interface Heading {
  id: string
  text: string
  level: number
}

const props = defineProps<{
  content: string
}>()

const headings = ref<Heading[]>([])

const extractHeadings = (html: string) => {
  const parser = new DOMParser()
  const doc = parser.parseFromString(html, 'text/html')
  const headingElements = doc.querySelectorAll('h2, h3')
  
  const extracted: Heading[] = []
  headingElements.forEach((el, index) => {
    const text = el.textContent || ''
    const level = parseInt(el.tagName.charAt(1))
    const id = `heading-${index}-${text.slice(0, 20).replace(/\s+/g, '-')}`
    extracted.push({ id, text, level })
  })
  
  headings.value = extracted
}

const scrollToHeading = (id: string) => {
  const element = document.getElementById(id)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}

watch(() => props.content, (newContent) => {
  if (newContent) {
    setTimeout(() => extractHeadings(newContent), 100)
  }
}, { immediate: true })

onMounted(() => {
  // 添加 id 到文章内容中的标题
  setTimeout(() => {
    const articleContent = document.querySelector('.article-body')
    if (articleContent) {
      const headingElements = articleContent.querySelectorAll('h2, h3')
      headingElements.forEach((el, index) => {
        if (!el.id) {
          el.id = `heading-${index}-${(el.textContent || '').slice(0, 20).replace(/\s+/g, '-')}`
        }
      })
    }
  }, 200)
})
</script>

<style scoped>
.toc-container {
  position: sticky;
  top: 80px;
  padding: var(--space-4);
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  max-height: calc(100vh - 120px);
  overflow-y: auto;
}

.toc-header {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: var(--text-sm);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-3);
  padding-bottom: var(--space-3);
  border-bottom: 1px solid var(--color-border-secondary);
}

.toc-nav {
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
}

.toc-link {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  text-decoration: none;
  padding: var(--space-2);
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
  border-left: 2px solid transparent;
}

.toc-link:hover {
  color: var(--color-primary);
  background: rgba(0, 102, 255, 0.05);
  border-left-color: var(--color-primary);
}

.toc-h3 {
  font-size: var(--text-xs);
}
</style>
