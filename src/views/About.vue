<template>
  <div class="about-page">
    <!-- Hero Section -->
    <section class="about-hero">
      <div class="container-app">
        <div class="hero-content">
          <div class="hero-badge">
            <span class="badge-dot"></span>
            <span>v1.0.0</span>
          </div>
          <h1 class="hero-title">
            关于 <span class="gradient-text">LiteBlog</span>
          </h1>
          <p class="hero-description">
            一个优雅、简洁、高效的企业级轻量博客平台，
            专注于内容创作与分享，让每一个想法都被记录。
          </p>
        </div>
      </div>
      
      <div class="hero-bg">
        <div class="hero-grid"></div>
        <div class="hero-glow"></div>
      </div>
    </section>
    
    <!-- 特性展示 -->
    <section class="features-section">
      <div class="container-app">
        <div class="section-header">
          <h2 class="section-title">核心特性</h2>
          <p class="section-subtitle">为创作者打造的现代化博客体验</p>
        </div>
        
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon-wrapper">
              <div class="feature-icon" style="background: linear-gradient(135deg, #0066FF 0%, #3385FF 100%)">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
                  <path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"/>
                  <polyline points="14 2 14 8 20 8"/>
                  <line x1="16" y1="13" x2="8" y2="13"/>
                  <line x1="16" y1="17" x2="8" y2="17"/>
                </svg>
              </div>
            </div>
            <h3 class="feature-title">Markdown 写作</h3>
            <p class="feature-description">
              原生支持 Markdown 语法，让写作更加高效。实时预览，所见即所得，
              支持代码高亮、表格、数学公式等丰富格式。
            </p>
          </div>
          
          <div class="feature-card">
            <div class="feature-icon-wrapper">
              <div class="feature-icon" style="background: linear-gradient(135deg, #8B5CF6 0%, #A78BFA 100%)">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
                  <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"/>
                  <line x1="7" y1="7" x2="7.01" y2="7"/>
                </svg>
              </div>
            </div>
            <h3 class="feature-title">标签管理</h3>
            <p class="feature-description">
              灵活的标签系统，帮助您组织内容。支持多标签、彩色标记，
              让读者轻松发现感兴趣的内容。
            </p>
          </div>
          
          <div class="feature-card">
            <div class="feature-icon-wrapper">
              <div class="feature-icon" style="background: linear-gradient(135deg, #10B981 0%, #34D399 100%)">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
                  <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
                </svg>
              </div>
            </div>
            <h3 class="feature-title">评论互动</h3>
            <p class="feature-description">
              内置评论系统，支持 Markdown 格式评论。与读者建立连接，
              收集反馈，构建活跃的社区氛围。
            </p>
          </div>
          
          <div class="feature-card">
            <div class="feature-icon-wrapper">
              <div class="feature-icon" style="background: linear-gradient(135deg, #F59E0B 0%, #FBBF24 100%)">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
                  <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
                  <line x1="3" y1="9" x2="21" y2="9"/>
                  <line x1="9" y1="21" x2="9" y2="9"/>
                </svg>
              </div>
            </div>
            <h3 class="feature-title">管理后台</h3>
            <p class="feature-description">
              强大的管理后台，管理文章、用户、标签和评论。
              数据统计，让您了解读者喜好。
            </p>
          </div>
          
          <div class="feature-card">
            <div class="feature-icon-wrapper">
              <div class="feature-icon" style="background: linear-gradient(135deg, #EC4899 0%, #F472B6 100%)">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
                  <rect x="2" y="3" width="20" height="14" rx="2" ry="2"/>
                  <line x1="8" y1="21" x2="16" y2="21"/>
                  <line x1="12" y1="17" x2="12" y2="21"/>
                </svg>
              </div>
            </div>
            <h3 class="feature-title">响应式设计</h3>
            <p class="feature-description">
              完美适配各种设备，从桌面到移动端，提供一致的优质体验。
              随时随地，畅享阅读与创作。
            </p>
          </div>
          
          <div class="feature-card">
            <div class="feature-icon-wrapper">
              <div class="feature-icon" style="background: linear-gradient(135deg, #EF4444 0%, #F87171 100%)">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
                  <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
                </svg>
              </div>
            </div>
            <h3 class="feature-title">安全可靠</h3>
            <p class="feature-description">
              JWT 认证机制，保护用户数据安全。密码加密存储，
              让您的账户安全无忧。
            </p>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 技术栈 -->
    <section class="tech-section">
      <div class="container-app">
        <div class="section-header">
          <h2 class="section-title">技术栈</h2>
          <p class="section-subtitle">采用现代化的技术架构，确保性能与可维护性</p>
        </div>
        
        <div class="tech-grid">
          <div class="tech-category">
            <h3 class="tech-category-title">前端</h3>
            <div class="tech-items">
              <div class="tech-item">
                <div class="tech-icon vue">
                  <svg viewBox="0 0 128 128" width="32" height="32">
                    <path fill="#42b883" d="M78.8,10L64,35.4L49.2,10H0l64,110l64-110C128,10,78.8,10,78.8,10z"/>
                    <path fill="#35495e" d="M78.8,10L64,35.4L49.2,10H25.6L64,76l38.4-66H78.8z"/>
                  </svg>
                </div>
                <div class="tech-info">
                  <h4>Vue 3</h4>
                  <p>渐进式 JavaScript 框架</p>
                </div>
              </div>
              
              <div class="tech-item">
                <div class="tech-icon ts">
                  <svg viewBox="0 0 128 128" width="32" height="32">
                    <rect fill="#3178c6" width="128" height="128" rx="6"/>
                    <path fill="#fff" d="M82.2 89.9v11.5c1.9 1 4.1 1.7 6.4 2.2s4.8.7 7.3.7c2.5 0 4.9-.3 7.1-.8s4.2-1.4 5.8-2.6 2.9-2.7 3.9-4.5 1.4-4.1 1.4-6.7c0-1.9-.3-3.5-.8-5s-1.4-2.8-2.4-4-2.4-2.3-4-3.3-3.4-2.1-5.5-3.1c-1.5-.7-2.8-1.3-4-1.9s-2.1-1.2-2.9-1.8-1.3-1.2-1.7-1.8-.6-1.3-.6-2c0-.6.1-1.1.4-1.6s.7-.9 1.2-1.2 1.2-.6 2-.8 1.7-.3 2.7-.3c.7 0 1.4.1 2.1.2s1.4.3 2 .5 1.3.5 1.9.8 1.1.7 1.5 1.1v-10.7c-1-.4-2.2-.7-3.4-.9s-2.7-.3-4.3-.3c-2.5 0-4.8.4-6.9 1.1s-4 1.8-5.5 3.1-2.8 2.9-3.6 4.8-1.3 4-1.3 6.3c0 2.4.5 4.5 1.4 6.3s2.3 3.3 4 4.6 3.9 2.5 6.4 3.6c1.3.5 2.5 1 3.6 1.5s2 .9 2.8 1.4 1.4 1 1.8 1.6.7 1.2.7 1.9c0 .5-.1 1-.4 1.5s-.6.8-1.1 1.2-1.1.6-1.9.8-1.6.3-2.6.3c-1.7 0-3.3-.3-4.9-.9s-3-1.5-4.2-2.7zm-34.6-34h15.8v-9.7h-43.6v9.7h15.7v46h12.1z"/>
                  </svg>
                </div>
                <div class="tech-info">
                  <h4>TypeScript</h4>
                  <p>类型安全的 JavaScript</p>
                </div>
              </div>
              
              <div class="tech-item">
                <div class="tech-icon tailwind">
                  <svg viewBox="0 0 128 128" width="32" height="32">
                    <path d="M64.004 25.602c-17.067 0-27.73 8.53-32 25.597 6.398-8.531 13.867-11.73 22.398-9.597 4.871 1.214 8.352 4.746 12.207 8.66C72.883 56.629 80.145 64 96.004 64c17.066 0 27.73-8.531 32-25.602-6.399 8.536-13.867 11.735-22.399 9.602-4.87-1.215-8.347-4.746-12.207-8.66-6.27-6.367-13.53-13.738-29.394-13.738zM32.004 64c-17.066 0-27.73 8.531-32 25.602C6.402 81.066 13.87 77.867 22.402 80c4.871 1.215 8.352 4.746 12.207 8.66 6.274 6.367 13.536 13.738 29.395 13.738 17.066 0 27.73-8.53 32-25.597-6.399 8.531-13.867 11.73-22.399 9.597-4.87-1.214-8.347-4.746-12.207-8.66C55.128 71.371 47.868 64 32.004 64zm0 0" fill="#38bdf8"/>
                  </svg>
                </div>
                <div class="tech-info">
                  <h4>Tailwind CSS</h4>
                  <p>原子化 CSS 框架</p>
                </div>
              </div>
            </div>
          </div>
          
          <div class="tech-category">
            <h3 class="tech-category-title">后端</h3>
            <div class="tech-items">
              <div class="tech-item">
                <div class="tech-icon java">
                  <svg viewBox="0 0 128 128" width="32" height="32">
                    <path fill="#0074bd" d="M47.617 98.12s-4.767 2.774 3.397 3.71c9.892 1.13 14.947.968 25.845-1.092 0 0 2.871 1.795 6.873 3.351-24.439 10.47-55.308-.607-36.115-5.969zm-2.988-13.665s-5.348 3.959 2.823 4.805c10.567 1.091 18.91 1.18 33.354-1.6 0 0 1.993 2.025 5.132 3.131-29.542 8.64-62.446.68-41.309-6.336z"/>
                    <path fill="#ea2d2e" d="M69.802 61.271c6.025 6.935-1.58 13.17-1.58 13.17s15.289-7.891 8.269-17.777c-6.559-9.215-11.587-13.792 15.635-29.58 0 .001-42.731 10.67-22.324 34.187z"/>
                    <path fill="#ea2d2e" d="M102.123 108.229s3.529 2.91-3.888 5.159c-14.102 4.272-58.706 5.56-71.094.171-4.451-1.938 3.899-4.625 6.526-5.192 2.739-.593 4.303-.485 4.303-.485-4.953-3.487-32.013 6.85-13.743 9.815 49.821 8.076 90.817-3.637 77.896-9.468zM49.912 70.294s-22.686 5.389-8.033 7.348c6.188.828 18.518.638 30.011-.326 9.39-.789 18.813-2.474 18.813-2.474s-3.308 1.419-5.704 3.053c-23.042 6.061-67.644 3.238-54.831-2.958 10.832-5.239 19.744-4.643 19.744-4.643zm40.697 22.747c23.421-12.167 12.591-23.86 5.032-22.285-1.848.385-2.677.72-2.677.72s.688-1.079 2-1.543c14.953-5.255 26.451 15.503-4.823 23.725 0-.002.359-.327.468-.617z"/>
                    <path fill="#ea2d2e" d="M76.491 1.587S89.459 14.563 64.188 34.51c-20.266 16.006-4.621 25.13-.007 35.559-11.831-10.673-20.509-20.07-14.688-28.815C58.041 28.42 81.722 22.195 76.491 1.587z"/>
                    <path fill="#0074bd" d="M52.214 126.021c22.476 1.437 56.999-.8 57.818-11.436 0 0-1.571 4.032-18.577 7.231-19.186 3.612-42.854 3.191-56.887.874 0 .001 2.875 2.381 17.646 3.331z"/>
                  </svg>
                </div>
                <div class="tech-info">
                  <h4>Java 17</h4>
                  <p>企业级编程语言</p>
                </div>
              </div>
              
              <div class="tech-item">
                <div class="tech-icon spring">
                  <svg viewBox="0 0 128 128" width="32" height="32">
                    <path fill="#6DB33F" d="M117.5 33.5l.3-.2c-.6-1.1-1.5-2.1-2.4-2.6L67.1 2.9c-.8-.5-1.9-.7-3.1-.7-1.2 0-2.3.3-3.1.7l-48 27.9c-1.7 1-2.9 3.5-2.9 5.4v55.7c0 1.1.2 2.3.9 3.4l-.2.1c.5.8 1.2 1.5 1.9 1.9l48.2 27.9c.8.5 1.9.7 3.1.7 1.2 0 2.3-.3 3.1-.7l48-27.9c1.7-1 2.9-3.5 2.9-5.4V36.1c.1-.8 0-1.7-.4-2.6zM64 88.5c9.1 0 17.1-5 21.3-12.4l12.9 7.6c-6.8 11.8-19.6 19.8-34.2 19.8-21.8 0-39.5-17.7-39.5-39.5S42.2 24.5 64 24.5c14.7 0 27.5 8.1 34.3 20l-13 7.5C81.1 44.5 73.1 39.5 64 39.5c-13.5 0-24.5 11-24.5 24.5s11 24.5 24.5 24.5z"/>
                  </svg>
                </div>
                <div class="tech-info">
                  <h4>Spring Boot 3</h4>
                  <p>企业级 Java 框架</p>
                </div>
              </div>
              
              <div class="tech-item">
                <div class="tech-icon mysql">
                  <svg viewBox="0 0 128 128" width="32" height="32">
                    <path fill="#00618A" d="M116.948 97.807c-6.863-.187-12.104.452-16.585 2.341-1.273.537-3.305.552-3.539 2.147.707.743.814 1.85 1.37 2.77 1.07 1.73 2.872 4.052 4.497 5.27 1.813 1.36 3.643 2.821 5.565 4.017 3.418 2.127 7.223 3.346 10.511 5.477 1.934 1.254 3.854 2.84 5.73 4.237.939.698 1.563 1.783 2.76 2.209v-.243c-.633-.808-.796-1.919-1.37-2.77-.854-.853-1.708-1.605-2.56-2.46-2.503-3.326-5.604-6.26-8.938-8.627-2.621-1.867-8.453-4.32-9.544-7.365l-.186-.2c1.842-.208 4.006-.876 5.757-1.412 2.875-.771 5.489-.59 8.434-1.356 1.37-.37 2.74-.789 4.11-1.183v-.763c-1.535-1.581-2.627-3.659-4.253-5.121-4.32-3.876-9.035-7.604-13.928-10.776-2.587-1.68-5.84-2.76-8.567-4.237-.923-.48-2.547-.729-3.155-1.478-1.444-1.84-2.233-4.172-3.315-6.302-2.326-4.575-4.607-9.618-6.586-14.463-1.405-3.44-2.312-6.835-4.06-10.04-8.226-15.098-17.074-24.23-30.756-33.207-2.913-1.91-6.417-2.74-10.119-3.747-1.99-.078-3.98-.158-5.97-.238-1.2-.505-2.451-1.983-3.54-2.698C28.218 2.15 14.458-2.893 10.457 8.24c-2.548 7.086 3.822 13.967 6.043 17.527 1.587 2.537 3.62 5.386 4.737 8.177.744 1.859.896 3.724 1.588 5.69 1.623 4.598 3.06 9.616 5.184 13.88 1.088 2.187 2.28 4.482 3.665 6.444.84 1.143 2.275 1.645 2.56 3.32-1.442 2.016-1.523 5.151-2.327 7.699-3.645 11.513-2.272 25.807 2.97 34.325 1.614 2.62 5.406 8.29 10.55 6.09 4.535-1.95 3.522-7.992 4.83-13.328.299-1.163.116-2.015.69-2.806v.186c1.354 2.724 2.709 5.449 4.06 8.174 3.012 4.842 8.353 9.899 12.782 13.286 2.327 1.778 4.157 4.844 7.06 5.972v-.243h-.186c-.576-.911-1.48-1.29-2.221-2.02-1.765-1.771-3.733-3.976-5.12-6.03-4.06-5.56-7.653-11.65-10.895-17.968-1.56-3.041-2.922-6.398-4.207-9.47-.503-1.203-.502-3.023-1.588-3.657-1.469 2.277-3.615 4.118-4.66 6.85-1.776 4.618-2.002 10.24-2.638 16.06-.388.14-.216.047-.4.198-3.064-.738-4.145-3.83-5.303-6.44-2.893-6.524-3.432-17.038-.884-24.564.658-1.945 3.63-8.064 2.45-9.9-.58-1.88-2.5-2.977-3.54-4.44-1.314-1.846-2.626-4.28-3.54-6.385-2.374-5.358-3.483-11.4-5.98-16.82-1.193-2.586-3.214-5.207-4.86-7.619-1.822-2.674-3.857-4.641-5.31-7.806-.506-1.104-1.19-2.87-.443-4.06.24-.506.592-.728 1.18-.88 1.1-.377 4.41.285 5.53.69 3.89 1.45 7.134 2.827 10.393 4.86 1.533.956 3.078 2.805 4.95 3.32h2.148c3.36.77 7.122.236 10.237 1.18 5.54 1.683 10.51 4.3 14.988 7.077 13.697 8.499 24.913 20.597 32.538 35.046 1.23 2.382 1.76 4.66 2.85 7.2 2.154 5.084 4.856 10.334 6.946 15.31 2.084 4.962 4.12 9.96 7.066 14.058 1.548 2.152 7.528 3.302 10.25 4.497 1.908.84 5.035 1.676 6.805 2.77 3.584 2.28 7.052 4.85 10.433 7.347 1.68 1.237 6.844 3.806 7.161 5.872z"/>
                    <path fill="#F29111" d="M58.186 43.118c-1.838-.05-3.128.19-4.273.564v.2h.2c.832 1.708 2.3 2.81 3.27 4.325l2.36 4.922.2-.2c1.465-1.032 2.136-2.683 1.87-4.922-.514-.54-.588-1.215-1.032-1.87-.587-.935-1.82-1.466-2.595-2.02z"/>
                  </svg>
                </div>
                <div class="tech-info">
                  <h4>MySQL 8</h4>
                  <p>关系型数据库</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    
    <!-- CTA -->
    <section class="cta-section">
      <div class="container-app">
        <div class="cta-card">
          <div class="cta-content">
            <h2 class="cta-title">准备好开始了吗？</h2>
            <p class="cta-description">
              立即注册，开启您的创作之旅
            </p>
          </div>
          <div class="cta-actions">
            <router-link to="/register" class="btn btn-primary btn-lg">
              免费注册
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M5 12h14M12 5l7 7-7 7"/>
              </svg>
            </router-link>
            <router-link to="/" class="btn btn-secondary btn-lg">
              浏览文章
            </router-link>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
