import request from './index'
import type { ApiResponse } from '@/types'
import type { SiteStats } from '@/types'

export const getSiteStats = () =>
  request.get<any, ApiResponse<SiteStats>>('/site-stats/all')

export const likeSite = () =>
  request.post<any, ApiResponse<boolean>>('/site-stats/likes/increment')

export const visitSite = () =>
  request.post<any, ApiResponse<boolean>>('/site-stats/visits/increment')

