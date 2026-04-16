<template>
  <div class="not-found-page">
    <div class="container-app">
      <div class="error-content">
        <div class="error-code">
          <span class="digit">4</span>
          <div class="error-icon">
            <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <circle cx="12" cy="12" r="10"/>
              <line x1="8" y1="15" x2="16" y2="15"/>
              <line x1="9" y1="9" x2="9.01" y2="9"/>
              <line x1="15" y1="9" x2="15.01" y2="9"/>
            </svg>
          </div>
          <span class="digit">4</span>
        </div>
        
        <h1 class="error-title">页面未找到</h1>
        <p class="error-description">
          抱歉，您访问的页面不存在或已被移除。
          <br />请检查链接是否正确，或返回首页继续浏览。
        </p>
        
        <div class="error-actions">
          <router-link to="/" class="btn btn-primary btn-lg">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
              <polyline points="9 22 9 12 15 12 15 22"/>
            </svg>
            返回首页
          </router-link>
          <button class="btn btn-secondary btn-lg" @click="$router.back()">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="19" y1="12" x2="5" y2="12"/>
              <polyline points="12 19 5 12 12 5"/>
            </svg>
            返回上一页
          </button>
        </div>
        
        <!-- 推荐文章 -->
        <div class="suggested-content">
          <h3>也许您想查看：</h3>
          <div class="suggested-links">
            <router-link to="/" class="suggested-link">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"/>
                <polyline points="14 2 14 8 20 8"/>
              </svg>
              最新文章
            </router-link>
            <router-link to="/about" class="suggested-link">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="12" cy="12" r="10"/>
                <line x1="12" y1="16" x2="12" y2="12"/>
                <line x1="12" y1="8" x2="12.01" y2="8"/>
              </svg>
              关于我们
            </router-link>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 背景装饰 -->
    <div class="error-bg">
      <div class="error-grid"></div>
    </div>
  </div>
</template>

<style scoped>
.not-found-page {
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: var(--space-8) 0;
  position: relative;
  overflow: hidden;
}

.error-content {
  text-align: center;
  position: relative;
  z-index: 1;
}

.error-code {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-4);
  margin-bottom: var(--space-8);
}

.digit {
  font-size: 120px;
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  line-height: 1;
  opacity: 0.1;
}

.error-icon {
  color: var(--color-primary);
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.error-title {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-4);
}

.error-description {
  font-size: var(--text-lg);
  color: var(--color-text-secondary);
  line-height: 1.8;
  margin-bottom: var(--space-8);
}

.error-actions {
  display: flex;
  justify-content: center;
  gap: var(--space-4);
  margin-bottom: var(--space-12);
  flex-wrap: wrap;
}

/* Suggested Content */
.suggested-content {
  padding-top: var(--space-8);
  border-top: 1px solid var(--color-border-primary);
}

.suggested-content h3 {
  font-size: var(--text-base);
  font-weight: var(--font-medium);
  color: var(--color-text-tertiary);
  margin-bottom: var(--space-4);
}

.suggested-links {
  display: flex;
  justify-content: center;
  gap: var(--space-4);
  flex-wrap: wrap;
}

.suggested-link {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-3) var(--space-5);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-secondary);
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-lg);
  text-decoration: none;
  transition: all var(--transition-fast);
}

.suggested-link:hover {
  border-color: var(--color-primary);
  color: var(--color-primary);
}

/* Background */
.error-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.error-grid {
  position: absolute;
  inset: 0;
  background-image: 
    linear-gradient(var(--color-border-secondary) 1px, transparent 1px),
    linear-gradient(90deg, var(--color-border-secondary) 1px, transparent 1px);
  background-size: 60px 60px;
  opacity: 0.3;
  mask-image: radial-gradient(ellipse 80% 50% at 50% 50%, black 30%, transparent 70%);
}

/* Responsive */
@media (max-width: 640px) {
  .digit {
    font-size: 80px;
  }
  
  .error-icon svg {
    width: 60px;
    height: 60px;
  }
  
  .error-title {
    font-size: var(--text-2xl);
  }
}
</style>
