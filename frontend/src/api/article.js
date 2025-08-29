import request from './index'

// 获取文章列表
export const getArticles = (page, size) =>
    request.get(`/article?page=${page}&size=${size}`)

// 获取单篇文章
export const getArticle = (id) =>
    request.get(`/article/${id}`)

// 创建文章
export const createArticle = (data) =>
    request.post('/article', data)

// 更新文章
export const updateArticle = (id, data) =>
    request.put(`/article/${id}`, data)

// 删除文章
export const deleteArticle = (id) =>
    request.delete(`/article/${id}`)

// 获取分类列表
export const getCategories = () =>
    request.get('/article/categories')

// 获取标签列表
export const getTags = () =>
    request.get('/article/tags')

