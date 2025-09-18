import request from './index'

// 获取项目列表（支持分页）
export const getProjects = (page, size) => {
    if (page && size) {
        return request.get(`/project?page=${page}&size=${size}`)
    }
    return request.get('/project/list')
}

// 获取单个项目
export const getProject = (id) => request.get(`/project/${id}`)

// 创建项目
export const createProject = (data) => request.post('/project', data)

// 更新项目
export const updateProject = (id, data) => request.put(`/project/${id}`, data)

// 删除项目
export const deleteProject = (id) => request.delete(`/project/${id}`)

// 点赞项目
export const likeProject = (id) => request.post(`/project/${id}/like`)

// 增加项目浏览量
export const incrementProjectViews = (id) => request.post(`/project/${id}/view`)

// 获取热门项目
export const getPopularProjects = (limit = 5) => request.get(`/project/popular?limit=${limit}`)


// 搜索项目
export const searchProjects = (keyword, page, size) => {
    if (page && size) {
        return request.get(`/project/search?keyword=${keyword}&page=${page}&size=${size}`)
    }
    return request.get(`/project/search?keyword=${keyword}`)
}

// 获取项目标签统计信息（包含项目数量）
export const getProjectTagStats = () => request.get('/project/tags/stats')

// 根据标签ID获取项目列表
export const getProjectsByTag = (tagId, page, size) => request.get(`/project/tag/${tagId}?page=${page}&size=${size}`)