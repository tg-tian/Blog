/**
 * 图片缓存工具类
 * 用于在前端缓存图片，减少重复请求
 */

// 缓存过期时间（默认1天）
const CACHE_EXPIRATION = 24 * 60 * 60 * 1000;

/**
 * 图片缓存管理类
 */
class ImageCache {
  constructor() {
    this.cacheKey = 'blog_image_cache';
    this.cacheExpirationKey = 'blog_image_cache_expiration';
    this.cache = this.loadCache();
    this.cleanExpiredCache();
  }

  /**
   * 从localStorage加载缓存
   */
  loadCache() {
    try {
      const cache = localStorage.getItem(this.cacheKey);
      return cache ? JSON.parse(cache) : {};
    } catch (error) {
      console.error('加载图片缓存失败:', error);
      return {};
    }
  }

  /**
   * 保存缓存到localStorage
   */
  saveCache() {
    try {
      localStorage.setItem(this.cacheKey, JSON.stringify(this.cache));
      localStorage.setItem(this.cacheExpirationKey, Date.now().toString());
    } catch (error) {
      console.error('保存图片缓存失败:', error);
      // 如果存储空间不足，清除缓存
      if (error instanceof DOMException && error.name === 'QuotaExceededError') {
        this.clearCache();
      }
    }
  }

  /**
   * 清理过期的缓存
   */
  cleanExpiredCache() {
    const lastCacheTime = localStorage.getItem(this.cacheExpirationKey);
    if (lastCacheTime && Date.now() - parseInt(lastCacheTime) > CACHE_EXPIRATION) {
      this.clearCache();
    }
  }

  /**
   * 清除所有缓存
   */
  clearCache() {
    this.cache = {};
    localStorage.removeItem(this.cacheKey);
    localStorage.removeItem(this.cacheExpirationKey);
  }

  /**
   * 获取图片的缓存数据
   * @param {string} url 图片URL
   * @returns {string|null} 图片的Base64数据或null
   */
  getImage(url) {
    return this.cache[url] || null;
  }

  /**
   * 缓存图片
   * @param {string} url 图片URL
   * @param {string} base64Data 图片的Base64数据
   */
  cacheImage(url, base64Data) {
    this.cache[url] = base64Data;
    this.saveCache();
  }

  /**
   * 预加载并缓存图片
   * @param {string} url 图片URL
   * @returns {Promise<string>} 图片的Base64数据
   */
  async preloadImage(url) {
    // 检查缓存中是否已有该图片
    const cachedImage = this.getImage(url);
    if (cachedImage) {
      return cachedImage;
    }

    // 加载图片并转换为Base64
    return new Promise((resolve, reject) => {
      const img = new Image();
      img.crossOrigin = 'Anonymous'; // 处理跨域问题
      img.onload = () => {
        try {
          const canvas = document.createElement('canvas');
          canvas.width = img.width;
          canvas.height = img.height;
          const ctx = canvas.getContext('2d');
          ctx.drawImage(img, 0, 0);
          const base64Data = canvas.toDataURL('image/jpeg');
          this.cacheImage(url, base64Data);
          resolve(base64Data);
        } catch (error) {
          console.error('图片转换失败:', error);
          reject(error);
        }
      };
      img.onerror = (error) => {
        console.error('图片加载失败:', error);
        reject(error);
      };
      img.src = url;
    });
  }
}

// 创建单例实例
const imageCache = new ImageCache();

export default imageCache;