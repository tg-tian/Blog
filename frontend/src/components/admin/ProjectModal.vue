<template>
  <div class="modal-backdrop">
    <div class="modal-container">
      <div class="mt-3">
        <!-- 头部 -->
        <div class="modal-header">
          <h3 class="heading-lg">
            {{ isEdit ? '编辑项目' : '新建项目' }}
          </h3>
          <button @click="$emit('close')" class="btn-close">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
            </svg>
          </button>
        </div>

        <!-- 表单 -->
        <form @submit.prevent="handleSubmit" class="content-spacing">
          <div class="grid-2-cols">
            <div>
              <label class="label-base">
                项目标题 *
              </label>
              <input v-model="form.title" type="text" required class="input-base" placeholder="请输入项目标题" />
            </div>
            <div>
              <label class="label-base">
                排序
              </label>
              <input v-model="form.orderNum" type="number" class="input-base" placeholder="排序号" />
            </div>
          </div>

          <div>
            <label class="label-base">
              项目描述
            </label>
            <textarea v-model="form.description" rows="3" class="textarea-base" placeholder="请输入项目描述"></textarea>
          </div>

          <div>
            <label class="label-base">
              项目内容
            </label>
            <textarea v-model="form.content" rows="5" class="textarea-base" placeholder="请输入项目详细内容"></textarea>
          </div>

          <div class="grid-2-cols">
            <div>
              <label class="label-base">
                项目链接
              </label>
              <input v-model="form.link" type="url" class="input-base" placeholder="https://example.com" />
            </div>
            <div>
              <label class="label-base">
                封面图片
              </label>
              <input v-model="form.coverImage" type="text" class="input-base" placeholder="封面图片路径" />
            </div>
          </div>

          <!-- 标签选择 -->
          <div>
            <label class="label-base">
              技术标签
            </label>
            <div class="relative">
              <button
                type="button"
                @click="toggleTagDropdown"
                class="input-base text-left flex justify-between items-center"
              >
                <span>{{ selectedTags.length > 0 ? `已选择 ${selectedTags.length} 个标签` : '选择技术标签' }}</span>
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                </svg>
              </button>
              
              <div v-if="showTagDropdown" class="absolute z-10 mt-1 w-full bg-white shadow-lg max-h-60 rounded-md py-1 text-base ring-1 ring-black ring-opacity-5 overflow-auto">
                <div v-for="tag in tags" :key="tag.id" @click="toggleTag(tag)" class="cursor-pointer select-none relative py-2 pl-3 pr-9 hover:bg-gray-50">
                  <div class="flex items-center">
                    <input type="checkbox" :checked="selectedTags.some(t => t.id === tag.id)" class="mr-2" />
                    <span class="font-normal block truncate">{{ tag.name }}</span>
                  </div>
                </div>
              </div>
            </div>

            <!-- 显示已选择的标签 -->
            <div v-if="selectedTags.length > 0" class="mt-2">
              <div class="text-sm text-gray-600 mb-1">已选择的标签：</div>
              <div class="flex flex-wrap gap-2">
                <span v-for="tag in selectedTags" :key="tag.id" class="inline-flex items-center px-3 py-1 rounded-full text-sm bg-indigo-100 text-indigo-800">
                  {{ tag.name }}
                  <button type="button" @click="removeTag(tag)" class="ml-1 text-indigo-600 hover:text-indigo-800">
                    ×
                  </button>
                </span>
              </div>
            </div>
          </div>

          <!-- 按钮 -->
          <div class="flex justify-end space-x-3 pt-4">
            <button
              type="button"
              @click="$emit('close')"
              class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50"
            >
              取消
            </button>
            <button
              type="submit"
              :disabled="loading"
              class="px-4 py-2 bg-indigo-600 border border-transparent rounded-md text-sm font-medium text-white hover:bg-indigo-700 disabled:opacity-50"
            >
              {{ loading ? '保存中...' : '保存' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { createProject, updateProject } from '@/api/project'
import { getTags } from '@/api/tag'

const props = defineProps({
  project: {
    type: Object,
    default: null
  },
  isEdit: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['close', 'save'])

const loading = ref(false)
const tags = ref([])
const selectedTags = ref([])
const showTagDropdown = ref(false)

const form = ref({
  title: '',
  description: '',
  content: '',
  coverImage: '',
  link: '',
  orderNum: 0,
  tagIds: []
})

// 切换标签下拉框显示状态
const toggleTagDropdown = () => {
  showTagDropdown.value = !showTagDropdown.value
}

// 移除标签
const removeTag = (tagToRemove) => {
  selectedTags.value = selectedTags.value.filter(tag => tag.id !== tagToRemove.id)
  form.value.tagIds = selectedTags.value.map(t => t.id)
}

// 切换标签选择状态
const toggleTag = (tag) => {
  const existingIndex = selectedTags.value.findIndex(t => t.id === tag.id)
  if (existingIndex > -1) {
    selectedTags.value.splice(existingIndex, 1)
  } else {
    selectedTags.value.push(tag)
  }
  form.value.tagIds = selectedTags.value.map(t => t.id)
}

// 点击外部关闭下拉框
const handleClickOutside = (event) => {
  if (!event.target.closest('.relative')) {
    showTagDropdown.value = false
  }
}

// 初始化表单数据
const initializeForm = async () => {
  if (props.project) {
    form.value = {
      title: props.project.title || '',
      description: props.project.description || '',
      content: props.project.content || '',
      coverImage: props.project.coverImage || '',
      link: props.project.link || '',
      orderNum: props.project.orderNum || 0,
      tagIds: props.project.tagIds || []
    }

    // 设置选中的标签
    if (props.project.tags && props.project.tags.length > 0) {
      selectedTags.value = props.project.tags
      form.value.tagIds = props.project.tags.map(tag => tag.id)
    } else if (props.project.tagIds && props.project.tagIds.length > 0 && tags.value.length > 0) {
      selectedTags.value = tags.value.filter(tag => props.project.tagIds.includes(tag.id))
      form.value.tagIds = props.project.tagIds
    }
  }
}

// 加载标签数据
const loadTags = async () => {
  try {
    const response = await getTags()
    tags.value = response.data || []
  } catch (error) {
    console.error('加载标签失败:', error)
    // 如果API请求失败，使用默认数据
    tags.value = [
      { id: 1, name: 'Vue' },
      { id: 2, name: 'React' },
      { id: 3, name: 'JavaScript' },
      { id: 4, name: 'TypeScript' },
      { id: 5, name: 'Node.js' },
      { id: 6, name: 'Spring Boot' },
      { id: 7, name: 'MySQL' },
      { id: 8, name: 'Redis' }
    ]
  }
}

const handleSubmit = async () => {
  loading.value = true
  try {
    if (props.isEdit) {
      await updateProject(props.project.id, form.value)
    } else {
      await createProject(form.value)
    }
    emit('save')
  } catch (error) {
    console.error('保存项目失败:', error)
    alert('保存失败，请重试')
  } finally {
    loading.value = false
  }
}

// 组件挂载时初始化
onMounted(async () => {
  document.addEventListener('click', handleClickOutside)
  await loadTags()
  await initializeForm()
})

// 组件卸载时清理事件监听器
onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>