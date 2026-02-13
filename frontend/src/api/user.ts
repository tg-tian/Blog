import request from './index'
import type { ApiResponse } from '@/types'

export const login = (data: { email: string; password: string }) =>
  request.post<any, string>('/user/login', data)

export const register = (data: { 
  email: string; 
  username: string; 
  password: string; 
  registerToken: string;
  avatar?: string 
}) =>
  request.post<any, string>('/user/register', data)

export const sendCode = (email: string) =>
  request.post<any, string>('/user/register/code', { email })

export const verifyCode = (data: { email: string; code: string }) =>
  request.post<any, string>('/user/register/verify', data)

export const logout = () =>
  request.post<any, ApiResponse<boolean>>('/user/logout')

export const verifyToken = () =>
  request.get<any, ApiResponse<boolean>>('/user/verify')
