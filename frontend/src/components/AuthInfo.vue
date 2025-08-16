<template>
    <div class="m-4 ml-12  p-3 flex flex-col items-center text-center 
              bg-white rounded-xl shadow-md hover:shadow-lg transition-shadow 
              duration-300 border border-gray-100 text-sm md:space-y-3 ">
        <div>
            <div class="relative w-16 h-16 mb-2">
                <img :src="avatarUrl" alt="作者头像"
                    class="w-full h-full object-cover rounded-full border-2 border-white shadow-md">
                <span
                    class="absolute bottom-0.5 right-0.5 w-3 h-3 bg-green-500 rounded-full border border-white"></span>
            </div>
            <h1 class="text-lg font-bold text-gray-900 mb-1 leading-snug">
                {{ authorName }}
            </h1>
        </div>
        <p class="text-xs text-gray-600 mb-3 max-w-xs italic">
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
import { avatarUrl, authorName, motto, githubIcon, emailIcon, email, githubUrl } from '@/config/author'

</script>
