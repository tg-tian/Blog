import request from './index'

// 登录
export const login = (data) =>
    request.post('/auth/login', data)

// 登出
export const logout = () =>
    request.post('/auth/logout')

// 验证token
export const verifyToken = () =>
    request.get('/auth/verify')
