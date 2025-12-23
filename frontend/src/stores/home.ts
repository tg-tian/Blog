import { defineStore } from 'pinia'
import { ref, type Ref } from 'vue'
import { getArticles } from '@/api/article'
import { getProjects } from '@/api/project'
import type { Article, Project, ApiResponse, Paginated } from '@/types'

export const useHomeStore = defineStore('home', () => {
  const articles: Ref<Article[]> = ref([])
  const projects: Ref<Project[]> = ref([])
  const loading = ref(false)
  const hasLoaded = ref(false)

  const loadHomeData = async () => {
    const storedTime = localStorage.getItem('home_timestamp')
    if (storedTime) {
      const expirationTime = 30 * 60 * 1000
      const now = new Date().getTime()
      if (now - parseInt(storedTime) > expirationTime) {
        resetHomeData()
        localStorage.removeItem('home')
        localStorage.removeItem('home_timestamp')
      }
    }
    if (hasLoaded.value && articles.value.length > 0) {
      return
    }
    try {
      loading.value = true
      const [articlesResponse, projectsResponse] = await Promise.all([
        getArticles(1, 10),
        getProjects(1, 6)
      ])

      const arData = (articlesResponse as ApiResponse<Paginated<Article>>).data
      articles.value = arData?.list || []

      const prData = (projectsResponse as ApiResponse<Paginated<Project>>).data || (projectsResponse as any)
      projects.value = (prData as Paginated<Project>)?.list || (prData as any) || []

      hasLoaded.value = true
      localStorage.setItem('home_timestamp', new Date().getTime().toString())
    } catch (error) {
      console.error('加载首页数据失败:', error)
    } finally {
      loading.value = false
    }
  }

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
    key: 'home',
    storage: localStorage,
    pick: ['articles', 'projects', 'hasLoaded']
  }
})
