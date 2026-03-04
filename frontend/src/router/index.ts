import { createRouter, createWebHistory, type RouteRecordRaw, type RouteLocationNormalized, type NavigationGuardNext } from 'vue-router'
import MainLayout from '@/layouts/MainLayout.vue'
import Home from '@/pages/Home.vue'
import Article from '@/pages/Article.vue'
import About from '@/pages/About.vue'
import Archive from '@/pages/Archive.vue'
import Login from '@/pages/Login.vue'
import Register from '@/pages/Register.vue'
import Admin from '@/pages/Admin.vue'
import { useUserStore } from '@/stores/user'

const routes: RouteRecordRaw[] = [
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
      { path: 'archive', component: Archive }
    ]
  },
  {
    path: '/login',
    component: Login
  },
  {
    path: '/register',
    component: Register
  },
  {
    path: '/admin',
    component: Admin,
    meta: { requiresAuth: true, requiresAdmin: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to: RouteLocationNormalized, _from: RouteLocationNormalized, next: NavigationGuardNext) => {
  const userStore = useUserStore()
  if (to.meta.requiresAuth) {
    if (!userStore.isLogin) {
      next('/login')
      return
    }
    if ((to.meta as any).requiresAdmin && !userStore.isAdmin) {
      next('/')
      return
    }
  }
  next()
})

export default router

