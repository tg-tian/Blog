import request from './index'

// 获取文章列表
export const getArticles = (page, size) => request.get(`/article?page=${page}&size=${size}`)

// 获取单篇文章
export const getArticle = (id) => request.get(`/article/${id}`)

// 创建文章
export const createArticle = (data) => request.post('/article', data)

// 更新文章
export const updateArticle = (id, data) => request.put(`/article/${id}`, data)

// 删除文章
export const deleteArticle = (id) => request.delete(`/article/${id}`)

// 点赞文章
export const likeArticle = (id) => request.post(`/article/${id}/like`)

// 增加文章浏览量
export const incrementViews = (id) => request.post(`/article/${id}/view`)

// 更新文章评论数
export const updateCommentCount = (id, count) => request.put(`/article/${id}/comments`, { count })
