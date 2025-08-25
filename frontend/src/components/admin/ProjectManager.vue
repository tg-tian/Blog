<template>
  <div class="p-6">
    <!-- 头部操作区 -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-lg font-medium text-gray-900">项目管理</h2>
      <button
        @click="showCreateModal = true"
        class="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-md text-sm font-medium"
      >
        新建项目
      </button>
    </div>

    <!-- 项目列表 -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div
        v-for="project in projects"
        :key="project.id"
        class="bg-white border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition-shadow"
      >
        <div class="p-6">
          <div class="flex justify-between items-start mb-4">
            <h3 class="text-lg font-medium text-gray-900">{{ project.name }}</h3>
            <div class="flex space-x-2">
              <button
                @click="editProject(project)"
                class="text-indigo-600 hover:text-indigo-900"
              >
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>
                </svg>
              </button>
              <button
                @click="deleteProject(project.id)"
                class="text-red-600 hover:text-red-900"
              >
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
                </svg>
              </button>
            </div>
          </div>
          
          <p class="text-gray-600 text-sm mb-4">{{ project.description }}</p>
          
          <div class="flex flex-wrap gap-2 mb-4">
            <span
              v-for="tech in project.technologies"
              :key="tech"
              class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800"
            >
              {{ tech }}
            </span>
          </div>
          
          <div class="flex justify-between items-center text-sm text-gray-500">
            <span>{{ formatDate(project.createdAt) }}</span>
            <div class="flex space-x-2">
              <a
                v-if="project.demoUrl"
                :href="project.demoUrl"
                target="_blank"
                class="text-indigo-600 hover:text-indigo-900"
              >
                演示
              </a>
              <a
                v-if="project.githubUrl"
                :href="project.githubUrl"
                target="_blank"
                class="text-indigo-600 hover:text-indigo-900"
              >
                代码
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-if="projects.length === 0" class="text-center py-12">
      <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"></path>
      </svg>
      <h3 class="mt-2 text-sm font-medium text-gray-900">暂无项目</h3>
      <p class="mt-1 text-sm text-gray-500">开始创建你的第一个项目吧</p>
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
import ProjectModal from './ProjectModal.vue'

const projects = ref([])
const showCreateModal = ref(false)
const showEditModal = ref(false)
const editingProject = ref(null)

const loadProjects = async () => {
  try {
    const response = await getProjects()
    projects.value = response.data || response
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

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('zh-CN')
}

onMounted(() => {
  loadProjects()
})
</script>
