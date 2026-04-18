import { defineStore } from 'pinia'
import axios from '@/utils/axios'

interface Settings {
  siteName: string
  siteDescription: string
  siteKeywords: string
  adminEmail: string
  icp: string
  articlesPerPage: number
  commentsEnabled: boolean
  commentAudit: boolean
  registrationEnabled: boolean
  emailVerification: boolean
}

export const useSettingsStore = defineStore('settings', {
  state: () => ({
    settings: {
      siteName: 'LiteBlog',
      siteDescription: '',
      siteKeywords: '',
      adminEmail: '',
      icp: '',
      articlesPerPage: 10,
      commentsEnabled: true,
      commentAudit: false,
      registrationEnabled: true,
      emailVerification: false
    } as Settings,
    loaded: false
  }),

  actions: {
    async loadSettings() {
      try {
        // 使用公开接口，不需要管理员权限
        const res = await axios.get('/settings')
        this.settings = { ...this.settings, ...res.data }
        this.loaded = true
        this.applySettings()
      } catch (e) {
        console.error('加载设置失败', e)
      }
    },

    applySettings() {
      // 更新页面标题
      document.title = this.settings.siteName || 'LiteBlog'
      
      // 更新 meta 标签
      const descMeta = document.querySelector('meta[name="description"]')
      if (descMeta) {
        descMeta.setAttribute('content', this.settings.siteDescription || '')
      }
      
      const kwMeta = document.querySelector('meta[name="keywords"]')
      if (kwMeta) {
        kwMeta.setAttribute('content', this.settings.siteKeywords || '')
      }
    },

    async saveSettings(newSettings: Partial<Settings>) {
      const res = await axios.put('/admin/settings', newSettings)
      this.settings = { ...this.settings, ...res.data }
      this.applySettings()
      return this.settings
    }
  }
})
