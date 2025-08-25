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

const giscusContainer = ref(null)
let giscusScript = null

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
  giscusScript.setAttribute('data-emit-metadata', '0')
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
})

onUnmounted(() => {
  if (giscusScript) {
    giscusScript.remove()
    giscusScript = null
  }
})
</script>

<style scoped>
.card-content > div {
  min-height: 200px;
}
</style>