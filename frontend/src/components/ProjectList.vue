<template>
    <div>
        <div class="border-b-2 border-gray-300 pb-4 mb-4 mt-8">
            <h2 class="text-3xl font-bold text-gray-900 flex items-center">
                <svg class="w-8 h-8 mr-3 text-green-600" fill="currentColor" viewBox="0 0 24 24">
                    <path
                        d="M12,15.5A3.5,3.5 0 0,1 8.5,12A3.5,3.5 0 0,1 12,8.5A3.5,3.5 0 0,1 15.5,12A3.5,3.5 0 0,1 12,15.5M19.43,12.97C19.47,12.65 19.5,12.33 19.5,12C19.5,11.67 19.47,11.34 19.43,11L21.54,9.37C21.73,9.22 21.78,8.95 21.66,8.73L19.66,5.27C19.54,5.05 19.27,4.96 19.05,5.05L16.56,6.05C16.04,5.66 15.5,5.32 14.87,5.07L14.5,2.42C14.46,2.18 14.25,2 14,2H10C9.75,2 9.54,2.18 9.5,2.42L9.13,5.07C8.5,5.32 7.96,5.66 7.44,6.05L4.95,5.05C4.73,4.96 4.46,5.05 4.34,5.27L2.34,8.73C2.22,8.95 2.27,9.22 2.46,9.37L4.57,11C4.53,11.34 4.5,11.67 4.5,12C4.5,12.33 4.53,12.65 4.57,12.97L2.46,14.63C2.27,14.78 2.22,15.05 2.34,15.27L4.34,18.73C4.46,18.95 4.73,19.03 4.95,18.95L7.44,17.94C7.96,18.34 8.5,18.68 9.13,18.93L9.5,21.58C9.54,21.82 9.75,22 10,22H14C14.25,22 14.46,21.82 14.5,21.58L14.87,18.93C15.5,18.68 16.04,18.34 16.56,17.94L19.05,18.95C19.27,19.03 19.54,18.95 19.66,18.73L21.66,15.27C21.78,15.05 21.73,14.78 21.54,14.63L19.43,12.97Z" />
                </svg>
                项目列表
            </h2>
        </div>
        
        <!-- 加载状态 -->
        <StatusMessage 
            v-if="loading" 
            type="loading" 
            message="加载项目列表中..." 
        />
        
        <!-- 错误状态 -->
        <StatusMessage 
            v-else-if="error" 
            type="error" 
            :message="error" 
            :show-retry="true" 
            @retry="loadProjects" 
        />
        
        <!-- 项目列表 -->
        <div v-else class="grid-responsive">
            <div v-for="(project, index) in projects" :key="project.id || index" 
                 class="card-base card-content flex flex-col card-hover cursor-pointer"
                 @click="openProject(project.link)">
                <!-- 项目名称 -->
                <h3 class="heading-md mb-3">{{ project.name }}</h3>
                
                <!-- 项目简介 -->
                <p class="text-description mb-4 flex-grow">{{ project.description }}</p>
                
                <!-- 技术栈 -->
                <div class="mb-4" v-if="project.technologies && project.technologies.length > 0">
                    <h4 class="text-sm font-semibold text-gray-700 mb-2">技术栈：</h4>
                    <div class="flex flex-wrap gap-2">
                        <span v-for="tech in project.technologies" :key="tech" 
                              class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                            {{ tech }}
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getProjects } from '@/api/project'
import { getFileUrl } from '@/utils/upload'
import StatusMessage from './StatusMessage.vue'

const projects = ref([])
const loading = ref(true)
const error = ref('')

// 加载项目列表
const loadProjects = async () => {
  try {
    loading.value = true
    error.value = ''
    const response = await getProjects()
    const projectsData = response.data || response
    
    // 处理项目数据，包括封面图片URL转换
    projects.value = await Promise.all(
      projectsData.map(async (project) => {
        let coverImageUrl = ''
        if (project.coverImage) {
          try {
            coverImageUrl = await getFileUrl(project.coverImage)
          } catch (err) {
            console.error('获取项目封面失败:', err)
          }
        }
        
        return {
          id: project.id,
          name: project.title, // 使用title作为name
          description: project.description,
          content: project.content,
          coverImage: coverImageUrl,
          link: project.link,
          technologies: project.tags ? project.tags.map(tag => tag.name) : [], // 使用tags作为technologies
          createTime: project.createTime,
          updateTime: project.updateTime
        }
      })
    )
  } catch (err) {
    console.error('加载项目列表失败:', err)
    error.value = '加载项目列表失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

// 处理项目卡片点击事件
const openProject = (link) => {
    if (link) {
        window.open(link, '_blank')
    }
}

// 组件挂载时加载数据
onMounted(() => {
  loadProjects()
})
</script>
