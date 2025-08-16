<template>
    <div class="space-y-2">
        <div v-for="item in articles" :key="item.id"
            class="bg-white rounded-xl shadow-md overflow-hidden hover">
            <router-link :to="`/article/${item.id}`" class="block p-5">
                <h2 class="text-xl font-semibold text-gray-800 mb-2">{{ item.title }}</h2>
                <p class="text-gray-600 text-sm leading-relaxed">{{ item.summary }}</p>
            </router-link>
        </div>
    </div>
</template>

<script setup>
defineProps({
    articles: Array
})
</script>

