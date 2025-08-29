<template>
  <div class="modal-backdrop">
    <div class="modal-container">
      <div class="mt-3">
        <!-- 头部 -->
        <div class="modal-header">
          <h3 class="heading-lg">
            {{ isEdit ? '编辑文章' : '新建文章' }}
          </h3>
          <button
            @click="$emit('close')"
            class="btn-close"
          >
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
                标题 *
              </label>
              <input
                v-model="form.title"
                type="text"
                required
                class="input-base"
                placeholder="请输入文章标题"
              />
            </div>
            <div>
              <label class="label-base">
                发布时间
              </label>
              <input
                v-model="form.publishTime"
                type="datetime-local"
                class="input-base"
              />
            </div>
          </div>

          <div class="grid-2-cols">
            <!-- 分类选择 -->
            <div>
              <label class="label-base">
                分类
              </label>
              <div class="relative">
                <button
                  type="button"
                  @click="toggleCategoryDropdown"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm bg-white text-left focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                >
                  <span v-if="!form.category" class="text-gray-500">请选择分类</span>
                  <span v-else class="text-gray-900">{{ form.category }}</span>
                  <svg class="absolute right-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                  </svg>
                </button>
                
                <!-- 下拉框 -->
                 <div v-if="showCategoryDropdown" class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-y-auto">
                   <div 
                     v-for="category in categories" 
                     :key="category.id" 
                     class="px-3 py-2 hover:bg-gray-50 cursor-pointer text-sm"
                     :class="{ 'bg-indigo-50 text-indigo-700': form.category === category.name }"
                     @click="selectCategory(category.name)"
                   >
                     {{ category.name }}
                   </div>
                 </div>
              </div>
            </div>

            <!-- 标签选择 -->
             <div>
               <label class="label-base">
                 标签
               </label>
               <div class="relative">
                 <button
                   type="button"
                   @click="toggleTagDropdown"
                   class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm bg-white text-left focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                 >
                   <span v-if="selectedTags.length === 0" class="text-gray-500">请选择标签</span>
                   <span v-else class="text-gray-900">已选择 {{ selectedTags.length }} 个标签</span>
                   <svg class="absolute right-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                     <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                   </svg>
                 </button>
                 
                 <!-- 下拉框 -->
                  <div v-if="showTagDropdown" class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-y-auto">
                    <div 
                      v-for="tag in tags" 
                      :key="tag.id" 
                      class="px-3 py-2 hover:bg-gray-50 cursor-pointer text-sm"
                      :class="{ 'bg-indigo-50 text-indigo-700': selectedTags.includes(tag.name) }"
                      @click="toggleTag(tag.name)"
                    >
                      {{ tag.name }}
                    </div>
                  </div>
               </div>
               
               <!-- 显示已选择的标签 -->
               <div v-if="selectedTags.length > 0" class="mt-2">
                 <div class="text-sm text-gray-600 mb-1">已选择的标签：</div>
                 <div class="flex flex-wrap gap-2">
                   <span
                     v-for="tag in selectedTags"
                     :key="tag"
                     class="inline-flex items-center px-3 py-1 rounded-full text-sm bg-indigo-100 text-indigo-800"
                   >
                     {{ tag }}
                     <button
                       type="button"
                       @click="removeTag(tag)"
                       class="ml-1 text-indigo-600 hover:text-indigo-800"
                     >
                       ×
                     </button>
                   </span>
                 </div>
               </div>
             </div>
          </div>

          <div>
            <label class="label-base">
              摘要
            </label>
            <textarea
              v-model="form.summary"
              rows="3"
              class="textarea-base"
              placeholder="请输入文章摘要"
            ></textarea>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              封面图片
            </label>
            <div class="space-y-3">
              <!-- 文件选择 -->
              <div class="flex items-center space-x-3">
                <input
                  ref="fileInput"
                  type="file"
                  accept="image/*"
                  @change="handleFileSelect"
                  class="hidden"
                />
                <button
                  type="button"
                  @click="$refs.fileInput.click()"
                  class="btn-secondary"
                >
                  选择图片
                </button>
                <span v-if="selectedFile" class="text-sm text-gray-600">
                  {{ selectedFile.name }}
                </span>
              </div>
              
              <!-- 图片预览 -->
              <div v-if="previewUrl" class="relative inline-block">
                <img
                  :src="previewUrl"
                  alt="封面预览"
                  class="w-32 h-32 object-cover rounded-md border border-gray-300"
                />
                <button
                  type="button"
                  @click="clearImage"
                  class="absolute -top-2 -right-2 w-6 h-6 bg-red-500 text-white rounded-full text-xs hover:bg-red-600"
                >
                  ×
                </button>
              </div>
              
              <!-- 上传进度 -->
              <div v-if="uploading" class="w-full bg-gray-200 rounded-full h-2">
                <div
                  class="bg-indigo-600 h-2 rounded-full transition-all duration-300"
                  :style="{ width: uploadProgress + '%' }"
                ></div>
                <p class="text-sm text-gray-600 mt-1">上传中... {{ uploadProgress }}%</p>
              </div>
              
              <!-- 当前封面URL（隐藏字段） -->
              <input
                v-model="form.coverUrl"
                type="hidden"
              />
            </div>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              内容 *
            </label>
            <MdEditor
              v-model="form.content"
              :height="400"
              @onUploadImg="handleImageUpload"
            />
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
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { MdEditor } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import { createArticle, updateArticle } from '@/api/article'
import { uploadToMinio, createPreviewUrl, revokePreviewUrl, getFileUrl } from '@/utils/upload'
import { formatDateForInput, formatDateForSubmit } from '@/utils/dateUtils'
import { refreshMarkdownImageUrls } from '@/utils/markdownUtils'

