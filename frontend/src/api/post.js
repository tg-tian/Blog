import request from './index'

export const getPosts = (page, size) =>
    request.get(`/posts?page=${page}&size=${size}`)
