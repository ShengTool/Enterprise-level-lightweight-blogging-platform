<template>
  <div class="page-container">
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">系统设置</h2>
        <p class="page-subtitle">配置网站基本信息和系统参数</p>
      </div>
    </div>
    
    <div class="settings-layout">
      <!-- 左侧设置菜单 -->
      <div class="settings-nav">
        <button 
          v-for="section in settingSections" 
          :key="section.key"
          class="nav-item"
          :class="{ active: activeSection === section.key }"
          @click="activeSection = section.key"
        >
          <component :is="section.icon" class="nav-icon" />
          <span>{{ section.label }}</span>
        </button>
      </div>
      
      <!-- 右侧设置内容 -->
      <div class="settings-content">
        <!-- 网站设置 -->
        <div v-if="activeSection === 'site'" class="settings-panel">
          <div class="panel-card">
            <h3 class="panel-title">网站信息</h3>
            <div class="form-group">
              <label>网站名称</label>
              <input v-model="settings.siteName" type="text" class="form-input" />
            </div>
            <div class="form-group">
              <label>网站描述</label>
              <textarea v-model="settings.siteDescription" class="form-input" rows="3"></textarea>
              <span class="form-hint">用于 SEO 和首页展示</span>
            </div>
            <div class="form-group">
              <label>网站关键词</label>
              <input v-model="settings.siteKeywords" type="text" class="form-input" />
              <span class="form-hint">多个关键词用逗号分隔</span>
            </div>
          </div>
          
          <div class="panel-card">
            <h3 class="panel-title">联系方式</h3>
            <div class="form-group">
              <label>管理员邮箱</label>
              <input v-model="settings.adminEmail" type="email" class="form-input" />
            </div>
            <div class="form-group">
              <label>备案号</label>
              <input v-model="settings.icp" type="text" class="form-input" placeholder="京ICP备XXXXXXXX号" />
            </div>
          </div>
        </div>
        
        <!-- 内容设置 -->
        <div v-if="activeSection === 'content'" class="settings-panel">
          <div class="panel-card">
            <h3 class="panel-title">文章设置</h3>
            <div class="form-group">
              <label>每页文章数量</label>
              <input v-model.number="settings.articlesPerPage" type="number" class="form-input" min="1" max="100" />
            </div>
            <div class="form-group">
              <label class="checkbox-label">
                <input v-model="settings.commentsEnabled" type="checkbox" />
                <span>开启评论功能</span>
              </label>
            </div>
            <div class="form-group">
              <label class="checkbox-label">
                <input v-model="settings.commentAudit" type="checkbox" />
                <span>评论需要审核</span>
              </label>
            </div>
          </div>
          
          <div class="panel-card">
            <h3 class="panel-title">用户设置</h3>
            <div class="form-group">
              <label class="checkbox-label">
                <input v-model="settings.registrationEnabled" type="checkbox" />
                <span>允许新用户注册</span>
              </label>
            </div>
            <div class="form-group">
              <label class="checkbox-label">
                <input v-model="settings.emailVerification" type="checkbox" />
                <span>注册需要邮箱验证</span>
              </label>
            </div>
          </div>
        </div>
        
        <!-- 系统信息 -->
        <div v-if="activeSection === 'system'" class="settings-panel">
          <div class="panel-card">
            <h3 class="panel-title">系统信息</h3>
            <div class="info-list">
              <div class="info-item">
                <span class="info-label">系统版本</span>
                <span class="info-value">v1.0.0</span>
              </div>
              <div class="info-item">
                <span class="info-label">前端框架</span>
                <span class="info-value">Vue 3.4 + TypeScript</span>
              </div>
              <div class="info-item">
                <span class="info-label">后端框架</span>
                <span class="info-value">Spring Boot 3.2</span>
              </div>
              <div class="info-item">
                <span class="info-label">数据库</span>
                <span class="info-value">MySQL 8.0</span>
              </div>
              <div class="info-item">
                <span class="info-label">Node 版本</span>
                <span class="info-value">v22.21.1</span>
              </div>
            </div>
          </div>
          
          <div class="panel-card">
            <h3 class="panel-title">数据备份</h3>
            <p class="panel-description">备份系统数据到本地文件</p>
            <button class="btn btn-secondary" @click="handleBackup">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
                <polyline points="7 10 12 15 17 10"/>
                <line x1="12" y1="15" x2="12" y2="3"/>
              </svg>
              立即备份
            </button>
          </div>
          
          <div class="panel-card danger-zone">
            <h3 class="panel-title">危险区域</h3>
            <p class="panel-description">以下操作不可逆，请谨慎操作</p>
            <div class="danger-actions">
              <button class="btn btn-ghost danger" @click="confirmClearCache">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <polyline points="3 6 5 6 21 6"/>
                  <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
                </svg>
                清除缓存
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 保存按钮 -->
    <div class="settings-footer">
      <button class="btn btn-primary btn-lg" @click="saveSettings">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"/>
          <polyline points="17 21 17 13 7 13 7 21"/>
          <polyline points="7 3 7 8 15 8"/>
        </svg>
        保存设置
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, h } from 'vue'

