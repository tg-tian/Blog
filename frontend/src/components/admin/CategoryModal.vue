<template>
  <div class="modal-backdrop">
    <div class="modal-container">
      <div class="mt-3">
        <!-- 头部 -->
        <div class="modal-header">
          <h3 class="heading-lg">
            {{ isEdit ? '编辑分类' : '新建分类' }}
          </h3>
          <button @click="$emit('close')" class="btn-close">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
            </svg>
          </button>
        </div>

        <!-- 表单 -->
        <form @submit.prevent="handleSubmit" class="content-spacing">
          <div>
            <label class="label-base">
              分类名称 *
            </label>
            <input v-model="form.name" type="text" required class="input-base" placeholder="请输入分类名称" />
          </div>

          <div>
            <label class="label-base">
              标识 (slug) *
            </label>
            <input v-model="form.slug" type="text" required class="input-base" placeholder="请输入分类标识，如：tech、life" />
            <p class="mt-1 text-sm text-gray-500">用于URL路径，建议使用英文小写字母和连字符</p>
          </div>

          <div>
            <label class="label-base">
              描述
            </label>
            <textarea v-model="form.description" rows="3" class="textarea-base" placeholder="请输入分类描述"></textarea>
          </div>

          <!-- 按钮 -->
          <div class="flex justify-end space-x-3 pt-4">
            <button type="button" @click="$emit('close')"
              class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50">
              取消
            </button>
            <button type="submit" :disabled="loading"
              class="px-4 py-2 bg-indigo-600 border border-transparent rounded-md text-sm font-medium text-white hover:bg-indigo-700 disabled:opacity-50">
              {{ loading ? '保存中...' : '保存' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { createCategory, updateCategory } from '@/api/category'

const props = defineProps({
  category: {
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

const form = ref({
  name: '',
  slug: '',
  description: ''
})

// 初始化表单数据
const initializeForm = () => {
  if (props.category) {
    form.value = {
      name: props.category.name || '',
      slug: props.category.slug || '',
      description: props.category.description || ''
    }
  }
}

const handleSubmit = async () => {
  loading.value = true

  try {
    if (props.isEdit) {
      await updateCategory(props.category.id, form.value)
    } else {
      await createCategory(form.value)
    }
    emit('save')
  } catch (error) {
    console.error('保存分类失败:', error)
    alert('保存失败，请重试')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  initializeForm()
})
</script>