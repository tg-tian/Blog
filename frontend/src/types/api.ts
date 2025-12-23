export interface ApiResponse<T = unknown> {
  code?: number
  message?: string
  data?: T
}

export interface Paginated<T = unknown> {
  list: T[]
  total?: number
  page?: number
  size?: number
}

export type MaybePaginated<T> = ApiResponse<Paginated<T>> | ApiResponse<T> | Paginated<T> | T

export interface IdParam {
  id: number | string
}

export interface SuccessResponse {
  success: boolean
}

