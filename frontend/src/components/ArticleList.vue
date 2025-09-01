<template>
    <div class="content-spacing">
        <div class="border-b-2 border-gray-300 pb-4 mb-4">
            <h2 class="text-3xl font-bold text-gray-900 flex items-center">
                <svg class="w-8 h-8 mr-3 text-blue-600" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z"/>
                </svg>
                文章列表
            </h2>
        </div>
        <div v-for="item in articles" :key="item.id" class="card-base overflow-hidden card-hover">
            <router-link :to="`/article/${item.id}`" class="block">
                <!-- 文章内容 -->
                <div class="card-content">
                    <h2 class="heading-lg mb-2">{{ item.title }}</h2>
                    <p class="text-description mb-3">{{ item.summary }}</p>
                    <!-- 封面图片 -->
                    <div v-if="item.coverUrl && coverUrlCache[item.coverUrl]" class="mt-3">
                        <img :src="coverUrlCache[item.coverUrl]" :alt="item.title" class="cover-image max-w-md"
                            @error="handleImageError" />
                    </div>
                    <!-- 文章统计信息 -->
                    <div class="mt-4">
                        <ArticleStats :publish-time="item.publishTime" :update-time="item.updateTime"
                            :views="item.views || 0" :likes="item.likes || 0" :comments="item.comments || 0"
                            :tags="item.tags || []" />
                    </div>
                </div>

            </router-link>
        </div>
    </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { getFileUrl } from '@/utils/upload'
import ArticleStats from './ArticleStats.vue'
const props = defineProps({
    articles: Array
})

// 缓存封面URL - 使用响应式对象而不是Map
const coverUrlCache = ref({})

// 预加载所有封面URL
const loadCoverUrls = async () => {
    if (!props.articles) return

    const promises = props.articles
        .filter(article => article.coverUrl && !coverUrlCache.value[article.coverUrl])
        .map(async (article) => {
            try {
                const url = await getFileUrl(article.coverUrl)
                coverUrlCache.value[article.coverUrl] = url
            } catch (error) {
                console.error('获取封面URL失败:', error)
                coverUrlCache.value[article.coverUrl] = ''
            }
        })

    await Promise.all(promises)
}

// 监听文章列表变化，预加载封面
watch(() => props.articles, loadCoverUrls, { immediate: true })

// 处理图片加载错误
const handleImageError = (event) => {
    event.target.style.display = 'none'
}
</script>
