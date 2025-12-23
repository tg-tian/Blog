export const formatDateForDisplay = (dateString: string): string => {
  if (!dateString) return '-'

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

export const formatDateForInput = (dateString?: string): string => {
  if (!dateString) return new Date().toISOString().slice(0, 16)

  const normalizedDate = dateString.replace(/(\.(\d{3}))\d*/, '$1')

  try {
    return new Date(normalizedDate).toISOString().slice(0, 16)
  } catch (error) {
    console.error('日期解析失败:', error, '原始日期:', dateString)
    return new Date().toISOString().slice(0, 16)
  }
}

export const formatDateForSubmit = (datetimeLocal?: string): string => {
  if (!datetimeLocal) return new Date().toISOString()

  try {
    return new Date(datetimeLocal).toISOString()
  } catch (error) {
    console.error('日期转换失败:', error, '原始日期:', datetimeLocal)
    return new Date().toISOString()
  }
}

