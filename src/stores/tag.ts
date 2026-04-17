import { defineStore } from 'pinia'
import axios from '../utils/axios'

interface Tag {
  id: number
  name: string
  color: string
  count: number
  created_at: string
  updated_at: string
}

export const useTagStore = defineStore('tag', {
  state: () => ({
    tags: [] as Tag[],
    loading: false,
    error: null as string | null
  }),
  actions: {
    async getTags() {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get('/tags')
        this.tags = Array.isArray(response.data) ? response.data : []
        return response.data || []
      } catch (error: any) {
        this.error = error.response?.data?.message || 'Failed to get tags'
        this.tags = []
        return []
      } finally {
        this.loading = false
      }
    },
    async createTag(tag: Partial<Tag>) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.post('/tags', tag)
        this.tags.push(response.data)
        return response.data
      } catch (error: any) {
        this.error = error.response?.data?.message || 'Failed to create tag'
        throw error
      } finally {
        this.loading = false
      }
    },
    async updateTag(id: number, tag: Partial<Tag>) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.put(`/tags/${id}`, tag)
        const index = this.tags.findIndex(t => t.id === id)
        if (index !== -1) {
          this.tags[index] = response.data
        }
        return response.data
      } catch (error: any) {
        this.error = error.response?.data?.message || 'Failed to update tag'
        throw error
      } finally {
        this.loading = false
      }
    },
    async deleteTag(id: number) {
      this.loading = true
      this.error = null
      try {
        await axios.delete(`/tags/${id}`)
        this.tags = this.tags.filter(t => t.id !== id)
      } catch (error: any) {
        this.error = error.response?.data?.message || 'Failed to delete tag'
        throw error
      } finally {
        this.loading = false
      }
    }
  }
})