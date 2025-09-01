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
        <ArticleStats :publish-time="article.publishTime" :update-time="article.updateTime" :views="article.views || 0"
          :likes="article.likes || 0" :comments="article.comments || 0" :tags="article.tags || []" />
      </header>
      <!-- 文章摘要 -->
      <div v-if="article.summary" class="mb-8 p-4 bg-gray-50 rounded-lg border-l-4 border-indigo-500">
        <p class="text-gray-700 italic">{{ article.summary }}</p>
      </div>
      <!-- 文章内容 -->
      <article class="prose prose-lg max-w-none">
        <MdPreview :modelValue="processedContent" :theme="'light'" :previewTheme="'default'" :codeTheme="'github'" />
      </article>

    </div>

    <!-- 返回按钮和点赞按钮 -->
    <div class="m-12 flex justify-center items-center space-x-6">
      <!-- 返回按钮 -->
      <button @click="goBack"
        class="group flex items-center justify-center w-12 h-12 bg-white border-2 border-gray-300 rounded-full shadow-lg hover:shadow-xl hover:border-indigo-500 transition-all duration-300 hover:scale-110"
        title="返回文章列表">
        <svg class="w-6 h-6 text-gray-600 group-hover:text-indigo-600 transition-colors duration-300" fill="none"
          stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
        </svg>
      </button>

      <!-- 点赞按钮 -->
      <div class="flex items-center space-x-3">
        <button @click="handleLike" :disabled="isLiking || hasLiked" :class="[
          'flex items-center space-x-2 px-4 py-2 rounded-full transition-all duration-300 transform hover:scale-105',
          hasLiked
            ? 'bg-blue-500 text-white shadow-lg'
            : 'bg-white border-2 border-gray-300 text-gray-600 hover:bg-blue-500 hover:text-white hover:border-blue-500 shadow-md hover:shadow-lg'
        ]" :title="hasLiked ? '已点赞' : '点赞文章'">
          <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
            <path
              d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3z" />
          </svg>
          <span class="text-sm font-medium">
            {{ isLiking ? '点赞中...' : hasLiked ? '已点赞' : '点赞' }}
          </span>
          <span class="text-sm font-bold">({{ article.likes || 0 }})</span>
        </button>
      </div>
    </div>
    <Comment @commentCountUpdate="handleCommentCountUpdate" />
  </div>
  <div v-else class="text-center py-8">
    <div class="text-gray-500">文章不存在</div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getArticle, likeArticle, incrementViews } from '@/api/article'
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
const isLiking = ref(false)
const hasLiked = ref(false)

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

    // 检查用户是否已点赞过此文章
    const likedArticles = JSON.parse(localStorage.getItem('likedArticles') || '[]')
    hasLiked.value = likedArticles.includes(articleId)

    // 增加文章浏览量
    try {
      await incrementViews(articleId)
      // 更新本地显示的浏览量
      if (article.value.views !== undefined) {
        article.value.views += 1
      }
    } catch (err) {
      console.error('增加浏览量失败:', err)
    }

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

// 点赞文章
const handleLike = async () => {
  if (isLiking.value || hasLiked.value) return

  try {
    isLiking.value = true
    const articleId = route.params.id
    await likeArticle(articleId)

    // 更新本地显示的点赞数
    if (article.value.likes !== undefined) {
      article.value.likes += 1
    }
    hasLiked.value = true

    // 将文章ID保存到localStorage，防止重复点赞
    const likedArticles = JSON.parse(localStorage.getItem('likedArticles') || '[]')
    if (!likedArticles.includes(articleId)) {
      likedArticles.push(articleId)
      localStorage.setItem('likedArticles', JSON.stringify(likedArticles))
    }
  } catch (err) {
    console.error('点赞失败:', err)
  } finally {
    isLiking.value = false
  }
}

// 处理评论数更新
const handleCommentCountUpdate = (newCount) => {
  if (article.value) {
    article.value.comments = newCount
  }
}

// 返回上一页
const goBack = () => {
  router.back()
}

onMounted(() => {
  loadArticle()
})
</script>