// No script needed for this static page
</script>

<style scoped>
.about-page {
  min-height: calc(100vh - 64px);
}

/* ============================================
   HERO SECTION
   ============================================ */

.about-hero {
  position: relative;
  padding: var(--space-20) 0 var(--space-16);
  text-align: center;
  overflow: hidden;
}

.hero-content {
  position: relative;
  z-index: 1;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-4);
  background: var(--color-bg-tertiary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-full);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-secondary);
  margin-bottom: var(--space-6);
}

.badge-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--color-success);
}

.hero-title {
  font-size: clamp(2.5rem, 5vw, 4rem);
  font-weight: var(--font-bold);
  line-height: 1.1;
  letter-spacing: -0.03em;
  color: var(--color-text-primary);
  margin-bottom: var(--space-6);
}

.hero-description {
  font-size: var(--text-xl);
  line-height: var(--leading-relaxed);
  color: var(--color-text-secondary);
  max-width: 600px;
  margin: 0 auto;
}

.hero-bg {
  position: absolute;
  inset: 0;
  overflow: hidden;
  pointer-events: none;
}

.hero-grid {
  position: absolute;
  inset: 0;
  background-image: 
    linear-gradient(var(--color-border-secondary) 1px, transparent 1px),
    linear-gradient(90deg, var(--color-border-secondary) 1px, transparent 1px);
  background-size: 60px 60px;
  opacity: 0.5;
  mask-image: radial-gradient(ellipse 80% 50% at 50% 0%, black 70%, transparent 100%);
}

