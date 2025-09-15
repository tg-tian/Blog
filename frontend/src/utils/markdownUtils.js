/**
 * Markdown 工具函数
 * 处理 Markdown 中的图片链接更新和其他相关功能
 */

import { getFileUrl } from './upload'

/**
 * 从 Markdown 文本中提取图片的 objectName
 * @param {string} markdownText - Markdown 文本
 * @returns {Array} objectName 数组
 */
const getObjectNameFromUrl = (url) => {
  try {
    let path = url;
    if (path.startsWith('http')) {
      path = new URL(url).pathname;
    }

    // 移除查询参数
    const queryIndex = path.indexOf('?');
    if (queryIndex !== -1) {
      path = path.substring(0, queryIndex);
    }

    const parts = path.split('/').filter(Boolean);
    // 移除 bucket 名称
    if (parts.length > 1 && parts[0] === (import.meta.env.VITE_MINIO_BUCKET || 'blog')) {
      return parts.slice(1).join('/');
    }
    return parts.join('/');
  } catch (e) {
    console.error('从URL解析objectName失败:', url, e);
    return null;
  }
};

export const extractImageObjectNames = (markdownText) => {
  if (!markdownText) return [];

  const imageRegex = /!\[.*?\]\((.*?)\)/g;
  const objectNames = new Set();
  let match;

  while ((match = imageRegex.exec(markdownText)) !== null) {
    const objectName = getObjectNameFromUrl(match[1]);
    if (objectName) {
      objectNames.add(objectName);
    }
  }
  
  console.log('提取的 objectNames:', [...objectNames]);
  return [...objectNames];
};

/**
 * 更新 Markdown 文本中的图片链接（仅更新URL，不包裹HTML）
 * @param {string} markdownText - 原始 Markdown 文本
 * @param {Object} urlMapping - objectName 到新 URL 的映射 { objectName: newUrl }
 * @returns {string} 更新后的 Markdown 文本
 */
export const updateMarkdownImageUrlsOnly = (markdownText, urlMapping) => {
  if (!markdownText || !urlMapping) return markdownText;

  return markdownText.replace(/!\[(.*?)\]\((.*?)\)/g, (match, alt, oldUrl) => {
    const objectName = getObjectNameFromUrl(oldUrl);
    if (objectName && urlMapping[objectName]) {
      return `![${alt}](${urlMapping[objectName]})`;
    }
    return match;
  });
};

/**
 * 更新 Markdown 文本中的图片链接（包裹HTML标签）
 * @param {string} markdownText - 原始 Markdown 文本
 * @param {Object} urlMapping - objectName 到新 URL 的映射 { objectName: newUrl }
 * @returns {string} 更新后的 Markdown 文本
 */
export const updateMarkdownImageUrls = (markdownText, urlMapping) => {
  if (!markdownText || !urlMapping) return markdownText;

  return markdownText.replace(/!\[(.*?)\]\((.*?)\)/g, (match, alt, oldUrl) => {
    const objectName = getObjectNameFromUrl(oldUrl);
    if (objectName && urlMapping[objectName]) {
      const newUrl = urlMapping[objectName];
      return `<div style="text-align: center; margin: 1rem 0;"><img src="${newUrl}" alt="${alt}" style="max-width: 80%; height: auto; border-radius: 8px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);" /></div>`;
    }
    return match;
  });
};

/**
 * 刷新 Markdown 文本中的所有图片链接（仅更新URL，用于编辑）
 * @param {string} markdownText - 原始 Markdown 文本
 * @returns {Promise<string>} 更新后的 Markdown 文本
 */
export const refreshMarkdownImageUrlsForEdit = async (markdownText) => {
  if (!markdownText) return markdownText
  
  try {
    // 提取所有图片的 objectName
    const objectNames = extractImageObjectNames(markdownText)
    
    if (objectNames.length === 0) {
      return markdownText
    }
    
    // 并发获取所有图片的新 URL
    const urlPromises = objectNames.map(async (objectName) => {
      try {
        const newUrl = await getFileUrl(objectName)
        return { objectName, newUrl }
      } catch (error) {
        console.error(`获取图片 URL 失败: ${objectName}`, error)
        return { objectName, newUrl: null }
      }
    })
    
    const urlResults = await Promise.all(urlPromises)
    
    // 创建 objectName 到新 URL 的映射
    const urlMapping = {}
    urlResults.forEach(({ objectName, newUrl }) => {
      if (newUrl) {
        urlMapping[objectName] = newUrl
      }
    })
    
    // 更新 Markdown 文本中的图片链接（仅更新URL，不包裹HTML）
    return updateMarkdownImageUrlsOnly(markdownText, urlMapping)
    
  } catch (error) {
    console.error('刷新 Markdown 图片链接失败:', error)
    return markdownText
  }
}

/**
 * 刷新 Markdown 文本中的所有图片链接（包裹HTML标签，用于显示）
 * @param {string} markdownText - 原始 Markdown 文本
 * @returns {Promise<string>} 更新后的 Markdown 文本
 */
