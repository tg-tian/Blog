<template>
  <div v-if="loading" class="text-center py-8">
    <div class="text-gray-500">加载中...</div>
  </div>
  <div v-else-if="error" class="text-center py-8">
    <div class="text-red-500">{{ error }}</div>
    <button @click="loadArticle" class="btn-primary mt-4">重试</button>
  </div>
  <div v-else-if="article" class="max-w-4xl mx-auto p-6">
    <div class="card-base card-content">
      <!-- 文章头部 -->
      <header class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-4">{{ article.title }}</h1>
        <!-- 文章统计信息 -->
        <ArticleStats 
          :publish-time="article.publishTime"
          :update-time="article.updateTime"
          :views="article.views || 0"
          :likes="article.likes || 0"
          :comments="article.comments || 0"
        />
      </header>
      <!-- 文章摘要 -->
      <div v-if="article.summary" class="mb-8 p-4 bg-gray-50 rounded-lg border-l-4 border-indigo-500">
        <p class="text-gray-700 italic">{{ article.summary }}</p>
      </div>
      <!-- 文章内容 -->
      <article class="prose prose-lg max-w-none">
        <MdPreview 
          :modelValue="processedContent" 
          :theme="'light'"
          :previewTheme="'default'"
          :codeTheme="'github'"
        />
      </article>
    </div>
    
    <!-- 返回按钮 -->
    <div class="mt-12 flex justify-center">
      <button @click="goBack" 
              class="group flex items-center justify-center w-12 h-12 bg-white border-2 border-gray-300 rounded-full shadow-lg hover:shadow-xl hover:border-indigo-500 transition-all duration-300 hover:scale-110"
              title="返回文章列表">
        <svg class="w-6 h-6 text-gray-600 group-hover:text-indigo-600 transition-colors duration-300" 
             fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" 
                d="M10 19l-7-7m0 0l7-7m-7 7h18"/>
        </svg>
      </button>
    </div>
    <Comment/>
  </div>
  <div v-else class="text-center py-8">
    <div class="text-gray-500">文章不存在</div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getArticle } from '@/api/article'
import { getFileUrl } from '@/utils/upload'
import { refreshMarkdownImageUrls } from '@/utils/markdownUtils'
import { MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import Comment from '@/components/Comment.vue'
import ArticleStats from '@/components/ArticleStats.vue'

const route = useRoute()
const router = useRouter()

const loading = ref(true)
const error = ref('')
const article = ref(null)
const coverUrl = ref('')
const processedContent = ref('')

// 加载文章数据
const loadArticle = async () => {
  try {
    loading.value = true
    error.value = ''
    
    const articleId = route.params.id
    if (!articleId) {
      error.value = '文章ID不存在'
      return
    }

    const response = await getArticle(articleId)
    article.value = response.data
    
    // 处理文章内容中的图片链接
    if (article.value.content) {
      try {
        processedContent.value = await refreshMarkdownImageUrls(article.value.content)
      } catch (err) {
        console.error('处理文章图片链接失败:', err)
        processedContent.value = article.value.content // 降级处理
      }
    }
    
    // 加载封面图片
    if (article.value.coverUrl) {
      try {
        coverUrl.value = await getFileUrl(article.value.coverUrl)
      } catch (err) {
        console.error('加载封面图片失败:', err)
      }
    }
  } catch (err) {
    console.error('加载文章失败:', err)
    error.value = '加载文章失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

// 处理图片加载错误
const handleImageError = () => {
  coverUrl.value = ''
}

// 返回上一页
const goBack = () => {
  router.back()
}

onMounted(() => {
  loadArticle()
})
</script>

<style scoped>
/* Markdown内容样式 */
.markdown-content {
  @apply text-gray-800 leading-relaxed;
}

.markdown-content :deep(h1) {
  @apply text-2xl font-bold mt-8 mb-4 text-gray-900;
}

.markdown-content :deep(h2) {
  @apply text-xl font-semibold mt-6 mb-3 text-gray-900;
}

.markdown-content :deep(h3) {
  @apply text-lg font-medium mt-4 mb-2 text-gray-900;
}

.markdown-content :deep(p) {
  @apply mb-4 text-gray-700;
}

.markdown-content :deep(ul), 
.markdown-content :deep(ol) {
  @apply mb-4 pl-6;
}

.markdown-content :deep(li) {
  @apply mb-2;
}

.markdown-content :deep(blockquote) {
  @apply border-l-4 border-gray-300 pl-4 italic text-gray-600 my-4;
}

.markdown-content :deep(code) {
  @apply bg-gray-100 px-2 py-1 rounded text-sm font-mono;
}

.markdown-content :deep(pre) {
  @apply bg-gray-900 text-gray-100 p-4 rounded-lg overflow-x-auto my-4;
}

.markdown-content :deep(pre code) {
  @apply bg-transparent p-0;
}

.markdown-content :deep(a) {
  @apply text-indigo-600 hover:text-indigo-800 underline;
}

.markdown-content :deep(img) {
  @apply max-w-2xl h-auto rounded-lg shadow-sm my-4 mx-auto block;
}

.markdown-content :deep(table) {
  @apply w-full border-collapse border border-gray-300 my-4;
}

.markdown-content :deep(th), 
.markdown-content :deep(td) {
  @apply border border-gray-300 px-4 py-2;
}

.markdown-content :deep(th) {
  @apply bg-gray-50 font-semibold;
}
</style>