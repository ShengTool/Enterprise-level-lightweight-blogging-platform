

// Toast 通知
export const useToast = () => {
  const toast = {
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

  return toast
}

// 确认对话框
export const useConfirm = () => {
  const confirm = (options: {
    title?: string
    message: string
    confirmText?: string
    cancelText?: string
    type?: 'danger' | 'warning' | 'success' | 'info'
  }): Promise<boolean> => {
    return new Promise((resolve) => {
      window.dispatchEvent(new CustomEvent('confirm', { 
        detail: { ...options, resolve } 
      }))
    })
  }

  return { confirm }
}

// 草稿自动保存
const DRAFT_KEY_PREFIX = 'liteblog_draft_'

export const useDraft = () => {
  const saveDraft = (key: string, content: any) => {
    const data = {
      content,
      savedAt: new Date().toISOString()
    }
    localStorage.setItem(DRAFT_KEY_PREFIX + key, JSON.stringify(data))
  }

  const loadDraft = (key: string) => {
    const data = localStorage.getItem(DRAFT_KEY_PREFIX + key)
    if (data) {
      try {
        return JSON.parse(data)
      } catch {
        return null
      }
    }
    return null
  }

  const clearDraft = (key: string) => {
    localStorage.removeItem(DRAFT_KEY_PREFIX + key)
  }

  return {
    saveDraft,
    loadDraft,
    clearDraft
  }
}
