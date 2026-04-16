import { ref, onMounted } from 'vue'

const isDark = ref(false)

export function useTheme() {
  onMounted(() => {
    const savedTheme = localStorage.getItem('theme')
    if (savedTheme) {
      isDark.value = savedTheme === 'dark'
    } else {
      // 检测系统偏好
      isDark.value = window.matchMedia('(prefers-color-scheme: dark)').matches
    }
    updateTheme()
  })

  const toggleTheme = () => {
    isDark.value = !isDark.value
    saveTheme()
    updateTheme()
  }

  const setTheme = (dark: boolean) => {
    isDark.value = dark
    saveTheme()
    updateTheme()
  }

  const saveTheme = () => {
    localStorage.setItem('theme', isDark.value ? 'dark' : 'light')
  }

  const updateTheme = () => {
    if (isDark.value) {
      document.documentElement.classList.add('dark')
    } else {
      document.documentElement.classList.remove('dark')
    }
  }

  return {
    isDark,
    toggleTheme,
    setTheme
  }
}
