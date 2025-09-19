package com.tg.blog.backend.service;

import com.tg.blog.backend.dto.SiteStatsDTO;

import java.util.Map;

/**
 * 网站统计服务接口
 * 提供网站访问量、点赞量等统计功能
 * 
 * @author TG
 * @since 1.0.0
 */
public interface SiteStatsService {

    /**
     * 增加总访问量
     */
    void incrementVisits();

    /**
     * 增加总点赞量
     */
    void incrementLikes();

    /**
     * 增加今日访问量
     */
    void incrementTodayVisits();

    /**
     * 增加今日点赞量
     */
    void incrementTodayLikes();

    /**
     * 获取网站统计信息
     * @return 网站统计数据
     */
    SiteStatsDTO getSiteStats();
    
    /**
     * 获取文章总数
     * @return 文章总数量
     */
    Long getTotalArticles();
    
    /**
     * 获取网站运行天数
     * @return 从建站到现在的天数
     */
    Long getUptime();
}
