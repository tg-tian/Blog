<template>
  <div class="p-6">
    <!-- 头部操作区 -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="heading-lg">分类管理</h2>
      <button @click="showCreateModal = true" class="btn-primary">
        新建分类
      </button>
    </div>

    <!-- 分类列表 -->
    <div class="overflow-hidden shadow ring-1 ring-black ring-opacity-5 md:rounded-lg">
      <table class="table-base">
        <thead class="table-header">
          <tr>
            <th class="table-header-cell">名称</th>
            <th class="table-header-cell">标识</th>
            <th class="table-header-cell">描述</th>
            <th class="table-header-cell">创建时间</th>
            <th class="table-header-cell">操作</th>
          </tr>
        </thead>
        <tbody class="table-body">
          <tr v-for="category in categories" :key="category.id">
            <td class="table-cell">
              <div class="text-sm font-medium text-gray-900">{{ category.name }}</div>
            </td>
            <td class="table-cell">
              <span
                class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-gray-100 text-gray-800">
                {{ category.slug }}
              </span>
            </td>
            <td class="table-cell">
              <div class="text-sm text-gray-500">{{ category.description || '-' }}</div>
            </td>
            <td class="table-cell">
              {{ formatDate(category.createTime) }}
            </td>
            <td class="table-cell">
              <button @click="editCategory(category)" class="btn-text mr-4">
                编辑
              </button>
              <button @click="deleteCategory(category.id)" class="btn-text-danger">
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
        <button @click="prevPage" :disabled="currentPage === 1" class="btn-pagination">
          上一页
        </button>
        <button @click="nextPage" :disabled="currentPage * pageSize >= total" class="btn-pagination">
          下一页
        </button>
      </div>
    </div>

    <!-- 创建/编辑模态框 -->
    <CategoryModal v-if="showCreateModal || showEditModal" :category="editingCategory" :is-edit="showEditModal"
      @close="closeModal" @save="handleSave" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCategories, deleteCategory as deleteCategoryApi } from '@/api/category'
import { formatDateForDisplay } from '@/utils/dateUtils'
import CategoryModal from './CategoryModal.vue'

const categories = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const showCreateModal = ref(false)
const showEditModal = ref(false)
const editingCategory = ref(null)

const loadCategories = async () => {
  try {
    const response = await getCategories()
    console.log('加载分类:', response)
    categories.value = response.data || []
    total.value = categories.value.length
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

const editCategory = (category) => {
  editingCategory.value = { ...category }
  showEditModal.value = true
}

const deleteCategory = async (id) => {
  if (confirm('确定要删除这个分类吗？')) {
    try {
      await deleteCategoryApi(id)
      await loadCategories()
    } catch (error) {
      console.error('删除分类失败:', error)
    }
  }
}

const closeModal = () => {
  showCreateModal.value = false
  showEditModal.value = false
  editingCategory.value = null
}

const handleSave = async () => {
  await loadCategories()
  closeModal()
}

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
    loadCategories()
  }
}

const nextPage = () => {
  if (currentPage.value * pageSize.value < total.value) {
    currentPage.value++
    loadCategories()
  }
}

// 使用通用的日期格式化函数
const formatDate = formatDateForDisplay

onMounted(() => {
  loadCategories()
})
</script>