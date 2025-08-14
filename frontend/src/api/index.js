import axios from 'axios'

const instance = axios.create({
    baseURL: '/api',
    timeout: 5000
})

instance.interceptors.response.use(
    res => res.data,
    err => Promise.reject(err)
)

export default instance
