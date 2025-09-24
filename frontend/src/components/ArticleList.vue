<template>
    <div class="content-spacing">
        <div class="border-b-2 border-gray-300 pb-4 mb-4">
            <div class="flex items-center justify-between">
                <h2 class="text-3xl font-bold text-gray-900 flex items-center">
                    <svg class="w-8 h-8 mr-3 text-blue-600" fill="currentColor" viewBox="0 0 24 24">
                        <path
                            d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z" />
                    </svg>
                    {{ title }}
                </h2>
                <button v-if="showBackButton" @click="emit('back')"
                    class="flex items-center gap-2 text-blue-600 hover:text-blue-800 transition-colors px-4 py-2 rounded-md hover:bg-blue-50">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7">
                        </path>
                    </svg>
                    返回归档
                </button>
            </div>
        </div>
        <div v-for="item in articles" :key="item.id" class="card-base overflow-hidden card-hover">
            <router-link :to="`/main/article/${item.id}`" class="block">
                <!-- 文章内容 -->
                <div class="card-content">
                    <h2 class="heading-lg mb-2">{{ item.title }}</h2>
                    <p class="text-description mb-3">{{ item.summary }}</p>
                    <!-- 封面图片 -->
                    <div v-if="item.coverUrl" class="mt-3">
                        <CachedImage :src="item.coverUrl" :alt="item.title" class="cover-image max-w-md" />
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
import ArticleStats from './ArticleStats.vue'
import CachedImage from './CachedImage.vue'
const props = defineProps({
    articles: {
        type: Array,
        default: () => []
    },
    title: {
        type: String,
        default: '文章列表'
    },
    showBackButton: {
        type: Boolean,
        default: false
    }
})

const emit = defineEmits(['back'])

// 不再需要手动缓存封面URL，使用CachedImage组件自动处理

// CachedImage组件已内置处理图片加载错误的功能
</script>
