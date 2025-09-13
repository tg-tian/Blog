<template>
  <div class="p-6">
    <!-- 头部操作区 -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="heading-lg">项目管理</h2>
      <button
        @click="showCreateModal = true"
        class="btn-primary"
      >
        新建项目
      </button>
    </div>

    <!-- 项目列表 -->
    <div class="overflow-hidden shadow ring-1 ring-black ring-opacity-5 md:rounded-lg">
      <table class="table-base">
        <thead class="table-header">
          <tr>
            <th class="table-header-cell">
              标题
            </th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              描述
            </th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              技术栈
            </th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              创建时间
            </th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              操作
            </th>
          </tr>
        </thead>
        <tbody class="table-body">
          <tr v-for="project in projects" :key="project.id">
            <td class="table-cell">
              <div class="text-sm font-medium text-gray-900">{{ project.title }}</div>
            </td>
            <td class="table-cell">
              <div class="text-sm text-gray-900 max-w-xs truncate">{{ project.description }}</div>
            </td>
            <td class="table-cell">
              <div class="flex flex-wrap gap-1">
                <span
                  v-for="tag in project.tags?.slice(0, 3)"
                  :key="tag.id"
                  class="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-blue-100 text-blue-800"
                >
                  {{ tag.name }}
                </span>
                <span v-if="project.tags?.length > 3" class="text-xs text-gray-500">
                  +{{ project.tags.length - 3 }}
                </span>
              </div>
            </td>
            <td class="table-cell">
              {{ formatDate(project.createTime) }}
            </td>
            <td class="table-cell">
              <button
                @click="editProject(project)"
                class="btn-text mr-4"
              >
                编辑
              </button>
              <button
                @click="deleteProject(project.id)"
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
    <ProjectModal
      v-if="showCreateModal || showEditModal"
      :project="editingProject"
      :is-edit="showEditModal"
      @close="closeModal"
      @save="handleSave"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getProjects, deleteProject as deleteProjectApi } from '@/api/project'
import { formatDateForDisplay } from '@/utils/dateUtils'
import ProjectModal from './ProjectModal.vue'

const projects = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const showCreateModal = ref(false)
const showEditModal = ref(false)
const editingProject = ref(null)

const loadProjects = async () => {
  try {
    const response = await getProjects(currentPage.value, pageSize.value)
    console.log('加载项目:', response)
    projects.value = response.data?.list || response.data || response
    total.value = response.data?.total || projects.value.length
  } catch (error) {
    console.error('加载项目失败:', error)
  }
}

const editProject = (project) => {
  editingProject.value = { ...project }
  showEditModal.value = true
}

const deleteProject = async (id) => {
  if (confirm('确定要删除这个项目吗？')) {
    try {
      await deleteProjectApi(id)
      await loadProjects()
    } catch (error) {
      console.error('删除项目失败:', error)
    }
  }
}

const closeModal = () => {
  showCreateModal.value = false
  showEditModal.value = false
  editingProject.value = null
}

const handleSave = async () => {
  await loadProjects()
  closeModal()
}

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
    loadProjects()
  }
}

const nextPage = () => {
  if (currentPage.value * pageSize.value < total.value) {
    currentPage.value++
    loadProjects()
  }
}

// 使用通用的日期格式化函数
const formatDate = formatDateForDisplay

onMounted(() => {
  loadProjects()
})
</script>
