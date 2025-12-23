import { defineStore } from 'pinia'
import { parseJwt } from '@/utils/jwt'
import type { JwtPayload } from '@/types'

export interface UserState {
  username: string
  isLogin: boolean
  isAdmin: boolean
  token: string
  id: number | null
  role: string
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    username: '',
    isLogin: false,
    isAdmin: false,
    token: '',
    id: null,
    role: ''
  }),
  actions: {
    login(data: { token?: string }) {
      this.isLogin = true
      let parsed: JwtPayload = {}
      if (data.token) {
        parsed = parseJwt(data.token) || {}
      }
      const roleValue = Array.isArray(parsed.role) ? parsed.role[0] : (parsed.role ?? '')
      this.role = String(roleValue || '')
      this.username = String(parsed.username ?? '')
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
    pick: ['username', 'isLogin', 'isAdmin', 'token']
  }
})
