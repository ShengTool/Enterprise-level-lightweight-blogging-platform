<template>
  <div class="notification-center" :class="{ open: isOpen }">
    <!-- 通知按钮 -->
    <button class="notification-trigger" @click="togglePanel">
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/>
        <path d="M13.73 21a2 2 0 0 1-3.46 0"/>
      </svg>
      <span v-if="unreadCount > 0" class="notification-badge">{{ unreadCount > 99 ? '99+' : unreadCount }}</span>
    </button>
    
    <!-- 通知面板 -->
    <transition name="slide-down">
      <div v-if="isOpen" class="notification-panel">
        <div class="panel-header">
          <h3>通知</h3>
          <div class="panel-actions">
            <button v-if="unreadCount > 0" class="mark-all-btn" @click="markAllAsRead">
              全部已读
            </button>
            <button class="close-btn" @click="isOpen = false">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/>
                <line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
            </button>
          </div>
        </div>
        
        <div class="panel-body">
          <div v-if="loading" class="notification-loading">
            <div class="loading-spinner"></div>
          </div>
          
          <div v-else-if="notifications.length === 0" class="notification-empty">
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/>
              <path d="M13.73 21a2 2 0 0 1-3.46 0"/>
            </svg>
            <p>暂无通知</p>
          </div>
          
          <div v-else class="notification-list">
            <div
              v-for="notification in notifications"
              :key="notification.id"
              class="notification-item"
              :class="{ unread: !notification.isRead }"
              @click="handleNotificationClick(notification)"
            >
              <div class="notification-icon" :class="notification.type.toLowerCase()">
                <svg v-if="notification.type === 'LIKE'" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
                </svg>
                <svg v-else-if="notification.type === 'COMMENT'" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
                </svg>
                <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <circle cx="12" cy="12" r="10"/>
                  <line x1="12" y1="16" x2="12" y2="12"/>
                  <line x1="12" y1="8" x2="12.01" y2="8"/>
                </svg>
              </div>
              <div class="notification-content">
                <p class="notification-text">{{ notification.title }}</p>
                <p class="notification-time">{{ formatTime(notification.createdAt) }}</p>
              </div>
              <div v-if="!notification.isRead" class="unread-dot"></div>
            </div>
          </div>
        </div>
        
        <div v-if="hasMore" class="panel-footer">
          <button class="load-more-btn" @click="loadMore">
            加载更多
          </button>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useUserStore } from '@/stores/user'
import axios from '../utils/axios'

const userStore = useUserStore()

const isOpen = ref(false)
const loading = ref(false)
const notifications = ref<any[]>([])
const unreadCount = ref(0)
const page = ref(1)
const hasMore = ref(false)

const togglePanel = () => {
  isOpen.value = !isOpen.value
  if (isOpen.value && notifications.value.length === 0) {
    loadNotifications()
  }
}

const loadNotifications = async () => {
  loading.value = true
  try {
    const token = userStore.token
    if (!token) return
    
    const response = await axios.get('/api/notifications', {
      headers: { Authorization: `Bearer ${token}` },
      params: { page: 1, size: 10 }
    })
    
    notifications.value = response.data.notifications || []
    unreadCount.value = response.data.unreadCount || 0
    hasMore.value = notifications.value.length < response.data.total
  } catch (error) {
    console.error('加载通知失败:', error)
  } finally {
    loading.value = false
  }
}

const loadMore = async () => {
  page.value++
  try {
    const token = userStore.token
    if (!token) return
    
    const response = await axios.get('/api/notifications', {
      headers: { Authorization: `Bearer ${token}` },
      params: { page: page.value, size: 10 }
    })
    
    notifications.value = [...notifications.value, ...response.data.notifications]
    hasMore.value = notifications.value.length < response.data.total
  } catch (error) {
    console.error('加载更多通知失败:', error)
  }
}

const markAllAsRead = async () => {
  try {
    const token = userStore.token
    if (!token) return
    
    await axios.post('/api/notifications/read-all', {}, {
      headers: { Authorization: `Bearer ${token}` }
    })
    
    notifications.value.forEach(n => n.isRead = true)
    unreadCount.value = 0
  } catch (error) {
    console.error('标记已读失败:', error)
  }
}

const handleNotificationClick = async (notification: any) => {
  if (!notification.isRead) {
    try {
      const token = userStore.token
      if (token) {
        await axios.post(`/api/notifications/${notification.id}/read`, {}, {
          headers: { Authorization: `Bearer ${token}` }
        })
        notification.isRead = true
        unreadCount.value = Math.max(0, unreadCount.value - 1)
      }
    } catch (error) {
      console.error('标记已读失败:', error)
    }
  }
  
  if (notification.link) {
    window.location.href = notification.link
  }
  isOpen.value = false
}

