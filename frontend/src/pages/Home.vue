<template>
    <div class="home-container">
        <!-- 英雄区域 - 全屏高度 -->
         <div class="hero-section relative h-screen overflow-hidden">
            <!-- 动态视频背景 -->
            <video
              autoplay
              loop
              muted
              playsinline
              class="absolute inset-0 z-0 h-full w-full object-cover"
            >
              <source src="/banner.mp4" type="video/mp4" />
              Your browser does not support the video tag.
            </video>
           
            <!-- 诗句内容 -->
           <div class="absolute bottom-16 right-8 z-10 text-right text-white px-4 animate-fade-in-up">
               <h1 class="text-xl md:text-2xl font-light tracking-wider" style="font-family: 'KaiTi', 'SimSun', serif;">
                   浴乎沂，风乎舞雩，咏而归
               </h1>
               <p class="mt-2 text-sm md:text-base opacity-60">
                   —— 论语 · 先进
               </p>
           </div>
            
            <!-- 下滑提示 -->
             <div class="absolute bottom-8 left-1/2 transform -translate-x-1/2 text-white animate-bounce">
                 <div class="flex justify-center cursor-pointer" @click="scrollToContent">
                     <svg class="w-8 h-8 animate-pulse" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                         <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 14l-7 7m0 0l-7-7m0 0l7-7"></path>
                     </svg>
                 </div>
             </div>
        </div>
        
        <!-- 完整的主页布局 - 使用MainLayout结构 -->
        <div id="content-section" class="flex flex-col min-h-screen bg-gradient-to-b from-gray-100 to-gray-200 antialiased">
            <NavBar />
            <div class="flex flex-1 flex-row">
                <aside class="sticky top-12 left-0 w-1/5 h-[calc(100vh-4rem)]">
                    <SideBar />
                </aside>
                <main class="flex-1  mt-12 mr-10">
                    <StatusMessage 
                        v-if="loading" 
                        type="loading" 
                        message="加载中..." 
                    />
                    <div v-else class="m-4 space-y-4">
                        <ArticleList :articles="articles" />
                        <ProjectList />
                    </div>
                    <Footer class="text-gray-400 text-center py-6" />
                </main>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import ArticleList from '@/components/ArticleList.vue'
import ProjectList from '@/components/ProjectList.vue'
import NavBar from '@/layouts/NavBar.vue'
import SideBar from '@/layouts/SideBar.vue'
import Footer from '@/layouts/Footer.vue'
import StatusMessage from '@/components/StatusMessage.vue'
import { getArticles } from '@/api/article'

const loading = ref(true)
const articles = ref([])
const page = ref(1)
const size = ref(10)
const navOpacity = ref(0)

// 加载文章列表
const loadArticles = async () => {
    try {
        loading.value = true
        const response = await getArticles(page.value,size.value)
        articles.value = response.data.list || []
    } catch (error) {
        console.error('加载文章列表失败:', error)
        articles.value = []
    } finally {
        loading.value = false
    }
}

// 处理滚动事件，调整导航栏透明度
const handleScroll = () => {
    const scrollY = window.scrollY
    const heroHeight = window.innerHeight
    // 在hero区域的前80%保持透明，后20%开始渐变
    const fadeStart = heroHeight * 0.8
    const fadeEnd = heroHeight
    
    if (scrollY <= fadeStart) {
        navOpacity.value = 0
    } else if (scrollY >= fadeEnd) {
        navOpacity.value = 1
    } else {
        navOpacity.value = (scrollY - fadeStart) / (fadeEnd - fadeStart)
    }
    
    // 更新导航栏样式
     const navbar = document.querySelector('nav')
     if (navbar) {
         navbar.style.backgroundColor = `rgba(255, 255, 255, ${navOpacity.value})`
         navbar.style.backdropFilter = navOpacity.value > 0 ? 'blur(10px)' : 'none'
         navbar.style.boxShadow = navOpacity.value > 0 ? `0 1px 3px 0 rgba(0, 0, 0, ${navOpacity.value * 0.1}), 0 1px 2px 0 rgba(0, 0, 0, ${navOpacity.value * 0.06})` : 'none'
         navbar.style.borderBottom = navOpacity.value > 0 ? `1px solid rgba(229, 231, 235, ${navOpacity.value})` : 'none'
     }
}

// 滚动到内容区域
const scrollToContent = () => {
    const contentSection = document.getElementById('content-section')
    if (contentSection) {
        contentSection.scrollIntoView({ 
            behavior: 'smooth',
            block: 'start'
        })
    }
}

onMounted(() => {
    loadArticles()
    // 添加滚动事件监听
    window.addEventListener('scroll', handleScroll)
    // 初始化导航栏状态
    handleScroll()
})

onUnmounted(() => {
    // 移除滚动事件监听
    window.removeEventListener('scroll', handleScroll)
})
</script>
