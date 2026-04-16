import type { App } from 'vue'

// Toast 通知助手 - 通过 window 事件触发 Toast 组件
// 使用方式: import { toast } from '@/components/ToastPlugin'
//           toast.success('操作成功')

export const toast = {
  success: (message: string, title?: string) => {
    window.dispatchEvent(new CustomEvent('toast', { 
      detail: { type: 'success', message, title } 
    }))
  },
  error: (message: string, title?: string) => {
    window.dispatchEvent(new CustomEvent('toast', { 
      detail: { type: 'error', message, title } 
    }))
  },
  warning: (message: string, title?: string) => {
    window.dispatchEvent(new CustomEvent('toast', { 
      detail: { type: 'warning', message, title } 
    }))
  },
  info: (message: string, title?: string) => {
    window.dispatchEvent(new CustomEvent('toast', { 
      detail: { type: 'info', message, title } 
    }))
  }
}

// Vue 插件安装函数
export const installToast = (app: App) => {
  app.config.globalProperties.$toast = toast
  app.provide('toast', toast)
}

export default { installToast, toast }
