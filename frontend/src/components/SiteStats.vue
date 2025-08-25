<template>
    <div class="m-4 ml-12 p-3 card-base card-hover">
        <!-- 标题 -->
        <div class="text-center mb-3">
            <h3 class="text-base font-bold text-gray-800 mb-1">网站统计</h3>
            <div class="w-8 h-0.5 bg-gray-400 mx-auto rounded"></div>
        </div>

        <!-- 统计数据网格 -->
        <div class="space-y-2 mb-3">
            <!-- 总访问量 -->
            <div class="card-stats">
                <div class="flex items-center justify-between">
                    <div class="flex items-center">
                        <svg class="w-3 h-3 text-gray-600 mr-1.5" fill="currentColor" viewBox="0 0 24 24">
                            <path d="M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z"/>
                        </svg>
                        <span class="text-caption text-gray-700">访问</span>
                    </div>
                    <span class="text-sm font-bold text-gray-900">{{ formatNumber(stats.totalViews) }}</span>
                </div>
            </div>

            <!-- 点赞量 -->
            <div class="card-stats">
                <div class="flex items-center justify-between">
                    <div class="flex items-center">
                        <svg class="w-3 h-3 text-gray-600 mr-1.5" fill="currentColor" viewBox="0 0 24 24">
                            <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
                        </svg>
                        <span class="text-caption text-gray-700">点赞</span>
                    </div>
                    <span class="text-sm font-bold text-gray-900">{{ formatNumber(stats.totalLikes) }}</span>
                </div>
            </div>

            <!-- 文章数量 -->
            <div class="card-stats">
                <div class="flex items-center justify-between">
                    <div class="flex items-center">
                        <svg class="w-3 h-3 text-gray-600 mr-1.5" fill="currentColor" viewBox="0 0 24 24">
                            <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z"/>
                        </svg>
                        <span class="text-caption text-gray-700">文章</span>
                    </div>
                    <span class="text-sm font-bold text-gray-900">{{ stats.totalArticles }}</span>
                </div>
            </div>

            <!-- 运行时长 -->
            <div class="card-stats">
                <div class="flex items-center justify-between">
                    <div class="flex items-center">
                        <svg class="w-3 h-3 text-gray-600 mr-1.5" fill="currentColor" viewBox="0 0 24 24">
                            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                        </svg>
                        <span class="text-caption text-gray-700">运行</span>
                    </div>
                    <span class="text-sm font-bold text-gray-900">{{ stats.uptime }}</span>
                </div>
            </div>
        </div>

        <!-- 点赞按钮 -->
        <div class="text-center">
            <button 
                @click="handleLike" 
                :disabled="isLiking"
                class="inline-flex items-center px-3 py-1.5 bg-gray-800 hover:bg-gray-900 
                       text-white text-caption font-medium rounded-lg 
                       transform hover:scale-105 transition-all duration-200 shadow-md hover:shadow-lg
                       disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none">
                <svg class="w-3 h-3 mr-1.5" :class="{ 'animate-pulse': isLiking }" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
                </svg>
                {{ isLiking ? '点赞中...' : '点赞' }}
            </button>
        </div>

        <!-- 今日访问量 -->
        <div class="mt-3 pt-2 border-t border-gray-200">
            <div class="flex justify-between items-center text-xs text-gray-600">
                <span>今日访问</span>
                <span class="font-semibold text-gray-800">+{{ stats.todayViews }}</span>
            </div>
            <div class="flex justify-between items-center text-xs text-gray-600 mt-0.5">
                <span>今日点赞</span>
                <span class="font-semibold text-gray-800">+{{ stats.todayLikes }}</span>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

// 响应式数据
const stats = ref({
    totalViews: 12580,
    totalLikes: 1024,
    totalArticles: 42,
    uptime: '168天',
    todayViews: 89,
    todayLikes: 12
})

const isLiking = ref(false)

// 格式化数字显示
const formatNumber = (num) => {
    if (num >= 10000) {
        return (num / 10000).toFixed(1) + 'w'
    } else if (num >= 1000) {
        return (num / 1000).toFixed(1) + 'k'
    }
    return num.toString()
}

// 点赞功能
const handleLike = async () => {
    if (isLiking.value) return
    
    isLiking.value = true
    
    try {
        // 模拟API调用延迟
        await new Promise(resolve => setTimeout(resolve, 800))
        
        // 更新点赞数
        stats.value.totalLikes += 1
        stats.value.todayLikes += 1
        
        // 显示成功提示（可以后续集成toast组件）
        console.log('点赞成功！')
    } catch (error) {
        console.error('点赞失败:', error)
    } finally {
        isLiking.value = false
    }
}

// 模拟实时更新运行时长
const updateUptime = () => {
    const startDate = new Date('2024-01-01') // 网站启动日期
    const now = new Date()
    const diffTime = Math.abs(now - startDate)
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
    stats.value.uptime = `${diffDays}天`
}

onMounted(() => {
    updateUptime()
    // 每小时更新一次运行时长
    setInterval(updateUptime, 3600000)
})
</script>