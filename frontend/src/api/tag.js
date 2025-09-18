import request from './index'

// 获取标签列表
export const getTags = () => request.get('/tag')

// 获取单个标签
export const getTag = (id) => request.get(`/tag/${id}`)

// 创建标签
export const createTag = (data) => request.post('/tag', data)

// 更新标签
export const updateTag = (id, data) => request.put(`/tag/${id}`, data)

// 删除标签
export const deleteTag = (id) => request.delete(`/tag/${id}`)

// 获取标签统计信息（包含文章数量）
export const getTagStats = () => request.get('/article/tags/stats')
