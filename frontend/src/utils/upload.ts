import { getOssUploadUrl, getPresignedUrl } from '@/api/file'

export interface ValidateOptions {
  allowedTypes?: string[]
  maxSize?: number
}

export const validateFile = (file: File, options: ValidateOptions = {}) => {
  const { allowedTypes = ['image/*'], maxSize = 1024 * 1024 * 1024 } = options

  if (!file) {
    return { valid: false, error: '请选择文件' }
  }

  const isValidType = allowedTypes.some(type => {
    if (type.endsWith('/*')) {
      return file.type.startsWith(type.replace('/*', '/'))
    }
    return file.type === type
  })

  if (!isValidType) {
    return { valid: false, error: `请选择正确的文件类型: ${allowedTypes.join(', ')}` }
  }

  if (file.size > maxSize) {
    const maxSizeMB = (maxSize / (1024 * 1024)).toFixed(1)
    return { valid: false, error: `文件大小不能超过${maxSizeMB}MB` }
  }

  return { valid: true, error: null as string | null }
}

export const createPreviewUrl = (file: File): Promise<string> => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = (e) => resolve(String(e.target?.result || ''))
    reader.onerror = () => reject(new Error('读取文件失败'))
    reader.readAsDataURL(file)
  })
}

export interface UploadOptions {
  prefix?: string
  onProgress?: (progress: number) => void
  validation?: ValidateOptions
}

export const uploadToMinio = async (
  file: File,
  options: UploadOptions = {}
): Promise<{ success: boolean; objectName: string | null; error: string | null }> => {
  const { prefix = 'uploads', onProgress, validation = {} } = options

  try {
    const validationResult = validateFile(file, validation)
    if (!validationResult.valid) {
      return { success: false, objectName: null, error: validationResult.error }
    }

    const response = await getOssUploadUrl(prefix, file.name)
    const data: any = (response as any).data || response

    if (!data.uploadUrl || !data.objectName) {
      return { success: false, objectName: null, error: '获取上传地址失败' }
    }

    let { uploadUrl, objectName } = data as { uploadUrl: string; objectName: string }

    if (uploadUrl.includes('http://minio:9002')) {
      const minioHost = import.meta.env.VITE_MINIO_HOST || 'localhost'
      uploadUrl = uploadUrl.replace('http://minio:9002', `https://${minioHost}/minio`)
    }

    return new Promise((resolve) => {
      const xhr = new XMLHttpRequest()

      xhr.upload.addEventListener('progress', (e) => {
        if (e.lengthComputable && onProgress) {
          const progress = Math.round((e.loaded / e.total) * 100)
          onProgress(progress)
        }
      })

      xhr.addEventListener('load', () => {
        if (xhr.status >= 200 && xhr.status < 300) {
          resolve({ success: true, objectName, error: null })
        } else {
          resolve({ success: false, objectName: null, error: `上传失败: ${xhr.statusText}` })
        }
      })

      xhr.addEventListener('error', () => {
        resolve({ success: false, objectName: null, error: '网络错误，上传失败' })
      })

      xhr.addEventListener('timeout', () => {
        resolve({ success: false, objectName: null, error: '上传超时，请重试' })
      })

      xhr.open('PUT', uploadUrl)
      xhr.timeout = 60000
      xhr.setRequestHeader('Content-Type', file.type)
      xhr.send(file)
    })
  } catch (error: any) {
    console.error('上传失败:', error)
    return { success: false, objectName: null, error: error?.message || '上传失败' }
  }
}

export const uploadMultipleFiles = async (
  files: File[],
  options: UploadOptions = {}
): Promise<Array<{ file: string; success: boolean; objectName: string | null; error: string | null }>> => {
  const results: Array<{ file: string; success: boolean; objectName: string | null; error: string | null }> = []

  for (const file of files) {
    const result = await uploadToMinio(file, options)
    results.push({ file: file.name, ...result })
  }

  return results
}

export const getFileUrl = async (objectName: string): Promise<string> => {
  if (!objectName) return ''

  if (objectName.startsWith('http://') || objectName.startsWith('https://')) {
    return objectName
  }

  try {
    const response = await getPresignedUrl(objectName)
    const data: any = (response as any).data || response
    if (data.presignedUrl) {
      let processedUrl: string = data.presignedUrl
      if (processedUrl.includes('http://minio:9002')) {
        const minioHost = import.meta.env.VITE_MINIO_HOST || 'localhost'
        processedUrl = processedUrl.replace('http://minio:9002', `https://${minioHost}/minio`)
      }
      return processedUrl
    }
  } catch (error) {
    console.warn('获取预签名URL失败，使用直接URL:', error)
    const minioHost = import.meta.env.VITE_MINIO_HOST || 'localhost'
    const minioBaseUrl = import.meta.env.VITE_MINIO_BASE_URL || `https://${minioHost}/minio`
    const bucketName = import.meta.env.VITE_MINIO_BUCKET || 'blog'
    return `${minioBaseUrl}/${bucketName}/${objectName}`
  }
  return ''
}

export const revokePreviewUrl = (url: string) => {
  if (url && url.startsWith('blob:')) {
    URL.revokeObjectURL(url)
  }
}