const activeSection = ref('site')

// 图标组件
const Globe = () => h('svg', { width: 20, height: 20, viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', strokeWidth: 2 }, [
  h('circle', { cx: 12, cy: 12, r: 10 }),
  h('line', { x1: 2, y1: 12, x2: 22, y2: 12 }),
  h('path', { d: 'M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z' })
])

const FileText = () => h('svg', { width: 20, height: 20, viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', strokeWidth: 2 }, [
  h('path', { d: 'M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z' }),
  h('polyline', { points: '14 2 14 8 20 8' })
])

const Server = () => h('svg', { width: 20, height: 20, viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', strokeWidth: 2 }, [
  h('rect', { x: 2, y: 2, width: 20, height: 8, rx: 2, ry: 2 }),
  h('rect', { x: 2, y: 14, width: 20, height: 8, rx: 2, ry: 2 }),
  h('line', { x1: 6, y1: 6, x2: 6.01, y2: 6 }),
  h('line', { x1: 6, y1: 18, x2: 6.01, y2: 18 })
])

const settingSections = [
  { key: 'site', label: '网站设置', icon: Globe },
  { key: 'content', label: '内容设置', icon: FileText },
  { key: 'system', label: '系统信息', icon: Server }
]

const settings = reactive({
  siteName: 'LiteBlog',
  siteDescription: '企业级轻量博客平台',
  siteKeywords: '博客,技术,分享',
  adminEmail: 'admin@example.com',
  icp: '',
  articlesPerPage: 12,
  commentsEnabled: true,
  commentAudit: false,
  registrationEnabled: true,
  emailVerification: false
})

const saveSettings = () => {
  // 保存设置逻辑
  alert('设置已保存')
}

const handleBackup = () => {
  alert('数据备份成功')
}

const confirmClearCache = () => {
  if (confirm('确定要清除缓存吗？')) {
    alert('缓存已清除')
  }
}
</script>

<style scoped>
.page-container {
  max-width: 1000px;
}

.page-header {
  margin-bottom: var(--space-6);
}

.page-title {
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-1);
}

.page-subtitle {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

/* Settings Layout */
.settings-layout {
  display: grid;
  grid-template-columns: 200px 1fr;
  gap: var(--space-6);
}

@media (max-width: 768px) {
  .settings-layout {
    grid-template-columns: 1fr;
  }
}

/* Settings Nav */
.settings-nav {
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
}

.nav-item {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-3) var(--space-4);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-secondary);
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.nav-item:hover {
  background: var(--color-bg-hover);
  color: var(--color-text-primary);
}

.nav-item.active {
  background: rgba(0, 102, 255, 0.1);
  border-color: var(--color-primary);
  color: var(--color-primary);
}

.nav-icon {
  flex-shrink: 0;
}

/* Settings Content */
.settings-content {
  min-width: 0;
}

.settings-panel {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
}

.panel-card {
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-xl);
  padding: var(--space-6);
}

.panel-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-4);
}

.panel-description {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
  margin-bottom: var(--space-4);
}

/* Form */
.form-group {
  margin-bottom: var(--space-4);
}

.form-group label {
  display: block;
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-primary);
  margin-bottom: var(--space-2);
}

.form-input {
  width: 100%;
  padding: var(--space-3) var(--space-4);
  font-size: var(--text-base);
  color: var(--color-text-primary);
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-lg);
  transition: all var(--transition-fast);
}

.form-input:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px rgba(0, 102, 255, 0.15);
}

.form-hint {
  display: block;
  font-size: var(--text-xs);
  color: var(--color-text-tertiary);
  margin-top: var(--space-1);
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  cursor: pointer;
}

.checkbox-label input[type="checkbox"] {
  width: 18px;
  height: 18px;
  cursor: pointer;
}

.checkbox-label span {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
}

/* Info List */
.info-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-3) 0;
  border-bottom: 1px solid var(--color-border-secondary);
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
}

.info-value {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-primary);
  font-family: var(--font-mono);
}

/* Danger Zone */
.danger-zone {
  border-color: rgba(239, 68, 68, 0.3);
}

.danger-zone .panel-title {
  color: var(--color-error);
}

.danger-actions {
  display: flex;
  gap: var(--space-3);
}

.btn.danger {
  color: var(--color-error);
}

.btn.danger:hover {
  background: rgba(239, 68, 68, 0.1);
}

/* Footer */
.settings-footer {
  margin-top: var(--space-6);
  padding-top: var(--space-6);
  border-top: 1px solid var(--color-border-primary);
  display: flex;
  justify-content: flex-end;
}
</style>
