<template>
    <div class="home">
        <h1>最新文章</h1>
        <ArticleList :articles="articles" />
    </div>
</template>

<script setup>
import ArticleList from '@/components/ArticleList.vue'

// 模拟数据，实际应通过 API 获取
const articles = [
    { id: 1, title: 'Vue3 项目搭建', summary: '手把手教你搭建 Vue3 + Pinia 项目' },
    { id: 2, title: 'Axios 封装技巧', summary: '如何优雅地封装 API 请求' }
]
</script>
