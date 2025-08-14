import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
    state: () => ({
        username: '',
        avatar: '',
        isLogin: false
    }),
    actions: {
        login(data) {
            this.username = data.username
            this.avatar = data.avatar
            this.isLogin = true
        }
    },
    persist: true
})
