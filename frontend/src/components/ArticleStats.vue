<template>
  <div class="flex justify-between items-center">
    <!-- 统计数据网格 -->
    <div class="flex flex-wrap justify-center gap-4 md:gap-6">
      <!-- 时间显示：优先显示更新时间，没有更新时间则显示发布时间 -->
      <div v-if="updateTime && updateTime !== publishTime"
        class="flex items-center space-x-1 text-gray-600 hover:text-purple-500 transition-colors" title="更新时间">
        <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
          <path
            d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z" />
        </svg>
        <span class="text-xs font-medium">{{ formatDate(updateTime) }}</span>
      </div>
      <div v-else class="flex items-center space-x-1 text-gray-600 hover:text-indigo-500 transition-colors"
        title="发布时间">
        <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
          <path
            d="M19 3h-1V1h-2v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V8h14v11zM7 10h5v5H7z" />
        </svg>
        <span class="text-xs font-medium">{{ formatDate(publishTime) }}</span>
      </div>

      <!-- 浏览量 -->
      <div class="flex items-center space-x-1 text-gray-600 hover:text-gray-800 transition-colors" title="浏览量">
        <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
          <path
            d="M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z" />
        </svg>
        <span class="text-sm font-medium">{{ formatNumber(views) }}</span>
      </div>

      <!-- 点赞量 -->
      <div class="flex items-center space-x-1 text-gray-600 hover:text-red-500 transition-colors" title="点赞数">
        <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
          <path
            d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" />
        </svg>
        <span class="text-sm font-medium">{{ formatNumber(likes) }}</span>
      </div>

      <!-- 评论数 -->
      <div class="flex items-center space-x-1 text-gray-600 hover:text-blue-500 transition-colors" title="评论数">
        <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
          <path
            d="M21.99 4c0-1.1-.89-2-2-2H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h14l4 4-.01-18zM18 14H6v-2h12v2zm0-3H6V9h12v2zm0-3H6V6h12v2z" />
        </svg>
        <span class="text-sm font-medium">{{ formatNumber(comments) }}</span>
      </div>
    </div>

    <!-- 标签显示 -->
    <div v-if="tags.length" class="hidden md:flex flex-wrap gap-2 justify-end">
      <Tag v-for="tag in tags" :key="tag.id" :name="tag.name" :color="tag.color" />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { formatDateForDisplay } from '@/utils/dateUtils'
import Tag from './Tag.vue'

// Props定义
const props = defineProps({
  publishTime: {
    type: String,
    required: true
  },
  updateTime: {
    type: String,
    default: ''
  },
  views: {
    type: Number,
    default: 0
  },
  likes: {
    type: Number,
    default: 0
  },
  comments: {
    type: Number,
    default: 0
  },
  tags: {
    type: Array,
    default: () => []
  }
})

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  return formatDateForDisplay(dateString)
}

// 格式化数字
const formatNumber = (num) => {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + 'w'
  } else if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'k'
  }
  return num.toString()
}
</script>

<style scoped>
/* 组件特定样式 */
.flex:hover svg {
  transform: scale(1.1);
  transition: transform 0.2s ease;
}
</style>