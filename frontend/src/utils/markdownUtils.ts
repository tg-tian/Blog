import { getFileUrl } from './upload'

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

export const updateMarkdownImageUrlsOnly = (
  markdownText: string,
  urlMapping: Record<string, string>
): string => {
  if (!markdownText || !urlMapping) return markdownText

  return markdownText.replace(/!\[(.*?)\]\((.*?)\)/g, (match, alt, oldUrl) => {
    const objectName = getObjectNameFromUrl(oldUrl)
    if (objectName && urlMapping[objectName]) {
      return `![${alt}](${urlMapping[objectName]})`
    }
    return match
  })
}

export const updateMarkdownImageUrls = (
  markdownText: string,
  urlMapping: Record<string, string>
): string => {
  if (!markdownText || !urlMapping) return markdownText

  return markdownText.replace(/!\[(.*?)\]\((.*?)\)/g, (match, alt, oldUrl) => {
    const objectName = getObjectNameFromUrl(oldUrl)
    if (objectName && urlMapping[objectName]) {
      const newUrl = urlMapping[objectName]
      return `<div style="text-align: center; margin: 1rem 0;"><img src="${newUrl}" alt="${alt}" style="max-width: 80%; height: auto; border-radius: 8px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);" /></div>`
    }
    return match
  })
}

export const refreshMarkdownImageUrlsForEdit = async (markdownText: string): Promise<string> => {
  if (!markdownText) return markdownText

  try {
    const objectNames = extractImageObjectNames(markdownText)
    if (objectNames.length === 0) {
      return markdownText
    }
    const urlPromises = objectNames.map(async (objectName) => {
      try {
        const newUrl = await getFileUrl(objectName)
        return { objectName, newUrl }
      } catch (error) {
        console.error(`获取图片 URL 失败: ${objectName}`, error)
        return { objectName, newUrl: null as string | null }
      }
    })

    const urlResults = await Promise.all(urlPromises)
    const urlMapping: Record<string, string> = {}
    urlResults.forEach(({ objectName, newUrl }) => {
      if (newUrl) {
        urlMapping[objectName] = newUrl
      }
    })
    return updateMarkdownImageUrlsOnly(markdownText, urlMapping)
  } catch (error) {
    console.error('刷新 Markdown 图片链接失败:', error)
    return markdownText
  }
}

export const refreshMarkdownImageUrls = async (markdownText: string): Promise<string> => {
  if (!markdownText) return markdownText

  try {
    const objectNames = extractImageObjectNames(markdownText)
    if (objectNames.length === 0) {
      return markdownText
    }

    const urlPromises = objectNames.map(async (objectName) => {
      try {
        const newUrl = await getFileUrl(objectName)
        return { objectName, newUrl }
      } catch (error) {
        console.error(`获取图片 URL 失败: ${objectName}`, error)
        return { objectName, newUrl: null as string | null }
      }
    })

    const urlResults = await Promise.all(urlPromises)
    const urlMapping: Record<string, string> = {}
    urlResults.forEach(({ objectName, newUrl }) => {
      if (newUrl) {
        urlMapping[objectName] = newUrl
      }
    })
    return updateMarkdownImageUrls(markdownText, urlMapping)
  } catch (error) {
    console.error('刷新 Markdown 图片链接失败:', error)
    return markdownText
  }
}

export const preprocessArticleContent = async <T extends { content?: string }>(article: T): Promise<T> => {
  if (!article || !article.content) {
    return article
  }
  try {
    const refreshedContent = await refreshMarkdownImageUrls(article.content)
    return {
      ...article,
      content: refreshedContent
    }
  } catch (error) {
    console.error('预处理文章内容失败:', error)
    return article
  }
}

export const preprocessArticleList = async <T extends { content?: string }>(articles: T[]): Promise<T[]> => {
  if (!Array.isArray(articles)) {
    return articles as any
  }
  try {
    const processedArticles = await Promise.all(
      articles.map((article) => preprocessArticleContent(article))
    )
    return processedArticles
  } catch (error) {
    console.error('批量预处理文章列表失败:', error)
    return articles
  }
}

export const renderMarkdown = (markdownText: string): string => {
  if (!markdownText) return ''

  let html = markdownText

  html = html.replace(/^### (.*$)/gim, '<h3>$1</h3>')
  html = html.replace(/^## (.*$)/gim, '<h2>$1</h2>')
  html = html.replace(/^# (.*$)/gim, '<h1>$1</h1>')

  html = html.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
  html = html.replace(/\*(.*?)\*/g, '<em>$1</em>')

  html = html.replace(
    /\[([^\]]+)\]\(([^\)]+)\)/g,
    '<a href="$2" target="_blank" rel="noopener noreferrer">$1</a>'
  )

  html = html.replace(/!\[([^\]]*)\]\(([^\)]+)\)/g, '<img src="$2" alt="$1" />')

  html = html.replace(/```([\w]*)?\n?([\s\S]*?)```/g, (match, _lang, code) => {
    const cleanCode = String(code).replace(/^\n+|\n+$/g, '')
    return `<pre><code>${cleanCode}</code></pre>`
  })
  html = html.replace(/`([^`\n]+)`/g, '<code>$1</code>')

  html = html.replace(/^> (.*$)/gim, '<blockquote>$1</blockquote>')

  html = html.replace(/^\* (.*$)/gim, '<li>$1</li>')
  html = html.replace(/^- (.*$)/gim, '<li>$1</li>')
  html = html.replace(/^\d+\. (.*$)/gim, '<li>$1</li>')

  html = html.replace(/((<li>.*<\/li>\s*)+)/g, '<ul>$1</ul>')

  html = html.replace(/\n\n/g, '</p><p>')
  html = '<p>' + html + '</p>'

  html = html.replace(/<p><\/p>/g, '')
  html = html.replace(/<p>\s*<\/p>/g, '')
  html = html.replace(/<p>(<pre><code>[\s\S]*?<\/code><\/pre>)<\/p>/g, '$1')
  html = html.replace(/<p>(<h[1-6]>[\s\S]*?<\/h[1-6]>)<\/p>/g, '$1')
  html = html.replace(/<p>(<ul>[\s\S]*?<\/ul>)<\/p>/g, '$1')
  html = html.replace(/<p>(<blockquote>[\s\S]*?<\/blockquote>)<\/p>/g, '$1')

  return html
}

