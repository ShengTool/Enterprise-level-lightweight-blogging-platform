<template>
  <!-- 添加一个根div来避免属性继承警告 -->
  <div class="toast-wrapper" aria-live="polite" v-bind="$attrs">
    <transition-group name="toast" tag="div" class="toast-container">
      <div
        v-for="toast in toasts"
        :key="toast.id"
        class="toast"
        :class="[`toast-${toast.type}`]"
        role="alert"
      >
        <div class="toast-icon">
          <svg v-if="toast.type === 'success'" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
            <polyline points="22 4 12 14.01 9 11.01"/>
          </svg>
          <svg v-else-if="toast.type === 'error'" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="12" cy="12" r="10"/>
            <line x1="15" y1="9" x2="9" y2="15"/>
            <line x1="9" y1="9" x2="15" y2="15"/>
          </svg>
          <svg v-else-if="toast.type === 'warning'" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/>
            <line x1="12" y1="9" x2="12" y2="13"/>
            <line x1="12" y1="17" x2="12.01" y2="17"/>
          </svg>
          <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="12" cy="12" r="10"/>
            <line x1="12" y1="16" x2="12" y2="12"/>
            <line x1="12" y1="8" x2="12.01" y2="8"/>
          </svg>
        </div>
        <div class="toast-content">
          <p v-if="toast.title" class="toast-title">{{ toast.title }}</p>
          <p class="toast-message">{{ toast.message }}</p>
        </div>
        <button class="toast-close" @click="removeToast(toast.id)" aria-label="关闭">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="18" y1="6" x2="6" y2="18"/>
            <line x1="6" y1="6" x2="18" y2="18"/>
          </svg>
        </button>
      </div>
    </transition-group>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

// 阻止 devtools inspector 注入属性产生警告
defineOptions({ inheritAttrs: false })

interface Toast {
  id: number
  type: 'success' | 'error' | 'warning' | 'info'
  message: string
  title?: string
  duration?: number
}

const toasts = ref<Toast[]>([])
let toastId = 0

const addToast = (toast: Omit<Toast, 'id'>) => {
  const id = ++toastId
  const newToast: Toast = {
    id,
    duration: 4000,
    ...toast
  }
  
  toasts.value.push(newToast)
  
  if (newToast.duration && newToast.duration > 0) {
    setTimeout(() => {
      removeToast(id)
    }, newToast.duration)
  }
  
  return id
}

const removeToast = (id: number) => {
  const index = toasts.value.findIndex(t => t.id === id)
  if (index > -1) {
    toasts.value.splice(index, 1)
  }
}

// 快捷方法
const success = (message: string, title?: string) => addToast({ type: 'success', message, title })
const error = (message: string, title?: string) => addToast({ type: 'error', message, title })
const warning = (message: string, title?: string) => addToast({ type: 'warning', message, title })
const info = (message: string, title?: string) => addToast({ type: 'info', message, title })

// 处理 window 事件
const handleToastEvent = (e: Event) => {
  const customEvent = e as CustomEvent<{ type: 'success' | 'error' | 'warning' | 'info', message: string, title?: string }>
  const { type, message, title } = customEvent.detail
  addToast({ type, message, title })
}

onMounted(() => {
  window.addEventListener('toast', handleToastEvent)
})

onUnmounted(() => {
  window.removeEventListener('toast', handleToastEvent)
})

// 暴露方法给全局使用
defineExpose({ addToast, removeToast, success, error, warning, info })
</script>

<style scoped>
/* Toast Wrapper - Fixed position at top-right */
.toast-wrapper {
  position: fixed;
  top: 80px;
  right: 20px;
  z-index: 9999;
  pointer-events: none;
}

/* Toast Container */
.toast-container {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
  pointer-events: none;
}

/* Individual Toast */
.toast {
  display: flex;
  align-items: flex-start;
  gap: var(--space-3);
  padding: var(--space-4);
  min-width: 320px;
  max-width: 420px;
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl);
  pointer-events: auto;
  transition: all var(--transition-base);
}

/* Toast Types */
.toast-success {
  border-left: 4px solid var(--color-success);
}

.toast-success .toast-icon {
  color: var(--color-success);
}

.toast-error {
  border-left: 4px solid var(--color-error);
}

.toast-error .toast-icon {
  color: var(--color-error);
}

.toast-warning {
  border-left: 4px solid var(--color-warning);
}

.toast-warning .toast-icon {
  color: var(--color-warning);
}

.toast-info {
  border-left: 4px solid var(--color-info);
}

.toast-info .toast-icon {
  color: var(--color-info);
}

/* Toast Icon */
.toast-icon {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* Toast Content */
.toast-content {
  flex: 1;
  min-width: 0;
}

.toast-title {
  font-size: var(--text-sm);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-1);
}

.toast-message {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  word-wrap: break-word;
}

/* Toast Close Button */
.toast-close {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  padding: 0;
  background: transparent;
  border: none;
  border-radius: var(--radius-md);
  color: var(--color-text-tertiary);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.toast-close:hover {
  background: var(--color-bg-hover);
  color: var(--color-text-primary);
}

/* Toast Animations */
.toast-enter-active {
  transition: all 0.3s ease;
}

.toast-leave-active {
  transition: all 0.2s ease;
}

.toast-enter-from {
  opacity: 0;
  transform: translateX(100%);
}

.toast-leave-to {
  opacity: 0;
  transform: translateX(100%);
}

.toast-move {
  transition: transform 0.3s ease;
}

/* Responsive */
@media (max-width: 640px) {
  .toast-wrapper {
    top: 16px;
    right: 16px;
    left: 16px;
  }
  
  .toast {
    min-width: auto;
    max-width: 100%;
  }
}
</style>
