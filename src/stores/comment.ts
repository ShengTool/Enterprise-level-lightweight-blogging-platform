import { defineStore } from 'pinia'
import axios from '../utils/axios'

interface Comment {
  id: number
  content: string
  articleId: number
  userId: number
  parentId: number | null
  createdAt: string
  updatedAt: string
  user: {
    id: number
    username: string
    avatar: string
  }
}

export const useCommentStore = defineStore('comment', {
  state: () => ({
    comments: [] as Comment[],
    loading: false,
    error: null as string | null
  }),
  actions: {
    async getComments(articleId: number) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get('/comments', { params: { articleId } })
        this.comments = response.data
        return response.data
      } catch (error: any) {
        this.error = error.response?.data?.message || 'Failed to get comments'
        throw error
      } finally {
        this.loading = false
      }
    },
    async createComment(comment: { content: string; articleId: number; parentId: number | null }) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.post('/comments', {
          content: comment.content,
          article: { id: comment.articleId },
          parentId: comment.parentId
        })
        this.comments.push(response.data)
        return response.data
      } catch (error: any) {
        this.error = error.response?.data?.message || 'Failed to create comment'
        throw error
      } finally {
        this.loading = false
      }
    },
    async deleteComment(id: number) {
      this.loading = true
      this.error = null
      try {
        await axios.delete(`/comments/${id}`)
        this.comments = this.comments.filter(c => c.id !== id)
      } catch (error: any) {
        this.error = error.response?.data?.message || 'Failed to delete comment'
        throw error
      } finally {
        this.loading = false
      }
    }
  }
})