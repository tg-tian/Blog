<template>
  <div class="p-6">
    <!-- 头部操作区 -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="heading-lg">标签管理</h2>
      <button @click="showCreateModal = true" class="btn-primary">
        新建标签
      </button>
    </div>



    <!-- 标签列表 -->
    <div class="overflow-hidden shadow ring-1 ring-black ring-opacity-5 md:rounded-lg">
      <table class="table-base">
        <thead class="table-header">
          <tr>
            <th class="table-header-cell">名称</th>
            <th class="table-header-cell">标识</th>
            <th class="table-header-cell">描述</th>
            <th class="table-header-cell">文章数量</th>
            <th class="table-header-cell">创建时间</th>
            <th class="table-header-cell">操作</th>
          </tr>
        </thead>
        <tbody class="table-body">
          <tr v-for="tag in tags" :key="tag.id">
            <td class="table-cell">
              <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium text-white"
                :style="{ backgroundColor: tag.color || '#3B82F6' }">
                {{ tag.name }}
              </span>
            </td>
            <td class="table-cell">
              <span
                class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-gray-100 text-gray-800">
                {{ tag.slug }}
              </span>
            </td>
            <td class="table-cell">
              <div class="text-sm text-gray-500">{{ tag.description || '-' }}</div>
            </td>
            <td class="table-cell">
              <span class="text-sm text-gray-900">{{ tag.articleCount || 0 }}</span>
            </td>
            <td class="table-cell">
              {{ formatDate(tag.createTime) }}
            </td>
            <td class="table-cell">
              <button @click="editTag(tag)" class="btn-text mr-4">
                编辑
              </button>
              <button @click="deleteTag(tag.id)" class="btn-text-danger">
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
    <TagModal v-if="showCreateModal || showEditModal" :tag="editingTag" :is-edit="showEditModal" @close="closeModal"
      @save="handleSave" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getTags, deleteTag as deleteTagApi } from '@/api/tag'
import { formatDateForDisplay } from '@/utils/dateUtils'
import TagModal from './TagModal.vue'

const tags = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const showCreateModal = ref(false)
const showEditModal = ref(false)
const editingTag = ref(null)


const loadTags = async () => {
  try {
    const response = await getTags()
    console.log('加载标签:', response)
    tags.value = response.data || []
    total.value = tags.value.length
  } catch (error) {
    console.error('加载标签失败:', error)
  }
}



const editTag = (tag) => {
  editingTag.value = { ...tag }
  showEditModal.value = true
}

const deleteTag = async (id) => {
  if (confirm('确定要删除这个标签吗？')) {
    try {
      await deleteTagApi(id)
      await loadTags()
    } catch (error) {
      console.error('删除标签失败:', error)
    }
  }
}

const closeModal = () => {
  showCreateModal.value = false
  showEditModal.value = false
  editingTag.value = null
}

const handleSave = async () => {
  await loadTags()
  closeModal()
}

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
    loadTags()
  }
}

const nextPage = () => {
  if (currentPage.value * pageSize.value < total.value) {
    currentPage.value++
    loadTags()
  }
}

// 使用通用的日期格式化函数
const formatDate = formatDateForDisplay

onMounted(() => {
  loadTags()
})
</script>