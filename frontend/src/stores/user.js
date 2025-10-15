import { defineStore } from 'pinia'
import { parseJwt } from '@/utils/jwt'

export const useUserStore = defineStore('user', {
    state: () => ({
        username: '',
        isLogin: false,
        isAdmin: false,
        token: '',
    }),
    actions: {
        login(data) {
            this.isLogin = true
            // 优先依据JWT中的role设置权限
            let parsed = {}
            if (data.token) {
                parsed = parseJwt(data.token) || {}
            }
            const roleValue = Array.isArray(parsed.role) ? parsed.role[0] : (parsed.role ?? '')
            this.role = roleValue || ''
            this.username = String(parsed.username ?? '')
            // isAdmin 根据 role 是否等于 ADMIN
            this.isAdmin = String(roleValue).toUpperCase() === 'ADMIN'
            if (data.token) {
                this.token = data.token
            }
        },
        logout() {
            this.username = ''
            this.isLogin = false
            this.isAdmin = false
            this.token = ''
            this.id = null
            this.role = ''
        }
    },
    persist: {
        paths: ['username', 'isLogin', 'isAdmin', 'token']
    }
})
