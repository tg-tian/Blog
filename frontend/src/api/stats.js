import request from './index'

export const getSiteStats = () => request.get('/site-stats/all')

export const likeSite = () => request.post('/site-stats/likes/increment')

export const visitSite = () => request.post('/site-stats/visits/increment')