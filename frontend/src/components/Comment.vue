<template>
  <div class="card-base">
    <div class="card-content">
      <h3 class="text-xl font-semibold text-gray-800 mb-4">💬 评论交流</h3>
      <div ref="giscusContainer"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { updateCommentCount } from '@/api/article'

// 定义props
const props = defineProps({
  repo: { type: String, default: 'tg-tian/Blog' },
  repoId: { type: String, default: 'R_kgDOPb6esA' },
  category: { type: String, default: 'Announcements' },
  categoryId: { type: String, default: 'DIC_kwDOPb6esM4CugaA' },
  mapping: { type: String, default: 'pathname' },
  theme: { type: String, default: 'preferred_color_scheme' },
  lang: { type: String, default: 'zh-CN' }
})

// 定义emits
const emit = defineEmits(['commentCountUpdate'])

const route = useRoute()

const giscusContainer = ref(null)
let giscusScript = null

// 处理Giscus消息 - 监听评论数变化
const handleGiscusMessage = async (event) => {
  // 安全检查：确保消息来自Giscus官方域名
  if (event.origin !== 'https://giscus.app') return
  
  // 检查消息是否包含discussion元数据
  if (event.data?.giscus?.discussion?.totalCommentCount !== undefined) {
    const totalCommentCount = event.data.giscus.discussion.totalCommentCount
    
    try {
      const articleId = route.params.id
      if (articleId) {
        // 同步更新后端评论数
        await updateCommentCount(articleId, totalCommentCount)
        // 通知父组件更新UI显示
        emit('commentCountUpdate', totalCommentCount)
      }
    } catch (error) {
      console.error('更新评论数失败:', error)
    }
  }
}

// 初始化Giscus
const initGiscus = () => {
  if (giscusScript) {
    giscusScript.remove()
  }

  giscusScript = document.createElement('script')
  giscusScript.src = 'https://giscus.app/client.js'
  giscusScript.setAttribute('data-repo', props.repo)
  giscusScript.setAttribute('data-repo-id', props.repoId)
  giscusScript.setAttribute('data-category', props.category)
  giscusScript.setAttribute('data-category-id', props.categoryId)
  giscusScript.setAttribute('data-mapping', props.mapping)
  giscusScript.setAttribute('data-strict', '0')
  giscusScript.setAttribute('data-reactions-enabled', '1')
  giscusScript.setAttribute('data-emit-metadata', '1')
  giscusScript.setAttribute('data-input-position', 'bottom')
  giscusScript.setAttribute('data-theme', props.theme)
  giscusScript.setAttribute('data-lang', props.lang)
  giscusScript.setAttribute('data-loading', 'lazy')
  giscusScript.crossOrigin = 'anonymous'
  giscusScript.async = true

  giscusContainer.value?.appendChild(giscusScript)
}

onMounted(() => {
  initGiscus()
  // 监听Giscus的postMessage事件
  // 工作原理：Giscus iframe通过postMessage API向父窗口发送消息
  // 当评论数发生变化时，会收到包含discussion.totalCommentCount的消息
  window.addEventListener('message', handleGiscusMessage)
})

onUnmounted(() => {
  if (giscusScript) {
    giscusScript.remove()
    giscusScript = null
  }
  // 清理事件监听器
  window.removeEventListener('message', handleGiscusMessage)
})
</script>

<style scoped>
.card-content>div {
  min-height: 200px;
}
</style>