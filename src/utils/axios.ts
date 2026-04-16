import axios from 'axios'

const axiosInstance = axios.create({
  // 根据环境变量设置 API 地址，生产环境必须设置 VITE_API_BASE_URL
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 15000, // 增加超时时间
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
axiosInstance.interceptors.request.use(
  (config) => {
    const token = sessionStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
axiosInstance.interceptors.response.use(
  (response) => {
    return response
  },
  async (error) => {
    const originalRequest = error.config
    
    // 处理401未授权错误
    if (error.response?.status === 401) {
      sessionStorage.removeItem('token')
      // 只有在非登录页面时才跳转
      if (!window.location.pathname.includes('/login')) {
        window.location.href = '/login'
      }
    }
    
    // 处理网络错误或超时，进行重试
    if (!error.response && !originalRequest._retry && originalRequest.method === 'get') {
      originalRequest._retry = true
      
      // 指数退避重试
      const retryDelay = Math.min(1000 * Math.pow(2, originalRequest._retryCount || 0), 10000)
      await new Promise(resolve => setTimeout(resolve, retryDelay))
      
      return axiosInstance(originalRequest)
    }
    
    return Promise.reject(error)
  }
)

export default axiosInstance
