<template>
  <div class="app" :class="{ 'dark': isDark }">
    <!-- 导航栏 -->
    <Navbar />
    
    <router-view v-slot="{ Component, route }">
      <transition name="page" mode="out-in">
        <component :is="Component" :key="route.path" />
      </transition>
    </router-view>
    
    <!-- Toast 通知组件 -->
    <Toast />
    
    <!-- 浮动操作按钮 -->
    <div class="floating-buttons">
      <!-- 主题切换按钮 -->
      <button 
        class="float-btn theme-btn"
        @click="toggleTheme"
        :title="isDark ? '切换到浅色模式' : '切换到深色模式'"
      >
        <!-- 太阳图标（浅色模式显示） -->
        <svg v-if="isDark" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="12" cy="12" r="5"/>
          <line x1="12" y1="1" x2="12" y2="3"/>
          <line x1="12" y1="21" x2="12" y2="23"/>
          <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"/>
          <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"/>
          <line x1="1" y1="12" x2="3" y2="12"/>
          <line x1="21" y1="12" x2="23" y2="12"/>
          <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"/>
          <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"/>
        </svg>
        <!-- 月亮图标（深色模式显示） -->
        <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/>
        </svg>
      </button>
      
      <!-- 回到顶部按钮 -->
      <transition name="fade">
        <button 
          v-if="showBackToTop" 
          class="float-btn back-btn"
          @click="scrollToTop"
        >
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="12" y1="19" x2="12" y2="5"/>
            <polyline points="5 12 12 5 19 12"/>
          </svg>
        </button>
      </transition>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import Toast from './components/Toast.vue'
import Navbar from './components/Navbar.vue'
import { useUserStore } from './stores/user'
import { useSettingsStore } from './stores/settings'

const route = useRoute()
const isDark = ref(false)
const showBackToTop = ref(false)
const userStore = useUserStore()
const settingsStore = useSettingsStore()

onMounted(async () => {
  // 初始化主题
  const savedTheme = sessionStorage.getItem('theme')
  if (savedTheme === 'dark') {
    isDark.value = true
    document.documentElement.classList.add('dark')
  } else if (!savedTheme && window.matchMedia('(prefers-color-scheme: dark)').matches) {
    isDark.value = true
    document.documentElement.classList.add('dark')
  }
  
  // 恢复用户登录状态
  if (userStore.token) {
    await userStore.getProfile()
  }
  
  // 加载网站设置
  await settingsStore.loadSettings()
  
  // 监听滚动
  window.addEventListener('scroll', handleScroll, { passive: true })
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})

const handleScroll = () => {
  showBackToTop.value = window.scrollY > 400
}

const scrollToTop = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const toggleTheme = () => {
  isDark.value = !isDark.value
  if (isDark.value) {
    document.documentElement.classList.add('dark')
    localStorage.setItem('theme', 'dark')
  } else {
    document.documentElement.classList.remove('dark')
    localStorage.setItem('theme', 'light')
  }
}

// 路由切换时滚动到顶部
watch(() => route.path, () => {
  window.scrollTo({ top: 0 })
})

// 暴露主题状态给子组件
defineExpose({ isDark })
</script>

<style>
/* Page Transitions */
.page-enter-active,
.page-leave-active {
  transition: opacity 0.2s ease, transform 0.2s ease;
}

.page-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.page-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* Floating Buttons */
.floating-buttons {
  position: fixed;
  bottom: 24px;
  right: 24px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  z-index: var(--z-fixed);
}

.float-btn {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: 50%;
  box-shadow: var(--shadow-lg);
  cursor: pointer;
  color: var(--color-text-secondary);
  transition: all var(--transition-fast);
}

.float-btn:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-xl);
}

.theme-btn:hover {
  color: var(--color-primary);
  border-color: var(--color-primary);
}

.theme-btn:hover svg {
  transform: rotate(15deg);
}

.back-btn {
  background: var(--color-primary);
  border-color: var(--color-primary);
  color: white;
}

.back-btn:hover {
  background: var(--color-primary-dark);
  border-color: var(--color-primary-dark);
  color: white;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease, transform 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}
</style>