const formatTime = (date: string) => {
  const now = new Date()
  const time = new Date(date)
  const diff = now.getTime() - time.getTime()
  
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)} 分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)} 小时前`
  if (diff < 604800000) return `${Math.floor(diff / 86400000)} 天前`
  return time.toLocaleDateString('zh-CN')
}

// 点击外部关闭
const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as HTMLElement
  if (!target.closest('.notification-center')) {
    isOpen.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
  // 加载未读�?  const token = userStore.token
  if (token) {
    axios.get('/api/notifications/unread-count', {
      headers: { Authorization: `Bearer ${token}` }
    }).then(res => {
      unreadCount.value = res.data.count || 0
    }).catch(() => {})
  }
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.notification-center {
  position: relative;
}

.notification-trigger {
  position: relative;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-lg);
  cursor: pointer;
  color: var(--color-text-secondary);
  transition: all var(--transition-fast);
}

.notification-trigger:hover {
  background: var(--color-bg-hover);
  color: var(--color-text-primary);
}

.notification-badge {
  position: absolute;
  top: -4px;
  right: -4px;
  min-width: 18px;
  height: 18px;
  padding: 0 4px;
  font-size: 10px;
  font-weight: var(--font-bold);
  color: white;
  background: var(--color-error);
  border-radius: var(--radius-full);
  display: flex;
  align-items: center;
  justify-content: center;
}

.notification-panel {
  position: absolute;
  top: calc(100% + 8px);
  right: 0;
  width: 360px;
  max-height: 480px;
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-2xl);
  overflow: hidden;
  z-index: var(--z-dropdown);
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--space-4);
  border-bottom: 1px solid var(--color-border-secondary);
}

.panel-header h3 {
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
}

.panel-actions {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.mark-all-btn {
  font-size: var(--text-xs);
  color: var(--color-primary);
  background: none;
  border: none;
  cursor: pointer;
}

.close-btn {
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: none;
  border: none;
  border-radius: var(--radius-md);
  cursor: pointer;
  color: var(--color-text-tertiary);
}

.close-btn:hover {
  background: var(--color-bg-hover);
  color: var(--color-text-primary);
}

.panel-body {
  max-height: 360px;
  overflow-y: auto;
}

.notification-loading,
.notification-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--space-8);
  color: var(--color-text-tertiary);
}

.loading-spinner {
  width: 24px;
  height: 24px;
  border: 2px solid var(--color-border-primary);
  border-top-color: var(--color-primary);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.notification-empty p {
  margin-top: var(--space-3);
  font-size: var(--text-sm);
}

.notification-list {
  padding: var(--space-2);
}

.notification-item {
  display: flex;
  align-items: flex-start;
  gap: var(--space-3);
  padding: var(--space-3);
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: background var(--transition-fast);
}

.notification-item:hover {
  background: var(--color-bg-secondary);
}

.notification-item.unread {
  background: rgba(0, 102, 255, 0.05);
}

.notification-icon {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-lg);
  flex-shrink: 0;
}

.notification-icon.like {
  background: rgba(239, 68, 68, 0.1);
  color: #EF4444;
}

.notification-icon.comment {
  background: rgba(0, 102, 255, 0.1);
  color: var(--color-primary);
}

.notification-icon.favorite {
  background: rgba(245, 158, 11, 0.1);
  color: var(--color-warning);
}

.notification-icon.system {
  background: var(--color-bg-tertiary);
  color: var(--color-text-secondary);
}

.notification-content {
  flex: 1;
  min-width: 0;
}

.notification-text {
  font-size: var(--text-sm);
  color: var(--color-text-primary);
  line-height: 1.4;
}

.notification-time {
  font-size: var(--text-xs);
  color: var(--color-text-tertiary);
  margin-top: var(--space-1);
}

.unread-dot {
  width: 8px;
  height: 8px;
  background: var(--color-primary);
  border-radius: 50%;
  flex-shrink: 0;
  margin-top: 4px;
}

.panel-footer {
  padding: var(--space-3);
  border-top: 1px solid var(--color-border-secondary);
}

.load-more-btn {
  width: 100%;
  padding: var(--space-2);
  font-size: var(--text-sm);
  color: var(--color-primary);
  background: none;
  border: 1px solid var(--color-primary);
  border-radius: var(--radius-lg);
  cursor: pointer;
}

.load-more-btn:hover {
  background: rgba(0, 102, 255, 0.1);
}

/* Transitions */
.slide-down-enter-active,
.slide-down-leave-active {
  transition: all 0.2s ease;
}

.slide-down-enter-from,
.slide-down-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}
</style>
