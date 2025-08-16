<template>
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        <ProjectCard v-for="(project, index) in projects" :key="index" :title="project.title"
            :description="project.description" :image="project.image" :link="project.link" />
    </div>
</template>

<script setup>
import ProjectCard from './ProjectCard.vue'

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
</script>
