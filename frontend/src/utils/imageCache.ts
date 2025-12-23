const CACHE_EXPIRATION = 24 * 60 * 60 * 1000

class ImageCache {
  private cacheKey = 'blog_image_cache'
  private cacheExpirationKey = 'blog_image_cache_expiration'
  private cache: Record<string, string> = {}

  constructor() {
    this.cache = this.loadCache()
    this.cleanExpiredCache()
  }

  private loadCache(): Record<string, string> {
    try {
      const cache = localStorage.getItem(this.cacheKey)
      return cache ? JSON.parse(cache) : {}
    } catch (error) {
      console.error('加载图片缓存失败:', error)
      return {}
    }
  }

  private saveCache() {
    try {
      localStorage.setItem(this.cacheKey, JSON.stringify(this.cache))
      localStorage.setItem(this.cacheExpirationKey, Date.now().toString())
    } catch (error: any) {
      console.error('保存图片缓存失败:', error)
      if (error instanceof DOMException && error.name === 'QuotaExceededError') {
        this.clearCache()
      }
    }
  }

  private cleanExpiredCache() {
    const lastCacheTime = localStorage.getItem(this.cacheExpirationKey)
    if (lastCacheTime && Date.now() - parseInt(lastCacheTime) > CACHE_EXPIRATION) {
      this.clearCache()
    }
  }

  clearCache() {
    this.cache = {}
    localStorage.removeItem(this.cacheKey)
    localStorage.removeItem(this.cacheExpirationKey)
  }

  getImage(url: string): string | null {
    return this.cache[url] || null
  }

  cacheImage(url: string, base64Data: string) {
    this.cache[url] = base64Data
    this.saveCache()
  }

  async preloadImage(url: string): Promise<string> {
    const cachedImage = this.getImage(url)
    if (cachedImage) {
      return cachedImage
    }

    return new Promise((resolve, reject) => {
      const img = new Image()
      img.crossOrigin = 'Anonymous'
      img.onload = () => {
        try {
          const canvas = document.createElement('canvas')
          canvas.width = img.width
          canvas.height = img.height
          const ctx = canvas.getContext('2d')!
          ctx.drawImage(img, 0, 0)
          const base64Data = canvas.toDataURL('image/jpeg')
          resolve(base64Data)
        } catch (error) {
          console.error('图片转换失败:', error)
          reject(error)
        }
      }
      img.onerror = (error) => {
        console.error('图片加载失败:', error)
        reject(error)
      }
      img.src = url
    })
  }
}

const imageCache = new ImageCache()

export default imageCache

