<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-b from-gray-100 to-gray-200 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8 bg-white p-8 rounded-xl shadow-lg">
      <div>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
          {{ step === 1 ? '验证邮箱' : '完善信息' }}
        </h2>
        <p class="mt-2 text-center text-sm text-gray-600">
          {{ step === 1 ? '请输入邮箱获取验证码' : '设置您的账户信息' }}
        </p>
      </div>

      <!-- Step 1: Email Verification -->
      <form v-if="step === 1" class="mt-8 space-y-6" @submit.prevent="handleVerify">
        <div class="rounded-md shadow-sm -space-y-px">
          <div>
            <label for="reg-email" class="sr-only">邮箱地址</label>
            <input
              id="reg-email"
              name="email"
              type="email"
              required
              v-model="form.email"
              class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
              placeholder="邮箱地址"
            />
          </div>
          <div class="flex">
            <div class="flex-grow">
              <label for="reg-code" class="sr-only">验证码</label>
              <input
                id="reg-code"
                name="code"
                type="text"
                required
                v-model="form.code"
                class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-bl-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
                placeholder="验证码"
              />
            </div>
            <button
              type="button"
              @click="handleSendCode"
              :disabled="countdown > 0 || loading"
              class="relative -ml-px inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-br-md text-gray-700 bg-gray-50 hover:bg-gray-100 focus:outline-none focus:ring-1 focus:ring-blue-500 focus:border-blue-500 disabled:opacity-50 disabled:cursor-not-allowed w-32 justify-center"
            >
              {{ countdown > 0 ? `${countdown}s` : '发送验证码' }}
            </button>
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
            {{ loading ? '验证中...' : '下一步' }}
          </button>
        </div>
      </form>

      <!-- Step 2: Profile Setup -->
      <form v-else class="mt-8 space-y-6" @submit.prevent="handleRegister">
        <div class="flex justify-center mb-6">
          <div class="relative group cursor-pointer" @click="triggerFileInput">
            <div class="w-24 h-24 rounded-full overflow-hidden border-2 border-gray-300 bg-gray-100 flex items-center justify-center">
              <img v-if="avatarPreview" :src="avatarPreview" class="w-full h-full object-cover" alt="Avatar" />
              <svg v-else class="w-12 h-12 text-gray-400" fill="currentColor" viewBox="0 0 24 24">
                <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z" />
              </svg>
            </div>
            <div class="absolute inset-0 bg-black bg-opacity-40 rounded-full flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
              <span class="text-white text-xs">更换头像</span>
            </div>
            <input 
              type="file" 
              ref="fileInput" 
              class="hidden" 
              accept="image/*"
              @change="handleAvatarChange"
            />
          </div>
        </div>

        <div class="rounded-md shadow-sm -space-y-px">
          <div>
            <label for="reg-nickname" class="sr-only">昵称</label>
            <input
              id="reg-nickname"
              name="nickname"
              type="text"
              required
              v-model="form.nickname"
              class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
              placeholder="昵称"
            />
          </div>
          <div>
            <label for="reg-password" class="sr-only">密码</label>
            <input
              id="reg-password"
              name="password"
              type="password"
              required
              v-model="form.password"
              class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
              placeholder="密码"
            />
          </div>
          <div>
            <label for="reg-confirm-password" class="sr-only">确认密码</label>
            <input
              id="reg-confirm-password"
              name="confirm-password"
              type="password"
              required
              v-model="form.confirmPassword"
              class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
              placeholder="确认密码"
            />
          </div>
        </div>

        <StatusMessage 
          v-if="error" 
          type="error" 
          :message="error" 
          :visible="!!error" 
        />
        
        <StatusMessage 
          v-if="successMessage" 
          type="success" 
          :message="successMessage" 
          :visible="!!successMessage" 
        />

        <div>
          <button
            type="submit"
            :disabled="loading"
            class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50"
          >
            {{ loading ? '注册中...' : '完成注册' }}
          </button>
        </div>
      </form>
        
      <div class="text-center mt-4">
        <button 
          type="button"
          @click="goBack"
          class="font-medium text-blue-600 hover:text-blue-500 text-sm"
        >
          {{ step === 1 ? '已有账号？去登录' : '返回上一步' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { register, sendCode, verifyCode } from '@/api/user'
import { uploadToMinio, createPreviewUrl } from '@/utils/upload'
import StatusMessage from '@/components/StatusMessage.vue'

const router = useRouter()

const step = ref(1)
const loading = ref(false)
const error = ref('')
const successMessage = ref('')
const countdown = ref(0)
const fileInput = ref(null)
const avatarPreview = ref('')
const avatarFile = ref(null)

const registerToken = ref('')

const form = reactive({
  email: '',
  code: '',
  nickname: '',
  password: '',
  confirmPassword: '',
  avatar: ''
})

const handleSendCode = async () => {
  if (!form.email) {
    error.value = '请输入邮箱地址'
    return
  }
  
  loading.value = true
  error.value = ''
  
  try {
    await sendCode(form.email)
    startCountdown()
    successMessage.value = '验证码已发送'
    setTimeout(() => successMessage.value = '', 3000)
  } catch (err) {
    error.value = err.message || '发送验证码失败'
  } finally {
    loading.value = false
  }
}

const startCountdown = () => {
  countdown.value = 60
  const timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
    }
  }, 1000)
}

const handleVerify = async () => {
  if (!form.email || !form.code) {
    error.value = '请输入邮箱和验证码'
    return
  }

  loading.value = true
  error.value = ''

  try {
    const token = await verifyCode({ email: form.email, code: form.code })
    registerToken.value = token
    step.value = 2
  } catch (err) {
    error.value = err.message || '验证码错误'
  } finally {
    loading.value = false
  }
}

const triggerFileInput = () => {
  fileInput.value.click()
}

const handleAvatarChange = async (event) => {
  const file = event.target.files[0]
  if (!file) return

  try {
    const preview = await createPreviewUrl(file)
    avatarPreview.value = preview
    avatarFile.value = file
  } catch (err) {
    error.value = '无法读取图片文件'
  }
}

const handleRegister = async () => {
  if (form.password !== form.confirmPassword) {
    error.value = '两次输入的密码不一致'
    return
  }

  loading.value = true
  error.value = ''
  successMessage.value = ''
  
  try {
    let avatarUrl = ''
    if (avatarFile.value) {
      const uploadRes = await uploadToMinio(avatarFile.value, { prefix: 'avatars' })
      if (!uploadRes.success) {
        throw new Error(uploadRes.error || '头像上传失败')
      }
      avatarUrl = uploadRes.objectName
    }

    await register({
      email: form.email,
      username: form.nickname,
      password: form.password,
      registerToken: registerToken.value,
      avatar: avatarUrl
    })
    
    successMessage.value = '注册成功，请登录'
    
    // Clear form
    Object.keys(form).forEach(key => form[key] = '')
    registerToken.value = ''
    avatarFile.value = null
    avatarPreview.value = ''
    
    // Delay switching to login to let user see success message
    setTimeout(() => {
        router.push('/login')
    }, 1500)
  } catch (err) {
    error.value = err.message || '注册失败'
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  if (step.value === 2) {
    step.value = 1
    error.value = ''
  } else {
    router.push('/login')
  }
}
</script>
