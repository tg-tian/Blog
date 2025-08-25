import request from './index'

// 获取项目列表
export const getProjects = () =>
    request.get('/projects')

// 获取单个项目
export const getProject = (id) =>
    request.get(`/projects/${id}`)

// 创建项目
export const createProject = (data) =>
    request.post('/projects', data)

// 更新项目
export const updateProject = (id, data) =>
    request.put(`/projects/${id}`, data)

// 删除项目
export const deleteProject = (id) =>
    request.delete(`/projects/${id}`)