export const refreshMarkdownImageUrls = async (markdownText) => {
  if (!markdownText) return markdownText
  
  try {
    // 提取所有图片的 objectName
    const objectNames = extractImageObjectNames(markdownText)
    
    if (objectNames.length === 0) {
      return markdownText
    }
    
    // 并发获取所有图片的新 URL
    const urlPromises = objectNames.map(async (objectName) => {
      try {
        const newUrl = await getFileUrl(objectName)
        return { objectName, newUrl }
      } catch (error) {
        console.error(`获取图片 URL 失败: ${objectName}`, error)
        return { objectName, newUrl: null }
      }
    })
    
    const urlResults = await Promise.all(urlPromises)
    
    // 创建 objectName 到新 URL 的映射
    const urlMapping = {}
    urlResults.forEach(({ objectName, newUrl }) => {
      if (newUrl) {
        urlMapping[objectName] = newUrl
      }
    })
    
    // 更新 Markdown 文本中的图片链接（包裹HTML标签）
    return updateMarkdownImageUrls(markdownText, urlMapping)
    
  } catch (error) {
    console.error('刷新 Markdown 图片链接失败:', error)
    return markdownText
  }
}

/**
 * 预处理文章内容，刷新图片链接
 * @param {Object} article - 文章对象
 * @returns {Promise<Object>} 处理后的文章对象
 */
export const preprocessArticleContent = async (article) => {
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

/**
 * 批量预处理文章列表，刷新图片链接
 * @param {Array} articles - 文章数组
 * @returns {Promise<Array>} 处理后的文章数组
 */
export const preprocessArticleList = async (articles) => {
  if (!Array.isArray(articles)) {
    return articles
  }
  
  try {
    const processedArticles = await Promise.all(
      articles.map(article => preprocessArticleContent(article))
    )
    return processedArticles
  } catch (error) {
    console.error('批量预处理文章列表失败:', error)
    return articles
  }
}

/**
 * 简单的 Markdown 渲染函数
 * @param {string} markdownText - Markdown 文本
 * @returns {string} 渲染后的 HTML
 */
export const renderMarkdown = (markdownText) => {
  if (!markdownText) return ''
  
  let html = markdownText
  
  // 转换标题
  html = html.replace(/^### (.*$)/gim, '<h3>$1</h3>')
  html = html.replace(/^## (.*$)/gim, '<h2>$1</h2>')
  html = html.replace(/^# (.*$)/gim, '<h1>$1</h1>')
  
  // 转换粗体和斜体
  html = html.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
  html = html.replace(/\*(.*?)\*/g, '<em>$1</em>')
  
  // 转换链接
  html = html.replace(/\[([^\]]+)\]\(([^\)]+)\)/g, '<a href="$2" target="_blank" rel="noopener noreferrer">$1</a>')
  
  // 转换图片
  html = html.replace(/!\[([^\]]*)\]\(([^\)]+)\)/g, '<img src="$2" alt="$1" />')
  
  // 转换代码块（先处理多行代码块，再处理行内代码）
  html = html.replace(/```([\w]*)?\n?([\s\S]*?)```/g, (match, lang, code) => {
    // 移除首尾的空白行，但保持代码内部的格式
    const cleanCode = code.replace(/^\n+|\n+$/g, '')
    return `<pre><code>${cleanCode}</code></pre>`
  })
  html = html.replace(/`([^`\n]+)`/g, '<code>$1</code>')
  
  // 转换引用
  html = html.replace(/^> (.*$)/gim, '<blockquote>$1</blockquote>')
  
  // 转换列表
  html = html.replace(/^\* (.*$)/gim, '<li>$1</li>')
  html = html.replace(/^- (.*$)/gim, '<li>$1</li>')
  html = html.replace(/^\d+\. (.*$)/gim, '<li>$1</li>')
  
  // 包装连续的li标签为ul
  html = html.replace(/((<li>.*<\/li>\s*)+)/g, '<ul>$1</ul>')
  
  // 转换段落（避免将代码块包装在段落中）
  html = html.replace(/\n\n/g, '</p><p>')
  html = '<p>' + html + '</p>'
  
  // 清理空段落和修复代码块被包装的问题
  html = html.replace(/<p><\/p>/g, '')
  html = html.replace(/<p>\s*<\/p>/g, '')
  html = html.replace(/<p>(<pre><code>[\s\S]*?<\/code><\/pre>)<\/p>/g, '$1')
  html = html.replace(/<p>(<h[1-6]>[\s\S]*?<\/h[1-6]>)<\/p>/g, '$1')
  html = html.replace(/<p>(<ul>[\s\S]*?<\/ul>)<\/p>/g, '$1')
  html = html.replace(/<p>(<blockquote>[\s\S]*?<\/blockquote>)<\/p>/g, '$1')
  
  return html
}