.hero-glow {
  position: absolute;
  top: -50%;
  left: 50%;
  transform: translateX(-50%);
  width: 800px;
  height: 600px;
  background: radial-gradient(circle, rgba(0, 102, 255, 0.15) 0%, transparent 70%);
  filter: blur(60px);
}

/* ============================================
   FEATURES SECTION
   ============================================ */

.features-section {
  padding: var(--space-20) 0;
  background: var(--color-bg-primary);
}

.section-header {
  text-align: center;
  margin-bottom: var(--space-12);
}

.section-title {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-4);
}

.section-subtitle {
  font-size: var(--text-lg);
  color: var(--color-text-tertiary);
}

.features-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: var(--space-6);
}

@media (min-width: 640px) {
  .features-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 1024px) {
  .features-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

.feature-card {
  padding: var(--space-6);
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border-secondary);
  border-radius: var(--radius-xl);
  transition: all var(--transition-base);
}

.feature-card:hover {
  border-color: var(--color-border-primary);
  box-shadow: var(--shadow-lg);
  transform: translateY(-2px);
}

.feature-icon-wrapper {
  margin-bottom: var(--space-4);
}

.feature-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
}

.feature-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-3);
}

.feature-description {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
  line-height: var(--leading-relaxed);
}

/* ============================================
   TECH SECTION
   ============================================ */

