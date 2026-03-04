import axios, { AxiosInstance, AxiosResponse, type InternalAxiosRequestConfig } from 'axios'
import router from '@/router'
import { useUserStore } from '@/stores/user'

const instance: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 5000
})

instance.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const userStore = useUserStore()
    const token = userStore?.token
    if (token) {
      config.headers = config.headers || {}
      ;(config.headers as any).Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

const handleError = (error: any) => {
  let message = '请求失败，请稍后重试'

  if (error.response) {
    const { status, data } = error.response as AxiosResponse
    switch (status) {
      case 400:
        message = (data as any)?.message || '请求参数错误'
        break
      case 401:
        message = '未登录或会话已过期，请先登录'
        try {
          const userStore = useUserStore()
          userStore.logout()
        } catch {}
        router.push('/login')
        break
      case 403:
        message = '权限不足，无法访问'
        break
      case 404:
        message = '请求的资源不存在'
        break
      case 500:
        message = '服务器内部错误'
        break
      case 502:
        message = '网关错误'
        break
      case 503:
        message = '服务暂时不可用'
        break
      default:
        message = (data as any)?.message || `请求失败 (${status})`
    }
  } else if (error.request) {
    if (error.code === 'ECONNABORTED') {
      message = '请求超时，请检查网络连接'
    } else {
      message = '网络连接失败，请检查网络设置'
    }
  } else {
    message = error.message || '未知错误'
  }

  return Promise.reject(new Error(message))
}

instance.interceptors.response.use(
  (res: AxiosResponse) => {
    const { code, message, data } = res.data
    if (code === 200) {
      return data
    }
    return Promise.reject(new Error(message || 'Error'))
  },
  handleError
)

export default instance
