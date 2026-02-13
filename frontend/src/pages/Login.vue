<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-b from-gray-100 to-gray-200 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8 bg-white p-8 rounded-xl shadow-lg">
      <div>
        <div>
          <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
            用户登录
          </h2>
        </div>
        <form class="mt-8 space-y-6" @submit.prevent="handleLogin">
          <div class="rounded-md shadow-sm -space-y-px">
            <div>
              <label for="email" class="sr-only">邮箱</label>
              <input
                id="email"
                name="email"
                type="email"
                required
                v-model="loginForm.email"
                class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
                placeholder="邮箱地址"
              />
            </div>
            <div>
              <label for="password" class="sr-only">密码</label>
              <input
                id="password"
                name="password"
                type="password"
                required
                v-model="loginForm.password"
                class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
                placeholder="密码"
              />
            </div>
          </div>

          <StatusMessage 
            v-if="error" 
            type="error" 
            :message="error" 
            :visible="!!error" 
          />

          <div>
            <button
              type="submit"
              :disabled="loading"
              class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50"
            >
              {{ loading ? '登录中...' : '登录' }}
            </button>
          </div>

          <div class="text-center mt-4">
            <button 
              type="button"
              @click="router.push('/register')"
              class="font-medium text-blue-600 hover:text-blue-500 text-sm"
            >
              没有账号？去注册
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { login } from '@/api/user'
import StatusMessage from '@/components/StatusMessage.vue'

const router = useRouter()
const userStore = useUserStore()

const loginForm = ref({
  email: '',
  password: ''
})

const loading = ref(false)
const error = ref('')

const handleLogin = async () => {
  loading.value = true
  error.value = ''
  
  try {
    const response = await login(loginForm.value)
    // 后端可能返回 { token } 或直接返回字符串令牌
    const token = response?.token ?? response
    userStore.login({
      token
    })
    router.push('/admin')
  } catch (err) {
    error.value = err.message || '登录失败'
  } finally {
    loading.value = false
  }
}
</script>