.tech-section {
  padding: var(--space-20) 0;
}

.tech-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: var(--space-8);
}

@media (min-width: 768px) {
  .tech-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

.tech-category-title {
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-4);
  padding-bottom: var(--space-3);
  border-bottom: 1px solid var(--color-border-secondary);
}

.tech-items {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.tech-item {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  padding: var(--space-4);
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-secondary);
  border-radius: var(--radius-lg);
  transition: all var(--transition-fast);
}

.tech-item:hover {
  border-color: var(--color-border-primary);
  background: var(--color-bg-secondary);
}

.tech-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.tech-info h4 {
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-1);
}

.tech-info p {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

/* ============================================
   CTA SECTION
   ============================================ */

.cta-section {
  padding: var(--space-16) 0;
  background: var(--color-bg-primary);
}

.cta-card {
  padding: var(--space-12);
  background: linear-gradient(135deg, #0066FF 0%, #8B5CF6 100%);
  border-radius: var(--radius-2xl);
  text-align: center;
}

.cta-title {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: white;
  margin-bottom: var(--space-4);
}

.cta-description {
  font-size: var(--text-lg);
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: var(--space-8);
}

.cta-actions {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: var(--space-4);
}

.cta-card .btn-primary {
  background: white;
  color: var(--color-primary);
  border-color: white;
}

.cta-card .btn-primary:hover {
  background: rgba(255, 255, 255, 0.9);
}

.cta-card .btn-secondary {
  background: transparent;
  color: white;
  border-color: rgba(255, 255, 255, 0.5);
}

.cta-card .btn-secondary:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: white;
}
</style>
