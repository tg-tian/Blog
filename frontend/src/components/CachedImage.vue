<template>
  <img
    :src="imageSrc"
    :alt="alt"
    :class="className"
    :style="style"
    @load="onImageLoaded"
    @error="onImageError"
  />
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import imageCache from '@/utils/imageCache'

const props = defineProps({
  src: {
    type: String,
    required: true
  },
  alt: {
    type: String,
    default: ''
  },
  className: {
    type: String,
    default: ''
  },
  style: {
    type: Object,
    default: () => ({})
  },
  fallbackSrc: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['loaded', 'error'])

const imageSrc = ref(props.fallbackSrc || '')
const isLoading = ref(true)
const hasError = ref(false)

// 加载图片
const loadImage = async () => {
  if (!props.src) return
  
  try {
    isLoading.value = true
    // 尝试从缓存获取图片
    const cachedImage = imageCache.getImage(props.src)
    
    if (cachedImage) {
      // 使用缓存的图片
      imageSrc.value = cachedImage
      isLoading.value = false
      emit('loaded')
    } else {
      // 预加载并缓存图片
      const base64Data = await imageCache.preloadImage(props.src)
      imageSrc.value = base64Data
      isLoading.value = false
      emit('loaded')
    }
  } catch (error) {
    console.error('图片加载失败:', error)
    hasError.value = true
    isLoading.value = false
    // 如果有备用图片，则使用备用图片
    if (props.fallbackSrc) {
      imageSrc.value = props.fallbackSrc
    }
    emit('error', error)
  }
}

// 图片加载完成事件
const onImageLoaded = () => {
  isLoading.value = false
  emit('loaded')
}

// 图片加载失败事件
const onImageError = (error) => {
  hasError.value = true
  isLoading.value = false
  // 如果有备用图片，则使用备用图片
  if (props.fallbackSrc && imageSrc.value !== props.fallbackSrc) {
    imageSrc.value = props.fallbackSrc
  }
  emit('error', error)
}

// 组件挂载时加载图片
onMounted(() => {
  loadImage()
})
</script>