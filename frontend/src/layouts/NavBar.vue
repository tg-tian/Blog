<template>
    <nav class="bg-gray-900 text-white px-6 py-3 shadow-md border-b border-gray-700">
        <div class="max-w-7xl mx-auto flex justify-between items-center">
            <!-- 左侧导航 -->
            <div class="flex items-center space-x-6">
                <router-link v-for="link in navLinks" :key="link.to" :to="link.to"
                    class="relative px-1 text-sm tracking-wide hover:text-blue-400 transition-colors"
                    :class="{ 'text-blue-500 font-semibold': route.path === link.to }">
                    {{ link.label }}
                    <!-- 激活下划线 -->
                    <span v-if="route.path === link.to"
                        class="absolute -bottom-1 left-0 w-full h-0.5 bg-blue-500 rounded transition-all"></span>
                </router-link>
            </div>

            <!-- 右侧用户信息 -->
            <div v-if="userStore.isLogin" class="flex items-center space-x-3">
                <img :src="userStore.avatar" alt="avatar"
                    class="w-9 h-9 rounded-full border-2 border-blue-500 shadow-sm" />
                <span class="font-medium">{{ userStore.username }}</span>
            </div>
            <div v-else>
                <button @click="loginTest"
                    class="bg-blue-500 hover:bg-blue-600 px-4 py-1.5 rounded-md text-sm font-medium shadow-md transition">
                    登录
                </button>
            </div>
        </div>
    </nav>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
import { useRoute } from 'vue-router'

const route = useRoute()
const userStore = useUserStore()

const navLinks = [
    { to: '/', label: '首页' },
    { to: '/blog', label: '博客' },
    { to: '/projects', label: '项目' },
    { to: '/about', label: '关于我' }
]

function loginTest() {
    userStore.login({
        username: '测试用户',
        avatar: 'https://i.pravatar.cc/40'
    })
}
</script>
