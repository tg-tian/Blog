import request from './index'

// 获取OSS存储路径
export const getOssUploadUrl = (prefix, filename) =>
    request.post('/file/getUploadUrl', { prefix, filename })

// 获取文件预签名URL（用于访问私有bucket中的文件）
export const getPresignedUrl = (objectName) =>
    request.post('/file/getPresignedUrl', { objectName })