const props = defineProps({
  article: {
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
const selectedFile = ref(null)
const previewUrl = ref('')
const uploading = ref(false)
const uploadProgress = ref(0)
const newTag = ref('')
const categories = ref([])
const tags = ref([])
const selectedTags = ref([])
const showTagDropdown = ref(false)
const showCategoryDropdown = ref(false)
// 处理图片上传 - 适配md-editor-v3
const handleImageUpload = async (files, callback) => {
  try {
    const uploadPromises = files.map(async (file) => {
      const result = await uploadToMinio(file, {
        prefix: 'article-images',
        validation: {
          allowedTypes: ['image/*'],
          maxSize: 5 * 1024 * 1024 // 5MB
        }
      })
      
      if (result.success) {
        const imageUrl = await getFileUrl(result.objectName)
        return imageUrl
      } else {
        throw new Error(result.error)
      }
    })
    
    const imageUrls = await Promise.all(uploadPromises)
    callback(imageUrls)
  } catch (error) {
    console.error('图片上传失败:', error)
    alert('图片上传失败: ' + (error.message || '未知错误'))
    callback([])
  }
}





const form = ref({
  title: '',
  summary: '',
  content: '',
  coverUrl: '',
  publishTime: new Date().toISOString().slice(0, 16),
  views: 0,
  likes: 0,
  comments: 0,
  category: '',
  tags: []
})

// 初始化表单数据
const initializeForm = async () => {
  if (props.article) {
    // 刷新文章内容中的图片链接
    const refreshedContent = await refreshMarkdownImageUrls(props.article.content)
    
    form.value = {
      ...props.article,
      content: refreshedContent,
      publishTime: formatDateForInput(props.article.publishTime)
    }
    
    // 设置选中的标签
    selectedTags.value = props.article.tags || []
    
    // 如果有封面URL，设置预览
    if (props.article.coverUrl) {
      getFileUrl(props.article.coverUrl).then(url => {
        previewUrl.value = url
      }).catch(error => {
        console.error('获取封面预览URL失败:', error)
      })
    }
  }
}

// 文件选择处理
const handleFileSelect = async (event) => {
  const file = event.target.files[0]
  if (!file) return
  
  selectedFile.value = file
  
  try {
    // 创建预览URL
    previewUrl.value = await createPreviewUrl(file)
    
    // 自动上传
    await uploadFile(file)
  } catch (error) {
    console.error('处理文件失败:', error)
    alert('处理文件失败，请重试')
  }
}

// 清除图片
const clearImage = () => {
  // 释放预览URL内存
  revokePreviewUrl(previewUrl.value)
  
  selectedFile.value = null
  previewUrl.value = ''
  form.value.coverUrl = ''
}

// 上传文件
const uploadFile = async (file) => {
  try {
    uploading.value = true
    uploadProgress.value = 0
    
    // 使用通用上传工具
    const result = await uploadToMinio(file, {
      prefix: 'article-covers',
      onProgress: (progress) => {
        uploadProgress.value = progress
      },
      validation: {
        allowedTypes: ['image/*'],
        maxSize: 5 * 1024 * 1024 // 5MB
      }
    })
    
    if (result.success) {
      form.value.coverUrl = result.objectName
      console.log('上传成功，objectName:', result.objectName)
    } else {
      throw new Error(result.error)
    }
    
  } catch (error) {
    console.error('上传失败:', error)
    alert(error.message || '图片上传失败，请重试')
  } finally {
    uploading.value = false
  }
}

// 切换标签下拉框显示状态
const toggleTagDropdown = () => {
  showTagDropdown.value = !showTagDropdown.value
  showCategoryDropdown.value = false // 关闭分类下拉框
}

// 切换分类下拉框显示状态
const toggleCategoryDropdown = () => {
  showCategoryDropdown.value = !showCategoryDropdown.value
  showTagDropdown.value = false // 关闭标签下拉框
}

// 关闭分类下拉框
const closeCategoryDropdown = () => {
  showCategoryDropdown.value = false
}

// 选择分类
const selectCategory = (categoryName) => {
  form.value.category = categoryName
  closeCategoryDropdown()
}

// 移除标签
const removeTag = (tagToRemove) => {
  selectedTags.value = selectedTags.value.filter(tag => tag !== tagToRemove)
}

// 切换标签选择状态
const toggleTag = (tagName) => {
  if (selectedTags.value.includes(tagName)) {
    selectedTags.value = selectedTags.value.filter(tag => tag !== tagName)
  } else {
    selectedTags.value.push(tagName)
  }
}

// 点击外部关闭下拉框
const handleClickOutside = (event) => {
  if (!event.target.closest('.relative')) {
    showTagDropdown.value = false
    showCategoryDropdown.value = false
  }
}

// 组件挂载时初始化
onMounted(async () => {
  // 添加点击外部关闭下拉框的事件监听器
  document.addEventListener('click', handleClickOutside)
  
  loadCategoriesAndTags()
  await initializeForm()
})

// 组件卸载时清理事件监听器
onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})

// 初始化分类和标签模拟数据
const loadCategoriesAndTags = () => {
  // 分类模拟数据
  categories.value = [
    { id: 1, name: '技术' },
    { id: 2, name: '生活' },
    { id: 3, name: '随笔' },
    { id: 4, name: '教程' },
    { id: 5, name: '项目' },
    { id: 6, name: '前端' },
    { id: 7, name: '后端' },
    { id: 8, name: '数据库' }
  ]
  
  // 标签模拟数据
  tags.value = [
    { id: 1, name: 'Vue' },
    { id: 2, name: 'JavaScript' },
    { id: 3, name: 'CSS' },
    { id: 4, name: 'HTML' },
    { id: 5, name: 'Node.js' },
    { id: 6, name: 'React' },
    { id: 7, name: 'TypeScript' },
    { id: 8, name: 'Python' },
    { id: 9, name: 'Java' },
    { id: 10, name: 'Spring Boot' },
    { id: 11, name: 'MySQL' },
    { id: 12, name: 'Redis' },
    { id: 13, name: 'Docker' },
    { id: 14, name: 'Git' },
    { id: 15, name: '算法' },
    { id: 16, name: '数据结构' },
    { id: 17, name: '设计模式' },
    { id: 18, name: '微服务' }
  ]
}

const handleSubmit = async () => {
  loading.value = true
  
  try {
    // 如果正在上传，等待上传完成
    if (uploading.value) {
      alert('图片正在上传中，请稍候...')
      return
    }
    
    // 准备提交数据，将datetime-local格式转换为ISO格式
    const submitData = {
      ...form.value,
      publishTime: formatDateForSubmit(form.value.publishTime),
      tags: selectedTags.value
    }
    
    if (props.isEdit) {
      await updateArticle(props.article.id, submitData)
    } else {
      await createArticle(submitData)
    }
    emit('save')
  } catch (error) {
    console.error('保存文章失败:', error)
    alert('保存失败，请重试')
  } finally {
    loading.value = false
  }
}


</script>