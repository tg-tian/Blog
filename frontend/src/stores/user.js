import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
    state: () => ({
        username: '',
        isLogin: false,
        isAdmin: false
    }),
    actions: {
        login(data) {
            this.username = data.username
            this.isLogin = true
            this.isAdmin = data.isAdmin || false
        },
        logout() {
            this.username = ''
            this.isLogin = false
            this.isAdmin = false
        }
    },
    persist: true
})
