<template>
  <div class="auth-page">
    <div class="auth-container">
      <!-- 左侧装饰 -->
      <div class="auth-decoration">
        <div class="auth-pattern"></div>
        <div class="auth-content">
          <h2 class="auth-welcome-title">欢迎回来</h2>
          <p class="auth-welcome-text">
            登录您的账户，继续您的创作之旅
          </p>
          
          <div class="auth-features">
            <div class="auth-feature">
              <div class="feature-icon">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"/>
                  <polyline points="14 2 14 8 20 8"/>
                  <line x1="16" y1="13" x2="8" y2="13"/>
                  <line x1="16" y1="17" x2="8" y2="17"/>
                </svg>
              </div>
              <div class="feature-text">
                <h4>Markdown 写作</h4>
                <p>支持 Markdown 语法，让写作更高效</p>
              </div>
            </div>
            
            <div class="auth-feature">
              <div class="feature-icon">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                  <circle cx="9" cy="7" r="4"/>
                  <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
                  <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
                </svg>
              </div>
              <div class="feature-text">
                <h4>社区互动</h4>
                <p>与读者交流，建立您的粉丝群体</p>
              </div>
            </div>
            
            <div class="auth-feature">
              <div class="feature-icon">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/>
                </svg>
              </div>
              <div class="feature-text">
                <h4>数据统计</h4>
                <p>详细的数据分析，了解您的读者</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 右侧表单 -->
      <div class="auth-form-wrapper">
        <div class="auth-form-container">
          <!-- Logo -->
          <router-link to="/" class="auth-logo">
            <svg viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg" width="40" height="40">
              <rect width="32" height="32" rx="8" fill="url(#login-logo-gradient)"/>
              <path d="M10 22V10h2.5l5 8.5V10H20v12h-2.5l-5-8.5V22H10z" fill="white"/>
              <defs>
                <linearGradient id="login-logo-gradient" x1="0" y1="0" x2="32" y2="32" gradientUnits="userSpaceOnUse">
                  <stop stop-color="#0066FF"/>
                  <stop offset="1" stop-color="#8B5CF6"/>
                </linearGradient>
              </defs>
            </svg>
            <span>LiteBlog</span>
          </router-link>
          
          <h1 class="auth-title">登录账户</h1>
          <p class="auth-subtitle">欢迎回来，请输入您的登录信息</p>
          
          <!-- 社交登录 -->
          <div class="social-login">
            <button class="social-btn">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285F4"/>
                <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853"/>
                <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" fill="#FBBC05"/>
                <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335"/>
              </svg>
              使用 Google 登录
            </button>
            <button class="social-btn">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"/>
              </svg>
              使用 GitHub 登录
            </button>
          </div>
          
          <div class="auth-divider">
            <span>或使用邮箱登录</span>
          </div>
          
          <!-- 登录表单 -->
          <form class="auth-form" @submit.prevent="handleLogin">
            <div class="form-group">
              <label for="email" class="form-label">邮箱地址</label>
              <div class="input-wrapper">
                <svg class="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                  <polyline points="22,6 12,13 2,6"/>
                </svg>
                <input
                  type="email"
                  id="email"
                  v-model="form.email"
                  class="form-input"
                  placeholder="请输入邮箱"
                  required
                />
              </div>
            </div>
            
            <div class="form-group">
              <div class="form-label-row">
                <label for="password" class="form-label">密码</label>
                <a href="#" class="form-link">忘记密码？</a>
              </div>
              <div class="input-wrapper">
                <svg class="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                  <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                </svg>
                <input
                  :type="showPassword ? 'text' : 'password'"
                  id="password"
                  v-model="form.password"
                  class="form-input"
                  placeholder="请输入密码"
                  required
                />
                <button 
                  type="button" 
                  class="input-action"
                  @click="showPassword = !showPassword"
                >
                  <svg v-if="showPassword" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"/>
                    <line x1="1" y1="1" x2="23" y2="23"/>
                  </svg>
                  <svg v-else width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                    <circle cx="12" cy="12" r="3"/>
                  </svg>
                </button>
              </div>
            </div>
            
            <div class="form-checkbox">
              <input type="checkbox" id="remember" v-model="rememberMe" />
              <label for="remember">记住我</label>
            </div>
            
            <button 
              type="submit" 
              class="btn btn-primary btn-lg btn-block"
              :disabled="userStore.loading"
            >
              <span v-if="userStore.loading" class="loading-spinner"></span>
              {{ userStore.loading ? '登录中...' : '登录' }}
            </button>
          </form>
          
          <p class="auth-footer">
            还没有账号？
            <router-link to="/register" class="auth-link">立即注册</router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const form = ref({
  email: '',
  password: ''
})

const showPassword = ref(false)
const rememberMe = ref(false)

