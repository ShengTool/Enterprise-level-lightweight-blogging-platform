import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vite.dev/config/
export default defineConfig({
  base: '/',
  server: {
    port: 5173,
    host: 'localhost',
  },
  build: {
    sourcemap: 'hidden',
    // 代码分割优化
    rollupOptions: {
      output: {
        manualChunks: {
          // Vue 核心库
          'vue-vendor': ['vue', 'vue-router', 'pinia'],
          // UI 框架
          'element-plus': ['element-plus'],
          // Markdown 处理
          'markdown': ['marked'],
          // XSS 防护
          'dompurify': ['dompurify'],
        },
      },
    },
    // 增大警告限制
    chunkSizeWarningLimit: 1000,
  },
  plugins: [
    vue(),
    // 所有 Trae IDE 插件已禁用 - 避免兼容性警告
    // Inspector(),
    // traeBadgePlugin(),
  ],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
})
