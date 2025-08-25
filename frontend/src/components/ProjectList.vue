<template>
    <div class="grid-responsive">
        <div v-for="(project, index) in projects" :key="index"
            class="card-base card-content flex flex-col card-hover">
            <img v-if="project.image" :src="project.image" alt="Project Image" 
                class="project-image mb-3" 
                @error="handleImageError" />
            <h3 class="heading-md mb-2">{{ project.title }}</h3>
            <p class="text-description flex-grow">{{ project.description }}</p>
            <a v-if="project.link" :href="project.link" target="_blank" 
                class="mt-4 text-link">
                查看详情 →
            </a>
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
