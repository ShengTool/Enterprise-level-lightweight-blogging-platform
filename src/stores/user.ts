import { defineStore } from 'pinia'
import axios from '../utils/axios'

interface User {
  id: number
  username: string
  email: string
  avatar: string
  isAdmin: boolean
}

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null as User | null,
    token: sessionStorage.getItem('token') || null,
    loading: false,
    error: null as string | null
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
    isAdmin: (state) => state.user?.isAdmin || false
  },
  actions: {
    async login(email: string, password: string) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.post('/auth/login', { email, password })
        this.token = response.data.token
        this.user = response.data.user
        sessionStorage.setItem('token', response.data.token)
        return response.data
      } catch (error: any) {
        this.error = error.response?.data?.message || 'Login failed'
        throw error
      } finally {
        this.loading = false
      }
    },
    async register(username: string, email: string, password: string) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.post('/auth/register', { username, email, password })
        return response.data
      } catch (error: any) {
        this.error = error.response?.data?.message || 'Registration failed'
        throw error
      } finally {
        this.loading = false
      }
    },
    async getProfile() {
      if (!this.token) return
      this.loading = true
      try {
        const response = await axios.get('/auth/verify')
        this.user = response.data
      } catch (error: any) {
        this.error = error.response?.data?.message || 'Failed to get profile'
        // 只在401错误时登出，其他错误不登出
        if (error.response?.status === 401) {
          this.logout()
        }
      } finally {
        this.loading = false
      }
    },
    logout() {
      this.user = null
      this.token = null
      sessionStorage.removeItem('token')
    }
  }
})