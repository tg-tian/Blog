import request from './index'

// 获取分类列表
export const getCategories = () => request.get('/category')

// 获取单个分类
export const getCategory = (id) => request.get(`/category/${id}`)

// 创建分类
export const createCategory = (data) => request.post('/category', data)

// 更新分类
export const updateCategory = (id, data) => request.put(`/category/${id}`, data)

// 删除分类
export const deleteCategory = (id) => request.delete(`/category/${id}`)
