<template>
    <div class="flex flex-col min-h-screen bg-gradient-to-br from-indigo-500 via-purple-600 to-blue-500 text-white">
        <!-- 导航栏 -->
        <NavBar />

        <!-- 中间内容 -->
        <main class="flex-1 container mx-auto px-4 py-8 rounded-lg shadow-lg bg-white/10 backdrop-blur-md">
            <router-view />
        </main>

        <!-- 页脚 -->
        <Footer />
    </div>
</template>

<script setup>
import NavBar from './NavBar.vue'
import Footer from '@/layouts/Footer.vue'
</script>