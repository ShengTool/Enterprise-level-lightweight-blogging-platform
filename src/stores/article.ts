import { defineStore } from 'pinia'
import axios from '../utils/axios'

interface Tag {
  id: number
  name: string
  color: string
  count: number
}

interface Article {
  id: number
  title: string
  content: string
  summary: string
  status: 'DRAFT' | 'PUBLISHED'
  user_id: number
  view_count: number
  created_at: string
  updated_at: string
  tags: Tag[]
  user?: {
    username: string
    avatar: string
  }
}

export const useArticleStore = defineStore('article', {
  state: () => ({
    articles: [] as Article[],
    currentArticle: null as Article | null,
    loading: false,
    error: null as string | null,
    total: 0
  }),
  actions: {
    async getArticles(page = 1, limit = 10, tag?: string, search?: string) {
      this.loading = true
      this.error = null
      try {
        const params = { page, limit, tag, search }
        const response = await axios.get('/articles', { params })
        this.articles = response.data?.articles || []
        this.total = response.data?.total || 0
        return response.data || { articles: [], total: 0 }
      } catch (error: any) {
        this.error = error.response?.data?.message || 'Failed to get articles'
        // 网络错误时返回空数据，避免前端崩溃
        if (!error.response) {
          this.articles = []
          this.total = 0
          return { data: [], total: 0 }
        }
        throw error
      } finally {
        this.loading = false
      }
    },
    async getArticleById(id: number) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/articles/${id}`)
        this.currentArticle = response.data
        return response.data
      } catch (error: any) {
        this.error = error.response?.data?.message || 'Failed to get article'
        throw error
      } finally {
        this.loading = false
      }
    },
    async createArticle(article: Partial<Article>) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.post('/articles', article)
        return response.data
      } catch (error: any) {
        this.error = error.response?.data?.message || 'Failed to create article'
        throw error
      } finally {
        this.loading = false
      }
    },
    async updateArticle(id: number, article: Partial<Article>) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.put(`/articles/${id}`, article)
        return response.data
      } catch (error: any) {
        this.error = error.response?.data?.message || 'Failed to update article'
        throw error
      } finally {
        this.loading = false
      }
    },
    async deleteArticle(id: number) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.delete(`/articles/${id}`)
        return response.data
      } catch (error: any) {
        this.error = error.response?.data?.message || 'Failed to delete article'
        throw error
      } finally {
        this.loading = false
      }
    },
    async saveDraft(id: number, article: Partial<Article>) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.put(`/articles/${id}/draft`, article)
        return response.data
      } catch (error: any) {
        this.error = error.response?.data?.message || 'Failed to save draft'
        throw error
      } finally {
        this.loading = false
      }
    }
  }
})