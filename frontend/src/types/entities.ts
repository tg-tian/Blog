export interface Tag {
  id: number
  name: string
  count?: number
}

export interface Category {
  id: number
  name: string
  count?: number
}

export interface Article {
  id: number
  title: string
  summary?: string
  content: string
  categoryId?: number
  tags?: Tag[]
  views?: number
  likes?: number
  commentCount?: number
  coverUrl?: string
  publishTime: string
  updateTime: string
  comments?: number
}

export interface Project {
  id: number
  name: string
  title: string
  description: string
  content: string
  tags?: Tag[]
  coverImage: string
  link: string
}

export interface SiteStats {
  likes: number
  visits: number
  articles?: number
  projects?: number
  tags?: number
  categories?: number
}

export interface JwtPayload {
  sub?: string | number
  username?: string
  role?: string | string[]
  exp?: number
  [key: string]: unknown
}

