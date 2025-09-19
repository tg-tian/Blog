package com.tg.blog.backend.dto;

import lombok.Data;

/**
 * 网站统计数据传输对象
 * 用于传输网站访问量、点赞量等统计信息
 * 
 * @author TG
 * @since 1.0.0
 */
@Data
public class SiteStatsDTO{

    /** 总访问量 */
    private Long totalVisits;
    
    /** 总点赞量 */
    private Long totalLikes;
    
    /** 文章总数 */
    private Long totalArticles;
    
    /** 网站运行天数 */
    private Long uptime;
    
    /** 今日访问量 */
    private Long todayVisits;
    
    /** 今日点赞量 */
    private Long todayLikes;
}
