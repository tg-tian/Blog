import request from './index'
import type { ApiResponse, Paginated } from '@/types'
import type { Project } from '@/types'

export const getProjects = (page?: number, size?: number) => {
  if (page && size) {
    return request.get<any, ApiResponse<Paginated<Project>>>(`/project?page=${page}&size=${size}`)
  }
  return request.get<any, ApiResponse<Paginated<Project>>>('/project/list')
}

export const getProject = (id: number | string) =>
  request.get<any, ApiResponse<Project>>(`/project/${id}`)

export const createProject = (data: Partial<Project>) =>
  request.post<any, ApiResponse<Project>>('/project', data)

export const updateProject = (id: number | string, data: Partial<Project>) =>
  request.put<any, ApiResponse<Project>>(`/project/${id}`, data)

export const deleteProject = (id: number | string) =>
  request.delete<any, ApiResponse<boolean>>(`/project/${id}`)

export const likeProject = (id: number | string) =>
  request.post<any, ApiResponse<boolean>>(`/project/${id}/like`)

export const incrementProjectViews = (id: number | string) =>
  request.post<any, ApiResponse<boolean>>(`/project/${id}/view`)

export const getPopularProjects = (limit: number = 5) =>
  request.get<any, ApiResponse<Paginated<Project>>>(`/project/popular?limit=${limit}`)

export const searchProjects = (keyword: string, page?: number, size?: number) => {
  if (page && size) {
    return request.get<any, ApiResponse<Paginated<Project>>>(`/project/search?keyword=${keyword}&page=${page}&size=${size}`)
  }
  return request.get<any, ApiResponse<Paginated<Project>>>(`/project/search?keyword=${keyword}`)
}

export const getProjectTagStats = () =>
  request.get<any, ApiResponse<any>>('/project/tags/stats')

export const getProjectsByTag = (tagId: number | string, page: number, size: number) =>
  request.get<any, ApiResponse<Paginated<Project>>>(`/project/tag/${tagId}?page=${page}&size=${size}`)

