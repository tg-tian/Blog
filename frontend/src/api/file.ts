import request from './index'
import type { ApiResponse } from '@/types'

export const getOssUploadUrl = (prefix: string, filename: string) =>
  request.post<any, ApiResponse<{ uploadUrl: string; objectName: string }>>('/file/getUploadUrl', {
    prefix,
    filename
  })
