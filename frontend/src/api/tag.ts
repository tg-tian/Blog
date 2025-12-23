import request from './index'
import type { ApiResponse } from '@/types'
import type { Tag } from '@/types'

export const getTags = () =>
  request.get<any, ApiResponse<Tag[]>>('/tag')

export const getTag = (id: number | string) =>
  request.get<any, ApiResponse<Tag>>(`/tag/${id}`)

export const createTag = (data: Partial<Tag>) =>
  request.post<any, ApiResponse<Tag>>('/tag', data)

export const updateTag = (id: number | string, data: Partial<Tag>) =>
  request.put<any, ApiResponse<Tag>>(`/tag/${id}`, data)

export const deleteTag = (id: number | string) =>
  request.delete<any, ApiResponse<boolean>>(`/tag/${id}`)

export const getTagStats = () =>
  request.get<any, ApiResponse<any>>('/article/tags/stats')

