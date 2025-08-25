<template>
  <div class="p-6">
    <!-- 头部操作区 -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="heading-lg">文章管理</h2>
      <button
        @click="showCreateModal = true"
        class="btn-primary"
      >
        新建文章
      </button>
    </div>

    <!-- 文章列表 -->
    <div class="overflow-hidden shadow ring-1 ring-black ring-opacity-5 md:rounded-lg">
      <table class="table-base">
        <thead class="table-header">
          <tr>
            <th class="table-header-cell">
              标题
            </th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              发布时间
            </th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              浏览量
            </th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              操作
            </th>
          </tr>
        </thead>
        <tbody class="table-body">
          <tr v-for="article in articles" :key="article.id">
            <td class="table-cell">
              <div class="text-sm font-medium text-gray-900">{{ article.title }}</div>
            </td>
            <td class="table-cell">
              {{ formatDate(article.publishTime) }}
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
              {{ article.views }}
            </td>
            <td class="table-cell">
              <button
                @click="editArticle(article)"
                class="btn-text mr-4"
              >
                编辑
              </button>
              <button
                @click="deleteArticle(article.id)"
                class="btn-text-danger"
              >
                删除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 分页 -->
    <div class="mt-6 flex justify-between items-center">
      <div class="text-sm text-gray-700">
        共 {{ total }} 条记录
      </div>
      <div class="flex space-x-2">
        <button
          @click="prevPage"
          :disabled="currentPage === 1"
          class="btn-pagination"
        >
          上一页
        </button>
        <button
          @click="nextPage"
          :disabled="currentPage * pageSize >= total"
          class="btn-pagination"
        >
          下一页
        </button>
      </div>
    </div>

    <!-- 创建/编辑模态框 -->
    <ArticleModal
      v-if="showCreateModal || showEditModal"
      :article="editingArticle"
      :is-edit="showEditModal"
      @close="closeModal"
      @save="handleSave"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getArticles, deleteArticle as deleteArticleApi } from '@/api/article'
import { formatDateForDisplay } from '@/utils/dateUtils'
import ArticleModal from './ArticleModal.vue'

const articles = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const showCreateModal = ref(false)
const showEditModal = ref(false)
const editingArticle = ref(null)

const loadArticles = async () => {
  try {
    const response = await getArticles(currentPage.value, pageSize.value)
    console.log('加载文章:', response)
    articles.value = response.data.list
    total.value = response.data.total
  } catch (error) {
    console.error('加载文章失败:', error)
  }
}

const editArticle = (article) => {
  editingArticle.value = { ...article }
  showEditModal.value = true
}

const deleteArticle = async (id) => {
  if (confirm('确定要删除这篇文章吗？')) {
    try {
      await deleteArticleApi(id)
      await loadArticles()
    } catch (error) {
      console.error('删除文章失败:', error)
    }
  }
}

const closeModal = () => {
  showCreateModal.value = false
  showEditModal.value = false
  editingArticle.value = null
}

const handleSave = async () => {
  await loadArticles()
  closeModal()
}

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
    loadArticles()
  }
}

const nextPage = () => {
  if (currentPage.value * pageSize.value < total.value) {
    currentPage.value++
    loadArticles()
  }
}

// 使用通用的日期格式化函数
const formatDate = formatDateForDisplay

onMounted(() => {
  loadArticles()
})
</script>