const getObjectNameFromUrl = (url: string): string | null => {
  try {
    let path = url
    if (path.startsWith('http')) {
      path = new URL(url).pathname
    }

    const queryIndex = path.indexOf('?')
    if (queryIndex !== -1) {
      path = path.substring(0, queryIndex)
    }

    const parts = path.split('/').filter(Boolean)

    if (
      parts.length >= 3 &&
      parts[0] === 'minio' &&
      parts[1] === (import.meta.env.VITE_MINIO_BUCKET || 'blog')
    ) {
      return parts.slice(2).join('/')
    } else if (parts.length > 1 && parts[0] === (import.meta.env.VITE_MINIO_BUCKET || 'blog')) {
      return parts.slice(1).join('/')
    }
    return parts.join('/')
  } catch (e) {
    console.error('从URL解析objectName失败:', url, e)
    return null
  }
}

export const extractImageObjectNames = (markdownText: string): string[] => {
  if (!markdownText) return []

  const imageRegex = /!\[.*?\]\((.*?)\)/g
  const objectNames = new Set<string>()
  let match: RegExpExecArray | null

  while ((match = imageRegex.exec(markdownText)) !== null) {
    const objectName = getObjectNameFromUrl(match[1])
    if (objectName) {
      objectNames.add(objectName)
    }
  }
  return [...objectNames]
}

export const refreshMarkdownImageUrls = async (markdownText: string): Promise<string> => {
  if (!markdownText) return markdownText
  try {
    return markdownText.replace(/!\[(.*?)\]\((.*?)\)/g, (match, alt, url) => {
      if (!url) return match
      return `<div style="text-align: center; margin: 1rem 0;"><img src="${url}" alt="${alt}" style="max-width: 80%; height: auto; border-radius: 8px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);" /></div>`
    })
  } catch (error) {
    console.error('刷新 Markdown 图片链接失败:', error)
    return markdownText
  }
}
