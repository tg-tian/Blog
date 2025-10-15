// 解析JWT并返回payload对象；失败时返回空对象
export function parseJwt(token) {
  if (!token || typeof token !== 'string') return {}
  try {
    const parts = token.split('.')
    if (parts.length !== 3) return {}
    const base64Url = parts[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const padded = base64 + '='.repeat((4 - (base64.length % 4)) % 4)
    const json = decodeURIComponent(
      atob(padded)
        .split('')
        .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    )
    return JSON.parse(json)
  } catch (e) {
    console.warn('JWT解析失败:', e)
    return {}
  }
}