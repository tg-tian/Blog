<template>
    <!-- <div class="article-page">
        <h1>{{ article.title }}</h1>
        <p>{{ article.content }}</p>
        <CommentList :comments="comments" />
    </div> -->
</template>

<script setup>
import { useRoute } from 'vue-router'
// import CommentList from '@/components/CommentList.vue'

const route = useRoute()

// 模拟数据
const article = {
    id: route.params.id,
    title: `文章标题 #${route.params.id}`,
    content: '这是文章的详细内容...'
}

const comments = [
    { id: 1, user: '小明', text: '写得真好！' },
    { id: 2, user: '小红', text: '受益匪浅，谢谢分享！' }
]
</script>
