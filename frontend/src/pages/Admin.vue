<template>
  <div class="min-h-screen bg-gray-100">
    <!-- 顶部导航 -->
    <nav class="bg-white shadow-sm border-b">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center">
            <h1 class="text-xl font-semibold text-gray-900">博客后台管理</h1>
          </div>
          <div class="flex items-center space-x-4">
            <span class="text-sm text-gray-700">欢迎，{{ userStore.username }}</span>
            <button
              @click="handleLogout"
              class="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-md text-sm font-medium"
            >
              退出登录
            </button>
          </div>
        </div>
      </div>
    </nav>

    <div class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
      <!-- 标签页导航 -->
      <div class="mb-6">
        <nav class="flex space-x-8">
          <button
          @click="activeTab = 'articles'"
          :class="[
            'py-2 px-1 border-b-2 font-medium text-sm',
            activeTab === 'articles'
              ? 'border-indigo-500 text-indigo-600'
              : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
          ]"
        >
          文章管理
        </button>
          <button
            @click="activeTab = 'projects'"
            :class="[
              'py-2 px-1 border-b-2 font-medium text-sm',
              activeTab === 'projects'
                ? 'border-indigo-500 text-indigo-600'
                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
            ]"
          >
            项目管理
          </button>
        </nav>
      </div>

      <!-- 内容区域 -->
      <div class="bg-white shadow rounded-lg">
        <ArticleManager v-if="activeTab === 'articles'" />
        <ProjectManager v-if="activeTab === 'projects'" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { logout } from '@/api/auth'
import ArticleManager from '@/components/admin/ArticleManager.vue'
import ProjectManager from '@/components/admin/ProjectManager.vue'

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref('articles')

const handleLogout = async () => {
  try {
    await logout()
  } catch (error) {
    console.error('登出失败:', error)
  } finally {
    userStore.logout()
    router.push('/login')
  }
}
</script>
