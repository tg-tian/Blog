<template>
  <div class="m-4 space-y-4">
    <div class="card-base">
      <div class="card-content">
        <MdPreview 
          :modelValue="markdownContent" 
          :theme="'light'"
          :previewTheme="'default'"
          :codeTheme="'github'"
        />
      </div>
    </div>
    <Comment />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import Comment from '@/components/Comment.vue'

// markdown内容
const markdownContent = ref('')

// 加载markdown文件
const loadMarkdown = async () => {
  try {
    const response = await fetch('/about.md')
    const text = await response.text()
    markdownContent.value = text // 直接使用原始markdown文本，不需要marked转换
  } catch (error) {
    console.error('加载markdown文件失败:', error)
    markdownContent.value = '# 加载内容失败\n\n请稍后重试。'
  }
}

// 组件挂载时加载内容
onMounted(() => {
  loadMarkdown()
})
</script>


