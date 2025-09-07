<template>
  <div v-if="visible" :class="containerClass">
    <div class="flex items-start">
      <!-- 图标 -->
      <svg :class="iconClass" fill="currentColor" viewBox="0 0 24 24">
        <path :d="iconPath" />
      </svg>
      
      <!-- 消息内容 -->
      <div class="flex-1">
        <p :class="textClass">{{ message }}</p>
        
        <!-- 重试按钮 -->
        <button 
          v-if="showRetry && onRetry" 
          @click="handleRetry"
          class="btn-retry mt-2"
        >
          重试
        </button>
      </div>
      
      <!-- 关闭按钮 -->
      <button 
        v-if="closable" 
        @click="handleClose"
        class="btn-close ml-2"
      >
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
        </svg>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'

const props = defineProps({
  // 消息类型：error, success, warning, info, loading
  type: {
    type: String,
    default: 'error',
    validator: (value) => ['error', 'success', 'warning', 'info', 'loading'].includes(value)
  },
  // 消息内容
  message: {
    type: String,
    required: true
  },
  // 是否显示
  visible: {
    type: Boolean,
    default: true
  },
  // 是否可关闭
  closable: {
    type: Boolean,
    default: false
  },
  // 是否显示重试按钮
  showRetry: {
    type: Boolean,
    default: false
  },
  // 重试回调函数
  onRetry: {
    type: Function,
    default: null
  },
  // 自动关闭时间（毫秒），0表示不自动关闭
  autoClose: {
    type: Number,
    default: 0
  }
})

const emit = defineEmits(['close', 'retry'])

// 自动关闭定时器
let autoCloseTimer = null

// 容器样式类
const containerClass = computed(() => {
  const baseClasses = {
    error: 'error-container',
    success: 'success-container',
    warning: 'warning-container',
    info: 'info-container',
    loading: 'loading-container'
  }
  return baseClasses[props.type] || baseClasses.error
})

// 文本样式类
const textClass = computed(() => {
  const baseClasses = {
    error: 'error-text',
    success: 'success-text',
    warning: 'warning-text',
    info: 'info-text',
    loading: 'loading-text'
  }
  return baseClasses[props.type] || baseClasses.error
})

// 图标样式类
const iconClass = computed(() => {
  const baseClasses = {
    error: 'error-icon',
    success: 'success-icon',
    warning: 'warning-icon',
    info: 'info-icon',
    loading: 'info-icon animate-spin'
  }
  return baseClasses[props.type] || baseClasses.error
})

// 图标路径
const iconPath = computed(() => {
  const paths = {
    error: 'M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L4.268 18.5c-.77.833.192 2.5 1.732 2.5z',
    success: 'M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z',
    warning: 'M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L4.268 18.5c-.77.833.192 2.5 1.732 2.5z',
    info: 'M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z',
    loading: 'M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z'
  }
  return paths[props.type] || paths.error
})

// 处理关闭
const handleClose = () => {
  emit('close')
}

// 处理重试
const handleRetry = () => {
  emit('retry')
  if (props.onRetry) {
    props.onRetry()
  }
}

// 设置自动关闭
const setupAutoClose = () => {
  if (autoCloseTimer) {
    clearTimeout(autoCloseTimer)
  }
  
  if (props.autoClose > 0 && props.visible) {
    autoCloseTimer = setTimeout(() => {
      handleClose()
    }, props.autoClose)
  }
}

// 监听visible变化，设置自动关闭
watch(() => props.visible, (newValue) => {
  if (newValue) {
    setupAutoClose()
  } else if (autoCloseTimer) {
    clearTimeout(autoCloseTimer)
    autoCloseTimer = null
  }
}, { immediate: true })

// 监听autoClose变化
watch(() => props.autoClose, () => {
  setupAutoClose()
})

// 组件卸载时清理定时器
import { onUnmounted } from 'vue'
onUnmounted(() => {
  if (autoCloseTimer) {
    clearTimeout(autoCloseTimer)
  }
})
</script>