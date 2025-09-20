import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getArticles } from '@/api/article'
import { getProjects } from '@/api/project'

export const useHomeStore = defineStore('home', () => {
  // 状态
  const articles = ref([])
  const projects = ref([])
  const loading = ref(false)
  const hasLoaded = ref(false)
  
  // 加载首页数据
  const loadHomeData = async () => {
    // 如果已经加载过数据，则不再重新加载
    if (hasLoaded.value && articles.value.length > 0) {
      return
    }
    
    try {
      loading.value = true
      const [articlesResponse, projectsResponse] = await Promise.all([
        getArticles(1, 10),
        getProjects(1, 6)
      ])
      
      articles.value = articlesResponse.data.list || []
      const projectsData = projectsResponse.data || projectsResponse
      projects.value = projectsData.list || projectsData || []
      
      hasLoaded.value = true
    } catch (error) {
      console.error('加载首页数据失败:', error)
    } finally {
      loading.value = false
    }
  }
  
  // 重置状态
  const resetHomeData = () => {
    articles.value = []
    projects.value = []
    hasLoaded.value = false
  }
  
  return {
    articles,
    projects,
    loading,
    hasLoaded,
    loadHomeData,
    resetHomeData
  }
}, {
  persist: {
    enabled: true,
    strategies: [
      {
        key: 'home',
        storage: localStorage,
        paths: ['articles', 'projects', 'hasLoaded'],
        // 设置过期时间为30分钟
        beforeRestore: (context) => {
          const storedTime = localStorage.getItem('home_timestamp')
          if (storedTime) {
            const expirationTime = 30 * 60 * 1000 // 30分钟，单位毫秒
            const now = new Date().getTime()
            if (now - parseInt(storedTime) > expirationTime) {
              // 数据已过期，清除存储
              localStorage.removeItem('home')
              localStorage.removeItem('home_timestamp')
              return false
            }
          }
          return true
        },
        afterSync: (context) => {
          // 更新时间戳
          localStorage.setItem('home_timestamp', new Date().getTime().toString())
        }
      }
    ]
  }
})