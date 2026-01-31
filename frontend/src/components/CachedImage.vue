<template>
  <img :src="imageSrc" :alt="alt" :class="className" :style="style" />
</template>

<script setup>
  import { ref, onMounted} from 'vue'
  import imageCache from '@/utils/imageCache'
  import { getFileUrl } from '@/utils/upload'

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
    }
  })

  const imageSrc = ref('')

  // 加载图片
  const loadImage = async () => {
    if (!props.src) return
    try {
      // 尝试从缓存获取图片
      const cachedImage = imageCache.getImage(props.src)
      if (cachedImage) {
        imageSrc.value = cachedImage
      } else {
        const url = await getFileUrl(props.src)
        const base64Data = await imageCache.preloadImage(url)
        imageCache.cacheImage(props.src, base64Data)
        imageSrc.value = base64Data
      }
    } catch (error) {
      console.error('图片加载失败:', error)
    }
  }

  onMounted(() => {
    loadImage()
  })
</script>
