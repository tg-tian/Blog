<template>
    <nav
        class="bg-white text-gray-800 px-6 py-4 flex items-center shadow-md border-b border-gray-200 font-medium text-lg fixed top-0 left-0 right-0 h-12 z-[99999]">
        <div class="flex space-x-8">
            <router-link v-for="link in navLinks" :key="link.to" :to="link.to"
                class="relative flex items-center space-x-2 px-3 py-2 rounded-lg tracking-wide hover:text-blue-600 hover:bg-gray-100 transition-all duration-200"
                :class="{ 'text-blue-600 bg-gray-100 font-semibold': route.path === link.to }">
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
        to: '/blog', 
        label: '博客', 
        iconPath: 'M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z' // Article/Blog icon
    },
    { 
        to: '/projects', 
        label: '项目', 
        iconPath: 'M12 15.5A3.5 3.5 0 0 1 8.5 12A3.5 3.5 0 0 1 12 8.5a3.5 3.5 0 0 1 3.5 3.5 3.5 3.5 0 0 1-3.5 3.5m7.43-2.53c.04-.32.07-.64.07-.97 0-.33-.03-.66-.07-1l2.11-1.63c.19-.15.24-.42.12-.64l-2-3.46c-.12-.22-.39-.31-.61-.22l-2.49 1c-.52-.39-1.06-.73-1.69-.98l-.37-2.65A.506.506 0 0 0 14 2h-4c-.25 0-.46.18-.5.42l-.37 2.65c-.63.25-1.17.59-1.69.98l-2.49-1c-.22-.09-.49 0-.61.22l-2 3.46c-.13.22-.07.49.12.64L4.57 11c-.04.34-.07.67-.07 1 0 .33.03.65.07.97L2.46 14.6c-.19.15-.24.42-.12.64l2 3.46c.12.22.39.31.61.22l2.49-1c.52.39 1.06.73 1.69.98l.37 2.65c.04.24.25.42.5.42h4c.25 0 .46-.18.5-.42l.37-2.65c.63-.25 1.17-.59 1.69-.98l2.49 1c.22.09.49 0 .61-.22l2-3.46c.12-.22.07-.49-.12-.64l-2.11-1.66Z' // Gear/Settings icon
    },
    { 
        to: '/about', 
        label: '关于', 
        iconPath: 'M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z' // User/Person icon
    }
]

</script>
