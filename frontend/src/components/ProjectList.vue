<template>
    <div>
        <div class="border-b-2 border-gray-300 pb-4 mb-4 mt-8">
            <h2 class="text-3xl font-bold text-gray-900 flex items-center">
                <svg class="w-8 h-8 mr-3 text-green-600" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M20 6h-2.18c.11-.31.18-.65.18-1a2.996 2.996 0 0 0-5.5-1.65l-.5.67-.5-.68C10.96 2.54 10.05 2 9 2 7.34 2 6 3.34 6 5c0 .35.07.69.18 1H4c-1.11 0-2 .89-2 2v11c0 1.11.89 2 2 2h16c1.11 0 2-.89 2-2V8c0-1.11-.89-2-2-2zM9 4c.55 0 1 .45 1 1s-.45 1-1 1-1-.45-1-1 .45-1 1-1zm6 0c.55 0 1 .45 1 1s-.45 1-1 1-1-.45-1-1 .45-1 1-1z"/>
                </svg>
                项目列表
            </h2>
        </div>
        <div class="grid-responsive">
        <div v-for="(project, index) in projects" :key="index" class="card-base card-content flex flex-col card-hover">
            <img v-if="project.image" :src="project.image" alt="Project Image" class="project-image mb-3"
                @error="handleImageError" />
            <h3 class="heading-md mb-2">{{ project.title }}</h3>
            <p class="text-description flex-grow">{{ project.description }}</p>
            <a v-if="project.link" :href="project.link" target="_blank" class="mt-4 text-link">
                查看详情 →
            </a>
        </div>
        </div>
    </div>
</template>

<script setup>
const projects = [
    {
        title: '博客系统',
        description: '一个基于Vue3和Spring Boot的个人博客系统。',
        image: 'https://via.placeholder.com/400x200',
        link: 'https://example.com/blog'
    },
    {
        title: '任务管理平台',
        description: '支持团队协作与任务跟踪的Web应用。',
        image: 'https://via.placeholder.com/400x200',
        link: 'https://example.com/task'
    },
    {
        title: '数据可视化系统',
        description: '实时展示业务指标的仪表盘。',
        image: 'https://via.placeholder.com/400x200',
        link: 'https://example.com/dashboard'
    }
]

// 处理图片加载错误
const handleImageError = (event) => {
    event.target.style.display = 'none'
}
</script>
