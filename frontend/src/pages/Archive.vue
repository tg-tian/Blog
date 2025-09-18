<template>
  <div class="m-4">
    <!-- 分类导航页面 -->
    <div v-if="!showList" class="card-base">
      <div class="card-content space-y-6">
        <!-- 标签页切换 -->
        <div class="flex justify-center">
          <div class="bg-gray-100 rounded-lg p-1">
            <button
              @click="activeTab = 'articles'"
              :class="[
                'px-6 py-2 rounded-md font-medium transition-all duration-200',
                activeTab === 'articles'
                  ? 'bg-white text-blue-600 shadow-sm'
                  : 'text-gray-600 hover:text-gray-900'
              ]"
            >
              文章
            </button>
            <button
              @click="activeTab = 'projects'"
              :class="[
                'px-6 py-2 rounded-md font-medium transition-all duration-200',
                activeTab === 'projects'
                  ? 'bg-white text-blue-600 shadow-sm'
                  : 'text-gray-600 hover:text-gray-900'
              ]"
            >
              项目
            </button>
          </div>
        </div>

        <!-- 分类和标签展示 -->
        <div v-if="activeTab === 'articles'" class="space-y-6">
          <!-- 分类 -->
          <div>
            <h3 class="text-lg font-semibold text-gray-900 mb-3">分类</h3>
            <div class="space-y-2">
              <div
                v-for="category in categories.filter(c => c.articleCount > 0)"
                :key="category.categoryId"
                class="flex items-center gap-3 cursor-pointer"
                @click="showCategoryArticles(category)"
              >
                <span class="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-blue-100 text-blue-800 hover:bg-blue-200 transition-colors">{{ category.categoryName }}</span>
                <span class="text-xs text-gray-500">({{ category.articleCount || 0 }})</span>
              </div>
            </div>
          </div>
          
          <!-- 标签 -->
          <div>
            <h3 class="text-lg font-semibold text-gray-900 mb-3">标签</h3>
            <div class="space-y-2">
              <div
                v-for="tag in tags.filter(t => t.articleCount > 0)"
                :key="tag.tagId"
                class="flex items-center gap-3 cursor-pointer"
                @click="showTagArticles(tag)"
              >
                <span class="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-green-100 text-green-800 hover:bg-green-200 transition-colors">{{ tag.tagName }}</span>
                <span class="text-xs text-gray-500">({{ tag.articleCount || 0 }})</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 项目标签展示 -->
        <div v-if="activeTab === 'projects'">
          <h3 class="text-lg font-semibold text-gray-900 mb-3">项目标签</h3>
          <div class="space-y-2">
            <div
              v-for="tag in projectTags.filter(t => t.articleCount > 0)"
              :key="tag.tagId"
              class="flex items-center gap-3 cursor-pointer"
              @click="showTagProjects(tag)"
            >
              <span class="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-purple-100 text-purple-800 hover:bg-purple-200 transition-colors">{{ tag.tagName }}</span>
              <span class="text-xs text-gray-500">({{ tag.articleCount || 0 }})</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 列表展示页面 -->
    <div v-else class="space-y-4">


      <!-- 加载状态 -->
      <div v-if="loading" class="card-base">
        <div class="card-content text-center py-8">
          <div class="text-gray-500">加载中...</div>
        </div>
      </div>

      <!-- 错误状态 -->
      <div v-else-if="error" class="card-base">
        <div class="card-content text-center py-8">
          <div class="text-red-500">{{ error }}</div>
        </div>
      </div>

      <!-- 文章列表 -->
      <div v-else-if="listType === 'articles'">
        <ArticleList :articles="articles" :title="listTitle" :show-back-button="true" @back="backToArchive" />
      </div>

      <!-- 项目列表 -->
      <div v-else-if="listType === 'projects'">
        <ProjectList :projects="projects" :title="listTitle" :show-back-button="true" :loading="loading" :error="error" @back="backToArchive" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCategoryStats } from '@/api/category'
import { getTagStats } from '@/api/tag'
import { getProjectTagStats } from '@/api/project'
import { getArticlesByCategory, getArticlesByTag } from '@/api/article'
import { getProjectsByTag } from '@/api/project'
import ArticleList from '@/components/ArticleList.vue'
import ProjectList from '@/components/ProjectList.vue'

// 当前激活的标签页
const activeTab = ref('articles')

// 页面状态
const showList = ref(false)
const listType = ref('') // 'articles' 或 'projects'
const listTitle = ref('')

// 分类和标签数据
const categories = ref([])
const tags = ref([])
const projectTags = ref([])

// 列表数据
const articles = ref([])
const projects = ref([])
const loading = ref(false)
const error = ref('')

// 加载分类数据
const loadCategories = async () => {
  try {
    const response = await getCategoryStats()
    categories.value = response.data || []
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

// 加载标签数据
const loadTags = async () => {
  try {
    // 分别获取文章标签统计和项目标签统计
    const [articleTagsResponse, projectTagsResponse] = await Promise.all([
      getTagStats(),
      getProjectTagStats()
    ])
    
    tags.value = articleTagsResponse.data || []
    projectTags.value = projectTagsResponse.data || []
  } catch (error) {
    console.error('加载标签失败:', error)
  }
}

// 显示分类文章列表
const showCategoryArticles = async (category) => {
  try {
    loading.value = true
    error.value = ''
    showList.value = true
    listType.value = 'articles'
    listTitle.value = `${category.categoryName}`
    
    const response = await getArticlesByCategory(category.categoryId, 1, 50)
    const data = response.data || response
    articles.value = data.list || data || []
  } catch (err) {
    console.error('加载分类文章失败:', err)
    error.value = '加载文章失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

// 显示标签文章列表
const showTagArticles = async (tag) => {
  try {
    loading.value = true
    error.value = ''
    showList.value = true
    listType.value = 'articles'
    listTitle.value = `${tag.tagName}`
    
    const response = await getArticlesByTag(tag.tagId, 1, 50)
    const data = response.data || response
    articles.value = data.list || data || []
  } catch (err) {
    console.error('加载标签文章失败:', err)
    error.value = '加载文章失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

// 显示标签项目列表
const showTagProjects = async (tag) => {
  try {
    loading.value = true
    error.value = ''
    showList.value = true
    listType.value = 'projects'
    listTitle.value = `${tag.tagName}`
    
    const response = await getProjectsByTag(tag.tagId, 1, 50)
    const data = response.data || response
    projects.value = data.list || data || []
  } catch (err) {
    console.error('加载标签项目失败:', err)
    error.value = '加载项目失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

// 返回归档页面
const backToArchive = () => {
  showList.value = false
  listType.value = ''
  listTitle.value = ''
  articles.value = []
  projects.value = []
  error.value = ''
}

// 组件挂载时加载数据
onMounted(() => {
  loadCategories()
  loadTags()
})
</script>

<style scoped>
/* 可以添加一些自定义样式 */
</style>