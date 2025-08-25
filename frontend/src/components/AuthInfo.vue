<template>
    <div class="m-4 ml-12 p-3 card-base card-hover flex flex-col items-center text-center text-sm md:space-y-3">
        <div>
            <div class="relative w-16 h-16 mb-2">
                <img :src="avatarUrl" alt="作者头像"
                    class="avatar-lg">
                <span class="status-online"></span>
            </div>
            <h1 class="heading-md mb-1 leading-snug">
                {{ authorName }}
            </h1>
        </div>
        <p class="text-caption text-gray-600 mb-3 max-w-xs italic">
            "{{ motto }}"
        </p>
        <div class="flex flex-col space-y-2 text-gray-700 hover:text-gray-900 transition-colors text-xs">
            <!-- GitHub -->
            <a :href="githubUrl" target="_blank" rel="noopener noreferrer" class="flex items-center space-x-2">
                <img :src="githubIcon" alt="GitHub" class="w-4 h-4">
                <span>GitHub</span>
            </a>

            <!-- Email -->
            <a class="flex items-center space-x-2">
                <img :src="emailIcon" alt="Email" class="w-4 h-4">
                <span>{{ email }}</span>
            </a>
        </div>
    </div>
</template>

<script setup>
import authorInfo from '@/config/author'

// 从作者信息对象中获取所需属性
const { avatarUrl, authorName, motto, githubIcon, emailIcon, email, githubUrl } = authorInfo

</script>
