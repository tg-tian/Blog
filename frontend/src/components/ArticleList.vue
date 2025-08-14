<template>
    <div class="article-list">
        <div v-for="item in articles" :key="item.id" class="article-card">
            <router-link :to="`/article/${item.id}`">
                <h2>{{ item.title }}</h2>
                <p>{{ item.summary }}</p>
            </router-link>
        </div>
    </div>
</template>

<script setup>
defineProps({
    articles: Array
})
</script>

<style scoped>
.article-card {
    padding: 12px;
    border-bottom: 1px solid #eee;
}

.article-card h2 {
    margin: 0;
}
</style>
