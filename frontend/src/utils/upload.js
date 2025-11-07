import { getOssUploadUrl, getPresignedUrl } from '@/api/file'

/**
 * 文件上传工具类
 * 
 * uploadUrl: 用于上传文件到MinIO的预签名URL，包含临时访问凭证
 * objectName: 文件在OSS中的对象名称，用于存储到数据库
 * 
 * MinIO上传流程：
 * 1. 后端生成预签名URL (uploadUrl) 和对象名称 (objectName)
 * 2. 前端使用预签名URL直接上传到MinIO
 * 3. 上传成功后使用objectName存储到数据库，访问时需要拼接完整URL
 */

/**
 * 验证文件类型和大小
 * @param {File} file - 要验证的文件
 * @param {Object} options - 验证选项
 * @param {string[]} options.allowedTypes - 允许的文件类型，如 ['image/*', 'application/pdf']
 * @param {number} options.maxSize - 最大文件大小（字节），默认5MB
 * @returns {Object} { valid: boolean, error: string }
 */
export const validateFile = (file, options = {}) => {
  const { allowedTypes = ['image/*'], maxSize = 1024 * 1024 * 1024 } = options
  
  if (!file) {
    return { valid: false, error: '请选择文件' }
  }
  
  // 验证文件类型
  const isValidType = allowedTypes.some(type => {
    if (type.endsWith('/*')) {
      return file.type.startsWith(type.replace('/*', '/'))
    }
    return file.type === type
  })
  
  if (!isValidType) {
    return { valid: false, error: `请选择正确的文件类型: ${allowedTypes.join(', ')}` }
  }
  
  // 验证文件大小
  if (file.size > maxSize) {
    const maxSizeMB = (maxSize / (1024 * 1024)).toFixed(1)
    return { valid: false, error: `文件大小不能超过${maxSizeMB}MB` }
  }
  
  return { valid: true, error: null }
}

/**
 * 创建文件预览URL
 * @param {File} file - 文件对象
 * @returns {Promise<string>} 预览URL
 */
export const createPreviewUrl = (file) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = (e) => resolve(e.target.result)
    reader.onerror = () => reject(new Error('读取文件失败'))
    reader.readAsDataURL(file)
  })
}

/**
 * 上传文件到MinIO
 * @param {File} file - 要上传的文件
 * @param {Object} options - 上传选项
 * @param {string} options.prefix - 文件存储前缀，默认为'uploads'
 * @param {Function} options.onProgress - 进度回调函数 (progress: number) => void
 * @param {Object} options.validation - 文件验证选项
 * @returns {Promise<Object>} { success: boolean, objectName: string, error: string }
 */
export const uploadToMinio = async (file, options = {}) => {
  const { prefix = 'uploads', onProgress, validation = {} } = options
  
  try {
    // 验证文件
    const validationResult = validateFile(file, validation)
    if (!validationResult.valid) {
      return { success: false, objectName: null, error: validationResult.error }
    }
    
    // 2. 获取OSS上传URL
    // uploadUrl: MinIO预签名上传URL，包含临时访问凭证，用于直接上传
    // objectName: 文件在OSS中的对象名称，用于存储到数据库
    const response = await getOssUploadUrl(prefix, file.name)
    const data = response.data || response
    
    if (!data.uploadUrl || !data.objectName) {
      return { success: false, objectName: null, error: '获取上传地址失败' }
    }
    
    let { uploadUrl, objectName } = data
    
    // 将MinIO内网地址转换为nginx代理地址
    if (uploadUrl.includes('http://minio:9002')) {
      const minioHost = import.meta.env.VITE_MINIO_HOST || 'localhost'
      uploadUrl = uploadUrl.replace('http://minio:9002', `https://${minioHost}/minio`)
    }
    
    // 3. 使用XMLHttpRequest上传到MinIO
    return new Promise((resolve) => {
      const xhr = new XMLHttpRequest()
      
      // 监听上传进度
      xhr.upload.addEventListener('progress', (e) => {
        if (e.lengthComputable && onProgress) {
          const progress = Math.round((e.loaded / e.total) * 100)
          onProgress(progress)
        }
      })
      
      // 上传完成
      xhr.addEventListener('load', () => {
        if (xhr.status >= 200 && xhr.status < 300) {
          resolve({ success: true, objectName, error: null })
        } else {
          resolve({ success: false, objectName: null, error: `上传失败: ${xhr.statusText}` })
        }
      })
      
      // 上传错误
      xhr.addEventListener('error', () => {
        resolve({ success: false, objectName: null, error: '网络错误，上传失败' })
      })
      
      // 上传超时
      xhr.addEventListener('timeout', () => {
        resolve({ success: false, objectName: null, error: '上传超时，请重试' })
      })
      
      // 配置请求
      xhr.open('PUT', uploadUrl)
      xhr.timeout = 60000 // 60秒超时
      
      // MinIO通常需要设置Content-Type
      xhr.setRequestHeader('Content-Type', file.type)
      
      // 发送文件
      xhr.send(file)
    })
    
  } catch (error) {
    console.error('上传失败:', error)
    return { success: false, objectName: null, error: error.message || '上传失败' }
  }
}

/**
 * 批量上传文件
 * @param {File[]} files - 文件数组
 * @param {Object} options - 上传选项
 * @returns {Promise<Object[]>} 上传结果数组
 */
export const uploadMultipleFiles = async (files, options = {}) => {
  const results = []
  
  for (const file of files) {
    const result = await uploadToMinio(file, options)
    results.push({ file: file.name, ...result })
  }
  
  return results
}

/**
 * 将objectName转换为完整的访问URL（支持私有bucket预签名URL）
 * @param {string} objectName - OSS对象名称
 * @returns {Promise<string>} 完整的访问URL
 */
export const getFileUrl = async (objectName) => {
  if (!objectName) return ''
  
  // 如果已经是完整URL，直接返回
  if (objectName.startsWith('http://') || objectName.startsWith('https://')) {
    return objectName
  }
  
  try {
    // 获取预签名URL用于访问私有bucket中的文件
    const response = await getPresignedUrl(objectName)
    const data = response.data || response
    if (data.presignedUrl) {
      // 将MinIO内部地址转换为nginx代理地址
      let processedUrl = data.presignedUrl
      // 替换 http://minio:9002 为 环境配置的主机地址/minio
      if (processedUrl.includes('http://minio:9002')) {
        const minioHost = import.meta.env.VITE_MINIO_HOST || 'localhost'
        processedUrl = processedUrl.replace('http://minio:9002', `https://${minioHost}/minio`)
      }
      
      return processedUrl
    }
  } catch (error) {
    console.warn('获取预签名URL失败，使用直接URL:', error)
    // 回退到直接拼接URL
    const minioHost = import.meta.env.VITE_MINIO_HOST || 'localhost'
    const minioBaseUrl = import.meta.env.VITE_MINIO_BASE_URL || `https://${minioHost}/minio`
    const bucketName = import.meta.env.VITE_MINIO_BUCKET || 'blog'
    return `${minioBaseUrl}/${bucketName}/${objectName}`
  }
}

/**
 * 删除预览URL（释放内存）
 * @param {string} url - 预览URL
 */
export const revokePreviewUrl = (url) => {
  if (url && url.startsWith('blob:')) {
    URL.revokeObjectURL(url)
  }
}