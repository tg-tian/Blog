<template>
  <span class="tag-chip" :style="tagStyle">
    {{ name }}
  </span>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  name: {
    type: String,
    required: true
  },
  color: {
    type: String,
    default: '#cccccc' // 默认颜色
  }
})

// 根据背景色计算对比色
const getContrastColor = (hexColor) => {
  if (!hexColor) return '#000000'
  const r = parseInt(hexColor.substr(1, 2), 16)
  const g = parseInt(hexColor.substr(3, 2), 16)
  const b = parseInt(hexColor.substr(5, 2), 16)
  const yiq = ((r * 299) + (g * 587) + (b * 114)) / 1000
  return (yiq >= 128) ? '#000000' : '#ffffff'
}

const tagStyle = computed(() => ({
  backgroundColor: props.color,
  color: getContrastColor(props.color)
}))
</script>

<style scoped>
.tag-chip {
  @apply inline-block bg-gray-100 text-gray-700 text-xs font-medium px-2.5 py-1 rounded-full transition-colors hover:bg-gray-200;
}
</style>