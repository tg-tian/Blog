<template>
    <nav class="bg-gray-900 text-white px-6 flex items-center shadow-md border-b border-gray-700 font-cute">
        <div class="space-x-6">
            <router-link v-for="link in navLinks" :key="link.to" :to="link.to"
                class="relative px-1 tracking-wide hover:text-blue-400 transition-colors"
                :class="{ 'text-blue-500 font-semibold': route.path === link.to }">
                {{ link.label }}
                <!-- 激活下划线 -->
                <span v-if="route.path === link.to"
                    class="absolute -bottom-1 left-0 w-full h-0.5 bg-blue-500 rounded transition-all"></span>
            </router-link>
        </div>
    </nav>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
import { useRoute } from 'vue-router'

const route = useRoute()
const userStore = useUserStore()

const navLinks = [
    { to: '/', label: 'home' },
    { to: '/blog', label: 'blogs' },
    { to: '/projects', label: 'projects' },
    { to: '/about', label: 'about' }
]

</script>
