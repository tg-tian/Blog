import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '@/layouts/MainLayout.vue'
import Home from '@/pages/Home.vue'
import Article from '@/pages/Article.vue'

const routes = [
    {
        path: '/',
        component: MainLayout,
        children: [
            { path: '', component: Home },
            { path: 'article/:id', component: Article }
        ]
    }
]

export default createRouter({
    history: createWebHistory(),
    routes
})
