/**
 * 日期工具函数
 * 处理后端返回的高精度时间格式，确保前端能正确解析和显示
 */

/**
 * 格式化日期字符串，处理高精度时间格式
 * @param {string} dateString - 日期字符串
 * @returns {string} 格式化后的日期字符串
 */
export const formatDateForDisplay = (dateString) => {
  if (!dateString) return '-'
  
  // 处理后端返回的高精度时间格式（如：2025-08-21T10:45:24.9282708）
  // 截取到毫秒级别，确保JavaScript Date能正确解析
  const normalizedDate = dateString.replace(/(\.(\d{3}))\d*/, '$1')
  
  try {
    const date = new Date(normalizedDate)
    return date.toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch (error) {
    console.error('日期解析失败:', error, '原始日期:', dateString)
    return '-'
  }
}

/**
 * 格式化日期用于表单输入（datetime-local格式）
 * @param {string} dateString - 日期字符串
 * @returns {string} datetime-local格式的日期字符串
 */
export const formatDateForInput = (dateString) => {
  if (!dateString) return new Date().toISOString().slice(0, 16)
  
  // 处理后端返回的高精度时间格式（如：2025-08-21T10:45:24.9282708）
  // 截取到毫秒级别，确保JavaScript Date能正确解析
  const normalizedDate = dateString.replace(/(\.(\d{3}))\d*/, '$1')
  
  try {
    return new Date(normalizedDate).toISOString().slice(0, 16)
  } catch (error) {
    console.error('日期解析失败:', error, '原始日期:', dateString)
    return new Date().toISOString().slice(0, 16)
  }
}

/**
 * 将datetime-local格式转换为ISO格式用于提交
 * @param {string} datetimeLocal - datetime-local格式的日期字符串
 * @returns {string} ISO格式的日期字符串
 */
export const formatDateForSubmit = (datetimeLocal) => {
  if (!datetimeLocal) return new Date().toISOString()
  
  try {
    return new Date(datetimeLocal).toISOString()
  } catch (error) {
    console.error('日期转换失败:', error, '原始日期:', datetimeLocal)
    return new Date().toISOString()
  }
}