import request from './index'
import type { ApiResponse } from '@/types'
import type { JwtPayload } from '@/types'

export const login = (data: { username: string; password: string }) =>
  request.post<any, ApiResponse<{ token: string; user?: JwtPayload }>>('/auth/login', data)

export const logout = () =>
  request.post<any, ApiResponse<boolean>>('/auth/logout')

export const verifyToken = () =>
  request.get<any, ApiResponse<boolean>>('/auth/verify')

