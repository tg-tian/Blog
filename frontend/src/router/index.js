import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '@/layouts/MainLayout.vue'
import Home from '@/pages/Home.vue'
import Article from '@/components/Article.vue'
import About from '@/pages/About.vue'
import Blogs from '@/pages/Blogs.vue'
import Projects from '@/pages/Projects.vue'
import Login from '@/pages/Login.vue'
import Admin from '@/pages/Admin.vue'
import { useUserStore } from '@/stores/user'

const routes = [
    {
        path: '/',
        component: Home
    },
    {
        path: '/main',
        component: MainLayout,
        children: [
            { path: 'article/:id', component: Article },
            { path: 'about', component: About },
            { path: 'blog', component: Blogs },
            { path: 'projects', component: Projects }
        ]
    },
    {
        path: '/login',
        component: Login
    },
    {
        path: '/admin',
        component: Admin,
        meta: { requiresAuth: true, requiresAdmin: true },
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 路由守卫，保护需要认证的路由
router.beforeEach((to, from, next) => {
    const userStore = useUserStore()
    
    // 检查路由是否需要认证
    if (to.meta.requiresAuth) {
        // 检查用户是否已登录
        if (!userStore.isLogin) {
            next('/login')
            return
        }
        
        // 检查路由是否需要管理员权限
        if (to.meta.requiresAdmin && !userStore.isAdmin) {
            next('/')
            return
        }
    }
    
    next()
})

export default router
