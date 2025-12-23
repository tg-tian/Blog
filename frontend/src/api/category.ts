import request from './index'
import type { ApiResponse } from '@/types'
import type { Category } from '@/types'

export const getCategories = () =>
  request.get<any, ApiResponse<Category[]>>('/category')

export const getCategory = (id: number | string) =>
  request.get<any, ApiResponse<Category>>(`/category/${id}`)

export const createCategory = (data: Partial<Category>) =>
  request.post<any, ApiResponse<Category>>('/category', data)

export const updateCategory = (id: number | string, data: Partial<Category>) =>
  request.put<any, ApiResponse<Category>>(`/category/${id}`, data)

export const deleteCategory = (id: number | string) =>
  request.delete<any, ApiResponse<boolean>>(`/category/${id}`)

export const getCategoryStats = () =>
  request.get<any, ApiResponse<any>>('/article/categories/stats')

