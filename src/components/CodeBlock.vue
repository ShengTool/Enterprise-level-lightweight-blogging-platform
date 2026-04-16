<template>
  <div class="code-block-wrapper">
    <div class="code-header">
      <span class="code-lang">{{ language || 'code' }}</span>
      <button class="copy-btn" @click="copyCode" :class="{ copied }">
        <svg v-if="!copied" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <rect x="9" y="9" width="13" height="13" rx="2" ry="2"/>
          <path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"/>
        </svg>
        <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <polyline points="20 6 9 17 4 12"/>
        </svg>
        {{ copied ? '已复制' : '复制' }}
      </button>
    </div>
    <pre class="code-pre"><code ref="codeRef" :class="`language-${language}`"><slot /></code></pre>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const { language } = defineProps<{
  language?: string
}>()

const codeRef = ref<HTMLElement>()
const copied = ref(false)

const copyCode = async () => {
  if (!codeRef.value) return
  
  const text = codeRef.value.textContent || ''
  try {
    await navigator.clipboard.writeText(text)
    copied.value = true
    setTimeout(() => {
      copied.value = false
    }, 2000)
  } catch (err) {
    console.error('Failed to copy:', err)
  }
}
</script>

<style scoped>
.code-block-wrapper {
  margin: var(--space-4) 0;
  border-radius: var(--radius-lg);
  overflow: hidden;
  background: #1E1E1E;
}

.code-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-2) var(--space-4);
  background: #2D2D2D;
  border-bottom: 1px solid #3D3D3D;
}

.code-lang {
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
  color: #888;
  text-transform: uppercase;
}

.copy-btn {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  padding: var(--space-1) var(--space-2);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
  color: #888;
  background: transparent;
  border: 1px solid #3D3D3D;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.copy-btn:hover {
  color: #fff;
  border-color: #555;
  background: #3D3D3D;
}

.copy-btn.copied {
  color: var(--color-success);
  border-color: var(--color-success);
}

.code-pre {
  margin: 0;
  padding: var(--space-4);
  overflow-x: auto;
  font-family: var(--font-mono);
  font-size: var(--text-sm);
  line-height: 1.6;
  color: #D4D4D4;
}

.code-pre code {
  font-family: inherit;
}
</style>
