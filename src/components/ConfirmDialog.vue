<template>
  <Teleport to="body">
    <Transition name="modal">
      <div v-if="visible" class="modal-overlay" @click.self="handleCancel">
        <div class="modal" :class="{ small: size === 'small' }">
          <div class="modal-icon" :class="type">
            <svg v-if="type === 'danger'" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/>
              <line x1="12" y1="9" x2="12" y2="13"/>
              <line x1="12" y1="17" x2="12.01" y2="17"/>
            </svg>
            <svg v-else-if="type === 'warning'" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <line x1="12" y1="8" x2="12" y2="12"/>
              <line x1="12" y1="16" x2="12.01" y2="16"/>
            </svg>
            <svg v-else-if="type === 'success'" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
              <polyline points="22 4 12 14.01 9 11.01"/>
            </svg>
            <svg v-else width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <line x1="12" y1="16" x2="12" y2="12"/>
              <line x1="12" y1="8" x2="12.01" y2="8"/>
            </svg>
          </div>
          
          <h3 class="modal-title">{{ title }}</h3>
          <p class="modal-message">{{ message }}</p>
          
          <div class="modal-actions">
            <button class="btn btn-secondary" @click="handleCancel">
              {{ cancelText }}
            </button>
            <button 
              class="btn" 
              :class="confirmButtonClass"
              @click="handleConfirm"
            >
              {{ confirmText }}
            </button>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

interface Props {
  title?: string
  message?: string
  confirmText?: string
  cancelText?: string
  type?: 'danger' | 'warning' | 'success' | 'info'
  size?: 'normal' | 'small'
}

const props = withDefaults(defineProps<Props>(), {
  title: '确认操作',
  message: '确定要执行此操作吗？',
  confirmText: '确定',
  cancelText: '取消',
  type: 'danger',
  size: 'normal'
})

const emit = defineEmits<{
  (e: 'confirm'): void
  (e: 'cancel'): void
}>()

const visible = ref(false)
let resolvePromise: ((value: boolean) => void) | null = null

const confirmButtonClass = computed(() => {
  return {
    'btn-primary': props.type !== 'danger',
    'btn-danger': props.type === 'danger'
  }
})

const show = (): Promise<boolean> => {
  visible.value = true
  return new Promise((resolve) => {
    resolvePromise = resolve
  })
}

const hide = () => {
  visible.value = false
  resolvePromise = null
}

const handleConfirm = () => {
  hide()
  emit('confirm')
  resolvePromise?.(true)
}

const handleCancel = () => {
  hide()
  emit('cancel')
  resolvePromise?.(false)
}

defineExpose({ show, hide })
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  inset: 0;
  z-index: var(--z-modal, 9998);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: var(--space-4);
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(4px);
}

.modal {
  width: 100%;
  max-width: 420px;
  padding: var(--space-8);
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-2xl);
  box-shadow: var(--shadow-2xl);
  text-align: center;
}

.modal.small {
  max-width: 360px;
  padding: var(--space-6);
}

.modal-icon {
  width: 64px;
  height: 64px;
  margin: 0 auto var(--space-4);
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}

.modal-icon.danger {
  background: rgba(239, 68, 68, 0.1);
  color: var(--color-error);
}

.modal-icon.warning {
  background: rgba(245, 158, 11, 0.1);
  color: var(--color-warning);
}

.modal-icon.success {
  background: rgba(16, 185, 129, 0.1);
  color: var(--color-success);
}

.modal-icon.info {
  background: rgba(59, 130, 246, 0.1);
  color: var(--color-info);
}

.modal-title {
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-2);
}

.modal-message {
  font-size: var(--text-base);
  color: var(--color-text-secondary);
  line-height: 1.6;
  margin-bottom: var(--space-6);
}

.modal-actions {
  display: flex;
  justify-content: center;
  gap: var(--space-3);
}

/* Transitions */
.modal-enter-active,
.modal-leave-active {
  transition: all 0.2s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-enter-from .modal,
.modal-leave-to .modal {
  transform: scale(0.95);
}
</style>