const handleLogin = async () => {
  try {
    await userStore.login(form.value.email, form.value.password)
    const redirect = route.query.redirect as string
    router.push(redirect || '/')
  } catch (error) {
    console.error('登录失败:', error)
  }
}
</script>

<style scoped>
.auth-page {
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-bg-secondary);
}

.auth-container {
  width: 100%;
  max-width: 1000px;
  margin: var(--space-4);
  display: grid;
  grid-template-columns: 1fr;
  background: var(--color-bg-primary);
  border-radius: var(--radius-2xl);
  overflow: hidden;
  box-shadow: var(--shadow-xl);
}

@media (min-width: 768px) {
  .auth-container {
    grid-template-columns: 1fr 1fr;
  }
}

/* Decoration Side */
.auth-decoration {
  display: none;
  position: relative;
  background: linear-gradient(135deg, #0066FF 0%, #8B5CF6 100%);
  padding: var(--space-12);
  overflow: hidden;
}

@media (min-width: 768px) {
  .auth-decoration {
    display: flex;
    flex-direction: column;
    justify-content: center;
  }
}

.auth-pattern {
  position: absolute;
  inset: 0;
  background-image: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.05'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
  opacity: 0.5;
}

.auth-content {
  position: relative;
  z-index: 1;
  color: white;
}

.auth-welcome-title {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  margin-bottom: var(--space-4);
}

.auth-welcome-text {
  font-size: var(--text-lg);
  opacity: 0.9;
  margin-bottom: var(--space-10);
  color: white;
}

.auth-features {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
}

.auth-feature {
  display: flex;
  gap: var(--space-4);
}

.feature-icon {
  width: 48px;
  height: 48px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.feature-text h4 {
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  margin-bottom: var(--space-1);
}

.feature-text p {
  font-size: var(--text-sm);
  opacity: 0.8;
  color: white;
}

/* Form Side */
.auth-form-wrapper {
  padding: var(--space-8);
  display: flex;
  align-items: center;
  justify-content: center;
}

@media (min-width: 768px) {
  .auth-form-wrapper {
    padding: var(--space-12);
  }
}

.auth-form-container {
  width: 100%;
  max-width: 360px;
}

.auth-logo {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  text-decoration: none;
  margin-bottom: var(--space-8);
}

.auth-title {
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-2);
}

.auth-subtitle {
  font-size: var(--text-base);
  color: var(--color-text-tertiary);
  margin-bottom: var(--space-8);
}

/* Social Login */
.social-login {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
  margin-bottom: var(--space-6);
}

.social-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-3);
  width: 100%;
  padding: var(--space-3) var(--space-4);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-primary);
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.social-btn:hover {
  background: var(--color-bg-hover);
  border-color: var(--color-text-tertiary);
}

/* Divider */
.auth-divider {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  margin: var(--space-6) 0;
}

.auth-divider::before,
.auth-divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: var(--color-border-primary);
}

.auth-divider span {
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

/* Form */
.auth-form {
  display: flex;
  flex-direction: column;
  gap: var(--space-5);
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
}

.form-label-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.form-label {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-primary);
}

.form-link {
  font-size: var(--text-sm);
  color: var(--color-primary);
  text-decoration: none;
}

.form-link:hover {
  text-decoration: underline;
}

.input-wrapper {
  position: relative;
}

.input-icon {
  position: absolute;
  left: var(--space-4);
  top: 50%;
  transform: translateY(-50%);
  color: var(--color-text-tertiary);
  pointer-events: none;
}

.form-input {
  width: 100%;
  padding: var(--space-3) var(--space-4);
  padding-left: calc(var(--space-4) * 2 + 18px);
  font-size: var(--text-base);
  color: var(--color-text-primary);
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border-primary);
  border-radius: var(--radius-lg);
  transition: all var(--transition-fast);
}

.form-input::placeholder {
  color: var(--color-text-placeholder);
}

.form-input:hover {
  border-color: var(--color-text-tertiary);
}

.form-input:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px rgba(0, 102, 255, 0.15);
}

.input-action {
  position: absolute;
  right: var(--space-4);
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  color: var(--color-text-tertiary);
  padding: var(--space-1);
  transition: color var(--transition-fast);
}

.input-action:hover {
  color: var(--color-text-primary);
}

/* Checkbox */
.form-checkbox {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.form-checkbox input[type="checkbox"] {
  width: 16px;
  height: 16px;
  cursor: pointer;
}

.form-checkbox label {
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  cursor: pointer;
}

/* Button */
.btn-block {
  width: 100%;
}

.loading-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid white;
  border-top-color: transparent;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Footer */
.auth-footer {
  margin-top: var(--space-6);
  text-align: center;
  font-size: var(--text-sm);
  color: var(--color-text-tertiary);
}

.auth-link {
  color: var(--color-primary);
  font-weight: var(--font-medium);
  text-decoration: none;
}

.auth-link:hover {
  text-decoration: underline;
}
</style>
