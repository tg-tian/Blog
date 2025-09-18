<template>
    <nav
        class="bg-white text-gray-800 px-6 py-4 flex items-center font-medium text-lg fixed top-0 left-0 right-0 h-12 z-[99999]">
        <div class="flex space-x-8">
            <router-link v-for="link in navLinks" :key="link.to" :to="link.to"
                class="relative flex items-center space-x-2 px-3 py-2 rounded-lg tracking-wide hover:text-blue-600 hover:bg-gray-100 transition-all duration-200"
                :class="{ 'text-blue-600 font-semibold': route.path === link.to }">
                <!-- SVG 图标 -->
                <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                    <path :d="link.iconPath"></path>
                </svg>
                <span class="font-medium">{{ link.label }}</span>
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
    {
        to: '/',
        label: '首页',
        iconPath: 'M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z' // Home icon
    },
    {
        to: '/main/archive',
        label: '归档',
        iconPath: 'M20.54 5.23l-1.39-1.68C18.88 3.21 18.47 3 18 3H6c-.47 0-.88.21-1.16.55L3.46 5.23C3.17 5.57 3 6.02 3 6.5V19c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V6.5c0-.48-.17-.93-.46-1.27zM12 17.5L6.5 12H10v-2h4v2h3.5L12 17.5zM5.12 5l.81-1h12l.94 1H5.12z' // Archive icon
    },
    {
        to: '/main/about',
        label: '关于',
        iconPath: 'M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z' // User/Person icon
    }
]

</script>
