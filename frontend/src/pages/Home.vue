<template>
    <div v-if="loading" class="text-center py-8">
        <div class="text-gray-500">加载中...</div>
    </div>
    <div v-else class="m-4 space-y-4">
        <ArticleList :articles="articles" />
        <ProjectList />
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import ArticleList from '@/components/ArticleList.vue'
import ProjectList from '@/components/ProjectList.vue'
import { getArticles } from '@/api/article'

const loading = ref(true)
const articles = ref([])
const page = ref(1)
const size = ref(10)

// 加载文章列表
const loadArticles = async () => {
    try {
        loading.value = true
        const response = await getArticles(page.value,size.value)
        articles.value = response.data.list || []
    } catch (error) {
        console.error('加载文章列表失败:', error)
        articles.value = []
    } finally {
        loading.value = false
    }
}

onMounted(() => {
    loadArticles()
})
</script>
