<template>
    <div
        class="bg-white rounded-xl shadow-md p-4 flex flex-col hover">
        <img v-if="image" :src="image" alt="Project Image" class="w-full h-40 object-cover rounded-lg mb-3" />
        <h3 class="text-lg font-bold text-gray-800 mb-2">{{ title }}</h3>
        <p class="text-gray-600 flex-grow">{{ description }}</p>
        <a v-if="link" :href="link" target="_blank" class="mt-4 text-indigo-600 hover:underline">
            查看详情 →
        </a>
    </div>
</template>

<script setup>
defineProps({
    title: String,
    description: String,
    image: String,
    link: String
})
</script>
