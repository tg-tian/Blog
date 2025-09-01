<template>
  <router-view />
</template>

<script setup>
import { onMounted } from 'vue'
import { visitSite } from '@/api/stats'

// 记录访问统计
const recordVisit = async () => {
  try {
    // 检查是否已经记录过本次会话的访问
    const hasVisited = sessionStorage.getItem('hasVisited')
    if (!hasVisited) {
      await visitSite()
      sessionStorage.setItem('hasVisited', 'true')
    }
  } catch (error) {
    console.error('记录访问统计失败:', error)
  }
}

onMounted(() => {
  recordVisit()
})
</script>

