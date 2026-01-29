<template>
  <PageTransition name="slide-up">
    <router-view v-slot="{ Component }">
      <component :is="Component" />
    </router-view>
  </PageTransition>
</template>

<script setup>
import { onMounted } from 'vue'
import { visitSite } from '@/api/stats'
import PageTransition from '@/components/PageTransition.vue'

const recordVisit = () => {
  const hasVisited = sessionStorage.getItem('hasVisited')
  if (hasVisited) return
  visitSite().catch(console.error)
  sessionStorage.setItem('hasVisited', 'true')
}

onMounted(() => {
  recordVisit()
})
</script>
