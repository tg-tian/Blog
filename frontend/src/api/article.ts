import request from './index'
import type { ApiResponse, Paginated } from '@/types'
import type { Article } from '@/types'

export const getArticles = (page: number, size: number) =>
  request.get<any, ApiResponse<Paginated<Article>>>(`/article?page=${page}&size=${size}`)

export const getArticle = (id: number | string) =>
  request.get<any, ApiResponse<Article>>(`/article/${id}`)

export const createArticle = (data: Partial<Article>) =>
  request.post<any, ApiResponse<Article>>('/article', data)

export const updateArticle = (id: number | string, data: Partial<Article>) =>
  request.put<any, ApiResponse<Article>>(`/article/${id}`, data)

export const deleteArticle = (id: number | string) =>
  request.delete<any, ApiResponse<boolean>>(`/article/${id}`)

export const likeArticle = (id: number | string) =>
  request.post<any, ApiResponse<boolean>>(`/article/${id}/like`)

export const incrementViews = (id: number | string) =>
  request.post<any, ApiResponse<boolean>>(`/article/${id}/view`)

export const updateCommentCount = (id: number | string, count: number) =>
  request.put<any, ApiResponse<boolean>>(`/article/${id}/comments`, { count })

export const getArticlesByCategory = (categoryId: number | string, page: number, size: number) =>
  request.get<any, ApiResponse<Paginated<Article>>>(`/article/category/${categoryId}?page=${page}&size=${size}`)

export const getArticlesByTag = (tagId: number | string, page: number, size: number) =>
  request.get<any, ApiResponse<Paginated<Article>>>(`/article/tag/${tagId}?page=${page}&size=${size}`)

