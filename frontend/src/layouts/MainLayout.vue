<template>
    <div class="flex flex-col min-h-screen bg-gradient-to-b from-gray-100 to-gray-200 antialiased">
        <NavBar />
        <div class="flex flex-1 flex-row">
            <aside class="fixed top-12 left-0 w-1/5 h-[calc(100vh-4rem)] ">
                <SideBar />
            </aside>
            <main class="flex flex-col flex-1 ml-[20%] mt-12 mr-10">
                <div class="flex-1">
                    <router-view />
                </div>
                <Footer class="text-gray-400 text-center py-6" />
            </main>
        </div>
    </div>
</template>



<script setup>
import NavBar from './NavBar.vue'
import Footer from '@/layouts/Footer.vue'
import SideBar from './SideBar.vue';
</script>
