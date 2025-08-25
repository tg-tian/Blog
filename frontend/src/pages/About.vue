<template>
  <div class="m-4 space-y-4">
    <div class="card-base">
      <div class="card-content">
        <div class="markdown-content prose prose-lg max-w-none" v-html="markdownContent"></div>
      </div>
    </div>
    <Comment />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { marked } from 'marked'
import Comment from '@/components/Comment.vue'

// markdown内容
const markdownContent = ref('')

// 加载markdown文件
const loadMarkdown = async () => {
  try {
    const response = await fetch('/about.md')
    const text = await response.text()
    markdownContent.value = marked(text)
  } catch (error) {
    console.error('加载markdown文件失败:', error)
    markdownContent.value = '<p>加载内容失败</p>'
  }
}

// 组件挂载时加载内容
onMounted(() => {
  loadMarkdown()
})
</script>



<style scoped>
/* Markdown内容样式 */
.markdown-content :deep(h1) {
  @apply text-3xl font-bold text-gray-900 mt-8 mb-4;
}

.markdown-content :deep(h1:first-child) {
  @apply mt-0;
}

.markdown-content :deep(h2) {
  @apply text-2xl font-semibold text-gray-800 mt-6 mb-3;
}

.markdown-content :deep(h2:first-child) {
  @apply mt-0;
}

.markdown-content :deep(h3) {
  @apply text-xl font-medium text-gray-700 mt-4 mb-2;
}

.markdown-content :deep(h3:first-child) {
  @apply mt-0;
}

.markdown-content :deep(p) {
  @apply text-gray-600 leading-relaxed mb-4;
}

.markdown-content :deep(ul) {
  @apply list-disc list-inside text-gray-600 mb-4 space-y-1;
}

.markdown-content :deep(li) {
  @apply ml-4;
}

.markdown-content :deep(strong) {
  @apply font-semibold text-gray-800;
}

.markdown-content :deep(code) {
  @apply bg-gray-100 px-2 py-1 rounded text-sm font-mono text-gray-800;
}

.markdown-content :deep(blockquote) {
  @apply border-l-4 border-indigo-500 pl-4 italic text-gray-600 my-4;
}

.markdown-content :deep(img) {
  @apply max-w-2xl h-auto rounded-lg shadow-sm my-4 mx-auto block;
}
</style>